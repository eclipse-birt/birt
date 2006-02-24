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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.context.ScalarParameterBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Fragment help rendering scalar parameter.
 * <p>
 * 
 * @see org.eclipse.birt.report.viewer.aggregation.BaseFragment
 */
public class ComboBoxParameterFragment extends ScalarParameterFragment
{

	/**
	 * Protected constructor.
	 * 
	 * @param parameter
	 *            parameter definition reference.
	 */
	public ComboBoxParameterFragment( ScalarParameterHandle parameter )
	{
		super( parameter );
	}

	protected void prepareParameterBean( HttpServletRequest request,
			IGetParameterDefinitionTask task, ScalarParameterBean parameterBean, String format,
			Locale locale )
	{
		Collection selectionList = null;
		if ( parameterBean.getParameter( ).getContainer( ) instanceof CascadingParameterGroupHandle )
		{
			ViewerAttributeBean attrBean = (ViewerAttributeBean) request
					.getAttribute( "attributeBean" ); //$NON-NLS-1$
			HashMap paramValues = attrBean.getParameters( );
			CascadingParameterGroupHandle group = (CascadingParameterGroupHandle) parameter
					.getContainer( );
			int index = group.getParameters( ).findPosn( parameter );
			Object[] keyValue = new Object[index];
			for ( int i = 0; i < index; i++ )
			{
				String parameterName = group.getParameters( ).get( i )
						.getName( );
				keyValue[i] = paramValues.get( parameterName );
				if ( keyValue[i] == null )
				{
					keyValue[i] = task.getDefaultValue( parameterName );
				}
			}
			task.evaluateQuery( group.getName( ) );
			selectionList = task.getSelectionListForCascadingGroup( group
					.getName( ), keyValue );
		}
		else
		{
			selectionList = task.getSelectionList( parameter.getName( ) );
		}

		parameterBean.setValueInList( false );

		if ( selectionList != null )
		{
			ReportParameterConverter converter = new ReportParameterConverter(
					format, locale );

			for ( Iterator iter = selectionList.iterator( ); iter.hasNext( ); )
			{
				IParameterSelectionChoice selectionItem = (IParameterSelectionChoice) iter
						.next( );

				String value = converter.format( selectionItem.getValue( ) );
				String label = selectionItem.getLabel( );
				label = ( label == null || label.length( ) <= 0 )
						? value
						: label;
				label = ParameterAccessor.htmlEncode( label );

				if ( label != null )
				{
					parameterBean.getSelectionList( ).add( label );
					parameterBean.getSelectionTable( ).put( label, value );
				}

				if ( value != null && value.equals( parameterBean.getValue( ) ) )
				{
					parameterBean.setValueInList( true );
				}
			}
		}
	}
}