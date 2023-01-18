/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Utilities;

import java.io.InputStream;

/**
 * Represents a File inside a Jar File. Used for accessing resources (models, textures), as they
 * are all inside a jar file when exported.
 * 
 * @author Eliseo
 *
 */
public class InternalJarFile {

	private static final String FILE_SEPARATOR = "/";

	private String path;
	private String name;

	public InternalJarFile(String path) {
		this.path = path;
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return getPath();
	}

	public String getName() {
		return name;
	}
}
