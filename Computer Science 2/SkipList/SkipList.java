/*
    Collin Speight
    NID: co889675
    COP 3503C Spring 2017
*/

import java.util.*;
import java.io.*;

//the skip list is a series of node objects connected on 1 or more levels
class Node<AnyType extends Comparable<AnyType>>
{
    int height;
    AnyType data;
    
    //an arraylist of next references allows for multiple levels of references
    ArrayList<Node<AnyType>> next;
    
    //constructor used for creating a head node, only takes a height argument
    public Node(int height){
        this.height = height;
        
        next = new ArrayList<>();
        //set all of the node's next references to null initially
        for(int i = 0; i < height; i++)
            next.add(null);
    }
    
    //constructor that takes a data argument; used for all nodes but head
    public Node(AnyType data, int height){
        this.height = height;
        this.data = data;
        
        next = new ArrayList<>();
        //again, set all next references to null
        for(int i = 0; i < height; i++)
            next.add(null);
    }
    
    //return the node's data
    public AnyType value(){
        return data;
    }
    
    //return the nodes height
    public int height(){
        return height;
    }
    
    //return a reference to the next node at a certain level of the current node
    public Node<AnyType> next(int level){
        if(level < 0 || level > (height-1))
            return null;
        
        return next.get(level);
    }
    
    //set the current node's next reference at a certain level to the argument node
    public void setNext(int level, Node<AnyType> node){
        next.set(level, node);
    }
    
    //increment the node's height by 1 and add a null reference at the top level
    //note: only used for head node growth
    public void grow(){
        height++;
        next.add(null);
    }
    
    //give the node a 50% chance to grow
    public boolean maybeGrow(){
        if(Math.random() < .50){
            height++;
            next.add(null);
            
            //if the growth is successful, return true
            return true;
        }
        else
            return false;
    }
    
    //trim the node's height to the value passed as an argument
    public void trim(int height){
        while(this.height > height){
            next.remove(this.height-1);
            --this.height;
        }
    }
}

public class SkipList<AnyType extends Comparable<AnyType>>
{
    int size = 0, listHeight = 1;
    Node<AnyType> head;
    
    //constructor that creates a new skiplist by initializing a head
    public SkipList(){
        //the given height of 1 is a personal choice
        head = new Node<AnyType>(1);
    }
    
    //constructor that creates a new skiplist of a given height
    public SkipList(int height){
        //if the passed height is less than the minimum, set it at the minimum
        if(height < 1)
            head = new Node<AnyType>(1);
            
        else{
            head = new Node<AnyType>(height);
            
            listHeight = height;
        }
    }
    
    //return the number of nodes in the skip list
    public int size(){
        return size;
    }
    
    //return the height of the skiplist
    public int height(){
        return listHeight;
    }
    
    //return a reference to the head node of the skip list
    public Node<AnyType> head(){
        return head;
    }
    
    //insert a node with the given data into the skiplist
    public void insert(AnyType data){
        int maxHeight = getMaxHeight(size);
        int randomHeight = generateRandomHeight(maxHeight);
        
        //create a new node with the given data and a randomly generated height
        Node<AnyType> newNode = new Node<AnyType>(data, randomHeight);
        
        //if list is empty, insert newnode next to head
        if(head.next(0) == null){
            for(int i = 0; i < newNode.height(); i++){
                newNode.setNext(i, head.next(i));
                head.setNext(i, newNode);
            }
            
            //increment the size of the list 
            ++size;
        }
        
        else{
            Node<AnyType> temp = head;
            int level = head.height()-1;
            
            //arraylist stores references to the nodes where decrease in level was necessary
            ArrayList<Node<AnyType>> dropDowns = new ArrayList<>();
            
            //traverse the skiplist and populate the dropDowns arraylist
            while(level >= 0){
                if(temp.next(level) != null && temp.next(level).value().compareTo(newNode.value()) < 0)
                    temp = temp.next(level);
                
                else{
                    dropDowns.add(temp);
                    --level;
                }
            }
            
            level = head.height()-1;
            //for each node that dropped down, consider changing its reference points
            for(Node<AnyType> n : dropDowns){
                //if the node is on an appropriate level, change its references
                if(level <= newNode.height()-1){
                    //simple linked list insertion
                    newNode.setNext(level, n.next(level));
                    n.setNext(level, newNode);
                }
                
                --level;
            }
            
            //increment the size of the list for the new node
            ++size;
        }
        
        //if the insertion makes growth necessary, grow the list
        if((int)(Math.ceil(Math.log(size)/Math.log(2))) > head.height())
            growSkipList();
    }
    
    //essentially the same as the above function, only taking away the randomness of node height
    public void insert(AnyType data, int height){
        Node<AnyType> newNode = new Node<AnyType>(data, height);
        
        //if list is empty, insert newnode next to head
        if(head.next(0) == null){
            for(int i = 0; i < newNode.height(); i++){
                newNode.setNext(i, head.next(i));
                head.setNext(i, newNode);
            }
            
            //increment the size of the list 
            ++size;
        }
        
        /*explained in-depth in previous insert method*/
        else{
            Node<AnyType> temp = head;
            int level = head.height()-1;
            ArrayList<Node<AnyType>> dropDowns = new ArrayList<>();
            
            while(level >= 0){
                if(temp.next(level) != null && temp.next(level).value().compareTo(newNode.value()) < 0)
                    temp = temp.next(level);
                
                else{
                    dropDowns.add(temp);
                    --level;
                }
            }
            
            level = head.height()-1;
            for(Node<AnyType> n : dropDowns){
                if(level <= newNode.height()-1){
                    newNode.setNext(level, n.next(level));
                    n.setNext(level, newNode);
                }
                
                --level;
            }
            
            ++size;
        }
        
        //check for list growth
        if((int)(Math.ceil(Math.log(size)/Math.log(2))) > head.height())
            growSkipList();
    }
    
    //delete a node with the given data from the list
    public void delete(AnyType data){
        ArrayList<Node<AnyType>> dropDowns = new ArrayList<>();
        Node<AnyType> temp = head;
        
        //track whether a node was deleted to properly control the size
        boolean deleted = false;
        int level = head.height()-1;
        
        //traverse the list, like in insertion
        while(level >= 0){
            if(temp.next(level) != null && temp.next(level).value().compareTo(data) < 0)
                temp = temp.next(level);
            
            else{
                dropDowns.add(temp);
                --level;
            }
        }
        
        //if a node with the given data is found, delete it
        Node<AnyType> target = null;
        if((target = getFirst(data)) != null){
            level = head.height()-1;
            
            //change the references pointing at the target to delete it
            for(Node<AnyType> n : dropDowns){
                if(level <= target.height()-1)
                    n.setNext(level, target.next(level));
                
                --level;
            }
            
            deleted = true;
        }
        
        //if a node was deleted, decrement the size of the list
        if(deleted){
            --size;
            
            //check if deleting the node made shrinking necessary
            if((int)(Math.ceil(Math.log(size)/Math.log(2))) < head.height())
                trimSkipList();
        }
    }
    
    //returns true if a node with the given data is in the list, false if not
    public boolean contains(AnyType data){
        Node<AnyType> temp = head;
        int level = head.height()-1;
        
        while(level >= 0){
            if(temp.next(level) != null && temp.next(level).value().compareTo(data) < 0)
                temp = temp.next(level);
            
            //the node is found
            else if(temp.next(level) != null && temp.next(level).value().compareTo(data) == 0)
                return true;
            
            else
                --level;
        }
        return false;
    }
    
    //returns a node (not necessarily leftmost) with the given data in the list
    public Node<AnyType> get(AnyType data){
        Node<AnyType> temp = head;
        int level = head.height()-1;
        
        while(level >= 0){
            if(temp.next(level) != null && temp.next(level).value().compareTo(data) < 0)
                temp = temp.next(level);
            
            //the node is found
            else if(temp.next(level) != null && temp.next(level).value().compareTo(data) == 0)
                return temp.next(level);
            
            else
                --level;
        }
        
        //if no node is found, return null
        return null;
    }
    
    //returns a reference to the first node found with the given data
    public Node<AnyType> getFirst(AnyType data){
        Node<AnyType> temp = head;
        int level = head.height()-1;
        
        //list traversal
        while(level >= 0){
            if(temp.next(level) != null && temp.next(level).value().compareTo(data) < 0)
                temp = temp.next(level);
            
            else
                --level;
        }
        if(level < 0)
            level = 0;
        
        //if the correct node is found, return a reference to it
        if(temp.next(level) != null && temp.next(level).value().compareTo(data) == 0)
            return temp.next(level);
        
        return null;
    }
    
    //returns the appropriate max height for the current list
    private int getMaxHeight(int size){
        if(size > 0){
            int maxHeight = (int)(Math.ceil(Math.log(size)/Math.log(2)));
            return Math.max(maxHeight, head.height());
        }
        else
            return head.height();
    }
    
    //generate a random height based on the appropriate probability and max height
    private static int generateRandomHeight(int maxHeight){
        int numFlips = 1;
        for(int i = 1; i < maxHeight; i++){
            if(Math.random() < 0.5)
                break;
            else
                numFlips++;
        }
        return numFlips;
    }
    
    //grows the skiplist if insertion causes the appropriate height change
    private void growSkipList(){
        //1st grow the head node
        head.grow();
        ++listHeight;
        
        //last reference to the last node that was grown
        Node<AnyType> last = head;
        
        Node<AnyType> temp = head.next(listHeight-2);
        //give each node at the previous top level a 50% chance of growth
        while(temp != null){
            if(temp.maybeGrow()){
                last.setNext(head.height()-1, temp);
                last = temp;
            }
            
            temp = temp.next(listHeight-2);
        }
    }
    
    //trim the skip list if deletion results in the appropriate height change
    private void trimSkipList(){
        int level = head.height()-2;
        int naturalHeight = (int)(Math.ceil(Math.log(size)/Math.log(2)));
        
        //two references are used for list traversal and manipulation
        Node<AnyType> temp = head.next(level);
        Node<AnyType> temp2 = null;
        
        while(temp != null){
            temp2 = temp.next(level);
            
            //if the list has a height of 1, it should not go below 1
            if(head.height() == 1)
                temp.trim(1);
            
            //if a node is inserted with a user-defined height, it should be trimmed
            //all the way down to it's natural maximum height
            else if(head.height() > naturalHeight)
                temp.trim(naturalHeight);
            
            //in most cases, just trim the list down a level
            else
                temp.trim(level);
                
            temp = temp2;
        }
        
        /*trim the head node last*/
        if(head.height() == 1)
            head.trim(1);
            
        else if(head.height() > naturalHeight)
            head.trim(naturalHeight);
                
        else
            head.trim(level);
        
        //update the list height
        listHeight = head.height();
    }
    
    //return my difficulty rating of the assignment
    public static double difficultyRating(){
        return 4.5;
    }
    
    //return the number of ours I spent on the assignment
    public static double hoursSpent(){
        return 20;
    }
}