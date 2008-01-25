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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.aggregation.IAggregationFactory;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.ULocale;

/**
 * This class provides various Data Engine Query utility methods for UI.
 */

public class DataUtil
{
	public static IAggregationFactory getAggregationFactory( ) throws BirtException
	{
		return DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) )
				.getAggregationFactory( );
	}
	/**
	 * Get all referenced bindings by the given binding in a set of binding
	 * list.
	 * 
	 * @param target
	 * @param allHandleList
	 * @return
	 */
	public static Set getReferencedBindings( ComputedColumnHandle target,
			List allHandleList )
	{
		return getReferencedBindings( target, allHandleList, new HashSet( ) );
	}

	/**
	 * 
	 * @param target
	 * @param allHandleList
	 * @param prohibitedSet
	 * @return
	 */
	private static Set getReferencedBindings( ComputedColumnHandle target,
			List allHandleList, Set prohibitedSet )
	{
		Set result = new HashSet( );
		if ( target == null
				|| allHandleList == null || allHandleList.size( ) == 0 )
			return result;
		prohibitedSet.add( target.getName( ) );
		String expr = target.getExpression( );
		try
		{
			List referredBindings = ExpressionUtil.extractColumnExpressions( expr );
			for ( int i = 0; i < referredBindings.size( ); i++ )
			{
				IColumnBinding binding = (IColumnBinding) referredBindings.get( i );
				for ( int j = 0; j < allHandleList.size( ); j++ )
				{
					ComputedColumnHandle handle = (ComputedColumnHandle) allHandleList.get( j );
					if ( handle.getName( )
							.equals( binding.getResultSetColumnName( ) )
							&& !prohibitedSet.contains( binding.getResultSetColumnName( ) ) )
					{
						result.add( handle );
					}
				}
			}

			Set temp = new HashSet( );

			for ( Iterator it = result.iterator( ); it.hasNext( ); )
			{
				Set newProhibitedSet = new HashSet( );
				newProhibitedSet.addAll( prohibitedSet );
				temp.addAll( getReferencedBindings( (ComputedColumnHandle) it.next( ),
						allHandleList,
						newProhibitedSet ) );
			}

			result.addAll( temp );

		}
		catch ( BirtException e )
		{
		}

		return result;
	}

	/**
	 * Return a list of valid group key bindings. Only those bindings that do
	 * not involve aggregations will be allowed.
	 * 
	 * @param availableHandles
	 * @return
	 */
	public static List getValidGroupKeyBindings( List availableHandles )
	{

		List result = new ArrayList( );
		if ( availableHandles == null )
			return result;
		try
		{
			for ( int i = 0; i < availableHandles.size( ); i++ )
			{
				ComputedColumnHandle handle = (ComputedColumnHandle) availableHandles.get( i );
				List originalNames = new ArrayList( );
				originalNames.add( handle.getName( ) );
				if ( acceptBinding( handle, availableHandles, originalNames ) )
					result.add( handle );
			}
		}
		catch ( Exception e )
		{
			return result;
		}

		return result;

	}

	/**
	 * Get the parameter value from .rptconfig file if it does exist
	 * 
	 * @return the parameter value
	 */
	public static String getParamValue( DataSetHandle dataSetHandle, OdaDataSetParameterHandle paramDefn )
			throws DesignFileException
	{
		ModuleHandle moduleHandle = dataSetHandle.getModuleHandle( );
		String designFileName = moduleHandle.getFileName( );
		// replace the file extension
		String reportConfigName = designFileName.substring( 0,
				designFileName.length( ) - "rptdesign".length( ) ) //$NON-NLS-1$
				+ "rptconfig"; //$NON-NLS-1$
		File file = new File( reportConfigName );
		if ( file.exists( ) )
		{
			String paraName = paramDefn.getParamName( );
			ScalarParameterHandle parameterHandle = ( ScalarParameterHandle )moduleHandle.findParameter( paraName );
			paraName = paraName + "_" + parameterHandle.getID( ); //$NON-NLS-1$
			SessionHandle sessionHandle = new DesignEngine( null ).newSessionHandle( ULocale.US );
			ReportDesignHandle rdHandle = null;
			// Open report config file
			rdHandle = sessionHandle.openDesign( reportConfigName );

			// handle config vars
			if ( rdHandle != null )
			{
				Iterator configVars = rdHandle.configVariablesIterator( );
				while ( configVars != null && configVars.hasNext( ) )
				{
					ConfigVariableHandle configVar = (ConfigVariableHandle) configVars.next( );
					if ( configVar != null )
					{
						String varName = prepareConfigVarName( configVar
								.getName( ) );
						Object varValue = configVar.getValue( );
						if ( varName == null || varValue == null )
						{
							continue;
						}
						if ( varName.equals( paraName ) )
						{
							String value = (String) varValue;
							//if the value actually is in String type, convert it by adding quotation marks
							if( isToBeConverted( parameterHandle.getDataType( ) ) )
							{
								value = "\"" + JavascriptEvalUtil.transformToJsConstants( value )+ "\""; //$NON-NLS-1$ //$NON-NLS-2$
							}
							return value;
						}
						if ( isNullValue( varName,
								(String) varValue,
								paraName ) )
						{
							return null;
						}
					}
				}
			}
		}
		return ExpressionUtil.createJSParameterExpression( ( (OdaDataSetParameterHandle) paramDefn ).getParamName( ) );
	}	
	
	/**
	 * To check whether the object with the specific type should be converted
	 * 
	 * @param type
	 * @return true if should be converted
	 */
	private static boolean isToBeConverted( String type ) 
	{
		return type.equals( DesignChoiceConstants.PARAM_TYPE_STRING )
		|| type.equals( DesignChoiceConstants.PARAM_TYPE_DATETIME )
		|| type.equals( DesignChoiceConstants.PARAM_TYPE_TIME )
		|| type.equals( DesignChoiceConstants.PARAM_TYPE_DATE );
	}

	/**
	 * Delete the last "_" part
	 * 
	 * @param name
	 * @return String
	 */
	private static String prepareConfigVarName( String name )
	{
		int index = name.lastIndexOf( "_" ); //$NON-NLS-1$
		return name.substring( 0, index );
	}
	
	private static boolean isNullValue( String varName, String varValue,
			String newParaName )
	{
		return varName.toLowerCase( ).startsWith( "__isnull" ) //$NON-NLS-1$
				&& varValue.equals( newParaName );
	}
	
	/**
	 * 
	 * @param binding
	 * @param bindings
	 * @param originalNames
	 * @return
	 */
	private static boolean acceptBinding( ComputedColumnHandle binding,
			List bindings, List originalNames )
	{
		try
		{
			if ( binding.getAggregateFunction( ) == null )
			{
				String expr = binding.getExpression( );

				if ( !ExpressionUtil.hasAggregation( expr ) )
				{
					List referredBindings = ExpressionUtil.extractColumnExpressions( expr );
					List names = new ArrayList( );
					names.add( binding.getName( ) );
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
		catch ( BirtException e )
		{
		}
		return false;
	}

	/**
	 * 
	 * @param originalBindingName
	 * @param availableHandles
	 * @param referredBindings
	 * @return
	 */
	private static boolean acceptindirectReferredBindings(
			List originalBindingName, List availableHandles,
			List referredBindings )
	{
		try
		{
			List candidateBindings = new ArrayList( );
			for ( int i = 0; i < referredBindings.size( ); i++ )
			{
				IColumnBinding cb = (IColumnBinding) referredBindings.get( i );
				for ( int j = 0; j < availableHandles.size( ); j++ )
				{
					ComputedColumnHandle binding = (ComputedColumnHandle) availableHandles.get( j );
					if ( originalBindingName.contains( binding.getName( ) ) )
						continue;
					if ( binding.getName( )
							.equals( cb.getResultSetColumnName( ) ) )
						candidateBindings.add( binding );
				}
			}

			for ( int i = 0; i < candidateBindings.size( ); i++ )
			{
				ComputedColumnHandle handle = (ComputedColumnHandle) candidateBindings.get( i );
				if ( !acceptBinding( handle,
						availableHandles,
						originalBindingName ) )
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
