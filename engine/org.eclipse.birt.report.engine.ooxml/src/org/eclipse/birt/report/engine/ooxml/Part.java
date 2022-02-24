/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ooxml;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.report.engine.ooxml.util.OOXmlUtil;

public class Part extends PartContainer implements IPart {

	private Package pkg;

	private ContentType contentType;

	private int relationshipId;

	private PartContainer parent;

	private String relationshipType;

	Part(PartContainer partContainer, String uri, ContentType contentType, String relationshipType,
			int relationshipId) {
		super(uri);
		this.parent = partContainer;
		this.pkg = partContainer.getPackage();
		this.relationshipId = relationshipId;
		this.relationshipType = relationshipType;
		this.contentType = contentType;
		pkg.addContentType(contentType);
	}

	public String getRelativeUri() {
		return uri.toString();
	}

	public Package getPackage() {
		return pkg;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public OutputStream getCacheOutputStream() throws IOException {
		return pkg.getOutputStream(getAbsoluteUri());
	}

	public String getAbsoluteUri() {
		return parent.getAbsolutUriOfChild(uri);
	}

	public String getRelationshipId() {
		return OOXmlUtil.getRelationShipId(relationshipId);
	}

	public String getRelationshipUri() {
		String partUri = getAbsoluteUri();
		int lastIndex = partUri.lastIndexOf('/');
		String uri = partUri.substring(0, lastIndex + 1) + "_rels/" + partUri.substring(lastIndex + 1) + ".rels";
		return uri;
	}

	public String getRelationshipType() {
		return relationshipType;
	}

	public boolean isCached() {
		return cacheWriterUsed;
	}

	public boolean isReference() {
		return false;
	}
}
