/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.io.InputStream;
import java.util.Locale;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemPresentationInfo;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * Proxy class of presentation for Chart. It will delegate to proper
 * implementation class according to Chart's context.
 */

public class ChartReportItemPresentationProxy
		implements
			IReportItemPresentation
{

	private IReportItemPresentation impl;
	private IReportItemPresentationInfo info;

	public void init( IReportItemPresentationInfo info )
	{
		if ( info == null )
		{
			throw new NullPointerException( );
		}

		this.info = info;
		impl = createImpl( info );
		impl.init( info );
	}

	protected IReportItemPresentation createImpl(
			IReportItemPresentationInfo info )
	{
		ExtendedItemHandle modelHandle = info.getModelObject( );
		if ( ChartXTabUtil.isInXTabMeasureCell( modelHandle ) )
		{
			// // If chart is in cross tab cell, use specific impl
			if ( ChartXTabUtil.isPlotChart( modelHandle ) )
			{
				return new ChartReportItemPresentationPlotImpl( );
			}
			else if ( ChartXTabUtil.isAxisChart( modelHandle ) )
			{
				return new ChartReportItemPresentationAxisImpl( );
			}
		}
		IAdapterManager adapterManager = Platform.getAdapterManager( );
		IChartReportItemFactory factory = (IChartReportItemFactory) adapterManager.loadAdapter( modelHandle,
				IChartReportItemFactory.class.getName( ) );
		if ( factory != null )
		{
			return factory.createReportItemPresentation( info );
		}
		return new ChartReportItemPresentationImpl( );
	}

	public void deserialize( InputStream istream )
	{
		assert impl != null;
		impl.deserialize( istream );
	}

	public void finish( )
	{
		assert impl != null;
		impl.finish( );
	}

	public String getImageMIMEType( )
	{
		assert impl != null;
		return impl.getImageMIMEType( );
	}

	public int getOutputType( )
	{
		assert impl != null;
		return impl.getOutputType( );
	}

	public Size getSize( )
	{
		assert impl != null;
		return impl.getSize( );
	}

	public Object onRowSets( IBaseResultSet[] results ) throws BirtException
	{
		assert impl != null;
		return impl.onRowSets( results );
	}

	public IReportItemPresentationInfo getPresentationConfig( )
	{
		return info;
	}

	// Follows deprecated methods. Empty implementation

	public Object onRowSets( IRowSet[] rowSets ) throws BirtException
	{
		assert false;
		return null;
	}

	public void setActionHandler( IHTMLActionHandler ah )
	{
		assert false;

	}

	public void setApplicationClassLoader( ClassLoader loader )
	{
		assert false;

	}

	public void setDynamicStyle( IStyle style )
	{
		assert false;

	}

	public void setExtendedItemContent( IContent content )
	{
		assert false;

	}

	public void setLocale( Locale locale )
	{
		assert false;

	}

	public void setModelObject( ExtendedItemHandle modelHandle )
	{
		assert false;

	}

	public void setOutputFormat( String outputFormat )
	{
		assert false;

	}

	public void setReportQueries( IDataQueryDefinition[] queries )
	{
		assert false;

	}

	public void setResolution( int dpi )
	{
		assert false;

	}

	public void setScriptContext( IReportContext context )
	{
		assert false;

	}

	public void setSupportedImageFormats( String supportedImageFormats )
	{
		assert false;

	}
}
