package utils.huffman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to handle writing to the file while simultaneously reading in from a file
 * with a buffer so that we can take the input, convert it into our Huffman Encoding
 * and write it.
 * @author Bob Nisco <BobNisco@gmail.com>
 */
public class WriteEncodedFileWorker extends WriteFileWorker {

	public Map<Character, String> map;

	/**
	 * Constructor
	 * @param path output path
	 * @param encodings the encodings
	 */
	public WriteEncodedFileWorker(String path, ArrayList<HuffmanTuple> encodings) {
		super(path);
		this.map = new HashMap<>();
		this.initMap(encodings);
	}


	/**
	 * Initializes the internal map with the encodings that were passed in
	 * @param encodings the ArrayList of HuffmanTuples that will be converted
	 *                  into a map for quicker lookup times
	 */
	private void initMap(ArrayList<HuffmanTuple> encodings) {
		for (HuffmanTuple tuple : encodings) {
			map.put(tuple.letter, tuple.representation);
		}
	}

	/**
	 * Handles writing the beginning part of the file
	 * @param headerString the header string
	 */
	protected void writeBeginningOfFile(String headerString) {
		try {
			for (int i = 0; i < headerString.length(); i += 2) {
				int intRep = Integer.parseInt(headerString.substring(i, i + 2), 16);
				String bin = Integer.toBinaryString(intRep);
				String paddedBin = Huffman.rightPadString(bin, NUM_OF_BITS_TO_WRITE);
				byteBuffer += paddedBin;
				this.writeToFile();
			}
		}catch (StringIndexOutOfBoundsException e) {
			System.out.println("Index out of bounds ");
		}
	}

	/**
	 * Writes the remaining bits and the end of file representation
	 */
	protected void writeEndOfFile() {
		byteBuffer += map.get((char) 0x00);
		// Properly pad the
		if (byteBuffer.length() != 8) {
			// determine how many bytes we'll pad by
			int howManyBytes = byteBuffer.length() / 8;
			byteBuffer = Huffman.leftPadString(byteBuffer, NUM_OF_BITS_TO_WRITE * (howManyBytes + 1));
		}
		this.writeToFile();
	}

	/**
	 * Internal handler for writing the data to the file
	 */
	public void writeToFile() {
		try {
			while (byteBuffer.length() >= NUM_OF_BITS_TO_WRITE) {
				int i = Integer.parseInt(byteBuffer.substring(0, NUM_OF_BITS_TO_WRITE), 2);
				fileOutputStream.write(i);
				byteBuffer = byteBuffer.substring(NUM_OF_BITS_TO_WRITE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Overridden doWork method. Takes the current byte, finds the character it represents
	 * in our map, and then writes the representation to a file
	 * @param currentByte the current byte from the input file
	 */
	@Override
	public void doWork(int currentByte) {
		byteBuffer += map.get((char) currentByte);
		this.writeToFile();
	}
}
