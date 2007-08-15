/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;

/**
 * This class provides various Data Engine Query utility methods for UI.
 */

public class DataUtil
{

	/**
	 * Return a list of valid group key bindings. Only those bindings that do not involve aggregations will be
	 * allowed.
	 * 
	 * @param bindings
	 * @return
	 */
	public static List getValidGroupKeyBindings( List bindings )
	{

		List result = new ArrayList( );
		if ( bindings == null )
			return result;
		try
		{
			for ( int i = 0; i < bindings.size( ); i++ )
			{
				IBinding binding = (IBinding) bindings.get( i );
				List originalNames = new ArrayList( );
				originalNames.add( binding.getBindingName( ) );
				if ( acceptBinding( binding, bindings, originalNames ) )
					result.add( binding );
			}
		}
		catch ( Exception e )
		{
			return result;
		}

		return result;

	}

	/**
	 * 
	 * @param binding
	 * @param bindings
	 * @param originalNames
	 * @return
	 */
	private static boolean acceptBinding( IBinding binding, List bindings,
			List originalNames )
	{
		try
		{
			if ( binding.getAggrFunction( ) == null )
			{
				IBaseExpression expr = binding.getExpression( );
				if ( expr instanceof IScriptExpression )
				{
					if ( !ExpressionUtil.hasAggregation( ( (IScriptExpression) expr ).getText( ) ) )
					{
						List referredBindings = ExpressionUtil.extractColumnExpressions( ( (IScriptExpression) expr ).getText( ) );
						List names = new ArrayList( );
						names.add( binding.getBindingName( ) );
						names.addAll( originalNames );
						if ( acceptindirectReferredBindings( originalNames,
								bindings,
								referredBindings ) )
						{
							return true;
						}
					}
				}
			}
		}
		catch ( BirtException e )
		{
		}
		return false;
	}

	/**
	 * 
	 * @param originalBindingName
	 * @param bindings
	 * @param referredBindings
	 * @return
	 */
	private static boolean acceptindirectReferredBindings(
			List originalBindingName, List bindings, List referredBindings )
	{
		try
		{
			List candidateBindings = new ArrayList( );
			for ( int i = 0; i < referredBindings.size( ); i++ )
			{
				IColumnBinding cb = (IColumnBinding) referredBindings.get( i );
				for ( int j = 0; j < bindings.size( ); j++ )
				{
					IBinding binding = (IBinding) bindings.get( j );
					if ( originalBindingName.contains( binding.getBindingName( ) ) )
						continue;
					if ( binding.getBindingName( )
							.equals( cb.getResultSetColumnName( ) ) )
						candidateBindings.add( binding );
				}
			}

			for ( int i = 0; i < candidateBindings.size( ); i++ )
			{
				IBinding binding = (IBinding) candidateBindings.get( i );
				if ( !acceptBinding( binding, bindings, originalBindingName ) )
					return false;
			}
			return true;
		}
		catch ( Exception e )
		{
			return false;
		}
	}
}
