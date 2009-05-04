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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.filterExtension.interfaces.IBirtFilterOperatorConstants;
import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;

/**
 * FilterExprDefinition
 */

public class FilterExprDefinition implements IFilterExprDefinition
{

	/**
	 * Filter operator defined by BIRT.
	 */
	private String birtFilterExprId = null;

	/**
	 * BIRT predefined filter expression operator display name.
	 */
	private String birtFilterDisplayName = null;

	/**
	 * The min number of arguments that this filter operator required.
	 */
	private int minArgs = -1;

	/**
	 * The max number of arguments that this filter operator required.
	 */
	private int maxArgs = -1;

	/**
	 * Indicates if this filter operator expression support unlimited max number
	 * of arguments.
	 */
	private boolean supportUnboundedMaxArgs = false;

	/**
	 * Constructor for FilterExprDefinition by BIRT predefined filter expression
	 * operator id. The instance returned is not mapped to any external ODA
	 * extension filter.
	 * 
	 * @param birtFilterExpr
	 *            BIRT predefined filter expression operator Id.
	 */
	public FilterExprDefinition( String birtFilterExpr )
			throws IllegalArgumentException
	{
		this.birtFilterExprId = birtFilterExpr;

		if ( !OdaFilterExprHelper.birtPredefinedFilters.contains( birtFilterExpr ) )
			throw new IllegalArgumentException( "The Birt filter expression Id is not valid." );

		initBitExpr( birtFilterExpr );
	}

	public int expressionSupportedType( )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public String getBirtFilterExprDisplayName( )
	{
		// TODO Auto-generated method stub
		return this.birtFilterDisplayName;
	}

	public String getBirtFilterExprId( )
	{
		// TODO Auto-generated method stub
		return this.birtFilterExprId;
	}

	public String getExtFilterDisplayName( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getExtFilterExprId( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getMaxArguments( )
	{
		// TODO Auto-generated method stub
		return this.maxArgs;
	}

	public Integer getMinArguments( )
	{
		// TODO Auto-generated method stub
		return this.minArgs;
	}

	public String getProviderExtensionId( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean supportsUnboundedMaxArguments( )
	{
		// TODO Auto-generated method stub
		return this.supportUnboundedMaxArgs;
	}

	private void initBitExpr( String birtExpr )
	{
		int birtOperator = birtExpr.toLowerCase( ).hashCode( );
		
		if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_BETWEEN == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_BETWEEN;
			this.birtFilterDisplayName = "Between";
			this.maxArgs = 2;
			this.minArgs = 2;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_BOTTOM_N == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N;
			this.birtFilterDisplayName = "Botton N";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_BOTTOM_PERCENT == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT;
			this.birtFilterDisplayName = "Botton Percent";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_EQ == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_EQ;
			this.birtFilterDisplayName = "Equal To";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_FALSE == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_FALSE;
			this.birtFilterDisplayName = "Is False";
			this.maxArgs = 0;
			this.minArgs = 0;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_GE == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_GE;
			this.birtFilterDisplayName = "Greater Than or Equal";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_GT == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_GT;
			this.birtFilterDisplayName = "Greater Than";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_LE == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_LE;
			this.birtFilterDisplayName = "Less Than or Equal";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_LIKE == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_LIKE;
			this.birtFilterDisplayName = "Like";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_LT == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_LT;
			this.birtFilterDisplayName = "Less Than";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_NE == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NE;
			this.birtFilterDisplayName = "Not Equal to";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_BETWEEN == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN;
			this.birtFilterDisplayName = "Not Between";
			this.maxArgs = 2;
			this.minArgs = 2;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_IN == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_IN;
			this.birtFilterDisplayName = "Not In";
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = true;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_NULL == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL;
			this.birtFilterDisplayName = "Not Null";
			this.maxArgs = 0;
			this.minArgs = 0;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_NULL == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NULL;
			this.birtFilterDisplayName = "Is Null";
			this.maxArgs = 0;
			this.minArgs = 0;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_TOP_N == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_TOP_N;
			this.birtFilterDisplayName = "Top N";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_TOP_PERCENT == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT;
			this.birtFilterDisplayName = "Top Percent";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_TRUE == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_TRUE;
			this.birtFilterDisplayName = "Is True";
			this.maxArgs = 0;
			this.minArgs = 0;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_MATCH == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_MATCH;
			this.birtFilterDisplayName = "Match";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;

		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_LIKE == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE;
			this.birtFilterDisplayName = "Not Like";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_MATCH == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH;
			this.birtFilterDisplayName = "Not Match";
			this.maxArgs = 1;
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = false;
		}
		else if ( IBirtFilterOperatorConstants.FILTER_OPERATOR_IN == birtOperator )
		{
			this.birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_IN;
			this.birtFilterDisplayName = "In";
			this.minArgs = 1;
			this.supportUnboundedMaxArgs = true;
		}
	}

}
