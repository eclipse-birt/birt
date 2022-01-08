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
import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

public class ForeignContent extends AbstractContent implements IForeignContent {

	protected String rawType;
	protected Object rawValue;

	protected String rawKey;

	protected String altText;
	protected String altTextKey;

	private boolean jTidy = true;

	ForeignContent(IForeignContent foreign) {
		super(foreign);
		this.rawType = foreign.getRawType();
		this.rawKey = foreign.getRawKey();
		this.rawValue = foreign.getRawValue();
		this.altText = foreign.getAltText();
		this.altTextKey = foreign.getAltTextKey();
	}

	public int getContentType() {
		return FOREIGN_CONTENT;
	}

	ForeignContent(ReportContent report) {
		super(report);
	}

	public ForeignContent(IContent content) {
		super(content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.
	 * eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitForeign(this, value);
	}

	public String getRawType() {
		return rawType;
	}

	public void setRawKey(String rawKey) {
		this.rawKey = rawKey;
	}

	public String getRawKey() {
		return this.rawKey;
	}

	/**
	 * @return Returns the content. Caller knows how to cast this object
	 */
	public Object getRawValue() {
		return rawValue;
	}

	/**
	 * @param rawType The rawType to set.
	 */
	public void setRawType(String rawType) {
		this.rawType = rawType;
	}

	/**
	 * @param rawValue The rawValue to set.
	 */
	public void setRawValue(Object rawValue) {
		this.rawValue = rawValue;
	}

	/**
	 * @param contentType
	 * @param content
	 * @return
	 */
	public static String getTextRawType(String contentType, Object content) {
		if (TextItemDesign.PLAIN_TEXT.equals(contentType)) {
			return IForeignContent.TEXT_TYPE;
		}
		if (TextItemDesign.HTML_TEXT.equals(contentType)) {
			return IForeignContent.HTML_TYPE;
		}
		String text = content == null ? "" : content.toString().trim();
		if (text.length() > 6) {
			if ("<html>".equalsIgnoreCase(text.substring(0, 6))) {
				return IForeignContent.HTML_TYPE;
			}
		}
		return IForeignContent.TEXT_TYPE;
	}

	public String getAltText() {
		if (altText == null) {
			if (generateBy instanceof ExtendedItemDesign) {
				// This is for backward compatibility. The alt text property was
				// stored as string and will not be written in the content.
				Expression expr = ((ExtendedItemDesign) generateBy).getAltText();
				if (expr != null && expr.getType() == Expression.CONSTANT) {
					return expr.getScriptText();
				}
				return null;
			}
		}
		return altText;
	}

	public String getAltTextKey() {
		if (altTextKey == null) {
			if (generateBy instanceof ExtendedItemDesign) {
				return ((ExtendedItemDesign) generateBy).getAltTextKey();
			}
		}
		return altTextKey;
	}

	public void setAltTextKey(String key) {
		altTextKey = key;
	}

	/**
	 * @param altText The altText to set.
	 */
	public void setAltText(String altText) {
		this.altText = altText;
	}

	public void setJTidy(boolean jTidy) {
		this.jTidy = jTidy;
	}

	public boolean isJTidy() {
		return jTidy;
	}

	static final protected short FIELD_RAW_TYPE = 400;
	static final protected short FIELD_RAWVALUE = 401;
	static final protected short FIELD_ALTTEXT = 402;
	static final protected short FIELD_ALTTEXTKEY = 403;
	static final protected short FIELD_RAWKEY = 404;
	static final protected short FIELD_JTIDY = 405;

	protected void writeFields(DataOutputStream out) throws IOException {
		super.writeFields(out);
		if (rawType != null) {
			IOUtil.writeShort(out, FIELD_RAW_TYPE);
			IOUtil.writeString(out, rawType);
		}
		if (rawValue != null) {
			IOUtil.writeShort(out, FIELD_RAWVALUE);
			IOUtil.writeObject(out, rawValue);
		}
		if (altText != null) {
			IOUtil.writeShort(out, FIELD_ALTTEXT);
			IOUtil.writeString(out, altText);
		}
		if (altTextKey != null) {
			IOUtil.writeShort(out, FIELD_ALTTEXTKEY);
			IOUtil.writeString(out, altTextKey);
		}
		if (rawKey != null) {
			IOUtil.writeShort(out, FIELD_RAWKEY);
			IOUtil.writeString(out, rawKey);
		}
		if (jTidy != true) {
			IOUtil.writeShort(out, FIELD_JTIDY);
			IOUtil.writeBool(out, jTidy);
		}
	}

	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_RAW_TYPE:
			rawType = IOUtil.readString(in);
			break;
		case FIELD_RAWVALUE:
			rawValue = IOUtil.readObject(in, loader);
			if (rawType.equals(TEMPLATE_TYPE) && rawValue instanceof HashMap) {
				rawValue = new Object[] { null, rawValue };
			}
			break;
		case FIELD_ALTTEXT:
			altText = IOUtil.readString(in);
			break;
		case FIELD_ALTTEXTKEY:
			altTextKey = IOUtil.readString(in);
			break;
		case FIELD_RAWKEY:
			rawKey = IOUtil.readString(in);
			break;
		case FIELD_JTIDY:
			jTidy = IOUtil.readBool(in);
			break;
		default:
			super.readField(version, filedId, in, loader);
		}
	}

	public boolean needSave() {
		if (rawType != null) {
			return true;
		}
		if (rawValue != null || rawKey != null) {
			return true;
		}
		if (altText != null || altTextKey != null) {
			return true;
		}
		if (jTidy == false) {
			return true;
		}
		return super.needSave();
	}

	protected IContent cloneContent() {
		return new ForeignContent(this);
	}

}
