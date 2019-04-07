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

package org.eclipse.birt.report.engine.extension;

import java.io.InputStream;
import java.util.Locale;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.internal.RowSet;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Implements a default presentation peer that does nothing
 */
public class ReportItemPresentationBase implements IReportItemPresentation
{
	protected IReportItemPresentationInfo info;
	protected ExtendedItemHandle modelHandle;
	protected ClassLoader appClassLoader;
	protected IReportContext context;
	protected int dpi = 72;
	protected String outputFormat;
	protected String supportedImageFormats;
	protected Locale locale;
	protected IDataQueryDefinition[] queries;
	protected IHTMLActionHandler ah = null;
	protected IStyle style = null;
	protected IContent content = null;

	/**
	 * Constructor that does nothing
	 */
	public ReportItemPresentationBase( )
	{
	}

	public void init( IReportItemPresentationInfo info )
	{
		if ( info == null )
		{
			throw new NullPointerException( );
		}

		this.info = info;

		setModelObject( info.getModelObject( ) );
		setApplicationClassLoader( info.getApplicationClassLoader( ) );
		setScriptContext( info.getReportContext( ) );
		setReportQueries( info.getReportQueries( ) );
		setDynamicStyle( info.getExtendedItemContent( ).getComputedStyle( ) );
		setResolution( info.getResolution( ) );
		setLocale( info.getReportContext( ).getLocale( ) );
		setExtendedItemContent( info.getExtendedItemContent( ) );
		setSupportedImageFormats( info.getSupportedImageFormats( ) );
		setActionHandler( info.getActionHandler( ) );
		setOutputFormat( info.getOutputFormat( ) );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getOutputType(java.lang.String,
	 *      java.lang.String)
	 */
	public int getOutputType( )
	{
		return OUTPUT_NONE;
	}

	public Object getOutputContent( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#getSize()
	 */
	public Size getSize( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#finish()
	 */
	public void finish( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject( ExtendedItemHandle modelHandle )
	{
		this.modelHandle = modelHandle;
	}

	public void setApplicationClassLoader( ClassLoader loader )
	{
		this.appClassLoader = loader;
	}
	
	public void setScriptContext( IReportContext context )
	{
		this.context = context;
	}

	public void setReportQueries( IDataQueryDefinition[] queries )
	{
		this.queries = queries;
	}

	public void setLocale( Locale locale )
	{
		this.locale = locale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setResolution(int)
	 */
	public void setResolution( int dpi )
	{
		this.dpi = dpi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setOutputFormat(java.lang.String)
	 */
	public void setOutputFormat( String outputFormat )
	{
		this.outputFormat = outputFormat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setSupportedImageFormats(java.lang.String)
	 */
	public void setSupportedImageFormats( String supportedImageFormats )
	{
		this.supportedImageFormats = supportedImageFormats;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#deserialize(java.io.InputStream)
	 */
	public void deserialize( InputStream istream )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#onRowSets(org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public Object onRowSets( IRowSet[] rowSets ) throws BirtException
	{
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#onRowSets(org.eclipse.birt.data.engine.api.IBaseQueryResults[])
	 */
	public Object onRowSets( IBaseResultSet[] results ) throws BirtException
	{
		if ( results == null )
		{
			return onRowSets( (IRowSet[]) null );
		}

		int length = results.length;

		// test if the IBaseResultSet is a ICubeResultSet
		for ( int i = 0; i < length; i++ )
		{
			if ( results[i].getType( ) == IBaseResultSet.CUBE_RESULTSET )
			{
				return null;
			}
		}

		IRowSet[] rowSets = new IRowSet[length];
		for ( int index = 0; index < length; index++ )
		{
			IQueryResultSet resultSet = (IQueryResultSet) results[index];
			rowSets[index] = new RowSet( resultSet );
		}

		return onRowSets( rowSets );
	}

	/**
	 * @return the image MIME type (e.g. "image/svg+xml")
	 */
	public String getImageMIMEType( )
	{
		return ""; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setActionHandler(org.eclipse.birt.report.engine.api.IHTMLActionHandler)
	 */
	public void setActionHandler(IHTMLActionHandler ah) {
		this.ah = ah;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setDynamicStyle(org.eclipse.birt.report.engine.content.IStyle)
	 */
	public void setDynamicStyle( IStyle style )
	{
		this.style = style;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemPresentation#setExtendedItemContent(org.eclipse.birt.report.engine.content.IContent)
	 */
	public void setExtendedItemContent( IContent content )
	{
		this.content = content;
	}

	public IReportItemPresentationInfo getPresentationConfig( )
	{
		return info;
	}

	/**
	 * Return true by default, the derived class should override this method.
	 */
	public boolean isCacheable( )
	{
		return true;
	}
}
