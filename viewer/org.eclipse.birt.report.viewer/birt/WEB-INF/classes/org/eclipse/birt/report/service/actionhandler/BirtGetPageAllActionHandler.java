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

package org.eclipse.birt.report.service.actionhandler;

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateContent;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.ParameterAccessor;

public class BirtGetPageAllActionHandler extends AbstractBaseActionHandler
{

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtGetPageAllActionHandler( IContext context, Operation operation,
			GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	/**
	 * Get report service
	 */
	public IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}

	/**
	 * implement __execute method
	 */
	protected void __execute( ) throws RemoteException
	{
		try
		{
			// get attribute bean
			ViewerAttributeBean attrBean = (ViewerAttributeBean) context
					.getBean( );
			assert attrBean != null;

			String format = ParameterAccessor.getFormat( context.getRequest( ) );
			Locale locale = attrBean.getLocale( );
			boolean master = attrBean.isMasterPageContent( );
			boolean svgFlag = getSVGFlag( operation.getOprand( ) );
			String docName = attrBean.getReportDocumentName( );

			// get bookmark
			String bookmark = getBookmark( operation.getOprand( ), context
					.getBean( ) );

			// input options
			InputOptions options = new InputOptions( );
			options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
			options.setOption( InputOptions.OPT_LOCALE, locale );
			options.setOption( InputOptions.OPT_RTL, new Boolean( attrBean
					.isRtl( ) ) );
			options.setOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT,
					new Boolean( master ) );
			options
					.setOption( InputOptions.OPT_SVG_FLAG,
							new Boolean( svgFlag ) );
			options.setOption( InputOptions.OPT_FORMAT, format );
			options.setOption( InputOptions.OPT_IS_DESIGNER, new Boolean(
					attrBean.isDesigner( ) ) );

			// output as byte array
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
			if ( ParameterAccessor.isGetImageOperator( context.getRequest( ) ) )
			{
				// render image
				BirtRenderImageActionHandler renderImageHandler = new BirtRenderImageActionHandler(
						context, operation, response );
				renderImageHandler.execute( );
			}
			else if ( ParameterAccessor.isGetReportlet( context.getRequest( ) ) )
			{
				// render reportlet
				String __reportletId = attrBean.getReportletId( );
				getReportService( ).renderReportlet( docName, __reportletId,
						options, new ArrayList( ), out );
			}
			else if ( context.getBean( ).documentInUrl )
			{
				// render document file
				getReportService( ).renderReport( docName, null, options, out );
			}
			else
			{
				// run and render report design
				IViewerReportDesignHandle reportDesignHandle = attrBean
						.getReportDesignHandle( context.getRequest( ) );

				Map parameterMap = attrBean.getParameters( );
				if ( parameterMap == null )
					parameterMap = new HashMap( );

				Map displayTexts = attrBean.getDisplayTexts( );
				if ( displayTexts == null )
					displayTexts = new HashMap( );

				// handle operation
				BirtUtility.handleOperation( operation, attrBean, parameterMap,
						displayTexts );

				getReportService( ).runAndRenderReport( reportDesignHandle,
						docName, options, parameterMap, out, new ArrayList( ),
						displayTexts );
			}

			// Update response.
			UpdateContent content = new UpdateContent( );
			content.setContent( out.toString( ) );
			content.setTarget( "Document" ); //$NON-NLS-1$
			if ( bookmark != null )
				content.setBookmark( bookmark );

			Update updateDocument = new Update( );
			updateDocument.setUpdateContent( content );

			response.setUpdate( new Update[]{updateDocument} );
		}
		catch ( Exception e )
		{
			AxisFault fault = new AxisFault( );
			fault.setFaultCode( new QName(
					"BirtGetPageAllActionHandler.__execute( )" ) ); //$NON-NLS-1$
			fault.setFaultReason( e.getLocalizedMessage( ) );
			throw fault;
		}
	}
}