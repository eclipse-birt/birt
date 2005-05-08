/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * base class of all elements in the report design.
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public abstract class ReportElementDesign
{

	/**
	 * handle of this report element
	 */
	DesignElementHandle handle;
	
	/**
	 * ID of the object.
	 */
	int ID;
	/**
	 * name of this element
	 */
	protected String name;
	/**
	 * parent element's name
	 */
	protected String extend;

	/**
	 * class implement this element.
	 */
	protected String javaClass;

	/**
	 * map used to store the custom properties
	 */
	protected Map customProperties = new HashMap();
	
	/**
	 * @return Returns the extend.
	 */
	public String getExtends( )
	{
		return extend;
	}

	/**
	 * @param extend
	 *            The extend to set.
	 */
	public void setExtends( String extend )
	{
		this.extend = extend;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName( )
	{
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return Returns the iD.
	 */
	public int getID( )
	{
		return ID;
	}

	/**
	 * @param id
	 *            The iD to set.
	 */
	public void setID( int id )
	{
		ID = id;
	}
	/**
	 * @return Returns the javaClass.
	 */
	public String getJavaClass( )
	{
		return javaClass;
	}
	/**
	 * @param javaClass The javaClass to set.
	 */
	public void setJavaClass( String javaClass )
	{
		this.javaClass = javaClass;
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.analyzer.IDataSource#getCustomProperties()
	 */
	public Map getCustomProperties( )
	{
		return customProperties;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.analyzer.IDataSource#getCustomPropertyValue(java.lang.String)
	 */
	public String getCustomPropertyValue( String name )
	{
		return (String) customProperties.get( name );
	}
	/**
	 * @return Returns the handle.
	 */
	public DesignElementHandle getHandle( )
	{
		return handle;
	}
	/**
	 * @param handle The handle to set.
	 */
	public void setHandle( DesignElementHandle handle )
	{
		this.handle = handle;
	}
}
