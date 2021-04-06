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
package org.eclipse.birt.report.engine.emitter.ods;

import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public abstract class SheetData {

	// TODO: change type to int
	public static final int DATE = 0;
	public static final int NUMBER = 1;
	public static final int STRING = 2;
	public static final int CALENDAR = 3;
	public static final int CDATETIME = 4;
	public static final int IMAGE = 5;
	public static final int BOOLEAN = 6;

	private int rspan = 0;

	protected int rowIndex;

	private int startX;

	private int endX;

	protected int dataType = STRING;

	protected Object value;

	protected StyleEntry styleId = null;

	protected int rowSpanInDesign;

	private HyperlinkInfo hyperLink;

	private BookmarkDef bookmark;

	private BookmarkDef linkedBookmark;

	protected float height;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public float getHeight() {
		return Math.max(height, 0);
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public BookmarkDef getLinkedBookmark() {
		return linkedBookmark;
	}

	public void setLinkedBookmark(BookmarkDef linkedBookmark) {
		this.linkedBookmark = linkedBookmark;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int datatype) {
		this.dataType = datatype;
	}

	public boolean isBlank() {
		return false;
	}

	public int getRowSpanInDesign() {
		return rowSpanInDesign;
	}

	public void setRowSpanInDesign(int rowSpan) {
		this.rowSpanInDesign = rowSpan;
	}

	public void setStyleId(StyleEntry id) {
		this.styleId = id;
	}

	public StyleEntry getStyleId() {
		return styleId;
	}

	public HyperlinkInfo getHyperlinkDef() {
		return hyperLink;
	}

	public void setHyperlinkDef(HyperlinkInfo def) {
		this.hyperLink = def;
	}

	public int getRowSpan() {
		return rspan;
	}

	public void setRowSpan(int rs) {
		if (rs > 0) {
			this.rspan = rs;
		}
	}

	public void decreasRowSpanInDesign() {
		rowSpanInDesign--;
	}

	public BookmarkDef getBookmark() {
		return bookmark;
	}

	public void setBookmark(BookmarkDef bookmark) {
		this.bookmark = bookmark;
	}

	public int getElementType() {
		return -1;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndX() {
		return endX;
	}
}
