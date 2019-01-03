import java.io.*;
import java.util.*;

/////////////////////////////////////////////////////////////////////////////
//Semester:         CS367 Fall 2017 
//PROJECT:          Running Median
//FILE:             MedianStream.Java
//
//TEAM:    43
//Authors: 
//Author1: (Neville Zi Feng NG, nzng@wisc.edu , nzng,lecture number 2)
//Author2: (Nawal Dua, ndua2@wisc.edu ,ndua2 ,lecture number 2)
//
//---------------- OTHER ASSISTANCE CREDITS 
//None
////////////////////////////80 columns wide //////////////////////////////////
/**
 * The class contains the main method. It also contains 3 other methods, one
 * that is the interactive mode where the user continuously enters numbers, 
 * the read from file mode, where the numbers are already in a file, and the
 * median is calculated continuously. The last method adds to the appropriate
 * priority queue and calculates the method.
 *
 * <p>Bugs: None that we know of.
 *
 * @author Nawal Dua, Neville NZF
 */


public class MedianStream {

	private static final String PROMPT_NEXT_VALUE = "Enter next value or q to quit: ";
	private static final String MEDIAN = "Current median: ";
	private static final String EXIT_MESSAGE = "That wasn't a double or 'q'. Goodbye!";
	private static final String FNF_MESSAGE = " not found.";

	/**
	 * Use this format to ensure that double values are formatted correctly. Double
	 * doubleValue = 1412.1221132 System.out.printf(DOUBLE_FORMAT, doubleValue);
	 */
	private static final String DOUBLE_FORMAT = "%8.3f\n";

	private Double currentMedian;
	private MaxPQ<Double> maxHeap;
	private MinPQ<Double> minHeap;

	/**
	 * Override Default Constructor
	 *
	 * Initialize the currentMedian = 0.0 Create a new MaxPQ and MinPQ.
	 */
	public MedianStream() {
		this.currentMedian = 0.0;
		this.maxHeap = new MaxPQ<Double>();
		this.minHeap = new MinPQ<Double>();
	}

	/**
	 * This method is called if the user passes NO command line arguments. The
	 * method prompts the user for a double value on each iteration.
	 *
	 * If the input received is a double, the current median is updated. After each
	 * iteration, print the new current median using MEDIAN string as declared and
	 * initialized with the data members above.
	 *
	 * If the input is the character 'q', return from the method.
	 *
	 * If the input is anything else, then you print an error using EXIT_MESSAGE
	 * string as declared and initialized with the data members above and then
	 * return from the method.
	 *
	 * For the purposes of calculating the median, every input received since the
	 * beginning of the method execution is part of the same stream.
	 */
	private static void runInteractiveMode() {
		// make new scanner
		Scanner scnr = new Scanner(System.in);
		// print out the prompt
		System.out.println(PROMPT_NEXT_VALUE);
		// create new instance of a median stream
		MedianStream ms = new MedianStream();

		// while the entered value is a double
		while (scnr.hasNextDouble()) {
			// set the entered double to a new variable
			double input = scnr.nextDouble();
			// set the current median to the newly calculated median
			ms.currentMedian = ms.getMedian(input);
			// print out the median statement with the median
			System.out.print(MEDIAN);
			System.out.printf(DOUBLE_FORMAT, ms.currentMedian);
			// print out the prompt again
			System.out.println(PROMPT_NEXT_VALUE);
		}
		// if scanner has next but it is not a double
		if (scnr.hasNext() && !scnr.hasNextDouble()) {
			// let string q be what the scanner had
			String q = scnr.next();
			// if what the scanner had the letter q
			if (q.equals("q")) {
				// close scanner
				scnr.close();
				// exit method
				return;
			}
			// if what the scanner was not a q
			else {
				// print exit message
				System.out.println(EXIT_MESSAGE);
				// close scanner
				scnr.close();
				// exit method
				return;

			}
		} 
		scnr.close();
	}

	/**
	 * This method is called if the user passes command line arguments. The method
	 * is called once for every filename passed by the user.
	 *
	 * The method reads values from the given file and writes the new median after
	 * reading each new double value to the output file.
	 *
	 * The name of the output file follows a format specified in the write-up.
	 *
	 * If the input file contains a non-double value, the program SHOULD NOT throw
	 * an exception, instead it should just read the values up to that point, write
	 * medians after each value up to that point and then return from the method.
	 *
	 * If a FileNotFoundException occurs, just print the string FNF_MESSAGE as
	 * declared and initialized with the data members above.
	 */
	private static void findMedianForFile(String filename) {

		try {
			// create a new instance of the input file
			File file = new File(filename);
			// remove ".txt" from the filename
			filename = filename.substring(0, filename.length()-4);
			// create a new instance of the output file
			File fw = new File(filename + "_out.txt");
			// create an instance of a printwriter for the output file
			PrintWriter pw = new PrintWriter(fw);
			// create an instance of a scanner for the input file
			Scanner scnr = new Scanner(file);
			// create new instance of a median stream
			MedianStream ms = new MedianStream();

			// while there are still doubles in the file
			while(scnr.hasNextDouble()) {
				// set the next double to the variable named input
				double input = scnr.nextDouble();
				// calculate the new median using the input and set it to the current median
				ms.currentMedian = ms.getMedian(input);
				// print out the new median on a new line
				pw.printf(DOUBLE_FORMAT, ms.currentMedian);
				pw.println();
			}
			pw.flush();
			pw.close();
			scnr.close();

		}
		catch (FileNotFoundException e) {
			// if file not found exception is thrown for the input file, print out
			// the FNF_MESSAGE
			System.out.println(filename + FNF_MESSAGE);
		}

	}

	/**
	 * YOU ARE NOT COMPULSORILY REQUIRED TO IMPLEMENT THIS METHOD.
	 *
	 * That said, we found it useful to implement.
	 *
	 * Adds the new temperature reading to the corresponding maxPQ or minPQ
	 * depending upon the current state.
	 *
	 * Then calculates and returns the updated median.
	 *
	 * @param newReading
	 *            - the new reading to be added.
	 * @return the updated median.
	 */
	private Double getMedian(Double newReading) {

		double median = 0;

		// if the new reading is larger than the current median, we need to add minPQ
		if (newReading > currentMedian) {

			// if the minPQ is larger than the maxPQ
			if (minHeap.size() > maxHeap.size()) {
				// insert the new reading into the minPQ
				minHeap.insert(newReading);
				// now that the minPQ is larger than maxPQ by 2, move the max priority into maxPQ
				maxHeap.insert(minHeap.getMax());
				// remove max
				minHeap.removeMax();
				// now that the PQs are equal in length, set median to the new median (the average
				// of the getmax of each pq)
				median = (minHeap.getMax()+maxHeap.getMax())/2;	
				return median;
			}
			// if the minPQ is equal to the maxPQ in size
			else if (minHeap.size() == maxHeap.size()) {
				// insert the new reading into minPQ
				minHeap.insert(newReading);
				// set the median to the getmax of the minPQ
				median = minHeap.getMax();
				return median;
			}
			// if the minPQ is smaller than the maxPQ
			else if (minHeap.size() < maxHeap.size()) {
				// insert the new reading to the minPQ
				minHeap.insert(newReading);
				// now that the PQs are equal in length, set median to the new median (the average
				// of the getmax of each pq)
				median = (minHeap.getMax()+maxHeap.getMax())/2;	
				return median;
			}
			else return median;

		}
		// if the new reading is smaller than the current median, we need to add maxPQ
		else if (newReading < currentMedian) {
			// if the minPQ is larger than the maxPQ
			if (minHeap.size() > maxHeap.size()) {
				// insert the new reading into maxPQ
				maxHeap.insert(newReading);
				// now that the PQs are equal in length, set median to the new median (the average
				// of the getmax of each pq)
				median = (minHeap.getMax()+maxHeap.getMax())/2;	
				return median;
			}
			// if the minPQ is equal in size to the maxPQ
			else if (minHeap.size() == maxHeap.size()) {
				// insert the new reading into the maxPQ
				maxHeap.insert(newReading);
				// set the median to the getMax of the maxPQ
				median = maxHeap.getMax();
				return median;
			}
			// if the minPQ is smaller than the maxPQ
			else if (minHeap.size() < maxHeap.size()) {
				// insert the new reading into the maxPQ
				maxHeap.insert(newReading);
				// since maxPQ is larger now
				minHeap.insert(maxHeap.getMax());
				// remove max too
				maxHeap.removeMax();
				// now that the PQs are equal in length, set median to the new median (the average
				// of the getmax of each pq)
				median = (minHeap.getMax()+maxHeap.getMax())/2;	
				return median;
			}
			else return median;
		}
		// if the new reading is equal to current median
		else if(newReading == currentMedian) {

			// if the minPQ is larger than the maxPQ
			if (minHeap.size() > maxHeap.size()) {
				// insert the new reading into the maxPQ
				maxHeap.insert(newReading);
				return newReading;
			}
			// if minPQ is equal to the maxPQ in size
			else if (minHeap.size() == maxHeap.size()) {
				// insert the new reading into the maxPQ
				maxHeap.insert(newReading);
				return newReading;
			}
			// if the minPQ is smaller than the maxPQ
			else if (minHeap.size() < maxHeap.size()) {
				// insert the new reading into the minPQ
				minHeap.insert(newReading);
				return newReading;
			}
			else return median;
		}
		else {
			return median;
		}

	}

	// DO NOT EDIT THE main METHOD.
	public static void main(String[] args) {
		// Check if files have been passed in the command line.
		// If no files are passed, run an infinite interactive loop taking a double
		// input each time until "q" is entered by the user.
		// After each iteration of the loop, update and display the new median.
		if (args.length == 0) {
			runInteractiveMode();
		}

		// If files are passed in the command line, open each file.
		// For each file, iterate over all the double values in the file.
		// After reading each new double value, write the new median to the
		// corresponding output file whose name will be inputFilename_out.txt
		// Stop reading the file at the moment a non-double value is detected.
		else {
			for (int i = 0; i < args.length; i++) {
				findMedianForFile(args[i]);
			}
		}
	}
}
