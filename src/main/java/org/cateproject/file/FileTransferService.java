package org.cateproject.file;

import java.io.File;
import java.io.IOException;

public interface FileTransferService {
	
	String copyFileOut(File from, String to) throws IOException;
	
	void copyFileIn(String from, File to) throws IOException;

	void copyDirectoryIn(String from, File to) throws IOException;
	
	void copyDirectoryOut(File from, String to) throws IOException;
	
	boolean delete(String file) throws IOException;

	boolean exists(String string) throws IOException;

}
