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
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;

public class LabelContent extends TextContent implements ILabelContent {

	protected String helpTextKey;
	protected String labelText;
	protected String labelTextKey;

	LabelContent(ILabelContent label) {
		super(label);
		LabelContent originalLabel = (LabelContent) label;
		this.helpText = originalLabel.helpText;
		this.labelTextKey = originalLabel.labelTextKey;
		this.helpTextKey = originalLabel.helpTextKey;
		this.labelText = originalLabel.labelText;
	}

	@Override
	public int getContentType() {
		return LABEL_CONTENT;
	}

	LabelContent(ReportContent report) {
		super(report);
	}

	LabelContent(IContent content) {
		super(content);
	}

	@Override
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	@Override
	public String getHelpText() {
		if (helpText == null) {
			if (generateBy instanceof LabelItemDesign) {
				return ((LabelItemDesign) generateBy).getHelpText();
			}
		}
		return helpText;
	}

	public void setHelpKey(String helpKey) {
		this.helpTextKey = helpKey;
	}

	@Override
	public String getHelpKey() {
		if (helpTextKey == null) {
			if (generateBy instanceof LabelItemDesign) {
				return ((LabelItemDesign) generateBy).getHelpTextKey();
			}
		}
		return helpTextKey;
	}

	@Override
	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	@Override
	public String getLabelText() {
		if (labelText == null) {
			if (generateBy instanceof LabelItemDesign) {
				return ((LabelItemDesign) generateBy).getText();
			}
		}
		return labelText;
	}

	@Override
	public void setLabelKey(String labelKey) {
		this.labelTextKey = labelKey;
	}

	@Override
	public String getLabelKey() {
		if (labelTextKey == null) {
			if (generateBy instanceof LabelItemDesign) {
				return ((LabelItemDesign) generateBy).getTextKey();
			}
		}
		return labelTextKey;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.
	 * eclipse.birt.report.engine.content.IContentVisitor)
	 */
	@Override
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitLabel(this, value);
	}

	static final protected short FIELD_HELPTEXTKEY = 600;
	static final protected short FIELD_LABELTEXT = 601;
	static final protected short FIELD_LABELTEXTKEY = 602;

	@Override
	protected void writeFields(DataOutputStream out) throws IOException {
		super.writeFields(out);
		if (helpTextKey != null) {
			IOUtil.writeShort(out, FIELD_HELPTEXTKEY);
			IOUtil.writeString(out, helpTextKey);
		}
		if (labelText != null) {
			IOUtil.writeShort(out, FIELD_LABELTEXT);
			IOUtil.writeString(out, labelText);
		}
		if (labelTextKey != null) {
			IOUtil.writeShort(out, FIELD_LABELTEXTKEY);
			IOUtil.writeString(out, labelTextKey);
		}
	}

	@Override
	public boolean needSave() {
		if ((helpTextKey != null) || labelText != null ||

				labelTextKey != null) {
			return true;
		}
		return super.needSave();
	}

	@Override
	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_HELPTEXTKEY:
			helpTextKey = IOUtil.readString(in);
			break;
		case FIELD_LABELTEXT:
			labelText = IOUtil.readString(in);
			break;
		case FIELD_LABELTEXTKEY:
			labelTextKey = IOUtil.readString(in);
			break;
		default:
			super.readField(version, filedId, in, loader);
		}
	}

	@Override
	protected IContent cloneContent() {
		return new LabelContent(this);
	}
}
