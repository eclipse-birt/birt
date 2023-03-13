/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.filterExtension;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.filterExtension.interfaces.IBirtFilterOperatorConstants;
import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

import com.ibm.icu.util.ULocale;

/**
 * FilterExprDefinition
 */

public class FilterExprDefinition implements IFilterExprDefinition {

	protected static final int UNDEFINED = -1;

	/**
	 * Filter operator defined by BIRT.
	 */
	protected String birtFilterExprId = null;

	/**
	 * BIRT predefined filter expression operator display name.
	 */
	protected String birtFilterDisplayName = null;

	/**
	 * The min number of arguments that this filter operator required.
	 */
	protected int minArgs = UNDEFINED;

	/**
	 * The max number of arguments that this filter operator required.
	 */
	protected int maxArgs = UNDEFINED;

	/**
	 * Indicates if this filter operator expression support unlimited max number of
	 * arguments.
	 */
	protected boolean supportUnboundedMaxArgs = false;

	public FilterExprDefinition() {
	}

	/**
	 * Constructor for FilterExprDefinition by BIRT predefined filter expression
	 * operator id. The instance returned is not mapped to any external ODA
	 * extension filter.
	 *
	 * @param birtFilterExpr BIRT predefined filter expression operator Id.
	 */
	public FilterExprDefinition(String birtFilterExpr) throws IllegalArgumentException {
		birtFilterExprId = birtFilterExpr;
		initBirtExpr(birtFilterExpr.toLowerCase().hashCode());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#expressionSupportedType()
	 */
	@Override
	public int expressionSupportedType() {
		return BIRT_SUPPORT_ONLY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#getBirtFilterExprId()
	 */
	@Override
	public String getBirtFilterExprId() {
		return birtFilterExprId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#getBirtFilterExprDisplayName()
	 */
	@Override
	public String getBirtFilterExprDisplayName() {
		return birtFilterDisplayName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#getExtFilterDisplayName()
	 */
	@Override
	public String getExtFilterDisplayName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#getExtFilterExprId()
	 */
	@Override
	public String getExtFilterExprId() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#getMaxArguments()
	 */
	@Override
	public Integer getMaxArguments() {
		if (maxArgs == UNDEFINED) {
			return null;
		}

		return maxArgs;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#getMinArguments()
	 */
	@Override
	public Integer getMinArguments() {
		if (minArgs == UNDEFINED) {
			return null;
		}

		return minArgs;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#getProviderExtensionId()
	 */
	@Override
	public String getProviderExtensionId() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#supportsUnboundedMaxArguments()
	 */
	@Override
	public boolean supportsUnboundedMaxArguments() {
		return supportUnboundedMaxArgs;
	}

	protected void initBirtExpr(int birtOperator) {
		if (IBirtFilterOperatorConstants.FILTER_OPERATOR_BETWEEN == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_BETWEEN;
			maxArgs = 2;
			minArgs = 2;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_BOTTOM_N == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_BOTTOM_PERCENT == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_EQ == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_EQ;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_FALSE == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_FALSE;
			maxArgs = 0;
			minArgs = 0;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_GE == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_GE;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_GT == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_GT;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_LE == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_LE;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_LIKE == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_LIKE;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_LT == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_LT;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_NE == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NE;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_BETWEEN == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN;
			maxArgs = 2;
			minArgs = 2;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_IN == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_IN;
			minArgs = 1;
			supportUnboundedMaxArgs = true;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_NULL == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL;
			maxArgs = 0;
			minArgs = 0;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_NULL == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NULL;
			maxArgs = 0;
			minArgs = 0;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_TOP_N == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_TOP_N;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_TOP_PERCENT == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_TRUE == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_TRUE;
			maxArgs = 0;
			minArgs = 0;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_MATCH == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_MATCH;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;

		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_LIKE == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_NOT_MATCH == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH;
			maxArgs = 1;
			minArgs = 1;
			supportUnboundedMaxArgs = false;
		} else if (IBirtFilterOperatorConstants.FILTER_OPERATOR_IN == birtOperator) {
			birtFilterExprId = DesignChoiceConstants.FILTER_OPERATOR_IN;
			minArgs = 1;
			supportUnboundedMaxArgs = true;
		} else {
			throw new IllegalArgumentException("The Birt filter expression Id is not valid.");
		}

		if (birtFilterExprId != null) {
			birtFilterDisplayName = getOperatorDisplayName(birtFilterExprId);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#isNegatedExtExprId()
	 */

	@Override
	public boolean isNegatedExtExprId() {
		// the default value is false.
		return false;
	}

	/**
	 * Finds the display name for the given operator.
	 *
	 * @param operator the operator name
	 * @return the display name
	 */

	private String getOperatorDisplayName(String operator) {
		IChoiceSet allowedChoices = MetaDataDictionary.getInstance()
				.getChoiceSet(DesignChoiceConstants.CHOICE_FILTER_OPERATOR);

		assert allowedChoices != null;

		IChoice choice = allowedChoices.findChoice(operator);
		if (choice != null) {
			return choice.getDisplayName();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition #getBirtFilterExprDisplayName(com.ibm.icu.util.ULocale)
	 */

	@Override
	public String getBirtFilterExprDisplayName(ULocale locale) {
		IChoiceSet allowedChoices = MetaDataDictionary.getInstance()
				.getChoiceSet(DesignChoiceConstants.CHOICE_FILTER_OPERATOR);

		assert allowedChoices != null;

		IChoice choice = allowedChoices.findChoice(birtFilterExprId);
		if (choice != null) {
			return choice.getDisplayName(locale);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IFilterExprDefinition#supportsAPIDataType(int)
	 */

	@Override
	public boolean supportsAPIDataType(int apiDataType) {
		return true;
	}

}
