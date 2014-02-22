package orc.sca.maven.resolver.holder;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class MavenJarArtifact {

	private String groupId;
	private String artifactId;
	private String version;
	private final String scope="compile";
	private final String packaging="jar";
	public String getScope() {
		return scope;
	}

	public String getPackaging() {
		return packaging;
	}

	private JarFile f;

	public MavenJarArtifact(File jarFile) throws IOException {

		f = new JarFile(jarFile);
		String fileName = jarFile.getName();
		
		if(f.getManifest()!=null){
			
			if(f.getManifest().getMainAttributes().containsKey("Bundle-SymbolicName"))
			this.artifactId = f.getManifest().getMainAttributes()
					.getValue("Bundle-SymbolicName").substring(0, fileName.lastIndexOf("-"));
			
			if(f.getManifest().getMainAttributes().containsKey("Bundle-Name"))
			this.groupId = f.getManifest().getMainAttributes()
					.getValue("Bundle-Name").substring(0, fileName.lastIndexOf("-"));
			
			if(f.getManifest().getMainAttributes().containsKey("Implementation-Version"))
			this.version = f.getManifest().getMainAttributes()
					.getValue("Implementation-Version");

		}
			

		if (this.artifactId == null) {
			this.artifactId = fileName.substring(0,
					fileName.lastIndexOf("."));
		}
		if (this.groupId == null) {
			this.groupId = fileName.substring(0, fileName.lastIndexOf("."));
		}

		if (this.version == null)
			this.version = "1.0.0";

	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	@Override
	public String toString() {
		return "MavenJarArtifact [groupId=" + groupId + ", artifactId="
				+ artifactId + ", version=" + version + ", scope=" + scope
				+ ", packaging=" + packaging + "]";
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
