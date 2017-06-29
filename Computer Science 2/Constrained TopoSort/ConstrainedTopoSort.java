/*
 * Collin Speight
 * NID: co889675
 * COP 3503, Spring 2017
 */

import java.io.*;
import java.util.*;

public class ConstrainedTopoSort{
    //Adjacency list to hold the graph
    ArrayList<LinkedList<Integer>> adjList = new ArrayList<>();
    
    //Constructor for storing the given graph in an adjacency list
    public ConstrainedTopoSort(String filename) throws IOException{
        Scanner in = new Scanner(new File(filename));
        
        int numNodes = in.nextInt();
        int listLen;
        
        //Populate the adjacency list
        for(int i = 0; i < numNodes; i++){
            adjList.add(new LinkedList<Integer>());
            
            listLen = in.nextInt();
            for(int j = 0; j < listLen; j++)
                adjList.get(i).addFirst(in.nextInt()-1);
        }
    }
    
    //Determines whether or not a given graph has topological sort with x preceding y
    public boolean hasConstrainedTopoSort(int x, int y){
        //If there exists a path between x and y starting from y, failure :(
        if(DFS(y-1, x-1))
            return false;
        
        //If a path doesn't exist or all paths strictly start from x, success!
        return true;
    }
    
    //Iterative DFS used to determine whether or not there's a path between y and x
    public boolean DFS(int start, int target){
	    Stack<Integer> stack = new Stack<>();
	    boolean[] visited = new boolean[adjList.size()];
	    	    
	    //The start vertex is marked as visited and pushed to the stack immediately
	    visited[start] = true;
	    stack.push(start);
	    	    
	    //While the stack is not empty...
	    while(!stack.empty()){
	        //Pop the stack and check if the top value is the target
	        int node = stack.pop();
	        
	        //If the target is found terminate the DFS
	        if(node == target)
	            return true;
	        
	        //If the neighbors of the current node have not been visited yet, 
	        //mark them as visited and push them onto the stack
	        for(int i = adjList.size()-1; i >= 0; i--){
	            if(adjList.get(node).contains(i) && !visited[i]){
	    	        visited[i] = true;
	    	        
	    	        stack.push(i);
	            }
	        }
	    }
	    
	    return false;
	}
    
    //return my difficulty rating of the assignment
    public static double difficultyRating(){
        return 2.5;
    }
    
    //return the number of ours I spent on the assignment
    public static double hoursSpent(){
        return 7.5;
    }
}