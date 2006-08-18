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
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.writer.LibraryWriter;
import org.eclipse.birt.report.model.writer.ModuleWriter;

/**
 * Represents the library module. The library is the container of reusable
 * report items , data sources, styles and so on. One library has its own
 * namespace, which is used to identify which library the element reference
 * refers.
 */

public class Library extends Module implements ILibraryModel
{

	/**
	 * Namespace of the library.
	 */

	private String namespace;

	/**
	 * The host module which includes this module.
	 */

	protected Module host = null;

	/**
	 * Constructor for loading library from design file.
	 * 
	 * @param theSession
	 *            the session in which this library is involved
	 * @param host
	 *            the host module which includes this library
	 */

	public Library( DesignSession theSession, Module host )
	{
		super( theSession );
		this.host = host;
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
		this( theSession, null );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getWriter()
	 */

	public ModuleWriter getWriter( )
	{
		return new LibraryWriter( this );
	}

	/**
	 * Returns the host module. If this module is not included by any module,
	 * return null.
	 * 
	 * @return the host module.
	 */

	public Module getHost( )
	{
		return host;
	}

	/**
	 * Sets the host module.
	 * 
	 * @param theHost
	 *            the host module to set
	 */

	public void setHost( Module theHost )
	{
		this.host = theHost;
	}

	/**
	 * Returns whether the library with the given namespace can be included in
	 * this module.
	 * 
	 * @param namespace
	 *            the library namespace
	 * @return true, if the library with the given namespace can be included.
	 */

	public boolean isRecursiveNamespace( String namespace )
	{
		Module module = this;
		while ( module instanceof Library )
		{
			Library library = (Library) module;

			if ( namespace.equals( library.getNamespace( ) ) )
				return true;

			module = library.getHost( );
		}

		return false;
	}

	/**
	 * Returns whether the library with the given url can be included in this
	 * module.
	 * 
	 * @param fileName
	 *            the library file url
	 * @return true, if the library with the given url can be included.
	 */

	public boolean isRecursiveFile( String fileName )
	{
		Module module = this;
		while ( module instanceof Library )
		{
			Library library = (Library) module;

			if ( fileName.equals( library.getLocation( ) ) )
				return true;

			module = library.getHost( );
		}

		return false;
	}

	/**
	 * Finds a theme in this module itself.
	 * 
	 * @param name
	 *            Name of the theme to find.
	 * @return The style, or null if the theme is not found.
	 */

	public Theme findNativeTheme( String name )
	{
		return (Theme) nameSpaces[THEME_NAME_SPACE].getElement( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getNameForDisplayLabel()
	 */

	protected String getNameForDisplayLabel( )
	{
		return namespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Module#getOptions()
	 */

	public ModuleOption getOptions( )
	{
		if ( options != null )
			return this.options;

		Module hostModule = this.host;
		while ( hostModule != null )
		{
			ModuleOption hostOptions = hostModule.getOptions( );
			if ( hostOptions != null )
				return hostOptions;

			if ( hostModule instanceof Library )
				hostModule = ( (Library) hostModule ).host;

			return null;
		}

		return null;
	}

}