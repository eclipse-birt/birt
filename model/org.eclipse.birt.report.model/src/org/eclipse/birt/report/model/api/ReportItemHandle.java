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
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;

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
			IReportItemModel, IStyledElementModel
{

	/**
	 * Constructs the handle for a report item with the given design and
	 * element. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public ReportItemHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns the data set of the report item.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet( )
	{
		DesignElement dataSet = ( (ReportItem) getElement( ) )
				.getDataSetElement( design );
		if ( dataSet == null )
			return null;

		assert dataSet instanceof DataSet;

		return (DataSetHandle) dataSet.getHandle( design );
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

		setStringProperty( ReportItem.DATA_SET_PROP, handle != null ? handle
				.getName( ) : null );
	}

	/**
	 * Gets a handle to deal with the item's x (horizontal) position.
	 * 
	 * @return a DimensionHandle for the item's x position.
	 */

	public DimensionHandle getX( )
	{
		return super.getDimensionProperty( ReportItem.X_PROP );
	}

	/**
	 * Gets a handle to deal with the item's y (vertical) position.
	 * 
	 * @return a DimensionHandle for the item's y position.
	 */

	public DimensionHandle getY( )
	{
		return super.getDimensionProperty( ReportItem.Y_PROP );
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
		setProperty( ReportItem.X_PROP, dimension );
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
		setFloatProperty( ReportItem.X_PROP, dimension );
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
		setProperty( ReportItem.Y_PROP, dimension );
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
		setFloatProperty( ReportItem.Y_PROP, dimension );
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
		setProperty( ReportItem.HEIGHT_PROP, dimension );
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
		setFloatProperty( ReportItem.HEIGHT_PROP, dimension );
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
		setProperty( ReportItem.WIDTH_PROP, dimension );
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
		setFloatProperty( ReportItem.WIDTH_PROP, dimension );
	}

	/**
	 * Gets a handle to deal with the item's width.
	 * 
	 * @return a DimensionHandle for the item's width.
	 */

	public DimensionHandle getWidth( )
	{
		return super.getDimensionProperty( ReportItem.WIDTH_PROP );
	}

	/**
	 * Gets a handle to deal with the item's height.
	 * 
	 * @return a DimensionHandle for the item's height.
	 */
	public DimensionHandle getHeight( )
	{
		return super.getDimensionProperty( ReportItem.HEIGHT_PROP );
	}

	/**
	 * Returns the bookmark of the report item.
	 * 
	 * @return the book mark as a string
	 */

	public String getBookmark( )
	{
		return getStringProperty( ReportItem.BOOKMARK_PROP );
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
		setStringProperty( ReportItem.BOOKMARK_PROP, value );
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
		PropertyHandle propHandle = getPropertyHandle( ReportItem.VISIBILITY_PROP );
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
		return getStringProperty( ReportItem.ON_CREATE_METHOD );
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
		setProperty( ReportItem.ON_CREATE_METHOD, value );
	}

	/**
	 * Returns the script executed when the element is prepared for rendering in
	 * the Presentation engine.
	 * 
	 * @return the script that executes
	 */

	public String getOnRender( )
	{
		return getStringProperty( ReportItem.ON_RENDER_METHOD );
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
		setProperty( ReportItem.ON_RENDER_METHOD, value );
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
		PropertyHandle propHandle = getPropertyHandle( ReportItem.PARAM_BINDINGS_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}
}