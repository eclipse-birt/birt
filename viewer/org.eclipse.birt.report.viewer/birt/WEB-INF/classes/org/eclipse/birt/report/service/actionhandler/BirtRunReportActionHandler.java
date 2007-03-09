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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.ParameterDataTypeConverter;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

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
	protected void __execute( ) throws Exception
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean( );
		assert attrBean != null;

		Map parameterMap = new HashMap( );
		Map displayTexts = new HashMap( );

		String docName = attrBean.getReportDocumentName( );
		IViewerReportDesignHandle designHandle = attrBean
				.getReportDesignHandle( context.getRequest( ) );

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		options.setOption( InputOptions.OPT_LOCALE, attrBean.getLocale( ) );
		options.setOption( InputOptions.OPT_RTL,
				new Boolean( attrBean.isRtl( ) ) );
		options.setOption( InputOptions.OPT_IS_DESIGNER, new Boolean( attrBean
				.isDesigner( ) ) );

		// convert parameter
		if ( operation != null )
		{
			String displayTextParam = null;
			Oprand[] oprands = operation.getOprand( );
			for ( int i = 0; i < oprands.length; i++ )
			{
				String paramName = oprands[i].getName( );
				Object paramValue = oprands[i].getValue( );

				// Check if parameter set to null
				if ( ParameterAccessor.PARAM_ISNULL
						.equalsIgnoreCase( paramName )
						&& paramValue != null )
				{
					// find the parameter
					ParameterDefinition parameter = attrBean
							.findParameterDefinition( paramValue.toString( ) );

					if ( parameter != null )
					{
						// set parametet to null value
						parameterMap.put( paramValue, null );
						continue;
					}
				}
				else if ( ( displayTextParam = ParameterAccessor
						.isDisplayText( paramName ) ) != null )
				{
					displayTexts.put( displayTextParam, paramValue );
					continue;
				}

				// find the parameter
				ParameterDefinition parameter = attrBean
						.findParameterDefinition( paramName );

				if ( parameter != null && paramValue != null )
				{
					String dataType = ParameterDataTypeConverter
							.ConvertDataType( parameter.getDataType( ) );

					// convert parameter to Object
					paramValue = DataUtil.validate( dataType, parameter
							.getPattern( ), paramValue.toString( ), attrBean
							.getLocale( ) );
				}

				if ( parameter != null )
				{
					parameterMap.put( paramName, paramValue );
				}
			}
		}

		getReportService( ).runReport( designHandle, docName, options,
				parameterMap, displayTexts );
	}

	protected IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}
}
