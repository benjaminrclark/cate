package org.cateproject.domain.batch;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.cateproject.domain.constants.DatasetType;

import org.junit.Before;
import org.junit.Test;

public class BatchDatasetTest {

        BatchDataset batchDataset;

        Set<BatchFile> batchFiles;

        Set<BatchFile> extensions;

        BatchFile core;
 
	@Before
	public void setUp() throws Exception {
            batchFiles = new HashSet<BatchFile>();
            extensions = new HashSet<BatchFile>();
            core = new BatchFile();
            core.setCore(true);
            batchFiles.add(core);

            BatchFile extension = new BatchFile();
            extension.setCore(false);
            batchFiles.add(extension);
            extensions.add(extension);

            batchDataset = new BatchDataset();
            batchDataset.setId(1L);
            batchDataset.setVersion(1);
            batchDataset.setType(DatasetType.DarwinCoreArchive);
            batchDataset.setIdentifier("identifier");
            batchDataset.setChangeDumpManifestPresent(false);
            batchDataset.setCitation("citation");
            batchDataset.setCreatorEmail("creatorEmail");
            batchDataset.setCreatorName("creatorName");
            batchDataset.setDescription("description");
            batchDataset.setHomepage("homepage");
            batchDataset.setLogo("logo");
            batchDataset.setPublished(new DateTime(2000, 1, 1, 1, 1, 1));
            batchDataset.setPublisherEmail("publisherEmail");
            batchDataset.setPublisherName("publisherName");
            batchDataset.setRights("rights");
            batchDataset.setSubject("subject");
            batchDataset.setTitle("title");
            batchDataset.setFiles(batchFiles);
        }

	@Test
	public void testGetId() {
		assertEquals("id should equal 1", new Long(1), batchDataset.getId());
	}

	@Test
	public void testGetVersion() {
		assertEquals("version should equal 1", new Integer(1), batchDataset.getVersion());
	}

	@Test
	public void testGetType() {
		assertEquals("type should equal 'DarwinCoreArchive'", DatasetType.DarwinCoreArchive, batchDataset.getType());
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'identifier'", "identifier", batchDataset.getIdentifier());
	}

	@Test
	public void testIsChangeDumpManifestPresent() {
		assertEquals("changeDumpManifestPresent should equal false", false, batchDataset.isChangeDumpManifestPresent());
	}

	@Test
	public void testGetCitation() {
		assertEquals("citation should equal 'citation'", "citation", batchDataset.getCitation());
	}

	@Test
	public void testGetCreatorEmail() {
		assertEquals("creatorEmail should equal 'creatorEmail'", "creatorEmail", batchDataset.getCreatorEmail());
	}

	@Test
	public void testGetCreatorName() {
		assertEquals("creatorName should equal 'creatorName'", "creatorName", batchDataset.getCreatorName());
	}

	@Test
	public void testGetDescription() {
		assertEquals("description should equal 'description'", "description", batchDataset.getDescription());
	}

	@Test
	public void testGetHomepage() {
		assertEquals("homepage should equal 'homepage'", "homepage", batchDataset.getHomepage());
	}

	@Test
	public void testGetLogo() {
		assertEquals("logo should equal 'logo'", "logo", batchDataset.getLogo());
	}

	@Test
	public void testGetPublished() {
		assertEquals("published should equal '2000-01-01T01:01:01'", new DateTime(2000, 1, 1, 1, 1, 1), batchDataset.getPublished());
	}

	@Test
	public void testGetPublisherEmail() {
		assertEquals("publisherEmail should equal 'publisherEmail'", "publisherEmail", batchDataset.getPublisherEmail());
	}

	@Test
	public void testGetPublisherName() {
		assertEquals("publisherName should equal 'publisherName'", "publisherName", batchDataset.getPublisherName());
	}

	@Test
	public void testGetRights() {
		assertEquals("rights should equal 'rights'", "rights", batchDataset.getRights());
	}

	@Test
	public void testGetSubject() {
		assertEquals("subject should equal 'subject'", "subject", batchDataset.getSubject());
	}

	@Test
	public void testGetTitle() {
		assertEquals("title should equal 'title'", "title", batchDataset.getTitle());
	}

	@Test
	public void testGetFiles() {
		assertEquals("files should equal batchFiles", batchFiles, batchDataset.getFiles());
	}

	@Test
	public void testGetCore() {
		assertEquals("getCore should equal core", core, batchDataset.getCore());
                batchDataset.setFiles(new HashSet<BatchFile>());
		assertNull("getCore should equal null", batchDataset.getCore());
	}

	@Test
	public void testGetExtensions() {
		assertEquals("getExtensions should equal extensions", extensions, batchDataset.getExtensions());
	}
}
