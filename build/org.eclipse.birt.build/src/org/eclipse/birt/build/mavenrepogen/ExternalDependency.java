/*******************************************************************************
 * Copyright (c) 2012 Innovent Solutions, Inc.
 * 
 * Unless otherwise indicated, all Content made available 
 * by Innovent Solutions, Inc  is provided to you under the terms and 
 * conditions of the Eclipse Public License Version 2.0 ("EPL"). A copy 
 * of the EPL is provided with this Content and is also available at 
 * http://www.eclipse.org/legal/epl-2.0.html. For purposes of the EPL, 
 * "Program" will mean the Content.
 * 
 * Contributors:
 *   Steve Schafer
 *******************************************************************************/
package org.eclipse.birt.build.mavenrepogen;

public class ExternalDependency {
	private final String fileName;
	private final String groupId;
	private final String artifactId;
	private final String version;

	public ExternalDependency(final String fileName, final String groupId, final String artifactId,
			final String version) {
		this.fileName = fileName;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}

	public String getFileName() {
		return fileName;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		return fileName.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ExternalDependency) {
			final ExternalDependency that = (ExternalDependency) obj;
			return fileName.equals(that.fileName);
		}
		if (obj instanceof String) {
			final String that = (String) obj;
			return fileName.equals(that);
		}
		return false;
	}

	@Override
	public String toString() {
		return fileName;
	}
}
