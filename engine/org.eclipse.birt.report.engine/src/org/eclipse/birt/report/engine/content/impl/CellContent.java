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
	 * Flag identify if need repeat content in cell after page-break
	 */
	protected boolean repeatContent = false;
	
	/**
	 * The cell design, which generate this cell content.
	 */
	CellDesign cellDesign = null;

	private String headers;
	
	private String scope;

	private String drop;
	
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
		if ( generateBy instanceof CellDesign )
		{
			cellDesign = (CellDesign) generateBy;
		}
	}
	
	/**
	 * @param generateBy
	 *            The generateBy to set.
	 */
	public void setGenerateBy( Object generateBy )
	{
		super.setGenerateBy( generateBy );

		if ( generateBy instanceof CellDesign )
		{
			cellDesign = (CellDesign) generateBy;
		}
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan( )
	{
		if ( rowSpan == -1 && cellDesign != null )
		{
			rowSpan = cellDesign.getRowSpan( );
		}
		return rowSpan;
	}

	/**
	 * 
	 * @return the column span
	 */
	public int getColSpan( )
	{
		if ( colSpan == -1 && cellDesign != null )
		{
			colSpan = cellDesign.getColSpan( );
		}
		return colSpan;
	}

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn( )
	{
		if ( column == -1  && cellDesign != null  )
		{
			column = cellDesign.getColumn( );
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
		this.drop = drop;
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
	static final protected short FIELD_DROP = 111;
	static final protected short FIELD_HEADERS = 112;
	static final protected short FIELD_SCOPE = 113;
	static final protected short FIELD_REPEAT_CONTENT = 114;

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
		if ( drop != null )
		{
			IOUtil.writeShort( out, FIELD_DROP );
			IOUtil.writeString( out, drop );
		}
		if ( headers != null )
		{
			IOUtil.writeShort( out, FIELD_HEADERS );
			IOUtil.writeString( out, headers );
		}
		if ( scope != null )
		{
			IOUtil.writeShort( out, FIELD_SCOPE );
			IOUtil.writeString( out, scope );
		}
		if ( repeatContent )
		{
			IOUtil.writeShort( out, FIELD_REPEAT_CONTENT );
			IOUtil.writeBool( out, true );
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
			case FIELD_DROP :
				drop = IOUtil.readString( in );
				break;
			case FIELD_HEADERS :
				headers = IOUtil.readString( in );
				break;
			case FIELD_SCOPE :
				scope = IOUtil.readString( in );
				break;
			case FIELD_REPEAT_CONTENT :
				repeatContent = IOUtil.readBool( in );
				break;
			default :
				super.readField( version, filedId, in, loader );
				break;
		}
	}

	public boolean needSave( )
	{
		if ( rowSpan != -1 || colSpan != -1 || column != -1 )
		{
			return true;
		}
		if ( displayGroupIcon != null || headers != null || scope != null )
		{
			return true;
		}
		return super.needSave( );
	}

	public boolean getDisplayGroupIcon( )
	{
		if ( displayGroupIcon == null )
		{
			if ( cellDesign != null )
			{
				return cellDesign.getDisplayGroupIcon( );
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
	
	public boolean hasDiagonalLine( )
	{
		if ( cellDesign != null )
		{
			return cellDesign.hasDiagonalLine( );
		}
		return false;
	}

	public int getDiagonalNumber( )
	{
		if ( cellDesign != null )
		{
			return cellDesign.getDiagonalNumber( );
		}
		return 0;
	}

	public String getDiagonalStyle( )
	{
		if ( cellDesign != null )
		{
			return cellDesign.getDiagonalStyle( );
		}
		return null;
	}

	public DimensionType getDiagonalWidth( )
	{
		if ( cellDesign != null )
		{
			return cellDesign.getDiagonalWidth( );
		}
		return null;
	}

	public int getAntidiagonalNumber( )
	{
		if ( cellDesign != null )
		{
			return cellDesign.getAntidiagonalNumber( );
		}
		return 0;
	}

	public String getAntidiagonalStyle( )
	{
		if ( cellDesign != null )
		{
			return cellDesign.getAntidiagonalStyle( );
		}
		return null;
	}

	public DimensionType getAntidiagonalWidth( )
	{
		if ( cellDesign != null )
		{
			return cellDesign.getAntidiagonalWidth( );
		}
		return null;
	}

	public String getHeaders( )
	{
		if ( headers != null )
		{
			return headers;
		}
		else if ( cellDesign != null && cellDesign.getHeaders( ) != null )
		{
			return getConstantValue( cellDesign.getHeaders( ) );
		}
		return null;
	}

	public String getScope( )
	{
		if ( scope != null )
		{
			return scope;
		}
		else if ( cellDesign != null )
		{
			return cellDesign.getScope( );
		}
		return null;
	}

	public void setHeaders( String headers )
	{
		this.headers = headers;
	}

	public void setScope( String scope )
	{
		this.scope = scope;
	}

	public boolean repeatContent( )
	{
		return repeatContent;
	}

	public void setRepeatContent( boolean repeatContent )
	{
		this.repeatContent = repeatContent;
	}

}