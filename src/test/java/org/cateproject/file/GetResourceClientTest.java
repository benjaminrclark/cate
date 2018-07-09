package org.cateproject.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.batch.test.AssertFile.assertFileEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

public class GetResourceClientTest {
   
    private HttpClient httpClient;
 
    private GetResourceClient getResourceClient;

    @Before
    public void setUp() {
        getResourceClient = new GetResourceClient();
        httpClient = EasyMock.createMock(HttpClient.class);
        getResourceClient.setHttpClient(httpClient);
    }

    @Test
    public void testIssueRequestHeadOK() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BasicHttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("http",1,0),200,"REASON");

        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpHead.class))).andReturn(httpResponse);

        EasyMock.replay(httpClient);
        assertEquals("Expect issueResponse to return a httpResponse",getResourceClient.issueRequest("RESOURCE", byteArrayOutputStream, new DateTime(2000,1,1,1,1), HttpMethod.HEAD), httpResponse);
        assertEquals("Expect the returned response to be ''", "", byteArrayOutputStream.toString("UTF-8"));       
        EasyMock.verify(httpClient);
    }

    @Test
    public void testIssueRequestGetOK() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("BODY".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BasicHttpEntity httpEntity = new BasicHttpEntity();
        httpEntity.setContent(inputStream);
        BasicHttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("http",1,0),200,"REASON");
        httpResponse.setEntity(httpEntity);

        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andReturn(httpResponse);

        EasyMock.replay(httpClient);
        assertEquals("Expect issueResponse to return a httpResponse",getResourceClient.issueRequest("RESOURCE", byteArrayOutputStream, null, HttpMethod.GET), httpResponse);
        assertEquals("Expect the returned response to be 'BODY'", "BODY", byteArrayOutputStream.toString("UTF-8"));       
        EasyMock.verify(httpClient);
    }

    @Test
    public void testIssueRequestGetNullEntity() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BasicHttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("http",1,0),200,"REASON");

        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andReturn(httpResponse);

        EasyMock.replay(httpClient);
        assertEquals("Expect issueResponse to return a httpResponse",getResourceClient.issueRequest("RESOURCE", byteArrayOutputStream, new DateTime(2000,1,1,1,1), HttpMethod.GET), httpResponse);
        assertEquals("Expect the returned response to be ''", "", byteArrayOutputStream.toString("UTF-8"));       
        EasyMock.verify(httpClient);
    }
    @Test
    public void testIssueRequestGetNotModified() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("BODY".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BasicHttpEntity httpEntity = new BasicHttpEntity();
        httpEntity.setContent(inputStream);
        BasicHttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("http",1,0),304,"REASON");
        httpResponse.setEntity(httpEntity);

        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andReturn(httpResponse);

        EasyMock.replay(httpClient);
        assertEquals("Expect issueResponse to return a httpResponse",getResourceClient.issueRequest("RESOURCE", byteArrayOutputStream, new DateTime(2000,1,1,1,1), HttpMethod.GET), httpResponse);
        assertEquals("Expect the returned response to be ''", "", byteArrayOutputStream.toString("UTF-8"));       
        EasyMock.verify(httpClient);
    }
    @Test
    public void testIssueRequestThrowsClientProtocolException() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ClientProtocolException clientProtocolException = new ClientProtocolException();
        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andThrow(clientProtocolException);

        EasyMock.replay(httpClient);
        assertNull("Expect issueResponse to return a httpResponse",getResourceClient.issueRequest("RESOURCE", byteArrayOutputStream, new DateTime(2000,1,1,1,1), HttpMethod.GET));
        assertEquals("Expect the returned response to be ''", "", byteArrayOutputStream.toString("UTF-8"));       
        EasyMock.verify(httpClient);
    }

    @Test
    public void testIssueRequestThrowsIOException() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOException ioException = new IOException();
        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andThrow(ioException);

        EasyMock.replay(httpClient);
        assertNull("Expect issueResponse to return a httpResponse",getResourceClient.issueRequest("RESOURCE", byteArrayOutputStream, new DateTime(2000,1,1,1,1), HttpMethod.GET));
        assertEquals("Expect the returned response to be ''", "", byteArrayOutputStream.toString("UTF-8"));       
        EasyMock.verify(httpClient);
    }

    @Test
    public void testIssueRequestThrowsURISyntaxException() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        EasyMock.replay(httpClient);
        assertNull("Expect issueResponse to return a httpResponse",getResourceClient.issueRequest("RESOURCE\\", byteArrayOutputStream, new DateTime(2000,1,1,1,1), HttpMethod.GET));
        assertEquals("Expect the returned response to be ''", "", byteArrayOutputStream.toString("UTF-8"));       
        EasyMock.verify(httpClient);
    }
    @Test
    public void testIssueRequestGetInternalServerError() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("BODY".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BasicHttpEntity httpEntity = new BasicHttpEntity();
        httpEntity.setContent(inputStream);
        BasicHttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("http",1,0),500,"REASON");
        httpResponse.setEntity(httpEntity);

        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andReturn(httpResponse);

        EasyMock.replay(httpClient);
        assertEquals("Expect issueResponse to return a httpResponse",getResourceClient.issueRequest("RESOURCE", byteArrayOutputStream, new DateTime(2000,1,1,1,1), HttpMethod.GET), httpResponse);
        assertEquals("Expect the returned response to be ''", "", byteArrayOutputStream.toString("UTF-8"));       
        EasyMock.verify(httpClient);
    }
    
    @Test
    public void testGetResource() throws Exception {
        File expected = new File("src/test/resources/org/cateproject/file/test.txt");
        InputStream inputStream = new FileInputStream(expected);
        File actual = File.createTempFile("test",".txt");
        BasicHttpEntity httpEntity = new BasicHttpEntity();
        httpEntity.setContent(inputStream);
        BasicHttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("http",1,0),200,"REASON");
        httpResponse.setEntity(httpEntity);
        httpResponse.setHeader("Last-Modified","Sat, 1 Jan 2000 01:02:00 GMT");
        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andReturn(httpResponse);

        EasyMock.replay(httpClient);
        assertEquals("Expect getResource to return the Last Modified date",getResourceClient.getResource("RESOURCE", new DateTime(2000,1,1,1,1), actual), new DateTime(2000,1,1,1,2, DateTimeZone.UTC));
        assertFileEquals(expected, actual);       
        EasyMock.verify(httpClient);
    }

    @Test
    public void testGetResourceNoNotModified() throws Exception {
        File expected = new File("src/test/resources/org/cateproject/file/test.txt");
        InputStream inputStream = new FileInputStream(expected);
        File actual = File.createTempFile("test",".txt");
        BasicHttpEntity httpEntity = new BasicHttpEntity();
        httpEntity.setContent(inputStream);
        BasicHttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("http",1,0),200,"REASON");
        httpResponse.setEntity(httpEntity);
        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andReturn(httpResponse);

        EasyMock.replay(httpClient);
        assertNull("Expect getResource to return null",getResourceClient.getResource("RESOURCE", new DateTime(2000,1,1,1,1), actual));
        assertFileEquals(expected, actual);       
        EasyMock.verify(httpClient);
    }

    @Test
    public void testGetResourceThrowsException() throws Exception {
        File actual = File.createTempFile("test",".txt");
        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andThrow(new ClientProtocolException());

        EasyMock.replay(httpClient);
        assertNull("Expect getResource to return null",getResourceClient.getResource("RESOURCE", new DateTime(2000,1,1,1,1), actual));
        EasyMock.verify(httpClient);
    }

    @Test(expected = RuntimeException.class)
    public void testGetResourceFileNotFound() throws Exception {
        File actual = new File("/does/not/exist"); 

        getResourceClient.getResource("RESOURCE", new DateTime(2000,1,1,1,1), actual);
    }

    @Test
    public void testHeadResource() throws Exception {
        BasicHttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("http",1,0),200,"REASON");
        httpResponse.setHeader("Last-Modified","Sat, 1 Jan 2000 01:02:00 GMT");
        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpHead.class))).andReturn(httpResponse);

        EasyMock.replay(httpClient);
        assertEquals("Expect getLastModified to return the Last Modified date",getResourceClient.getLastModified("RESOURCE"), new DateTime(2000,1,1,1,2, DateTimeZone.UTC));
        EasyMock.verify(httpClient);
    }

    @Test
    public void testHeadResourceNoNotModified() throws Exception {
        BasicHttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("http",1,0),200,"REASON");
        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpHead.class))).andReturn(httpResponse);

        EasyMock.replay(httpClient);
        assertNull("Expect getResource to return null",getResourceClient.getLastModified("RESOURCE"));
        EasyMock.verify(httpClient);
    }

    @Test
    public void testHeadResourceThrowsException() throws Exception {
        EasyMock.expect(httpClient.execute(EasyMock.isA(HttpHead.class))).andThrow(new ClientProtocolException());

        EasyMock.replay(httpClient);
        assertNull("Expect getResource to return null",getResourceClient.getLastModified("RESOURCE"));
        EasyMock.verify(httpClient);
    }
}
