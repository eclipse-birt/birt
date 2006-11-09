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
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;

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
	 * Gets a string that defines the event handle class. 
	 * 
	 * @return the expression as a string
	 * 
	 * @see #setEventHandleClass(String)
	 */

	public String getEventHandleClass( )
	{
		return getStringProperty( IDesignElementModel.EVENT_HANDLER_CLASS_PROP );
	}
	
	/**
	 * Sets the group expression.
	 * 
	 * @param expr
	 *            the expression to set
	 * @throws SemanticException
	 *             If the expression is invalid.
	 * 
	 * @see #getEventHandleClass()
	 */

	public void setEventHandleClass( String expr ) throws SemanticException
	{
		setProperty( IDesignElementModel.EVENT_HANDLER_CLASS_PROP, expr );
	}
}