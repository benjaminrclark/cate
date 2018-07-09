package org.cateproject.domain.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.MultimediaFileType;
import org.junit.Before;
import org.junit.Test;

public class MultimediaFileTest {
	
	private MultimediaFile multimediaFile;

        private File file;

        private Multimedia multimedia;
	
	@Before
	public void setUp() throws Exception {
                multimedia = new Multimedia();
                multimedia.setIdentifier("IDENTIFIER");
                file = new File("test.jpg");
		multimediaFile = new MultimediaFile(multimedia, file, MultimediaFileType.original);
		multimediaFile.setMultimedia(multimedia);
		multimediaFile.setFile(file);
		multimediaFile.setType(MultimediaFileType.original);
	}

	@Test
	public void testGetMultimedia() {
		assertEquals("multimedia should equal multimedia", multimedia, multimediaFile.getMultimedia());
	}

	@Test
	public void testGetFile() {
		assertEquals("file should equal file", file, multimediaFile.getFile());
	}

	@Test
	public void testGetType() {
		assertEquals("type should equal 'original'", MultimediaFileType.original, multimediaFile.getType());
	}

}
