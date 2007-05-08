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

package org.eclipse.birt.report.model.core.namespace;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * 
 */
public class DummyNameContext implements INameContext
{

	/**
	 * 
	 */
	public boolean canContain( String elementName )
	{
		return false;
	}

	/**
	 * 
	 */
	public List getElements( int level )
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * 
	 */
	public ElementRefValue resolve( String elementName, PropertyDefn propDefn )
	{
		return null;
	}

	/**
	 * 
	 */
	public ElementRefValue resolve( DesignElement element, PropertyDefn propDefn )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#getNameSpace()
	 */
	public NameSpace getNameSpace( )
	{
		return new NameSpace();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#findElement(java.lang.String, org.eclipse.birt.report.model.api.metadata.IElementDefn)
	 */
	public DesignElement findElement( String elementName,
			IElementDefn elementDefn )
	{
		return null;
	}

	public DesignElement getElement( )
	{
		return null;
	}

	public int getNameSpaceID( )
	{
		return -1;
	}

}
