/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.table.LayoutTableModel;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ColumnHelper;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableItemModel;

import com.ibm.icu.util.ULocale;

/**
 * Represents a table element. A table has a localized caption and can repeat
 * its heading at the top of each page. The table is a list that is structured
 * into a rows and columns. The columns are defined for the entire table. Rows
 * are clustered into a set of groups.
 * <p>
 * To get the layout of a table, it is recommended to use
 * <code>LayoutTableModel</code>.
 * 
 * @see org.eclipse.birt.report.model.elements.TableItem
 * @see org.eclipse.birt.report.model.api.elements.table.LayoutTableModel
 */

public class TableHandle extends ListingHandle implements ITableItemModel
{

	/**
	 * Constructs a handle for the table with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public TableHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the column slot. The column slot represents a list of Column
	 * elements that describe the table columns.
	 * 
	 * @return a handle to the detail slot
	 * @see SlotHandle
	 */

	public SlotHandle getColumns( )
	{
		return getSlot( ITableItemModel.COLUMN_SLOT );
	}

	/**
	 * Returns the number of columns in the table. The number is defined as 1)
	 * the sum of columns described in the "column" slot, or 2) the widest row
	 * defined in the detail, header or footer slots if column slot is empty.
	 * 
	 * @return the number of columns in the table
	 */

	public int getColumnCount( )
	{
		return ( (TableItem) getElement( ) ).getColumnCount( module );
	}

	/**
	 * Returns the caption text of this table.
	 * 
	 * @return the caption text
	 */

	public String getCaption( )
	{
		return getStringProperty( ITableItemModel.CAPTION_PROP );
	}

	/**
	 * Sets the caption text of this table.
	 * 
	 * @param caption
	 *            the caption text
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setCaption( String caption ) throws SemanticException
	{
		setStringProperty( ITableItemModel.CAPTION_PROP, caption );
	}

	/**
	 * Returns the value of the summary.
	 * 
	 * @return the value of summary
	 */
	public String getSummary( )
	{
		return getStringProperty( ITableItemModel.SUMMARY_PROP );
	}

	/**
	 * Sets the value of summary.
	 * 
	 * @param summary
	 *            the value of summary
	 * @throws SemanticException
	 */
	public void setSummary( String summary ) throws SemanticException
	{
		setStringProperty( ITableItemModel.SUMMARY_PROP, summary );
	}

	/**
	 * Returns the resource key of the caption.
	 * 
	 * @return the resource key of the caption
	 */

	public String getCaptionKey( )
	{
		return getStringProperty( ITableItemModel.CAPTION_KEY_PROP );
	}

	/**
	 * Sets the resource key of the caption.
	 * 
	 * @param captionKey
	 *            the resource key of the caption
	 * @throws SemanticException
	 *             if the caption resource-key property is locked.
	 */

	public void setCaptionKey( String captionKey ) throws SemanticException
	{
		setStringProperty( ITableItemModel.CAPTION_KEY_PROP, captionKey );
	}

	/**
	 * Copies a column and cells under it with the given column number.
	 * 
	 * @param columnIndex
	 *            the column position indexing from 1.
	 * @return <code>true</code> if this column band can be copied. Otherwise
	 *         <code>false</code>.
	 */

	public boolean canCopyColumn( int columnIndex )
	{
		ColumnBandPasteAction pasteAction = new ColumnBandPasteAction(
				new TableColumnBandAdapter( this ) );

		try
		{
			pasteAction.copyColumnBand( columnIndex );
		}
		catch ( SemanticException e )
		{
			return false;
		}

		return true;
	}

	/**
	 * Checks whether the paste operation can be done with the given copied
	 * column band data, the column index and the operation flag.
	 * 
	 * @param data
	 *            the column band data to paste
	 * @param columnIndex
	 *            the column index from 1 to the number of columns in the table
	 * @param inForce
	 *            <code>true</code> indicates to paste the column regardless of
	 *            the different layout of cells. <code>false</code> indicates
	 *            not.
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canPasteColumn( ColumnBandData data, int columnIndex,
			boolean inForce )
	{
		if ( data == null )
			throw new IllegalArgumentException( "empty column to check." ); //$NON-NLS-1$

		ColumnBandPasteAction pasteAction = new ColumnBandPasteAction(
				new TableColumnBandAdapter( this ) );

		return pasteAction.canPaste( columnIndex, inForce, data );
	}

	/**
	 * Checks whether the copy operation can be done with the given parameters.
	 * 
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @return <code>true</code> if this row band can be copied. Otherwise
	 *         <code>false</code>.
	 * 
	 */

	public boolean canCopyRow( RowOperationParameters parameters )
	{
		if ( parameters == null )
			return false;
		RowBandCopyAction action = new RowBandCopyAction(
				new TableRowBandAdapter( this ) );

		return action.canCopy( parameters );
	}

	/**
	 * Checks whether the paste operation can be done with the given parameters.
	 * 
	 * @param copiedRow
	 *            the copied table row
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canPasteRow( IDesignElement copiedRow,
			RowOperationParameters parameters )
	{
		if ( copiedRow == null || parameters == null
				|| !( copiedRow instanceof TableRow ) )
			return false;
		RowBandPasteAction pasteAction = new RowBandPasteAction(
				new TableRowBandAdapter( this ) );

		return pasteAction.canPaste( (TableRow) copiedRow, parameters );
	}

	/**
	 * Checks whether inserting an empty table row can be done with the given
	 * parameters.
	 * 
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @return <code>true</code> indicates the insert operation can be done.
	 *         Otherwise <code>false</code>.
	 */
	public boolean canInsertRow( RowOperationParameters parameters )
	{
		if ( parameters == null )
			return false;
		RowBandInsertAction pasteAction = new RowBandInsertAction(
				new TableRowBandAdapter( this ) );

		return pasteAction.canInsert( parameters );
	}

	/**
	 * Checks whether the inserting and paste table row to the given destination
	 * row with the given parameters.
	 * 
	 * @param copiedRow
	 *            the copied table row
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @return <code>true</code> indicates the insert and paste operation can be
	 *         done. Otherwise <code>false</code>.
	 */

	public boolean canInsertAndPasteRow( IDesignElement copiedRow,
			RowOperationParameters parameters )
	{
		if ( copiedRow == null || parameters == null
				|| !( copiedRow instanceof TableRow ) )
			return false;

		RowBandInsertAndPasteAction action = new RowBandInsertAndPasteAction(
				new TableRowBandAdapter( this ) );

		return action.canInsertAndPaste( (TableRow) copiedRow, parameters );
	}

	/**
	 * Checks whether the shift operation can be done with the given the given
	 * parameters.
	 * 
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @return <code>true</code> indicates the shift operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canShiftRow( RowOperationParameters parameters )
	{
		if ( parameters == null )
			return false;
		RowBandShiftAction action = new RowBandShiftAction(
				new TableRowBandAdapter( this ) );

		return action.canShift( parameters );
	}

	/**
	 * Copies a column and cells under it with the given column number.
	 * 
	 * @param columnIndex
	 *            the column number
	 * @return a new <code>ColumnBandAdapter</code> instance
	 * @throws SemanticException
	 *             if the cell layout of the column is invalid.
	 */

	public ColumnBandData copyColumn( int columnIndex )
			throws SemanticException
	{
		ColumnBandPasteAction pasteAction = new ColumnBandPasteAction(
				new TableColumnBandAdapter( this ) );

		return pasteAction.copyColumnBand( columnIndex );
	}

	/**
	 * Pastes a column with its cells to the given column number.
	 * 
	 * @param data
	 *            the data of a column band to paste
	 * @param columnNumber
	 *            the column index from 1 to the number of columns in the table
	 * @param inForce
	 *            <code>true</code> if pastes the column regardless of the
	 *            warning. Otherwise <code>false</code>.
	 * @throws SemanticException
	 * 
	 */

	public void pasteColumn( ColumnBandData data, int columnNumber,
			boolean inForce ) throws SemanticException
	{
		if ( data == null )
			throw new IllegalArgumentException( "empty column to paste." ); //$NON-NLS-1$

		ColumnBandPasteAction pasteAction = new ColumnBandPasteAction(
				new TableColumnBandAdapter( this ) );

		pasteAction.pasteColumnBand( columnNumber, inForce, data );
	}

	/**
	 * Copies table row with the given parameters.
	 * 
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @return a new <code>TableRow</code> instance
	 * @throws SemanticException
	 *             throw if paste operation is forbidden
	 * @throws IllegalArgumentException
	 *             throw if the input parameters are not valid
	 */

	public IDesignElement copyRow( RowOperationParameters parameters )
			throws SemanticException
	{
		if ( parameters == null )
			throw new IllegalArgumentException( "empty row to copy." );//$NON-NLS-1$
		RowBandCopyAction action = new RowBandCopyAction(
				new TableRowBandAdapter( this ) );

		return action.doCopy( parameters );

	}

	/**
	 * Pastes table row to destination row with the given parameters.
	 * 
	 * @param copiedRow
	 *            the copied table row
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @throws SemanticException
	 *             throw if paste operation is forbidden
	 * @throws IllegalArgumentException
	 *             throw if the input parameters are not valid
	 */

	public void pasteRow( IDesignElement copiedRow,
			RowOperationParameters parameters ) throws SemanticException
	{
		if ( copiedRow == null || parameters == null
				|| !( copiedRow instanceof TableRow ) )
			throw new IllegalArgumentException( "empty row to paste." );//$NON-NLS-1$

		RowBandPasteAction pasteAction = new RowBandPasteAction(
				new TableRowBandAdapter( this ) );

		pasteAction.doPaste( (TableRow) copiedRow, parameters );
	}

	/**
	 * Inserts table row to the given destination row with the given parameters.
	 * 
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @throws SemanticException
	 *             throw if paste operation is forbidden
	 * @throws IllegalArgumentException
	 *             throw if the input parameters are not valid
	 */

	public void insertRow( RowOperationParameters parameters )
			throws SemanticException
	{
		if ( parameters == null )
			throw new IllegalArgumentException( "empty row to insert." );//$NON-NLS-1$

		RowBandInsertAction action = new RowBandInsertAction(
				new TableRowBandAdapter( this ) );

		action.doInsert( parameters );
	}

	/**
	 * Inserts and paste table row to the given destination row with the given
	 * parameters.
	 * 
	 * @param copiedRow
	 *            the copied table row
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @throws SemanticException
	 *             throw if paste operation is forbidden
	 * @throws IllegalArgumentException
	 *             throw if the input parameters are not valid
	 */

	public void insertAndPasteRow( IDesignElement copiedRow,
			RowOperationParameters parameters ) throws SemanticException
	{
		if ( copiedRow == null || parameters == null
				|| !( copiedRow instanceof TableRow ) )
			throw new IllegalArgumentException(
					"empty row to insert and paste." );//$NON-NLS-1$

		RowBandInsertAndPasteAction action = new RowBandInsertAndPasteAction(
				new TableRowBandAdapter( this ) );

		action.doInsertAndPaste( (TableRow) copiedRow, parameters );
	}

	/**
	 * Shifts table row to the given destination row with the given parameters.
	 * 
	 * @param parameters
	 *            parameters needed by insert operation.
	 * @throws SemanticException
	 *             throw if paste operation is forbidden
	 * @throws IllegalArgumentException
	 *             throw if the input parameters are not valid
	 */

	public void shiftRow( RowOperationParameters parameters )
			throws SemanticException
	{
		if ( parameters == null )
			throw new IllegalArgumentException( "empty row to shift." );//$NON-NLS-1$

		RowBandShiftAction action = new RowBandShiftAction(
				new TableRowBandAdapter( this ) );

		action.doShift( parameters );
	}

	/**
	 * Inserts and pastes a column with its cells to the given column number.
	 * 
	 * @param data
	 *            the data of a column band to paste
	 * @param columnNumber
	 *            the column index from 0 to the number of columns in the table
	 * @throws SemanticException
	 */

	public void insertAndPasteColumn( ColumnBandData data, int columnNumber )
			throws SemanticException
	{
		if ( data == null )
			throw new IllegalArgumentException( "empty column to paste." ); //$NON-NLS-1$

		ColumnBandInsertPasteAction insertAction = new ColumnBandInsertPasteAction(
				new TableColumnBandAdapter( this ) );

		insertAction.insertAndPasteColumnBand( columnNumber, data );
	}

	/**
	 * Checks whether the insert and paste operation can be done with the given
	 * copied column band data, the column index and the operation flag. This is
	 * different from <code>canPasteColumn</code> since this action creates an
	 * extra column for the table.
	 * 
	 * @param data
	 *            the column band data to paste
	 * @param columnIndex
	 *            the column index from 0 to the number of columns in the table
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canInsertAndPasteColumn( ColumnBandData data, int columnIndex )
	{
		if ( data == null )
			throw new IllegalArgumentException( "empty column to check." ); //$NON-NLS-1$

		ColumnBandInsertPasteAction insertAction = new ColumnBandInsertPasteAction(
				new TableColumnBandAdapter( this ) );

		return insertAction.canInsertAndPaste( columnIndex, data );
	}

	/**
	 * Moves the column from <code>sourceColumn</code> to <code>destIndex</code>
	 * .
	 * 
	 * @param sourceColumn
	 *            the source column ranging from 1 to the column number
	 * @param destColumn
	 *            the target column ranging from 0 to the column number
	 * @throws SemanticException
	 *             if the chosen column band is forbidden to shift
	 */

	public void shiftColumn( int sourceColumn, int destColumn )
			throws SemanticException
	{
		shiftColumn( sourceColumn, destColumn, true );
	}
	
	public void shiftColumn( int sourceColumn, int destColumn, boolean weakMode )
			throws SemanticException
	{
		ColumnBandShiftAction shiftAction = new ColumnBandShiftAction(
				new TableColumnBandAdapter( this ), weakMode );
		shiftAction.shiftColumnBand( sourceColumn, destColumn );		
	}

	/**
	 * Moves the column from <code>sourceColumn</code> to
	 * <code>destColumn</code>.
	 * 
	 * @param sourceColumn
	 *            the source column ranging from 1 to the column number
	 * @param destColumn
	 *            the target column ranging from 0 to the column number
	 * @return <code>true</code> if the chosen column band is legal to shift.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canShiftColumn( int sourceColumn, int destColumn )
	{
		ColumnBandShiftAction shiftAction = new ColumnBandShiftAction(
				new TableColumnBandAdapter( this ) );

		try
		{
			shiftAction.getShiftData( sourceColumn );
		}
		catch ( SemanticException e )
		{
			return false;
		}
		return shiftAction.checkTargetColumn( sourceColumn, destColumn );
	}

	/**
	 * Returns the layout model of the table.
	 * 
	 * @return the layout model of the table
	 */

	public LayoutTableModel getLayoutModel( )
	{
		return new LayoutTableModel( this );
	}

	/**
	 * Inserts a column to the table. This includes inserting a table column and
	 * corresponding table cells to the table item.
	 * <p>
	 * The insert action cannot be finished succesfully for cases like this:
	 * 
	 * <pre>
	 *                                       		&lt;cell colSpan=&quot;1/&gt;&lt;cell colSpan=&quot;1/&gt;
	 *                                       		&lt;cell colSpan=&quot;2/&gt;
	 * </pre>
	 * 
	 * if the user want to insert a column with cells to the column 2.
	 * 
	 * @param columnNumber
	 *            The 1-based column number.
	 * @param positionFlag
	 *            The column insert sign. 1 insert after position. -1 insert
	 *            before position
	 * @throws SemanticException
	 *             if the given position is occupied by any cell with a column
	 *             span equal or greater than 1.
	 * 
	 */

	public void insertColumn( int columnNumber, int positionFlag )
			throws SemanticException
	{
		ColumnBandInsertAction insertAction = new ColumnBandInsertAction(
				new TableColumnBandAdapter( this ) );
		insertAction.insertColumnBand( columnNumber, positionFlag );
	}

	/**
	 * Gets the cell at the position where the given row and column intersect
	 * regardless of slot information. The table is viewed as be constructed by
	 * a set of flattened rows.
	 * 
	 * @param row
	 *            the row position indexing from 1
	 * @param column
	 *            the column position indexing from 1
	 * @return the cell handle at the position if the cell exists, otherwise
	 *         <code>null</code>
	 */

	protected CellHandle getCell( int row, int column )
	{
		return getLayoutModel( ).getCell( row, column );
	}

	/**
	 * Gets the cell at the position where the given row and column intersect
	 * within the given slot. The first row in the slot is count as 1. And so
	 * on.
	 * <p>
	 * If <code>groupLevel</code> is less or equal than 0, then retrieve cell
	 * from header/detail/footer. If not, return the cell from the given group.
	 * 
	 * @param slotId
	 *            the slot id
	 * @param groupLevel
	 *            the group level indexing from 1. Or -1 if to get the cell from
	 *            header/detail/footer.
	 * @param row
	 *            the row position indexing from 1
	 * @param column
	 *            the column position indexing from 1
	 * @return the cell handle at the position if the cell exists, otherwise
	 *         <code>null</code>
	 */

	public CellHandle getCell( int slotId, int groupLevel, int row, int column )
	{
		if ( groupLevel <= 0 )
			return getLayoutModel( ).getCell( slotId, row, column );

		return getLayoutModel( ).getCell( groupLevel, slotId, row, column );

	}

	/**
	 * Figures out the column according to the index of the column.
	 * 
	 * @param columnIndex
	 *            the 1-based column index
	 * 
	 * @return the handle of the column at the specified position, or null if
	 *         not found.
	 */

	public ColumnHandle findColumn( int columnIndex )
	{
		TableColumn targetColumn = ColumnHelper.findColumn( module,
				getColumns( ).getSlot( ), columnIndex );
		return (ColumnHandle) targetColumn.getHandle( module );
	}

	/**
	 * Returns a list containing filters applied to the column at position of
	 * colIndex.
	 * 
	 * @param colIndex
	 *            the column index ranging from 0 to columnCount - 1
	 * @return a list containing matched filter conditions
	 */

	public List<FilterConditionHandle> getFilters( int colIndex )
	{
		if ( colIndex < 0 || colIndex >= getColumnCount( ) )
			return Collections.emptyList();

		String expr = getResultSetColumn( colIndex );
		if ( expr == null )
			return null;

		Iterator<FilterConditionHandle> iter = filtersIterator( );

		List<FilterConditionHandle> retValue = new ArrayList<FilterConditionHandle>( );

		// check filters in table
		List<FilterConditionHandle> tempList = checkFilters( iter, expr );
		if ( tempList != null )
			retValue.addAll( tempList );

		// check filters in groups
		SlotHandle groupSlot = getGroups( );
		for ( int i = 0; i < groupSlot.getCount( ); i++ )
		{
			TableGroupHandle tableGroup = (TableGroupHandle) groupSlot.get( i );
			iter = tableGroup.filtersIterator( );
			tempList = checkFilters( iter, expr );
			if ( tempList != null )
				retValue.addAll( tempList );
		}

		return retValue;
	}

	private List<FilterConditionHandle> checkFilters(Iterator<FilterConditionHandle> iter, String expr)
	{
		List<FilterConditionHandle> retValue = new ArrayList<FilterConditionHandle>();
		while ( iter.hasNext( ) )
		{
			FilterConditionHandle condition = iter.next();
			String curExpr = condition.getExpr( );
			List<IColumnBinding> cols = null;
			try
			{
				cols = ExpressionUtil.extractColumnExpressions(curExpr, ExpressionUtil.ROW_INDICATOR);
			}
			catch ( BirtException e )
			{
				// do nothing
				continue;
			}
			if ( cols != null )
			{
				for ( int i = 0; i < cols.size( ); i++ )
				{
					String tmpExpr = cols.get(i).getResultSetColumnName();
					if ( expr.equals( tmpExpr ) )
					{
						retValue.add( condition );
						break;
					}
				}
			}
		}

		return retValue;
	}

	/**
	 * Check all detail cells in the column, and retrun the first encountered
	 * data item.
	 * 
	 * @param columnIndex
	 *            0-based column index
	 * @return the result set column of the data item
	 */

	private String getResultSetColumn( int columnIndex )
	{
		SlotHandle detail = getDetail( );

		for ( int i = 0; i < detail.getCount( ); i++ )
		{
			CellHandle detailcell = getCell( IListingElementModel.DETAIL_SLOT,
					-1, i + 1, columnIndex + 1 );
			if ( detailcell == null )
				continue;

			Iterator<DesignElementHandle> it = detailcell.getContent().iterator();
			while ( it.hasNext( ) )
			{
				DesignElementHandle rptItem = it.next();
				if ( rptItem instanceof DataItemHandle )
				{
					return ( (DataItemHandle) rptItem ).getResultSetColumn( );
				}
			}
		}

		return null;
	}

	/**
	 * Returns if the table is a summary table. A summary table should not allow
	 * adding any detail rows.
	 * 
	 * @return <code>true<code> if the table is a summary table.Otherwise <code>false<code>.
	 */
	public boolean isSummaryTable( )
	{
		return getBooleanProperty( IS_SUMMARY_TABLE_PROP );
	}

	/**
	 * Sets the flag to control whether the table is a summary table. If the
	 * flag is checked, there should be no detail rows added for this table
	 * allowed.
	 * 
	 * @param isSummaryTable
	 *            the flag to set
	 * @throws SemanticException
	 */

	public void setIsSummaryTable( boolean isSummaryTable )
			throws SemanticException
	{
		setBooleanProperty( IS_SUMMARY_TABLE_PROP, isSummaryTable );
	}

	/**
	 * Sets the width of the table to fit columns' widths with default dpi
	 * value. The new width value will be the sum of the columns' widths.
	 * 
	 * @throws SemanticException
	 *             when width of the table cannot be calculated.
	 */
	public void setWidthToFitColumns( ) throws SemanticException
	{
		setWidthToFitColumns( -1 );
	}

	/**
	 * Sets the width of the table to fit columns' widths with the given dpi
	 * value. The new width value will be the sum of the columns' widths.
	 * 
	 * @param dpi
	 *            the dpi value
	 * @throws SemanticException
	 *             when width of the table cannot be calculated.
	 */
	public void setWidthToFitColumns( int dpi ) throws SemanticException
	{
		if ( null == getWidth( ).getValue( ) )
			return;

		DimensionValue absoluteWidths = null;
		DimensionValue relativeWidths = null;
		if ( element.getSlot( COLUMN_SLOT ).getCount( ) == 0 )
		{
			throw new SemanticError( element,
					SemanticError.DESIGN_EXCEPTION_TABLE_NO_COLUMN_FOUND );
		}
		List<DesignElementHandle> columns = getColumns().getContents();
		if ( dpi <= 0 )
		{
			// Invalid dpi value. Try the value defined in the design file.
			ModuleHandle moduleHandle = getModuleHandle( );
			if ( moduleHandle instanceof ReportDesignHandle )
			{
				dpi = ( (ReportDesignHandle) moduleHandle ).getImageDPI( );
			}
		}

		for ( int index = 0; index < columns.size( ); index++ )
		{
			ColumnHandle column = (ColumnHandle) columns.get( index );
			DimensionValue columnWidth = (DimensionValue) column.getWidth( )
					.getValue( );
			if ( columnWidth == null )
			{ // column index is 1-based.
				throw new SemanticError(
						element,
						new String[]{String.valueOf( index + 1 )},
						SemanticError.DESIGN_EXCEPTION_TABLE_COLUMN_WITH_NO_WIDTH );
			}
			int repeat = column.getRepeatCount( );
			String unit = columnWidth.getUnits( );
			if ( repeat > 1 )
			{ // Process repeat
				columnWidth = new DimensionValue( columnWidth.getMeasure( )
						* repeat, unit );
			}
			if ( DimensionUtil.isAbsoluteUnit( unit )
					|| DesignChoiceConstants.UNITS_PX.equalsIgnoreCase( unit ) )
			{
				absoluteWidths = DimensionUtil.mergeDimension( absoluteWidths,
						columnWidth, dpi );
			}
			else
			{
				if ( relativeWidths == null )
				{
					relativeWidths = columnWidth;
				}
				else
				{
					relativeWidths = DimensionUtil.mergeDimension(
							relativeWidths, columnWidth );
					if ( relativeWidths == null )
					{ // Fail to merge relative widths
						throw new SemanticError(
								element,
								SemanticError.DESIGN_EXCEPTION_TABLE_COLUMN_INCONSISTENT_RELATIVE_UNIT );
					}
				}
			}
		}
		if ( absoluteWidths == null || relativeWidths == null )
		{ // Only has columns with either absolute or relative widths
			if ( absoluteWidths == null )
			{
				if ( !DesignChoiceConstants.UNITS_PERCENTAGE
						.equalsIgnoreCase( relativeWidths.getUnits( ) ) )
				{ // Sum of percentage will not be set
					getWidth( ).setValue( relativeWidths );
				}
			}
			else
			{
				getWidth( ).setValue( absoluteWidths );
			}
		}
		else
		{ // Has columns with both absolute and relative widths
			if ( !DesignChoiceConstants.UNITS_PERCENTAGE
					.equalsIgnoreCase( relativeWidths.getUnits( ) ) )
			{ // Only percentage is acceptable
				throw new SemanticError(
						element,
						SemanticError.DESIGN_EXCEPTION_TABLE_COLUMN_INCONSISTENT_UNIT_TYPE );
			}
			double percent = relativeWidths.getMeasure( );
			if ( Double.compare( percent, 100 ) >= 0 )
			{ // Out of 100%
				throw new SemanticError(
						element,
						SemanticError.DESIGN_EXCEPTION_TABLE_COLUMN_ILLEGAL_PERCENTAGE );
			}
			percent /= 100;

			ULocale locale = getModule().getLocale();
			double value = absoluteWidths.getMeasure() / (1 - percent);
			String stringValue = StringUtil.doubleToString(value, 2, locale);

			setWidth(stringValue + absoluteWidths.getUnits());
		}
	}
}