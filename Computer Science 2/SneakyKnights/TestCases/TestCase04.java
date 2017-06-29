// Sean Szumlanski
// COP 3503, Spring 2017

// ==============================
// SneakyKnights: TestCase04.java
// ==============================
// A small test case in which none of the knights can attack one another.


import java.io.*;
import java.util.*;

public class TestCase04
{
	private static void checkTest(boolean success)
	{
		System.out.println(success ? "Hooray!" : "fail whale :(");
	}

	public static void main(String [] args) throws Exception
	{
		Scanner in = new Scanner(new File("input_files/TestCase04-input.txt"));
		ArrayList<String> list = new ArrayList<String>();

		// Read each line from the input file into the ArrayList.
		while (in.hasNext())
			list.add(in.next());

		checkTest(SneakyKnights.allTheKnightsAreSafe(list, 5) == true);
	}
}
