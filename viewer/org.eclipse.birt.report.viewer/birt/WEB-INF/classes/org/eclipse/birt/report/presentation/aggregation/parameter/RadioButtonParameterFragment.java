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

import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.service.BirtViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
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
	public RadioButtonParameterFragment( ScalarParameterHandle parameter )
	{
		super( parameter );
	}

	protected void prepareParameterBean( HttpServletRequest request,
			IViewerReportService service, ScalarParameterBean parameterBean,
			String format, Locale locale ) throws ReportServiceException
	{
		String reportDesignName = ParameterAccessor.getReport( request );
		
		//TODO: Content type?
		IViewerReportDesignHandle designHandle = new BirtViewerReportDesignHandle(
				null, reportDesignName );
		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, request );

		Collection selectionList = service.getParameterSelectionList(
				designHandle, options, parameterBean.getName( ) );

		if ( selectionList != null )
		{
			ReportParameterConverter converter = new ReportParameterConverter(
					format, locale );

			for ( Iterator iter = selectionList.iterator( ); iter.hasNext( ); )
			{
				IParameterSelectionChoice selectionItem = ( IParameterSelectionChoice ) iter
						.next( );

				String value = converter.format( selectionItem.getValue( ) );
				String label = selectionItem.getLabel( );
				label = ( label == null || label.length( ) <= 0 ) ? value
						: label;
				label = ParameterAccessor.htmlEncode( label );

				parameterBean.getSelectionList( ).add( label );
				parameterBean.getSelectionTable( ).put( label, value );
			}
		}
	}
}