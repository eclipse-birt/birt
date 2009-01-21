/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * This class presents the parameter binding that bind data set input parameter
 * to expression by position. Order of these bindings must match the order of
 * parameter markers ("?"") in the statement. Each parameter binding has the
 * following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Parameter Name </strong></dt>
 * <dd>a parameter bing has a required parameter name to bind.</dd>
 * 
 * <dt><strong>Expression </strong></dt>
 * <dd>associated an expression with a named input parameter.</dd>
 * </dl>
 * 
 */

public class ParamBinding extends Structure
{

	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public static final String PARAM_BINDING_STRUCT = "ParamBinding"; //$NON-NLS-1$

	/**
	 * Name of the parameter name member.
	 */

	public static final String PARAM_NAME_MEMBER = "paramName"; //$NON-NLS-1$

	/**
	 * Name of the parameter binding expression member.
	 */

	public static final String EXPRESSION_MEMBER = "expression"; //$NON-NLS-1$

	/**
	 * The parameter name.
	 */

	private String paramName = null;

	/**
	 * The parameter expression expression.
	 */

	private String expression = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return PARAM_BINDING_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( PARAM_NAME_MEMBER.equals( propName ) )
			return paramName;
		if ( EXPRESSION_MEMBER.equals( propName ) )
			return expression;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( PARAM_NAME_MEMBER.equals( propName ) )
			paramName = (String) value;
		else if ( EXPRESSION_MEMBER.equals( propName ) )
			expression = (String) value;
		else
			assert false;

	}

	/**
	 * Returns the parameter name of this binding.
	 * 
	 * @return the parameter name of this binding
	 */

	public String getParamName( )
	{
		return (String) getProperty( null, PARAM_NAME_MEMBER );
	}

	/**
	 * Sets the parameter name of this binding.
	 * 
	 * @param name
	 *            the parameter name to set
	 */

	public void setParamName( String name )
	{
		setProperty( PARAM_NAME_MEMBER, name );
	}

	/**
	 * Returns the binding expression.
	 * 
	 * @return the binding expression
	 */

	public String getExpression( )
	{
		return (String) getProperty( null, EXPRESSION_MEMBER );
	}

	/**
	 * Sets the binding expression.
	 * 
	 * @param expression
	 *            the expression to set
	 */

	public void setExpression( String expression )
	{
		setProperty( EXPRESSION_MEMBER, expression );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt
	 * .report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List<SemanticException> validate( Module module,
			DesignElement element )
	{
		ArrayList<SemanticException> list = new ArrayList<SemanticException>( );

		if ( StringUtil.isBlank( getParamName( ) ) )
		{
			list.add( new PropertyValueException( element, getDefn( )
					.getMember( PARAM_NAME_MEMBER ), getParamName( ),
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new ParamBindingHandle( valueHandle, index );
	}

}