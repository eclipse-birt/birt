/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.filterExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;

/**
 * OdaFilterExprHelper
 */

public class OdaFilterExprHelper 
{

	/**
	 * the flag to initialize the birt predefined filter operators.
	 */
	private static boolean initBirtExpr = false;

	/**
	 * Map to store the filter expression definition. The key is the BIRT
	 * predefined filter operator id.
	 */
	private static Map filterExprDefMap = new HashMap( );

	/**
	 * The list contains the BIRT predefined filter definitions.
	 */
	private static List<IFilterExprDefinition> filterExprDefList = new ArrayList( );

	/**
	 * BIRT predefined filter expression id.
	 */
	public static Set birtPredefinedFilters = new HashSet( );

	/**
	 * Returns the list of IFilterExprDefinition. If under commercial BIRT, the
	 * list contains both of ODA extension provider registered filter
	 * definitions, and BIRT predefined filter definitions. If under OS BIRT,
	 * the list will only contain the IFilterExprDefinition instance which
	 * represent the BIRT predefined ones.
	 * 
	 *@param odaDatasetExtensionId
	 *            oda datasource extension id.
	 *@param odaDataSourceExtensionId
	 *            oda dataset extension id.
	 * @return List of IFilterExprDefinition instance.
	 */
	public static List<IFilterExprDefinition> getMappedFilterExprDefinitions(
			String dataSetExtId, String dataSourceExtId )
	{

		return filterExprDefList;
	}

	/**
	 * Return the IFilterExprDefinition instance based on the passed in BIRT
	 * predefined Filter expression name. For commercial BIRT, the returned
	 * IFilterExprDefinition will provide the information that mapped to a
	 * corresponding ODA extension Filter if there is one. For OS BIRT, the
	 * returned IFilterExprDefinition will not have any map information to the
	 * ODA extension filters.
	 * 
	 * @param birtFilterExprId
	 *            the BIRT predefined fitler expression id.
	 * @param datasetExtId
	 *            ODA dataset extension id. Null if is for OS BIRT.
	 * @param datasourceExtId
	 *            ODA datasource extension id. Null if is for OS BIRT.
	 * @return Instance of IFilterExprDefinition. IFilterExprDefinition instance
	 *         based on the passed in filter expression id.
	 */
	public static IFilterExprDefinition getFilterExpressionDefn(
			String birtFilterExprId, String datasetExtId, String datasourceExtId )
	{
		return (IFilterExprDefinition) filterExprDefMap.get( birtFilterExprId );
	}

	/**
	 * Indicates if support the ODA extension filter expressions.
	 * 
	 * @return true if support, false if not.
	 */
	public static boolean supportOdaExtensionFilters( )
	{
		return false;
	}

	static
	{
		if ( !initBirtExpr )
		{
			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_EQ );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_EQ );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_FALSE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_FALSE );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_GE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_GE );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_GT );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_GT );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_IN );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_IN );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_LE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_LE );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_LIKE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_LIKE );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_LT );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_LT );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_MATCH );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_MATCH );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_NE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NE );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_IN );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_IN );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_NULL );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NULL );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_TOP_N );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_TOP_N );

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT);

			birtPredefinedFilters.add( DesignChoiceConstants.FILTER_OPERATOR_TRUE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_TRUE);

			initBirtExpr = true;
		}
	}

	private static void addToList( String key )
	{
		IFilterExprDefinition fed = new FilterExprDefinition( key );
		filterExprDefMap.put( key, fed );
		filterExprDefList.add( fed );
	}
}
