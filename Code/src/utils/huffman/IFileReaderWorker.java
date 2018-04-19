package utils.huffman;

/**
 * An interface for doing work while we're reading in a file
 * @author Bob Nisco <BobNisco@gmail.com>
 */
public interface IFileReaderWorker {
	/**
	 * A method that will be implemented in all classes that implement this interface.
	 * @param currentByte the current byte of the file being read in
	 */
	public void doWork(int currentByte);
}
