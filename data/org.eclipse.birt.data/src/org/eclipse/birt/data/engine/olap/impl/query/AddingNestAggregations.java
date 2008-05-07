/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;

/**
 * Adding nest aggregations operation
 */
public class AddingNestAggregations implements ICubeOperation
{

	private static final String EXPRESSION_FORMAT = "^\\Q" + ExpressionUtil.DATA_INDICATOR + "[\"\\E.+\\Q\"]\\E$";
	
	// used to define new nest aggregations
	private IBinding[] nestAggregations;

	/**
	 * @param nestAggregations:
	 *            new nest aggregations
	 * @throws DataException
	 */
	public AddingNestAggregations( IBinding[] nestAggregations )
			throws DataException
	{
		if ( nestAggregations == null || nestAggregations.length == 0 )
		{
			throw new IllegalArgumentException("nestAggregations is null or empty");
		}

		this.nestAggregations = new IBinding[nestAggregations.length];

		int i = 0;

		for ( IBinding addedBinding : nestAggregations )
		{
			if ( addedBinding == null )
			{
				throw new IllegalArgumentException("nestAggregations contains null member");
			}
			String bindingName = addedBinding.getBindingName( );
			if ( bindingName == null || bindingName.equals( "" ) )
			{

				throw new DataException( ResourceConstants.UNSPECIFIED_BINDING_NAME );
			}
			// Here, only check whether it's an aggregation binding
			// Whether it's a nest aggregation binding is checked during the
			// cube query execution
			if ( !isExpressionValid( addedBinding )
					|| !OlapExpressionUtil.isAggregationBinding( addedBinding ) )
			{
				throw new DataException( ResourceConstants.NOT_NEST_AGGREGATION_BINDING,
						addedBinding.getBindingName( ) );
			}
			this.nestAggregations[i++] = addedBinding;
		}
	}

	public IBinding[] getNewBindings( )
	{
		return nestAggregations;
	}
	

	/**
	 * Check whether the expression text of binding matches "data["xxxx"]"
	 * @param binding
	 * @return
	 * @throws DataException
	 */
	private boolean isExpressionValid( IBinding binding ) throws DataException
	{
		if ( !( binding.getExpression( ) instanceof IScriptExpression ) )
		{
			return false;
		}
		String expression = ((IScriptExpression)binding.getExpression( )).getText( );
		if (expression == null)
		{
			return false;
		}
		expression = expression.trim( );
		return expression.matches( EXPRESSION_FORMAT );
	}
}
