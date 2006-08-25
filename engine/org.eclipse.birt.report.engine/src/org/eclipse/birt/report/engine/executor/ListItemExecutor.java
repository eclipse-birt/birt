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

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.script.internal.ListScriptExecutor;

/**
 * Defines execution logic for a List report item.
 * 
 * @version $Revision: 1.39 $ $Date: 2006/06/22 08:38:23 $
 */
public class ListItemExecutor extends ListingElementExecutor
{

	/**
	 * @param context
	 *            execution context
	 * @param visitor
	 *            visitor object for driving the execution
	 */
	protected ListItemExecutor( ExecutorManager manager )
	{
		super( manager );
	}

	/**
	 * Execute a listint and create the contents.
	 * 
	 * List create a serials of contents.
	 * 
	 * The execution process is:
	 * 
	 * <li> create an container which will contain all the contents it creates.
	 * <li> push it into the stack
	 * <li> open query
	 * <li> process action, bookmark, style and visibility
	 * <li> call the onCreate if necessary
	 * <li> call emitter to start the list
	 * <li> access the query
	 * <li> call emitter to end the list
	 * <li> close the query.
	 * <li> pop up the container.
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#load(org.eclipse.birt.report.engine.ir.ReportItemDesign,
	 *      org.eclipse.birt.report.engine.emitter.IReportEmitter)
	 */
	public IContent execute( )
	{
		ListItemDesign listDesign = ( ListItemDesign ) getDesign();

		IListContent listContent = report.createListContent( );
		setContent(listContent);
		
		executeQuery( );
		
		initializeContent( listDesign, listContent );

		processAction( listDesign, listContent );
		processBookmark( listDesign, listContent );
		processStyle( listDesign, listContent );
		processVisibility( listDesign, listContent );

		if ( context.isInFactory( ) )
		{
			ListScriptExecutor.handleOnCreate(
					listContent, context );
		}
		startTOCEntry( listContent );
		if ( emitter != null )
		{
			emitter.startList( listContent );
		}
		
		//prepare to execute the children
		prepareToExecuteChildren();
		
		return listContent;
	}
	
	public void close( )
	{
		IListContent listContent = (IListContent)getContent();
		if ( emitter != null )
		{
			emitter.endList( listContent );
		}

		finishTOCEntry( );
		closeQuery( );
	}
}