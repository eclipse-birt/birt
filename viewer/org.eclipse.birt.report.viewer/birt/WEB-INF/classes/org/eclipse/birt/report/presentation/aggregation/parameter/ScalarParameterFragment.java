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
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Fragment help rendering scalar parameter.
 * <p>
 * 
 * @see BaseFragment
 */
public class ScalarParameterFragment extends BirtBaseFragment
{

	/**
	 * Reference to the real parameter definition.
	 */
	protected ParameterDefinition parameter = null;

	/**
	 * Protected constructor.
	 * 
	 * @param parameter
	 *            parameter definition reference.
	 */
	protected ScalarParameterFragment( ParameterDefinition parameter )
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
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		assert attrBean != null;
		assert parameter != null;

		ScalarParameterBean parameterBean = new ScalarParameterBean( parameter );
		attrBean.setParameterBean( parameterBean );

		Locale locale = attrBean.getLocale( );
		boolean isDesigner = attrBean.isDesigner( );
		try
		{
			prepareParameterBean( attrBean.getReportDesignHandle( request ),
					getReportService( ), request, parameterBean, parameter,
					locale, isDesigner );
			// Prepare additional parameter properties.
			prepareParameterBean( request, getReportService( ), parameterBean,
					locale );
		}
		catch ( ReportServiceException e )
		{
			// TODO: What to do with exception?
			e.printStackTrace( );
		}
	}

	/**
	 * Override implementation of doPostService.
	 */
	protected String doPostService( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException
	{
		String className = getClass( ).getName( ).substring(
				getClass( ).getName( ).lastIndexOf( '.' ) + 1 );
		return JSPRootPath + "/pages/parameter/" + className + ".jsp"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	public static void prepareParameterBean(
			IViewerReportDesignHandle designHandle,
			IViewerReportService service, HttpServletRequest request,
			ScalarParameterBean parameterBean, ParameterDefinition parameter,
			Locale locale, boolean isDesigner ) throws ReportServiceException
	{
		// Display name
		String displayName = parameter.getPromptText( );
		displayName = ( displayName == null || displayName.length( ) <= 0 )
				? parameter.getDisplayName( )
				: displayName;
		displayName = ( displayName == null || displayName.length( ) <= 0 )
				? parameter.getName( )
				: displayName;
		displayName = ParameterAccessor.htmlEncode( displayName );
		parameterBean.setDisplayName( displayName );

		// Directly get parameter values from AttributeBean
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		assert attrBean != null;

		// parameter value.
		String parameterValue = null;

		if ( attrBean.getParametersAsString( ) != null )
			parameterValue = (String) attrBean.getParametersAsString( ).get(
					parameterBean.getName( ) );

		if ( parameterValue == null && !parameter.allowNull( )
				&& parameter.allowBlank( ) )
		{
			parameterValue = ""; //$NON-NLS-1$
		}

		// isRequired
		switch ( parameter.getDataType( ) )
		{
			case ParameterDefinition.TYPE_STRING :
			{
				// parame default value may be null if it allows "null" value
				// assert paramDefaultValueObj instanceof String;
				parameterBean.setRequired( false );

				if ( parameterValue == null && !parameter.allowNull( ) )
				{
					parameterBean.setRequired( true );
				}
				else if ( parameterValue != null
						&& parameterValue.length( ) <= 0
						&& !parameter.allowBlank( ) )
				{
					parameterBean.setRequired( true );
				}

				break;
			}
			default :
			{
				parameterBean.setRequired( parameterValue == null );
				break;
			}
		}

		// Set parameter current value
		parameterBean.setValue( parameterValue );

		// Set parameter default value
		Map defaultValues = attrBean.getDefaultValues( );
		Object defaultValue = defaultValues.get( parameter.getName( ) );
		if ( defaultValue != null )
		{
			parameterBean.setDefaultValue( DataUtil
					.getDisplayValue( defaultValue ) );
		}
	}

	protected void prepareParameterBean( HttpServletRequest request,
			IViewerReportService service, ScalarParameterBean parameterBean,
			Locale locale ) throws ReportServiceException
	{
	}
}