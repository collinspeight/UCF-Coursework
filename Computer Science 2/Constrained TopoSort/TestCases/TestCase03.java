// Sean Szumlanski
// COP 3503, Spring 2017

// ====================================
// ConstrainedTopoSort: TestCase03.java
// ====================================
// A small test case for ConstrainedTopoSort.


import java.io.*;
import java.util.*;

public class TestCase03
{
	private static void failwhale(String params)
	{
		System.out.println("hasConstrainedTopoSort(" + params + "): fail whale :(");
		System.exit(0);
	}

	public static void main(String [] args) throws IOException
	{
		ConstrainedTopoSort t = new ConstrainedTopoSort("g1.txt");

		if (t.hasConstrainedTopoSort(3, 1) != false) failwhale("3, 1");
		if (t.hasConstrainedTopoSort(3, 2) != false) failwhale("3, 2");
		if (t.hasConstrainedTopoSort(3, 4) != true) failwhale("3, 4");

		System.out.println("Hooray!");
	}
}
