import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class VancouverBus {


	public static void main(String[] args) throws FileNotFoundException {
		//construct graph

		ArrayList<Stop> g = new ArrayList<Stop>();

		Graph graph = new Graph(g);

		File f = new File("src\\stops.txt");
		Scanner scan = new Scanner(f);

		scan.nextLine();

		while(scan.hasNextLine()){
			Stop s = new Stop(scan.nextLine());
			graph.addStop(s);
		}
		scan.close();

		File h = new File("src\\stop_times.txt");
		Scanner journeys = new Scanner(h);

		journeys.nextLine();

		ArrayList <StopTime> stopTime = new ArrayList<StopTime>();

		while(journeys.hasNextLine()) {
			StopTime j = new StopTime(journeys.nextLine());
			stopTime.add(j);
			graph.addTrip(j);
		}

		for (int i = 0; i < stopTime.size()-1; i++) {
			StopTime stopI = stopTime.get(i);
			StopTime stopJ = stopTime.get(i+1);

			if(stopI.trip_id == stopJ.trip_id) {
				Stop s = graph.findStop(stopI.stop_id);
				s.addDestination(stopJ);
			}
		}

		journeys.close();

		File l = new File("src\\transfers.txt");
		Scanner transferScanner = new Scanner(l);

		transferScanner.nextLine();

		while(transferScanner.hasNextLine()) {
			Transfer t = new Transfer(transferScanner.nextLine());
			Stop s = graph.findStop(t.from);
			if(s!=null) {s.addTransfer(t);}
		}

		transferScanner.close();

		//select mode
		boolean quit = false;
		Scanner inputScanner = new Scanner(System.in);
		while(!quit) {
			
			System.out.println("Select Mode: 1 - shortest route, 2 - search bus stop, 3 - search by arrival time, 4 - exit");
			int choice = 0;
			while(!inputScanner.hasNextInt()) {}
			choice = inputScanner.nextInt();
			switch(choice) {
			case 1: ShortestRoute(graph);
				break;
			case 2:	SearchForStops(graph);
				break;
			case 3:	SearchByArrival(graph);
				break;
			case 4: quit = true;
				break;
			default: System.out.println("Please enter 1,2,3 or 4");
				break;
			}
			
		}
		inputScanner.close();

	}

	private static void ShortestRoute(Graph graph){
		Scanner inputScanner = new Scanner(System.in);

		System.out.println("Enter Beginning Stop ID:");
		int start = 0;
		while(!inputScanner.hasNextInt()) {}
		start = inputScanner.nextInt();

		System.out.println("Enter Destination Stop ID:");
		int end = 0;
		while(!inputScanner.hasNextInt()) {}
		end = inputScanner.nextInt();
		
		System.out.println("Processing...");

		LinkedList<Stop> via = new LinkedList<Stop>();

		int cost = 0;

		//dijkstra
		cost = calculateShortestPathFromSource(graph, graph.findStop(start), graph.findStop(end));
		via = graph.findStop(end).via;
		if(via.size() == 0) {
			System.out.println("No Route Found");
			inputScanner.close();
			return;
		}
		
		//reset graph
		for(int i=0; i<graph.stops.size();i++) {
			Stop st = graph.stops.get(i);
			st.setCost(0);
			st.setStopVia(new LinkedList<>());
		}
		
		String result = "" + start + " - ";

		for(int i=0;i<via.size();i++) {
			result+=via.get(i).getId();
			result+= " - ";
		}

		result += end;

		System.out.println(result);
		System.out.println("Cost:" + cost);

		inputScanner.close();
	}

	public static int calculateShortestPathFromSource(Graph graph, Stop source, Stop termunus){
		source.setCost(0);

		Set<Stop> settledStops = new HashSet<>();
		Set<Stop> unsettledStops = new HashSet<>();

		unsettledStops.add(source);

		while (unsettledStops.size() != 0) {
			Stop currentStop = Stop.getLowestCostStop(unsettledStops);
			unsettledStops.remove(currentStop);
			for(int i=0; i<currentStop.destinations.size(); i++) {
				Stop adjacentStop = graph.findStop(currentStop.destinations.get(i).stop_id);
				int edgeWeight = 1;

				if(!settledStops.contains(adjacentStop)) {
					CalculateMinimumCost(adjacentStop, edgeWeight, currentStop);
					unsettledStops.add(adjacentStop);

				}	
			}
			for(int i=0; i<currentStop.transfers.size(); i++) {
				Stop adjacentStop = graph.findStop(currentStop.transfers.get(i).to);
				int edgeWeight;
				if(currentStop.transfers.get(i).type == 2) {
					edgeWeight = currentStop.transfers.get(i).minTransferTime /100;
				}
				else {
					edgeWeight = 2;
				}

				if(!settledStops.contains(adjacentStop)) {
					CalculateMinimumCost(adjacentStop, edgeWeight, currentStop);
					unsettledStops.add(adjacentStop);

				}
				settledStops.add(currentStop);
			}
	
		}
		
		return termunus.cost;
	}

	private static void CalculateMinimumCost(Stop evaluationStop, int edgeWeight, Stop sourceStop) {
		int sourceCost = sourceStop.getCost();
		if (sourceCost + edgeWeight < evaluationStop.getCost()) {
			evaluationStop.setCost(sourceCost + edgeWeight);
			LinkedList<Stop> shortestRoute = new LinkedList<>(sourceStop.via);
			shortestRoute.add(sourceStop);
			evaluationStop.setStopVia(shortestRoute);
		}
	}
	
	private static void SearchForStops(Graph graph) {
		ArrayList<String> stopNames = new ArrayList<>();
		ArrayList<Stop> graphList = new ArrayList<>();
		for(int i=0; i<graphList.size();i++) {
			String g = graphList.get(i).toText();
			//string manipulation
			String g2 = g.substring(0, 2);
			if (g2 == "WB" || g2 == "EB" || g2 == "NB" || g2 == "SB") {
				g = g.substring(2) + g2;
			}
			
			stopNames.add(g);	
		}
		TST tree = new TST(stopNames);
		System.out.println("Enter a stop:");
		Scanner scan = new Scanner(System.in);
		while (!scan.hasNext()) {}
		String key = scan.next();
		ArrayList<String> results = new ArrayList<>();
		results = tree.Search(key);
		for(int i = 0; i < results.size(); i++) {
			System.out.println(results.get(i));
		}
		scan.close();
	}
	
	private static void SearchByArrival(Graph graph) {
		Scanner scan = new Scanner(System.in);
		boolean done = false;
		while(!done) {
			System.out.print("Insert time (HH:MM:SS - 24 hour)");
			String timeDay = scan.next();		
			int time = TimetoSeconds(timeDay);
		
			if (time >= 0 && time < 86400) {
				done = true;
				for(int i = 0; i < graph.trips.size(); i++) {
					if(graph.trips.get(i).arrival_time == time) {
						System.out.println(graph.trips.get(i).toText());
					}
				}
			}
		}
		scan.close();
	}
	
	public static int TimetoSeconds(String input) {
		
		Scanner scan = new Scanner(input);
		scan.useDelimiter(":");
		int hours = 0, minutes = 0, seconds = 0;
		if(scan.hasNextInt()) {hours = scan.nextInt();}
		if(scan.hasNextInt()) {minutes = scan.nextInt();}
		if(scan.hasNextInt()) {seconds = scan.nextInt();}
		scan.close();
		
		return ((hours*3600) + (minutes*60) + seconds);
	}

}
