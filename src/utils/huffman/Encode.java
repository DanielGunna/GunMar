package utils.huffman;

import java.util.ArrayList;
import java.util.Map;

/**
 * An huffman.Encode class for a canonical Huffman Tree
 * @author Bob Nisco <BobNisco@gmail.com>
 */
public class Encode {

	private String sourceFilePath;
	private String targetFilePath;

	/**
	 * Constructor that takes in the file path to the source and target file
	 * @param sourceFilePath input file path
	 * @param targetFilePath output file path
	 */
	public Encode(String sourceFilePath, String targetFilePath) {
		this.sourceFilePath = sourceFilePath;
		this.targetFilePath = targetFilePath;
	}

	/**
	 * The runner function to perform the encoding
	 */
	public  void performEncode() {
		System.out.println("==== Encoding File Begin ====");
		// 1. Create the character mapping by reading in from source file
		Map<Character, Integer> map = Huffman.createMapFromFile(this.sourceFilePath);
		// 2. Create the Huffman Tree
		Node rootNode = Huffman.huffman(map);
		// 3. Canonize the Huffman Tree
		ArrayList<HuffmanTuple> encodings = Huffman.canonizeHuffmanTree(rootNode);
		System.out.println("==== Canonized Tree Encodings ====");
		System.out.println(encodings);
		// 4. Write the file based on the encoding
		System.out.println("=== Writing Encoded File to " + this.targetFilePath + " ====");
		Huffman.writeEncodedFile(this.sourceFilePath, this.targetFilePath, encodings);
		System.out.println("==== Encoding File Finish ====");
	}

	/**
	 * A main method for huffman.Encode
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Encode encode;

		if (args.length == 2) {
			encode = new Encode(args[0], args[1]);
		} else {
			// Sample data for ease of use when running while developing
			encode = new Encode("samples/input/sample2.txt", "samples/output/sample2.huf");
		}

		encode.performEncode();
	}
}
