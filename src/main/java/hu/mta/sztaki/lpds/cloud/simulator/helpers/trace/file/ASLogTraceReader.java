package hu.mta.sztaki.lpds.cloud.simulator.helpers.trace.file;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import hu.mta.sztaki.lpds.cloud.simulator.helpers.job.Job;

public class ASLogTraceReader extends TraceFileReaderFoundation {


	public ASLogTraceReader(String filename, int from, int to, boolean allowReadingFurther, Class<? extends Job> jobType)
			throws SecurityException, NoSuchMethodException {
		super("Log Format", filename, from, to, allowReadingFurther, jobType);
		// TODO Auto-generated constructor stub

	}

	protected boolean isTraceLine(String a) {

		String splitOn = " ";
		// split the string into four parts (by whitespace)
		String[] words = a.split(splitOn);
		
		// check part 1 is a number (in unix time)
	
		if (words[0].contains("[a-zA-Z]+") == false) {
			//System.out.println("PASS Section 1 is in unix time");
		} else {
			System.out.println("FAIL Section 1 is NOT in unix time");
			return false;
		}
		// check part 2 is a float
		if (words[1].matches("[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?")) {
			//System.out.println("PASS Section 2 is type float");
		} else {
			System.out.println("FAIL Section 2 is NOT type float");
			return false;
		}
		// check part 3 is a string (no whitespaces)
		if (!words[2].contains(" ")) {
			//System.out.println("PASS Section 3 does not contain whitespaces");

		} else {
			System.out.println("FAIL Section 3 DOES contain whitespaces");
			System.out.println(words[2]);
			return false;
		}
		// check part 4 is a string, containing either url/default/export
		if (words[3].contains("url") || words[3].contains("default") || words[3].contains("export")) {
			//System.out.println("PASS Section 4 does contain either url/default/export)");
		} else {
			System.out.println("FAIL Section 4 DOES NOT contain either url/default/export)");
			return false;
		}
		return true;
	}

	protected void metaDataCollector(String c) {
		//if (c.contains("Processors")) {
			//String[] splitLine = c.split("\\s");
			//try {
				//maxProcCount = parseLongNumber((splitLine[splitLine.length - 1]));
			//} catch (NumberFormatException e) {
			//}
		//}
	}

	protected Job createJobFromLine(String d)
			throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {

		String splitOn = " ";

		String[] fragments = d.split(splitOn); //splits the strace line into its parts again by whitespace
		float aTime = Float.parseFloat(fragments[0]); // converts the job arrival time into a float
		float duration = Float.parseFloat(fragments[1]); // converts the job duration time into a float
		int aTimeRounded = Math.round(aTime); // rounds job arrival time 
		int durationRounded = Math.round(duration); // rounds job duration time 
		fragments[0] = Integer.toString(aTimeRounded); // loads the formatted job arrival time back into array as string
		fragments[1] = Integer.toString(durationRounded);  // loads the formatted job duration time back into array as string

		try {

			//specifying all the variables for the job constructor
			String id = fragments[2];
			long submit = Long.parseLong(fragments[0]);
			long queue = 0;
			long exec = Long.parseLong(fragments[1]);
			int nprocs = 1;
			double ppCpu = 1;
			long ppMem = 512000; // in kb
			String user = null;
			String group = null;
			String executable = fragments[3];
			Job preceding = null;
			long delayAfter = 0;
			
			// creating the new job using specified variables
			return jobCreator.newInstance(id, submit, queue, exec, nprocs, ppCpu, ppMem, user, group, executable, preceding, delayAfter);
			
		
			
		
			
			
		} catch (ArrayIndexOutOfBoundsException ex) {
			return null;
		}
		
	}

	
}