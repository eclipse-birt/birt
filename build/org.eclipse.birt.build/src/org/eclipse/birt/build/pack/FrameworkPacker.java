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

package org.eclipse.birt.build.pack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.birt.build.framework.Bundle;
import org.eclipse.birt.build.framework.BundleEntry;
import org.eclipse.birt.build.framework.BundleFile;
import org.eclipse.birt.build.framework.Framework;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FrameworkPacker {

	static Logger logger = Logger.getLogger(FrameworkPacker.class.getName());

	HashSet<String> existingEntries = new HashSet<>();
	HashMap<String, Filter> filters = new HashMap<>();

	Framework framework;

	public FrameworkPacker(Framework framework) {
		this.framework = framework;
	}

	public void setFilter(String bundleId, Filter filter) {
		filters.put(bundleId, filter);
	}

	public void pack(ZipOutputStream zipOutput) throws IOException, ParserConfigurationException, TransformerException {
		packFiles(zipOutput);
		packDescription(zipOutput);
	}

	public void packFiles(ZipOutputStream zipOutput) throws IOException {
		// pack them together
		List<Bundle> bundles = framework.getAllBundles();
		for (Bundle bundle : bundles) {
			if (!bundle.isFragment()) {
				List<Bundle> fragments = bundle.getFragments();
				if (fragments != null) {
					for (Bundle fragment : fragments) {
						packBundle(zipOutput, fragment);
					}
				}
			}
			packBundle(zipOutput, bundle);
		}
	}

	public void packDescription(ZipOutputStream zipOutput)
			throws IOException, ParserConfigurationException, TransformerException {
		ZipEntry entry = new ZipEntry("plugin.xml");
		zipOutput.putNextEntry(entry);
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element documentRoot = document.createElement("plugin");
			document.appendChild(documentRoot);

			List<Bundle> bundles = framework.getAllBundles();
			for (Bundle bundle : bundles) {
				if (!bundle.isFragment()) {
					List<Bundle> fragments = bundle.getFragments();
					if (fragments != null) {
						for (Bundle fragment : fragments) {
							Document description = fragment.getDescription();
							if (description != null) {
								Node comment = document.createComment("import from " + fragment.getBundleID());
								document.getDocumentElement().appendChild(comment);
								appendDescription(document, description);
							}
						}
					}
				}
				Document description = bundle.getDescription();
				if (description != null) {
					Node comment = document.createComment("import from " + bundle.getBundleID());
					document.getDocumentElement().appendChild(comment);
					appendDescription(document, description);
				}
			}
			// Prepare the DOM document for writing
			Source source = new DOMSource(document);
			// Prepare the output file
			Result result = new StreamResult(zipOutput);
			// Write the DOM document to the file
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(source, result);
		} finally {
			zipOutput.closeEntry();
		}
	}

	protected void appendDescription(Document tgt, Document src) {
		Element tgtRoot = tgt.getDocumentElement();
		Element srcRoot = src.getDocumentElement();
		NodeList nodes = srcRoot.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = tgt.importNode(nodes.item(i), true);
			tgtRoot.appendChild(node);
		}

	}

	protected void packBundle(ZipOutputStream output, Bundle bundle) throws IOException {
		BundleFile bundleFile = bundle.getBundleFile();
		Filter filter = filters.get(bundle.getBundleID());
		List<String> pathes = bundleFile.getEntryPaths("/");
		for (String path : pathes) {
			packBundle(output, bundle, path, filter);
		}
	}

	protected File createTempFile(BundleEntry entry) throws IOException {
		File tmpFile = File.createTempFile("zip", ".zip");
		FileOutputStream output = new FileOutputStream(tmpFile);
		try (output) {
			InputStream input = entry.getInputStream();
			try {
				copyStream(input, output);
			} finally {
				input.close();
			}
		}
		return tmpFile;

	}

	protected void packBundle(ZipOutputStream output, Bundle bundle, String path, Filter filter) throws IOException {
		if (filter != null && !filter.accept(path)) {
			log(Level.FINE, "exclude {0}/{1}", new Object[] { bundle.getBundleID(), path });
			return;
		}
		BundleFile bundleFile = bundle.getBundleFile();
		if (bundleFile.isDirectory(path)) {
			List<String> entries = bundleFile.getEntryPaths(path);
			for (String entry : entries) {
				packBundle(output, bundle, entry, filter);
			}
			return;
		}

		// pack the JAR/ZIP
		BundleEntry bundleEntry = bundleFile.getEntry(path);
		if (path.endsWith(".jar") || path.endsWith(".zip")) {
			File tempZipFile = createTempFile(bundleEntry);
			try {
				ZipFile zipFile = new ZipFile(tempZipFile);
				try {
					packZip(output, bundleEntry, zipFile, filter);
				} finally {
					zipFile.close();
				}
			} finally {
				tempZipFile.delete();
			}
			return;
		}

		// pack the normal entry
		if (existingEntries.contains(path)) {
			log(Level.WARNING, "duplicate {0}/{1}", new Object[] { bundle.getBundleID(), path });
			return;
		}
		existingEntries.add(path);

		InputStream input = bundleEntry.getInputStream();
		try (input) {
			ZipEntry zipEntry = new ZipEntry(bundleEntry.getName());
			zipEntry.setTime(bundleEntry.getTime());
			zipEntry.setSize(bundleEntry.getSize());
			output.putNextEntry(zipEntry);
			try {
				copyStream(input, output);
			} finally {
				output.closeEntry();
			}
		}
	}

	protected void packZip(ZipOutputStream output, BundleEntry bundleEntry, ZipFile zipFile, Filter filter)
			throws IOException {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry srcEntry = entries.nextElement();
			String entryName = srcEntry.getName();
			if (filter != null && !filter.accept(bundleEntry.getName() + "/" + entryName)) {
				log(Level.FINE, "exclude {0}/{1}/{2}",
						new Object[] { bundleEntry.getBundleID(), bundleEntry.getName(), entryName });
				continue;
			}
			if (entryName.endsWith("/")) {
				continue;
			}
			if (existingEntries.contains(entryName)) {
				log(Level.WARNING, "duplicate {0}/{1}/{2}",
						new Object[] { bundleEntry.getBundleID(), bundleEntry.getName(), entryName });
				continue;
			}
			existingEntries.add(entryName);

			ZipEntry tgtEntry = new ZipEntry(entryName);
			tgtEntry.setTime(srcEntry.getTime());
			output.putNextEntry(tgtEntry);
			try {
				InputStream input = zipFile.getInputStream(srcEntry);
				try (input) {
					copyStream(input, output);
				}
			} finally {
				output.closeEntry();
			}
		}
	}

	protected void copyStream(InputStream src, OutputStream tgt) throws IOException {
		byte[] buffer = new byte[4096];
		int size = src.read(buffer);
		while (size > 0) {
			tgt.write(buffer, 0, size);
			size = src.read(buffer);
		}
	}

	protected void log(Level level, String message) {
		logger.log(level, message);
	}

	protected void log(Level level, String message, Object[] params) {
		logger.log(level, message, params);
	}

	protected void log(Level level, String message, Object[] params, Throwable cause) {
		if (logger.isLoggable(level)) {
			LogRecord record = new LogRecord(level, message);
			record.setParameters(params);
			record.setThrown(cause);
			logger.log(record);
		}
	}
}
