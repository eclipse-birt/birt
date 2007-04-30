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

package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * Processes an extended item.
 */
public class ExtendedItemExecutor extends ContainerExecutor
{

	public ExtendedItemExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.EXTENDEDITEM );
	}

	protected void doExecute( ) throws Exception
	{
		executeQuery( );
	}

	protected IContent doCreateContent( )
	{
		throw new java.lang.IllegalStateException(
				"can't create the content for extended item" );
	}

	protected ReportItemExecutor doCreateExecutor( long offset )
			throws Exception
	{
		if ( offset != -1 )
		{
			IContent content = reader.loadContent( offset );
			InstanceID iid = content.getInstanceID( );
			ReportItemDesign design = (ReportItemDesign) report.getDesign( )
					.getReportItemByID( iid.getComponentID( ) );
			return manager.createExecutor( this, design, offset );
		}
		return null;
	}

	/**
	 * adjust the nextItem to the nextContent.
	 * 
	 * before call this method, both the nextContent and the nextFragment can't
	 * be NULL.
	 * 
	 * @return
	 */
	protected void doSkipToExecutor( InstanceID id, long offset )
			throws Exception
	{
		// must get the instance id from the locator.
		// the offset has been assign to the executor.
	}

	public void close( )
	{
		closeQuery( );
		super.close( );
	}
}
