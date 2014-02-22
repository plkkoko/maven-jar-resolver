package org.sca.maven.resolver.resource;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class JarFileResource implements Serializable {

	private File directory;
    private String scope = "compile";
    private List<String> includes;
    private List<String> excludes;
    

    public File getDirectory() {
            return directory;
    }

    public void setDirectory(File directory) {
            this.directory = directory;
    }

    public String getScope() {
            return scope;
    }

    public void setScope(String scope) {
            this.scope = scope;
    }

    public List<String> getIncludes() {
            return includes;
    }

    public void setIncludes(List<String> includes) {
            this.includes = includes;
    }

    public List<String> getExcludes() {
            return excludes;
    }

    public void setExcludes(List<String> excludes) {
            this.excludes = excludes;
    }
    
}
