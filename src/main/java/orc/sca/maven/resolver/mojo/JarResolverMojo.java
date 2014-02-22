package orc.sca.maven.resolver.mojo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
 




import orc.sca.maven.resolver.holder.MavenJarArtifact;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.sca.maven.resolver.resource.JarFileResource;

/**
 * Adds arbitrary jars to project's classpath.
 * 
 * @goal resolve-jars
 * @phase generate-sources
 * 
 * @author Shreedhara C A <shreedhara.ca@gmail.com>
 */
public class JarResolverMojo extends AbstractMojo {

 
	/**
	 * @parameter expression="${remoteRepository}"
	 * @required
	 * @readonly
	 */
	private String remoteRepository;

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	private String localRepository;

	/**
	 * @parameter
	 * @required
	 * @readonly
	 */
	private List<JarFileResource> resources;

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	 

	@Override
	public void execute()     {

		try {
			
		 	//searchLocalRepo();
			
			executeInt();
		} catch (Exception e) {
			  e.printStackTrace();
			  getLog().debug("Maven Repo Does not Contain the artifact for ");
		} 
		
	}

	 

	private void executeInt() throws Exception {
		 
	 final String DIGG_SEARCH_ENDPOINT =  "https://repository.jboss.org/nexus/service/local/artifact/maven/resolve";
	 
	 for (JarFileResource resource : resources) {
			for (File jar : getJars(resource)) {

				
				MavenJarArtifact mj= new MavenJarArtifact(jar);
				
				ClientRequest Test = createMavenJarArtifactFromFile(DIGG_SEARCH_ENDPOINT,mj);

 				if (mj != null) {

					if (mj.getGroupId().contains(" ")) {
						 getLog().debug(
								"Group id for the Jar should not contins spaces :: "
										+ mj.getGroupId());
					} else {
						 
						if (resolveMavenArtifact(Test)) {

							 getLog().debug(
									"Maven Repo   ContainS the artifact for "
											+ mj.toString());

						} else {

							 getLog().error(
									"Maven Repo Does not Contain the artifact for "
											+ mj.toString());
							 
						}
					}
				}
			}
		}
	 
	 
   
     
	}



	private boolean resolveMavenArtifact(ClientRequest test) throws Exception {
		 
		ClientResponse response= test.get();
		
		if(response.getStatus()==200)
			return true;
		
		return false;
	}



	private ClientRequest createMavenJarArtifactFromFile(String DIGG_SEARCH_ENDPOINT, MavenJarArtifact test) throws IOException {
	 	ClientRequest req = new ClientRequest(DIGG_SEARCH_ENDPOINT);
		
		req
        .pathParameter("version", "2.0")
        .queryParameter("g", test.getGroupId()) 
    		.queryParameter("a", test.getArtifactId()) 
    		.queryParameter("v", "LATEST") 
    		.queryParameter("r", "central"); 
		
		
		return req;
	}



	private Dependency createDependency(Artifact a) {
		Dependency d = new Dependency();
		d.setGroupId(a.getGroupId());
		d.setArtifactId(a.getArtifactId());
		d.setVersion(a.getVersion());
		d.setScope(a.getScope());
		d.setType(a.getType());
		return d;
	}

	  
	private List<File> getJars(JarFileResource resource) throws IOException {

		List<File> fileList = FileUtils.getFiles(resource.getDirectory(),
				"**/*.jar", "*.xml");

		return fileList;
	}
}
