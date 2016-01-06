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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;

/**
 * AggregationArgumentHandle.
 */
public class AggregationArgumentHandle extends StructureHandle
{

	/**
	 * Constructs the handle of aggregation argument.
	 * 
	 * @param valueHandle
	 *            the value handle for aggregation argument list of one property
	 * @param index
	 *            the position of this aggregation argument in the list
	 */

	public AggregationArgumentHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns the argument name.
	 * 
	 * @return the argument name.
	 */

	public String getName( )
	{
		return getStringProperty( AggregationArgument.NAME_MEMBER );
	}

	/**
	 * Sets the argument name.
	 * 
	 * @param argumentName
	 *            the argument name to set
	 * @throws SemanticException
	 */

	public void setName( String argumentName ) throws SemanticException
	{
		setProperty( AggregationArgument.NAME_MEMBER, argumentName );
	}

	/**
	 * Returns the argument value.
	 * 
	 * @return the argument value.
	 */

	public String getValue( )
	{
		return getStringProperty( AggregationArgument.VALUE_MEMBER );
	}

	/**
	 * Sets the argument value.
	 * 
	 * @param argumentValue
	 *            the argument value to set
	 * @throws SemanticException
	 */

	public void setValue( String argumentValue ) throws SemanticException
	{
		setProperty( AggregationArgument.VALUE_MEMBER, argumentValue );
	}

	/**
	 * Returns the argument expression.
	 * 
	 * @return the argument expression.
	 */
	public Expression getExpression( )
	{
		return (Expression) ( getExpressionProperty(
				AggregationArgument.VALUE_MEMBER ).getValue( ) );
	}
}
