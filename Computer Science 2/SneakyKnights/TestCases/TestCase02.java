// Sean Szumlanski
// COP 3503, Spring 2017

// ==============================
// SneakyKnights: TestCase02.java
// ==============================
// A brief test case to help ensure you've implemented the hoursSpent()
// method correctly.


import java.io.*;
import java.util.*;

public class TestCase02
{
	public static void main(String [] args)
	{
		double hours = SneakyKnights.hoursSpent();
		System.out.println((hours <= 0.0) ? "fail whale :(" : "Hooray!");
	}
}
