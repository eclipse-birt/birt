/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v1;

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
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableBandContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.IReportContentReader;

/**
 * read the content from the content stream.
 * 
 */
class ReportContentReaderV1 implements IReportContentReader {

	protected static Logger logger = Logger.getLogger(ReportContentReaderV1.class.getName());

	protected IReportDocument document;
	protected ReportContent reportContent;
	protected RAInputStream stream;
	protected long offset;
	protected ClassLoader loader;

	public ReportContentReaderV1(ReportContent reportContent, IReportDocument document, ClassLoader loader) {
		this.reportContent = reportContent;
		this.document = document;
		this.loader = loader;
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

	protected IContent readContent(DataInputStream oi) throws IOException {
		int contentType = IOUtil.readInt(oi);
		switch (contentType) {
		case IContent.CELL_CONTENT:
			CellContent cellContent = (CellContent) reportContent.createCellContent();
			cellContent.readContent(oi, loader);
			return cellContent;

		case IContent.CONTAINER_CONTENT:
			ContainerContent containerContent = (ContainerContent) reportContent.createContainerContent();
			containerContent.readContent(oi, loader);
			return containerContent;

		case IContent.DATA_CONTENT:
			DataContent dataContent = (DataContent) reportContent.createDataContent();
			dataContent.readContent(oi, loader);
			return dataContent;

		case IContent.FOREIGN_CONTENT:
			ForeignContent foreignContent = (ForeignContent) reportContent.createForeignContent();
			foreignContent.readContent(oi, loader);
			return foreignContent;

		case IContent.IMAGE_CONTENT:
			ImageContent imageContent = (ImageContent) reportContent.createImageContent();
			imageContent.readContent(oi, loader);
			return imageContent;

		case IContent.LABEL_CONTENT:
			LabelContent labelContent = (LabelContent) reportContent.createLabelContent();
			labelContent.readContent(oi, loader);
			return labelContent;

		case IContent.PAGE_CONTENT:
			PageContent pageContent = (PageContent) reportContent.createPageContent();
			pageContent.readContent(oi, loader);
			return pageContent;

		case IContent.ROW_CONTENT:
			RowContent rowContent = (RowContent) reportContent.createRowContent();
			rowContent.readContent(oi, loader);
			return rowContent;

		case IContent.TABLE_BAND_CONTENT:
			TableBandContent tableBandContent = (TableBandContent) reportContent.createTableBandContent();
			tableBandContent.readContent(oi, loader);
			return tableBandContent;

		case IContent.TABLE_CONTENT:
			TableContent tableContent = (TableContent) reportContent.createTableContent();
			tableContent.readContent(oi, loader);
			return tableContent;

		case IContent.TEXT_CONTENT:
			TextContent textContent = (TextContent) reportContent.createTextContent();
			textContent.readContent(oi, loader);
			return textContent;

		case IContent.AUTOTEXT_CONTENT:
			AutoTextContent autoText = (AutoTextContent) reportContent.createAutoTextContent();
			autoText.readContent(oi, loader);
			return autoText;

		default:
			throw new IOException("No a valid content type: " + contentType);
		}
	}

	public IContent readContent() throws IOException {
		stream.seek(offset);
		int size = stream.readInt();
		offset += 4;
		offset += size;
		byte[] buffer = new byte[size];
		stream.readFully(buffer, 0, size);
		DataInputStream oi = new DataInputStream(new ByteArrayInputStream(buffer));
		return readContent(oi);
	}

	public IContent readContent(long offset) throws IOException {
		if (offset >= stream.available()) {
			return null;
		}
		stream.seek(offset);
		int size = stream.readInt();
		byte[] buffer = new byte[size];
		stream.readFully(buffer, 0, size);
		DataInputStream oi = new DataInputStream(new ByteArrayInputStream(buffer));
		IContent content = readContent(oi);
		DocumentExtension ext = new DocumentExtension(offset);
		content.setExtension(IContent.DOCUMENT_EXTENSION, ext);
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
