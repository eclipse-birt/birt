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
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents the "private style" for an element. The private style is the set
 * of style properties set on the element itself, instead of inherited from a
 * shared style.
 */

public class PrivateStyleHandle extends StyleHandle
{

	/**
	 * Constructs the handle for a private style with the given design and
	 * element. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public PrivateStyleHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns a handle to the element that owns this private style.
	 * 
	 * @return a handle to the element that contains this private style
	 */

	public DesignElementHandle getElementHandle( )
	{
		return element.getHandle( design );
	}

	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#addUserPropertyDefn(org.eclipse.birt.report.model.api.core.UserPropertyDefn)
	 */

	public void addUserPropertyDefn( UserPropertyDefn prop )
			throws UserPropertyException
	{
		assert false;
	}

	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called. To change the element that owns this private style, use
	 * <code>getElementHandle</code> to first get a handle to that element.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#dropAndClear()
	 */

	public void dropAndClear( ) throws SemanticException
	{
		assert false;
	}

	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called. To change the element that owns this private style, use
	 * <code>getElementHandle</code> to first get a handle to that element.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#drop()
	 */
	
	public void drop( ) throws SemanticException
	{
		assert false;
	}
	
	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called. To change the element that owns this private style, use
	 * <code>getElementHandle</code> to first get a handle to that element.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#dropUserPropertyDefn(java.lang.String)
	 */

	public void dropUserPropertyDefn( String propName )
			throws UserPropertyException
	{
		assert false;
	}

	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called. To change the element that owns this private style, use
	 * <code>getElementHandle</code> to first get a handle to that element.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#findContentSlot(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */

	public int findContentSlot( DesignElementHandle content )
	{
		assert false;
		return -1;
	}

	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called. To change the element that owns this private style, use
	 * <code>getElementHandle</code> to first get a handle to that element.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getExtends()
	 */

	public DesignElementHandle getExtends( )
	{
		assert false;
		return null;
	}

	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called. To change the element that owns this private style, use
	 * <code>getElementHandle</code> to first get a handle to that element.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#moveTo(org.eclipse.birt.report.model.api.DesignElementHandle,
	 *      int)
	 */

	public void moveTo( DesignElementHandle newContainer, int toSlot )
			throws ContentException
	{
		assert false;
	}

	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called. To change the element that owns this private style, use
	 * <code>getElementHandle</code> to first get a handle to that element.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#setExtends(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */

	public void setExtends( DesignElementHandle parent )
			throws ExtendsException
	{
		assert false;
	}

	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called. To change the element that owns this private style, use
	 * <code>getElementHandle</code> to first get a handle to that element.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#setExtendsElement(org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void setExtendsElement( DesignElement parent )
			throws ExtendsException
	{
		assert false;
	}

	/**
	 * This method is not defined for private styles. It will raise an assertion
	 * if called. To change the element that owns this private style, use
	 * <code>getElementHandle</code> to first get a handle to that element.
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#setExtendsName(java.lang.String)
	 */

	public void setExtendsName( String name ) throws ExtendsException
	{
		assert false;
	}
}