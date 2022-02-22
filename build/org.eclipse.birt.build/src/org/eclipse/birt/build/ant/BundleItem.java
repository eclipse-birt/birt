/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.build.ant;

import java.util.ArrayList;
import java.util.List;

public class BundleItem {

	private String name;
	private ArrayList<FilterItem> includes = new ArrayList<>();
	private ArrayList<FilterItem> excludes = new ArrayList<>();

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
