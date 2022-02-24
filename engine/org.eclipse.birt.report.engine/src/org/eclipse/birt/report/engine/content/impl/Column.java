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

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.CompositeStyle;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 * column content object
 * 
 */
public class Column implements IColumn {
	transient protected ReportContent report;

	transient protected CSSEngine cssEngine;

	protected DimensionType width;

	protected String styleClass;

	protected InstanceID instanceId;

	protected String visibleFormat;

	protected IStyle inlineStyle;

	transient protected IStyle style;

	transient protected IStyle computedStyle;

	transient protected Object generateBy;

	protected Boolean isColumnHeader = null;

	protected boolean isRepeated;

	/**
	 * constructor use by serialize and deserialize
	 */
	public Column(IReportContent report) {
		assert (report != null && report instanceof ReportContent);
		this.report = (ReportContent) report;
		this.cssEngine = this.report.getCSSEngine();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IColumn#getStyle()
	 */
	public IStyle getStyle() {
		if (style == null) {
			if (inlineStyle == null) {
				inlineStyle = report.createStyle();
			}
			String styleClass = getStyleClass();
			IStyle classStyle = report.findStyle(styleClass);
			style = new CompositeStyle(classStyle, inlineStyle);
		}
		return style;
	}

	/*
	 * Return this column is a column header or not.
	 */
	public boolean isColumnHeader() {
		if (null != isColumnHeader) {
			return isColumnHeader.booleanValue();
		}

		if (generateBy instanceof ColumnDesign) {
			return ((ColumnDesign) generateBy).isColumnHeader();
		}
		return false;
	}

	public void setColumnHeaderState(boolean isColumnHeader) {
		this.isColumnHeader = isColumnHeader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IColumn#getWidth()
	 */
	public DimensionType getWidth() {
		if (width != null) {
			return width;
		}
		if (generateBy instanceof ColumnDesign) {
			return ((ColumnDesign) generateBy).getWidth();
		}
		return null;
	}

	public void setWidth(DimensionType width) {
		this.width = width;
	}

	public String getStyleClass() {
		if (styleClass != null) {
			return styleClass;
		}
		if (generateBy instanceof ColumnDesign) {
			return ((ColumnDesign) generateBy).getStyleName();
		}
		return null;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public InstanceID getInstanceID() {
		return instanceId;
	}

	public void setInstanceID(InstanceID id) {
		this.instanceId = id;
	}

	public String getVisibleFormat() {
		return visibleFormat;
	}

	public void setVisibleFormat(String visibleFormat) {
		this.visibleFormat = visibleFormat;
	}

	/**
	 * @param style The style to set.
	 */
	public void setInlineStyle(IStyle style) {
		this.inlineStyle = style;
		this.style = null;
		this.computedStyle = null;
	}

	public IStyle getInlineStyle() {
		return inlineStyle;
	}

	/**
	 * @param generateBy The generateBy to set.
	 */
	public void setGenerateBy(Object generateBy) {
		this.generateBy = generateBy;
	}

	public Object getGenerateBy() {
		return generateBy;
	}

	/**
	 * object document column version
	 */
	static final protected int VERSION = 0;

	final static int FIELD_NONE = -1;
	final static int FIELD_WIDTH = 0;
	final static int FIELD_STYLECLASS = 1;
	final static int FIELD_INSTANCE_ID = 2;
	final static int FIELD_VISIBLE_FORMAT = 3;
	final static int FIELD_INLINESTYLE_VERSION_0 = 8;
	final static int FIELD_ISCOLUMNHEADER = 9;
	// change the way of writing and reading the style.
	final static int FIELD_INLINESTYLE_VERSION_1 = 10;
	final static int FIELD_ISREPEAT = 11;

	protected void writeFields(DataOutputStream out) throws IOException {
		if (width != null) {
			IOUtil.writeInt(out, FIELD_WIDTH);
			width.writeObject(out);
		}
		if (styleClass != null) {
			IOUtil.writeInt(out, FIELD_STYLECLASS);
			IOUtil.writeString(out, styleClass);
		}
		if (instanceId != null) {
			IOUtil.writeInt(out, FIELD_INSTANCE_ID);
			IOUtil.writeString(out, instanceId.toString());
		}
		if (visibleFormat != null) {
			IOUtil.writeInt(out, FIELD_VISIBLE_FORMAT);
			IOUtil.writeString(out, visibleFormat);
		}
		if (inlineStyle != null) {
			if (!inlineStyle.isEmpty()) {
				IOUtil.writeInt(out, FIELD_INLINESTYLE_VERSION_1);
				inlineStyle.write(out);
			}
		}
		if (isColumnHeader != null && isColumnHeader) {
			IOUtil.writeInt(out, FIELD_ISCOLUMNHEADER);
			IOUtil.writeBool(out, isColumnHeader);
		}
		if (isRepeated) {
			IOUtil.writeInt(out, FIELD_ISREPEAT);
			IOUtil.writeBool(out, isRepeated);
		}
	}

	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_WIDTH:
			width = new DimensionType();
			width.readObject(in);
			break;
		case FIELD_STYLECLASS:
			styleClass = IOUtil.readString(in);
			break;
		case FIELD_INSTANCE_ID:
			String value = IOUtil.readString(in);
			instanceId = InstanceID.parse(value);
			break;
		case FIELD_VISIBLE_FORMAT:
			visibleFormat = IOUtil.readString(in);
			break;
		case FIELD_INLINESTYLE_VERSION_0:
			String styleCssText = IOUtil.readString(in);
			if (styleCssText != null && styleCssText.length() != 0) {
				inlineStyle = new StyleDeclaration(cssEngine);
				inlineStyle.setCssText(styleCssText);
			}
			break;
		case FIELD_INLINESTYLE_VERSION_1:
			IStyle style = new StyleDeclaration(cssEngine);
			if (null != style) {
				style.read(in);
				if (!style.isEmpty()) {
					inlineStyle = style;
				}
			}
			break;
		case FIELD_ISCOLUMNHEADER:
			isColumnHeader = Boolean.valueOf(IOUtil.readBool(in));
			break;
		case FIELD_ISREPEAT:
			isRepeated = Boolean.valueOf(IOUtil.readBool(in));
			break;
		}
	}

	public void readObject(DataInputStream in, ClassLoader loader) throws IOException {
		int version = IOUtil.readInt(in);
		int filedId = IOUtil.readInt(in);
		while (filedId != FIELD_NONE) {
			readField(version, filedId, in, loader);
			filedId = IOUtil.readInt(in);
		}
	}

	public void writeObject(DataOutputStream out) throws IOException {
		IOUtil.writeInt(out, VERSION);
		writeFields(out);
		IOUtil.writeInt(out, FIELD_NONE);
	}

	/**
	 * @return the cssEngine
	 */
	public CSSEngine getCssEngine() {
		return cssEngine;
	}

	public boolean hasDataItemsInDetail() {
		if (generateBy instanceof ColumnDesign) {
			return ((ColumnDesign) generateBy).hasDataItemsInDetail();
		}
		return false;
	}

	public boolean isRepeated() {
		return isRepeated;
	}

	public void setRepeated(boolean isRepeated) {
		this.isRepeated = isRepeated;

	}

	public IStyle getComputedStyle() {
		return getStyle();
	}
}
