
package org.eclipse.birt.build.ant;

import java.util.ArrayList;
import java.util.List;

public class BundleItem {

	private String name;
	private ArrayList<FilterItem> includes = new ArrayList<FilterItem>();
	private ArrayList<FilterItem> excludes = new ArrayList<FilterItem>();

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public FilterItem createInclude() {
		FilterItem filter = new FilterItem();
		includes.add(filter);
		return filter;
	}

	public FilterItem createExclude() {
		FilterItem filter = new FilterItem();
		excludes.add(filter);
		return filter;
	}

	public List<FilterItem> getIncludeFilters() {
		return includes;
	}

	public List<FilterItem> getExcludeFilters() {
		return excludes;
	}
}
