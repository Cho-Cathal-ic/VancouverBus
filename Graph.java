import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

class Stop{
		int id, code;
		String name, desc;
		double lon, lat;
		String zone_id;
		int location_type;
		int parent_station;
		ArrayList<StopTime> destinations = new ArrayList<StopTime>();
		ArrayList<Transfer> transfers = new ArrayList<Transfer>();
		int cost = Integer.MAX_VALUE;
		LinkedList<Stop> via = new LinkedList<Stop>();

		public Stop(String input) {
			Scanner scan = new Scanner(input);
			scan.useDelimiter(",");
			
			id = scan.nextInt();
			if(scan.hasNextInt()) {
				code = scan.nextInt();
			}
			else {
				code = -1;
				scan.next();
			}
			name = scan.next();
			desc = scan.next();
			lon = scan.nextDouble();
			lat = scan.nextDouble();
			zone_id = scan.next();
			scan.next();
			location_type = scan.nextInt();
			
			if (scan.hasNextInt()){
				parent_station = scan.nextInt();
			}
			scan.close();
			
		}
		
		
		public void addDestination(StopTime st) {
			destinations.add(st);
		}
		
		public void addTransfer(Transfer trans) {
			transfers.add(trans);
		}
		
		public void addStopVia(Stop stop) {
			via.add(stop);
		}
		
		public void setStopVia(LinkedList<Stop> stops) {
			via = stops;
		}
		
		//connections
		
		public String toText() {
			return "" + id + ", " + code + ", " + name + ", " + desc + ", " + lon + ", " + lat + ", " + 
					zone_id + ", " + location_type + ", " + parent_station;
		}
		
		public int getId() {
			return id;
		}
		
		public int getcode() {
			return code;
		}
		
		public String getName() {
			return name;
		}
		
		public String getDesc() {
			return desc;
		}
		
		public double getlongidute() {
			return lon;
		}
		
		public double getlatidute() {
			return lat;
		}
		
		public int getLocationType() {
			return location_type;
		}
		
		public int getParentStation() {
			return parent_station;
		}
		
		public void setCost(int c) {
			cost = c;
		}
		
		public int getCost() {
			return cost;
		}
		
		public static Stop getLowestCostStop(Set <Stop> unsettledStops) {
			Stop lowestCostStop = null;
			int lowestCost = Integer.MAX_VALUE;
			for (Stop stop: unsettledStops) {
				int stopCost = stop.getCost();
				if(stopCost < lowestCost) {
					lowestCost = stopCost;
					lowestCostStop = stop;
					
				}
				
			}
			
			return lowestCostStop;
		}
	}

	class StopTime{
		//trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,
		//pickup_type,drop_off_type,shape_dist_traveled
		int trip_id;
		int arrival_time, departure_time;
		int stop_id;
		int stop_headsign;
		int pickup_type;
		int drop_off_type;
		double shape_dist_traveled;
		
		public StopTime(String input){
			Scanner scan = new Scanner(input);
			scan.useDelimiter(",");
			
			trip_id = scan.nextInt();
			arrival_time = TimetoSeconds(scan.next());
			departure_time = TimetoSeconds(scan.next());
			stop_id = scan.nextInt();
			stop_headsign = scan.nextInt();
			
			if(scan.hasNextInt()) {pickup_type = scan.nextInt();}
			else {
				pickup_type = -1;
				scan.next();
			}
			drop_off_type = scan.nextInt();
			
			scan.close();
		}
		
		public String toText(){
			String text = "";
			
			text += ("" + trip_id + ", " + arrival_time + ", " + departure_time + ", " + stop_id + ", ," +
					stop_headsign + ", " + pickup_type + ", " + drop_off_type + ", " + shape_dist_traveled);
			
			return text;
		}
		
		public int TimetoSeconds(String input) {
			
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
	
	class Transfer{
		int from;
		int to;
		int type;
		int minTransferTime;
		
		Transfer(String input){
			Scanner scan = new Scanner(input);
			scan.useDelimiter(":");
			if(scan.hasNextInt()) {from = scan.nextInt();} 
			else {scan.next();}
			if(scan.hasNextInt()) {to = scan.nextInt();} 
			else if(scan.hasNext()){scan.next();}
			if(scan.hasNextInt()) {type = scan.nextInt();} 
			else if(scan.hasNext()){scan.next();}
			if(scan.hasNext()) {minTransferTime = scan.nextInt();}
			else {
				minTransferTime = 0;
			}
			scan.close();
		}
	}
	
	class Graph{
		public ArrayList<Stop> stops;
		public ArrayList<StopTime> trips;
		
		public Graph(ArrayList<Stop> verteces) {
			stops = new ArrayList<Stop>();
			for (int i = 0; i < stops.size(); i++) {
				stops.add(verteces.get(i));
			}
		}
		
		public void addStop(Stop vertex){
			stops.add(vertex);
		}
		
		public void addTrip(StopTime trip) {
			trips.add(trip);
		}
		
		public ArrayList<Stop> getStops() {
			ArrayList<Stop> s = new ArrayList<Stop>();
			
			for(int i=0; i<stops.size(); i++) {
				s.add(stops.get(i));
			}
			
			return s;
		}
		
		public Stop findStop(int id) {
			Stop s = null;
			
			for(int i=0; i<stops.size(); i++) {
				s = stops.get(i);
				if(s.getId() == id) {
					return s;
				}
			}
			
			return null;
		}
		public Stop findStopFromName(String name) {
			Stop s = null;
			
			for(int i=0; i<stops.size(); i++) {
				s = stops.get(i);
				if(s.getName() == name) {
					return s;
				}
			}
			
			return null;
		}
	}