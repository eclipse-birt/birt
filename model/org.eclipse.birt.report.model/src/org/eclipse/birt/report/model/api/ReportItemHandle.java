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
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.eclipse.birt.report.model.util.UnusedBoundColumnsMgr;

/**
 * Represents a report item: any element that can appear within a section of the
 * report. Report items have a size and position that are used in some
 * containers. Report items also have a style. Report items can references to
 * the data set to use for itself. Many report items can be the target of
 * hyperlinks. The bookmark property identifies the item location. It also has a
 * set of visibility rules that say when a report item should be hidden. The
 * bindings allow a report item to pass data into its data source. Call
 * {@link DesignElementHandle#getPrivateStyle}( ) to get a handle with
 * getter/setter methods for the style properties.
 * 
 * @see org.eclipse.birt.report.model.elements.ReportItem
 */

public abstract class ReportItemHandle extends ReportElementHandle
		implements
			IReportItemModel,
			IStyledElementModel
{

	/**
	 * Constructs the handle for a report item with the given design and
	 * element. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public ReportItemHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the data set of the report item.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet( )
	{
		DesignElement dataSet = ( (ReportItem) getElement( ) )
				.getDataSetElement( module );
		if ( dataSet == null )
			return null;

		assert dataSet instanceof DataSet;

		return (DataSetHandle) dataSet.getHandle( dataSet.getRoot( ) );
	}

	/**
	 * Sets the data set of the report item.
	 * 
	 * @param handle
	 *            the handle of the data set
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setDataSet( DataSetHandle handle ) throws SemanticException
	{
		if ( handle == null )
			setStringProperty( DATA_SET_PROP, null );
		else
		{
			ModuleHandle moduleHandle = handle.getRoot( );
			String valueToSet = handle.getName( );
			if ( moduleHandle instanceof LibraryHandle )
			{
				String namespace = ( (LibraryHandle) moduleHandle )
						.getNamespace( );
				valueToSet = StringUtil.buildQualifiedReference( namespace,
						handle.getName( ) );
			}
			setStringProperty( IReportItemModel.DATA_SET_PROP, valueToSet );
		}
	}

	/**
	 * Gets a handle to deal with the item's x (horizontal) position.
	 * 
	 * @return a DimensionHandle for the item's x position.
	 */

	public DimensionHandle getX( )
	{
		return super.getDimensionProperty( IReportItemModel.X_PROP );
	}

	/**
	 * Gets a handle to deal with the item's y (vertical) position.
	 * 
	 * @return a DimensionHandle for the item's y position.
	 */

	public DimensionHandle getY( )
	{
		return super.getDimensionProperty( IReportItemModel.Y_PROP );
	}

	/**
	 * Sets the item's x position using a dimension string with optional unit
	 * suffix such as "10" or "10pt". If no suffix is provided, then the units
	 * are assumed to be in the design's default units. Call this method to set
	 * a string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	public void setX( String dimension ) throws SemanticException
	{
		setProperty( IReportItemModel.X_PROP, dimension );
	}

	/**
	 * Sets the item's x position to a value in default units. The default unit
	 * may be defined by the property in BIRT or the application unit defined in
	 * the design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setX( double dimension ) throws SemanticException
	{
		setFloatProperty( IReportItemModel.X_PROP, dimension );
	}

	/**
	 * Sets the item's y position using a dimension string with optional unit
	 * suffix such as "10" or "10pt". If no suffix is provided, then the units
	 * are assumed to be in the design's default units. Call this method to set
	 * a string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	public void setY( String dimension ) throws SemanticException
	{
		setProperty( IReportItemModel.Y_PROP, dimension );
	}

	/**
	 * Sets the item's y position to a value in default units. The default unit
	 * may be defined by the property in BIRT or the application unit defined in
	 * the design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setY( double dimension ) throws SemanticException
	{
		setFloatProperty( IReportItemModel.Y_PROP, dimension );
	}

	/**
	 * Sets the item's height using a dimension string with optional unit suffix
	 * such as "10" or "10pt". If no suffix is provided, then the units are
	 * assumed to be in the design's default units. Call this method to set a
	 * string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	public void setHeight( String dimension ) throws SemanticException
	{
		setProperty( IReportItemModel.HEIGHT_PROP, dimension );
	}

	/**
	 * Sets the item's height to a value in default units. The default unit may
	 * be defined by the property in BIRT or the application unit defined in the
	 * design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setHeight( double dimension ) throws SemanticException
	{
		setFloatProperty( IReportItemModel.HEIGHT_PROP, dimension );
	}

	/**
	 * Sets the item's width using a dimension string with optional unit suffix
	 * such as "10" or "10pt". If no suffix is provided, then the units are
	 * assumed to be in the design's default units. Call this method to set a
	 * string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	public void setWidth( String dimension ) throws SemanticException
	{
		setProperty( IReportItemModel.WIDTH_PROP, dimension );
	}

	/**
	 * Sets the item's width to a value in default units. The default unit may
	 * be defined by the property in BIRT or the application unit defined in the
	 * design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setWidth( double dimension ) throws SemanticException
	{
		setFloatProperty( IReportItemModel.WIDTH_PROP, dimension );
	}

	/**
	 * Gets a handle to deal with the item's width.
	 * 
	 * @return a DimensionHandle for the item's width.
	 */

	public DimensionHandle getWidth( )
	{
		return super.getDimensionProperty( IReportItemModel.WIDTH_PROP );
	}

	/**
	 * Gets a handle to deal with the item's height.
	 * 
	 * @return a DimensionHandle for the item's height.
	 */
	public DimensionHandle getHeight( )
	{
		return super.getDimensionProperty( IReportItemModel.HEIGHT_PROP );
	}

	/**
	 * Returns the bookmark of the report item.
	 * 
	 * @return the book mark as a string
	 */

	public String getBookmark( )
	{
		return getStringProperty( IReportItemModel.BOOKMARK_PROP );
	}

	/**
	 * Sets the bookmark of the report item.
	 * 
	 * @param value
	 *            the property value to be set.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setBookmark( String value ) throws SemanticException
	{
		setStringProperty( IReportItemModel.BOOKMARK_PROP, value );
	}

	/**
	 * Returns visibility rules defined on the report item. The element in the
	 * iterator is the corresponding <code>StructureHandle</code> that deal
	 * with a <code>Hide</code> in the list.
	 * 
	 * @return the iterator for visibility rules defined on this report item.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.HideRule
	 */

	public Iterator visibilityRulesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( IReportItemModel.VISIBILITY_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns the script executed when the element is created in the Factory.
	 * Called after the item is created, but before the item is saved to the
	 * report document file.
	 * 
	 * @return the script that executes
	 */

	public String getOnCreate( )
	{
		return getStringProperty( IReportItemModel.ON_CREATE_METHOD );
	}

	/**
	 * Sets the script executed when the element is created in the Factory.
	 * Called after the item is created, but before the item is saved to the
	 * report document file.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setOnCreate( String value ) throws SemanticException
	{
		setProperty( IReportItemModel.ON_CREATE_METHOD, value );
	}

	/**
	 * Returns the script executed when the element is prepared for rendering in
	 * the Presentation engine.
	 * 
	 * @return the script that executes
	 */

	public String getOnRender( )
	{
		return getStringProperty( IReportItemModel.ON_RENDER_METHOD );
	}

	/**
	 * Sets the script executed when the element is prepared for rendering in
	 * the Presentation engine.
	 * 
	 * @param value
	 *            the script to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setOnRender( String value ) throws SemanticException
	{
		setProperty( IReportItemModel.ON_RENDER_METHOD, value );
	}

	/**
	 * Returns the iterator for parameter binding list defined on this report
	 * item. The element in the iterator is the corresponding
	 * <code>StructureHandle</code> that deal with a <code>ParamBinding</code>
	 * in the list.
	 * 
	 * @return the iterator for parameter binding structure list defined on this
	 *         data set.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ParamBinding
	 */

	public Iterator paramBindingsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( IReportItemModel.PARAM_BINDINGS_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Sets a table of contents entry for this item. The TOC property defines an
	 * expression that returns a string that is to appear in the Table of
	 * Contents for this item or its container.
	 * 
	 * @param expression
	 *            the expression that returns a string
	 * @throws SemanticException
	 *             if the TOC property is locked by the property mask.
	 * 
	 * @see #getTocExpression()
	 * @deprecated
	 */

	public void setTocExpression( String expression ) throws SemanticException
	{
		if ( StringUtil.isEmpty( expression ) )
		{
			setProperty( IReportItemModel.TOC_PROP, null );
			return;
		}
		TOCHandle tocHandle = getTOC( );
		if ( StringUtil.isBlank( expression ) )
			return;
		if ( tocHandle == null )
		{
			TOC toc = StructureFactory.createTOC( expression );
			addTOC( toc );
		}
		else
		{
			tocHandle.setExpression( expression );
		}
	
	}

	/**
	 * Returns the expression evalueated as a table of contents entry for this
	 * item.
	 * 
	 * @return the expression evaluated as a table of contents entry for this
	 *         item
	 * @see #setTocExpression(String)
	 * @deprecated
	 */

	public String getTocExpression( )
	{
		TOCHandle tocHandle = getTOC( );
		if ( tocHandle == null )
			return null;
		return tocHandle.getExpression( );
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
		return getStringProperty( IReportItemModel.ON_PREPARE_METHOD );
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
		setProperty( IReportItemModel.ON_PREPARE_METHOD, script );
	}

	/**
	 * Gets the on-pageBreak script of the report item. Presentation phase. It
	 * is for a script executed when the element is prepared for page breaking
	 * in the Presentation engine.
	 * 
	 * @return the on-pageBreak script of the report item
	 * 
	 */

	public String getOnPageBreak( )
	{
		return getStringProperty( ON_PAGE_BREAK_METHOD );
	}

	/**
	 * Sets the on-pageBreak script of the report item.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if the method is locked.
	 * 
	 * @see #getOnPageBreak()
	 */

	public void setOnPageBreak( String script ) throws SemanticException
	{
		setProperty( ON_PAGE_BREAK_METHOD, script );
	}

	/**
	 * Returns the bound columns that binds the data set columns. The item in
	 * the iterator is the corresponding <code>ComputedColumnHandle</code>.
	 * 
	 * @return a list containing the bound columns.
	 */

	public Iterator columnBindingsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( BOUND_DATA_COLUMNS_PROP );
		return propHandle.iterator( );
	}

	/**
	 * Get a handle to deal with the bound column.
	 * 
	 * @return a handle to deal with the boudn data column.
	 */

	public PropertyHandle getColumnBindings( )
	{
		return getPropertyHandle( BOUND_DATA_COLUMNS_PROP );
	}

	/**
	 * Adds a bound column to the list.
	 * 
	 * @param addColumn
	 *            the bound column to add
	 * @param inForce
	 *            <code>true</code> the column is added to the list regardless
	 *            of duplicate expression. <code>false</code> do not add the
	 *            column if the expression already exist
	 * @param column
	 *            the bound column
	 * @return the newly created <code>ComputedColumnHandle</code> or the
	 *         existed <code>ComputedColumnHandle</code> in the list
	 * @throws SemanticException
	 *             if expression is not duplicate but the name duplicates the
	 *             exsiting bound column. Or, if the both name/expression are
	 *             duplicate, but <code>inForce</code> is <code>true</code>.
	 */

	public ComputedColumnHandle addColumnBinding( ComputedColumn addColumn,
			boolean inForce ) throws SemanticException
	{
		if ( addColumn == null )
			return null;

		String expr = addColumn.getExpression( );
		if ( expr == null )
			return null;

		List columns = (List) getProperty( BOUND_DATA_COLUMNS_PROP );
		if ( columns == null )
			return (ComputedColumnHandle) getPropertyHandle(
					BOUND_DATA_COLUMNS_PROP ).addItem( addColumn );
		
		String aggregateOn = addColumn.getAggregateOn( );
		ComputedColumn column = DataBoundColumnUtil.getColumn( columns, expr , aggregateOn );

		if ( column != null && !inForce )
		{
			return (ComputedColumnHandle) column.handle(
					getPropertyHandle( BOUND_DATA_COLUMNS_PROP ), columns
							.indexOf( column ) );
		}
		return (ComputedColumnHandle) getPropertyHandle(
				BOUND_DATA_COLUMNS_PROP ).addItem( addColumn );
	}

	/**
	 * Removes unused bound columns from the element. Bound columns of nested
	 * elements will not be removed. For example, if calls this method for a
	 * list thaat contains a text-data, unused bound columns on list are
	 * removed. While, unused columns on text-data still are kept.
	 * 
	 * @throws SemanticException
	 *             if bound column property is locked.
	 */

	public void removedUnusedColumnBindings( ) throws SemanticException
	{
		UnusedBoundColumnsMgr.removedUnusedBoundColumns( this );
	}

	/**
	 * Gets TOC handle.
	 * 
	 * @return toc handle
	 */

	public TOCHandle getTOC( )
	{
		PropertyHandle propHandle = getPropertyHandle( IReportItemModel.TOC_PROP );
		TOC toc = (TOC) propHandle.getValue( );

		if ( toc == null )
			return null;

		return (TOCHandle) toc.getHandle( propHandle );
	}

	/**
	 * Adds toc structure.
	 * 
	 * @param expression
	 *            toc expression
	 * @return toc handle
	 * @throws SemanticException
	 */

	public TOCHandle addTOC( String expression ) throws SemanticException
	{
		if ( StringUtil.isEmpty( expression ) )
			return null;

		TOC toc = StructureFactory.createTOC( expression );
		setProperty( IReportItemModel.TOC_PROP, toc );

		return (TOCHandle) toc
				.getHandle( getPropertyHandle( IReportItemModel.TOC_PROP ) );
	}

	/**
	 * Adds toc structure.
	 * 
	 * @param toc
	 *            toc structure
	 * @return toc handle
	 * @throws SemanticException
	 */

	public TOCHandle addTOC( TOC toc ) throws SemanticException
	{
		setProperty( IReportItemModel.TOC_PROP, toc );

		if ( toc == null )
			return null;
		return (TOCHandle) toc
				.getHandle( getPropertyHandle( IReportItemModel.TOC_PROP ) );
	}


	/**
	 * Gets the item's z position as an integer.
	 * 
	 * @return the z depth. Start from 0
	 */

	public int getZIndex( )
	{
		return super.getIntProperty( IReportItemModel.Z_INDEX_PROP );
	}

	/**
	 * Sets the item's z position to an integer.
	 * 
	 * @param zIndex
	 *            the z depth. Start from 0
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setZIndex( int zIndex ) throws SemanticException
	{
		setIntProperty( IReportItemModel.Z_INDEX_PROP, zIndex );
	}
}