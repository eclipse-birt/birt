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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IForeignContent;

public class ForeignContent extends AbstractContentWrapper implements IForeignContent {

	IForeignContent foreignContent;

	protected String altText;
	protected String altTextKey;

	public ForeignContent(IForeignContent content) {
		super(content);
		foreignContent = content;
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
		return foreignContent.getRawType();
	}

	/**
	 * @return Returns the content. Caller knows how to cast this object
	 */
	public Object getRawValue() {
		return foreignContent.getRawValue();
	}

	/**
	 * @param rawType The rawType to set.
	 */
	public void setRawType(String rawType) {
		foreignContent.setRawType(rawType);
	}

	/**
	 * @param rawValue The rawValue to set.
	 */
	public void setRawValue(Object rawValue) {
		foreignContent.setRawValue(rawValue);
	}

	public String getAltText() {
		return foreignContent.getAltText();
	}

	public String getAltTextKey() {
		return foreignContent.getAltTextKey();
	}

	public void setAltTextKey(String key) {
		foreignContent.setAltTextKey(key);
	}

	public void setAltText(String altText) {
		foreignContent.setAltText(altText);
	}

	public String getRawKey() {
		return foreignContent.getRawKey();
	}

	public void setRawKey(String rawKey) {
		foreignContent.setRawKey(rawKey);

	}

	public void setJTidy(boolean jTidy) {
		foreignContent.setJTidy(jTidy);

	}

	public boolean isJTidy() {
		return foreignContent.isJTidy();

	}
}
