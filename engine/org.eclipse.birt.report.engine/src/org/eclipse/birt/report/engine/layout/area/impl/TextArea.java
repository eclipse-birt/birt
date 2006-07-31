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
		removePadding( );
		removeBorder( );
		removeMargin();
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
