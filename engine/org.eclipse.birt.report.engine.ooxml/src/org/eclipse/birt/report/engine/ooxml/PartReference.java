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
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public class PartReference implements IPart {

	private PartContainer parentPart;

	private IPart realPart;

	private int relationshipId;

	private String uri;

	PartReference(PartContainer partContainer, IPart realPart, int relationshipId) {
		this.parentPart = partContainer;
		this.realPart = realPart;
		this.relationshipId = relationshipId;
		this.uri = OOXmlUtil.getRelativeUri(parentPart.getAbsoluteUri(), getAbsoluteUri());
	}

	@Override
	public String getAbsoluteUri() {
		return realPart.getAbsoluteUri();
	}

	@Override
	public ContentType getContentType() {
		return realPart.getContentType();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return realPart.getOutputStream();
	}

	@Override
	public Package getPackage() {
		return realPart.getPackage();
	}

	@Override
	public String getRelationshipId() {
		return OOXmlUtil.getRelationShipId(relationshipId);
	}

	@Override
	public String getRelationshipType() {
		return realPart.getRelationshipType();
	}

	@Override
	public String getRelationshipUri() {
		return realPart.getRelationshipUri();
	}

	@Override
	public String getRelativeUri() {
		return uri;
	}

	@Override
	public OOXmlWriter getCacheWriter() throws IOException {
		return realPart.getCacheWriter();
	}

	@Override
	public IPart getPart(String uri, String type, String relationshipType) {
		return realPart.getPart(uri, type, relationshipType);
	}

	@Override
	public IPart createPartReference(IPart part) {
		return realPart.createPartReference(part);
	}

	@Override
	public IPart getPart(String uri, ContentType type, String relationshipType) {
		return realPart.getPart(uri, type, relationshipType);
	}

	@Override
	public IPart getPart(String uri) {
		return realPart.getPart(uri);
	}

	@Override
	public String getHyperlinkId(String url) {
		return realPart.getHyperlinkId(url);
	}

	@Override
	public String getExternalImageId(String url) {
		return realPart.getExternalImageId(url);
	}

	@Override
	public OutputStream getCacheOutputStream() throws IOException {
		return realPart.getCacheOutputStream();
	}

	@Override
	public OOXmlWriter getWriter() throws IOException {
		return realPart.getWriter();
	}

	@Override
	public boolean isCached() {
		return realPart.isCached();
	}

	@Override
	public boolean isReference() {
		return true;
	}

	@Override
	public String getBookmarkId(String bmkurl) {// req implementation: does not do anything
		return null;
	}

}
