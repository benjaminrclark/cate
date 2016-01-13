package org.cateproject.multitenant.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventType;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class MultitenantAwareSolrServerTest {

    private Path tempDir = null;
 
    private MultitenantAwareSolrServer multitenantAwareSolrServer;

    private SolrServer solrServer;

    private CoreContainer coreContainer;

    private MultitenantRepository multitenantRepository;

    @Before
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("multitenant-solr");
        multitenantAwareSolrServer = new MultitenantAwareSolrServer();
        multitenantAwareSolrServer.setServerClass(EmbeddedSolrServer.class);
        multitenantAwareSolrServer.setSolrHomeString(tempDir.toFile().getAbsolutePath());
        solrServer = EasyMock.createMock(SolrServer.class);
        multitenantRepository = EasyMock.createMock(MultitenantRepository.class);
        coreContainer = EasyMock.createMock(CoreContainer.class);

        multitenantAwareSolrServer.setSolrServer(solrServer);
        multitenantAwareSolrServer.setCoreContainer(coreContainer);
        multitenantAwareSolrServer.setMultitenantRepository(multitenantRepository);
    }

    @After
    public void tearDown() {
       tempDir.toFile().delete();
    }

    @Test
    public void testExtractFromClasspathToFile() throws IOException {
        ClassPathResource test1 = new ClassPathResource("org/cateproject/multitenant/solr/solrhome/test1.txt");
        File test1Destination = new File(tempDir.toFile(),"test1.txt");
        FileUtils.copyURLToFile(test1.getURL(), test1Destination);
        assertEquals("extractFromClasspathToFile should return the number of files copied", 3, multitenantAwareSolrServer.extractFromClasspathToFile("org/cateproject/multitenant/solr/solrhome/",tempDir.toFile()));
    }

    @Test(expected = IOException.class)
    public void testExtractFromClasspathToFileNoFiles() throws IOException {
        multitenantAwareSolrServer.extractFromClasspathToFile("org/cateproject/multitenant/solr/empty/",tempDir.toFile());
        
    }

    @Test
    public void testExtractFromClasspathToFileNoChanges() throws IOException {
        ClassPathResource test1 = new ClassPathResource("org/cateproject/multitenant/solr/solrhome/test1.txt");
        ClassPathResource test2 = new ClassPathResource("org/cateproject/multitenant/solr/solrhome/test2.txt");
        ClassPathResource test3 = new ClassPathResource("org/cateproject/multitenant/solr/solrhome/test3.txt");
        ClassPathResource test4 = new ClassPathResource("org/cateproject/multitenant/solr/solrhome/test4.txt");


        File test1Destination = new File(tempDir.toFile(),"test1.txt");
        File test2Destination = new File(tempDir.toFile(),"test2.txt");
        File test3Destination = new File(tempDir.toFile(),"test3.txt");
        File test4Destination = new File(tempDir.toFile(),"test4.txt");
        FileUtils.copyURLToFile(test1.getURL(), test1Destination);
        FileUtils.copyURLToFile(test2.getURL(), test2Destination);
        FileUtils.copyURLToFile(test3.getURL(), test3Destination);
        FileUtils.copyURLToFile(test4.getURL(), test4Destination);
        assertEquals("extractFromClasspathToFile should return the number of files copied", 0, multitenantAwareSolrServer.extractFromClasspathToFile("org/cateproject/multitenant/solr/solrhome/",tempDir.toFile()));
        
    }

    @Test
    public void testInitializeCoreContainer() throws IOException {
        multitenantAwareSolrServer.initialize();
    }

    
    @Test
    public void testInitializeCoreContainerSolrHomeDirectoryAbsent() throws IOException {
        tempDir.toFile().delete();
        multitenantAwareSolrServer.initialize();
    }


    @Test
    public void testInitializeCoreContainerSolrHomePresent() throws IOException {
        multitenantAwareSolrServer.extractFromClasspathToFile("solr/",tempDir.toFile());
        multitenantAwareSolrServer.initialize();
    }

    @Test
    public void testInitializeHttpSolrServer() throws MalformedURLException, IOException {
        multitenantAwareSolrServer.setServerClass(HttpSolrServer.class);
        multitenantAwareSolrServer.setServerUrl(new URL("http://localhost:1234"));
        multitenantAwareSolrServer.initialize();
    }

    @Test
    public void testCreateCore() throws IOException, SolrServerException {
        Multitenant multitenant = new Multitenant();
        multitenant.setIdentifier("test");
        multitenantAwareSolrServer.initialize();
        multitenantAwareSolrServer.createCore(multitenant);
    }

    @Test
    public void testCreateCoreCoreAlreadyExists() throws IOException, SolrServerException {
        Multitenant multitenant = new Multitenant();
        multitenant.setIdentifier("collection1");
        multitenantAwareSolrServer.initialize();
        multitenantAwareSolrServer.createCore(multitenant);
    }

    @Test
    public void testInitializeCore() throws IOException {
        Multitenant multitenant = new Multitenant();
        multitenant.setIdentifier("test");
        multitenantAwareSolrServer.initialize(); 
        EasyMock.replay(solrServer, coreContainer);
        multitenantAwareSolrServer.initializeCore(multitenant);
        EasyMock.verify(solrServer, coreContainer);
        assertEquals("solrServer.solrServers should contain a new server 'test'", multitenantAwareSolrServer.getSolrServers().get("test").getClass(), EmbeddedSolrServer.class);
    }

    @Test
    public void testInitializeCoreHttpSolrServer() throws IOException {
        Multitenant multitenant = new Multitenant();
        multitenant.setIdentifier("test.localhost.org");
        multitenantAwareSolrServer.setServerClass(HttpSolrServer.class);
        multitenantAwareSolrServer.initialize(); 
        EasyMock.replay(solrServer, coreContainer);
        multitenantAwareSolrServer.initializeCore(multitenant);
        EasyMock.verify(solrServer, coreContainer);
        assertEquals("solrServer.solrServers should contain a new server 'test.localhost.org'", multitenantAwareSolrServer.getSolrServers().get("test.localhost.org").getClass(), HttpSolrServer.class);
    }


    @Test
    public void testAfterPropertiesSet() throws Exception {
        Multitenant multitenant = new Multitenant();
        multitenant.setIdentifier("test.localhost.org");
        List<Multitenant> multitenants = new ArrayList<Multitenant>();
        multitenants.add(multitenant);
        multitenantAwareSolrServer.setServerClass(HttpSolrServer.class);
        EasyMock.expect(multitenantRepository.findAll()).andReturn(multitenants);

        EasyMock.replay(solrServer, coreContainer, multitenantRepository);
        multitenantAwareSolrServer.afterPropertiesSet();
        EasyMock.verify(solrServer, coreContainer, multitenantRepository);
        assertEquals("solrServer.solrServers should contain a server 'test.localhost.org'", multitenantAwareSolrServer.getSolrServers().get("test.localhost.org").getClass(), HttpSolrServer.class);
    }

    @Test
    public void testNotifyCreate() throws IOException {
        Multitenant multitenant = new Multitenant();
        multitenant.setIdentifier("test.localhost.org");
        MultitenantEvent multitenantEvent = new MultitenantEvent();
        multitenantEvent.setIdentifier("test.localhost.org");
        multitenantEvent.setType(MultitenantEventType.CREATE); 

        multitenantAwareSolrServer.setServerClass(HttpSolrServer.class);
        multitenantAwareSolrServer.initialize();
        EasyMock.expect(multitenantRepository.findByIdentifier(EasyMock.eq("test.localhost.org"))).andReturn(multitenant);


        EasyMock.replay(solrServer, coreContainer, multitenantRepository);
        multitenantAwareSolrServer.notify(multitenantEvent);
        EasyMock.verify(solrServer, coreContainer, multitenantRepository);
        assertEquals("solrServer.solrServers should contain a server 'test.localhost.org'", multitenantAwareSolrServer.getSolrServers().get("test.localhost.org").getClass(), HttpSolrServer.class);
    }

    @Test
    public void testNotifyDefault() throws IOException {
        Multitenant multitenant = new Multitenant();
        multitenant.setIdentifier("test.localhost.org");
        MultitenantEvent multitenantEvent = new MultitenantEvent();
        multitenantEvent.setIdentifier("test.localhost.org");
        multitenantEvent.setType(MultitenantEventType.OTHER); 

        multitenantAwareSolrServer.setServerClass(HttpSolrServer.class);
        multitenantAwareSolrServer.initialize();

        EasyMock.replay(solrServer, coreContainer, multitenantRepository);
        multitenantAwareSolrServer.notify(multitenantEvent);
        EasyMock.verify(solrServer, coreContainer, multitenantRepository);
        assertNull("solrServer.solrServers should not contain a server 'test.localhost.org'", multitenantAwareSolrServer.getSolrServers().get("test.localhost.org"));
    }

    @Test
    public void testHandleCreate() throws Exception {
        Multitenant multitenant = new Multitenant();
        multitenant.setIdentifier("test.localhost.org");
        MultitenantEvent multitenantEvent = new MultitenantEvent();
        multitenantEvent.setIdentifier("test.localhost.org");
        multitenantEvent.setType(MultitenantEventType.CREATE); 
        SolrException so = new HttpSolrServer.RemoteSolrException(404, "No such core: test.localhost.org", new Exception());

        NamedList<Object> cores = new NamedList<Object>();
        NamedList<Object> response = new NamedList<Object>();
        response.add("status", cores);
        EasyMock.expect(multitenantRepository.findByIdentifier(EasyMock.eq("test.localhost.org"))).andReturn(multitenant);
        EasyMock.expect(coreContainer.getCore(EasyMock.eq("test.localhost.org"))).andThrow(so);
        EasyMock.expect(solrServer.request(EasyMock.isA(CoreAdminRequest.Create.class))).andReturn(response);
        EasyMock.expect(solrServer.request(EasyMock.isA(CoreAdminRequest.Persist.class))).andReturn(response);

        EasyMock.replay(solrServer, coreContainer, multitenantRepository);
        multitenantAwareSolrServer.handle(multitenantEvent);
        EasyMock.verify(solrServer, coreContainer, multitenantRepository);
    }

    @Test
    public void testHandleDefault() {
        Multitenant multitenant = new Multitenant();
        multitenant.setIdentifier("test.localhost.org");
        MultitenantEvent multitenantEvent = new MultitenantEvent();
        multitenantEvent.setIdentifier("test.localhost.org");
        multitenantEvent.setType(MultitenantEventType.OTHER); 
        multitenantAwareSolrServer.setServerClass(HttpSolrServer.class);

        EasyMock.replay(solrServer, coreContainer, multitenantRepository);
        multitenantAwareSolrServer.handle(multitenantEvent);
        EasyMock.verify(solrServer, coreContainer, multitenantRepository);
    }

    @Test
    public void testRequest() throws SolrServerException, IOException {
        SolrServer tenantSolrServer = EasyMock.createMock(SolrServer.class);
        QueryRequest request = new QueryRequest();
        NamedList<Object> response = new NamedList<Object>();
        multitenantAwareSolrServer.getSolrServers().put("TENANT_ID",tenantSolrServer);
        EasyMock.expect(tenantSolrServer.request(EasyMock.eq(request))).andReturn(response);

        MultitenantContextHolder.getContext().setTenantId("TENANT_ID");

        EasyMock.replay(solrServer, coreContainer, multitenantRepository, tenantSolrServer);
        assertEquals("request should return the expected response",multitenantAwareSolrServer.request(request), response);
        EasyMock.verify(solrServer, coreContainer, multitenantRepository, tenantSolrServer);
    }

    @Test()
    public void testRequestTenantNotPresent() throws SolrServerException, IOException {
        QueryRequest request = new QueryRequest();
        NamedList<Object> response = new NamedList<Object>();
        EasyMock.expect(solrServer.request(EasyMock.eq(request))).andReturn(response);

        MultitenantContextHolder.getContext().setTenantId("TENANT_ID");

        EasyMock.replay(solrServer, coreContainer, multitenantRepository);
        assertEquals("request should return the expected response",multitenantAwareSolrServer.request(request), response);
        EasyMock.verify(solrServer, coreContainer, multitenantRepository);
    }

    @Test
    public void testShutdown() {
        SolrServer tenantSolrServer = EasyMock.createMock(SolrServer.class);
        multitenantAwareSolrServer.getSolrServers().put("TENANT_ID",tenantSolrServer);
        tenantSolrServer.shutdown();
        solrServer.shutdown();

        EasyMock.replay(solrServer, coreContainer, multitenantRepository, tenantSolrServer);
        multitenantAwareSolrServer.shutdown();
        EasyMock.verify(solrServer, coreContainer, multitenantRepository, tenantSolrServer);
    }

    @Test
    public void testShutdownNotInitialized() {
        multitenantAwareSolrServer.setSolrServer(null);

        EasyMock.replay(solrServer, coreContainer, multitenantRepository);
        multitenantAwareSolrServer.shutdown();
        EasyMock.verify(solrServer, coreContainer, multitenantRepository);
    }

    @Test
    public void testDestroy() {
        SolrServer tenantSolrServer = EasyMock.createMock(SolrServer.class);
        multitenantAwareSolrServer.getSolrServers().put("TENANT_ID",tenantSolrServer);
        tenantSolrServer.shutdown();
        solrServer.shutdown();

        EasyMock.replay(solrServer, coreContainer, multitenantRepository, tenantSolrServer);
        multitenantAwareSolrServer.destroy();
        EasyMock.verify(solrServer, coreContainer, multitenantRepository, tenantSolrServer);
    }
}
