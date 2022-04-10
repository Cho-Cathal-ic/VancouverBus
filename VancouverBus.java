import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class VancouverBus {


	public static void main(String[] args) throws FileNotFoundException {
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
		}
		
		for (int i = 0; i < stopTime.size(); i++) {
			for (int j = i+1; j < stopTime.size(); j++) {
				StopTime stopI = stopTime.get(i);
				StopTime stopJ = stopTime.get(j);
				
				if(stopI.trip_id == stopJ.trip_id) {
					if(stopI.arrival_time < stopJ.arrival_time) {
						Stop s = graph.findStop(stopI.stop_id);
						s.addDestination(stopJ);
					}
					else {
						Stop s = graph.findStop(stopJ.stop_id);
						s.addDestination(stopI);
					}
				}
			}
		}
		
		journeys.close();
		
		
		
	}

}
