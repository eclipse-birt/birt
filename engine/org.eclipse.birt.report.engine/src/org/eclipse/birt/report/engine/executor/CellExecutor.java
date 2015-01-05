/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class CellExecutor extends QueryItemExecutor
{
	protected CellExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.CELLITEM );
	}
	
	int cellId;
	
	public IContent execute( )
	{
		CellDesign cellDesign = (CellDesign)getDesign();
		
		ICellContent cellContent = report.createCellContent( );
		setContent(cellContent);
		
		executeQuery( );
		initializeContent( cellDesign, cellContent );

		//cellContent.setColumn( cellDesign.getColumn( ) );
		//cellContent.setColSpan( cellDesign.getColSpan( ) );
		//cellContent.setRowSpan( cellDesign.getRowSpan( ) );

		processAction( cellDesign, cellContent );
		processBookmark( cellDesign, cellContent );
		processHeaders( cellDesign, cellContent );
		processStyle( cellDesign, cellContent );
		processVisibility( cellDesign, cellContent );
		processUserProperties( cellDesign, cellContent );
		processAltText( cellDesign, cellContent );

		//cellContent.setDisplayGroupIcon( cellDesign.getDisplayGroupIcon( ) );
		
		if ( context.isInFactory( ) )
		{
			handleOnCreate( cellContent );
		}

		startTOCEntry( cellContent );

		//prepare to execute the children.
		currentItem = 0;
		
		return content;
	}

	private void processAltText( CellDesign design, IContent content )
	{
		Expression altTextExpr = design.getAltText( );
		if ( altTextExpr != null )
		{
			Object altText = evaluate( altTextExpr );
			if ( altText != null )
			{
				content.setAltText( altText.toString( ) );
			}
		}
	}
	
	private void processHeaders( CellDesign cellDesign, ICellContent cellContent )
	{
		String headers = evaluateString( cellDesign.getHeaders( ) );
		if ( headers != null && !headers.equals( "" ) )
		{
			cellContent.setHeaders( headers );
		}
	}

	public void close( ) throws BirtException
	{
		finishTOCEntry( );
		closeQuery( );
		this.cellId = 0;
		super.close( );
	}
	
	private int currentItem = 0;

	public boolean hasNextChild()
	{
		CellDesign cellDesign = (CellDesign) getDesign();
		return currentItem < cellDesign.getContentCount( );
	}
	
	public IReportItemExecutor getNextChild( )
	{
		CellDesign cellDesign = (CellDesign) getDesign();
		if ( currentItem < cellDesign.getContentCount( ) )
		{
			ReportItemDesign itemDesign = cellDesign.getContent( currentItem++ );
			ReportItemExecutor executor = manager.createExecutor( this,  
					itemDesign);
			return executor;
		}
		return null;
	}
}
