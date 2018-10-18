package org.cateproject.web.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ben
 *
 */
public final class UploadDto {

    private List<MultipartFile> files;

    private String delimiter;
	
    private String extension;
	
    private String datasetId;

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public String getDelimiter() {
	return delimiter;
    }
	
    public void setDelimiter(String delimiter) {
	this.delimiter = delimiter;
    }

    public String getExtension() {
	return extension;
    }

    public void setExtension(String extension) {
	this.extension = extension;
    }

    public String getDatasetId() {
 	return datasetId;
    }

    public void setDatasetId(String datasetId) {
	this.datasetId = datasetId;
    }
}
