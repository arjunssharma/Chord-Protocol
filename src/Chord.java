import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

class Node {
	int node_id;
	boolean isActive;
	int successor;
	int predecessor;
	int finger_table[];
	public Node(int node_id, int successor, int predecessor, int finger_table[]) {
		this.node_id = node_id;
		this.isActive = true;
		this.successor = successor;
		this.predecessor = predecessor;
		this.finger_table = finger_table;
	}
}

public class Chord {
	
	private final static Logger LOGGER = Logger.getLogger(Chord.class.getName());
	private static BufferedReader br;
	private static Map<Integer, Node> map;
	private static int number_of_nodes;
	private static int finger_table_size;
	
	public static void main(String args[]) throws Exception {
		if(args.length < 2) {
			LOGGER.warning("SYNTAX ERROR: cmd expects " + 2 + " parameters not " + args.length);
			System.exit(1);
		}
		
		try {
			br = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOGGER.warning("ERROR: Invalid Input File Path, Try Again!");
			System.exit(1);
		}
		
		map = new HashMap<>();
		finger_table_size = Integer.valueOf(args[1]);
		number_of_nodes = (int)Math.pow(finger_table_size, 2);
		
		String line;
		while((line = br.readLine()) != null) { //implement normal mode
			if(line.trim().equals("end")) { //no output
				break;
			}
			else if(line.trim().startsWith("add")) {
				int node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				if(node_id < 0) {
					LOGGER.warning("ERROR: invalid integer " + node_id);
					continue;
				}
				else if(node_id > number_of_nodes) {
					LOGGER.warning("ERROR: node id must be in [0," + number_of_nodes + ")");
					continue;
				}
				else if(map.containsKey(node_id)) {
					LOGGER.warning("ERROR: Node " + node_id + " exists");
					continue;
				}
				else {
					int finger_table[] = new int[finger_table_size];
					for (int i = 0; i < finger_table.length; i++)
						finger_table[i] = node_id;

					Node new_node = new Node(node_id, node_id, -1, finger_table);
					map.put(node_id, new_node);
					System.out.println("Added node " + node_id);
					continue;
				}
			}
			else if(line.trim().startsWith("drop")) {
				int node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				if(!map.containsKey(node_id)) {
					LOGGER.warning("ERROR: Node " + node_id + " does not exist");
					continue;
				}
				else {
					
				}
			}
			else if(line.trim().startsWith("join")) { //no output
				int from_node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				int to_node_id = Integer.valueOf(line.trim().split("\\s")[2]);
				if(!map.containsKey(from_node_id) || !map.containsKey(to_node_id)) {
					if(!map.containsKey(from_node_id))
						LOGGER.warning("ERROR: Node " + from_node_id + " does not exist");
					if(!map.containsKey(to_node_id))
						LOGGER.warning("ERROR: Node " + to_node_id + " does not exist");
					continue;
				}
				else {
					//logic
				}
			}
			else if(line.trim().startsWith("fix")) { //no output
				
			}
			else if(line.trim().startsWith("stab")) { //no output
				
			}
			else if(line.trim().equals("list")) {
				if(map.isEmpty()) {
					LOGGER.warning("ERROR: No nodes exists");
					continue;
				}
				else {
					StringBuilder sb = new StringBuilder();
					sb.append("Nodes:");
					for(Integer key : map.keySet())
						sb.append(key + ",");
					sb.deleteCharAt(sb.length() - 1);
					System.out.println(sb.toString());
				}
			}
			else if(line.trim().startsWith("show")) {
				int node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				if(!map.containsKey(node_id)) {
					LOGGER.warning("ERROR: Node " + node_id + " does not exist");
					continue;
				}
				else {
					StringBuilder sb = new StringBuilder();
					Node node = map.get(node_id);
					sb.append("Node " + node.node_id + ": ");
					sb.append("suc " + node.successor + ", ");
					if(node.predecessor != -1)
						sb.append("pre " + node.predecessor + ": ");
					else
						sb.append("pre None: ");
					int finger_table[] = node.finger_table;
					for(int i = 0; i < finger_table.length; i++)
						sb.append(finger_table[i] + ",");
					sb.deleteCharAt(sb.length() - 1);
					System.out.println(sb.toString());
				}
			}
			else {
				LOGGER.warning("ERROR: Invalid Command");
				continue;
			}
		}
		
		br.close();
	}
}
