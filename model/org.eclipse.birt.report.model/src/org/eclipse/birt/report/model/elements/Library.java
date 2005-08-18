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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.activity.ReadOnlyActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;

/**
 * Represents the library module. The library is the container of reusable
 * report items , data sources, styles and so on. One library has its own
 * namespace, which is used to identify which library the element reference
 * refers.
 */

public class Library extends Module implements ILibraryModel
{

	private String namespace;

	/**
	 * Default constructor for loading library from design file.
	 */

	public Library( )
	{
		super( null );
		initSlots( );
		onCreate( );
	}

	/**
	 * Constructos for opening library directly.
	 * 
	 * @param theSession
	 *            the session in which this library is involved
	 */

	public Library( DesignSession theSession )
	{
		super( theSession );
		initSlots( );
		onCreate( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitLibrary( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.LIBRARY_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @return an API handle for this element
	 */

	public LibraryHandle handle( )
	{
		if ( handle == null )
		{
			handle = new LibraryHandle( this );
		}
		return (LibraryHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.module#getSlotCount()
	 */
	protected int getSlotCount( )
	{
		return SLOT_COUNT;
	}

	/**
	 * Returns the library namespace.
	 * 
	 * @return the library namespace
	 */

	public String getNamespace( )
	{
		return namespace;
	}

	/**
	 * Sets the library namespace.
	 * 
	 * @param namespace
	 *            The namespace to set.
	 */

	public void setNamespace( String namespace )
	{
		this.namespace = namespace;
	}

	/**
	 * Sets the library is read-only one. That means any operation on it will
	 * throw runtime exception.
	 */

	public void setReadOnly( )
	{
		activityStack = new ReadOnlyActivityStack( );
	}
}