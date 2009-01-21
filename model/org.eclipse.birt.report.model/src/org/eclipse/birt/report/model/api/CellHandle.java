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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;

/**
 * Represents a cell within a table or grid. A cell can span multiple rows
 * and/or columns. A cell can contain zero, one or many contents. However, since
 * BIRT will position multiple items automatically, the application should
 * generally provide its own container if the cell is to hold multiple items.
 * <p>
 * The application generally does not create cell handles directly. Instead, it
 * uses one of the navigation methods available on other element handles such as
 * <code>RowHandle</code>.
 * 
 * @see org.eclipse.birt.report.model.elements.Cell
 * @see RowHandle#getCells()
 */

public class CellHandle extends ReportElementHandle implements ICellModel
{

	/**
	 * Constructs a handle for the given design and design element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public CellHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the cell's column span. This is the number of table or grid
	 * columns occupied by this cell.
	 * 
	 * @return the column span
	 */

	public int getColumnSpan( )
	{
		return getIntProperty( ICellModel.COL_SPAN_PROP );
	}

	/**
	 * Sets the cell's column span. This is the number of table or grid columns
	 * occupied by this cell.
	 * 
	 * @param span
	 *            the column span
	 * 
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setColumnSpan( int span ) throws SemanticException
	{
		setIntProperty( ICellModel.COL_SPAN_PROP, span );
	}

	/**
	 * Returns the cell's row span. This is the number of table or grid rows
	 * occupied by this cell.
	 * 
	 * @return the row span
	 */

	public int getRowSpan( )
	{
		return getIntProperty( ICellModel.ROW_SPAN_PROP );
	}

	/**
	 * Sets the cell's row span. This is the number of table or grid rows
	 * occupied by this cell.
	 * 
	 * @param span
	 *            the row span
	 * 
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setRowSpan( int span ) throws SemanticException
	{
		setIntProperty( ICellModel.ROW_SPAN_PROP, span );
	}

	/**
	 * Returns the cell's drop property. This is how the cell should expand to
	 * fill the entire table or group. This property is valid only for cells
	 * within a table; but not for cells within a grid.
	 * 
	 * @return the string value of the drop property
	 * @see #setDrop(String)
	 */

	public String getDrop( )
	{
		return getStringProperty( ICellModel.DROP_PROP );
	}

	/**
	 * Sets the cell's drop property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li>DROP_TYPE_NONE</li>
	 * <li>DROP_TYPE_DETAIL</li>
	 * <li>DROP_TYPE_ALL</li>
	 * </ul>
	 * 
	 * <p>
	 * 
	 * Note that This property is valid only for cells within a table; but not
	 * for cells within a grid.
	 * 
	 * @param drop
	 *            the string value of the drop property
	 * 
	 * @throws SemanticException
	 *             if the property is locked or the input value is not one of
	 *             the above.
	 * 
	 * @see #getDrop()
	 */

	public void setDrop( String drop ) throws SemanticException
	{
		setStringProperty( ICellModel.DROP_PROP, drop );
	}

	/**
	 * Returns the contents of the cell. The cell can contain any number of
	 * items, but normally contains just one.
	 * 
	 * @return a handle to the content slot
	 */

	public SlotHandle getContent( )
	{
		return getSlot( ICellModel.CONTENT_SLOT );
	}

	/**
	 * Returns the cell's column property. The return value gives the column in
	 * which the cell starts. Columns are numbered from 1.
	 * 
	 * @return the column index, starting from 1.
	 */

	public int getColumn( )
	{
		return getIntProperty( ICellModel.COLUMN_PROP );
	}

	/**
	 * Sets the cell's column property. The input value gives the column in
	 * which the cell starts. Columns are numbered from 1.
	 * 
	 * @param column
	 *            the column index, starting from 1.
	 * 
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setColumn( int column ) throws SemanticException
	{
		setIntProperty( ICellModel.COLUMN_PROP, column );
	}

	/**
	 * Returns the cell's height.
	 * 
	 * @return the cell's height
	 */

	public DimensionHandle getHeight( )
	{
		return getDimensionProperty( ICellModel.HEIGHT_PROP );
	}

	/**
	 * Returns the cell's width.
	 * 
	 * @return the cell's width
	 */

	public DimensionHandle getWidth( )
	{
		return getDimensionProperty( ICellModel.WIDTH_PROP );
	}

	/**
	 * Gets the on-prepare script of the group. Startup phase. No data binding
	 * yet. The design of an element can be changed here.
	 * 
	 * @return the on-prepare script of the group
	 * 
	 */

	public String getOnPrepare( )
	{
		return getStringProperty( ICellModel.ON_PREPARE_METHOD );
	}

	/**
	 * Gets the on-finish script of the group. Presentation phase. The report
	 * item has been read from the report document, but not sent to emitter yet.
	 * 
	 * @return the on-finish script of the group
	 */

	public String getOnCreate( )
	{
		return getStringProperty( ICellModel.ON_CREATE_METHOD );
	}

	/**
	 * Gets the on-finish script of the group. Presentation phase. The report
	 * item has been read from the report document, but not sent to emitter yet.
	 * 
	 * @return the on-finish script of the group
	 */

	public String getOnRender( )
	{
		return getStringProperty( ICellModel.ON_RENDER_METHOD );
	}

	/**
	 * Sets the on-prepare script of the group element.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if the method is locked.
	 * 
	 * @see #getOnPrepare()
	 */

	public void setOnPrepare( String script ) throws SemanticException
	{
		setProperty( ICellModel.ON_PREPARE_METHOD, script );
	}

	/**
	 * Sets the on-create script of the group element.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if the method is locked.
	 * 
	 * @see #getOnCreate()
	 * 
	 */

	public void setOnCreate( String script ) throws SemanticException
	{
		setProperty( ICellModel.ON_CREATE_METHOD, script );
	}

	/**
	 * Sets the on-render script of the group element.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if the method is locked.
	 * 
	 * @see #getOnRender()
	 */

	public void setOnRender( String script ) throws SemanticException
	{
		setProperty( ICellModel.ON_RENDER_METHOD, script );
	}

	/**
	 * Sets the number of the diagonal lines that are from top-left to
	 * bottom-right corners.
	 * 
	 * @param diagonalNumber
	 *            the diagonal number
	 * @throws SemanticException
	 */
	public void setDiagonalNumber( int diagonalNumber )
			throws SemanticException
	{
		setIntProperty( ICellModel.DIAGONAL_NUMBER_PROP, diagonalNumber );
	}

	/**
	 * Gets the number of the diagonal lines that are from top-left to
	 * bottom-right corners.
	 * 
	 * @return the diagonal number.
	 */
	public int getDiagonalNumber( )
	{
		return getIntProperty( ICellModel.DIAGONAL_NUMBER_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the diagonal thickness. Besides the
	 * dimension value, the dimension handle may return one of constants defined
	 * in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li>
	 * <code>LINE_WIDTH_THICK</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the diagonal thickness
	 */
	public DimensionHandle getDiagonalThickness( )
	{
		return getDimensionProperty( ICellModel.DIAGONAL_THICKNESS_PROP );
	}

	/**
	 * Sets the style of the diagonal that is from top-left to bottom-right. The
	 * input value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li>
	 * <code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li>
	 * <code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li>
	 * <code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li>
	 * <code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param lineStyle
	 *            the line style.
	 * @throws SemanticException
	 *             if the input value is not one of the above values.
	 */
	public void setDiagonalStyle( String lineStyle ) throws SemanticException
	{
		setStringProperty( ICellModel.DIAGONAL_STYLE_PROP, lineStyle );
	}

	/**
	 * Returns the style of the diagonal that is from top-left to bottom-right.
	 * The return value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li>
	 * <code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li>
	 * <code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li>
	 * <code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li>
	 * <code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the diagonal style.
	 */
	public String getDiagonalStyle( )
	{
		return getStringProperty( ICellModel.DIAGONAL_STYLE_PROP );
	}

	/**
	 * Sets the number of the anti-diagonal lines that are from the top-right to
	 * bottom-left.
	 * 
	 * @param antidiagonalNumber
	 *            the anti-diagonal number
	 * @throws SemanticException
	 */
	public void setAntidiagonalNumber( int antidiagonalNumber )
			throws SemanticException
	{
		setIntProperty( ICellModel.ANTIDIAGONAL_NUMBER_PROP, antidiagonalNumber );
	}

	/**
	 * Gets the number of the anti-diagonal lines that are from the top-right to
	 * bottom-left.
	 * 
	 * @return the anti-diagonal number.
	 */
	public int getAntidiagonalNumber( )
	{
		return getIntProperty( ICellModel.ANTIDIAGONAL_NUMBER_PROP );
	}

	/**
	 * Gets a dimension handle to deal with the anti-diagonal thickness. Besides
	 * the dimension value, the dimension handle may return one of constants
	 * defined in <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li>
	 * <code>LINE_WIDTH_THICK</code>
	 * </ul>
	 * 
	 * @return a DimensionHandle for the anti-diagonal thickness
	 */
	public DimensionHandle getAntidiagonalThickness( )
	{
		return getDimensionProperty( ICellModel.ANTIDIAGONAL_THICKNESS_PROP );
	}

	/**
	 * Returns the style of the anti-diagonal that is from top-right to
	 * bottom-left corner. The return value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li>
	 * <code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li>
	 * <code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li>
	 * <code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li>
	 * <code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the anti-diagonal style.
	 */
	public String getAntidiagonalStyle( )
	{
		return getStringProperty( ICellModel.ANTIDIAGONAL_STYLE_PROP );
	}

	/**
	 * Sets the style of the anti-diagonal that is from top-right to bottom-left
	 * corner. The input value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li>
	 * <code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li>
	 * <code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li>
	 * <code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li>
	 * <code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param antidiagonalStyle
	 *            the anti-diagonal style.
	 * @throws SemanticException
	 *             if the input value is not one of the above values.
	 */
	public void setAntidiagonalStyle( String antidiagonalStyle )
			throws SemanticException
	{
		setStringProperty( ICellModel.ANTIDIAGONAL_STYLE_PROP,
				antidiagonalStyle );
	}

}