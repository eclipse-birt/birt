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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;

class ReportContentWriterV1 {

	protected static Logger logger = Logger.getLogger(ReportContentWriterV1.class.getName());

	protected RAOutputStream raStream;
	protected DataOutputStream stream;

	/**
	 * stack store the parent's offset
	 */
	protected Stack contents = new Stack();

	/**
	 * parent offset, the top of the contents stack
	 */
	protected long parentOffset;
	/**
	 * the offset of current node
	 */
	protected long offset;

	public ReportContentWriterV1(RAOutputStream aStream) {
		raStream = aStream;
		stream = new DataOutputStream(raStream);
		offset = 0;
		parentOffset = -1;
		contents.clear();
	}

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

	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	/**
	 * get the current offset.
	 * 
	 * @return
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * get the parent offset of the next content.
	 * 
	 * @return
	 */
	public long getParentOffset() {
		return parentOffset;
	}

	/**
	 * @throws IOException
	 * 
	 */
	protected void writeContent(DataOutputStream oo, Object object) throws IOException {
		if (object instanceof IContent) {
			IContent content = (IContent) object;
			IOUtil.writeInt(oo, content.getContentType());
			content.writeContent(oo);
		} else {
			IOUtil.writeInt(oo, IContent.SERIALIZE_CONTENT);
			IOUtil.writeObject(oo, object);
		}
	}

	/**
	 * 
	 * @param content
	 * @return the content object's offset.
	 * @throws IOException
	 */
	public long openObject(Object content) {
		buffer.reset();
		try {
			long ptr = offset;
			try {
				// save the content into core stream
				DataOutputStream oo = new DataOutputStream(buffer);
				writeContent(oo, content);
				oo.flush();
				oo.close();
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Failed in write the content", ex);
			}
			byte[] values = buffer.toByteArray();
			stream.writeInt(values.length);
			stream.write(values);
			// push the offset into the stack, and set the parentOffset to
			parentOffset = offset;
			offset = offset + 4 + values.length;
			// current offset
			contents.push(new Long(parentOffset));
			return ptr;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed in write the content", ex);
		}
		return -1;
	}

	/**
	 * return the parent offset of the content.
	 * 
	 * @param content
	 * @return
	 */
	public long closeObject(Object content) {
		long ptr = parentOffset;
		// the content is ended, popup the offset.
		contents.pop();
		if (contents.isEmpty()) {
			parentOffset = -1;
		} else {
			parentOffset = ((Long) contents.peek()).longValue();
		}
		return ptr;
	}
}
