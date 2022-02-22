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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.IReportContentWriter;

public class ReportContentWriterV2 implements IReportContentWriter {

	protected static Logger logger = Logger.getLogger(IReportContentWriter.class.getName());

	/**
	 * report document used to save the contents
	 */
	protected ReportDocumentWriter document;

	/**
	 * stream in the document, used to save the contents
	 */
	protected DataOutputStream stream;

	/**
	 * the offset of current node
	 */
	protected long offset;

	public ReportContentWriterV2(ReportDocumentWriter document) {
		this.document = document;
	}

	/**
	 * open the content writer.
	 */
	public void open(String name) throws IOException {
		IDocArchiveWriter archive = document.getArchive();
		RAOutputStream out = archive.createRandomAccessStream(name);
		stream = new DataOutputStream(out);
		offset = 0;
	}

	/**
	 * close the content writer
	 */
	@Override
	public void close() {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Failed in close the writer", ex);
			}
			stream = null;
		}
	}

	/**
	 * get the current offset.
	 *
	 * @return
	 */
	@Override
	public long getOffset() {
		return offset;
	}

	/**
	 * buffer used to save the report content.
	 */
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	/**
	 * data output stream used to save the report content.
	 */
	private DataOutputStream bufferStream = new DataOutputStream(buffer);

	/**
	 * stack used to save the parent's offset
	 */
	protected Stack contents = new Stack();

	/**
	 * write the content into the stream.
	 *
	 * @param content the content object.
	 * @return the content object's offset.
	 * @throws IOException
	 */
	@Override
	public long writeContent(IContent content) throws IOException {
		buffer.reset();

		// get search the parent offset
		long parentOffset = -1;
		IContent parent = (IContent) content.getParent();
		if (parent != null) {
			DocumentExtension docExt = (DocumentExtension) parent.getExtension(IContent.DOCUMENT_EXTENSION);
			if (docExt != null) {
				parentOffset = docExt.getIndex();
			}
		}

		// get the byte[] of the content
		buffer.reset();
		IOUtil.writeInt(bufferStream, content.getContentType());
		content.writeContent(bufferStream);
		bufferStream.flush();
		byte[] values = buffer.toByteArray();

		// write the content out as: parent, length, data
		stream.writeLong(parentOffset);
		stream.writeInt(values.length);
		stream.write(values);

		DocumentExtension docExt = new DocumentExtension(offset);
		content.setExtension(IContent.DOCUMENT_EXTENSION, docExt);

		offset = offset + 8 + 4 + values.length;

		return docExt.getIndex();
	}

	/**
	 * save the content and its children into the streams.
	 *
	 * @param content the content object
	 * @return the offset of this content object.
	 * @throws IOException
	 */
	@Override
	public long writeFullContent(IContent content) throws IOException, BirtException {
		ContentWriterVisitor writer = new ContentWriterVisitor();
		writer.write(content, this);
		DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
		if (docExt != null) {
			return docExt.getIndex();
		}
		return -1;
	}

	/**
	 * use to writer the content into the disk.
	 *
	 */
	private static class ContentWriterVisitor extends ContentVisitorAdapter {

		/**
		 * write all the contents in the content into streams. After the method, the
		 * offset of the content is saved into the offset, and the offset of the parent
		 * is saved into the parent offset.
		 *
		 * @param content content object
		 * @param writer  writer used to write the content.
		 *
		 */
		public void write(IContent content, IReportContentWriter writer) throws BirtException {
			visit(content, writer);
		}

		protected void writeContent(IReportContentWriter writer, IContent content) {
			try {
				writer.writeContent(content);
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "write content failed");
			}
		}

		@Override
		public Object visitContent(IContent content, Object value) {
			IReportContentWriter writer = (IReportContentWriter) value;
			writeContent(writer, content);
			Iterator iter = content.getChildren().iterator();
			while (iter.hasNext()) {
				IContent child = (IContent) iter.next();
				visitContent(child, value);
			}
			return value;
		}

		@Override
		public Object visitPage(IPageContent page, Object value) {
			IReportContentWriter writer = (IReportContentWriter) value;
			writeContent(writer, page);

			// output all the page header
			Iterator iter = page.getHeader().iterator();
			while (iter.hasNext()) {
				IContent content = (IContent) iter.next();
				visitContent(content, writer);
			}

			// output all the page footer
			iter = page.getFooter().iterator();
			while (iter.hasNext()) {
				IContent content = (IContent) iter.next();
				visitContent(content, writer);
			}
			return value;
		}
	}
}
