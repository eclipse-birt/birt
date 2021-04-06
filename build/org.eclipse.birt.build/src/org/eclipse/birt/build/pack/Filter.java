
package org.eclipse.birt.build.pack;

public class Filter {

	protected Filter parent;
	protected String[] includes;
	protected String[] excludes;

	public Filter(String[] includes, String[] excludes) {
		this(null, includes, excludes);
	}

	public Filter(Filter parent, String[] includes, String[] excludes) {
		this.parent = parent;
		this.includes = includes;
		this.excludes = excludes;
	}

	public boolean accept(String fileName) {
		// test if it is exclude
		for (String pattern : excludes) {
			if (fileName.matches(pattern)) {
				return false;
			}
		}
		// test if it is include
		for (String pattern : includes) {
			if (fileName.matches(pattern)) {
				return true;
			}
		}
		// test if it is match the parent's filter
		if (parent != null) {
			return parent.accept(fileName);
		}

		// the default is include
		return true;
	}
}
