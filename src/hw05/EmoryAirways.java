package hw05;

/**
 * Created by tylerangert on 3/19/17.
 */
public class EmoryAirways {

    public static void main(String[] args) {
        System.out.println("hello airways!");

        //airports
        Graph airports = new Graph();

        //Atlanta
        //ATL
        airports.addEdge("ATL","SFO",2134.13);
        airports.addEdge("ATL","ASQ",1851.89);
        airports.addEdge("ATL","SEA",2177.79);
        airports.addEdge("ATL","JFK",759.34);

        //San Francisco
        //SFO
        airports.addEdge("SFO","ASQ",307.62);
        airports.addEdge("SFO","SEA",679.16);
        airports.addEdge("SFO","JFK",2579.68);
        //first antiparallel
        airports.addEdge("SFO","ATL",2134.13);


        //Austin
        //ASQ
        airports.addEdge("ASQ","SEA",607.79);
        airports.addEdge("ASQ","JFK",2273.88);
        //second, third antiparallel
        airports.addEdge("ASQ","ATL",1851.89);
        airports.addEdge("ASQ","SFO",307.62);

        //Seattle
        //SEA
        airports.addEdge("SEA","JFK",2414.93);
        //fourth,fifth,sixth antiparallel
        airports.addEdge("SEA","ATL",2177.79);
        airports.addEdge("SEA","SFO",679.16);
        airports.addEdge("SEA","ASQ",607.79);


        //New York
        //JFK
        //all antiparallels
        airports.addEdge("JFK","ATL",759.3);
        airports.addEdge("JFK","SFO",2579.68);
        airports.addEdge("JFK","ASQ",2273.88);
        airports.addEdge("JFK","SEA",2414.93);

        airports.breadthFirstSearch("ATL");
        System.out.println("Initial graph: " + airports.toString());

        //Starting from ATL
        Graph minTrip = airports.prim("ATL");
        minTrip.breadthFirstSearch("ATL");
        System.out.println(minTrip.toString());
        System.out.println("Total distance of all city connections: " + airports.totalWeight());
        System.out.println("Minimum distance to visit all cities: " + minTrip.totalWeight());


    }
}
