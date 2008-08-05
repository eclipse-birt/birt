/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.mock;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Mock a ServletContext class for Viewer UnitTest
 * 
 */
public class ServletContextSimulator implements ServletContext
{

	/**
	 * Init parameters of Servlet Context
	 */
	private Hashtable initParameters;

	/**
	 * Attributes of Servlet Context
	 */
	private Hashtable attributes;

	/**
	 * The defined root context directory
	 */
	private File contextDir;

	/**
	 * Request Dispatcher Object
	 */
	private RequestDispatcher dispatcher;

	/**
	 * Constructor
	 * 
	 */
	public ServletContextSimulator( )
	{
		this.initParameters = new Hashtable( );
		this.attributes = new Hashtable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
	 */
	public Object getAttribute( String name )
	{
		return this.attributes.get( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getAttributeNames()
	 */
	public Enumeration getAttributeNames( )
	{
		return this.attributes.keys( );
	}

	public void setAttribute( String name, Object value )
	{
		this.attributes.put( name, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
	 */
	public void removeAttribute( String name )
	{
		this.attributes.remove( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getContext(java.lang.String)
	 */
	public ServletContext getContext( String uri )
	{
		throw new UnsupportedOperationException(
				"Do not support getContext operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
	 */
	public String getInitParameter( String name )
	{
		if ( name == null )
			return null;

		Object param = this.initParameters.get( name );
		if ( param != null )
			return (String) param;

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getInitParameterNames()
	 */
	public Enumeration getInitParameterNames( )
	{
		return this.initParameters.keys( );
	}

	/**
	 * Set init parameter
	 * 
	 * @param name
	 * @param value
	 */
	public void setInitParameter( String name, String value )
	{
		this.initParameters.put( name, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getMajorVersion()
	 */
	public int getMajorVersion( )
	{
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
	 */
	public String getMimeType( String arg0 )
	{
		throw new UnsupportedOperationException(
				"Do not support getMimeType operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getMinorVersion()
	 */
	public int getMinorVersion( )
	{
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
	 */
	public RequestDispatcher getNamedDispatcher( String uri )
	{
		throw new UnsupportedOperationException(
				"Do not support getNamedDispatcher operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
	 */
	public String getRealPath( String path )
	{
		if ( contextDir == null || path == null )
			return null;

		return new File( contextDir, path ).getAbsolutePath( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher( String uri )
	{
		return this.dispatcher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getResource(java.lang.String)
	 */
	public URL getResource( String path ) throws MalformedURLException
	{
		throw new UnsupportedOperationException(
				"Do not support getResource operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
	 */
	public InputStream getResourceAsStream( String arg0 )
	{
		throw new UnsupportedOperationException(
				"Do not support getResourceAsStream operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
	 */
	public Set getResourcePaths( String arg0 )
	{
		throw new UnsupportedOperationException(
				"Do not support getResourcePaths operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServerInfo()
	 */
	public String getServerInfo( )
	{
		return "BirtMockServletEngine"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServlet(java.lang.String)
	 */
	public Servlet getServlet( String name ) throws ServletException
	{
		throw new UnsupportedOperationException(
				"Do not support getServlet operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServletContextName()
	 */
	public String getServletContextName( )
	{
		throw new UnsupportedOperationException(
				"Do not support getServletContextName operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServletNames()
	 */
	public Enumeration getServletNames( )
	{
		throw new UnsupportedOperationException(
				"Do not support getServletNames operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServlets()
	 */
	public Enumeration getServlets( )
	{
		throw new UnsupportedOperationException(
				"Do not support getServlets operation!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#log(java.lang.String)
	 */
	public void log( String content )
	{
		System.out.println( content );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#log(java.lang.Exception,
	 *      java.lang.String)
	 */
	public void log( Exception exception, String content )
	{
		System.out.println( content + "--" + exception.getMessage( ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#log(java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void log( String content, Throwable throwable )
	{
		System.out.println( content + "--" + throwable.getMessage( ) ); //$NON-NLS-1$
	}

	/**
	 * @return the contextDir
	 */
	public File getContextDir( )
	{
		return contextDir;
	}

	/**
	 * @param contextDir
	 *            the contextDir to set
	 */
	public void setContextDir( File contextDir )
	{
		this.contextDir = contextDir;
	}

	/**
	 * @return the dispatcher
	 */
	public RequestDispatcher getRequestDispatcher( )
	{
		return dispatcher;
	}

	/**
	 * @param dispatcher
	 *            the dispatcher to set
	 */
	public void setDispatcher( RequestDispatcher dispatcher )
	{
		this.dispatcher = dispatcher;
	}

	/**
	 * Pass a Servlet object to create RequestDispatcher
	 * 
	 * @param servlet
	 */
	public void setDispatcher( Servlet servlet )
	{
		this.dispatcher = new RequestDispatcherSimulator( servlet );
	}
}
