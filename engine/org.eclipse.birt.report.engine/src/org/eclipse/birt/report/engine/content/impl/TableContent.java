/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * 
 * the table content object which contains columns object and row objects
 * 
 */
public class TableContent extends AbstractContent implements ITableContent {

	protected List columns = new ArrayList();
	protected String caption;
	protected String captionKey;
	protected String summary;

	protected Boolean headerRepeat;

	TableContent(ITableContent table) {
		super(table);
		this.caption = table.getCaption();
		this.captionKey = table.getCaptionKey();
		this.headerRepeat = Boolean.valueOf(table.isHeaderRepeat());
		this.columns = table.getColumns();
		this.summary = table.getSummary();
	}

	public int getContentType() {
		return TABLE_CONTENT;
	}

	public void setHeaderRepeat(boolean headerRepeat) {
		if (generateBy instanceof TableItemDesign) {
			boolean repeatHeader = ((TableItemDesign) generateBy).isRepeatHeader();
			if (repeatHeader == headerRepeat) {
				this.headerRepeat = null;
				return;
			}
		}
		this.headerRepeat = Boolean.valueOf(headerRepeat);
	}

	public boolean isHeaderRepeat() {
		if (headerRepeat != null) {
			return headerRepeat.booleanValue();
		}
		if (generateBy instanceof TableItemDesign) {
			return ((TableItemDesign) generateBy).isRepeatHeader();
		}
		return false;
	}

	/**
	 * constructor
	 * 
	 * @param item the table deign
	 */
	TableContent(IReportContent report) {
		super(report);
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitTable(this, value);
	}

	/**
	 * @return Returns the caption.
	 */
	public String getCaption() {
		if (caption == null) {
			if (generateBy instanceof TableItemDesign) {
				return ((TableItemDesign) generateBy).getCaption();
			} else if (generateBy instanceof GridItemDesign) {
				return ((GridItemDesign) generateBy).getCaption();
			}
		}
		return caption;
	}

	/**
	 * @param caption The caption to set.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void setCaptionKey(String key) {
		this.captionKey = key;
	}

	public String getCaptionKey() {
		if (captionKey == null) {
			if (generateBy instanceof TableItemDesign) {
				return ((TableItemDesign) generateBy).getCaptionKey();
			} else if (generateBy instanceof GridItemDesign) {
				return ((GridItemDesign) generateBy).getCaptionKey();
			}
		}
		return captionKey;
	}

	public int getColumnCount() {
		return columns.size();
	}

	public IColumn getColumn(int index) {
		return (IColumn) columns.get(index);
	}

	public void addColumn(IColumn column) {
		this.columns.add(column);
	}

	public ITableBandContent getHeader() {
		return getTableBand(ITableBandContent.BAND_HEADER);
	}

	public ITableBandContent getFooter() {
		return getTableBand(ITableBandContent.BAND_FOOTER);
	}

	protected ITableBandContent getTableBand(int type) {
		ITableBandContent tableBand;
		if (children == null) {
			return null;
		}
		Iterator iter = children.iterator();
		while (iter.hasNext()) {
			Object child = iter.next();
			if (child instanceof ITableBandContent) {
				tableBand = (ITableBandContent) child;
				if (tableBand.getBandType() == type) {
					return tableBand;
				}
			}
		}
		return null;
	}

	static final protected short FIELD_COLUMNS = 1000;
	static final protected short FIELD_CAPTION = 1001;
	static final protected short FIELD_CAPTIONKEY = 1002;
	static final protected short FIELD_HEADERREPEAT = 1003;
	static final protected short FIELD_SUMMARY = 1004;

	protected void writeFields(DataOutputStream out) throws IOException {
		super.writeFields(out);
		if (columns != null) {
			IOUtil.writeShort(out, FIELD_COLUMNS);
			Column column;
			IOUtil.writeInt(out, columns.size());
			for (int i = 0; i < columns.size(); i++) {
				column = (Column) columns.get(i);
				column.writeObject(out);
			}
		}
		if (caption != null) {
			IOUtil.writeShort(out, FIELD_CAPTION);
			IOUtil.writeString(out, caption);
		}
		if (captionKey != null) {
			IOUtil.writeShort(out, FIELD_CAPTIONKEY);
			IOUtil.writeString(out, captionKey);
		}
		if (headerRepeat != null) {
			IOUtil.writeShort(out, FIELD_HEADERREPEAT);
			IOUtil.writeBool(out, headerRepeat.booleanValue());
		}
		if (summary != null) {
			IOUtil.writeShort(out, FIELD_SUMMARY);
			IOUtil.writeString(out, summary);
		}
	}

	public boolean needSave() {
		return true;
	}

	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_COLUMNS:
			int columnsSize = IOUtil.readInt(in);
			for (int i = 0; i < columnsSize; i++) {
				Column column = new Column(report);
				column.readObject(in, loader);
				addColumn(column);
			}
			break;
		case FIELD_CAPTION:
			caption = IOUtil.readString(in);
			break;
		case FIELD_CAPTIONKEY:
			captionKey = IOUtil.readString(in);
			break;
		case FIELD_SUMMARY:
			summary = IOUtil.readString(in);
			break;
		case FIELD_HEADERREPEAT:
			headerRepeat = Boolean.valueOf(IOUtil.readBool(in));
			break;
		default:
			super.readField(version, filedId, in, loader);
		}
	}

	public List getColumns() {
		return this.columns;
	}

	protected IContent cloneContent() {
		return new TableContent(this);
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @returns Return the summary
	 */
	public String getSummary() {
		if (summary == null) {
			if (generateBy instanceof TableItemDesign) {
				return ((TableItemDesign) generateBy).getSummary();
			} else if (generateBy instanceof GridItemDesign) {
				return ((GridItemDesign) generateBy).getSummary();
			}
		}
		return summary;
	}
}
