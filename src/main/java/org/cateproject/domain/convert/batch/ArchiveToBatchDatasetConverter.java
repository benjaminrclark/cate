package org.cateproject.domain.convert.batch;

import org.cateproject.domain.batch.BatchDataset;
import org.cateproject.domain.batch.BatchField;
import org.cateproject.domain.batch.BatchFile;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.metadata.BasicMetadata;
import org.gbif.metadata.MetadataException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class ArchiveToBatchDatasetConverter implements Converter<Archive, BatchDataset>
{
	private static final Logger logger = LoggerFactory.getLogger(ArchiveToBatchDatasetConverter.class);

	public BatchDataset convert(Archive archive)
	{
		BatchDataset batchDataset = new BatchDataset();
		try
		{
			BasicMetadata metadata = archive.getMetadata();
			batchDataset.setCitation(metadata.getCitationString());
			batchDataset.setCreatorEmail(metadata.getCreatorEmail());
			batchDataset.setCreatorName(metadata.getCreatorName());
			batchDataset.setDescription(metadata.getDescription());
			batchDataset.setHomepage(metadata.getHomepageUrl());
			batchDataset.setIdentifier(metadata.getIdentifier());
			batchDataset.setLogo(metadata.getLogoUrl());
			if (metadata.getPublished() != null)
			{
				batchDataset.setPublished(new DateTime(metadata.getPublished()));
			}
			batchDataset.setPublisherEmail(metadata.getPublisherEmail());
			batchDataset.setPublisherName(metadata.getPublisherName());
			batchDataset.setRights(metadata.getRights());
			batchDataset.setSubject(metadata.getSubject());
			batchDataset.setTitle(metadata.getTitle());
			ArchiveFile core = archive.getCore();
			batchDataset.getFiles().add(convertArchiveFile(core, batchDataset, true));
			for (ArchiveFile archiveFile : archive.getExtensions())
			{
				batchDataset.getFiles().add(convertArchiveFile(archiveFile, batchDataset, false));
			}
			return batchDataset;
		}
		catch (MetadataException me)
		{
			throw new IllegalArgumentException("Error parsing metadata", me);
		}
	}

	public BatchFile convertArchiveFile(ArchiveFile archiveFile, BatchDataset batchDataset, boolean isCore)
	{
		BatchFile batchFile = new BatchFile();
		batchFile.setDataset(batchDataset);
		batchFile.setCore(isCore);
		batchFile.setDateFormat(archiveFile.getDateFormat());
		batchFile.setEncoding(archiveFile.getEncoding());
		batchFile.setFieldsEnclosedBy(archiveFile.getFieldsEnclosedBy());
		batchFile.setFieldsTerminatedBy(archiveFile.getFieldsTerminatedBy());
		batchFile.setIgnoreHeaderLines(archiveFile.getIgnoreHeaderLines());
		batchFile.setLocation(archiveFile.getLocation());
		batchFile.setLinesTerminatedBy(archiveFile.getLinesTerminatedBy());
		batchFile.setRowType(archiveFile.getRowType());
                if(archiveFile.getId().getTerm() == null) {
                    archiveFile.getId().setTerm(DwcTerm.taxonID);
                }
		batchFile.getFields().add(convertArchiveField(archiveFile.getId(), batchFile, true));
		for (ArchiveField archiveField : archiveFile.getFieldsSorted())
		{
			batchFile.getFields().add(convertArchiveField(archiveField, batchFile, false));
		}
		logger.debug("Adding batch file {}", new Object[] {archiveFile.getLocation()});
		return batchFile;
	}

	public BatchField convertArchiveField(ArchiveField archiveField, BatchFile batchFile, boolean isId)
	{
                logger.debug("Field {} {} {}", new Object[]{archiveField.getIndex(), archiveField.getTerm(), archiveField.getDefaultValue()});
		BatchField batchField = new BatchField();
		batchField.setFile(batchFile);
                batchField.setIdField(isId);
		batchField.setIndex(archiveField.getIndex());
                if(archiveField.getTerm() != null) {
		    batchField.setTerm(archiveField.getTerm().qualifiedName());
                }
		batchField.setDefaultValue(archiveField.getDefaultValue());
		return batchField;
	}
}
