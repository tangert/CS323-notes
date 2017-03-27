package hw06;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.reflect.Array;
import java.util.*;
/**
 * Created by tylerangert on 3/20/17.
 */
public class Graph {

    public static void main(String[] args) {
        System.out.println("Hello HW6");
        Graph g = new Graph();
        g.addEdge("A","B",1);
        g.addEdge("A","C",2);

        g.addEdge("B","C",2);
        g.addEdge("B","D",2);
        g.addEdge("B","E",3);
        g.addEdge("B","H",3);


        g.addEdge("C","F",4);
        g.addEdge("C","G",4);
        g.addEdge("C","H",3);

//        System.out.println("Bellman ford: " + g.bellmanFord("A") + "\n");
//        System.out.println("Dijkstra: " + g.dijkstra("A"));

        System.out.println(g.toString());
        g.source = "A";
        g.sink = "H";
        System.out.println("Max flow: " + g.maxFlow());
        System.out.println(g.toString());
    }

    //these numbers represent the different colors of nodes depending on their state
    //in a search.
    //WHITE is not discovered
    //GRAY is discovered but not explored
    //BLACK is explored
    public static final int WHITE = 0;
    public static final int GRAY = 1;
    public static final int BLACK = 2;
    public Vertex NIL;
    public String source;
    public String sink;

    //This is the map of Vertices in a given graph, given a string (key)
    Map<String, Vertex> vertices;
    //This is the map of Adjacent edges to a given vertex, given a key
    Map<String, ArrayList<Edge>> adjacent;
    //this stores the search steps during BFS or DFS
    List<String> searchSteps;

    // graph constructor which initializes with empty maps
    //vertices: maps vertex names to the appropriate vertex object
    //adjacent: maps a vertex key to a list of edges adjacent to it
    //search steps: the amount of steps taken by the given algorithm to complete
    public Graph() {
        vertices = new TreeMap<String, Vertex>();
        adjacent = new HashMap<String, ArrayList<Edge>>();
        searchSteps = new ArrayList<String>();
    }

    /////NEW METHODS FOR HW 6/////
    public boolean bellmanFord(String source) {
        initializeSingleSource(vertices.get(source));
        Stack<String> s = new Stack<String>();

        for(int i = 1; i < vertices.size()-1; i++) {
            for(Edge e: getAllEdges()) {
                relax(e.source,e.target,e.weight);
            }
        }

        for(Edge e: getAllEdges()) {
            if (e.target.distance > e.source.distance + e.weight) {
                System.out.println("Negative weight cycle found");
                return false;
            }
        }

        //Prints out all of the keys
        for (String key : vertices.keySet()){
            Vertex v = vertices.get(key);
            System.out.println(key + "  " + v.distance);
        }

        return true;
    }

    public boolean dijkstra(String source) {
        initializeSingleSource(vertices.get(source));

        Stack<String> s = new Stack<String>();
        PriorityQueue<Vertex> q = new PriorityQueue<>();

        for(String key: vertices.keySet()) {
            q.add(vertices.get(key));
        }

        while(!q.isEmpty()) {
            Vertex u = q.poll();
            u.color = BLACK;
            s.push(u.label);

            for(Edge e: adjacent.get(u.label)) {
                //checking for negative edges
                if (e.weight < 0) {
                    System.out.println("found a negative edge");
                    System.out.println("Stack: " + s);
                    return false;
                }
                relax(e.source, e.target, e.weight);
            }
        }

        //Prints out all of the distances found for the shortest path
        for (String key : vertices.keySet()){
            Vertex v = vertices.get(key);
            System.out.println(key + "  " + v.distance);
        }

        return true;
    }

    //Helper methods
    public void initializeSingleSource(Vertex s) {
        for(String key: vertices.keySet()) {
            Vertex v = vertices.get(key);
            v.distance = Double.POSITIVE_INFINITY;
            v.parent = NIL;
        }
        s.distance = 0;
    }

    public void relax(Vertex u, Vertex v, Double w) {
        if (v.distance > u.distance + w) {
            v.distance = u.distance + w;
            v.parent = u;
        }
    }

    public double maxFlow(){
        //Ford-Fulkerson
        Graph residual = new Graph();
        double maxFlow = 0;

        //Fill the residual graph
        fillResidualGraph(residual);

        //Assign source and sink from Graph
        String source = this.source;
        String sink = this.sink;

        while(augmentingPathExists(source, sink, residual)) {
            //find path from given residual
            System.out.println(residual.path(source,sink));
            ArrayList<String> path = residual.path(source,sink);
            ArrayList<Edge> residualEdges = new ArrayList<Edge>();

            //populate arraylist of edges from path
            for(int i = 0; i < path.size(); i++) {
                for(Edge e: residual.getAllEdges()) {
                    if(e.source.label == path.get(i) && e.target.label == path.get(i+1)) {
                        residualEdges.add(e);
                    }
                }
            }

            //for every edge in path, find bottleneck
            double bottleNeckCapacity = getBottleNeck(residualEdges);
            double pathFlow = Double.POSITIVE_INFINITY;

            //path flow is min of path flow and bottleneck
            pathFlow = Math.min(pathFlow, bottleNeckCapacity);

            for(Edge e: residualEdges){
                if(getAllEdges().contains(e)) {
                    //for every edge in path, increment flow by bottleneck
                    e.flow += pathFlow;
                } else {
                    //decrement opposite edge by bottlement
                    e.residual.flow -= pathFlow;
                }
            }
            maxFlow+=pathFlow;
        }

        return maxFlow;
    }

    //HELPER METHODS FOR FORD FULKERSON

    //fills the residual graph with references to the base graph, along with adding all the residual edges
    private void fillResidualGraph(Graph residualGraph) {
        residualGraph.adjacent = adjacent;
        residualGraph.vertices = vertices;

        for(Edge e: residualGraph.getAllEdges()) {
            e.residual = new Edge(e.target,e.source,e.weight);
        }
    }

    //returns the bottleneck capacity of a given path.
    private double getBottleNeck(ArrayList<Edge> path) {
        double bottleNeck = Double.POSITIVE_INFINITY;
        for(Edge e: path) {
            if(e.capacity - e.flow < bottleNeck) {
                bottleNeck = e.capacity - e.flow;
            }
        }
        System.out.println("Bottleneck of " + path + ": " + bottleNeck);
        return bottleNeck;
    }

    //checks if an augmenting path exists
    private boolean augmentingPathExists(String s, String t, Graph g) {
        Vertex source = g.vertices.get(s);
        for(String key: g.vertices.keySet()) {
            Vertex v = g.vertices.get(key);
            v.color = WHITE;
            v.distance = Double.POSITIVE_INFINITY;
            v.parent = NIL;
        }

        source.color = GRAY;
        source.distance = 0;
        source.parent = NIL;

        //Complete the BFS
        LinkedList<Vertex> q = new LinkedList<Vertex>();
        q.add(source);

        while(!q.isEmpty()) {
            Vertex last = q.remove();

            for(Edge e: g.adjacent.get(last.label)){
                Vertex target = e.target;
                if(target.color == WHITE && e.flow < e.capacity) {
                    target.color = GRAY;
                    target.parent = last;
                    target.distance = target.parent.distance + e.weight;
                    q.add(target);

                    //reached the sink!
                    if(target.label == t){
                        return true;
                    }
                }
            }
            last.color = BLACK;
        }

        //couldn't find a path
        return false;
    }


    /////NEW METHODS FOR HW 5/////
    public double totalWeight(){
        double sum = 0;
        double offset = 0;

        for(Edge edge: getAllEdges()) {
            Vertex target = edge.target;
            Vertex source = edge.source;

            //if the target has an edge whose source == source vertex,
            //divide weight by 2

            for(Edge targetAdj: adjacent.get(target.label)) {
                if (targetAdj.target.equals(source)) {
                    //creates an offset found by antiparallel edges
                    offset += (edge.weight/2);
                }
            }

            sum+=edge.weight;
        }

        return sum-offset;
    }

    //helper method that returns all edges
    private ArrayList<Edge> getAllEdges() {
        ArrayList<Edge> foundEdges = new ArrayList<Edge>();
        for(String key: vertices.keySet()) {
            ArrayList<Edge> edges = adjacent.get(key);

            for(Edge edge: edges) {
                if (!foundEdges.contains(edge)) {
                    foundEdges.add(edge);
                }
            }
        }
        return foundEdges;
    }

    public Graph prim(String root) {
        //new graph to be returned
        Graph MST = new Graph();

        //root vertex
        Vertex r = vertices.get(root);
        for(String label: vertices.keySet()) {
            Vertex v = vertices.get(label);
            v.parent = NIL;
            v.distance = Double.POSITIVE_INFINITY;
        }

        r.distance = 0;

        //copy over all of the keys
        ArrayList<String>  q = new ArrayList<>();
        for(String key: vertices.keySet()) {
            q.add(key);
        }

        MST.addVertex(root);
        q.remove(root);

        while(!q.isEmpty()) {

            //making an empty edge
            Edge currentEdge = new Edge(NIL, NIL, Double.POSITIVE_INFINITY);

            //goes through the keyset and gets all the adjacent edges to the current key
            //essentially finds the currentEdge we are comparing instead of comparing all the edges one by one
            for(String key: MST.vertices.keySet()) {
                ArrayList<Edge> adj = adjacent.get(key);

                for(int i = 0; i < adj.size(); i++) {
                    Edge compared = adj.get(i);
                    if((currentEdge.weight > compared.weight) && (q.contains(compared.target.label))) {
                        currentEdge = adj.get(i);
                    }
                }
            }

            Vertex u = currentEdge.target;
            q.remove(u.label);
            MST.addEdge(currentEdge.source.label,u.label,currentEdge.weight);

            ArrayList<Edge> adjacentEdges = adjacent.get(u.label);
            for(Edge edge: adjacentEdges) {
                Vertex v = edge.target;

                //double edges
                if (edge.target.equals(currentEdge.source)) {
                    MST.addEdge(edge.source.label,edge.target.label,edge.weight);
                }

                if((q.contains(v)) && (edge.weight < v.distance)) {
                    v.parent = edge.target;
                    v.distance = edge.weight;
                }
            }
        }

        return MST;
    }

    public Graph kruskal(){
        //A = /O
        Graph MST = new Graph();

        for(String key: vertices.keySet()) {
            Vertex v = vertices.get(key);
            makeSet(v);
        }

        //Sorting all of the edges in non-decreasing order
        ArrayList<Edge> sortedEdges = new ArrayList<Edge>(getAllEdges().size());
        ArrayList<Double> weights = new ArrayList<Double>(sortedEdges.size());
        for(Edge edge: getAllEdges()) {
            weights.add(edge.weight);
        }

        Collections.sort(weights);

        //Sort the edges according to the weights
        for(Double weight: weights) {
            for (Edge edge : getAllEdges()) {
                if ((edge.weight == weight) && (!sortedEdges.contains(edge))) {
                    sortedEdges.add(edge);
                }
            }
        }

        for(Edge e: sortedEdges) {
            if(!(find(e.source).equals(find(e.target)))) {

                //adds an antiparallel if it exists
                for(Edge targetAdj: adjacent.get(e.target.label)) {
                    if (targetAdj.target.equals(e.source)) {
                        MST.addEdge(e.target.label, e.source.label, e.weight);
                    }
                }

                MST.addEdge(e.source.label, e.target.label, e.weight);
                union(e.source,e.target);
            }
        }

        return MST;
    }

    public void makeSet(Vertex v) {
        v.parent = v;
    }

    public Vertex find(Vertex v) {
        if(v.parent.equals(v)) {
            return v;
        } else {
            return(find(v.parent));
        }
    }

    public void union(Vertex u, Vertex v) {
        find(u).parent = find(v);
    }

    //adds a new vertex into the graph given a specific key
    //adds a new adjacent edge with the appropriate key
    public void addVertex(String key) {
        Vertex v = new Vertex(key);
        vertices.put(key, v);
        adjacent.put(key, new ArrayList<Edge>());
    }

    //adds a new edge given a source and target key for vertices, along with a weight for the edge
    public void addEdge(String source, String target, double weight) {
        //adds the vertices in the given edge if they don't exist already
        if (!vertices.containsKey(source)) {
            addVertex(source);
        }
        if (!vertices.containsKey(target)) {
            addVertex(target);
        }

        ArrayList<Edge> edges = adjacent.get(source);
        Edge e = new Edge(vertices.get(source), vertices.get(target), weight);
        edges.add(e);
    }

    //converts the current graph to a string
    public String toString() {
        StringBuilder s = new StringBuilder();
        StringBuilder dot = new StringBuilder();

        s.append("Vertices:\n");
        //beginning of dot file
        dot.append("digraph G {\n");
        for (String key : vertices.keySet()) {
            //handling spaces/special characters
            String output;
            if (key.contains(" ")) {
                output = "\"" + key + "\"";
            } else {
                output = key;
            }
            switch (vertices.get(key).color) {
                //creates the DOT variables
                case WHITE:
                    dot.append("    " + output + "[style=\"dotted\"];\n");
                    break;
                case GRAY:
                    dot.append("    " + output + "[fillcolor=\"gray\",style=\"filled\"];\n");
                    break;
                case BLACK:
                    dot.append("    " + output + "[style=\"bold\"];\n");
                    break;
            }
            s.append(vertices.get(key) + "\n");
        }

        s.append("Edges:\n");
        for (String key : vertices.keySet()) {
            ArrayList<Edge> edges = adjacent.get(key);
            for (Edge e : edges) {

                //handling spaces/special characters
                String source;
                if (e.source.label.contains(" ")) {
                    source = "\"" + e.source.label + "\"";
                } else {
                    source = e.source.label;
                }

                String target;
                if (e.target.label.contains(" ")) {
                    target = "\"" + e.target.label + "\"";
                } else {
                    target = e.target.label;
                }

                s.append(e + "\n");
                //creates the DOT relationships
                dot.append("    " + source + " -> " + target + " [label=\"capacity: "+e.weight+", flow: "+e.flow+ "\"];\n");
            }
        }

        //ends the DOT file
        dot.append("}");
        return dot.toString();
    }

    public void breadthFirstSearch(String startVertex) {
        //clearing search steps
        searchSteps.clear();

        //clearing vertex distances
        for (String label : vertices.keySet()) {
            Vertex u = vertices.get(label);
            u.distance = 0;
        }

        if (vertices.get(startVertex) == null) {
            System.out.println("This vertex doesn't exist");
        } else {

            Vertex s = vertices.get(startVertex);
            for (String label : vertices.keySet()) {
                Vertex v = vertices.get(label);
                if (!v.equals(s)) {
                    v.color = WHITE;
                    v.distance = Double.POSITIVE_INFINITY;
                    v.parent = NIL;
                }
            }

            //2. DISCOVER FIRST VERTEX
            s.color = GRAY;
            s.distance = 0;
            s.parent = NIL;

            LinkedList<Vertex> queue = new LinkedList<Vertex>();
            queue.add(s);

            while (!queue.isEmpty()) {
                Vertex u = queue.remove();
                u.discoverStep = searchSteps.size();

                for (Edge e : adjacent.get(u.label)) {
                    Vertex v = e.target;
                    if (v.color == WHITE) {
                        //printing steps
                        v.color = GRAY;
                        v.parent = u;
                        v.distance = v.parent.distance + e.weight;
                        queue.add(v);
                    }
                }

                u.color = BLACK;
                u.finishStep = searchSteps.size();
            }
        }
    }


    //    The distance attribute of each vertex should be updated using the weight of the corresponding edges
// (i.e., the total distance of a path is the sum of the weights of the composing edges).
    public void depthFirstSearch(String startVertex) {
        //remove the vertex weights before each search
        for (String label : vertices.keySet()) {
            Vertex u = vertices.get(label);
            u.distance = 0;
        }

        if (vertices.get(startVertex) == null) {
            System.out.println("This vertex doesn't exist");
        } else {

            Vertex start = vertices.get(startVertex);
            for (String label : vertices.keySet()) {
                Vertex u = vertices.get(label);
                u.color = WHITE;
                u.parent = null;
            }

            searchSteps.clear();
            for (String label : vertices.keySet()) {
                Vertex u = vertices.get(label);
                if (u.color == WHITE) {
                    DFSVisit(start);
                }
            }

            System.out.println(searchSteps.size());
        }
    }

    public void DFSVisit(Vertex u) {
        //this also serves as the initial snapshot to avoid duplicates
        recordSnapshot();
        u.discoverStep = searchSteps.size();
        u.color = GRAY;

        for (Edge e : adjacent.get(u.label)) {
            Vertex v = e.target;
            if (v.color == WHITE) {
                v.parent = u;
                v.distance = v.parent.distance + e.weight;
                System.out.println(v.label + " distance: " + v.distance);
                DFSVisit(v);
            }
        }

        u.color = BLACK;
        u.finishStep = searchSteps.size();
        recordSnapshot();
        System.out.println("Vertex: " + u.label + ". Discover: " + u.discoverStep + "; Finish: " + u.finishStep);
    }

    public void recordSnapshot() {
        searchSteps.add(this.toString());
        System.out.println(this.toString());
    }

    public ArrayList<String> path(String startVertex, String endVertex) {
        //declares an empty linked list to pass into the path helper
        if (vertices.get(startVertex) == null || vertices.get(endVertex) == null) {
            System.out.println("This vertex doesn't exist");
            return null;
        } else {
            System.out.print("Path from " + startVertex + " to " + endVertex + ": ");
            ArrayList<String> path = new ArrayList<String>();
            //recursively creates a path of nodes except for the last one
            ArrayList<String> basePath = pathHelper(path, startVertex, endVertex);
            if (basePath == null) {
                System.out.println("Base path is null");
                return null;
            } else {
                basePath.add(basePath.size(),endVertex);
                return basePath;
            }
        }
    }

    private ArrayList<String> pathHelper(ArrayList<String> path, String start, String end) {
        Vertex s = vertices.get(start);
        Vertex e = vertices.get(end);

        if (s.equals(e)) {
            return path;
        } else if (e.parent == null) {
            System.out.println("No path");
            path.clear();
            return null;
        } else {
            System.out.println("About to add: " + e.parent.label);
            if(path.size()==0) {
                path.add(e.parent.label);
            } else {
                path.add(path.size()-1,e.parent.label);
            }
            pathHelper(path, start, e.parent.label);
        }

        return path;
    }

    public double pathWeight(String startVertex, String endVertex) {
        // You implement this method
        double totalWeight = 0;
        System.out.println("\nPath weight from " + startVertex + " to " + endVertex);
        return pathWeightHelper(totalWeight, startVertex, endVertex);
    }

    //this helper method recursively propogates up the entire graph starting at the end vertex
    //at each step, it checks for adjacent edges to the end vertex's parent,
    //then of all possible adjacent edges, if the target is equal to the end,
    //it records the weight of that edge into total edge.
    //this repeats for each correponding edge until the graph reaches the top.
    public double pathWeightHelper(double totalWeight, String start, String end) {
        Vertex s = vertices.get(start);
        Vertex e = vertices.get(end);

        if (s.equals(e)) {
            return totalWeight;
        } else if (e.parent == null) {
            System.out.println(e.label + "'s parent is null");
            return Double.POSITIVE_INFINITY;
        } else {


            //get relevant edges
            for (Edge edge : adjacent.get(e.parent.label)) {
                //if the edge target equals the end node as inputted
                if (edge.target.equals(e)) {
//                    System.out.println("Edge " + edge.toString());
                    totalWeight += edge.weight;
                }
            }
            return pathWeightHelper(totalWeight, start, e.parent.label);
        }
    }

    //TOPOLOGICAL SORT
    //THIS SORTING METHOD PRODUCES A LIST OF VERTICES S.T. FOR EVERY EDGE
    //CONNECTING U,V, U COMES BEFORE V IN THE SORTED LIST.
    public List<String> topologicalSort() {
        //get first value from the vertices
        Object firstKey = vertices.keySet().toArray()[0];
        //perform DFS on first value in vertices.
        return dfsTopSort((String) firstKey);
    }

    //HELPER METHODS FOR TOPOLOGICAL SORT
    //THIS IS A MODIFIED DFS THAT RECORDS EVERY NODE INTO A LINKEDLIST
    public List<String> dfsTopSort(String startVertex) {
        if (vertices.get(startVertex) == null) {
            System.out.println("This vertex doesn't exist");
        }

        Vertex start = vertices.get(startVertex);
        for (String label : vertices.keySet()) {
            Vertex u = vertices.get(label);
            u.color = WHITE;
            u.parent = null;
        }

        LinkedList<String> list = new LinkedList<String>();
        searchSteps.clear();
        for (String label : vertices.keySet()) {
            Vertex u = vertices.get(label);
            if (u.color == WHITE) {
                LinkedList<String> result = DFSVisitTopSort(u, list);
                list = result;
            }
        }

        return list;
    }

    public LinkedList<String> DFSVisitTopSort(Vertex u, LinkedList<String> list) {
        searchSteps.add(this.toString());
        u.discoverStep = searchSteps.size();
        u.color = GRAY;

        for (Edge e : adjacent.get(u.label)) {
            Vertex v = e.target;
            if (v.color == WHITE) {
                v.parent = u;
                DFSVisitTopSort(v, list);
            }
        }

        searchSteps.add(this.toString());
        u.color = BLACK;
        u.finishStep = searchSteps.size();
        System.out.println("Vertex: " + u.label + ". Discover: " + u.discoverStep + "; Finish: " + u.finishStep);
        list.addFirst(u.label);

        return list;
    }

}

class Vertex implements Comparable<Vertex> {
    public static final int WHITE = 0;
    public static final int GRAY = 1;
    public static final int BLACK = 2;

    //data stored within vertex
    String label;

    //the color of the node denoting its discovery status
    int color;

    //the weight of the given edge a vertex is connected to
    double distance;

    Vertex parent;
    double key;

    //discoverStep: the time stamp/point in graph traversal when a node is discovered (gray)
    //finishStep: when a node is traversed (black)
    int discoverStep;
    int finishStep;

    //empty constructor for NIL vertex
    public Vertex(){}

    public Vertex(String label) {
        this.label = label;
        color = WHITE;
        distance = Double.POSITIVE_INFINITY;
        parent = null;
        discoverStep = 0;
        finishStep = 0;
    }

    public String toString() {
        return label;
    }

    public int compareTo (Vertex v){
        return Integer.compare((int) this.distance, (int) v.distance);
    }

}

class Edge {
    //vertex that the edge emerges from
    Vertex source;

    //vertex that the edge connects to
    Vertex target;

    //weight of the given edge
    double weight;

    //capacity
    double capacity;

    //flow
    double flow = 0;

    //residual edge reference
    Edge residual;

    //empty constructor
    public Edge(){}

    public Edge antiParallel() {
        Edge ap = new Edge(this.target, this.source, this.weight);
        return ap;
    }

    public Edge(Vertex source, Vertex target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.capacity = weight;
    }

    public String toString() {
        return "(" + source + " -> " + target + " : " + weight + ")";
    }

}