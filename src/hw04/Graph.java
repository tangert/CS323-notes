package hw04;

/**
 * Created by tylerangert on 2/19/17.
 */
import java.util.*;

public class Graph {

    public static void main(String[] args) {
        System.out.println("Hello graphs!");

        //Test graph 1
        Graph graph = new Graph();
        graph.addVertex("a");
        graph.addVertex("b");
        graph.addVertex("c");
        graph.addVertex("d");
        graph.addVertex("e");
        graph.addVertex("f");
        graph.addVertex("g");
        graph.addVertex("h");
        graph.addVertex("i");
        graph.addVertex("j");
        graph.addVertex("k");


        graph.addEdge("a", "b", 1.0);
        graph.addEdge("a", "c", 2.0);
        graph.addEdge("b", "d", 3.0);
        graph.addEdge("c", "d", 4.0);
        graph.addEdge("c", "e", 5.0);
        graph.addEdge("c", "f", 6.0);
        graph.addEdge("e", "g", 7.0);
        graph.addEdge("e", "h", 8.0);
        graph.addEdge("g", "h", 9.0);
        graph.addEdge("g", "j", 10.0);
        graph.addEdge("h", "i", 11.0);
        graph.addEdge("i", "j", 12.0);
        graph.addEdge("i", "k", 13.0);


        System.out.println("\n***BREADTH FIRST SEARCH***");
        graph.breadthFirstSearch("a");
        System.out.println("\n***DEPTH FIRST SEARCH***");
        graph.depthFirstSearch("a");

        System.out.println("\n***TOPOLOGICAL SORT***");
        System.out.println(graph.topologicalSort());

        System.out.println("\n***PATH PRINT***");
        System.out.println("Prints the vertices of the path up until the final vertex");
        System.out.println(graph.path("a", "g"));
        System.out.println(graph.path("a", "f"));
        System.out.println(graph.path("a", "e"));
        System.out.println(graph.path("a", "j"));
        System.out.println(graph.path("a", "k"));

        System.out.println(graph.path("b", "d"));
        System.out.println(graph.path("c", "g"));
        System.out.println(graph.path("c", "j"));
        System.out.println(graph.path("e", "k"));



        //this doesn't exist
        System.out.println("\nTHESE PATHS DON'T EXIST");
        System.out.println(graph.path("b", "e"));
        System.out.println(graph.path("b", "f"));
        System.out.println(graph.path("f", "g"));
        System.out.println(graph.path("f", "j"));


        System.out.println("\n***PATH WEIGHT***");
        System.out.println(graph.pathWeight("a", "g"));
        System.out.println(graph.pathWeight("a", "f"));
        System.out.println(graph.pathWeight("a", "e"));
        System.out.println(graph.pathWeight("a", "j"));
        System.out.println(graph.pathWeight("a", "k"));
        System.out.println(graph.pathWeight("b", "d"));
        System.out.println(graph.pathWeight("c", "g"));
        System.out.println(graph.pathWeight("c", "j"));
        System.out.println(graph.pathWeight("e", "k"));


        //FIGURE 22.6 GRAPH
        Graph textbookGraph = new Graph();
        textbookGraph.addVertex("q");
        textbookGraph.addVertex("r");
        textbookGraph.addVertex("s");
        textbookGraph.addVertex("t");
        textbookGraph.addVertex("u");
        textbookGraph.addVertex("v");
        textbookGraph.addVertex("w");
        textbookGraph.addVertex("x");
        textbookGraph.addVertex("y");
        textbookGraph.addVertex("z");

        textbookGraph.addEdge("q","s",5);
        textbookGraph.addEdge("q","w",5);
        textbookGraph.addEdge("q","t",5);

        textbookGraph.addEdge("r","u",5);
        textbookGraph.addEdge("r","y",5);

        textbookGraph.addEdge("s","v",5);

        textbookGraph.addEdge("t","y",5);
        textbookGraph.addEdge("t","x",5);

        textbookGraph.addEdge("u","y",5);

        textbookGraph.addEdge("v","w",5);

        textbookGraph.addEdge("w","s",5);

        textbookGraph.addEdge("x","z",5);

        textbookGraph.addEdge("y","q",5);

        textbookGraph.addEdge("z","x",5);

//        System.out.println("\n***BREADTH FIRST SEARCH***");
//        textbookGraph.breadthFirstSearch("t");
//        System.out.println("\n***DEPTH FIRST SEARCH***");
//        textbookGraph.depthFirstSearch("t");
//        System.out.println("\n***ENDED DFS***");

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

    //adds a new vertex into the graph given a specific key
    //adds a new adjacent edge with the appropriate key
    public void addVertex(String key) {
        Vertex v = new Vertex(key);
        vertices.put(key, v);
        adjacent.put(key, new ArrayList<Edge>());
    }

    //adds a new edge given a source and target key for vertices, along with a weight for the edge
    public void addEdge(String source, String target, double weight) {
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
        recordSnapshot();

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
                recordSnapshot();
                for (Edge e : adjacent.get(u.label)) {
                    Vertex v = e.target;
                    if (v.color == WHITE) {
                        //printing steps
                        v.color = GRAY;
                        v.parent = u;
                        v.distance = v.parent.distance + e.weight;
                        System.out.println(v.label + " distance: " + v.distance);
                        //4. TURN VERTEX GRAY
                        recordSnapshot();
                        queue.add(v);
                    }
                }

                u.color = BLACK;
                u.finishStep = searchSteps.size();

                System.out.println("Vertex: " + u.label + ". Discover: " + u.discoverStep + "; Finish: " + u.finishStep);
            }
            //FINAL SNAPSHOT
            recordSnapshot();
        }
        System.out.println(searchSteps.size());

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

