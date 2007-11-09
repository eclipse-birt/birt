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
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Proxy class of presentation for Chart. It will delegate to proper
 * implementation class according to Chart's context.
 */

public class ChartReportItemPresentationProxy
		implements
			IReportItemPresentation
{

	private IReportItemPresentation impl;

	private IReportItemPresentation createImpl( ExtendedItemHandle modelHandle )
	{
//		DesignElementHandle handle = modelHandle.getContainer( );
//		if ( handle instanceof ExtendedItemHandle )
//		{
//			String exName = ( (ExtendedItemHandle) handle ).getExtensionName( );
//			if ( ICrosstabConstants.CROSSTAB_CELL_EXTENSION_NAME.equals( exName ) )
//			{
//				// If chart is in cross tab cell, use specific impl
//				return new ChartCrosstabItemPresentationImpl( );
//			}
//		}
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

	public Object onRowSets( IRowSet[] rowSets ) throws BirtException
	{
		assert impl != null;
		return impl.onRowSets( rowSets );
	}

	public void setActionHandler( IHTMLActionHandler ah )
	{
		assert impl != null;
		impl.setActionHandler( ah );
	}

	public void setApplicationClassLoader( ClassLoader loader )
	{
		assert impl != null;
		impl.setApplicationClassLoader( loader );
	}

	public void setDynamicStyle( IStyle style )
	{
		assert impl != null;
		impl.setDynamicStyle( style );
	}

	public void setLocale( Locale locale )
	{
		assert impl != null;
		impl.setLocale( locale );
	}

	public void setModelObject( ExtendedItemHandle modelHandle )
	{
		impl = createImpl( modelHandle );
		assert impl != null;
		impl.setModelObject( modelHandle );
	}

	public void setOutputFormat( String outputFormat )
	{
		assert impl != null;
		impl.setOutputFormat( outputFormat );
	}

	public void setReportQueries( IBaseQueryDefinition[] queries )
	{
		assert impl != null;
		impl.setReportQueries( queries );
	}

	public void setResolution( int dpi )
	{
		assert impl != null;
		impl.setResolution( dpi );
	}

	public void setScriptContext( IReportContext context )
	{
		assert impl != null;
		impl.setScriptContext( context );
	}

	public void setSupportedImageFormats( String supportedImageFormats )
	{
		assert impl != null;
		impl.setSupportedImageFormats( supportedImageFormats );
	}

}
