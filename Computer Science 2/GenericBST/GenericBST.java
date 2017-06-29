/*
 * Collin Speight
 * NID: co889675
 * COP 3503 Spring 2017
 *
 *
 * Credit to Dr. Sean Szumlanski for original program structure
 */
import java.io.*;
import java.util.*;

//nodes are the objects that make up the binary tree
class Node<AnyType extends Comparable<AnyType>>
{
    //they hold data and pointers to their left and right children
	AnyType data;
	Node<AnyType> left, right;

	Node(AnyType data)
	{
		this.data = data;
	}
}

public class GenericBST<AnyType extends Comparable<AnyType>>
{
	private Node<AnyType> root;
        
    //insert calling method that passes root as an argument for ease of use
	public void insert(AnyType data)
	{
		root = insert(root, data);
	}
        
    //insert node into the BST
	private Node<AnyType> insert(Node<AnyType> root, AnyType data)
	{
        //if tree is empty, create a node to act as the root
		if (root == null)
		{
			return new Node<AnyType>(data);
		}
        //if data is less than the current node, move left
		else if (data.compareTo(root.data) < 0)
		{
			root.left = insert(root.left, data);
		}
        //if data is greater than the current node, move right
		else if (data.compareTo(root.data) > 0)
		{
			root.right = insert(root.right, data);
		}
		else
		{
			//insertion of duplicate items is not allowed
			;
		}

		return root;
	}
        
    //delete calling method that passes root as an argument for ease of use
	public void delete(AnyType data)
	{
		root = delete(root, data);
	}
        
    //delete a node from the tree
	private Node<AnyType> delete(Node<AnyType> root, AnyType data)
	{
        //check if the tree is empty
		if (root == null)
		{
			return null;
		}
                
        //if data is less than the current node, move left
		else if (data.compareTo(root.data) < 0)
		{
			root.left = delete(root.left, data);
		}
                
        //if data is more than the current node, move right
		else if (data.compareTo(root.data) > 0)
		{
			root.right = delete(root.right, data);
		}
                
        //once found, actually delete the node and change the tree
		else
		{       
            //if node has no children, simply remove it
			if (root.left == null && root.right == null)
			{
				return null;
			}
            //if node has a left child and no right child, replace it
			else if (root.right == null)
			{
				return root.left;
			}
            //if node has a right child and no left child, replace it
			else if (root.left == null)
			{
				return root.right;
			}
			else
			{
				root.data = findMax(root.left);
				root.left = delete(root.left, root.data);
			}
		}

		return root;
	}

	//returns maximum value in BST by going to the rightmost leaf
	private AnyType findMax(Node<AnyType> root)
	{
		while (root.right != null)
		{
			root = root.right;
		}

		return root.data;
	}
        
    //contains calling method for cleaner, more readable code
	public boolean contains(AnyType data)
	{
        //passes root as an argument for ease of use
		return contains(root, data);
	}
        
    //returns true if the value is contained in the BST, false otherwise.
	private boolean contains(Node<AnyType> root, AnyType data)
	{
        //check if tree is empty
		if (root == null)
		{
			return false;
		}
		else if (data.compareTo(root.data) < 0)
		{
			return contains(root.left, data);
		}
		else if (data.compareTo(root.data) > 0)
		{
			return contains(root.right, data);
		}
		else
		{
			return true;
		}
	}
        
    //preorder calling method for cleaner, more readable code
	public void inorder()
	{
		System.out.print("In-order Traversal:");
		inorder(root);
		System.out.println();
	}
        
    //outputs the inorder transversal of the BST
	private void inorder(Node<AnyType> root)
	{
		if (root == null)
			return;
                
        //print each node in a (Left, Root, Right) order using recursion
		inorder(root.left);
		System.out.print(" " + root.data);
		inorder(root.right);
	}
        
    //preorder calling method for cleaner, more readable code
	public void preorder()
	{
		System.out.print("Pre-order Traversal:");
		preorder(root);
		System.out.println();
	}
        
    //outputs the preorder transversal of the BST
	private void preorder(Node<AnyType> root)
	{
		if (root == null)
			return;
                
        //print each node in a (Root, Left, Right) order using recursion
		System.out.print(" " + root.data);
		preorder(root.left);
		preorder(root.right);
	}
        
    //outputs the postorder transversal of the BST
	public void postorder()
	{
		System.out.print("Post-order Traversal:");
                
        //calling postorder in this method eliminates the need to pass
        //an argument in the postorder method call
		postorder(root);
		System.out.println();
	}
        
    //outputs the postorder transversal of the BST
	private void postorder(Node<AnyType> root)
	{
		if (root == null)
			return;
                
        //print each node in a (Left, Right, Root) order using recursion
		postorder(root.left);
		postorder(root.right);
		System.out.print(" " + root.data);
	}
        
    //returns the difficulty rating of the assignment
    public static double difficultyRating(){
        return 1.0;
    }
        
    //returns the number of hours spent on the assignment
    public static double hoursSpent(){
        return 5.0;
    }
}
