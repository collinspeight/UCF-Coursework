// Sean Szumlanski
// COP 3503, Spring 2017

// ==============================
// SneakyKnights: TestCase09.java
// ==============================
// A very large test case in which at least two of the knights can attack one another.
// If you want to find one of the knights that can attack another in this test
// case, run 'diff' on TestCase08-input.txt and TestCase09-input.txt. The one
// line where they differ contains one of the knights that can attack another.

import java.io.*;
import java.util.*;

public class TestCase09
{
	private static void checkTest(boolean success)
	{
		System.out.println(success ? "Hooray!" : "fail whale :(");
	}

	public static void main(String [] args) throws Exception
	{
		Scanner in = new Scanner(new File("input_files/TestCase09-input.txt"));
		ArrayList<String> list = new ArrayList<String>();

		// Read each line from the input file into the ArrayList.
		while (in.hasNext())
			list.add(in.next());

		checkTest(SneakyKnights.allTheKnightsAreSafe(list, Integer.MAX_VALUE) == false);
	}
}
