// Sean Szumlanski
// COP 3503, Spring 2017

// ==============================
// TopolALLgical: TestCase01.java
// ==============================
// The input graph for this test case corresponds to the graph from the
// assignment PDF.


import java.io.*;
import java.util.*;

public class TestCase01
{
	private static HashSet<String> loadSolution(String filename) throws IOException
	{
		Scanner in = new Scanner(new File(filename));
		HashSet<String> strings = new HashSet<>();

		while (in.hasNextLine())
			strings.add(in.nextLine());

		return strings;
	}

	public static void main(String [] args) throws IOException
	{
		HashSet<String> solution = loadSolution(
			"input_files/TestCase01-solution.txt");

		HashSet<String> toposorts = TopolALLgical.allTopologicalSorts(
			"input_files/TestCase01-input.txt");

		// Sanity check.
		if (solution.size() != 10) System.out.println("Uh oh!");

		System.out.println(toposorts.equals(solution) ? "Hooray!" : "fail whale :(");
	}
}
