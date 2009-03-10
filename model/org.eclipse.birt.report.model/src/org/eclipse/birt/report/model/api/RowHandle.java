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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;

/**
 * Represents one row in a Grid or Table. Each row contains some number of
 * cells. And one row can define its height.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.TableRow
 */

public class RowHandle extends ReportElementHandle implements ITableRowModel
{

	/**
	 * Constructs the handle for a row with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public RowHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the cell slot of row. Through SlotHandle, each cell can be
	 * obtained.
	 * 
	 * @return the handle to the cell slot
	 * 
	 * @see SlotHandle
	 */

	public SlotHandle getCells( )
	{
		return getSlot( ITableRowModel.CONTENT_SLOT );
	}

	/**
	 * Gets a handle to deal with the row's height.
	 * 
	 * @return a DimensionHandle for the row's height.
	 */

	public DimensionHandle getHeight( )
	{
		return super.getDimensionProperty( ITableRowModel.HEIGHT_PROP );
	}

	/**
	 * Returns the bookmark of this row.
	 * 
	 * @return the bookmark of this row
	 */

	public String getBookmark( )
	{
		return getStringProperty( ITableRowModel.BOOKMARK_PROP );
	}

	/**
	 * Sets the bookmark of this row.
	 * 
	 * @param value
	 *            the bookmark to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setBookmark( String value ) throws SemanticException
	{
		setStringProperty( ITableRowModel.BOOKMARK_PROP, value );
	}

	/**
	 * Returns visibility rules defined on the table row. The element in the
	 * iterator is the corresponding <code>StructureHandle</code> that deal with
	 * a <code>Hide</code> in the list.
	 * 
	 * @return the iterator for visibility rules defined on this row.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.HideRule
	 */

	public Iterator visibilityRulesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ITableRowModel.VISIBILITY_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
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
		return getStringProperty( ITableRowModel.ON_PREPARE_METHOD );
	}

	/**
	 * Gets the on-finish script of the group. Presentation phase. The report
	 * item has been read from the report document, but not sent to emitter yet.
	 * 
	 * @return the on-finish script of the group
	 */

	public String getOnCreate( )
	{
		return getStringProperty( ITableRowModel.ON_CREATE_METHOD );
	}

	/**
	 * Gets the on-finish script of the group. Presentation phase. The report
	 * item has been read from the report document, but not sent to emitter yet.
	 * 
	 * @return the on-finish script of the group
	 */

	public String getOnRender( )
	{
		return getStringProperty( ITableRowModel.ON_RENDER_METHOD );
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
		setProperty( ITableRowModel.ON_PREPARE_METHOD, script );
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
		setProperty( ITableRowModel.ON_CREATE_METHOD, script );
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
		setProperty( ITableRowModel.ON_RENDER_METHOD, script );
	}

	/**
	 * Tests whether to suppress duplicates rows with same content.
	 * 
	 * @return <code>true</code> if suppress duplicates rows with same content,
	 *         otherwise <code>false</code>.
	 * 
	 * @see #setSuppressDuplicates(boolean)
	 */

	public boolean suppressDuplicates( )
	{
		return getBooleanProperty( SUPPRESS_DUPLICATES_PROP );
	}

	/**
	 * Sets the flag whether to suppress duplicates rows with same content.
	 * 
	 * @param value
	 *            <code>true</code> if suppress duplicates rows with same
	 *            content, otherwise <code>false</code>.
	 * @throws SemanticException
	 *             If the expression is invalid.
	 * 
	 * @see #suppressDuplicates()
	 */

	public void setSuppressDuplicates( boolean value ) throws SemanticException
	{
		setBooleanProperty( SUPPRESS_DUPLICATES_PROP, value );
	}
}