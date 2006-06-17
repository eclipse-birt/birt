/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

public class TextArea extends AbstractArea implements ITextArea
{
	protected String text;
	
	protected FontInfo fi;

	TextArea(ITextContent textContent, String text, FontInfo fi)
	{
		super(textContent);
		this.text = text;
		this.fi = fi;

		style.setMarginBottom(CSSConstants.CSS_NONE_VALUE);
		style.setMarginTop(CSSConstants.CSS_NONE_VALUE);
		style.setMarginLeft(CSSConstants.CSS_NONE_VALUE);
		style.setMarginRight(CSSConstants.CSS_NONE_VALUE);
		
		style.setBorderBottom(CSSConstants.CSS_NONE_VALUE);
		style.setBorderTop(CSSConstants.CSS_NONE_VALUE);
		style.setBorderLeft(CSSConstants.CSS_NONE_VALUE);
		style.setBorderRight(CSSConstants.CSS_NONE_VALUE);
		
		style.setPaddingBottom(CSSConstants.CSS_NONE_VALUE);
		style.setPaddingTop(CSSConstants.CSS_NONE_VALUE);
		style.setPaddingLeft(CSSConstants.CSS_NONE_VALUE);
		style.setPaddingRight(CSSConstants.CSS_NONE_VALUE);
	}

	public String getText()
	{
		return text;
	}
	
	public FontInfo getFontInfo()
	{
		return this.fi;
	}
	
	public void accept(IAreaVisitor visitor)
	{
		visitor.visitText(this);
	}
	
}
