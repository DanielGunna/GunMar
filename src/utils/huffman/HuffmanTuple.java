package utils.huffman;

/**
 * A tuple class specifically for Huffman Nodes
 * @author Bob Nisco <BobNisco@gmail.com>
 */
public class HuffmanTuple {
	public char letter;
	public String representation;

	/**
	 * Constructor
	 * @param letter the letter
	 * @param representation the binary representation as a String
	 */
	public HuffmanTuple(char letter, String representation) {
		this.letter = letter;
		this.representation = representation;
	}

	/**
	 * Overridden toString method
	 * @return a String representation of this tuple
	 */
	@Override
	public String toString() {
		return this.letter + " => " + this.representation;
	}
}
