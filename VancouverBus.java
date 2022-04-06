import java.util.ArrayList;
import java.util.Scanner;

public class VancouverBus {
	
	class Stop{
		int id, code;
		String name, desc;
		double lon, lat;
		String zone_id;
		int location_type;
		int parent_station;
		
		//TODO getters, setters, to_text
		
		public Stop(String input) {
			Scanner scan = new Scanner(input);
			scan.useDelimiter(",");
			
			id = scan.nextInt();
			code = scan.nextInt();
			name = scan.next();
			desc = scan.next();
			lon = scan.nextDouble();
			lat = scan.nextDouble();
			zone_id = scan.next();
			scan.next();
			location_type = scan.nextInt();
			parent_station = scan.nextInt();
			
			scan.close();
		}
		
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
	}
	
	class Graph{
		public ArrayList<Stop> stops;
		
		public Graph(ArrayList<Stop> verteces) {
			stops = new ArrayList<Stop>();
			for (int i = 0; i < stops.size(); i++) {
				stops.add(verteces.get(i));
			}
		}
		
		public void addStop(Stop vertex){
			stops.add(vertex);
		}
	}

	public static void main(String[] args) {
		
	}

}
