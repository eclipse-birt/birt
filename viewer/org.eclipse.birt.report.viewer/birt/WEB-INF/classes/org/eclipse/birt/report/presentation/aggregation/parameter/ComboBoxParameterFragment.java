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

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.service.api.ParameterSelectionChoice;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.DataUtil;

/**
 * Fragment help rendering scalar parameter.
 * <p>
 * 
 * @see org.eclipse.birt.report.presentation.aggregation.BaseFragment
 */
public class ComboBoxParameterFragment extends ScalarParameterFragment
{

	/**
	 * Protected constructor.
	 * 
	 * @param parameter
	 *            parameter definition reference.
	 */
	public ComboBoxParameterFragment( ParameterDefinition parameter )
	{
		super( parameter );
	}

	/**
	 * Override implementation of prepareParameterBean method
	 * 
	 * @see org.eclipse.birt.report.presentation.aggregation.parameter.ScalarParameterFragment#prepareParameterBean(javax.servlet.http.HttpServletRequest,
	 *      org.eclipse.birt.report.service.api.IViewerReportService,
	 *      org.eclipse.birt.report.context.ScalarParameterBean,
	 *      java.util.Locale)
	 */
	protected void prepareParameterBean( HttpServletRequest request,
			IViewerReportService service, ScalarParameterBean parameterBean,
			Locale locale ) throws ReportServiceException
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		assert attrBean != null;

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, request );

		Collection selectionList = null;
		ParameterDefinition paramDef = parameterBean.getParameter( );
		if ( paramDef.getGroup( ) != null && paramDef.getGroup( ).cascade( ) )
		{
			// get parameter list from cascading group
			Map paramValues = attrBean.getParameters( );
			selectionList = getParameterSelectionListForCascadingGroup(
					attrBean.getReportDesignHandle( request ), service,
					paramValues, options );

			// Set cascade flag as true
			parameterBean.setCascade( true );
		}
		else
		{
			// get parameter list
			selectionList = service.getParameterSelectionList( attrBean
					.getReportDesignHandle( request ), options, parameter
					.getName( ) );

			// Set cascade flag as false
			parameterBean.setCascade( false );
		}

		parameterBean.setValueInList( false );
		if ( selectionList != null )
		{
			for ( Iterator iter = selectionList.iterator( ); iter.hasNext( ); )
			{
				ParameterSelectionChoice selectionItem = (ParameterSelectionChoice) iter
						.next( );

				Object value = selectionItem.getValue( );
				if ( value == null )
					continue;
				
				// try convert value to parameter definition data type
				try
				{
					value = DataUtil.convert( value, paramDef.getDataType( ) );
				}
				catch ( Exception e )
				{

				}

				// Convert parameter value using standard format
				String displayValue = DataUtil.getDisplayValue( value );
				if ( displayValue == null )
					continue;

				String label = selectionItem.getLabel( );
				if ( label == null || label.length( ) <= 0 )
				{
					// If label is null or blank, then use the format parameter
					// value for display
					label = ParameterValidationUtil.getDisplayValue( null,
							paramDef.getPattern( ), value, locale );
				}

				if ( label != null )
				{
					parameterBean.getSelectionList( ).add( label );
					parameterBean.getSelectionTable( )
							.put( label, displayValue );
				}

				// If parameter value is in the selection list
				if ( displayValue.equals( parameterBean.getValue( ) ) )
				{
					parameterBean.setValueInList( true );
				}

				// If parameter default value is in the selection list
				if ( displayValue.equals( parameterBean.getDefaultValue( ) ) )
				{
					parameterBean.setDefaultValueInList( true );
				}
			}
		}
	}

	private Collection getParameterSelectionListForCascadingGroup(
			IViewerReportDesignHandle design, IViewerReportService service,
			Map paramValues, InputOptions options )
			throws ReportServiceException
	{

		ParameterGroupDefinition group = (ParameterGroupDefinition) parameter
				.getGroup( );
		int index = group.getParameters( ).indexOf( parameter );
		Object[] groupKeys = new Object[index];
		for ( int i = 0; i < index; i++ )
		{
			ParameterDefinition def = (ParameterDefinition) group
					.getParameters( ).get( i );
			String parameterName = def.getName( );
			groupKeys[i] = paramValues.get( parameterName );
			if ( groupKeys[i] == null )
			{
				groupKeys[i] = service.getParameterDefaultValue( design,
						parameterName, options );
			}
		}
		return service.getSelectionListForCascadingGroup( design, group
				.getName( ), groupKeys, options );
	}
}