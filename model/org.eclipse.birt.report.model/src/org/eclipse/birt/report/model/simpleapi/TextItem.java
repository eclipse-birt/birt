/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.ITextItem;

public class TextItem extends ReportItem implements ITextItem
{

	public TextItem( TextItemHandle text )
	{
		super( text );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextItem#getContent()
	 */

	public String getContent( )
	{
		return ( (TextItemHandle) handle ).getContent( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextItem#getDisplayContent()
	 */

	public String getDisplayContent( )
	{
		return ( (TextItemHandle) handle ).getDisplayContent( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextItem#setContent(java.lang.String)
	 */

	public void setContent( String value ) throws SemanticException
	{
		( (TextItemHandle) handle ).setContent( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextItem#getContentType()
	 */

	public String getContentType( )
	{
		return ( (TextItemHandle) handle ).getContentType( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextItem#setContentType(java.lang.String)
	 */

	public void setContentType( String contentType ) throws SemanticException
	{
		( (TextItemHandle) handle ).setContentType( contentType );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextItem#getContentKey()
	 */

	public String getContentKey( )
	{
		return ( (TextItemHandle) handle ).getContentKey( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextItem#setContentKey(java.lang.String)
	 */

	public void setContentKey( String resourceKey ) throws SemanticException
	{
		( (TextItemHandle) handle ).setContentKey( resourceKey );
	}
}
