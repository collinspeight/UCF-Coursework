// Sean Szumlanski
// COP 3503, Spring 2017

// =============================
// SneakyQueens: TestCase05.java
// =============================
// A very large test case to help you determine whether your program is working
// correctly, and also to help determine whether your program is running in
// linear time. If so, this one should execute very quickly.
//
// None of the queens in this one are able to attack one another.
//
// For detailed compilation and testing instructions, see the assignment PDF.


import java.io.*;
import java.util.*;

public class TestCase05
{
	private static void checkTest(boolean success)
	{
		System.out.println(success ? "Hooray!" : "fail whale :(");
	}

	public static void main(String [] args) throws Exception
	{
		Scanner in = new Scanner(new File("input_files/TestCase05-input.txt"));
		ArrayList<String> list = new ArrayList<String>();

		// Read each line from the input file into the ArrayList.
		while (in.hasNext())
			list.add(in.next());

		checkTest(SneakyQueens.allTheQueensAreSafe(list, 60000) == true);
	}
}
