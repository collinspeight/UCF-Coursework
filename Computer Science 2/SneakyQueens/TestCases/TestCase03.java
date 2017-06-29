// Sean Szumlanski
// COP 3503, Spring 2017

// =============================
// SneakyQueens: TestCase03.java
// =============================
// A larger test case to help you determine whether your program is working
// correctly. None of the queens in this one are able to attack one another.
//
// For detailed compilation and testing instructions, see the assignment PDF.


import java.io.*;
import java.util.*;

public class TestCase03
{
	private static void checkTest(boolean success)
	{
		System.out.println(success ? "Hooray!" : "fail whale :(");
	}

	public static void main(String [] args) throws Exception
	{
		Scanner in = new Scanner(new File("input_files/TestCase03-input.txt"));
		ArrayList<String> list = new ArrayList<String>();

		// Read each line from the input file into the ArrayList.
		while (in.hasNext())
			list.add(in.next());

		checkTest(SneakyQueens.allTheQueensAreSafe(list, 10000) == true);
	}
}
