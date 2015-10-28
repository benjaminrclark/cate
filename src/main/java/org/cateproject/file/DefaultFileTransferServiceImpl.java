package org.cateproject.file;

import java.io.File;
import java.io.IOException;

public class DefaultFileTransferServiceImpl implements FileTransferService {

	public String copyFileOut(File from, String to) throws IOException {
            return null;
        }
	
	public void copyFileIn(String from, File to) throws IOException {

        }

	public void copyDirectoryIn(String from, File to) throws IOException {

        }
	
	public void copyDirectoryOut(File from, String to) throws IOException {

        }
	
	public boolean delete(String file) throws IOException {
            return false;
        }

	public boolean exists(String string) throws IOException {
            return false;
        }
}
