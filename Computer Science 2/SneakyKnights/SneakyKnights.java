/*
 * Collin Speight
 * NID: co889675
 * COP 3503 Spring 2017
 */

import java.util.*;
import java.io.*;
import java.awt.Point;

public class SneakyKnights {
    
    public static boolean allTheKnightsAreSafe(ArrayList<String> coordinateStrings, int boardSize){
        String col, row;
        int colNum, rowNum, x, y;
        //HashSet used to store the position of the knights
        HashSet<Point> coordHash = new HashSet<>();
        
        for(String coord : coordinateStrings){
            int counter = 0;
            
            //Separating coordinates into a column string and row string using substring static method and a counter
            for(int i = 0; i<coord.length(); i++){  
                if(Character.isDigit(coord.charAt(i)))
                    break;
                counter++;
            }
            col = coord.substring(0, counter);
            row = coord.substring(counter, coord.length());
            
            //Convert column value from a base-26 string to a base-10 integer
            colNum = convertCol(col);   
            //Convert row value from a string to an integer
            rowNum = Integer.parseInt(row);
            
            //(x,y) coordinate for the current knight being processed
            Point coordinate = new Point(colNum, rowNum);
            
            //If the hash table is empty, just add the first knight into it
            if(coordHash.isEmpty()){
                x = (int)coordinate.getX();
                y = (int)coordinate.getY();
                
                //Me being paranoid about edge cases
                if(x>0 && y>0 && x<=boardSize && y<=boardSize)
                    coordHash.add(coordinate);
            }
            else{
                //Find all possible moves of the current knight
                Point [] points = findPoints(colNum, rowNum);
                
                for(int j = 0; j<8; j++){
                    //If a knight is at the position of a possible move, return false
                    if(coordHash.contains(points[j]))
                        return false;
                    
                    x = (int)coordinate.getX();
                    y = (int)coordinate.getY();
                    //Check to ensure that current knight being processed is indeed viable
                    if(x>0 && y>0 && x<=boardSize && y<=boardSize)
                        coordHash.add(coordinate);
                }
            }
        }
        
        //If none of the knights can attack each other, return true
        return true;
    }
    
    //Declares, initializes, and returns an array of pointer objects
    public static Point[] findPoints(int colNum, int rowNum){
        Point [] coords = new Point[8];
        
        //Each element is a possible move for the current piece
        coords[0] = new Point(colNum+1, rowNum+2);
        coords[1] = new Point(colNum+1, rowNum-2);
        coords[2] = new Point(colNum-1, rowNum+2);
        coords[3] = new Point(colNum-1, rowNum-2);
        coords[4] = new Point(colNum+2, rowNum+1);
        coords[5] = new Point(colNum+2, rowNum-1);
        coords[6] = new Point(colNum-2, rowNum+1);
        coords[7] = new Point(colNum-2, rowNum-1);
        
        return coords;
    }
    
    //convert the column from a string in base 26 to an integer in base 10
    public static int convertCol(String col){   
        char colChars[] = col.toCharArray();
        int colNums[] = new int[col.length()], result = 0;
        
        for(int i = 0; i<col.length(); i++){
            //Convert each character to it's corresponding base 10 value by taking it's ascii value - 96
            colNums[i] = (int)colChars[i] - 96;
            
            //Simple base conversion
            result += (colNums[i] * Math.pow(26, ((col.length()-1)-i)));
        }
        
        return result;
    }
    
    //Default method that returns the difficulty of the program
    public static double difficultyRating(){
        return 3.0;
    }
    
    //Default method that returns the hours spent on the program
    public static double hoursSpent(){
        return 8.0;
    }
}