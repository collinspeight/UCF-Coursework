// Sean Szumlanski
// COP 3503, Spring 2017

// ==============================
// TopolALLgical: TestCase02.java
// ==============================
// The input graph for this test case has no edges, and so there should be n!
// valid topological sorts.


import java.io.*;
import java.util.*;

public class TestCase02
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
			"input_files/TestCase02-solution.txt");

		HashSet<String> toposorts = TopolALLgical.allTopologicalSorts(
			"input_files/TestCase02-input.txt");

		// Sanity check.
		if (solution.size() != 40320) System.out.println("Uh oh!");

		System.out.println(toposorts.equals(solution) ? "Hooray!" : "fail whale :(");
	}
}
