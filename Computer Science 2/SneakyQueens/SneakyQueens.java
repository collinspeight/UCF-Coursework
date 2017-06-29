package sneakyqueens;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Collin Speight
 * 
 * Due: 1/22/17
 * 
 * COP3503C-17Spring 0001
 */
public class SneakyQueens {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        ArrayList<String> list = new ArrayList<String>();

		in = new Scanner(new File("input_files/SQinput01.txt"));
		// Read each line from the input file into the ArrayList.
		while (in.hasNext())
			list.add(in.next());
			
		if(allTheQueensAreSafe(list, 60000) == true)
			System.out.println("Yay!\n");
		else
			System.out.println("Boo! :(");
    }
    
    public static boolean allTheQueensAreSafe(ArrayList<String> coordinateStrings, int boardSize){
        int colNum = 0, rowNum = 0;
        String col, row;
        int vertical[] = new int[boardSize], horizontal[] = new int[boardSize],     //Arrays used to represent each board space in relation to rows, columns, and diagonals
            rDiag[] = new int[(boardSize*2)-1], lDiag[] = new int[(boardSize*2)-1];
        
        for(String coord : coordinateStrings){
            int counter = 0;
            for(int i = 0; i<coord.length(); i++){  //Separating coordinates into a column string and row string using substring static method and a counter
                if(Character.isDigit(coord.charAt(i)))
                    break;
                counter++;
            }
            col = coord.substring(0, counter);
            row = coord.substring(counter, coord.length());
            
            rowNum = Integer.parseInt(row);
            colNum = convertCol(col);
            
            horizontal[rowNum-1]++;    //Indices of each array represents a queen's location on the board in respect to other pieces
            vertical[colNum-1]++;      
            lDiag[Math.abs((colNum-rowNum) - (boardSize-1))]++;
            rDiag[Math.abs((colNum+rowNum) - 2)]++;
        }
        
        for(int j = 0; j < ((boardSize*2)-1); j++){     //Check if any row, column, or diagonal is occupied by more than one piece, if so, the queens are not safe
            if(j < boardSize && (vertical[j] > 1 || horizontal[j] > 1)){
                return false;
            }
            if(lDiag[j] > 1 || rDiag[j] > 1){
                return false;
            }
        }
        
        return true;
    }
    
    public static int convertCol(String col){   //Convert the column from a string in base 26 to an integer in base 10
        char colChars[] = col.toCharArray();
        int colNums[] = new int[col.length()], result = 0;
        
        for(int i = 0; i<col.length(); i++){
            colNums[i] = (int)colChars[i] - 96; //Convert each character to it's corresponding base 10 value by taking it's ascii value - 96
            
            result += (colNums[i] * Math.pow(26, ((col.length()-1)-i)));
        }
        
        return result;
    }
    
    public static double difficultyRating(){    //Assignment difficulty
        double rating = 3.0;
        
        return rating;
    }
    
    public static double hoursSpent(){  //Time spent on assignment
        double hours = 10.0;
        
        return hours;
    }
}
