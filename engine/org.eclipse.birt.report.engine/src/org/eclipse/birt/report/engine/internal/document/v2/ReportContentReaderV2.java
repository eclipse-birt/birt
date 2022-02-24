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

package org.eclipse.birt.report.engine.internal.document.v2;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.AutoTextContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.content.impl.ImageContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.content.impl.ListBandContent;
import org.eclipse.birt.report.engine.content.impl.ListContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableBandContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;

/**
 * read the content from the content stream.
 * 
 */
public class ReportContentReaderV2 {

	protected static Logger logger = Logger.getLogger(ReportContentReaderV2.class.getName());

	protected ReportContent reportContent;
	protected IReportDocument document;
	protected RAInputStream stream;

	/**
	 * the current offset of the stream.
	 */
	protected long offset;

	protected ClassLoader loader;

	public ReportContentReaderV2(ReportContent reportContent, IReportDocument document, ClassLoader loader) {
		this.reportContent = reportContent;
		this.loader = loader;
		this.document = document;
	}

	public void open(String name) throws IOException {
		IDocArchiveReader reader = document.getArchive();
		stream = reader.getStream(name);
	}

	public void close() {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Failed to close the reader", ex);
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
	protected IContent readContent(DataInputStream oi) throws IOException {
		IContent object = null;
		int contentType = IOUtil.readInt(oi);
		switch (contentType) {
		case IContent.CELL_CONTENT:
			CellContent cellContent = (CellContent) reportContent.createCellContent();
			object = cellContent;
			break;
		case IContent.CONTAINER_CONTENT:
			ContainerContent containerContent = (ContainerContent) reportContent.createContainerContent();
			containerContent.readContent(oi, loader);
			object = containerContent;
			break;
		case IContent.DATA_CONTENT:
			DataContent dataContent = (DataContent) reportContent.createDataContent();
			dataContent.readContent(oi, loader);
			object = dataContent;
			break;
		case IContent.FOREIGN_CONTENT:
			ForeignContent foreignContent = (ForeignContent) reportContent.createForeignContent();
			foreignContent.readContent(oi, loader);
			object = foreignContent;
			break;
		case IContent.IMAGE_CONTENT:
			ImageContent imageContent = (ImageContent) reportContent.createImageContent();
			imageContent.readContent(oi, loader);
			object = imageContent;
			break;
		case IContent.LABEL_CONTENT:
			LabelContent labelContent = (LabelContent) reportContent.createLabelContent();
			labelContent.readContent(oi, loader);
			object = labelContent;
			break;
		case IContent.PAGE_CONTENT:
			PageContent pageContent = (PageContent) reportContent.createPageContent();
			pageContent.readContent(oi, loader);
			object = pageContent;
			break;
		case IContent.ROW_CONTENT:
			RowContent rowContent = (RowContent) reportContent.createRowContent();
			rowContent.readContent(oi, loader);
			object = rowContent;
			break;
		case IContent.TABLE_BAND_CONTENT:
			TableBandContent tableBandContent = (TableBandContent) reportContent.createTableBandContent();
			tableBandContent.readContent(oi, loader);
			object = tableBandContent;
			break;
		case IContent.TABLE_CONTENT:
			TableContent tableContent = (TableContent) reportContent.createTableContent();
			tableContent.readContent(oi, loader);
			object = tableContent;
			break;
		case IContent.TEXT_CONTENT:
			TextContent textContent = (TextContent) reportContent.createTextContent();
			textContent.readContent(oi, loader);
			object = textContent;
			break;
		case IContent.AUTOTEXT_CONTENT:
			AutoTextContent autoText = (AutoTextContent) reportContent.createAutoTextContent();
			autoText.readContent(oi, loader);
			object = autoText;
			break;
		case IContent.LIST_CONTENT:
			ListContent list = (ListContent) reportContent.createListContent();
			list.readContent(oi, loader);
			object = list;
			break;
		case IContent.LIST_BAND_CONTENT:
			ListBandContent listBand = (ListBandContent) reportContent.createListBandContent();
			listBand.readContent(oi, loader);
			object = listBand;
			break;
		}
		return object;
	}

	/**
	 * read the content object out from the input stream in the curretn offset.
	 * After call this method, the user can use getContent(), getContentOffset()
	 * getParentOffset() to access the object.
	 * 
	 * @return the object read out.
	 * 
	 */
	public IContent readContent() throws IOException {
		if (offset >= stream.length()) {
			return null;
		}

		ContentTreeCache.TreeEntry entry = contentCache.getEntry(offset);
		if (entry != null) {
			stream.seek(offset + 8);
			int size = stream.readInt();
			offset = offset + 12 + size;
			return (IContent) entry.value;
		}

		stream.seek(offset);
		long parentOffset = stream.readLong();
		int size = stream.readInt();
		byte[] buffer = new byte[size];
		stream.readFully(buffer, 0, size);
		DataInputStream oi = new DataInputStream(new ByteArrayInputStream(buffer));
		IContent content = readContent(oi);
		DocumentExtension docExt = new DocumentExtension(offset);
		content.setExtension(IContent.DOCUMENT_EXTENSION, entry);
		if (parentOffset != -1) {
			IContent parent = loadContent(parentOffset);
			content.setParent(parent);
		}
		offset = offset + 12 + size;
		contentCache.addEntry(new ContentTreeCache.TreeEntry(docExt.getIndex(), parentOffset, offset, content));
		return content;
	}

	protected ContentTreeCache contentCache = new ContentTreeCache();

	/**
	 * read the content object from the reader at offset. After this action, the
	 * current offset will not be changed. The user can use getContent,
	 * getContentOffset, getParentOffset to access the object just read out.
	 * 
	 * @param offset
	 * @return
	 */
	private IContent loadContent(long offset) throws IOException {
		ContentTreeCache.TreeEntry entry = contentCache.getEntry(offset);
		if (entry != null) {
			return (IContent) entry.value;
		}

		// then try to read the parent form the streams
		stream.seek(offset);
		long parentOffset = stream.readLong();
		int size = stream.readInt();
		byte[] buffer = new byte[size];
		stream.readFully(buffer, 0, size);
		DataInputStream oi = new DataInputStream(new ByteArrayInputStream(buffer));
		IContent content = readContent(oi);
		DocumentExtension docExt = new DocumentExtension(offset);
		content.setExtension(IContent.DOCUMENT_EXTENSION, docExt);
		IContent parent = null;
		if (parentOffset != -1) {
			parent = loadContent(parentOffset);
		}
		content.setParent(parent);
		offset = offset + 12 + size;
		contentCache.addEntry(new ContentTreeCache.TreeEntry(docExt.getIndex(), parentOffset, offset, content));
		return content;
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
}
