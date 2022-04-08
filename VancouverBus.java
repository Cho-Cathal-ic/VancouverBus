import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class VancouverBus {


	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<Stop> g = new ArrayList<Stop>();
		
		Graph graph = new Graph(g);
		
		File f = new File("stops.txt");
		Scanner scan = new Scanner(f);
		
		while(scan.hasNextLine()){
			Stop s = new Stop(scan.nextLine());
			graph.addStop(s);
		}
		scan.close();
		
		
	}

}
