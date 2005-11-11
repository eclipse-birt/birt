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
package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ITextContent;

public class TextContent extends AbstractContent implements ITextContent
{
	String text;

	public TextContent(ReportContent report)
	{
		super(report);
	}

	public TextContent(IContent content)
	{
		super(content);
	}
	
	public String getText( )
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public void accept( IContentVisitor visitor , Object value)
	{
		visitor.visitText(this, value);
	}
}
