/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;

public class AutoTextContent extends TextContent implements IAutoTextContent
{
	protected byte type;

	public int getContentType( )
	{
		return AUTOTEXT_CONTENT;
	}

	public AutoTextContent( ReportContent report )
	{
		super( report );
	}

	public AutoTextContent( IContent content )
	{
		super( content );
	}
	
	public void setType ( byte type )
	{
		this.type  = type;
	}

	public byte getType ( )
	{
		return this.type;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitAutoText( this, value );
	}

}
