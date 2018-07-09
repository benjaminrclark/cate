package org.cateproject.domain.batch;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.cateproject.domain.constants.DatasetType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Configurable
public class BatchDataset
{

	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	private DatasetType type;

	private String identifier;

        private boolean changeDumpManifestPresent;

	private String citation;

	private String creatorEmail;

	private String creatorName;

	private String description;

	private String homepage;

	private String logo;

	@Type(type = "dateTimeUserType")
	private DateTime published;

	private String publisherEmail;

	private String publisherName;

	private String rights;

	private String subject;

	private String title;

        @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
        private Set<BatchFile> files  = new HashSet<BatchFile>();

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return id;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * @return the citation
	 */
	public String getCitation()
	{
		return citation;
	}

	/**
	 * @param citation the citation to set
	 */
	public void setCitation(String citation)
	{
		this.citation = citation;
	}

	/**
	 * @return the creatorEmail
	 */
	public String getCreatorEmail()
	{
		return creatorEmail;
	}

	/**
	 * @param creatorEmail the creatorEmail to set
	 */
	public void setCreatorEmail(String creatorEmail)
	{
		this.creatorEmail = creatorEmail;
	}

	/**
	 * @return the creatorName
	 */
	public String getCreatorName()
	{
		return creatorName;
	}

	/**
	 * @param creatorName the creatorName to set
	 */
	public void setCreatorName(String creatorName)
	{
		this.creatorName = creatorName;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the hasChangeDumpManifest
	 */
	public boolean isChangeDumpManifestPresent() {
		return changeDumpManifestPresent;
	}

	/**
	 * @param hasChangeDumpManifest the hasChangeDumpManifest to set
	 */
	public void setChangeDumpManifestPresent(boolean changeDumpManifestPresent) {
		this.changeDumpManifestPresent = changeDumpManifestPresent;
	}

	/**
	 * @return the homepage
	 */
	public String getHomepage()
	{
		return homepage;
	}

	/**
	 * @param homepage the homepage to set
	 */
	public void setHomepage(String homepage)
	{
		this.homepage = homepage;
	}

	/**
	 * @return the logo
	 */
	public String getLogo()
	{
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo)
	{
		this.logo = logo;
	}

	/**
	 * @return the published
	 */
	public DateTime getPublished()
	{
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(DateTime published)
	{
		this.published = published;
	}

	/**
	 * @return the publisherEmail
	 */
	public String getPublisherEmail()
	{
		return publisherEmail;
	}

	/**
	 * @param publisherEmail the publisherEmail to set
	 */
	public void setPublisherEmail(String publisherEmail)
	{
		this.publisherEmail = publisherEmail;
	}

	/**
	 * @return the publisherName
	 */
	public String getPublisherName()
	{
		return publisherName;
	}

	/**
	 * @param publisherName the publisherName to set
	 */
	public void setPublisherName(String publisherName)
	{
		this.publisherName = publisherName;
	}

	/**
	 * @return the rights
	 */
	public String getRights()
	{
		return rights;
	}

	/**
	 * @param rights the rights to set
	 */
	public void setRights(String rights)
	{
		this.rights = rights;
	}

	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the extensions
	 */
	public Set<BatchFile> getFiles() {
		return files;
	}

	/**
	 * @param extensions the extensions to set
	 */
	public void setFiles(Set<BatchFile> files) {
		this.files = files;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setType(DatasetType type)
	{
		this.type = type;
	}

	public DatasetType getType()
	{
		return type;
	}

        @Transient
        public BatchFile getCore() {
            for(BatchFile file : files) {
                if(file.isCore()) {
                    return file;
                }   
            }
            return null;
        }

        @Transient
        public Set<BatchFile> getExtensions() {
            Set<BatchFile> extensions = new HashSet<BatchFile>();
            for(BatchFile file : files) {
                if(!file.isCore()) {
                    extensions.add(file);
                }
            }
            return extensions;
        }
}
