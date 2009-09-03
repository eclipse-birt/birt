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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;

/**
 * OdaFilterExprHelper
 */

class OdaFilterExprHelperImpl
{

	/**
	 * The flag to initialize the birt predefined filter operators.
	 */
	private static boolean initBirtExpr = false;

	/**
	 * BIRT predefined filter expression id.
	 */
	protected static Set birtPredefinedFilterConstants = new HashSet( );

	/**
	 * The list contains the BIRT predefined filter definitions.
	 */
	protected static List<IFilterExprDefinition> birtFilterExprDefList = new ArrayList( );

	static
	{
		if ( !initBirtExpr )
		{
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_EQ );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_EQ );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_FALSE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_FALSE );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_GE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_GE );

			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_GT );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_GT );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_IN );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_IN );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_LE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_LE );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_LIKE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_LIKE );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_LT );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_LT );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_MATCH );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_MATCH );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_NE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NE );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_IN );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_IN );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_NULL );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_NULL );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_TOP_N );
			addToList( DesignChoiceConstants.FILTER_OPERATOR_TOP_N );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT );

			addToList( DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT );
			birtPredefinedFilterConstants
					.add( DesignChoiceConstants.FILTER_OPERATOR_TRUE );

			addToList( DesignChoiceConstants.FILTER_OPERATOR_TRUE );

			initBirtExpr = true;
		}
	}

	private static void addToList( String key )
	{
		IFilterExprDefinition fed = new FilterExprDefinition( key );
		birtFilterExprDefList.add( fed );
	}
}
