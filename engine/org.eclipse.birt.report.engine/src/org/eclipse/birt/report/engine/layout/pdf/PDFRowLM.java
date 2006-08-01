/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IInlineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.IPDFTableLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;

public class PDFRowLM extends PDFInlineStackingLM
		implements
			IInlineStackingLayoutManager
{

	protected IPDFTableLayoutManager tbl;
	protected int rowID;

	protected boolean clear = false;

	public PDFRowLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );
		tbl = getTableLayoutManager( );
		rowID = ( (IRowContent) content ).getRowID( );
		calculateSpecifiedHeight( );
		tbl.startRow( (IRowContent) content );

	}
	
	public boolean layout()
	{
		boolean childBreak = super.layout( );
		if ( childBreak )
		{
			if ( tbl != null )
			{
				if ( !isFinished( ) && needPageBreakBefore( ) )
				{
					tbl.setTableCloseStateAsForced( );
				}
				else if ( isFinished( ) && needPageBreakAfter( ) )
				{
					tbl.setTableCloseStateAsForced( );
				}
			}
		}
		return childBreak;
	}
	
	protected boolean checkAvailableSpace( )
	{
		boolean availableSpace = super.checkAvailableSpace( );
		if(availableSpace && tbl != null)
		{
			tbl.setTableCloseStateAsForced( );
		}
		return availableSpace;
	}

	protected void calculateSpecifiedHeight( )
	{
		super.calculateSpecifiedHeight( );
		if ( specifiedHeight == 0 )
		{
			IStyle style = content.getComputedStyle( );
			int fontSize = this.getDimensionValue( style
					.getProperty( IStyle.STYLE_FONT_SIZE ) );
			specifiedHeight = fontSize;
		}
	}

	protected void createRoot( )
	{
		root = AreaFactory.createRowArea( (IRowContent) content );
	}

	protected void newContext( )
	{
		createRoot( );
		setMaxAvaWidth( parent.getMaxAvaWidth( ) );
		setMaxAvaHeight( parent.getMaxAvaHeight( ) - parent.getCurrentBP( ) );
		root.setWidth( getMaxAvaWidth( ) );
	}

	protected boolean traverseChildren( )
	{
		boolean childBreak = false;
		// first loop
		if ( children.size( ) == 0 )
		{
			while ( executor.hasNextChild( ) )
			{
				IReportItemExecutor childExecutor = executor.getNextChild( );
				IContent childContent = childExecutor.execute( );
				PDFAbstractLM childLM = getFactory( ).createLayoutManager(
						this, childContent, emitter, childExecutor );
				addChild( childLM );
				if ( childLM.layout( ) && !childBreak )
				{
					childBreak = true;
				}
			}
		}
		else
		{
			if ( !isRowFinished( ) )
			{
				for ( int i = 0; i < children.size( ); i++ )
				{
					ILayoutManager childLM = (ILayoutManager) children.get( i );
					if ( childLM.layout( ) && !childBreak )
					{
						childBreak = true;
					}
				}
			}
		}
		if ( childBreak )
		{
			return true;
		}
		status = STATUS_END;
		if ( isPageBreakAfter( ) )
		{
			return true;
		}
		return false;
	}

	protected void closeLayout( )
	{
		tbl.updateRow( (RowArea) root, specifiedHeight );
	}

	public boolean addArea( IArea area )
	{
		CellArea cArea = (CellArea) area;
		root.addChild( area );
		cArea.setPosition( tbl.getXPos( cArea.getColumnID( ) ), 0 );
		return true;
	}

	/*
	 * protected void setupMinHeight( ) { if(content!=null) { int
	 * specifiedHeight = PropertyUtil.getDimensionValue(content.getHeight( ));
	 * if(specifiedHeight>0 && specifiedHeight<getNMaxHeight( )) { minHeight =
	 * specifiedHeight; } } }
	 */

	protected boolean isRowFinished( )
	{
		for ( int i = 0; i < children.size( ); i++ )
		{
			PDFAbstractLM lm = (PDFAbstractLM) children.get( i );
			if ( lm != null )
			{
				if ( !lm.isFinished( ) )
				{
					return false;
				}
			}
		}
		return true;
	}

}
