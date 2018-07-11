package org.cateproject.domain.batch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.cateproject.domain.Base;
import org.cateproject.domain.sync.ChangeManifestUrl;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Configurable
public class BatchLine implements Comparable<BatchLine> {

    @Id
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    private Integer lineNumber;

    @Lob
    private String line;

    @ManyToOne
    @NotNull
    private BatchFile file;

    @Transient 
    private transient Base entity;

    private Integer numberOfColumns;

    @ManyToOne
    private ChangeManifestUrl changeManifestUrl;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the lineNumber
     */
    public Integer getLineNumber() {
        return lineNumber;
    }

    /**
     * @param lineNumber the lineNumber to set
     */
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @return the line
     */
    public String getLine() {
        return line;
    }

    /**
     * @param line the line to set
     */
    public void setLine(String line) {
        this.line = line;
    }

    /**
     * @return the file
     */
    public BatchFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(BatchFile file) {
        this.file = file;
    }

    /**
     * @return the entity
     */
    public Base getEntity() {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(Base entity) {
        this.entity = entity;
    }

    /**
     * @return the numberOfColumns
     */
    public Integer getNumberOfColumns() {
        return numberOfColumns;
    }

    /**
     * @param numberOfColumns the numberOfColumns to set
     */
    public void setNumberOfColumns(Integer numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    /**
     * @param changeManifestUrl the changeManifestUrl to set
     */   
    public void setChangeManifestUrl(ChangeManifestUrl changeManifestUrl) {
        this.changeManifestUrl = changeManifestUrl;
    }

    public ChangeManifestUrl getChangeManifestUrl() {
        return this.changeManifestUrl;
    }

    public int compareTo(BatchLine b) {
        return this.lineNumber.compareTo(b.lineNumber);
    }
}
