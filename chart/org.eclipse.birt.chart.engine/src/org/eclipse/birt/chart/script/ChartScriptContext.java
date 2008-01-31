/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.impl.SerializerImpl;

import com.ibm.icu.util.ULocale;

/**
 * An internal implementation for IChartScriptContext
 */
public class ChartScriptContext implements IChartScriptContext
{

	private static final long serialVersionUID = 1L;

	private transient IExternalContext externalContext;

	private ULocale locale;

	private transient Chart cm;

	private transient ILogger logger;

	/**
	 * The constructor.
	 */
	public ChartScriptContext( )
	{
		super( );
	}

	private void writeObject( java.io.ObjectOutputStream out )
			throws IOException
	{
		out.defaultWriteObject( );

		ByteArrayOutputStream bao = null;

		try
		{
			bao = SerializerImpl.instance( ).asXml( cm, true );
		}
		catch ( Exception e )
		{
			if ( logger != null )
			{
				logger.log( e );
			}
			bao = new ByteArrayOutputStream( );
		}

		out.writeObject( bao.toByteArray( ) );
	}

	private void readObject( java.io.ObjectInputStream in ) throws IOException,
			ClassNotFoundException
	{
		in.defaultReadObject( );

		ByteArrayInputStream bai = new ByteArrayInputStream( (byte[]) in.readObject( ) );

		try
		{
			cm = SerializerImpl.instance( ).fromXml( bai, true );
		}
		catch ( IOException e )
		{
			if ( logger != null )
			{
				logger.log( e );
			}
			cm = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartScriptContext#getChartInstance()
	 */
	public Chart getChartInstance( )
	{
		return cm;
	}

	/**
	 * Binding the script context with the chart instance
	 * 
	 * @param cm
	 *            Chart
	 */
	public void setChartInstance( Chart cm )
	{
		this.cm = cm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartScriptContext#getExternalContext()
	 */
	public IExternalContext getExternalContext( )
	{
		return externalContext;
	}

	/**
	 * @param externalContext
	 *            the context of script
	 */
	public void setExternalContext( IExternalContext externalContext )
	{
		this.externalContext = externalContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartScriptContext#getLocale()
	 */
	public Locale getLocale( )
	{
		return locale == null ? null : locale.toLocale( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartScriptContext#getULocale()
	 */
	public ULocale getULocale( )
	{
		return locale;
	}

	/**
	 * Sets associated locale.
	 * 
	 * @param locale
	 *            Locale
	 */
	public void setULocale( ULocale locale )
	{
		this.locale = locale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartScriptContext#getLogger()
	 */
	public ILogger getLogger( )
	{
		return logger;
	}

	/**
	 * Sets associated logger.
	 * 
	 * @param logger
	 *            Logger
	 */
	public void setLogger( ILogger logger )
	{
		this.logger = logger;
	}
}
