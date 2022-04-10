import java.util.ArrayList;

public class TST {

	TSTNode root;
	
	public TST(ArrayList<String> names) {
		root = new TSTNode(names.get(0).substring(1), "");
		for(int i = 0; i<names.size(); i++) {
			root.add(names.get(i));
			
		}
		
	}
	
	public ArrayList<String> Search(String key) {
		ArrayList<String> result = new ArrayList<>();
		TSTNode location = root.search(key);
		ArrayList<TSTNode> desc = location.descendants();
		
		for(int i = 0; i < desc.size(); i++) {
			result.add(desc.get(i).substring);
		}
		
		return result;
	}
	
	class TSTNode{
		char character;
		boolean storesKey = false;
		String substring;
		
		TSTNode left, middle, right;
		
		public TSTNode(String key, String loc) {
			this.substring = loc;
			if (key.isEmpty()) {
	            throw new IllegalArgumentException();
	        }
	        character = key.charAt(0);
	        if (key.length() > 1) {
	            // Stores the rest of the key in a middle-link chain
	            middle = new TSTNode(key.substring(1), loc + key.substring(0, 1));
	        } else {
	        	storesKey = true;
	            return;
	        }
		}
		
		public ArrayList<TSTNode> descendants() { 
			ArrayList<TSTNode> descs = new ArrayList<>();
			if(storesKey) {
				descs.add(this);
			}
			
			if(middle !=null) {
				descs.addAll(middle.descsentRecursive());
			}
			
			return descs;
		}
		
		private ArrayList<TSTNode> descsentRecursive() {
			ArrayList<TSTNode> descs = new ArrayList<>();
			if(storesKey) {
				descs.add(this);
			}
			
			if(left != null) {
				descs.addAll(left.descsentRecursive());
			}
			if(middle != null) {
				descs.addAll(middle.descsentRecursive());
			}
			if(right != null) {
				descs.addAll(right.descsentRecursive());
			}
			
			return descs;
		}
		
		public TSTNode add(String key) {
			return add(key, "");
		}
		
		public TSTNode add(String key, String loc) {
			char c = key.charAt(0);
			if (character == c) {                             
				if (key.length() == 1) {                           
					boolean updated = storesKey;
						// We return a non-null node only for insertion, and null for update
						return updated ? null : this;
					} 
				else if (this.middle != null) {                  
					return middle.add(key.substring(1));} 
				else {                                           
					this.middle = new TSTNode(key.substring(1), loc + key.substring(0, 1));
					// We need to find the last node in the chain to return it
					return middle.search(key.substring(1));
				}
			} 
			else if (c < character) {               
				if (this.left != null) {                           
					return left.add(key);
				} 
				else {                                           
					left = new TSTNode(key, loc);
					return left.search(key);
				}
			} 
			else {                                               
				if (this.right != null) {                          
					return right.add(key);
				} 
				else {                                         
					right = new TSTNode(key, loc);
					return right.search(key);
				}
			}
		}
		
		public TSTNode search(String key) {
			if (key.isEmpty()) {
				return null;
			}
			Character c = key.charAt(0);
			if (c.equals(this.character)) {                      
				if (key.length() == 1) {                          
					return this;
				} 
				else {                                          
					return this.middle == null ? null: this.middle.search(key.substring(1));  
				}
			} 
			else if (c.compareTo(this.character) < 0) {         
				return left == null ? null : left.search(key);
			} 
			else {                                              
				return right == null ? null : right.search(key);
			}
		}
	}
}
