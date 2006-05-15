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

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;

public class BirtRunReportActionHandler extends AbstractBaseActionHandler
{

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtRunReportActionHandler( IContext context, Operation operation,
			GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	/**
	 * Local execution.
	 * 
	 * @exception ReportServiceException
	 * @return
	 */
	protected void __execute( ) throws RemoteException
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean( );
		Map parameterMap = new HashMap( );

		if ( operation != null )
		{
			Oprand[] oprands = operation.getOprand( );
			for ( int i = 0; i < oprands.length; i++ )
			{
				String paramName = oprands[i].getName( );
				String paramValue = oprands[i].getValue( );
				parameterMap.put( paramName, paramValue );
			}
		}

		String reportDesignName = attrBean.getReportDesignName( );
		String docName = attrBean.getReportDocumentName( );
		// TODO: Content type?
		IViewerReportDesignHandle designHandle = new BirtViewerReportDesignHandle(
				null, reportDesignName );
		try
		{
			InputOptions options = new InputOptions( );
			options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
			options.setOption( InputOptions.OPT_LOCALE, attrBean.getLocale( ) );
			options.setOption( InputOptions.OPT_RTL, new Boolean( attrBean
					.isRtl( ) ) );
			options.setOption( InputOptions.OPT_IS_DESIGNER, new Boolean(
					attrBean.isDesigner( ) ) );
			getReportService( ).runReport( designHandle, docName, options,
					parameterMap );
		}
		catch ( ReportServiceException e )
		{
			AxisFault fault = new AxisFault( );
			fault.setFaultReason( e.getMessage( ) );
			throw fault;
		}
	}

	protected IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}
}
