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

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.structures.Action;

/**
 * Represents a data item element. A data item has an action, value expression
 * and help text.
 * 
 * @see org.eclipse.birt.report.model.elements.DataItem
 */

public class DataItemHandle extends ReportItemHandle
{

	/**
	 * Constructs a handle of the data item with the given design and a data
	 * item. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public DataItemHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns a handle to work with the action property, action is a structure
	 * that defines a hyperlink.
	 * 
	 * @return a handle to the action property, return <code>null</code> if
	 *         the action has not been set on the data item.
	 * @see ActionHandle
	 */

	public ActionHandle getActionHandle( )
	{
		PropertyHandle propHandle = getPropertyHandle( DataItem.ACTION_PROP );
		Action action = (Action) propHandle.getValue( );

		if ( action == null )
			return null;

		return (ActionHandle) action.getHandle( propHandle );
	}

	/**
	 * Set an action on the image.
	 * 
	 * @param action
	 *            new action to be set on the image, it represents a bookmark
	 *            link, hyperlink, and drill through etc.
	 * @return a handle to the action property, return <code>null</code> if
	 *         the action has not been set on the image.
	 * 
	 * @throws SemanticException
	 *             if member of the action is not valid.
	 */

	public ActionHandle setAction( Action action ) throws SemanticException
	{
		setProperty( DataItem.ACTION_PROP, action );
        
        if( action == null )
            return null;
		return (ActionHandle) action
				.getHandle( getPropertyHandle( DataItem.ACTION_PROP ) );
	}

	/**
	 * Returns the value of the distinct property. The return value is defined
	 * in <code>DesignChoiceConstants</code> and is one of these:
	 * 
	 * <ul>
	 * <li>DISTINCT_ALL</li>
	 * <li>DISTINCT_REPEAT</li>
	 * <li>DISTINCT_REPEAT_ON_PAGE</li>
	 * </ul>
	 * 
	 * @return the distinct value as a string
	 */

	public String getDistinct( )
	{
		return getStringProperty( DataItem.DISTINCT_PROP );
	}

	/**
	 * Sets the value of the distinct property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and is one of these:
	 * 
	 * <ul>
	 * <li>DISTINCT_ALL</li>
	 * <li>DISTINCT_REPEAT</li>
	 * <li>DISTINCT_REPEAT_ON_PAGE</li>
	 * </ul>
	 * 
	 * @param distinct
	 *            the distinct value as a string
	 * 
	 * @throws SemanticException
	 *             If the property is locked or the value is not one of the
	 *             above.
	 */

	public void setDistinct( String distinct ) throws SemanticException
	{
		setProperty( DataItem.DISTINCT_PROP, distinct );
	}

	/**
	 * Returns the value of the distinct-reset property.
	 * 
	 * @return the distinct-set value as a string
	 */

	public String getDistinctReset( )
	{
		return getStringProperty( DataItem.DISTINCT_RESET_PROP );
	}

	/**
	 * Returns the value of the distinct-reset property.
	 * 
	 * @param value
	 *            the distinct-set value as a string
	 * @throws SemanticException
	 *             If the property is locked.
	 */

	public void setDistinctReset( String value ) throws SemanticException
	{
		setProperty( DataItem.DISTINCT_RESET_PROP, value );
	}

	/**
	 * Returns the expression that gives the value that the data item displays.
	 * 
	 * @return the value expression
	 */

	public String getValueExpr( )
	{
		return getStringProperty( DataItem.VALUE_EXPR_PROP );
	}

	/**
	 * Sets the expression for the value that the data item is to display.
	 * 
	 * @param expr
	 *            the expression to set
	 * @throws SemanticException
	 *             If the property is locked.
	 */

	public void setValueExpr( String expr ) throws SemanticException
	{
		setProperty( DataItem.VALUE_EXPR_PROP, expr );
	}

	/**
	 * Returns the help text of this data item.
	 * 
	 * @return the help text
	 */

	public String getHelpText( )
	{
		return getStringProperty( DataItem.HELP_TEXT_PROP );
	}

	/**
	 * Sets the help text of this data item.
	 * 
	 * @param value
	 *            the help text
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setHelpText( String value ) throws SemanticException
	{
		setStringProperty( DataItem.HELP_TEXT_PROP, value );
	}

	/**
	 * Returns the help text resource key of this data item.
	 * 
	 * @return the help text key
	 */

	public String getHelpTextKey( )
	{
		return getStringProperty( DataItem.HELP_TEXT_KEY_PROP );
	}

	/**
	 * Sets the resource key of the help text of this data item.
	 * 
	 * @param value
	 *            the resource key of the help text
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setHelpTextKey( String value ) throws SemanticException
	{
		setStringProperty( DataItem.HELP_TEXT_KEY_PROP, value );
	}

}