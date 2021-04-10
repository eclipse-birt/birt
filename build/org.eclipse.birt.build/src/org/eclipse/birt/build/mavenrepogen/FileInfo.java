/*
Copyright (c) 2012 Innovent Solutions, Inc.

Unless otherwise indicated, all Content made available 
by Innovent Solutions, Inc  is provided to you under the terms and 
conditions of the Eclipse Public License Version 1.0 ("EPL"). A copy 
of the EPL is provided with this Content and is also available at 
http://www.eclipse.org/legal/epl-v10.html. For purposes of the EPL, 
"Program" will mean the Content.

Author: Steve Schafer
 */
package org.eclipse.birt.build.mavenrepogen;

import java.io.File;

class FileInfo {
	private final File file;
	private final String groupId;
	private final String artifactId;
	private String version;

	public FileInfo(final File file, final String groupId, final String artifactId, final String version) {
		this.file = file;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}

	public File getFile() {
		return file;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getVersion(final boolean snapshot) {
		return version + (snapshot ? "-SNAPSHOT" : "");
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String v) {
		this.version = v;
	}

	public String getGroupId() {
		return groupId;
	}
}