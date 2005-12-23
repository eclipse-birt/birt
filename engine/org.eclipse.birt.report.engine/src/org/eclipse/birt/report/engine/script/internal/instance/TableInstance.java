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

import org.eclipse.birt.report.engine.api.script.instance.ITableInstance;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of a table
 */
public class TableInstance extends ReportItemInstance implements ITableInstance
{

	public TableInstance( TableContent table, ExecutionContext context )
	{
		super( table, context );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#getCaption()
	 */
	public String getCaption( )
	{
		return ( ( TableContent ) content ).getCaption( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#setCaption(java.lang.String)
	 */
	public void setCaption( String caption )
	{
		( ( TableContent ) content ).setCaption( caption );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#getCaptionKey()
	 */
	public String getCaptionKey( )
	{
		return ( ( TableContent ) content ).getCaptionKey( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#setCaptionKey(java.lang.String)
	 */
	public void setCaptionKey( String captionKey )
	{
		( ( TableContent ) content ).setCaptionKey( captionKey );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#getRepeatHeader()
	 */
	public boolean getRepeatHeader( )
	{
		return ( ( TableContent ) content ).isHeaderRepeat( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#setRepeatHeader(boolean)
	 */
	public void setRepeatHeader( boolean repeat )
	{
		( ( TableContent ) content ).setHeaderRepeat( repeat );
	}
}
