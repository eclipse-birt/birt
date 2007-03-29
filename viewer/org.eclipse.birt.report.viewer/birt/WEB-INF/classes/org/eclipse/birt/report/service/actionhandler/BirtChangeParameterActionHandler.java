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
import java.util.Locale;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.OutputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Page;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateContent;
import org.eclipse.birt.report.soapengine.api.UpdateData;
import org.eclipse.birt.report.utility.DataUtil;

public class BirtChangeParameterActionHandler
		extends
			AbstractChangeParameterActionHandler
{

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtChangeParameterActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void runReport( ) throws Exception
	{
		BirtRunReportActionHandler handler = new BirtRunReportActionHandler(
				context, operation, response );
		handler.__execute( );
	}

	protected void doRenderPage( String docName, long pageNumber,
			String format, boolean svgFlag, boolean isMasterContent,
			boolean useBookmark, String bookmark, Locale locale, boolean isRtl )
			throws ReportServiceException, RemoteException
	{
		ArrayList activeIds = new ArrayList( );
		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		options.setOption( InputOptions.OPT_FORMAT, format );
		options.setOption( InputOptions.OPT_LOCALE, locale );
		options.setOption( InputOptions.OPT_RTL, new Boolean( isRtl ) );
		options.setOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT,
				new Boolean( isMasterContent ) );
		options.setOption( InputOptions.OPT_SVG_FLAG, new Boolean( svgFlag ) );
		ByteArrayOutputStream page = getReportService( ).getPage( docName,
				pageNumber + "", options, activeIds ); //$NON-NLS-1$

		// Update instruction for document.
		UpdateContent content = new UpdateContent( );
		content.setContent( DataUtil.toUTF8( page.toByteArray( ) ) );
		content.setTarget( operation.getTarget( ).getId( ) );
		content.setInitializationId( parseReportId( activeIds ) );
		if ( useBookmark )
		{
			content.setBookmark( bookmark );
		}

		Update updateDocument = new Update( );
		updateDocument.setUpdateContent( content );

		// Update instruction for nav bar.
		UpdateData updateData = new UpdateData( );
		updateData.setTarget( "navigationBar" ); //$NON-NLS-1$
		Page pageObj = new Page( );
		pageObj.setPageNumber( String.valueOf( pageNumber ) );
		pageObj.setTotalPage( String.valueOf( getReportService( ).getPageCount(
				docName, options, new OutputOptions( ) ) ) );
		Data data = new Data( );
		data.setPage( pageObj );
		updateData.setData( data );
		Update updateNavbar = new Update( );
		updateNavbar.setUpdateData( updateData );

		response.setUpdate( new Update[]{updateDocument, updateNavbar} );
	}

	protected IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}
}