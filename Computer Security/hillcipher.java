/*
 * Collin Speight
 * NID: co889675
 * CIS 3360 Spring 2017
 */

import java.util.*;
import java.io.*;
import java.lang.*;


public class hillcipher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        int keySize;
        
        //open key file
        Scanner in = new Scanner(new File(args[0]));
        //first integer in file is the size of the key
        keySize = Integer.parseInt(in.next());
        
        //store key in a 2D array
        int [][] key = new int[keySize][keySize];
        while(in.hasNext()){
            for(int i = 0; i<keySize; i++){
                for(int j = 0; j<keySize; j++){
                    key[i][j] = Integer.parseInt(in.next());
                }
            }
        }
        
        //read in the plaintext, character by character
        int read, i = 0;
        ArrayList<Character> plainText = new ArrayList<>();
        
        FileInputStream fin = new FileInputStream(args[1]);
        while((read = fin.read()) != -1){
            //for each character read in, ignore it if not a letter and make lowercase
            if(Character.isLetter((char)read))
                plainText.add(Character.toLowerCase((char)read));
            else
                ;
        }
        fin.close();
        
        //if plainText is an odd number of characters, pad with x
        while(plainText.size() % keySize != 0)
            plainText.add('x');
        
        //populate a cipherText arraylist
        ArrayList<Character> cipherText = calculate(keySize, key, plainText);
        
        //output everything to console
        echo(keySize, key, plainText, cipherText);
    }
    
    private static void echo(int keySize, int[][] key, ArrayList<Character> plainText, ArrayList<Character> cipherText){
        //output key to console
        System.out.println("\nKey matrix:\n");
        for(int i = 0; i < keySize; i++){
            for(int j = 0; j < keySize; j++){
                System.out.print(key[i][j] + " ");
            }
            System.out.println("");
        }
        
        //output plaintext to console
        System.out.println("\nPlaintext:\n");
        int i = 0;
        for(char c : plainText){
            System.out.print(c);
            i++;
            
            //limit each outputted line to 80 characters
            if(i % 80 == 0)
                System.out.print("\n");
        }
        System.out.println("");
        
        //output ciphertext to console
        System.out.println("\nCiphertext:\n");
        i = 0;
        for(char c : cipherText){
            System.out.print(c);
            i++;
            
            if(i % 80 == 0)
                System.out.print("\n");
        }
        System.out.println("");
    }
    
    private static ArrayList<Character> calculate(int keySize, int[][]key, ArrayList<Character> plainText){
        //convert plain text into each character's number equivalent
        int [] plainNumbers = new int[plainText.size()];
        int i = 0;
        for(char c : plainText){
            plainNumbers[i] = ((int)c - 97);
            i++;
        }
        
        ArrayList<Character> cipherText = new ArrayList<>();
        
        //perform calculations necessary to hill cipher
        for(i = 0; i < plainNumbers.length; i += keySize){
            //split input into blocks to be processed one at a time
            int [] tempBlock = new int[keySize];
            for(int j = 0; j < tempBlock.length; j++)
                tempBlock[j] = plainNumbers[i+j];
            
            int result;
            //perform multiplication and addition one row at a time
            for(int row = 0; row < keySize; row++){
                result = 0;
                
                for(int col = 0; col < keySize; col++)
                    result += ((key[row][col] * tempBlock[col])%26);
                
                //use ascii manipulation to convert ints back to chars
                if(result <= 25)
                    cipherText.add((char)(result + 'a'));
                //make sure the resulting letters wrap around the alphabet
                else{
                    result = result % 26;
                    cipherText.add((char)(result + 'a'));
                }
            }
        }
        
        return cipherText;
    }
}