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

package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.dom.CellComputedStyle;
import org.eclipse.birt.report.engine.css.dom.ComputedStyle;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 * cell content object Implement IContentContainer interface the content of cell
 * can be any report item
 * 
 */
public class CellContent extends AbstractContent implements ICellContent
{

	/**
	 * row span
	 */
	protected int rowSpan = -1;

	/**
	 * col span, if equals to 1, then get it from the design.
	 */
	protected int colSpan = -1;

	/**
	 * column id, if equals to 0, get it from the design
	 */
	protected int column = -1;

	/**
	 * Flag indicading if this cell is the start of a group.
	 */
	protected Boolean displayGroupIcon;
	
	/**
	 * The number of the diagonal line.
	 */
	private int diagonalNumber = -1;
	/**
	 * The style of the diagonal line.
	 */
	private String diagonalStyle = null;
	/**
	 * The width of the diagonal line.
	 */
	private DimensionType diagonalWidth = null;
	/**
	 * The number of the antidiagonal line.
	 */
	private int antidiagonalNumber = -1;
	/**
	 * The style of the antidiagonal line.
	 */
	private String antidiagonalStyle = null;
	/**
	 * The width of the antidiagonal line.
	 */
	private DimensionType antidiagonalWidth = null;

	private String headers;

	private String scope;

	public int getContentType( )
	{
		return CELL_CONTENT;
	}

	/**
	 * constructor
	 * 
	 * @param item
	 *            cell design item
	 */
	CellContent( IReportContent report )
	{
		super( report );
	}
	
	CellContent(ICellContent cell)
	{
		super(cell);
		this.colSpan = cell.getColSpan( );
		this.rowSpan = cell.getRowSpan( );
		this.column = cell.getColumn( );
		this.displayGroupIcon = new Boolean(cell.getDisplayGroupIcon( ));
		this.columnInstance = cell.getColumnInstance( );
		this.diagonalNumber = cell.getDiagonalNumber( );
		this.diagonalStyle = cell.getDiagonalStyle( );
		this.diagonalWidth = cell.getDiagonalWidth( );
		this.antidiagonalNumber = cell.getAntidiagonalNumber( );
		this.antidiagonalStyle = cell.getAntidiagonalStyle( );
		this.antidiagonalWidth = cell.getAntidiagonalWidth( );
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan( )
	{
		if ( rowSpan == -1 )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getRowSpan( );
			}
		}
		return this.rowSpan;
	}

	/**
	 * 
	 * @return the column span
	 */
	public int getColSpan( )
	{
		if ( colSpan == -1 )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getColSpan( );
			}
		}
		return colSpan;
	}

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn( )
	{
		if ( column == -1 )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getColumn( );
			}
		}
		return column;
	}

	public int getRow( )
	{
		if ( parent != null && parent instanceof IRowContent )
		{
			return ( (IRowContent) parent ).getRowID( );
		}
		return 0;
	}

	public void setDrop( String drop )
	{
		if ( generateBy instanceof CellDesign )
			( (CellDesign) generateBy ).setDrop( drop );
	}

	public Object accept( IContentVisitor visitor, Object value )
			throws BirtException
	{
		return visitor.visitCell( this, value );
	}

	/**
	 * @param rowSpan
	 *            The rowSpan to set.
	 */
	public void setRowSpan( int rowSpan )
	{
		this.rowSpan = rowSpan;
	}

	public void setColSpan( int colSpan )
	{
		this.colSpan = colSpan;
	}

	public void setColumn( int column )
	{
		this.column = column;
	}

	public IStyle getComputedStyle( )
	{
		if ( computedStyle == null )
		{
			if ( inlineStyle == null || inlineStyle.isEmpty( ) )
			{
				String cacheKey = getStyleClass();
				ITableContent table = ( (IRowContent) parent ).getTable( );
				int column = getColumn( );
				if ( column >= 0 && column < table.getColumnCount( ) )
				{
					IColumn tblColumn = table.getColumn( column );
					if ( tblColumn != null )
					{
						String columnStyleClass = tblColumn.getStyleClass( );
						if ( columnStyleClass != null )
						{
							cacheKey = cacheKey + columnStyleClass;
						}
					}
				}

				ComputedStyle pcs = (ComputedStyle) ( (IContent) parent )
						.getComputedStyle( );
				ComputedStyle cs = pcs.getCachedStyle( cacheKey );
				if ( cs == null )
				{
					cs = new CellComputedStyle( this );
					pcs.addCachedStyle( cacheKey, cs );
				}
				computedStyle = cs;
			}
			else
			{
				computedStyle = new CellComputedStyle( this );
			}
		}
		return computedStyle;
	}

	static final protected short FIELD_ROW_SPAN = 100;
	static final protected short FIELD_COL_SPAN = 101;
	static final protected short FIELD_COLUMN = 102;
	static final protected short FIELD_START_OF_GROUP = 103;
	static final protected short FIELD_DISPLAY_GROUP_ICON = 104;
	static final protected short FIELD_DIAGONAL_NUMBER = 105;
	static final protected short FIELD_DIAGONAL_STYLE = 106;
	static final protected short FIELD_DIAGONAL_WIDTH = 107;
	static final protected short FIELD_ANTIDIAGONAL_NUMBER = 108;
	static final protected short FIELD_ANTIDIAGONAL_STYLE = 109;
	static final protected short FIELD_ANTIDIAGONAL_WIDTH = 110;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( rowSpan != -1 )
		{
			IOUtil.writeShort( out, FIELD_ROW_SPAN );
			IOUtil.writeInt( out, rowSpan );
		}
		if ( colSpan != -1 )
		{
			IOUtil.writeShort( out, FIELD_COL_SPAN );
			IOUtil.writeInt( out, colSpan );
		}
		if ( column != -1 )
		{
			IOUtil.writeShort( out, FIELD_COLUMN );
			IOUtil.writeInt( out, column );
		}
		if ( displayGroupIcon != null )
		{
			IOUtil.writeShort( out, FIELD_DISPLAY_GROUP_ICON );
			IOUtil.writeBool( out, displayGroupIcon.booleanValue( ) );
		}
		if ( diagonalNumber != -1 )
		{
			IOUtil.writeShort( out, FIELD_DIAGONAL_NUMBER );
			IOUtil.writeInt( out, diagonalNumber );
		}
		if ( diagonalStyle != null )
		{
			IOUtil.writeShort( out, FIELD_DIAGONAL_STYLE );
			IOUtil.writeString( out, diagonalStyle );
		}
		if ( diagonalWidth != null )
		{
			IOUtil.writeShort( out, FIELD_DIAGONAL_WIDTH );
			diagonalWidth.writeObject( out );
		}
		if ( antidiagonalNumber != -1 )
		{
			IOUtil.writeShort( out, FIELD_ANTIDIAGONAL_NUMBER );
			IOUtil.writeInt( out, antidiagonalNumber );
		}
		if ( antidiagonalStyle != null )
		{
			IOUtil.writeShort( out, FIELD_ANTIDIAGONAL_STYLE );
			IOUtil.writeString( out, antidiagonalStyle );
		}
		if ( antidiagonalWidth != null )
		{
			IOUtil.writeShort( out, FIELD_ANTIDIAGONAL_WIDTH );
			antidiagonalWidth.writeObject( out );
		}
		
	}

	protected void readField( int version, int filedId, DataInputStream in,
			ClassLoader loader ) throws IOException
	{
		switch ( filedId )
		{
			case FIELD_ROW_SPAN :
				rowSpan = IOUtil.readInt( in );
				break;
			case FIELD_COL_SPAN :
				colSpan = IOUtil.readInt( in );
				break;
			case FIELD_COLUMN :
				column = IOUtil.readInt( in );
				break;
			case FIELD_START_OF_GROUP :
				IOUtil.readBool( in );
				break;
			case FIELD_DISPLAY_GROUP_ICON :
				displayGroupIcon = Boolean.valueOf( IOUtil.readBool( in ) );
				break;
			case FIELD_DIAGONAL_NUMBER :
				diagonalNumber = IOUtil.readInt( in );
			case FIELD_DIAGONAL_STYLE :
				diagonalStyle = IOUtil.readString( in );
			case FIELD_DIAGONAL_WIDTH :
				diagonalWidth = new DimensionType( );
				diagonalWidth.readObject( in );
			case FIELD_ANTIDIAGONAL_NUMBER :
				antidiagonalNumber = IOUtil.readInt( in );
			case FIELD_ANTIDIAGONAL_STYLE :
				antidiagonalStyle = IOUtil.readString( in );
			case FIELD_ANTIDIAGONAL_WIDTH :
				antidiagonalWidth = new DimensionType( );
				antidiagonalWidth.readObject( in );
			default :
				super.readField( version, filedId, in, loader );
		}
	}

	public boolean needSave( )
	{
		if ( rowSpan != -1 || colSpan != -1 || column != -1 )
		{
			return true;
		}
		if ( displayGroupIcon != null )
		{
			return true;
		}
		if ( diagonalNumber != -1
				|| diagonalStyle != null || diagonalWidth != null
				|| antidiagonalNumber != -1 || antidiagonalStyle != null
				|| antidiagonalWidth != null )
		{
			return true;
		}
		return super.needSave( );
	}

	public boolean getDisplayGroupIcon( )
	{
		if ( displayGroupIcon == null )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getDisplayGroupIcon( );
			}
			return false;
		}
		return displayGroupIcon.booleanValue( );
	}

	public void setDisplayGroupIcon( boolean displayGroupIcon )
	{
		this.displayGroupIcon = Boolean.valueOf( displayGroupIcon );
	}

	private IColumn columnInstance;
	public IColumn getColumnInstance( )
	{
		if (columnInstance != null)
		{
			return columnInstance;
		}
		if ( parent instanceof IRowContent )
		{
			IRowContent row = ( IRowContent ) parent;
			ITableContent table = row.getTable( );
			if ( table != null )
			{
				int columnId = getColumn( );
				if ( columnId >= 0 && columnId < table.getColumnCount( ) )
				{
					columnInstance = table.getColumn( columnId );
				}
			}
		}
		return columnInstance;
	}
	
	protected IContent cloneContent()
	{
		return new CellContent(this);
	}

	public void setDiagonalNumber( int diagonalNumber )
	{
		this.diagonalNumber = diagonalNumber;
	}

	public int getDiagonalNumber( )
	{
		if ( diagonalNumber == -1 )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getDiagonalNumber( );
			}
		}
		return diagonalNumber;
	}

	public void setDiagonalStyle( String diagonalStyle )
	{
		this.diagonalStyle = diagonalStyle;
	}

	public String getDiagonalStyle( )
	{
		if ( diagonalStyle == null )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getDiagonalStyle( );
			}
		}
		return diagonalStyle;
	}

	public void setDiagonalWidth( DimensionType diagonalWidth )
	{
		this.diagonalWidth = diagonalWidth;
	}

	public DimensionType getDiagonalWidth( )
	{
		if ( diagonalWidth == null )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getDiagonalWidth( );
			}
		}
		return diagonalWidth;
	}

	public void setAntidiagonalNumber( int antidiagonalNumber )
	{
		this.antidiagonalNumber = antidiagonalNumber;
	}

	public int getAntidiagonalNumber( )
	{
		if ( antidiagonalNumber == -1 )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getAntidiagonalNumber( );
			}
		}
		return antidiagonalNumber;
	}

	public void setAntidiagonalStyle( String antidiagonalStyle )
	{
		this.antidiagonalStyle = antidiagonalStyle;
	}

	public String getAntidiagonalStyle( )
	{
		if ( antidiagonalStyle == null )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getAntidiagonalStyle( );
			}
		}
		return antidiagonalStyle;
	}

	public void setAntidiagonalWidth( DimensionType antidiagonalWidth )
	{
		this.antidiagonalWidth = antidiagonalWidth;
	}

	public DimensionType getAntidiagonalWidth( )
	{
		if ( antidiagonalWidth == null )
		{
			if ( generateBy instanceof CellDesign )
			{
				return ( (CellDesign) generateBy ).getAntidiagonalWidth( );
			}
		}
		return antidiagonalWidth;
	}

	public String getHeaders( )
	{
		return headers;
	}

	public String getScope( )
	{
		return scope;
	}

	public void setHeaders( String headers )
	{
		this.headers = headers;
	}

	public void setScope( String scope )
	{
		this.scope = scope;
	}

}