import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;
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
	
	static int count = 0;
	
	public int find_successor(int id, Map<Integer, Node> map) {
		count++;
		if(count == (int)Math.pow(2, this.finger_table.length))
			return this.node_id;
		
		if(boundary_condition(this.node_id + 1, id, this.successor))
			return this.successor;
		else {
			Node n = this.closest_preceding_node(id, map);
			return n.find_successor(id, map);
		}
	}
	
	public boolean boundary_condition(int left, int value, int right) {
		//if(left == value || right == value)
			//return true;
		if(left <= right && (left <= value && value <= right))
			return true;
		else if(left >= right && !(right <= value && value < left))
			return true;
		else
			return false;
	}
	
	public Node closest_preceding_node(int id, Map<Integer, Node> map) {
		int finger_table[] = this.finger_table;
		for(int i = finger_table.length - 1; i >= 0; i--) {
			if(boundary_condition(this.node_id + 1, finger_table[i], id - 1))
				return map.get(finger_table[i]);
		}
		return map.get(this.node_id);
	}
	
	public void join(Node node1, Map<Integer, Node> map){
		count = 0;
		this.predecessor = -1;
		this.successor = node1.find_successor(this.node_id, map);
		map.get(this.successor).notify(this.node_id, map);
		this.stabilize(map);
		node1.stabilize(map);
	}
	
	public void stabilize(Map<Integer, Node> map) {
		int x = map.get(this.successor).predecessor;
		if(boundary_condition(this.node_id + 1, x, this.successor - 1))
			this.successor = x;
		map.get(this.successor).notify(this.node_id, map);
	}
	
	public void notify(int n1, Map<Integer, Node> map) {
		if(this.predecessor == -1 || boundary_condition(this.predecessor + 1, n1, this.node_id - 1))
			this.predecessor = n1;
	}
	
	public void fix_finger(Map<Integer, Node> map) {
		count = 0;
		for(int i = 0; i < this.finger_table.length; i++) {
			this.finger_table[i] = find_successor((this.node_id + (int)Math.pow(2, i)) % (int)Math.pow(2, this.finger_table.length), map);
			if(count == (int)Math.pow(2, this.finger_table.length))
				return;
		}
	}
}

public class Chord {
	
	private final static Logger LOGGER = Logger.getLogger(Chord.class.getName());
	private static BufferedReader br;
	private static Map<Integer, Node> map;
	private static int number_of_nodes;
	private static int finger_table_size;
	private static BufferedReader br_normal;
	
	public static void main(String args[]) throws Exception {
		if(args.length > 3) {
			System.out.println("SYNTAX ERROR: cmd expects 1 or 3 parameters not " + args.length);
			System.exit(1);
		}
		
		if(args.length == 3 && args[0].equalsIgnoreCase("-i")) {
			try {
				br = new BufferedReader(new FileReader(args[1]));
				finger_table_size = Integer.valueOf(args[2]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("ERROR: Invalid Input File Path, Try Again!");
				System.exit(1);
			}
		}
		else {
			br_normal = new BufferedReader(new InputStreamReader(System.in));
			finger_table_size = Integer.valueOf(args[0]);
		}
		
		map = new TreeMap<>();
		number_of_nodes = (int)Math.pow(finger_table_size, 2);
		
		String line;
		while(br != null ? (line = br.readLine()) != null : (line = br_normal.readLine()) != null) {
			if(line.trim().equals("end")) { //-------------------------END--------------------------
				break;
			}
			else if(line.trim().startsWith("add")) { //----------------ADD--------------------------
				if(line.trim().split("\\s").length != 2) {
					System.out.println("SYNTAX ERROR: cmd expects 2 parameters not " + args.length);
					continue;
				}
				int node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				if(node_id < 0) {
					System.out.println("ERROR: invalid integer " + node_id);
					continue;
				}
				else if(node_id > number_of_nodes) {
					System.out.println("ERROR: node id must be in [0," + number_of_nodes + ")");
					continue;
				}
				else if(map.containsKey(node_id)) {
					System.out.println("ERROR: Node " + node_id + " exists");
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
			else if(line.trim().startsWith("drop")) { //---------------DROP--------------------------
				if(line.trim().split("\\s").length != 2) {
					System.out.println("SYNTAX ERROR: cmd expects 2 parameters not " + args.length);
					continue;
				}
				int node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				if(!map.containsKey(node_id)) {
					System.out.println("ERROR: Node " + node_id + " does not exist");
					continue;
				}
				else {
					Node n = map.get(node_id);
					
				}
			}
			else if(line.trim().startsWith("join")) { //----------------JOIN--------------------------
				if(line.trim().split("\\s").length != 3) {
					System.out.println("SYNTAX ERROR: cmd expects 3 parameters not " + args.length);
					continue;
				}
				int from_node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				int to_node_id = Integer.valueOf(line.trim().split("\\s")[2]);
				if(!map.containsKey(from_node_id) || !map.containsKey(to_node_id)) {
					if(!map.containsKey(from_node_id))
						System.out.println("ERROR: Node " + from_node_id + " does not exist");
					if(!map.containsKey(to_node_id))
						System.out.println("ERROR: Node " + to_node_id + " does not exist");
					continue;
				}
				else {
					try {
						Node node = map.get(from_node_id);
						Node node1 = map.get(to_node_id);
						node.join(node1, map);
					}
					catch(Exception e) {
						continue;
					}
				}
			}
			else if(line.trim().startsWith("fix")) { //----------------FIX--------------------------
				if(line.trim().split("\\s").length != 2) {
					System.out.println("SYNTAX ERROR: cmd expects 2 parameters not " + args.length);
					continue;
				}
				
				int node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				if(!map.containsKey(node_id)) {
					System.out.println("ERROR: Node " + node_id + " does not exist");
					continue;
				}
				else {
					Node node = map.get(node_id);
					node.fix_finger(map);
				}
			}
			else if(line.trim().startsWith("stab")) { //---------------STAB--------------------------
				if(line.trim().split("\\s").length != 2) {
					System.out.println("SYNTAX ERROR: cmd expects 2 parameters not " + args.length);
					continue;
				}
				
				int node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				Node node = map.get(node_id);
				node.stabilize(map);
			}
			else if(line.trim().equals("list")) { //-------------------LIST--------------------------
				if(line.trim().split("\\s").length != 1) {
					System.out.println("SYNTAX ERROR: cmd expects 2 parameters not " + args.length);
					continue;
				}
				else if(map.isEmpty()) {
					System.out.println("ERROR: No nodes exists");
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
			else if(line.trim().startsWith("show")) { //----------------SHOW--------------------------
				if(line.trim().split("\\s").length != 2) {
					System.out.println("SYNTAX ERROR: cmd expects 2 parameters not " + args.length);
					continue;
				}
				int node_id = Integer.valueOf(line.trim().split("\\s")[1]);
				if(!map.containsKey(node_id)) {
					System.out.println("ERROR: Node " + node_id + " does not exist");
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
					sb.append("finger ");
					int finger_table[] = node.finger_table;
					for(int i = 0; i < finger_table.length; i++)
						sb.append(finger_table[i] + ",");
					sb.deleteCharAt(sb.length() - 1);
					System.out.println(sb.toString());
				}
			}
			else {
				System.out.println("ERROR: Invalid Command");
				continue;
			}
		}
		
		if(br != null)
			br.close();
		if(br_normal != null)
			br_normal.close();
	}	
}
