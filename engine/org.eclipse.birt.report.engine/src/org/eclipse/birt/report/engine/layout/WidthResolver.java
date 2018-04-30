/*******************************************************************************
 * Copyright (c) 2016 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;

/**
 * Utility class to resolve percentage size to fixed unit size.
 */

public class WidthResolver
{

	private static final DimensionType PERCENTAGE_100 = new DimensionType( 100,
			DimensionType.UNITS_PERCENTAGE );

	private final int dpi;

	public WidthResolver( int dpi )
	{
		this.dpi = dpi;
	}

	private boolean isPercentage( DimensionType d )
	{
		return DimensionType.UNITS_PERCENTAGE.equals( d.getUnits( ) );
	}

	/**
	 * Computes the fixed unit width according to current content. If it's
	 * percentage size, look for the value from container. If the root contain
	 * still has no size, use master page width. If master page does not define
	 * size, null may return.
	 * 
	 * @param content
	 *            report item content
	 * @return fixed unit width like in, pt
	 */
	public DimensionValue resolveWidth( IContent content )
	{
		if ( content instanceof ICellContent )
		{
			return resolveCellWidth( (ICellContent) content );
		}
		DimensionType width = content.getWidth( );
		if ( width == null )
		{
			width = PERCENTAGE_100;
		}
		if ( !isPercentage( width ) )
		{
			return convert( width );
		}

		IContent parent = (IContent) content.getParent( );
		// If parent is null, use the page width in master page
		DimensionValue parentWidth;
		if ( parent == null )
		{
			parentWidth = getMasterPageWidth( content );
			if ( parentWidth == null )
			{
				return null;
			}
		}
		else
		{
			parentWidth = resolveWidth( parent );
		}
		return new DimensionValue(
				parentWidth.getMeasure( ) * width.getMeasure( ) / 100,
				parentWidth.getUnits( ) );
	}

	private DimensionValue getMasterPageWidth( IContent content )
	{
		if ( content.getReportContent( ) == null
				|| content.getReportContent( ).getDesign( ) == null
				|| content.getReportContent( )
						.getDesign( )
						.getPageSetup( ) == null
				|| content.getReportContent( )
						.getDesign( )
						.getPageSetup( )
						.getMasterPageCount( ) == 0 )
		{
			return null;
		}
		MasterPageDesign page = content.getReportContent( )
				.getDesign( )
				.getPageSetup( )
				.getMasterPage( 0 );
		if ( page == null )
		{
			return null;
		}
		if ( page.getLeftMargin( ) == null || page.getRightMargin( ) == null )
		{
			return convert( page.getPageWidth( ) );
		}
		// Final width is page width minus margin
		DimensionValue margin = DimensionUtil.mergeDimension(
				convert( page.getLeftMargin( ) ),
				convert( page.getRightMargin( ) ),
				dpi );
		return DimensionUtil.mergeDimension( convert( page.getPageWidth( ) ),
				new DimensionValue( -margin.getMeasure( ), margin.getUnits( ) ),
				dpi );
	}

	private DimensionValue resolveCellWidth( ICellContent cell )
	{
		ITableContent table = getTable( cell );
		DimensionValue tableWidth = null;
		int colId = cell.getColumn( );
		int colSpan = cell.getColSpan( );
		DimensionValue cellWidth = null;
		for ( int i = 0; i < colSpan; i++ )
		{
			IColumn column = table.getColumn( colId + i );
			DimensionType colWidth = column.getWidth( );
			colWidth = colWidth == null ? PERCENTAGE_100 : colWidth;
			if ( isPercentage( colWidth ) )
			{
				if ( tableWidth == null )
				{
					tableWidth = resolveWidth( table );
				}
				colWidth = new DimensionType(
						tableWidth.getMeasure( ) * colWidth.getMeasure( ) / 100,
						tableWidth.getUnits( ) );
			}
			if ( cellWidth == null )
			{
				cellWidth = convert( colWidth );
			}
			else
			{
				cellWidth = DimensionUtil.mergeDimension( cellWidth,
						convert( colWidth ),
						dpi );
			}
		}
		return cellWidth;
	}

	DimensionValue convert( DimensionType dim )
	{
		return new DimensionValue( dim.getMeasure( ), dim.getUnits( ) );
	}

	private ITableContent getTable( ICellContent cell )
	{
		IElement parent = cell.getParent( );
		while ( parent != null )
		{
			if ( parent instanceof ITableContent )
			{
				return (ITableContent) parent;
			}
			parent = parent.getParent( );
		}
		return null;
	}
}
