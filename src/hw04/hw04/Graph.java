import java.util.*;

public class Graph {

	public static void main(String[] args) {

	}
	
	Map<String, Vertex> vertices;
	Map<String, ArrayList<Edge>> adjacent;
	List<String> searchSteps;
	
	public Graph() {
		vertices = new TreeMap<String, Vertex>();
		adjacent = new HashMap<String, ArrayList<Edge>>();
		searchSteps = new ArrayList<String>();
	}

	public void addVertex(String key) {
		Vertex v = new Vertex(key);
		vertices.put(key, v);
		adjacent.put(key, new ArrayList<Edge>());
	}
	
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
		// You modify this method
		StringBuilder s = new StringBuilder();
		s.append("Vertices:\n");
		for (String key : vertices.keySet()) {
			s.append(vertices.get(key) + "\n");
		}
		s.append("Edges:\n");
		for (String key : vertices.keySet()) {
			ArrayList<Edge> edges = adjacent.get(key);
			for (Edge e : edges) {
				s.append(e + "\n");
			}
		}
		return s.toString();
	}
	
	public void breadthFirstSearch(String startVertex) {
		// You implement this method
	}
	
	public void depthFirstSearch() {
		// You implement this method
	}
	
	public List<String> path(String startVertex, String endVertex) {
		// You implement this method
		return null;
	}
	
	public double pathWeight(String startVertex, String endVertex) {
		// You implement this method
		return 0;
	}
	
	public List<String> topologicalSort() {
		// You implement this method
		return null;
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

	public Edge(Vertex source, Vertex target, double weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public String toString() {
		return "(" + source + " -> " + target + " : " + weight + ")";
	}
	
}
