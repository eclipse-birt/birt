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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.IReportContentWriter;

public class ReportContentWriterV3 implements IReportContentWriter {

	protected static Logger logger = Logger.getLogger(IReportContentWriter.class.getName());

	/**
	 * stream in the document, used to save the contents
	 */
	protected RAOutputStream cntStream;

	/**
	 * the offset of current node
	 */
	protected long cntOffset;

	/**
	 * the previous root offset.
	 */
	protected long rootOffset;

	public ReportContentWriterV3(IDocArchiveWriter writer, String name) throws IOException {
		cntStream = writer.createRandomAccessStream(name);
		// write the version information
		cntStream.writeInt(VERSION_1);
		cntOffset = 0;
		rootOffset = -1;
	}

	/**
	 * close the content writer
	 */
	public void close() {
		if (cntStream != null) {
			try {
				cntStream.close();
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Failed in close the writer", ex);
			}
			cntStream = null;
		}
	}

	/**
	 * get the current offset.
	 * 
	 * @return
	 */
	public long getOffset() {
		return cntOffset;
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
	public long writeContent(IContent content) throws IOException {
		// write the index into the stream
		updateIndex(content);

		// get the byte[] of the content
		buffer.reset();
		IOUtil.writeInt(bufferStream, content.getContentType());
		content.writeContent(bufferStream);
		bufferStream.flush();
		byte[] values = buffer.toByteArray();
		// write the content out as: length, data
		cntStream.seek(cntOffset + VERSION_SIZE);
		cntStream.writeInt(values.length);
		cntStream.write(values);
		cntOffset = cntOffset + 4 + values.length;

		DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
		if (docExt != null) {
			return docExt.getIndex();
		}
		return -1;
	}

	public long writeReport(IReportContent report) throws IOException {
		cntStream.seek(VERSION_SIZE);
		cntStream.writeLong(-1); // parent
		cntStream.writeLong(-1); // next
		cntStream.writeLong(-1); // first child
		cntOffset += INDEX_ENTRY_SIZE;

		// get the byte[] of the content
		buffer.reset();
		IOUtil.writeInt(bufferStream, IContent.REPORT_CONTENT);
		report.writeContent(bufferStream);
		bufferStream.flush();
		byte[] values = buffer.toByteArray();
		// write the content out as: length, data
		cntStream.seek(cntOffset + VERSION_SIZE);
		cntStream.writeInt(values.length);
		cntStream.write(values);
		cntOffset = cntOffset + 4 + values.length;

		return cntOffset;
	}

	/**
	 * save the content and its children into the streams.
	 * 
	 * @param content the content object
	 * @return the offset of this content object.
	 * @throws IOException
	 */
	public long writeFullContent(IContent content) throws IOException {
		long offset = writeContent(content);
		Iterator iter = content.getChildren().iterator();
		while (iter.hasNext()) {
			IContent child = (IContent) iter.next();
			writeFullContent(child);
		}
		return offset;
	}

	/**
	 * parent index
	 */
	final static long OFFSET_PARENT = 0;
	/**
	 * next index
	 */
	final static long OFFSET_NEXT = 8;
	/**
	 * first child index
	 */
	final static long OFFSET_CHILD = 16;

	final static int INDEX_ENTRY_SIZE = 24;

	final static int VERSION_SIZE = 4;

	protected final static int VERSION_1 = 1;

	/**
	 * There is a content start from the offset, which parent start from the
	 * parentOffset.
	 * 
	 * update the index for that object.
	 * 
	 * @param parentOffset
	 * @param offset
	 * @throws IOException
	 */
	protected void updateIndex(IContent content) throws IOException {
		long index = cntOffset;
		long previous = -1;
		// the parent document extension of current content
		DocumentExtension pDocExt = null;
		// the document extension of current content
		DocumentExtension docExt = new DocumentExtension(index);
		docExt.setContentId(content.getInstanceID().getUniqueID());
		content.setExtension(IContent.DOCUMENT_EXTENSION, docExt);

		IContent pContent = (IContent) content.getParent();
		if (pContent != null) {
			pDocExt = (DocumentExtension) pContent.getExtension(IContent.DOCUMENT_EXTENSION);
			if (pDocExt != null) {
				pDocExt.add(docExt);
				previous = docExt.getPrevious();
			} else {
				previous = rootOffset;
				rootOffset = index;
			}
		} else {
			previous = rootOffset;
			rootOffset = index;
		}

		cntStream.seek(VERSION_SIZE + index);
		cntStream.writeLong(docExt.getParent()); // parent
		cntStream.writeLong(docExt.getNext()); // next
		cntStream.writeLong(-1); // first child
		cntOffset += INDEX_ENTRY_SIZE;

		// update the links refer to this content
		if (previous == -1) {
			// it has no previous sibling
			// it may be the first element of its parent, always updates this field.
			if (docExt.getParent() != -1) {
				cntStream.seek(VERSION_SIZE + docExt.getParent() + OFFSET_CHILD);
				cntStream.writeLong(index);
			}
		} else {
			// update the previous link
			cntStream.seek(VERSION_SIZE + previous + OFFSET_NEXT);
			cntStream.writeLong(index);
		}
	}
}
