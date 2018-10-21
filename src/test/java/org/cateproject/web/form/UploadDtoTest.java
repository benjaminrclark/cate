package org.cateproject.web.form;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

public class UploadDtoTest {

    private UploadDto uploadDto;

    private List<MultipartFile> files;

    @Before
    public void setUp() {
        files = new ArrayList<MultipartFile>();
        uploadDto = new UploadDto();
        uploadDto.setDatasetId("datasetId");
        uploadDto.setDelimiter("delimiter");
        uploadDto.setExtension("extension");
        uploadDto.setFiles(files);
    }

    @Test
    public void testGetDatasetId() {
        assertEquals("datasetId should equal 'datasetId'", "datasetId", uploadDto.getDatasetId());
    }

    @Test
    public void testGetDelimiter() {
        assertEquals("delimiter should equal 'delimiter'", "delimiter", uploadDto.getDelimiter());
    }

    @Test
    public void testGetExtension() {
        assertEquals("extension should equal 'extension'", "extension", uploadDto.getExtension());
    }

    @Test
    public void testGetFiles() {
        assertEquals("files should equal files", files, uploadDto.getFiles());
    }
}
