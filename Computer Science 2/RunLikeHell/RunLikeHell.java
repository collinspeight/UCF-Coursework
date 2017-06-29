/*
 * Collin Speight
 * NID: co889675
 * COP 3503, Spring 2017
 */

import java.io.*;
import java.util.*;

public class RunLikeHell{
    //DP maxGain function - Runtime: O(n), Space Complexity: O(n)
    public static int maxGain(int [] blocks) {
        //Array to store the bottom-up procedure that leads to the solution
        int [] maxKnowledge = new int [blocks.length+3];
        
        //Starting from the end of the array, accumulate the maximum added values at each step
        for(int i = maxKnowledge.length-4; i >= 0; i--)
            maxKnowledge[i] = blocks[i] + Math.max(maxKnowledge[i+2], maxKnowledge[i+3]);
        
        //The final answer will be the maximum of either the first or second index
        //which each represent the knowledge gained from different strategies
        return Math.max(maxKnowledge[0], maxKnowledge[1]);
    }
    
    //How difficult I found the assignment
    public static double difficultyRating(){
        return 3.5;
    }
    
    //The amount of time spent on the assignment
    public static double hoursSpent(){
        return 12.0;
    }
}