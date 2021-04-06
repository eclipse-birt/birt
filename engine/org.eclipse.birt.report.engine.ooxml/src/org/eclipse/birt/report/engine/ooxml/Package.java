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

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public class Package extends PartContainer {
	private static Logger logger = Logger.getLogger(Package.class.getName());

	private List<ContentType> contents = new ArrayList<ContentType>();

	private String tempFileName;
	private ArchiveWriter archiveWriter;
	private ArchiveFile archive;

	private Object data;

	private ZipOutputStream zipStream;

	private boolean hasEntryWriter = false;

	public static Package createInstance(OutputStream out, String tempFileDir, int compressionMode) {
		try {
			File tempFile = File.createTempFile("BIRT_OOXML_Temp_", "", new File(tempFileDir));
			return new Package(tempFile.getAbsolutePath(), out, compressionMode);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return null;
	}

	private Package(String tempFileName, OutputStream out, int compressionMode) throws IOException {
		super("/");

		this.tempFileName = tempFileName;
		this.archive = new ArchiveFile(tempFileName, "rw");
		this.archive.setCacheSize(4084);
		this.archiveWriter = new ArchiveWriter(archive);
		zipStream = new ZipOutputStream(out);
		zipStream.setLevel(compressionMode);
		addContentType(MimeType.RELS);
		addContentType(MimeType.XML);
	}

	@Override
	protected Package getPackage() {
		return this;
	}

	public String getAbsoluteUri() {
		return uri;
	}

	OutputStream getOutputStream(String uri) throws IOException {
		return archiveWriter.getOutputStream(uri);
	}

	OOXmlWriter getTempWriter(String uri) throws IOException {
		OutputStream out = getOutputStream(uri);
		OOXmlWriter writer = new OOXmlWriter();
		writer.open(out, "UTF-8");
		return writer;
	}

	void addContentType(ContentType contentType) {
		if (!contents.contains(contentType)) {
			contents.add(contentType);
		}
	}

	private InputStream getInputStream(String uri) throws IOException {
		return archiveWriter.getInputStream(uri);
	}

	private void save() throws IOException {
		try {
			outputRelationships();
			outputContentTypes();

			Collection<IPart> parts = getParts();
			for (IPart part : parts) {
				write(part);
			}
			zipStream.flush();
		} finally {
			if (zipStream != null) {
				zipStream.close();
			}
		}
	}

	private void write(IPart part) throws IOException {
		if (!part.isReference()) {
			Part part2 = (Part) part;
			writePart(part2);
			part2.release();
		}
	}

	private void writePart(Part part) throws IOException {
		part.outputRelationships();
		writePartData(part);
		Collection<IPart> children = part.getParts();
		for (IPart child : children) {
			if (!child.isReference()) {
				writePart((Part) child);
			}
		}
	}

	private void writePartData(Part part) throws IOException {
		if (!part.isCached()) {
			return;
		}
		InputStream inStream = null;
		try {
			String uri = part.getAbsoluteUri().toString();
			inStream = getInputStream(uri);
			zipStream.putNextEntry(new ZipEntry(uri.substring(1)));

			int length = -1;
			byte[] buff = new byte[4096];
			while ((length = inStream.read(buff)) != -1) {
				zipStream.write(buff, 0, length);
			}
			zipStream.closeEntry();
		} finally {
			if (inStream != null) {
				inStream.close();
			}
		}
	}

	private void outputContentTypes() throws IOException {
		OOXmlWriter writer = getEntryWriter("/[Content_Types].xml");
		writer.startWriter();
		writer.openTag("Types");
		writer.attribute("xmlns", NameSpaces.CONTENT_TYPES);
		for (ContentType type : contents) {
			type.write(writer);
		}
		writer.closeTag("Types");
		writer.endWriter();
		writer.close();
	}

	OOXmlWriter getEntryWriter(String entry) throws IOException {
		OOXmlWriter writer = new OOXmlWriter();
		writer.open(getFinalOutputStream(entry), "UTF-8");
		return writer;
	}

	public void close() throws IOException {
		save();
		archiveWriter.finish();
		archive.close();
		File file = new File(tempFileName);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}

	public Object getExtensionData() {
		return data;
	}

	public void setExtensionData(Object data) {
		this.data = data;
	}

	ZipOutputStream getZipOutputStream() {
		return zipStream;
	}

	protected String getRelationshipUri() {
		return "/_rels/.rels";

	}

	OutputStream getFinalOutputStream(String entry) throws IOException {
		assert (entry != null && entry.length() > 0);
		return new FinalOutputStream(entry.substring(1));
	}

	private class FinalOutputStream extends FilterOutputStream {

		public FinalOutputStream(String entry) throws IOException {
			super(zipStream);
			if (hasEntryWriter) {
				throw new RuntimeException("It is not allowed to open more than 1 entry writers concurrently.");
			}
			hasEntryWriter = true;

			zipStream.putNextEntry(new ZipEntry(entry));
		}

		/**
		 * @see java.io.FilterOutputStream#write(byte[], int, int)
		 */
		public void write(byte[] b, int off, int len) throws IOException {
			// call zipstream directly.
			// if we don't, then FilterOutputStream will call write(int) byte by byte
			zipStream.write(b, off, len);
		}

		public void close() {
			hasEntryWriter = false;
			try {
				zipStream.flush();
				zipStream.closeEntry();
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
}