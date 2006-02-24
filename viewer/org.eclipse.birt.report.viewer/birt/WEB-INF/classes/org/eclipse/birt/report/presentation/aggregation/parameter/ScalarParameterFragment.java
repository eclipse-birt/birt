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

package org.eclipse.birt.report.presentation.aggregation.parameter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.utility.ParameterAccessor;
import org.eclipse.birt.report.presentation.aggregation.BaseFragment;

/**
 * Fragment help rendering scalar parameter.
 * <p>
 * 
 * @see BaseFragment
 */
public class ScalarParameterFragment extends BaseFragment
{

	/**
	 * Reference to the real parameter definition.
	 */
	protected ScalarParameterHandle parameter = null;

	/**
	 * Protected constructor.
	 * 
	 * @param parameter
	 *            parameter definition reference.
	 */
	protected ScalarParameterFragment( ScalarParameterHandle parameter )
	{
		this.parameter = parameter;
	}

	/**
	 * Get report parameters from engine.
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @return target jsp pages
	 * @exception ServletException
	 * @exception IOException
	 */
	protected void doService( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request
				.getAttribute( "attributeBean" ); //$NON-NLS-1$
		assert attrBean != null;

		assert parameter != null;
		ScalarParameterBean parameterBean = new ScalarParameterBean( parameter );
		attrBean.setParameterBean( parameterBean );

		// Display name
		String displayName = parameter.getPromptText( );
		displayName = ( displayName == null || displayName.length( ) <= 0 ) ? parameter.getDisplayName( ) : displayName;
		displayName = ( displayName == null || displayName.length( ) <= 0 ) ? parameter.getName( ) : displayName;
		displayName = ParameterAccessor.htmlEncode( displayName );
		parameterBean.setDisplayName( displayName );

		// Default value.
		String parameterDefaultValue = null;
		IGetParameterDefinitionTask parameterTask = attrBean.getParameterTask( );
		assert parameterTask != null;
		Object paramDefaultValueObj = parameterTask
				.getDefaultValue( parameter.getName( ) );

		// isRequired
		switch ( ReportEngineService.getInstance( ).getEngineDataType( parameter.getDataType( ) ) )
		{
			case IScalarParameterDefn.TYPE_STRING :
			{
				assert paramDefaultValueObj instanceof String;

				parameterBean.setRequired( false );

				if ( paramDefaultValueObj == null && !parameter.allowNull( ) )
				{
					parameterBean.setRequired( true );
				}
				else if ( paramDefaultValueObj != null
						&& ( (String) paramDefaultValueObj ).length( ) <= 0
						&& !parameter.allowBlank( ) )
				{
					parameterBean.setRequired( true );
				}

				break;
			}
			default :
			{
				parameterBean.setRequired( paramDefaultValueObj == null );
				break;
			}
		}

		// Current value
		Locale locale = attrBean.getLocale( );
		String format = parameter.getFormat( );
		ReportParameterConverter converter = new ReportParameterConverter(
				format, locale );
		parameterDefaultValue = converter.format( paramDefaultValueObj );

		// Get value from test config
		if ( attrBean.isDesigner( ) )
		{
			HashMap configVariables = attrBean.getReportTestConfig( );

			if ( configVariables != null )
			{
				String configValue = (String) configVariables.get( parameter
						.getName( ) );

				if ( configValue != null && configValue.length( ) > 0 )
				{
					ReportParameterConverter cfgConverter = new ReportParameterConverter(
							format, Locale.US );
					Object configValueObj = cfgConverter.parse( configValue,
							ReportEngineService.getInstance( ).getEngineDataType( parameter.getDataType( ) ) );
					parameterDefaultValue = converter.format( configValueObj );
				}
			}
		}

		parameterBean.setValue( ParameterAccessor.getReportParameter( request,
				parameter.getName( ), parameterDefaultValue ) );

		// Prepare additional parameter properties.
		prepareParameterBean( request, attrBean.getParameterTask( ),
				parameterBean, format, locale );
	}

	/**
	 * Override implementation of doPostService.
	 */
	protected String doPostService( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException
	{
		String className = getClass( ).getName( ).substring(
				getClass( ).getName( ).lastIndexOf( '.' ) + 1 );
		return "/pages/parameter/" + className + ".jsp"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	protected void prepareParameterBean( HttpServletRequest request,
			IGetParameterDefinitionTask task,
			ScalarParameterBean parameterBean, String format, Locale locale )
	{
	}
}