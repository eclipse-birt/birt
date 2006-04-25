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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.context.ParameterGroupBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Fragment help rendering parameter group.
 * <p>
 * 
 * @see BaseFragment
 */
public class ParameterGroupFragment extends BirtBaseFragment
{

	/**
	 * Reference to the real parameter group definition.
	 */
	protected ParameterGroupHandle parameterGroup = null;

	/**
	 * Protected constructor.
	 * 
	 * @param parameterGroup
	 *            parameter group definition reference.
	 */
	public ParameterGroupFragment( ParameterGroupHandle parameterGroup )
	{
		this.parameterGroup = parameterGroup;
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
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute( "attributeBean" ); //$NON-NLS-1$
		assert attrBean != null;

		assert parameterGroup != null;
		ParameterGroupBean parameterGroupBean = new ParameterGroupBean( parameterGroup );
		attrBean.setParameterBean( parameterGroupBean );

		// Display name.
		String displayName = parameterGroup.getDisplayName( );
		displayName = ( displayName == null || displayName.length( ) <= 0 ) ? parameterGroup.getName( )
				: displayName;
		displayName = ParameterAccessor.htmlEncode( displayName );
		parameterGroupBean.setDisplayName( displayName );

		// Parameters inside group.
		Collection fragments = new ArrayList( );
		IFragment fragment = null;

		for ( Iterator iter = parameterGroup.getParameters( ).iterator( ); iter.hasNext( ); )
		{
			Object obj = iter.next( );
			if ( obj instanceof ScalarParameterHandle )
			{
				ScalarParameterHandle scalarParameter = (ScalarParameterHandle) obj;

				if ( !scalarParameter.isHidden( ) )
				{
					switch ( getEngineControlType( scalarParameter.getControlType( ) ) )
					{
						case IScalarParameterDefn.TEXT_BOX :
						{
							fragment = new TextBoxParameterFragment( scalarParameter );
							break;
						}
						case IScalarParameterDefn.LIST_BOX :
						{
							fragment = new ComboBoxParameterFragment( scalarParameter );
							break;
						}
						case IScalarParameterDefn.RADIO_BUTTON :
						{
							fragment = new RadioButtonParameterFragment( scalarParameter );
							break;
						}
						case IScalarParameterDefn.CHECK_BOX :
						{
							fragment = new CheckboxParameterFragment( scalarParameter );
							break;
						}
					}
				}

				if ( fragment != null )
				{
					fragments.add( fragment );
				}
			}
		}

		request.setAttribute( "fragments", fragments ); //$NON-NLS-1$
	}

	/**
	 * Override implementation of doPostService.
	 */
	protected String doPostService( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException
	{
		String className = getClass( ).getName( )
				.substring( getClass( ).getName( ).lastIndexOf( '.' ) + 1 );
		return "/iportal/birt" + "/pages/parameter/" + className + ".jsp"; //$NON-NLS-1$  //$NON-NLS-2$
	}
}