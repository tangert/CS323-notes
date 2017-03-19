package hw05;

import com.sun.xml.internal.xsom.XSWildcard;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import sun.awt.image.ImageWatched;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by tylerangert on 3/16/17.
 */
public class Graph {

    public static void main(String[] args) {
        //Directed
        Graph directed = new Graph();
        directed.addEdge("A","B",3);
        directed.addEdge("B","A",3);

        directed.addEdge("A","C",3);
        directed.addEdge("C","A",3);


        directed.addEdge("B","C",3);
        directed.addEdge("B","D",1);

        directed.addEdge("C","D",3);
        directed.addEdge("C","E",4);

        directed.addEdge("D","E",1);

        directed.addEdge("E","F",2);
        directed.addEdge("E","G",1);
        directed.addEdge("E","H",5);

        directed.addEdge("F","G",2);
        directed.addEdge("F","H",4);

        directed.addEdge("G","H",1);
        directed.addEdge("G","I",2);

        directed.addEdge("I","J",1);


        //Undirected
        Graph undirected = new Graph();
        undirected.addEdge("0", "1", 5);
        undirected.addEdge("1", "0", 5);

        undirected.addEdge("0", "3", 2);
        undirected.addEdge("3", "0", 2);

        undirected.addEdge("1", "2", 4);
        undirected.addEdge("2", "1", 4);

        undirected.addEdge("1", "3", 3);
        undirected.addEdge("3", "1", 3);

        undirected.addEdge("2", "3", 7);
        undirected.addEdge("3", "2", 7);

        undirected.addEdge("2", "4", 10);
        undirected.addEdge("4", "2", 10);

        undirected.addEdge("1", "4", 8);
        undirected.addEdge("4", "1", 8);

        undirected.addEdge("4", "5", 15.5);
        undirected.addEdge("5", "4", 15.5);

        Graph undir_kruskal = undirected.kruskal();
        Graph undir_prim = undirected.prim("0");

        undirected.breadthFirstSearch("0");

        System.out.println("*****UNDIRECTED GRAPH TESTING****");
        System.out.println("\nOriginal graph: \n" + undirected.toString());
        System.out.println("\nKruskal MST: \n" + undir_kruskal.toString());
        System.out.println("\nPrim MST: \n" + undir_prim.toString());

        System.out.println("Total weight of undirected graph: " + undirected.totalWeight());
        System.out.println("Prim Total weight: " + undir_prim.totalWeight());
        System.out.println("Kruskal Total weight: " + undir_kruskal.totalWeight());

        System.out.println("\n*****DIRECTED GRAPH TESTING****");
        Graph Prim_MST = directed.prim("A");
        Graph Kruskal_MST = directed.kruskal();

        System.out.println("\nOriginal graph: \n" + directed.toString());
        System.out.println("\nKruskal MST: \n" + Kruskal_MST.toString());
        System.out.println("\nPrim MST: \n" + Prim_MST.toString());

        System.out.println("Total weight of Original Graph: " + directed.totalWeight());
        System.out.println("Total weight of Kruskal MST: " + Kruskal_MST.totalWeight());
        System.out.println("Total weight of Prim MST: " + Prim_MST.totalWeight());
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

    public ArrayList<Edge> getAllEdges() {
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

        for(Edge edge: sortedEdges) {
            if(!(find(edge.source).equals(find(edge.target)))) {

                //adds an antiparallel if it exists
                for(Edge targetAdj: adjacent.get(edge.target.label)) {
                    if (targetAdj.target.equals(edge.source)) {
//                        System.out.println("Edge: " + edge + " ::: Antiparallel: " + targetAdj);
                        MST.addEdge(edge.target.label, edge.source.label, edge.weight);
                    }
                }

                MST.addEdge(edge.source.label, edge.target.label, edge.weight);
                union(edge.source,edge.target);
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
                dot.append("    " + source + " -> " + target + ";\n");
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

        //1. INITIAL PRINTING OF GRAPH
//        recordSnapshot();

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
                //3. POP OFF A VERTEX
//                recordSnapshot();
                for (Edge e : adjacent.get(u.label)) {
                    Vertex v = e.target;
                    if (v.color == WHITE) {
                        //printing steps
                        v.color = GRAY;
                        v.parent = u;
                        v.distance = v.parent.distance + e.weight;
//                        System.out.println(v.label + " distance: " + v.distance);
                        //4. TURN VERTEX GRAY
//                        recordSnapshot();
                        queue.add(v);
                    }
                }

                u.color = BLACK;
                u.finishStep = searchSteps.size();

//                System.out.println("Vertex: " + u.label + ". Discover: " + u.discoverStep + "; Finish: " + u.finishStep);
            }
            //FINAL SNAPSHOT
//            recordSnapshot();
        }
//        System.out.println(searchSteps.size());
//
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

    public List<String> path(String startVertex, String endVertex) {
        //declares an empty linked list to pass into the path helper
        if (vertices.get(startVertex) == null || vertices.get(endVertex) == null) {
            System.out.println("This vertex doesn't exist");
            return null;
        } else {
            System.out.print("Path from " + startVertex + " to " + endVertex + ": ");
            LinkedList<String> path = new LinkedList<String>();
            //recursively creates a path of nodes except for the last one
            LinkedList<String> basePath = pathHelper(path, startVertex, endVertex);
            if (basePath == null) {
                System.out.println("Base path is null");
                return null;
            } else {
                return basePath;
            }
        }
    }

    private LinkedList<String> pathHelper(LinkedList<String> path, String start, String end) {
        Vertex s = vertices.get(start);
        Vertex e = vertices.get(end);

        if (s.equals(e)) {
            return path;
        } else if (e.parent == null) {
            System.out.println("No path");
            path.clear();
            return null;
        } else {
            path.addFirst(e.parent.label);
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
                    System.out.println("Edge " + edge.toString());
                    totalWeight += edge.weight;
                }
            }
            return pathWeightHelper(totalWeight, start, e.parent.label);
        }
    }

    //TOPOLOGICAL SORT
    //THIS SORTING METHOD PRODUCES A LIST OF VERTECES S.T. FOR EVERY EDGE
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

class Vertex {
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

}

class Edge {
    //vertex that the edge emerges from
    Vertex source;

    //vertex that the edge connects to
    Vertex target;

    //weight of the given edge
    double weight;

    //empty constructor
    public Edge(){}

    public Edge(Vertex source, Vertex target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public String toString() {
        return "(" + source + " -> " + target + " : " + weight + ")";
    }

}