package org.cateproject.domain.sync;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

@Embeddable
public class ChangeManifestChange {

    @Enumerated
    private ChangeManifestChangeType change; 

    private String hash;

    private Integer length;

    private String type;

    private String path;

    /**
     * @return the change
     */
    public ChangeManifestChangeType getChange() {
	return change;
    }

    /**
     * @param change the change to set
     */
    public void setChange(ChangeManifestChangeType change) {
	this.change = change;
    }

    /**
     * @return the hash
     */
    public String getHash() {
	return hash;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(String hash) {
	this.hash = hash;
    }

    /**
     * @return the length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
}
