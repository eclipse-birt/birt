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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.IModelAdaptHelper;
import org.eclipse.birt.report.designer.core.model.ITableAdaptHelper;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.command.ContentException;
import org.eclipse.birt.report.model.command.NameException;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.metadata.DimensionValue;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.jface.util.Assert;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement CellHandleAdapter responds to model CellHandle
 *  
 */

public class TableHandleAdapter extends ReportItemtHandleAdapter
{

	//	private static Log log = LogFactory.getLog( TableHandleAdapter.class );
	private static final String TRANS_LABEL_INSERT_ROW = Messages.getString( "TableHandleAdapter.transLabel.insertRow" ); //$NON-NLS-1$
	private static final String NAME_NULL = ""; //$NON-NLS-1$
	private static final String NAME_DETAIL = Messages.getString( "TableHandleAdapter.name.detail" ); //$NON-NLS-1$
	private static final String NAME_FOOTER = Messages.getString( "TableHandleAdapter.name.footer" ); //$NON-NLS-1$
	private static final String NAME_HEADRER = Messages.getString( "TableHandleAdapter.name.header" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_NOT_INCLUDE = Messages.getString( "TableHandleAdapter.transLabel.notInclude" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_INCLUDE = Messages.getString( "TableHandleAdapter.transLabel.include" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_INSERT_GROUP = Messages.getString( "TableHandleAdapter.transLabel.insertGroup" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_SPLIT_CELLS = Messages.getString( "TableHandleAdapter.transLabel.splitCells" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_DELETE_ROW = Messages.getString( "TableHandleAdapter.transLabel.deleteRow" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_DELETE_ROWS = Messages.getString( "TableHandleAdapter.transLabel.deleteRows" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_DELETE_COLUMN = Messages.getString( "TableHandleAdapter.transLabel.deleteColumn" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_DELETE_COLUMNS = Messages.getString( "TableHandleAdapter.transLabel.deleteColumns" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_INSERT_COLUMN = Messages.getString( "TableHandleAdapter.transLabel.insertColumn" ); //$NON-NLS-1$
	public static final int HEADER = TableItem.HEADER_SLOT;
	public static final int DETAIL = TableItem.DETAIL_SLOT;
	public static final int FOOTER = TableItem.FOOTER_SLOT;

	public static final String TABLE_HEADER = "H"; //$NON-NLS-1$
	public static final String TABLE_FOOTER = "F"; //$NON-NLS-1$
	public static final String TABLE_DETAIL = "D"; //$NON-NLS-1$
	public static final String TABLE_GROUP_HEADER = "gh"; //$NON-NLS-1$
	public static final String TABLE_GROUP_FOOTER = "gf"; //$NON-NLS-1$

	/* the name model should support */
	private HashMap rowInfo = new HashMap( );
	protected List rows = new ArrayList( );

	/**
	 * Constructor
	 * 
	 * @param table
	 *            The handle of report item.
	 * 
	 * @param mark
	 */
	public TableHandleAdapter( ReportItemHandle table, IModelAdaptHelper mark )
	{
		super( table, mark );
	}

	/**
	 * Gets the Children list.
	 * 
	 * @return Children iterator
	 */
	public List getChildren( )
	{
		List children = new ArrayList( );

		SlotHandle header = getTableHandle( ).getHeader( );

		for ( Iterator it = header.iterator( ); it.hasNext( ); )
		{
			insertIteratorToList( ( (RowHandle) it.next( ) ).getCells( )
					.iterator( ), children );
		}

		SlotHandle group = getTableHandle( ).getGroups( );

		for ( Iterator it = group.iterator( ); it.hasNext( ); )
		{
			TableGroupHandle tableGroups = (TableGroupHandle) it.next( );
			SlotHandle groupHeaders = tableGroups.getSlot( GroupElement.HEADER_SLOT );
			for ( Iterator heards = groupHeaders.iterator( ); heards.hasNext( ); )
			{
				insertIteratorToList( ( (RowHandle) heards.next( ) ).getCells( )
						.iterator( ), children );
			}
		}

		SlotHandle detail = getTableHandle( ).getDetail( );

		for ( Iterator detailRows = detail.iterator( ); detailRows.hasNext( ); )
		{
			insertIteratorToList( ( (RowHandle) detailRows.next( ) ).getCells( )
					.iterator( ), children );
		}

		group = getTableHandle( ).getGroups( );

		for ( ListIterator it = convertIteratorToListIterator( group.iterator( ) ); it.hasPrevious( ); )
		{
			TableGroupHandle tableGroups = (TableGroupHandle) it.previous( );
			SlotHandle groupFooters = tableGroups.getSlot( GroupElement.FOOTER_SLOT );
			for ( Iterator heards = groupFooters.iterator( ); heards.hasNext( ); )
			{
				insertIteratorToList( ( (RowHandle) heards.next( ) ).getCells( )
						.iterator( ), children );
			}
		}

		SlotHandle footer = getTableHandle( ).getFooter( );
		for ( Iterator footerIT = footer.iterator( ); footerIT.hasNext( ); )
		{
			insertIteratorToList( ( (RowHandle) footerIT.next( ) ).getCells( )
					.iterator( ), children );
		}

		return children;
	}

	private ListIterator convertIteratorToListIterator( Iterator iterator )
	{
		ArrayList list = new ArrayList( );
		for ( Iterator it = iterator; it.hasNext( ); )
		{
			list.add( it.next( ) );
		}
		return list.listIterator( list.size( ) );
	}

	private void insertIteratorToList( Iterator iterator,
			TableHandleAdapter.RowUIInfomation info )
	{
		List addList = new ArrayList( );
		for ( Iterator it = iterator; it.hasNext( ); )
		{
			addList.add( it.next( ) );
		}
		info.addChildren( addList );
	}

	/**
	 * Inserts the iterator to the given list
	 * 
	 * @param iterator
	 *            The iterator
	 * @param list
	 *            The list
	 */
	protected void insertIteratorToList( Iterator iterator, List list,
			String displayNmae, String type )
	{
		for ( Iterator it = iterator; it.hasNext( ); )
		{
			RowHandle handle = (RowHandle) it.next( );
			list.add( handle );
			TableHandleAdapter.RowUIInfomation info = new TableHandleAdapter.RowUIInfomation( getColumnCount( ) );
			info.setType( type );
			info.setRowDisplayName( displayNmae );
			insertIteratorToList( handle.getCells( ).iterator( ), info );
			rowInfo.put( handle, info );
		}

	}

	public List getRows( )
	{
		if ( checkDirty( ) || rowInfo.isEmpty( ) )
		{
			reload( );
		}
		return rows;
	}

	protected void clearBuffer( )
	{
		rowInfo.clear( );
		rows.clear( );
	}

	/**
	 * Gets all rows list.
	 * 
	 * @return The rows list.
	 */
	public List initRowsInfo( )
	{
		clearBuffer( );

		SlotHandle header = getTableHandle( ).getHeader( );

		insertIteratorToList( header.iterator( ),
				rows,
				TABLE_HEADER,
				TABLE_HEADER );

		SlotHandle group = getTableHandle( ).getGroups( );

		int number = 0;
		for ( Iterator it = group.iterator( ); it.hasNext( ); )
		{
			number++;
			TableGroupHandle tableGroups = (TableGroupHandle) it.next( );
			SlotHandle groupHeaders = tableGroups.getSlot( GroupElement.HEADER_SLOT );
			insertIteratorToList( groupHeaders.iterator( ),
					rows,
					( TABLE_GROUP_HEADER + number ),
					TABLE_GROUP_HEADER );
		}

		SlotHandle detail = getTableHandle( ).getDetail( );
		insertIteratorToList( detail.iterator( ),
				rows,
				TABLE_DETAIL,
				TABLE_DETAIL );

		group = getTableHandle( ).getGroups( );

		number = 0;
		for ( Iterator it = group.iterator( ); it.hasNext( ); )
		{

			TableGroupHandle tableGroups = (TableGroupHandle) it.next( );
			SlotHandle groupFooters = tableGroups.getSlot( GroupElement.FOOTER_SLOT );
			number++;
		}
		for ( ListIterator it = convertIteratorToListIterator( group.iterator( ) ); it.hasPrevious( ); )
		{

			TableGroupHandle tableGroups = (TableGroupHandle) it.previous( );
			SlotHandle groupFooters = tableGroups.getSlot( GroupElement.FOOTER_SLOT );
			insertIteratorToList( groupFooters.iterator( ),
					rows,
					TABLE_GROUP_FOOTER + number,
					TABLE_GROUP_FOOTER );
			number--;
		}

		SlotHandle footer = getTableHandle( ).getFooter( );
		insertIteratorToList( footer.iterator( ),
				rows,
				TABLE_FOOTER,
				TABLE_FOOTER );

		caleRowInfo( rows );
		return rows;
	}

	/**
	 * @param children
	 */
	protected void caleRowInfo( List children )
	{
		int size = children.size( );

		for ( int i = 0; i < size; i++ )
		{
			RowHandleAdapter adapt = HandleAdapterFactory.getInstance( )
					.getRowHandleAdapter( children.get( i ) );
			List cellChildren = adapt.getChildren( );
			int len = cellChildren.size( );

			TableHandleAdapter.RowUIInfomation info = (TableHandleAdapter.RowUIInfomation) rowInfo.get( children.get( i ) );
			for ( int j = 0; j < len; j++ )
			{
				CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance( )
						.getCellHandleAdapter( cellChildren.get( j ) );
				int cellIndex = info.getAllChildren( )
						.indexOf( cellChildren.get( j ) );

				if ( cellAdapt.getColumnSpan( ) != 1 )
				{
					if ( cellIndex + 2 <= info.getAllChildren( ).size( )
							&& cellIndex >= 0 )
					{
						fillRowInfoChildrenList( children.get( i ),
								cellIndex + 2,
								cellAdapt.getColumnSpan( ) - 1,
								cellChildren.get( j ) );
					}

				}
				if ( cellAdapt.getRowSpan( ) != 1 )
				{
					for ( int k = i + 1; k < i + cellAdapt.getRowSpan( ); k++ )
					{
						if ( cellIndex < 0
								|| cellIndex + cellAdapt.getColumnSpan( ) > info.getAllChildren( )
										.size( ) )
						{
							continue;
						}
						fillRowInfoChildrenList( children.get( k ),
								cellIndex + 1,
								cellAdapt.getColumnSpan( ),
								cellChildren.get( j ) );
					}
				}
			}
		}
	}

	private void fillRowInfoChildrenList( Object row, int columnNumber,
			int colSpan, Object cell )
	{
		TableHandleAdapter.RowUIInfomation info = (TableHandleAdapter.RowUIInfomation) rowInfo.get( row );
		if ( info == null )
		{
			return;
		}

		for ( int i = 0; i < colSpan; i++ )
		{
			info.addChildren( cell, columnNumber + i - 1 );
		}
	}

	/**
	 * Get GUI infromation of row. For CSS table support auto layout, the GUI
	 * infor is different with model info.
	 * 
	 * @param row
	 * @return
	 */
	public TableHandleAdapter.RowUIInfomation getRowInfo( Object row )
	{
		if ( checkDirty( ) )
		{
			reload( );
		}
		return (TableHandleAdapter.RowUIInfomation) rowInfo.get( row );
	}

	/**
	 * 
	 * @see org.eclipse.birt.designer.core.facade.DesignElementHandleAdapter#reload()
	 */
	public void reload( )
	{
		super.reload( );
		initRowsInfo( );
		getModelAdaptHelper( ).markDirty( false );
	}

	/**
	 * Gets the all columns list.
	 * 
	 * @return The columns list.
	 */
	public List getColumns( )
	{
		List list = new ArrayList( );
		insertIteratorToList( getTableHandle( ).getColumns( ).iterator( ), list );
		return list;
	}

	/**
	 * Gets the special row
	 * 
	 * @param i
	 *            The row number.
	 * @return The special row.
	 */
	public Object getRow( int i )
	{
		List list = getRows( );
		if ( i >= 1 && i <= list.size( ) )
		{
			return list.get( i - 1 );
		}
		return null;
	}

	/**
	 * Gets the special column
	 * 
	 * @param i
	 *            The column number.
	 * @return The special column.
	 */
	public Object getColumn( int i )
	{

		List list = getColumns( );
		if ( i >= 1 && i <= list.size( ) )
		{
			return list.get( i - 1 );
		}
		return null;
	}

	/**
	 * Gets the special cell.
	 * 
	 * @param rowNumber
	 *            The row number.
	 * @param columnNumber
	 *            The column number.
	 * @param bool
	 * @return The special cell.
	 */
	public Object getCell( int rowNumber, int columnNumber, boolean bool )
	{
		Object obj = getRow( rowNumber );
		TableHandleAdapter.RowUIInfomation info = getRowInfo( obj );
		Object retValue = info.getAllChildren( ).get( columnNumber - 1 );
		if ( bool )
		{
			return retValue;
		}

		if ( HandleAdapterFactory.getInstance( )
				.getCellHandleAdapter( retValue )
				.getRowNumber( ) != rowNumber
				|| HandleAdapterFactory.getInstance( )
						.getCellHandleAdapter( retValue )
						.getColumnNumber( ) != columnNumber )
		{
			retValue = null;
		}

		return retValue;

	}

	/**
	 * Gets the special cell.
	 * 
	 * @param i
	 *            The row number.
	 * @param j
	 *            The column number.
	 * @return The special cell.
	 */
	public Object getCell( int i, int j )
	{
		return getCell( i, j, true );
	}

	/**
	 * Calculates table layout size. For table supports auto layout, the layout
	 * size need to be calculated when drawing.
	 * 
	 * @return
	 */
	public Dimension calculateSize( )
	{
		if ( !( getModelAdaptHelper( ) instanceof ITableAdaptHelper ) )
		{
			return new Dimension( );
		}

		ITableAdaptHelper tableHelper = (ITableAdaptHelper) getModelAdaptHelper( );

		int columnCount = getColumnCount( );
		int samColumnWidth = 0;
		for ( int i = 0; i < columnCount; i++ )
		{
			samColumnWidth = samColumnWidth
					+ tableHelper.caleVisualWidth( i + 1 );
		}

		int rowCount = getRowCount( );
		int samRowHeight = 0;
		for ( int i = 0; i < rowCount; i++ )
		{
			samRowHeight = samRowHeight + tableHelper.caleVisualHeight( i + 1 );
		}

		return new Dimension( samColumnWidth, samRowHeight ).expand( tableHelper.getInsets( )
				.getWidth( ),
				tableHelper.getInsets( ).getHeight( ) );
	}

	/**
	 * Adjust size of table layout.
	 * 
	 * @param size
	 *            is all figure size
	 * @throws SemanticException
	 */
	public void ajustSize( Dimension size ) throws SemanticException
	{
		if ( !( getModelAdaptHelper( ) instanceof ITableAdaptHelper ) )
		{
			return;
		}

		ITableAdaptHelper tableHelper = (ITableAdaptHelper) getModelAdaptHelper( );
		size = size.shrink( tableHelper.getInsets( ).getWidth( ),
				tableHelper.getInsets( ).getHeight( ) );

		int columnCount = getColumnCount( );
		int samColumnWidth = 0;
		for ( int i = 0; i < columnCount; i++ )
		{
			if ( i != columnCount - 1 )
			{
				samColumnWidth = samColumnWidth
						+ tableHelper.caleVisualWidth( i + 1 );
			}
		}
		int lastColumnWidth = size.width - samColumnWidth;
		if ( lastColumnWidth < tableHelper.getMinWidth( columnCount ) )
		{
			lastColumnWidth = tableHelper.getMinWidth( columnCount );
			HandleAdapterFactory.getInstance( )
					.getColumnHandleAdapter( getColumn( columnCount ) )
					.setWidth( lastColumnWidth );
		}
		else if ( lastColumnWidth != tableHelper.caleVisualWidth( columnCount ) )
		{
			HandleAdapterFactory.getInstance( )
					.getColumnHandleAdapter( getColumn( columnCount ) )
					.setWidth( lastColumnWidth );
		}

		int rowCount = getRowCount( );
		int samRowHeight = 0;
		for ( int i = 0; i < rowCount; i++ )
		{
			if ( i != rowCount - 1 )
			{
				samRowHeight = samRowHeight
						+ tableHelper.caleVisualHeight( i + 1 );
			}
		}
		int lastRowHeight = size.height - samRowHeight;

		if ( lastRowHeight < tableHelper.getMinHeight( rowCount ) )
		{
			lastRowHeight = tableHelper.getMinHeight( rowCount );
			HandleAdapterFactory.getInstance( )
					.getRowHandleAdapter( getRow( rowCount ) )
					.setHeight( lastRowHeight );
		}
		else if ( lastRowHeight != tableHelper.caleVisualHeight( rowCount ) )
		{
			HandleAdapterFactory.getInstance( )
					.getRowHandleAdapter( getRow( rowCount ) )
					.setHeight( lastRowHeight );
		}
		HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( getRow( rowCount ) )
				.setHeight( lastRowHeight );

		setSize( new Dimension( samColumnWidth + lastColumnWidth, samRowHeight
				+ lastRowHeight ).expand( tableHelper.getInsets( ).getWidth( ),
				tableHelper.getInsets( ).getHeight( ) ) );
	}

	/**
	 * Get the minimum height.of a specific row.
	 * 
	 * @param rowNumber
	 * @return The minimum height.
	 */
	public int getMinHeight( int rowNumber )
	{
		// TODO Auto-generated method stub
		return RowHandleAdapter.DEFAULT_MINHEIGHT;
	}

	/**
	 * Get the minimum width a specific row.
	 * 
	 * @param columnNumber
	 * @return The minimum width.
	 */
	public int getMinWidth( int columnNumber )
	{

		// TODO Auto-generated method stub
		return ColumnHandleAdapter.DEFAULT_MINWIDTH;
	}

	/**
	 * @return client area
	 */
	public Dimension getClientAreaSize( )
	{
		if ( getModelAdaptHelper( ) instanceof ITableAdaptHelper )
		{
			return ( (ITableAdaptHelper) getModelAdaptHelper( ) ).getClientAreaSize( );
		}

		return new Dimension( );
	}

	private TableHandle getTableHandle( )
	{
		return (TableHandle) getHandle( );
	}

	/**
	 * Returns the defined width in model in Pixel.
	 * 
	 * @return
	 */
	public String getDefinedWidth( )
	{
		DimensionHandle handle = ( (ReportItemHandle) getHandle( ) ).getWidth( );

		if ( handle.getUnits( ) == null || handle.getUnits( ).length( ) == 0 )
		{
			return null;
		}
		else if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( handle.getUnits( ) ) )
		{
			return handle.getMeasure( )
					+ DesignChoiceConstants.UNITS_PERCENTAGE;
		}
		else
		{
			int px = (int) DEUtil.convertoToPixel( handle );

			if ( px <= 0 )
			{
				return null;
			}

			return String.valueOf( px );
		}
	}

	/**
	 * Get the default width.
	 * 
	 * @param colNumber
	 *            The column number.
	 * @return The default width.
	 */
	public int getDefaultWidth( int colNumber )
	{
		Dimension size = getDefaultSize( );
		Object obj = getRow( 1 );
		if ( obj == null )
		{
			return size.width;
		}

		int allNumbers = getColumnCount( );
		if ( allNumbers <= 0 )
		{
			return size.width;
		}
		if ( colNumber <= 0 )
		{
			return size.width;
		}
		int width = size.width;
		int columnNumber = allNumbers;
		for ( int i = 1; i < columnNumber + 1; i++ )
		{
			Object column = getColumn( i );
			ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance( )
					.getColumnHandleAdapter( column );
			if ( adapt.isCustomWidth( ) )
			{
				allNumbers = allNumbers - 1;
				width = width - adapt.getWidth( );
			}
		}

		if ( colNumber == allNumbers )
		{
			return width / allNumbers + width % allNumbers;
		}
		return ( width / allNumbers );
	}

	/**
	 * Gets the row count
	 * 
	 * @return The row count.
	 */
	public int getRowCount( )
	{
		return getRows( ).size( );
	}

	/**
	 * Gets the column count
	 * 
	 * @return The column count.
	 */
	public int getColumnCount( )
	{
		return getColumns( ).size( );
	}

	/**
	 * @return The data set.
	 */
	public Object getDataSet( )
	{
		return getTableHandle( ).getDataSet( );
	}

	/**
	 * Insert a row to a specific position.
	 * 
	 * @param rowNumber
	 *            The row number.
	 * @param parentRowNumber
	 *            The row number of parent.
	 * @throws SemanticException
	 */
	public void insertRow( int rowNumber, int parentRowNumber )
			throws SemanticException
	{
		transStar( TRANS_LABEL_INSERT_ROW );
		Assert.isLegal( rowNumber != 0 );
		int realRowNumber = rowNumber > 0 ? parentRowNumber + rowNumber
				: parentRowNumber + rowNumber + 1;
		int shiftPos = rowNumber > 0 ? rowNumber : rowNumber + 1;
		RowHandle row = (RowHandle) getRow( parentRowNumber );
		RowHandleAdapter adapt = HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( row );

		RowHandle copy = (RowHandle) adapt.copy( );

		TableHandleAdapter.RowUIInfomation rowInfo = getRowInfo( row );
		List rowList = rowInfo.getAllChildren( );
		int rowSize = rowList.size( );
		for ( int i = 0; i < rowSize; i++ )
		{
			CellHandle parentCell = (CellHandle) rowList.get( i );
			CellHandle cell = getCellHandleCopy( parentCell );
			copy.getSlot( TableRow.CONTENT_SLOT ).add( cell );
		}

		SlotHandle parentHandle = row.getContainerSlotHandle( );
		parentHandle.add( ( copy ) );

		int pos = parentHandle.findPosn( row.getElement( ) );
		parentHandle.shift( copy, pos + shiftPos );

		RowHandleAdapter copyAdapt = HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( copy );
		List copyChildren = copyAdapt.getChildren( );

		TableHandleAdapter.RowUIInfomation info = getRowInfo( copy );
		List list = info.getAllChildren( );

		List temp = new ArrayList( );
		int size = list.size( );

		List hasAdjust = new ArrayList( );
		for ( int i = 0; i < size; i++ )
		{
			Object fillCell = list.get( i );
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance( )
					.getCellHandleAdapter( fillCell );
			if ( cellAdapt.getRowNumber( ) != realRowNumber )
			{
				if ( !hasAdjust.contains( fillCell ) )
				{
					cellAdapt.setRowSpan( cellAdapt.getRowSpan( ) + 1 );
					hasAdjust.add( fillCell );
				}
				temp.add( new Integer( i ) );
			}
		}

		int copyRowSize = copyChildren.size( );
		for ( int i = 0; i < copyRowSize; i++ )
		{
			if ( temp.contains( new Integer( i ) ) )
			{
				( (CellHandle) copyChildren.get( i ) ).drop( );
			}
		}
		transEnd( );
	}

	/**
	 * Insert a column to a specific position.
	 * 
	 * @param columnNumber
	 *            The column number.
	 * @param parentColumnNumber
	 *            The column number of parent.
	 * @throws SemanticException
	 */
	public void insertColumn( int columnNumber, int parentColumnNumber )
			throws SemanticException
	{
		transStar( TRANS_LABEL_INSERT_COLUMN );
		assert columnNumber != 0;
		int realColumnNumber = columnNumber > 0 ? parentColumnNumber
				+ columnNumber : parentColumnNumber + columnNumber + 1;
		int shiftPos = columnNumber > 0 ? columnNumber : columnNumber + 1;
		ColumnHandle column = (ColumnHandle) getColumn( parentColumnNumber );
		ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance( )
				.getColumnHandleAdapter( column );

		ColumnHandle copy = (ColumnHandle) adapt.copy( );

		int rowNumber = getRowCount( );
		List copyChildren = new ArrayList( );
		for ( int i = 0; i < rowNumber; i++ )
		{

			RowHandle row = (RowHandle) getRow( i + 1 );

			TableHandleAdapter.RowUIInfomation rowInfo = getRowInfo( getRow( i + 1 ) );
			List rowList = rowInfo.getAllChildren( );

			CellHandle parentCell = (CellHandle) rowList.get( parentColumnNumber - 1 );
			CellHandle cell = getCellHandleCopy( parentCell );

			copyChildren.add( cell );
		}

		int copyRowSize = copyChildren.size( );
		for ( int i = 0; i < copyRowSize; i++ )
		{
			RowHandle row = (RowHandle) getRow( i + 1 );
			row.getSlot( TableRow.CONTENT_SLOT )
					.add( (CellHandle) ( copyChildren.get( i ) ),
							realColumnNumber - 1 );

		}
		SlotHandle parentHandle = column.getContainerSlotHandle( );
		parentHandle.add( ( copy ) );

		int pos = parentHandle.findPosn( column.getElement( ) );
		parentHandle.shift( copy, pos + shiftPos );

		List temp = new ArrayList( );

		List hasAdjust = new ArrayList( );
		for ( int i = 0; i < rowNumber; i++ )
		{
			TableHandleAdapter.RowUIInfomation rowInfo = getRowInfo( getRow( i + 1 ) );
			List rowList = rowInfo.getAllChildren( );

			Object fillCell = rowList.get( realColumnNumber - 1 );
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance( )
					.getCellHandleAdapter( fillCell );
			if ( cellAdapt.getColumnNumber( ) != realColumnNumber )
			{
				if ( !hasAdjust.contains( fillCell ) )
				{
					cellAdapt.setColumnSpan( cellAdapt.getColumnSpan( ) + 1 );
					hasAdjust.add( fillCell );
				}
				temp.add( new Integer( i ) );
			}
		}

		for ( int i = 0; i < copyRowSize; i++ )
		{
			if ( temp.contains( new Integer( i ) ) )
			{
				( (CellHandle) copyChildren.get( i ) ).drop( );
			}
		}

		transEnd( );
	}

	/**
	 * @param model
	 *            The object to be removed.
	 * @throws SemanticException
	 */
	public void removeChild( Object model ) throws SemanticException
	{
		assert ( model instanceof DesignElementHandle );
		DesignElementHandle ele = (DesignElementHandle) model;
		ele.drop( );
	}

	/**
	 * Get the padding of the current table.
	 * 
	 * @param retValue
	 *            The padding value of the current table.
	 * @return The padding's new value of the current table.
	 */
	public Insets getPadding( Insets retValue )
	{
		if ( retValue == null )
		{
			retValue = new Insets( );
		}
		else
		{
			retValue = new Insets( retValue );
		}

		DimensionHandle fontHandle = getHandle( ).getPrivateStyle( )
				.getFontSize( );

		int fontSize = 12;//??
		if ( fontHandle.getValue( ) instanceof String )
		{
			fontSize = Integer.valueOf( (String) DesignerConstants.fontMap.get( DEUtil.getFontSize( getHandle( ) ) ) )
					.intValue( );
		}
		else if ( fontHandle.getValue( ) instanceof DimensionValue )
		{
			DEUtil.convertToPixel( fontHandle.getValue( ), fontSize );
		}

		DimensionValue dimensionValue = (DimensionValue) getReportItemHandle( ).getProperty( Style.PADDING_TOP_PROP );
		double px = DEUtil.convertToPixel( dimensionValue, fontSize );

		dimensionValue = (DimensionValue) getReportItemHandle( ).getProperty( Style.PADDING_BOTTOM_PROP );
		double py = DEUtil.convertToPixel( dimensionValue, fontSize );

		retValue.top = (int) px;
		retValue.bottom = (int) py;

		dimensionValue = (DimensionValue) getReportItemHandle( ).getProperty( Style.PADDING_LEFT_PROP );
		px = DEUtil.convertToPixel( dimensionValue, fontSize );

		dimensionValue = (DimensionValue) getReportItemHandle( ).getProperty( Style.PADDING_RIGHT_PROP );
		py = DEUtil.convertToPixel( dimensionValue, fontSize );

		retValue.left = (int) px;
		retValue.right = (int) py;

		return retValue;
	}

	/**
	 * Delete specific columns from the current table.
	 * 
	 * @param columns
	 *            The columns to be deleted.
	 * @throws SemanticException
	 */
	public void deleteColumn( int[] columns ) throws SemanticException
	{

		if ( getColumnCount( ) == 1 )
		{
			getTableHandle( ).drop( );
			return;
		}
		transStar( TRANS_LABEL_DELETE_COLUMNS );
		int len = columns.length;
		for ( int i = 0; i < len; i++ )
		{
			deleteColumn( columns[i] );
		}
		transEnd( );
	}

	/**
	 * Delete a specific column from the current table.
	 * 
	 * @param columnNumber
	 *            The column to be deleted.
	 * @throws SemanticException
	 */
	public void deleteColumn( int columnNumber ) throws SemanticException
	{
		transStar( TRANS_LABEL_DELETE_COLUMN );
		int rowCount = getRowCount( );
		ColumnHandle column = (ColumnHandle) getColumn( columnNumber );

		List deleteCells = new ArrayList( );
		for ( int i = 0; i < rowCount; i++ )
		{
			Object row = getRow( i + 1 );
			TableHandleAdapter.RowUIInfomation info = getRowInfo( row );
			deleteCells.add( info.getAllChildren( ).get( columnNumber - 1 ) );
		}

		List trueDeleteCells = new ArrayList( );
		int size = deleteCells.size( );
		for ( int i = 0; i < size; i++ )
		{
			Object cell = deleteCells.get( i );
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance( )
					.getCellHandleAdapter( cell );
			if ( cellAdapt.getColumnNumber( ) == columnNumber
					&& cellAdapt.getColumnSpan( ) == 1
					&& !trueDeleteCells.contains( cell ) )
			{
				trueDeleteCells.add( cell );
			}
		}
		List temp = new ArrayList( );
		for ( int i = 0; i < size; i++ )
		{
			Object cell = deleteCells.get( i );
			if ( !trueDeleteCells.contains( cell ) && !temp.contains( cell ) )
			{
				CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance( )
						.getCellHandleAdapter( cell );
				cellAdapt.setColumnSpan( cellAdapt.getColumnSpan( ) - 1 );
				temp.add( cell );
			}

		}

		size = trueDeleteCells.size( );
		for ( int i = 0; i < size; i++ )
		{
			CellHandle cell = (CellHandle) trueDeleteCells.get( i );
			cell.drop( );
		}
		column.drop( );

		transEnd( );
		reload( );
	}

	/**
	 * Delete specific rows from the current table.
	 * 
	 * @param rows
	 *            The rows to be deleted.
	 * @throws SemanticException
	 */
	public void deleteRow( int[] rows ) throws SemanticException
	{

		if ( getRowCount( ) == 1 )
		{
			getTableHandle( ).drop( );
			return;
		}
		transStar( TRANS_LABEL_DELETE_ROWS );
		Arrays.sort( rows );
		int len = rows.length;
		for ( int i = len - 1; i >= 0; i-- )
		{
			deleteRow( rows[i] );
		}
		transEnd( );
	}

	/**
	 * Delete a specific row from the current table.
	 * 
	 * @param rowsNumber
	 *            The row to be deleted.
	 * @throws SemanticException
	 */
	public void deleteRow( int rowsNumber ) throws SemanticException
	{
		transStar( TRANS_LABEL_DELETE_ROW );
		int rowCount = getRowCount( );
		RowHandle row = (RowHandle) getRow( rowsNumber );

		RowHandleAdapter rowAdapt = HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( row );

		List temp = new ArrayList( );
		RowHandle nextRow = null;

		List shiftCellInfo = new ArrayList( );
		if ( rowsNumber + 1 <= rowCount )
		{
			List trueChildren = rowAdapt.getChildren( );
			int cellSize = trueChildren.size( );

			nextRow = (RowHandle) getRow( rowsNumber + 1 );
			TableHandleAdapter.RowUIInfomation nextRowInfo = getRowInfo( nextRow );
			List nextRowChildren = nextRowInfo.getAllChildren( );

			for ( int i = 0; i < cellSize; i++ )
			{
				Object cellHandle = trueChildren.get( i );
				CellHandleAdapter adapt = HandleAdapterFactory.getInstance( )
						.getCellHandleAdapter( cellHandle );

				if ( adapt.getRowSpan( ) != 1 )
				{
					int numberInfo = 0;
					int index = nextRowChildren.indexOf( cellHandle );
					for ( int j = 0; j < index; j++ )
					{
						Object nextRowCell = nextRowChildren.get( j );
						CellHandleAdapter nextRowCellAdapt = HandleAdapterFactory.getInstance( )
								.getCellHandleAdapter( nextRowCell );

						if ( nextRowCellAdapt.getRowNumber( ) == rowsNumber + 1
								&& !temp.contains( nextRowCell ) )
						{
							numberInfo = numberInfo + 1;
						}
						temp.add( nextRowCell );
					}
					numberInfo = numberInfo + shiftCellInfo.size( );
					shiftCellInfo.add( new ShiftNexRowInfo( numberInfo,
							cellHandle ) );

				}
			}
		}

		TableHandleAdapter.RowUIInfomation info = getRowInfo( row );
		List cells = info.getAllChildren( );
		temp.clear( );

		int cellSize = cells.size( );
		for ( int j = 0; j < cellSize; j++ )
		{
			Object cellHandle = cells.get( j );
			CellHandleAdapter adapt = HandleAdapterFactory.getInstance( )
					.getCellHandleAdapter( cellHandle );

			if ( adapt.getRowNumber( ) != rowsNumber
					&& !temp.contains( cellHandle ) )
			{
				adapt.setRowSpan( adapt.getRowSpan( ) - 1 );
				temp.add( cellHandle );
			}
		}
		//row.drop( );
		for ( int i = 0; i < shiftCellInfo.size( ); i++ )
		{
			ShiftNexRowInfo shiftInfo = (ShiftNexRowInfo) shiftCellInfo.get( i );
			CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance( )
					.getCellHandleAdapter( shiftInfo.cell );
			cellAdapt.setRowSpan( cellAdapt.getRowSpan( ) - 1 );
			SlotHandle slotHandle = row.getCells( );
			slotHandle.move( (DesignElementHandle) shiftInfo.cell,
					nextRow,
					TableRow.CONTENT_SLOT,
					shiftInfo.index );
		}

		row.drop( );

		transEnd( );
		reload( );
	}

	class ShiftNexRowInfo
	{

		protected int index;
		protected Object cell;

		/**
		 * @param index
		 * @param cell
		 */
		public ShiftNexRowInfo( int index, Object cell )
		{
			super( );
			this.index = index;
			this.cell = cell;
		}
	}

	static class RowUIInfomation
	{

		protected static final String GRID_ROW = NAME_NULL; //$NON-NLS-1$

		private String type = ""; //$NON-NLS-1$
		private String rowDisplayName = ""; //$NON-NLS-1$
		Object[] cells = null;
		int[] infactAdd = new int[0];

		private RowUIInfomation( int columnMunber )
		{
			cells = new Object[columnMunber];
		}

		public String getRowDisplayName( )
		{
			return rowDisplayName;
		}

		public void setRowDisplayName( String rowDisplayName )
		{
			this.rowDisplayName = rowDisplayName;
		}

		public String getType( )
		{
			return type;
		}

		public void setType( String type )
		{
			this.type = type;
		}

		public void addChildren( Object obj, int index )
		{
			int cellSize = cells.length;
			if ( index >= cellSize )
			{
				return;
			}
			ArrayList list = new ArrayList( );
			if ( cells[index] != null )
			{
				Object[] newArray = new Object[cellSize];
				for ( int i = 0; i < cellSize; i++ )
				{
					if ( containIndex( i ) )
					{
						newArray[i] = cells[i];
					}
					else if ( cells[i] != null )
					{
						list.add( cells[i] );
					}
				}
				newArray[index] = obj;
				int listSize = list.size( );

				for ( int i = 0; i < listSize; i++ )
				{
					for ( int j = 0; j < cellSize; j++ )
					{
						if ( newArray[j] == null )
						{
							newArray[j] = list.get( i );
							break;
						}
					}
				}
				cells = newArray;
			}
			else
			{
				cells[index] = obj;
			}

			int lenegth = infactAdd.length;
			int[] temp = new int[lenegth + 1];

			System.arraycopy( infactAdd, 0, temp, 0, lenegth );
			temp[lenegth] = index;
			infactAdd = temp;
		}

		private boolean containIndex( int index )
		{
			int length = infactAdd.length;
			for ( int i = 0; i < length; i++ )
			{
				if ( infactAdd[i] == index )
				{
					return true;
				}
			}
			return false;
		}

		public void addChildren( Collection c )
		{
			Iterator itor = c.iterator( );
			int cellSize = cells.length;
			while ( itor.hasNext( ) )
			{
				Object obj = itor.next( );
				for ( int i = 0; i < cellSize; i++ )
				{
					if ( cells[i] == null )
					{
						cells[i] = obj;
						break;
					}
				}
			}
		}

		public List getAllChildren( )
		{
			ArrayList retValue = new ArrayList( );
			int cellSize = cells.length;
			for ( int i = 0; i < cellSize; i++ )
			{
				retValue.add( cells[i] );
			}
			return retValue;
		}
	}

	/**
	 * @param list
	 * @return If can merge return true, else false.
	 */
	public boolean canMerge( List list )
	{
		assert list != null;
		int size = list.size( );
		if ( size <= 1 )
		{
			return false;
		}
		String first = getRowInfo( ( (CellHandle) list.get( 0 ) ).getContainer( ) ).getRowDisplayName( );
		for ( int i = 1; i < size; i++ )
		{
			RowUIInfomation info = getRowInfo( ( (CellHandle) list.get( i ) ).getContainer( ) );
			if ( info == null )
			{
				return false;
			}
			String str = info.getRowDisplayName( );
			if ( !first.equals( str ) )
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Split a cell to cells.
	 * 
	 * @param model
	 * @throws SemanticException
	 * @throws NameException
	 * @throws ContentException
	 */
	public void splitCell( Object model ) throws ContentException,
			NameException, SemanticException
	{
		// TODO Auto-generated method stub
		transStar( TRANS_LABEL_SPLIT_CELLS );
		assert model instanceof CellHandle;

		CellHandle cellHandle = (CellHandle) model;
		CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance( )
				.getCellHandleAdapter( cellHandle );
		int rowNumber = cellAdapt.getRowNumber( );
		int rowSpan = cellAdapt.getRowSpan( );
		int colSpan = cellAdapt.getColumnSpan( );

		//fill the cell row
		if ( colSpan != 1 )
		{
			int index = getIndexofParent( cellHandle );
			RowHandle rowHandle = (RowHandle) cellHandle.getContainer( );
			for ( int i = 1; i < colSpan; i++ )
			{
				rowHandle.addElement( getCellHandleCopy( cellHandle ),
						TableRow.CONTENT_SLOT,
						i + index );
			}
		}
		if ( rowSpan != 1 )
		{
			for ( int i = rowNumber + 1; i < rowNumber + rowSpan; i++ )
			{
				RowHandle rowHandle = (RowHandle) getRow( i );
				int index = getIndexofParent( cellHandle );
				for ( int j = 0; j < colSpan; j++ )
				{
					rowHandle.addElement( getCellHandleCopy( cellHandle ),
							TableRow.CONTENT_SLOT,
							j + index );
				}
			}

		}
		cellAdapt.setRowSpan( 1 );
		cellAdapt.setColumnSpan( 1 );
		transEnd( );
	}

	private int getIndexofParent( CellHandle cellHandle )
	{
		CellHandleAdapter cellAdapt = HandleAdapterFactory.getInstance( )
				.getCellHandleAdapter( cellHandle );
		TableHandleAdapter.RowUIInfomation info = getRowInfo( cellHandle.getContainer( ) );
		List list = info.getAllChildren( );
		int index = list.indexOf( cellHandle );
		List temp = new ArrayList( );
		int number = 0;
		for ( int j = 0; j < index; j++ )
		{
			CellHandleAdapter childCellAdapt = HandleAdapterFactory.getInstance( )
					.getCellHandleAdapter( list.get( j ) );
			if ( childCellAdapt.getRowNumber( ) == cellAdapt.getRowNumber( )
					&& !temp.contains( list.get( j ) ) )
			{
				number = number + 1;
			}
			temp.add( list.get( j ) );
		}
		return number;
	}

	/**
	 * Gets the cell handle copoy to support row/column insert.
	 * 
	 * @param cellHandle
	 * @return
	 * @throws SemanticException
	 */
	public CellHandle getCellHandleCopy( CellHandle cellHandle )
			throws SemanticException
	{
		if ( cellHandle == null )
		{
			return null;
		}
		CellHandle cell = cellHandle.getElementFactory( ).newCell( );
		Iterator iter = cellHandle.getPropertyIterator( );
		while ( iter.hasNext( ) )
		{
			PropertyHandle handle = (PropertyHandle) iter.next( );
			String key = handle.getDefn( ).getName( );
			if ( handle.isLocal( )
					&& ( !( Cell.COL_SPAN_PROP.equals( key ) || Cell.ROW_SPAN_PROP.equals( key ) ) ) )
			{
				cell.setProperty( key, cellHandle.getProperty( key ) );
			}
		}
		return cell;
	}

	/**
	 * Provides insert group function.
	 * 
	 * @return
	 * @throws ContentException
	 * @throws NameException
	 */
	public TableGroupHandle insertGroup( ) throws ContentException,
			NameException
	{
		if ( DEUtil.getDataSetList( getTableHandle( ) ).isEmpty( ) )
		{
			return null;
		}
		transStar( TRANS_LABEL_INSERT_GROUP );

		RowHandle header = getTableHandle( ).getElementFactory( ).newTableRow( );
		RowHandle footer = getTableHandle( ).getElementFactory( ).newTableRow( );
		addCell( header );
		addCell( footer );

		TableGroupHandle groupHandle = getTableHandle( ).getElementFactory( )
				.newTableGroup( );
		groupHandle.getSlot( TableGroup.HEADER_SLOT ).add( header );
		groupHandle.getSlot( TableGroup.FOOTER_SLOT ).add( footer );

		SlotHandle handle = getTableHandle( ).getGroups( );
		handle.add( groupHandle );

		transEnd( );
		return groupHandle;
	}

	/**
	 * Provides remove group function
	 * 
	 * @throws SemanticException
	 *  
	 */
	public void removeGroup( Object group ) throws SemanticException
	{
		( (RowHandle) group ).getContainer( ).drop( );
	}

	private void addCell( RowHandle handle ) throws ContentException,
			NameException
	{
		int count = getColumnCount( );
		for ( int i = 0; i < count; i++ )
		{
			CellHandle cell = handle.getElementFactory( ).newCell( );
			handle.addElement( cell, TableRow.CONTENT_SLOT );
		}
	}

	/**
	 * Insert row in model
	 * 
	 * @param id
	 * @throws ContentException
	 * @throws NameException
	 */
	public void insertRowInSlotHandle( int id ) throws ContentException,
			NameException
	{
		transStar( TRANS_LABEL_INCLUDE + getOperationName( id ) );
		RowHandle rowHandle = getTableHandle( ).getElementFactory( )
				.newTableRow( );
		addCell( rowHandle );
		getTableHandle( ).getSlot( id ).add( rowHandle );
		transEnd( );
	}

	/**
	 * Delete row in model
	 * 
	 * @param id
	 * @throws SemanticException
	 */
	public void deleteRowInSlotHandle( int id ) throws SemanticException
	{
		transStar( TRANS_LABEL_NOT_INCLUDE + getOperationName( id ) );
		int[] rows = new int[0];
		Iterator itor = getTableHandle( ).getSlot( id ).iterator( );
		while ( itor.hasNext( ) )
		{
			Object obj = itor.next( );
			RowHandleAdapter adapt = HandleAdapterFactory.getInstance( )
					.getRowHandleAdapter( obj );
			int lenegth = rows.length;
			int[] temp = new int[lenegth + 1];

			System.arraycopy( rows, 0, temp, 0, lenegth );
			temp[lenegth] = adapt.getRowNumber( );
			rows = temp;
		}
		deleteRow( rows );
		transEnd( );
	}

	private String getOperationName( int id )
	{
		switch ( id )
		{
			case HEADER :
				return NAME_HEADRER;
			case FOOTER :
				return NAME_FOOTER;
			case DETAIL :
				return NAME_DETAIL;
			default :
				return NAME_NULL; //$NON-NLS-1$
		}
	}

	/**
	 * Check if the slot handle contains specified id.
	 * 
	 * @param id
	 * @return
	 */
	public boolean hasSlotHandleRow( int id )
	{
		Iterator itor = getTableHandle( ).getSlot( id ).iterator( );
		while ( itor.hasNext( ) )
		{
			return true;
		}
		return false;
	}
}