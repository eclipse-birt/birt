
package org.eclipse.birt.report.engine.css.dom;

import java.util.Collection;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class CellComputedStyle extends ComputedStyle
{

	private CSSStylableElement cell;
	private IStyle columnStyle;
	private IStyle rowStyle;

	public CellComputedStyle( ICellContent elt )
	{
		super( elt );
		IRowContent row = (IRowContent) elt.getParent( );
		if ( row != null )
		{
			rowStyle = row.getStyle();
			ITableContent table = row.getTable( );
			if ( table != null )
			{
				int columnId = elt.getColumn( );
				if ( columnId >= 0 && columnId < table.getColumnCount( ) )
				{
					IColumn column = table.getColumn( columnId );
					columnStyle = column.getStyle( );
					cell = new StyledCell( elt );
				}
			}
		}
		if ( cell == null )
		{
			cell = elt;
			columnStyle = null;
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
		
		//none inheritable properties
		//background color: if the property defined in the row is empty, use column's property.
		//vertical-align: if the row property is not null, use it, otherwise, use the column.
		//other properties, use the property of column directly.
		if ( sv == null && columnStyle != null )
		{
			if ( engine.isInheritedProperty( index ) == false )
			{
				if ( isBackgroundProperties( index ) )
				{
					Value rowValue = null;
					if ( rowStyle != null )
					{
						rowValue = (Value) rowStyle.getProperty( index );
					}
					if ( rowValue == null )
					{
						sv = (Value) columnStyle.getProperty( index );
					}
				}
				else if ( index == STYLE_VERTICAL_ALIGN )
				{
					if ( rowStyle != null )
					{
						sv = (Value) rowStyle.getProperty( index );
					}
					if ( sv == null )
					{
						sv = (Value) columnStyle.getProperty( index );
					}
				}
				else
				{
					sv = (Value) columnStyle.getProperty( index );
				}
			}
		}

		Value cv = engine.resolveStyle( elt, index, sv, pcs );
		return cv;
	}
	
	private boolean isBackgroundProperties(int index)
	{
		if (StyleConstants.STYLE_BACKGROUND_COLOR==index 
				||StyleConstants.STYLE_BACKGROUND_ATTACHMENT==index
				||StyleConstants.STYLE_BACKGROUND_IMAGE==index
				||StyleConstants.STYLE_BACKGROUND_REPEAT==index)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private abstract static class StyledElement implements CSSStylableElement
	{

		private IStyle computedStyle;

		public void setParent( IElement parent )
		{
		}

		public Collection getChildren( )
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
			ITableContent table  = rowContent.getTable( );
			column = new StyledColumn( table, columnId );
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
		private IColumn column;
		
		public StyledColumn( ITableContent table, int columnId )
		{
			this.tableContent = table;
			if ( columnId >= 0 && columnId <= table.getColumnCount( ) )
			{
				column = table.getColumn( columnId );
			}
		}

		public IStyle getStyle( )
		{
			return column.getStyle( );
		}

		public IElement getParent( )
		{
			return tableContent;
		}
		
		public CSSEngine getCSSEngine()
		{
			return column.getCssEngine( );
		}
	}
}