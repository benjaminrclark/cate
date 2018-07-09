package org.cateproject.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.message.BasicHeader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;


public class GetResourceClient {

	static int BUFFER = 2048;
	
	private static final DateTimeFormatter HTTP_DATETIME_FORMATTER = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withZoneUTC();

	private Logger logger = LoggerFactory.getLogger(GetResourceClient.class);

    private HttpClient httpClient;
    
    public void setHttpClient(HttpClient httpClient) {
    	this.httpClient = httpClient;
    }
	
	/**
	 * Executes a HTTP GET request with the If-Modified-Since header set to
	 * ifModifiedSince. If the resource has not been modified then the Source
	 * may respond with the HTTP status 304 NOT MODIFIED, in which case the
	 * method will return an exit code 'NOT MODIFIED'.
	 * 
	 * If the resource has been modified, the client will save the response in a
	 * document specified in temporaryFileName and will return an exit code 'OK'.
	 * 
	 * @param resource
	 *            The url being harvested.
	 * @param ifModifiedSince
	 *            The dateTime when this Source was last harvested.
	 * @param outputStream
	 *            An output stream to store the response in
	 * @return An exit status indicating that the request was completed, failed, or
	 *         if the server responded with a 304 NOT MODIFIED response
	 *         indicating that the resource has not been modified
	 */
	public HttpResponse issueRequest(String resource, OutputStream outputStream, DateTime ifModifiedSince, HttpMethod httpMethod) {
	    try {
	        logger.info("GET: " + resource);
	        URI uri = new URI(resource);
	        HttpUriRequest httpUriRequest = null;
	        switch(httpMethod) {
                    case HEAD:
                        httpUriRequest = new HttpHead(uri.toASCIIString());
                        break;
                    case GET:
                    default:
                        httpUriRequest = new HttpGet(uri.toASCIIString());
                        break;
	        }
                 
                if(ifModifiedSince != null) {
                    httpUriRequest.addHeader(new BasicHeader("If-Modified-Since", HTTP_DATETIME_FORMATTER.print(ifModifiedSince)));
                }
                HttpResponse httpResponse = httpClient.execute(httpUriRequest);
                 
                switch (httpResponse.getStatusLine().getStatusCode()) {
                    case HttpURLConnection.HTTP_NOT_MODIFIED:
                        return httpResponse;
                    case HttpURLConnection.HTTP_OK:
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            try(BufferedInputStream bufferedInputStream = new BufferedInputStream(entity.getContent());
                                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                                int count;
                                byte[] data = new byte[BUFFER];
                                while ((count = bufferedInputStream.read(data, 0, BUFFER)) != -1) {
                                    bufferedOutputStream.write(data, 0, count);
                                }
                                bufferedOutputStream.flush();
                            }
                        } else {
                            // TODO error condition 
                            logger.error("Server returned {} but HttpEntity is null", new Object[]{httpResponse.getStatusLine()});
                            return httpResponse;
                        }
                        return httpResponse;
                    default:
                        // TODO error condition 
                        logger.error("Server returned unexpected status code {} for document {}", new Object[]{httpResponse.getStatusLine(), resource}); 
                        /**
                         *  This is not an error in this
                         *  application but a server side
                         *  error
                         */
                        BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(httpResponse.getEntity());
                        InputStreamReader reader = new InputStreamReader(bufferedEntity.getContent());
                        StringBuffer stringBuffer = new StringBuffer();
                        char[] content = new char[BUFFER];
                        while (reader.read(content, 0, BUFFER) != -1) {
                            stringBuffer.append(content);
                        }
                        logger.info("Server Response was: {}", new Object[]{stringBuffer.toString()});
                        httpUriRequest.abort();
                        return httpResponse;
                    }
                } catch (ClientProtocolException cpe) {
                    // TODO error condition
                    logger.error("Client Protocol Exception getting document {}: {}", new Object[]{resource,cpe.getLocalizedMessage()});
                    return null;
                } catch (IOException ioe) {
                    // TODO error condition
                    logger.error("Input Output Exception getting document {}: {}", new Object[]{resource, ioe.getLocalizedMessage()});
                    return null;
                } catch (URISyntaxException urise) {
                    // TODO error condition
                    logger.error("URI Syntax exception for {}: {}", new Object[]{resource, urise.getLocalizedMessage()});
                    return null;
                }
	}
	
	public DateTime getResource(String resource, DateTime ifModifiedSince, File localFile) {
		try {
			OutputStream outputStream = new FileOutputStream(localFile);
			HttpResponse httpResponse = issueRequest(resource, outputStream, ifModifiedSince, HttpMethod.GET);
			if(httpResponse != null) {
				Header lastModifiedHeader = httpResponse.getFirstHeader("Last-Modified");
				if(lastModifiedHeader != null) {					
					return HTTP_DATETIME_FORMATTER.parseDateTime(lastModifiedHeader.getValue());
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (FileNotFoundException fnfe) {
			logger.error(fnfe.toString());
			throw new RuntimeException(fnfe);
		}
	}

        public DateTime getResource(String resource, File localFile) {
            return getResource(resource, null, localFile);
        }
	
	public DateTime getLastModified(String resource) {
		OutputStream outputStream = new ByteArrayOutputStream();
		HttpResponse httpResponse = issueRequest(resource, outputStream, null, HttpMethod.HEAD);
		if(httpResponse != null) {
			Header lastModifiedHeader = httpResponse.getFirstHeader("Last-Modified");
			if(lastModifiedHeader != null) {
				return HTTP_DATETIME_FORMATTER.parseDateTime(lastModifiedHeader.getValue());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
