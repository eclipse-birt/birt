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

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.ITextItemInstance;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;

/**
 * A class representing the runtime state of a text item
 */
public class TextItemInstance extends ForeignTextInstance implements ITextItemInstance
{

	public TextItemInstance( TextContent content )
	{
		setContent( content );
	}

	public TextItemInstance( ForeignContent content )
	{
		super( content );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITextItemInstance#getText()
	 */
	public String getText( )
	{
		if ( content instanceof TextContent )
			return ( ( TextContent ) content ).getText( );
		return super.getText( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITextItemInstance#setText(java.lang.String)
	 */
	public void setText( String value )
	{
		if ( content instanceof TextContent )
			( ( TextContent ) content ).setText( value );
		else
			super.setText( value );
	}

}
