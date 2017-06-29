// Sean Szumlanski
// COP 3503, Spring 2017

// =============================
// SneakyQueens: TestCase02.java
// =============================
// Several small test cases to help you determine whether your program is
// working correctly.
//
// For detailed compilation and testing instructions, see the assignment PDF.


import java.io.*;
import java.util.*;

public class TestCase02
{
	private static void checkTest(boolean success)
	{
		System.out.println(success ? "Hooray!" : "fail whale :(");
	}

	public static void main(String [] args)
	{
		ArrayList<String> list = new ArrayList<String>();

		list.clear();
		list.add("a1");
		list.add("b2");
		checkTest(SneakyQueens.allTheQueensAreSafe(list, 60000) == false);

		list.clear();		
		list.add("a2");
		list.add("b1");
		checkTest(SneakyQueens.allTheQueensAreSafe(list, 60000) == false);

		list.clear();
		list.add("xx342");
		list.add("xy343");
		checkTest(SneakyQueens.allTheQueensAreSafe(list, 60000) == false);

		list.clear();
		list.add("xx342");
		list.add("xy341");
		checkTest(SneakyQueens.allTheQueensAreSafe(list, 60000) == false);

		list.clear();
		list.add("xx342");
		list.add("xw341");
		checkTest(SneakyQueens.allTheQueensAreSafe(list, 60000) == false);

		list.clear();
		list.add("xx342");
		list.add("xw343");
		checkTest(SneakyQueens.allTheQueensAreSafe(list, 60000) == false);

		list.clear();
		list.add("a3");
		list.add("b1");
		list.add("c4");
		list.add("d2");
		checkTest(SneakyQueens.allTheQueensAreSafe(list, 4) == true);

		list.clear();
		list.add("a2");
		list.add("b4");
		list.add("c1");
		list.add("d3");
		checkTest(SneakyQueens.allTheQueensAreSafe(list, 4) == true);
	}
}
