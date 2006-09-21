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

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterSelectionChoice;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Fragment help rendering scalar parameter.
 * <p>
 * 
 * @see org.eclipse.birt.report.presentation.aggregation.BaseFragment
 */
public class RadioButtonParameterFragment extends ScalarParameterFragment
{

	/**
	 * Protected constructor.
	 * 
	 * @param parameter
	 *            parameter definition reference.
	 */
	public RadioButtonParameterFragment( ParameterDefinition parameter )
	{
		super( parameter );
	}

	protected void prepareParameterBean( HttpServletRequest request,
			IViewerReportService service, ScalarParameterBean parameterBean,
			Locale locale ) throws ReportServiceException
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		assert attrBean != null;

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, request );

		Collection selectionList = service.getParameterSelectionList( attrBean
				.getReportDesignHandle( request ), options, parameterBean
				.getName( ) );

		if ( selectionList != null )
		{
			// Get Scalar parameter handle
			ScalarParameterHandle parameterHandle = (ScalarParameterHandle) attrBean
					.findParameter( parameter.getName( ) );

			for ( Iterator iter = selectionList.iterator( ); iter.hasNext( ); )
			{
				ParameterSelectionChoice selectionItem = (ParameterSelectionChoice) iter
						.next( );

				// Convert parameter value using standard format
				String value = DataUtil.getDisplayValue( selectionItem
						.getValue( ) );

				if ( value == null )
					continue;

				String label = selectionItem.getLabel( );
				if ( label == null || label.length( ) <= 0 )
				{
					// If label is null or blank, then use the format parameter
					// value for display
					label = ParameterValidationUtil.getDisplayValue( null,
							parameterHandle.getPattern( ), selectionItem
									.getValue( ), locale );
				}

				label = ParameterAccessor.htmlEncode( label );

				parameterBean.getSelectionList( ).add( label );
				parameterBean.getSelectionTable( ).put( label, value );
			}
		}
	}
}