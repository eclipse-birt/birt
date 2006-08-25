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

import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.script.internal.TableScriptExecutor;

/**
 * Defines execution logic for a List report item.
 * <p>
 * Currently table header and footer do not support data items
 * 
 * <p>
 * if the table contains any drop cells, we need buffer the cell contents unitl
 * we resolved all the drop cells. we resovles the drop cells at the end of each
 * group as the drop cells can only start from the group header and terminate in
 * the group footer.
 * 
 * @version $Revision: 1.52 $ $Date: 2006/06/22 08:38:23 $
 */
public class TableItemExecutor extends ListingElementExecutor
{
	protected static Logger logger = Logger.getLogger( TableItemExecutor.class
			.getName( ) );

	int rowId = 0;

	/**
	 * @param context
	 *            execution context
	 * @param visitor
	 *            visitor object for driving the execution
	 */
	protected TableItemExecutor( ExecutorManager manager )
	{
		super( manager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		super.reset( );
		rowId = 0;
	}
	
	public IContent execute( )
	{
		TableItemDesign tableDesign = ( TableItemDesign ) getDesign();

		ITableContent tableContent = report.createTableContent( );
		setContent(tableContent);
		
		executeQuery( );
		
		initializeContent( tableDesign, tableContent );
		processStyle( tableDesign, tableContent );
		processVisibility( tableDesign, tableContent );
		processBookmark( tableDesign, tableContent );
		processAction( tableDesign, tableContent );

		for ( int i = 0; i < tableDesign.getColumnCount( ); i++ )
		{
			ColumnDesign columnDesign = tableDesign.getColumn( i );

			Column column = new Column( report );
			column.setGenerateBy( columnDesign );
			
			InstanceID iid = new InstanceID( null, columnDesign.getID( ), null );
			column.setInstanceID( iid );
			
			processColumnVisibility( columnDesign, column );
			
			tableContent.addColumn( column );
		}
		if ( context.isInFactory( ) )
		{
			TableScriptExecutor.handleOnCreate( ( TableContent ) tableContent,
					context );
		}

		startTOCEntry( tableContent );
		if ( emitter != null )
		{
			emitter.startTable( tableContent );
		}

		//prepare to execute the children
		prepareToExecuteChildren();
		return tableContent;
	}
	
	public void close( )
	{

		ITableContent tableContent = (ITableContent) getContent( );
		if ( emitter != null )
		{
			emitter.endTable( tableContent );
		}

		finishTOCEntry( );
		closeQuery( );
	}
	

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor executor = super.getNextChild( );
		if ( executor instanceof TableBandExecutor )
		{
			TableBandExecutor bandExecutor = (TableBandExecutor) executor;
			bandExecutor.setTableExecutor( this );
		}
		return executor;
	}

}