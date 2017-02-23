package hw04;

/**
 * Created by tylerangert on 2/19/17.
 */
import java.util.*;

public class Graph {

    public static void main(String[] args) {
        System.out.println("Hello graphs!");
        Graph graph = new Graph();
        graph.addVertex("a");
        graph.addVertex("b");
        graph.addVertex("c");
        graph.addVertex("d");
        graph.addVertex("e");
        graph.addVertex("f");
        graph.addVertex("g");


        graph.addEdge("a", "b", 5.0);
        graph.addEdge("a", "c", 6.0);
        graph.addEdge("b", "d", 7.0);
        graph.addEdge("c", "d", 8.0);
        graph.addEdge("c", "e", 9.0);
        graph.addEdge("c", "f", 10.0);
        graph.addEdge("e", "g", 11.0);

        System.out.println("\n***BREADTH FIRST SEARCH***");
        graph.breadthFirstSearch("a");
        System.out.println("\n***DEPTH FIRST SEARCH***");
        graph.depthFirstSearch("a");

        System.out.println("\n***TOPOLOGICAL SORT***");
        System.out.println(graph.topologicalSort());

        System.out.println("\n***PATH PRINT***");
        System.out.println(graph.path("a", "g"));

        System.out.println("\n***PATH WEIGHT***");
        System.out.println(graph.pathWeight("a", "g"));

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

    //
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
                        v.distance = e.weight;
                        v.parent = u;
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
        if (vertices.get(startVertex) == null) {
            System.out.println("This vertex doesn't exist");
        }

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
                DFSVisit(u);
            }
        }

        System.out.println(searchSteps.size());

    }

    public void DFSVisit(Vertex u) {
        //this also serves as the initial snapshot to avoid duplicates
        recordSnapshot();
        u.discoverStep = searchSteps.size();
        u.color = GRAY;

        for (Edge e : adjacent.get(u.label)) {
            Vertex v = e.target;
            if (v.color == WHITE) {
                v.distance = e.weight;
                v.parent = u;
                DFSVisit(v);
            }
        }

        recordSnapshot();
        u.color = BLACK;
        u.finishStep = searchSteps.size();
        System.out.println("Vertex: " + u.label + ". Discover: " + u.discoverStep + "; Finish: " + u.finishStep);
    }

    public void recordSnapshot() {
        searchSteps.add(this.toString());
        System.out.println(this.toString());
    }

    public List<String> path(String startVertex, String endVertex) {
        //declares an empty linked list to pass into the path helper
        LinkedList<String> path = new LinkedList<String>();

        //recursively creates a path of nodes except for the last one
        LinkedList<String> basePath = pathHelper(path, startVertex, endVertex);
        //adds the end vertex
        basePath.add(endVertex);
        return basePath;
    }

    private LinkedList<String> pathHelper(LinkedList<String> path, String start, String end) {
        Vertex s = vertices.get(start);
        Vertex e = vertices.get(end);

        if (s.equals(e)) {
            return path;
        } else if (e.parent == NIL) {
            System.out.println("No path");
            return path;
        } else {
            path.addFirst(e.parent.label);
            pathHelper(path, start, e.parent.label);
        }

        return path;
    }

    public double pathWeight(String startVertex, String endVertex) {
        // You implement this method
        double totalWeight = 0;
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
        } else if (e.parent == NIL) {
            return Double.POSITIVE_INFINITY;
        } else {

            System.out.println("\nStart: " + s.label);
            System.out.println("End: " + e.label);

            //get relevant edges
            for (Edge edge : adjacent.get(e.parent.label)) {
                if (edge.target.equals(e)) {
                    System.out.println("Found the right edge: " + edge.toString());
                    totalWeight += edge.weight;
                }
            }
            return pathWeightHelper(totalWeight, start, e.parent.label);
        }
    }

    public List<String> topologicalSort() {
        //get first value from the vertices
        Object firstKey = vertices.keySet().toArray()[0];
        //perform DFS on first value in vertices.
        return dfsTopSort((String) firstKey);
    }

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
                v.distance = e.weight;
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

    String label;

    int color;

    double distance;

    Vertex parent;

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
    Vertex source;
    Vertex target;
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

