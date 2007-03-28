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

package org.eclipse.birt.report.model.api.extension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

/**
 * Extension adapter class for the IReportItem. By default, the report item will
 * have no model. Therefore, it has no model properties and all the related
 * method will do nothing or return null.
 */

public class ReportItem implements IReportItem, Cloneable
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItem#serialize(java.lang.String)
	 */

	public ByteArrayOutputStream serialize( String propName )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItem#deserialize(java.lang.String,
	 *      java.io.ByteArrayInputStream)
	 */

	public void deserialize( String propName, ByteArrayInputStream data )
			throws ExtendedElementException
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItem#getPropertyDefinitions()
	 */
	public IPropertyDefinition[] getPropertyDefinitions( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItem#getProperty(java.lang.String)
	 */
	public Object getProperty( String propName )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItem#checkProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	public void checkProperty( String propName, Object value )
			throws ExtendedElementException
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItem#setProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	public void setProperty( String propName, Object value )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItem#validate()
	 */

	public List validate( )
	{
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItem#copy()
	 */

	public IReportItem copy( )
	{
		try
		{
			return (IReportItem) super.clone( );
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItem#refreshPropertyDefinition()
	 */

	public boolean refreshPropertyDefinition( )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IReportItem#getMethods()
	 */

	public IPropertyDefinition[] getMethods( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IReportItem#getScriptPropertyDefinition()
	 */

	public IPropertyDefinition getScriptPropertyDefinition( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IReportItem#getScriptableElement()
	 */

	public org.eclipse.birt.report.model.api.simpleapi.IReportItem getSimpleElement( )
	{
		return null;
	}

	/**
	 * 
	 */
	public List getPredefinedStyles( )
	{
		return Collections.EMPTY_LIST;
	}

}