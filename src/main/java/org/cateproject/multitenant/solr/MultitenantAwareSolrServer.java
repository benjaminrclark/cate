package org.cateproject.multitenant.solr;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventAwareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class MultitenantAwareSolrServer extends SolrServer implements InitializingBean, DisposableBean, MultitenantEventAwareService  {
	
        private static final Logger logger = LoggerFactory.getLogger(MultitenantAwareSolrServer.class);

	private Map<String, SolrServer> solrServers = new HashMap<String, SolrServer>();

        private SolrServer solrServer = null;
	
        @Value("${solr.server.class:#{'org.apache.solr.client.solrj.embedded.EmbeddedSolrServer'}}")
	private Class<? extends SolrServer> serverClass;
	
	private CoreContainer coreContainer;

        private MultitenantRepository multitenantRepository;	
 
        @Value("${solr.home:solr/}")
	private String solrHomeString = "solr/";

        private FileSystemResource solrHome;

        @Value("${solr.configuration:solr/}")
        private String solrResources = "solr/";

        @Value("${solr.server.url:http://localhost:8983}")
	private URL serverUrl = null; 	

        @Value("${solr.connection.timeout:100}")
	private int connectionTimeout = 100;  // milliseconds
	
        @Value("${solr.so.timeout:3000}")
	private int soTimeout = 3000; // read timeout, milliseconds
	
        public MultitenantAwareSolrServer() {
            try {
                this.serverUrl = new URL("http://localhost:8983");
            } catch(MalformedURLException murle) { }

        }

	public void setMultitenantRepository(MultitenantRepository multitenantRepository) {
	    this.multitenantRepository = multitenantRepository;
	}

	public void setServerUrl(URL serverUrl) {
	    this.serverUrl = serverUrl;
	}
	
	public void setSolrHomeString(String solrHomeString) {
	    this.solrHomeString = solrHomeString;
	}
	
	public void setServerClass(Class<? extends SolrServer> serverClass) {
	    this.serverClass = serverClass;
	}
	
        public void setSolrResources(String solrResources) {
            this.solrResources = solrResources;
        }

        public void setSolrServer(SolrServer solrServer) {
            this.solrServer = solrServer;
        }

        public void setCoreContainer(CoreContainer coreContainer) {
            this.coreContainer = coreContainer;
        }

        public Map<String, SolrServer> getSolrServers() {
            return solrServers;
        }

	@Override
	public void afterPropertiesSet() throws Exception {
                initialize();
		List<Multitenant> multitenants = multitenantRepository.findAll();  
		for(Multitenant multitenant : multitenants) {
		     initializeCore(multitenant);
		}
	}

        public void initialize() throws IOException {
	    if(serverClass.equals(EmbeddedSolrServer.class)) {
                File solrHomeFile = new File(this.solrHomeString);
                solrHomeFile.mkdirs();
                this.solrHome = new FileSystemResource(solrHomeFile);
                if(!this.solrHome.exists() || !this.solrHome.createRelative(solrHomeFile.getName() + File.separator + "solr.xml").exists()) {
                    extractFromClasspathToFile(this.solrResources, this.solrHome.getFile());
                }
                Resource solrConfig = this.solrHome.createRelative(solrHomeFile.getName() + File.separator + "solr.xml");
                coreContainer = CoreContainer.createAndLoad(this.solrHome.getFile().getAbsolutePath(), solrConfig.getFile());
                logger.info("Loading Core container {}", new Object[]{this.solrHome.getFile().getAbsolutePath()});
                this.solrServer = new EmbeddedSolrServer(coreContainer, "collection1");
            } else {
		HttpSolrServer httpSolrServer = new HttpSolrServer(serverUrl.toString());
		httpSolrServer.setConnectionTimeout(connectionTimeout);
		httpSolrServer.setSoTimeout(soTimeout);
                this.solrServer = httpSolrServer;
            }
        }

        public SolrServer createSolrServer(String core) throws MalformedURLException {
		if(serverClass.equals(EmbeddedSolrServer.class)) {
		    return new EmbeddedSolrServer(coreContainer, core);
		} else {
		    URL solrCoreUrl = new URL(serverUrl, core);
		    HttpSolrServer solrServer = new HttpSolrServer(serverUrl.toString());
		    solrServer.setConnectionTimeout(connectionTimeout);
		    solrServer.setSoTimeout(soTimeout);
		    return solrServer;
		}
        }

	public void initializeCore(Multitenant multitenant) throws MalformedURLException {
            logger.info("Initializing " + multitenant.getIdentifier());
	    solrServers.put(multitenant.getIdentifier(), createSolrServer(multitenant.getIdentifier()));
	    logger.info("Initialized " + multitenant.getIdentifier());
	}

        public int extractFromClasspathToFile(String packagePath, File toDir) throws IOException {
            String locationPattern = "classpath*:" + packagePath + "**";
            logger.info("Copying files to {}", new Object[]{toDir});
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(locationPattern);
            if (resources.length == 0) {
                throw new IOException("Nothing found at " + locationPattern);
            }
            int counter = 0;
            for (Resource resource : resources) {
                
                logger.info("Resource {}", new Object[]{resource.getURL()});
                if (resource.isReadable() && !resource.getURL().toString().endsWith("/") ) { // Skip hidden or system files
                    URL url = resource.getURL();
                    String path = url.toString();
                    int p = path.lastIndexOf(packagePath) + packagePath.length();
                    path = path.substring(p);
                    File targetFile = new File(toDir, path);
                    long len = resource.contentLength();
                    logger.info("Resource {} {} {} {}", new Object[]{resource.getURL(), targetFile.exists(), targetFile.length(), len});
                    if (!targetFile.exists() || targetFile.length() != len) { // Only copy new files
                        FileUtils.copyURLToFile(url, targetFile);
                        logger.info("Copying {} to {}", new Object[]{url, targetFile});
                        counter++;
                    }
                }
            }
            if (counter > 0) {
                Object[] info = new Object[] { counter, locationPattern, toDir };
                logger.info("Unpacked {} files from {} to {}", info);
            }
            return counter;
        }

  
	public void createCore(Multitenant multitenant) throws SolrServerException, SolrException, IOException {
            SolrServer coreServer = createSolrServer(multitenant.getIdentifier());
            try {
                SolrPingResponse pingResponse = coreServer.ping();
                // Throws a solr exception if the core doesn't exist
                return;
            } catch(SolrException se) {
                if(!se.getMessage().startsWith("No such core")) {
                    throw se;
                }
            }

	    CoreAdminRequest.Create createCoreRequest = new CoreAdminRequest.Create();
            createCoreRequest.setConfigSet("default");
            createCoreRequest.setCoreName(multitenant.getIdentifier());
            createCoreRequest.setInstanceDir(multitenant.getIdentifier());
            NamedList<Object> response = solrServer.request(createCoreRequest);
            if(response != null) {
   	        NamedList<Object> cores = (NamedList<Object>) response.get("status");
            }

            CoreAdminRequest.Persist persistCoreRequest = new CoreAdminRequest.Persist(); 
            persistCoreRequest.setFileName("solr.xml");
            response = solrServer.request(persistCoreRequest);
	}

        @Override
        public void notify(MultitenantEvent multitenantEvent) {
            logger.info("TenantEvent recieved");
            switch(multitenantEvent.getType()) {
                case CREATE:
	            logger.info("Tenant " + multitenantEvent.getIdentifier() + " has been created");
	            Multitenant multitenant  = multitenantRepository.findByIdentifier(multitenantEvent.getIdentifier());
                    try {
          	        initializeCore(multitenant);
                    } catch (MalformedURLException murle) {
                        throw new RuntimeException(murle);
                    }
	            break;
                default:
	            logger.info("Recieved MultitenantEvent about " + multitenantEvent.getIdentifier());
	            break;
            }
        }

	@Override
	public void handle(MultitenantEvent multitenantEvent) {
            logger.info("TenantEvent recieved");
            switch(multitenantEvent.getType()) {
                case CREATE:
	            logger.info("Tenant " + multitenantEvent.getIdentifier() + " has been created");
	            Multitenant multitenant  = multitenantRepository.findByIdentifier(multitenantEvent.getIdentifier());
                    try {
          	        createCore(multitenant);
                    } catch (SolrServerException sse) {
                        throw new RuntimeException(sse);
                    } catch(IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
	            break;
                default:
	            logger.info("Recieved MultitenantEvent about " + multitenantEvent.getIdentifier());
	            break;
            }
	}

	@Override
	public NamedList<Object> request(final SolrRequest request)
			throws SolrServerException, IOException {
		String tenantId = MultitenantContextHolder.getContext().getTenantId();
		if(!solrServers.containsKey(tenantId)) {
                   logger.debug("Querying root server {}", tenantId, request);
                   return this.solrServer.request(request);
		}
                logger.debug("Querying server {}", tenantId, request);
		return solrServers.get(tenantId).request(request);
	}

        @Override
        public void destroy() {
            this.shutdown();
        }

	@Override
	public void shutdown() {
		for(SolrServer solrServer : solrServers.values()) {
		    solrServer.shutdown();
		}
                if(solrServer != null) {
                    solrServer.shutdown();
                }
	}
}
