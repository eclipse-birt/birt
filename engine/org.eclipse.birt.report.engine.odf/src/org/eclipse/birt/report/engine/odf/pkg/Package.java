/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.odf.pkg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.report.engine.odf.writer.ManifestWriter;

/**
 * Class representing an ODF package.
 */
public class Package {

	private static Logger logger = Logger.getLogger(Package.class.getName());

	private static final String MANIFEST_URI = "META-INF/manifest.xml"; //$NON-NLS-1$
	private static final String MIME_URI = "mimetype"; //$NON-NLS-1$

	private Map<String, PackageEntry> entries;

	private String tempFileName;
	private ArchiveWriter archiveWriter;
	private ArchiveFile archive;

	private ZipOutputStream zipStream;

	private boolean hasEntryWriter;

	private boolean closed = false;

	private String rootMime;

	public static Package createInstance(OutputStream out, String tempFileDir, String rootMime) {
		try {
			File tempFile = File.createTempFile("BIRT_ODF_Temp_", "", new File(tempFileDir));
			return new Package(tempFile.getAbsolutePath(), out, rootMime);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return null;
	}

	private Package(String tempFileName, OutputStream out, String rootMime) throws IOException {
		this.tempFileName = tempFileName;
		this.archive = new ArchiveFile(tempFileName, "rw"); //$NON-NLS-1$
		this.archive.setCacheSize(4096);
		this.archiveWriter = new ArchiveWriter(archive);

		this.rootMime = rootMime;
		this.closed = false;
		entries = new HashMap<String, PackageEntry>();
		zipStream = new ZipOutputStream(out);
		// zipStream.setLevel( compressionMode );
	}

	OutputStream getCachedOutputStream(String uri) throws IOException {
		assertOpen();
		return archiveWriter.getOutputStream(uri);
	}

	OutputStream getEntryOutputStream(String uri) throws IOException {
		assertOpen();
		return new EntryOutputStream(uri);
	}

	public PackageEntry addEntry(String uri, String contentType) {
		PackageEntry entry = new PackageEntry(this, uri, contentType, false);
		entries.put(uri, entry);
		return entry;
	}

	void addEntry(PackageEntry entry) {
		entries.put(entry.getUri(), entry);
	}

	public PackageEntry addCachedEntry(String uri, String contentType) {
		PackageEntry entry = new PackageEntry(this, uri, contentType, true);
		entries.put(uri, entry);
		return entry;
	}

	public void close() throws IOException {
		// save the files from the archive to the zip output
		try {
			save();
			writeMimeFile();
			writeManifest();
		} finally {
			zipStream.flush();
			zipStream.close();
		}
		archiveWriter.flush();
		archive.close();
		File file = new File(tempFileName);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		closed = true;
	}

	private void save() throws IOException {
		for (PackageEntry entry : entries.values()) {
			if (!entry.isCached()) {
				continue;
			}

			String uri = entry.getUri();
			InputStream input = null;
			try {
				if (uri.startsWith("/")) //$NON-NLS-1$
				{
					uri = uri.substring(1);
				}

				input = archiveWriter.getInputStream(entry.getUri());
				zipStream.putNextEntry(new ZipEntry(uri));

				int length = -1;
				byte[] buf = new byte[4096];
				while ((length = input.read(buf)) != -1) {
					zipStream.write(buf, 0, length);
				}
				zipStream.closeEntry();
			} finally {
				if (input != null) {
					input.close();
				}
			}
		}
	}

	private void writeMimeFile() throws IOException {
		zipStream.putNextEntry(new ZipEntry(MIME_URI));
		zipStream.write(rootMime.getBytes());
		zipStream.closeEntry();
	}

	private void writeManifest() throws IOException {
		zipStream.putNextEntry(new ZipEntry(MANIFEST_URI));
		zipStream.write(GetByteArrayFromManifestWriter());
		zipStream.closeEntry();

	}

	private byte[] GetByteArrayFromManifestWriter() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ManifestWriter writer = new ManifestWriter(out);
		writer.start(rootMime);
		for (PackageEntry entry : entries.values()) {
			writer.writeEntry(entry);
		}
		writer.end();
		return out.toByteArray();

	}

	private void assertOpen() {
		if (closed) {
			throw new IllegalStateException("Package is already closed"); //$NON-NLS-1$
		}
	}

	private class EntryOutputStream extends FilterOutputStream {

		public EntryOutputStream(String entry) throws IOException {
			super(zipStream);
			if (hasEntryWriter) {
				throw new RuntimeException("Can't open more than one entry writers concurrently."); //$NON-NLS-1$
			}
			hasEntryWriter = true;

			zipStream.putNextEntry(new ZipEntry(entry));
		}

		/**
		 * @see java.io.FilterOutputStream#write(byte[], int, int)
		 */
		public void write(byte[] b, int off, int len) throws IOException {
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
