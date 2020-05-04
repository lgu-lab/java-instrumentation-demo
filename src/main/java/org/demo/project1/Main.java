package org.demo.project1;

import org.demo.instrumentation.Instrumentation;
import org.demo.instrumentation.counters.Counters;
import org.demo.instrumentation.measures.TimeMeasure;
import org.demo.instrumentation.measures.TimeMeasureRoot;

public class Main {

	public static void main(String[] args) {
		System.out.println("Starting...");
		
		// Instrumentation activation
		Instrumentation.setActive(true); // just for tests
		// For real project set environment variable "CODE_INSTRUM" = true

		// "Root" of all measures ("reporting)
		try (TimeMeasure tm = new TimeMeasureRoot("func0")) {  // reporting on "stdout" (console)
		//try (TimeMeasure tm = new TimeMeasureRoot("func0", "D:/tmp/duration")) { // reporting in file (one file for each thread)
			func0();
		} // reporting is triggered here if "TimeMeasureRoot" 

		
		// Counters reporting on "stdout" (console)
		Counters.write();

		// Counters reporting in file (1 file for each thread)
		// e.g. "/tmp/counters" --> "/tmp/counters_2020-05-04_15-16-50_246_T1.txt"
		Counters.write("D:/tmp/counters");
	}

	private static void func0() {
		Counters.increment("func0-count");
		for (int i = 0; i < 20; i++) {
			// Measure : "func1" duration
			try (TimeMeasure tm = new TimeMeasure("func1")) {
				func1(i);
			}
		}
	}

	private static void func1(int n) {
		Counters.increment("func1-count");
		System.out.println("func1(" + n + ")");
		for (int i = 0; i < 10; i++) {
			try (TimeMeasure tm = new TimeMeasure("func2")) { // func2 duration
				func2(n, i);
			}
		}
	}

	private static void func2(int n, int i) {
		Counters.increment("func2-count");
		try {
			Thread.sleep((i % 3) * 10);
		} catch (InterruptedException e) {
		}
	}
}
