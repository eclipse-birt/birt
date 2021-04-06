/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ooxml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;
import org.eclipse.birt.report.engine.ooxml.util.OOXmlUtil;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public abstract class PartContainer implements IPartContainer {

	private Map<String, IPart> parts;

	private Map<Hyperlink, Integer> hyperlinks;

	private int relationshipCount = 0;

	protected String uri;

	protected boolean cacheWriterUsed = false;

	public PartContainer(String uri) {
		this.uri = uri;
	}

	@Override
	public IPart getPart(String uri) {
		if (parts == null) {
			return null;
		}
		return parts.get(uri);
	}

	@Override
	public IPart getPart(String uri, String type, String relationshipType) {
		OOXmlType xmlType = new OOXmlType(type);
		IPart part = getPart(uri, xmlType, relationshipType);
		xmlType.setPart(part);
		return part;
	}

	@Override
	public IPart getPart(String uri, ContentType type, String relationshipType) {
		uri = OOXmlUtil.getRelativeUri(getAbsoluteUri(), uri);
		IPart part = getPart(uri);
		if (part != null) {
			if (!type.equals(part.getContentType())) {
				throw new PartAlreadyExistsException(part);
			}
			return part;
		}
		part = new Part(this, uri, type, relationshipType, nextRelationshipId());
		addPart(part);
		return part;
	}

	public String getHyperlinkId(String url) {
		return getHyperlinkId(url, RelationshipTypes.HYPERLINK);
	}

	public String getBookmarkId(String bmk) {
		return getHyperlinkId(bmk, RelationshipTypes.SLIDE);
	}

	public String getExternalImageId(String url) {
		return getHyperlinkId(url, RelationshipTypes.IMAGE);
	}

	private String getHyperlinkId(String url, String type) {
		Hyperlink hyperlink = new Hyperlink(url, type);
		Integer hyperlinkId = null;
		if (hyperlinks == null) {
			hyperlinks = new HashMap<PartContainer.Hyperlink, Integer>(8);
		} else {
			hyperlinkId = hyperlinks.get(hyperlink);
			if (hyperlinkId != null) {
				return OOXmlUtil.getRelationShipId(hyperlinkId);
			}
		}
		hyperlinkId = nextRelationshipId();
		hyperlinks.put(hyperlink, hyperlinkId);
		return OOXmlUtil.getRelationShipId(hyperlinkId);
	}

	public Collection<IPart> getParts() {
		if (parts == null) {
			return Collections.EMPTY_SET;
		}
		return parts.values();
	}

	public OOXmlWriter getCacheWriter() throws IOException {
		cacheWriterUsed = true;
		return getPackage().getTempWriter(getAbsoluteUri());
	}

	public OOXmlWriter getWriter() throws IOException {
		return getPackage().getEntryWriter(getAbsoluteUri());
	}

	public OutputStream getOutputStream() throws IOException {
		return getWriter().getOutputStream();
	}

	void addPart(IPart part) {
		if (parts == null) {
			parts = new HashMap<String, IPart>(8);
		}
		parts.put(part.getRelativeUri(), part);
	}

	@Override
	public IPart createPartReference(IPart part) {
		PartReference partReference = new PartReference(this, part, nextRelationshipId());
		addPart(partReference);
		return partReference;
	}

	protected void outputRelationships() throws IOException {
		if (notEmpty(parts) || notEmpty(hyperlinks)) {
			OOXmlWriter writer = getPackage().getEntryWriter(getRelationshipUri());
			writer.startWriter();
			writer.openTag("Relationships");
			writer.attribute("xmlns", NameSpaces.PACKAGE_RELATIONSHIPS);
			if (notEmpty(parts)) {
				Collection<IPart> children = parts.values();
				for (IPart part : children) {
					if (part.getRelationshipType() != null) {
						writeRelationshipEntry(writer, part.getRelativeUri(), part.getRelationshipId(),
								part.getRelationshipType());
					}
				}
			}
			if (notEmpty(hyperlinks)) {
				Set<Entry<Hyperlink, Integer>> hyperlinkSet = hyperlinks.entrySet();
				for (Entry<Hyperlink, Integer> entry : hyperlinkSet) {
					Hyperlink hyperlink = entry.getKey();
					String url = hyperlink.url;
					String relationshipId = OOXmlUtil.getRelationShipId(entry.getValue());
					String type = hyperlink.type;
					if (type.equals(RelationshipTypes.SLIDE)) {
						writeRelationshipEntry(writer, url, relationshipId, type);
					} else {
						writeRelationshipEntry(writer, url, relationshipId, type, "External");
					}
				}
			}
			writer.closeTag("Relationships");
			writer.endWriter();
			writer.close();
		}
	}

	public boolean notEmpty(Map<?, ?> map) {
		return map != null && map.size() > 0;
	}

	private void writeRelationshipEntry(OOXmlWriter writer, String url, String relationshipId, String type) {
		writeRelationshipEntry(writer, url, relationshipId, type, null);
	}

	private void writeRelationshipEntry(OOXmlWriter writer, String url, String relationshipId, String type,
			String mode) {
		writer.openTag("Relationship");
		writer.attribute("Id", relationshipId);
		writer.attribute("Type", type);
		writer.attribute("Target", url);
		if (mode != null) {
			writer.attribute("TargetMode", mode);
		}
		writer.closeTag("Relationship");
	}

	protected String getAbsolutUriOfChild(String uri) {
		return OOXmlUtil.getAbsoluteUri(getAbsoluteUri(), uri);
	}

	protected abstract String getAbsoluteUri();

	protected abstract Package getPackage();

	protected abstract String getRelationshipUri();

	private int nextRelationshipId() {
		++relationshipCount;
		return relationshipCount;
	}

	public static class Hyperlink {

		String url;
		String type;

		public Hyperlink(String url, String type) {
			this.url = url;
			this.type = type;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof Hyperlink)) {
				return false;
			}
			Hyperlink link = (Hyperlink) obj;
			return url == null ? link.url == null : url.equals(link.url);
		}

		@Override
		public int hashCode() {
			if (url == null) {
				return 0;
			}
			return url.hashCode();
		}
	}

	public void release() {
		if (parts != null) {
			parts.clear();
			parts = null;
		}
		if (hyperlinks != null) {
			hyperlinks.clear();
			hyperlinks = null;
		}
	}

	public void updateBmk(String wrngurl, String realurl) {
		if (realurl != null) {// set default or leave it dummy link
			Hyperlink link = new Hyperlink(wrngurl, RelationshipTypes.SLIDE);
			Integer relationshipid = hyperlinks.get(link);
			hyperlinks.remove(link);
			link = new Hyperlink(realurl, RelationshipTypes.SLIDE);
			hyperlinks.put(link, relationshipid);
		}
	}
}
