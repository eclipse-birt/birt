/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document.v3;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.AbstractContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;

/**
 * read the content from the content stream.
 *
 */
public class ReportContentReaderV3 {

	protected static Logger logger = Logger.getLogger(ReportContentReaderV3.class.getName());

	protected ReportContent reportContent;
	protected RAInputStream stream;
	protected int version = -1;

	protected final static int INDEX_ENTRY_SIZE_V0 = 40;
	protected final static int INDEX_ENTRY_SIZE_V1 = 24;

	protected final static int VERSION_0 = 0;
	protected final static int VERSION_1 = 1;
	protected final static int VERSION_SIZE = 4;

	/**
	 * the current offset of the stream.
	 */
	protected long offset;

	protected long rootOffset;

	protected boolean isEmpty = false;

	protected ClassLoader loader;

	public ReportContentReaderV3(ReportContent reportContent, RAInputStream stream, ClassLoader loader)
			throws IOException {
		this.reportContent = reportContent;
		this.loader = loader;
		this.stream = stream;
		long length = stream.length();
		if (this.stream.length() >= 4) {
			stream.seek(0);
			version = stream.readInt();
			if (version == -1) {
				version = VERSION_0;
			} else if (version == VERSION_1) {
				if (length == 4) {
					isEmpty = true;
				} else {
					loadReport();
				}
			} else {
				throw new IOException("unrecognized stream version!");
			}
		} else {
			throw new IOException("unrecognized stream version!");
		}
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void close() {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Failed to close the reader", ex);
			}
			stream = null;
		}
	}

	public long getRoot() {
		return rootOffset;
	}

	private void loadReport() throws IOException {
		// skip the first document extension
		readDocumentExtensionV1(0);
		int size = stream.readInt();
		if (size != -1) // -1 means it is the first
		{
			byte[] buffer = new byte[size];
			stream.readFully(buffer, 0, size);
			DataInputStream oi = new DataInputStream(new ByteArrayInputStream(buffer));
			int contentType = IOUtil.readInt(oi);
			if (contentType == IContent.REPORT_CONTENT) {
				reportContent.readContent(oi, loader);
				offset += INDEX_ENTRY_SIZE_V1 + 4 + size;
				rootOffset = offset;
			}
		}
	}

	/**
	 * read the content object from the input stream.
	 *
	 * @param oi the input stream.
	 * @return the object read out.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected IContent readObject(long offset) throws IOException {
		if (VERSION_0 == version) {
			stream.seek(offset);
		} else if (VERSION_1 == version) {
			stream.seek(VERSION_SIZE + offset);
		}

		int size = stream.readInt();
		byte[] buffer = new byte[size];
		stream.readFully(buffer, 0, size);
		DataInputStream oi = new DataInputStream(new ByteArrayInputStream(buffer));
		AbstractContent object = null;
		int contentType = IOUtil.readInt(oi);
		switch (contentType) {
		case IContent.CELL_CONTENT:
			object = (AbstractContent) reportContent.createCellContent();
			break;
		case IContent.CONTAINER_CONTENT:
			object = (AbstractContent) reportContent.createContainerContent();
			break;
		case IContent.DATA_CONTENT:
			object = (AbstractContent) reportContent.createDataContent();
			break;
		case IContent.FOREIGN_CONTENT:
			object = (AbstractContent) reportContent.createForeignContent();
			break;
		case IContent.IMAGE_CONTENT:
			object = (AbstractContent) reportContent.createImageContent();
			break;
		case IContent.LABEL_CONTENT:
			object = (AbstractContent) reportContent.createLabelContent();
			break;
		case IContent.PAGE_CONTENT:
			object = (AbstractContent) reportContent.createPageContent();
			break;
		case IContent.ROW_CONTENT:
			object = (AbstractContent) reportContent.createRowContent();
			break;
		case IContent.TABLE_BAND_CONTENT:
			object = (AbstractContent) reportContent.createTableBandContent();
			break;
		case IContent.TABLE_CONTENT:
			object = (AbstractContent) reportContent.createTableContent();
			break;
		case IContent.TEXT_CONTENT:
			object = (AbstractContent) reportContent.createTextContent();
			break;
		case IContent.AUTOTEXT_CONTENT:
			object = (AbstractContent) reportContent.createAutoTextContent();
			break;
		case IContent.LIST_CONTENT:
			object = (AbstractContent) reportContent.createListContent();
			break;
		case IContent.LIST_BAND_CONTENT:
			object = (AbstractContent) reportContent.createListBandContent();
			break;
		case IContent.LIST_GROUP_CONTENT:
			object = (AbstractContent) reportContent.createListGroupContent();
			break;
		case IContent.TABLE_GROUP_CONTENT:
			object = (AbstractContent) reportContent.createTableGroupContent();
			break;
		default:
			// Not expected
			throw new IOException("Found invalid contentType" + contentType + " at object offset " + offset);
		}
		object.setVersion(version);
		object.readContent(oi, loader);
		return object;
	}

	/**
	 * read the content object out from the input stream in the curretn offset.
	 * After call this methods, the offset is position to the next element in
	 * pre-depth order.
	 *
	 * The content's parent is loaded in this time.
	 *
	 * @return the object read out.
	 */
	public IContent readContent() throws IOException {
		long index = offset;

		// load the content from the stream
		IContent content = readContent(index);

		// try to locate the next element
		DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
		// the next element is its child if exits
		if (docExt.getFirstChild() != -1) {
			offset = docExt.getFirstChild();
			return content;
		}
		// otherise use it's sibling if exists
		if (docExt.getNext() != -1) {
			offset = docExt.getNext();
			return content;
		}
		// or use the parent's sibling if exits
		docExt = readDocumentExtension(docExt.getParent());
		while (docExt != null) {

			if (docExt.getNext() != -1) {
				offset = docExt.getNext();
				return content;
			}
			docExt = readDocumentExtension(docExt.getParent());
		}

		offset = -1;

		return content;
	}

	public IContent readContent(long index) throws IOException {

		if (VERSION_0 == version) {
			return readContentV0(index);
		} else if (VERSION_1 == version) {
			return readContentV1(index);
		} else {
			throw new IOException("unrecognized stream version!");
		}
	}

	private IContent readContentV0(long index) throws IOException {
		if (index >= stream.length() || index < 0) {
			throw new IOException("Invalid content offset:" + index);
		}
		DocumentExtension docExt = readDocumentExtensionV0(index);
		IContent content = readObject(index + INDEX_ENTRY_SIZE_V0);
		if (content != null) {
			content.setExtension(IContent.DOCUMENT_EXTENSION, docExt);
		}
		return content;
	}

	private IContent readContentV1(long index) throws IOException {
		DocumentExtension docExt = readDocumentExtensionV1(index);
		IContent content = readObject(index + INDEX_ENTRY_SIZE_V1);
		if (content != null) {
			content.setExtension(IContent.DOCUMENT_EXTENSION, docExt);
		}
		return content;
	}

	private DocumentExtension readDocumentExtension(long index) throws IOException {
		if (VERSION_0 == version) {
			return readDocumentExtensionV0(index);
		} else if (VERSION_1 == version) {
			return readDocumentExtensionV1(index);
		} else {
			throw new IOException("unrecognized stream version!");
		}
	}

	private DocumentExtension readDocumentExtensionV0(long index) throws IOException {
		stream.seek(index);
		index = stream.readLong();
		long parent = stream.readLong();
		long previous = stream.readLong();
		long next = stream.readLong();
		long child = stream.readLong();
		DocumentExtension docExt = new DocumentExtension(index);
		docExt.setParent(parent);
		docExt.setPrevious(previous);
		docExt.setNext(next);
		docExt.setFirstChild(child);
		return docExt;
	}

	private DocumentExtension readDocumentExtensionV1(long index) throws IOException {
		stream.seek(VERSION_SIZE + index);
		long parent = stream.readLong();
		long next = stream.readLong();
		long child = stream.readLong();
		DocumentExtension docExt = new DocumentExtension(index);
		docExt.setParent(parent);
		docExt.setNext(next);
		docExt.setFirstChild(child);
		return docExt;
	}

	/**
	 * get the current offset.
	 *
	 * The current offset is changed by set of readContent.
	 *
	 * @return
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * set the current offset. The offset must pints to a valid content.
	 *
	 * @param offset
	 */
	public void setOffset(long offset) {
		this.offset = offset;
	}

	public void dump() throws IOException {
		IContent content = readContent(0);
		dumpContent(0, content);
	}

	protected void dumpContent(int level, IContent content) throws IOException {
		for (int i = 0; i < level; i++) {
			System.out.print("  ");
		}
		DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
		long index = docExt.getIndex();
		System.out.print(index);
		System.out.print(":");
		System.out.println(content.getInstanceID());
		long firstChild = docExt.getFirstChild();
		if (firstChild != -1) {
			IContent child = readContent(firstChild);
			dumpContent(level + 1, child);
		}

		long nextOffset = docExt.getNext();
		if (nextOffset != -1) {
			IContent next = readContent(nextOffset);
			dumpContent(level, next);
		}
	}
}
