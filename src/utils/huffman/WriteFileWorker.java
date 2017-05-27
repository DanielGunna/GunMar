package utils.huffman;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An abstract class which deals with any IFileReaderWorkers who need
 * to deal with also writing to a file.
 * @author Bob Nisco <BobNisco@gmail.com>
 */
public abstract class WriteFileWorker implements IFileReaderWorker {
	private File file;
	protected FileOutputStream fileOutputStream;
	public String byteBuffer;
	public static final int NUM_OF_BITS_TO_WRITE = 8;

	/**
	 * Constructor that takes in the file path of the file to write to
	 * @param path the path to the file to write to
	 */
	public WriteFileWorker(String path) {
		this.file = new File(path);
		this.byteBuffer = "";
		this.initFile();
	}

	/**
	 * Handle all the initialization of the fileOutputStream and File
	 */
	private void initFile() {
		try {
			this.fileOutputStream = new FileOutputStream(this.file);
			// Ensure that the file exists before writing to it
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Internal handler for writing the data to the file
	 */
	public abstract void writeToFile();
}
