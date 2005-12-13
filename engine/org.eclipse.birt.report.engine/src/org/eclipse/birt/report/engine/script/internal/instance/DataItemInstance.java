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

import org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance;
import org.eclipse.birt.report.engine.content.impl.TextContent;

/**
 * A class representing the runtime state of a data item
 */
public class DataItemInstance extends ReportItemInstance implements IDataItemInstance
{

	public DataItemInstance( TextContent data )
	{
		super( data );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance#getText()
	 */
	public String getText( )
	{
		return ( ( TextContent ) content ).getText( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance#setText(java.lang.String)
	 */
	public void setText( String value )
	{
		( ( TextContent ) content ).setText( value );
	}

}
