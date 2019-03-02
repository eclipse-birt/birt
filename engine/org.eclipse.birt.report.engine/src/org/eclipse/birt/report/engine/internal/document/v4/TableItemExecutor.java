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
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

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
 */
public class TableItemExecutor extends ListingElementExecutor
{

	private int rowId = 0;

	/**
	 * @param context
	 *            execution context
	 * @param visitor
	 *            visitor object for driving the execution
	 */
	protected TableItemExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.TABLEITEM );
	}

	int getRowId( )
	{
		return rowId;
	}

	void setRowId( int rowId )
	{
		this.rowId = rowId;
	}

	protected IContent doCreateContent( )
	{
		return report.createTableContent( );
	}

	protected void doExecute( ) throws Exception
	{

		TableItemDesign tableDesign = (TableItemDesign) design;
		ITableContent tableContent = (ITableContent) content;

		executeQuery( );

		if ( tableContent.getColumnCount( ) == 0 )
		{
			for ( int i = 0; i < tableDesign.getColumnCount( ); i++ )
			{
				ColumnDesign columnDesign = tableDesign.getColumn( i );
				Column column = new Column( report );
				column.setGenerateBy( columnDesign );

				InstanceID iid = new InstanceID( null, columnDesign.getID( ),
						null );
				column.setInstanceID( iid );

				tableContent.addColumn( column );
			}
		}
		else
		{
			int columnCount = tableContent.getColumnCount( );
			for ( int i = 0; i < columnCount; i++ )
			{
				Column column = (Column) tableContent.getColumn( i );
				InstanceID iid = column.getInstanceID( );
				if ( iid != null )
				{
					long componentId = iid.getComponentID( );
					ReportElementDesign element = report.getDesign( )
							.getReportItemByID( componentId );
					column.setGenerateBy( element );
				}
			}
		}
		
		//create an empty result set to handle the showIfBlank
		boolean showIfBlank = "true".equalsIgnoreCase( content.getStyle( )
				.getShowIfBlank( ) );
		if ( showIfBlank && rsetEmpty )
		{
			createQueryForShowIfBlank( );
		}

	}

	public void close( )
	{
		closeQuery( );
		super.close( );
	}

	protected ReportItemExecutor doCreateExecutor( long offset )
			throws Exception
	{
		ReportItemExecutor executor = super.doCreateExecutor( offset );
		if ( executor instanceof TableBandExecutor )
		{
			TableBandExecutor bandExecutor = (TableBandExecutor) executor;
			bandExecutor.setTableExecutor( this );
		}
		return executor;
	}
}
