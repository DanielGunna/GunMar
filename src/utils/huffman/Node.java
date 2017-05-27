package utils.huffman;

/**
 * A huffman.Node class that will be used in the Huffman algorithm
 * @author Bob Nisco <BobNisco@gmail.com>
 */
public class Node implements Comparable<Node> {
	public Node left;
	public Node right;
	public int freq;
	public char letter;
	public final char INTERIOR_NODE_CHAR = (char) 0x01;

	/**
	 * No-args constructor
	 */
	public Node() {
	}

	/**
	 * Node constructor that only takes a frequency.
	 * Sets the letter to the INTERIOR_NODE_CHAR to denote that this is an interior node
	 * @param freq the frequency of this node
	 */
	public Node(int freq) {
		this.freq = freq;
		// We will use 0x01 ASCII code to denote these types of new nodes
		// Originally, it would have just been 0x00, but since we use that
		// for the EOF marker, we need to change the behavior.
		this.letter = INTERIOR_NODE_CHAR;
	}

	/**
	 * Node constructor
	 * @param letter the letter of this node
	 * @param freq the frequency of this node
	 */
	public Node(char letter, int freq) {
		this.left = null;
		this.right = null;
		this.freq = freq;
		this.letter = letter;
	}

	/**
	 * Overriden toString method
	 * @return a String representation of this node
	 */
	@Override
	public String toString() {
		return this.letter + " => " + this.freq;
	}

	/**
	 * Since we will be using these nodes in a Priority Queue, we will
	 * implement the Comparable class so they are properly sorted
	 * @param o the huffman.Node to compare to
	 * @return standard numeric representation for comparison
	 */
	@Override
	public int compareTo(Node o) {
		if (this.freq > o.freq) {
			return 1;
		} else if (this.freq < o.freq) {
			return -1;
		}
		return 0;
	}

	/**
	 * Overriden equals method so we can properly compare Nodes
	 * @param obj the object to compare to
	 * @return true if the nodes have the same frequency and letter,
	 *         false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		Node o = (Node) obj;
		return (this.freq == o.freq) && (this.letter == o.letter);
	}
}
