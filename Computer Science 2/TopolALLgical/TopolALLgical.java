/*
 * Collin Speight
 * NID: co889675
 * COP 3503, Spring 2017
 */

import java.util.*;
import java.io.*;

public class TopolALLgical {
    //Adjacency list is used to store the graph for improved runtime
    private static ArrayList<LinkedList<Integer>> adjList = new ArrayList<>();
    
    //Primary control method of the program, also serves as a utility function to the recursive backtracking method
    public static HashSet<String> allTopologicalSorts(String filename) throws IOException {
        HashSet<String> sorts = new HashSet<>();

        //Try to read graph file into an adjacency list, if the file cannot be found, catch the exception and return an empty hash set
        try (Scanner in = new Scanner(new File(filename))) {
            int numNodes = in.nextInt();
            int listLen;

            //Populate the adjacency list
            for (int i = 0; i < numNodes; i++) {
                adjList.add(new LinkedList<Integer>());

                listLen = in.nextInt();
                for (int j = 0; j < listLen; j++)
                    adjList.get(i).addFirst(in.nextInt() - 1);
            }
        }
        catch (IOException e) {
            //If there is an exception, return an empty HashSet
            return sorts;
        }
        
        //Populate an incoming array with the in-degree of each node
        int[] incoming = new int[adjList.size()];
        for(int i = 0; i < adjList.size(); i++)
            for(int j = 0; j < adjList.get(i).size(); j++)
                incoming[adjList.get(i).get(j)]++;
        
        //The following are variables used to keep track of the current state of the backtracking process
        boolean [] visited = new boolean[adjList.size()];
        int numVisited = 0;
        ArrayList<Integer> sort = new ArrayList<>();
        
        findTopoSorts(sorts, sort, visited, incoming, numVisited);
        
        return sorts;
    }
    
    //Recursive backtracking method used to generate all possible topological sorts one node at a time
    public static void findTopoSorts(HashSet<String> sorts, ArrayList<Integer> sort, boolean [] visited, int [] incoming, int numVisited) {
        //If all nodes have been visited, add the sort string to the set (Base Case)
        if(numVisited == adjList.size()) {
            //StringBuilder is used to prevent horrific runtimes
            StringBuilder sortString = new StringBuilder();
            
            //Create a string from the nodes' integer values
            for(int i = 0; i < sort.size(); i++){
                sortString.append(Integer.toString(sort.get(i)+1));
                
                //Do not add a space after the final value
                if(i != sort.size()-1)
                    sortString.append(" ");
            }
            
            sorts.add(sortString.toString());
        }
        
        //Because the current node is visited, all of it's adjacent nodes have one less unvisited pre-requisite
        //(Generate Possible Moves)
        for(int i = 0; i < adjList.size(); i++){
            //By taking only the nonvisited nodes that have no incoming edges, no 2 permutations will be identical
            if(!visited[i] && incoming[i] == 0){
                for(int j = 0; j < adjList.size(); j++)
                    //Decrease the incoming value of all adjacent nodes
                    if(adjList.get(i).contains(j))
                        --incoming[j];
                
                //Add the current node to the sort, mark it as visited, and increment the number of visited nodes
                //(Change State)
                sort.add(i);
                visited[i] = true;
                ++numVisited;
                
                //Perform Recursive Descent
                findTopoSorts(sorts, sort, visited, incoming, numVisited);
                
                //Undo all state changes performed pre-recursive descent
                for(int j = 0; j < adjList.size(); j++)
                    if(adjList.get(i).contains(j))
                        ++incoming[j];
                
                sort.remove((Integer)i);
                visited[i] = false;
                --numVisited;
            }
        }
    }

    //How difficult I found this assignment:
    public static double difficultyRating() {
        return 4.0;
    }

    //How long it took me to complete this assignment:
    public static double hoursSpent() {
        return 20;
    }
}