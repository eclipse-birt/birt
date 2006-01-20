
package org.eclipse.birt.report.engine.css.dom;

import java.util.List;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class CellComputedStyle extends ComputedStyle
{

	private CSSStylableElement cell;

	public CellComputedStyle( ICellContent elt )
	{
		super( elt );
		IRowContent row = (IRowContent) elt.getParent( );
		if ( row != null )
		{
			IElement parentElt = row.getParent( );
			if ( parentElt instanceof ITableBandContent )
			{
				parentElt = parentElt.getParent( );
			}
			ITableContent table = (ITableContent) parentElt;
			if ( table != null )
			{
				int columnId = elt.getColumn( );
				if ( columnId >= 0 && columnId < table.getColumnCount( ) )
				{
					IColumn column = table.getColumn( columnId );
					if ( column.getStyleClass( ) != null )
					{
						cell = new StyledCell( elt );
					}
				}
			}
		}
		if ( cell == null )
		{
			cell = elt;
		}
	}

	protected Value resolveProperty( int index )
	{
		CSSStylableElement parent = (CSSStylableElement) cell.getParent( );
		IStyle pcs = null;
		if ( parent != null )
		{
			pcs = parent.getComputedStyle( );
		}
		// get the specified style
		IStyle s = cell.getStyle( );

		Value sv = s == null ? null : (Value) s.getProperty( index );

		Value cv = engine.resolveStyle( elt, index, sv, pcs );

		return cv;
	}

	private abstract static class StyledElement implements CSSStylableElement
	{

		private IStyle computedStyle;

		public void setParent( IElement parent )
		{
		}

		public List getChildren( )
		{
			return null;
		}

		public IStyle getComputedStyle( )
		{
			if ( computedStyle == null )
			{
				return new ComputedStyle( this );
			}
			return computedStyle;
		}
	}

	private static class StyledCell extends StyledElement
	{

		ICellContent cellContent;
		StyledRow row;

		StyledCell( ICellContent cellContent )
		{
			this.cellContent = cellContent;
			row = new StyledRow( (IRowContent) cellContent.getParent( ),
					cellContent.getColumn( ) );
		}

		public IStyle getStyle( )
		{
			return cellContent.getStyle( );
		}

		public IElement getParent( )
		{
			return row;
		}
		
		public CSSEngine getCSSEngine()
		{
			return cellContent.getCSSEngine();
		}
	}

	private static class StyledRow extends StyledElement
	{

		private StyledColumn column;
		private IRowContent rowContent;

		public StyledRow( IRowContent rowContent, int columnId )
		{
			IElement parentElt = rowContent.getParent( );
			if ( parentElt instanceof ITableBandContent )
			{
				parentElt = parentElt.getParent( );
			}
			column = new StyledColumn( (ITableContent) parentElt, columnId );
			this.rowContent = rowContent;
		}

		public IStyle getStyle( )
		{
			return rowContent.getStyle( );
		}

		public IElement getParent( )
		{
			return column;
		}
		public CSSEngine getCSSEngine()
		{
			return rowContent.getCSSEngine();
		}
	}

	private static class StyledColumn extends StyledElement
	{

		private ITableContent tableContent;
		private IStyle columnStyle;

		public StyledColumn( ITableContent table, int columnId )
		{
			this.tableContent = table;
			if ( columnId >= 0 && columnId <= table.getColumnCount( ) )
			{
				IColumn column = table.getColumn( columnId );
				String styleName = column.getStyleClass( );
				if ( styleName != null )
				{
					IReportContent reportContent = table.getReportContent( );
					if ( reportContent != null )
					{
						columnStyle = reportContent.findStyle( styleName );
					}
				}
			}
		}

		public IStyle getStyle( )
		{
			return columnStyle;
		}

		public IElement getParent( )
		{
			return tableContent;
		}
		
		public CSSEngine getCSSEngine()
		{
			return tableContent.getCSSEngine();
		}
	}
}
