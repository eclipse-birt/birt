/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class BookmarkContent implements Cloneable {

	private final static int ENDING = 0;

	private final static int BOOKMARK = 1;
	private final static int ELEMENT_ID = 2;
	private final static int PAGE_NUMBER = 3;

	private String bookmark;
	private long elementId = -1;
	private long pageNumber = -1;

	public BookmarkContent() {

	}

	public BookmarkContent(String bookmark, long elementId) {
		this.bookmark = bookmark;
		this.elementId = elementId;
	}

	public String getBookmark() {
		return bookmark;
	}

	public long getElementId() {
		return elementId;
	}

	public void setBookmark(String value) {
		this.bookmark = value;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	public long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(long number) {
		pageNumber = number;
	}

	/**
	 * suggest to use copy()
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException ex) {
			// impossible
		}
		return null;
	}

	/**
	 * It's suggested to use copy() instead of new an object if the new object keeps
	 * the same bookmark value
	 *
	 * @return
	 */
	public BookmarkContent copy() {
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException ex) {
			// impossible
		}
		return (BookmarkContent) obj;
	}

	public void writeStream(DataOutput out) throws IOException {
		if (bookmark != null) {
			out.writeInt(BOOKMARK);
			out.writeUTF(bookmark);
		}
		if (elementId != -1) {
			out.writeInt(ELEMENT_ID);
			out.writeLong(elementId);
		}
		if (pageNumber != -1) {
			out.writeInt(PAGE_NUMBER);
			out.writeLong(pageNumber);
		}
		out.writeInt(ENDING); // ending mark or empty mark
	}

	public void readStream(DataInput in) throws IOException {
		int type = in.readInt();
		while (type != ENDING) {
			switch (type) {
			case BOOKMARK:
				bookmark = in.readUTF();
				break;
			case ELEMENT_ID:
				elementId = in.readLong();
				break;
			case PAGE_NUMBER:
				pageNumber = in.readLong();
				break;
			}
			type = in.readInt();
		}
	}
}
