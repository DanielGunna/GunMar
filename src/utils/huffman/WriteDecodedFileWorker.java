package utils.huffman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Our worker class to handle reading in an encoded file and outputting
 * a decoded version of that file.
 * @author Bob Nisco <BobNisco@gmail.com>
 */
public class WriteDecodedFileWorker extends WriteFileWorker {

	private Map<String, Character> map;
	private Map<Character, Integer> frequencyMap;
	private char charToWrite;
	private int numberOfChars;
	private int currentCodeIndex;
	private boolean readChar;
	private char inputChar;
	private enum State {
		FIRST_BYTE, DICTIONARY, ENCODED_FILE, FINISHED
	};
	private State currentState;

	/**
	 * Constructor for our write decoded file worker
	 * @param path the output path
	 */
	public WriteDecodedFileWorker(String path) {
		super(path);
		this.charToWrite = (char) 0x00;
		this.numberOfChars = 0;
		this.currentCodeIndex = 0;
		this.readChar = false;
		this.frequencyMap = new HashMap<>();
		this.currentState = State.FIRST_BYTE;
	}

	/**
	 * Internal handler for writing the data to the file
	 */
	public void writeToFile() {
		try {
			fileOutputStream.write(this.charToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Internal handler for reading in the dictionary from the file
	 * @param currentByte the current byte being read in from the file
	 */
	private void handleReadDictionary(int currentByte) {
		// Build the frequency map
		char currentChar = (char) currentByte;
		if (readChar) {
			// If we are reading in a character, place it in the map
			frequencyMap.put(currentChar, 0);
			// Hold onto the current character
			this.inputChar = currentChar;
			// Set the bool flag so we know to read in the frequency on next byte
			this.readChar = false;
		} else {
			// Read in the frequency and put it into the map
			frequencyMap.put(this.inputChar, currentByte);
			// Read next char on next byte
			this.readChar = true;
			// Increase our counter
			currentCodeIndex++;
		}

		// Check to see if we have read in all the characters
		if (this.currentCodeIndex >= this.numberOfChars) {
			// Convert the frequency map into some tuples
			ArrayList<HuffmanTuple> tuples = Decode.convertMapToTuples(frequencyMap);
			Huffman.sortHuffmanTuples(tuples);
			// Canonize the tuples
			Huffman.canonizeEncodings(tuples);
			// Put them into a hashmap for quick lookup
			this.map = Decode.convertTuplesToLookupMap(tuples);
			System.out.println("==== Decoded Canonized Huffman Encodings ====");
			System.out.println(this.map);
			// Move onto decoding the encoded file state
			this.currentState = State.ENCODED_FILE;
		}
	}

	/**
	 * Internal handler for dealing with the first byte from the file
	 * @param currentByte the current byte being read in from the file
	 */
	private void handleReadFirstByte(int currentByte) {
		// We're on the first byte which tells us how many chars there are
		// So let's initialize things to get ready to decode the dictionary
		this.numberOfChars = currentByte;
		this.readChar = true;
		// Move onto the Dictionary
		this.currentState = State.DICTIONARY;
	}

	/**
	 * Internal handler for trying to decode each bit
	 * @param currentByte the current byte being read in from the file
	 */
	private void handleDecodeByByte(int currentByte) {
		// Add the current byte into the buffer
		this.byteBuffer += Huffman.rightPadString(Integer.toBinaryString(currentByte), NUM_OF_BITS_TO_WRITE);
		int currentLength = 1;

		while (true) {
			try {
				String current = this.byteBuffer.substring(0, currentLength);
				Character possibility = this.map.get(current);
				if (possibility != null) {
					if (possibility == '\u0000') {
						this.currentState = State.FINISHED;
						break;
					}
					this.byteBuffer = this.byteBuffer.substring(currentLength);
					this.charToWrite = possibility;
					this.writeToFile();
					currentLength = 1;
				} else {
					currentLength++;
				}
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
	}

	/**
	 * Overriden doWork method. Takes in the current byte from the read in file,
	 * reads in the binary data, converts to chars and writes to the output file
	 * @param currentByte the current byte from the input file
	 */
	@Override
	public void doWork(int currentByte) {
		if (this.currentState == State.FINISHED) {
			return;
		} else if (this.currentState == State.FIRST_BYTE) {
			this.handleReadFirstByte(currentByte);
		} else if (this.currentState == State.DICTIONARY) {
			this.handleReadDictionary(currentByte);
		} else if (this.currentState == State.ENCODED_FILE) {
			this.handleDecodeByByte(currentByte);
		}
	}
}
