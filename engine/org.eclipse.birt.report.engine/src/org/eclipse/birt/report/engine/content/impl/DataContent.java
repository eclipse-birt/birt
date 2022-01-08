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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;

public class DataContent extends TextContent implements IDataContent {

	protected Object value;

	protected String labelText;

	protected String labelKey;

	protected String helpKey;

	public int getContentType() {
		return DATA_CONTENT;
	}

	DataContent(ReportContent report) {
		super(report);
	}

	DataContent(IContent content) {
		super(content);
	}

	DataContent(IDataContent data) {
		super(data);
		this.value = data.getValue();
		this.labelText = data.getLabelText();
		this.labelKey = data.getLabelKey();
		this.helpKey = data.getHelpKey();
		this.helpText = data.getHelpText();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String text) {
		this.labelText = text;
	}

	public String getLabelKey() {
		return this.labelKey;
	}

	public void setLabelKey(String key) {
		this.labelKey = key;
	}

	public String getHelpText() {
		if (helpText == null) {
			if (generateBy instanceof DataItemDesign) {
				return ((DataItemDesign) generateBy).getHelpText();
			}
		}
		return helpText;
	}

	public String getHelpKey() {
		if (helpKey == null) {
			if (generateBy instanceof DataItemDesign) {
				return ((DataItemDesign) generateBy).getHelpTextKey();
			}
		}
		return helpKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.
	 * eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitData(this, value);
	}

	static final protected short FIELD_VALUE = 300;
	static final protected short FIELD_LAVELTEXT = 301;
	static final protected short FIELD_LABELKEY = 302;
	static final protected short FIELD_HELPKEY = 303;

	protected void writeFields(DataOutputStream out) throws IOException {
		super.writeFields(out);
		if (value != null) {
			boolean needSave = true;
			if (this.generateBy instanceof DataItemDesign) {
				DataItemDesign design = (DataItemDesign) generateBy;
				if (design.getMap() == null) {
					needSave = false;
				}
			}
			if (needSave) {
				IOUtil.writeShort(out, FIELD_VALUE);
				IOUtil.writeObject(out, value);
			}
		}
		if (labelText != null) {
			IOUtil.writeShort(out, FIELD_LAVELTEXT);
			IOUtil.writeString(out, labelText);
		}
		if (labelKey != null) {
			IOUtil.writeShort(out, FIELD_LABELKEY);
			IOUtil.writeString(out, labelKey);
		}
		if (helpKey != null) {
			IOUtil.writeShort(out, FIELD_HELPKEY);
			IOUtil.writeString(out, helpKey);
		}
	}

	public boolean needSave() {
		if (value != null) {
			if (this.generateBy instanceof DataItemDesign) {
				DataItemDesign design = (DataItemDesign) generateBy;
				MapDesign map = design.getMap();
				if (map != null && map.getRuleCount() != 0) {
					return true;
				}
			}
		}
		if (labelText != null || labelKey != null) {
			return true;
		}
		if (helpKey != null) {
			return true;
		}
		return super.needSave();
	}

	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_VALUE:
			value = IOUtil.readObject(in, loader);
			break;
		case FIELD_LAVELTEXT:
			labelText = IOUtil.readString(in);
			break;
		case FIELD_LABELKEY:
			labelKey = IOUtil.readString(in);
			break;
		case FIELD_HELPKEY:
			helpKey = IOUtil.readString(in);
			break;
		default:
			super.readField(version, filedId, in, loader);
		}
	}

	protected IContent cloneContent() {
		return new DataContent(this);
	}
}
