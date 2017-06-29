// Sean Szumlanski
// COP 3503, Spring 2017

// ==============================
// SneakyKnights: TestCase01.java
// ==============================
// A brief test case to help ensure you've implemented the difficultyRating()
// method correctly.


import java.io.*;
import java.util.*;

public class TestCase01
{
	public static void main(String [] args)
	{
		double difficulty = SneakyKnights.difficultyRating();
		System.out.println((difficulty < 1.0 || difficulty > 5.0) ? "fail whale :(" : "Hooray!");
	}
}
