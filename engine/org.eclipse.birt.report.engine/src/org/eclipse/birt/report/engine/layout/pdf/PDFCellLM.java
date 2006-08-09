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

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.IPDFTableLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;

public class PDFCellLM extends PDFBlockStackingLM
		implements
			IBlockStackingLayoutManager
{

	/**
	 * table layout manager of current cell
	 */
	protected IPDFTableLayoutManager tableLM;

	protected int columnWidth = 0;

	/**
	 * cell content
	 */
	private ICellContent cellContent;

	public PDFCellLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
		assert ( parent != null );
		tableLM = getTableLayoutManager( );
		cellContent = (ICellContent) content;
		tableLM.startCell( cellContent );

		// set max width constraint
		int startColumn = cellContent.getColumn( );
		int endColumn = startColumn + cellContent.getColSpan( );
		columnWidth = tableLM.getCellWidth( startColumn, endColumn );

	}

	protected void createRoot( )
	{
		if(!isFirst)
		{
			int startColumn = cellContent.getColumn( );
			int endColumn = startColumn + cellContent.getColSpan( );
			columnWidth = tableLM.getCellWidth( startColumn, endColumn );
		}
		root = AreaFactory.createCellArea( cellContent );
		tableLM.resolveBorderConflict( (CellArea) root );
		root.setWidth( columnWidth );
		if ( !isFirst )
		{
			IStyle areaStyle = root.getStyle( );
			areaStyle.setProperty( IStyle.STYLE_BORDER_TOP_WIDTH,
					IStyle.NUMBER_0 );
			areaStyle.setProperty( IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0 );
			areaStyle.setProperty( IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0 );
		}
	}

	protected void newContext( )
	{
		createRoot( );
		IStyle areaStyle = root.getStyle( );
		setOffsetX( getDimensionValue( areaStyle
				.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) )
				+ getDimensionValue( areaStyle
						.getProperty( StyleConstants.STYLE_PADDING_LEFT ) ) );
		setOffsetY( isFirst
				? ( getDimensionValue( areaStyle
						.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) ) + getDimensionValue( areaStyle
						.getProperty( StyleConstants.STYLE_PADDING_TOP ) ) )
				: 0 );

		int borderWidth = getDimensionValue( areaStyle
				.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) )
				+ getDimensionValue( areaStyle
						.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );
		int paddingWidth = getDimensionValue( areaStyle
				.getProperty( StyleConstants.STYLE_PADDING_LEFT ) )
				+ getDimensionValue( areaStyle
						.getProperty( StyleConstants.STYLE_PADDING_RIGHT ) );
		if ( borderWidth + paddingWidth < columnWidth )
		{
			setMaxAvaWidth( columnWidth - borderWidth - paddingWidth );

		}
		else if ( borderWidth < columnWidth )// drop padding
		{
			setMaxAvaWidth( columnWidth - borderWidth );
		}
		else
		{
			// FIXME how to resolve this case
			setMaxAvaWidth( 0 );
		}
		root.setAllocatedHeight( parent.getMaxAvaHeight( )
				- parent.getCurrentBP( ) );
		setMaxAvaHeight( root.getContentHeight( ) );

		if ( isFirst )
		{
			isFirst = false;
		}
		setCurrentBP( 0 );
		setCurrentIP( 0 );

	}

	protected void closeLayout( )
	{
		root.setHeight( getCurrentBP( )
				+ getOffsetY( )
				+ getDimensionValue( root.getStyle( ).getProperty(
						StyleConstants.STYLE_PADDING_BOTTOM ) ) );

	}

	protected boolean isHidden( )
	{
		if ( columnWidth == 0 || !tableLM.isCellVisible( cellContent ) )
		{
			return true;
		}
		return super.isHidden( );
	}

	protected boolean submitRoot( boolean childBreak )
	{
		if ( parent != null )
		{
			parent.addArea( root );
		}
		return true;
	}

	protected boolean isRootEmpty( )
	{
		return false;
	}

}
