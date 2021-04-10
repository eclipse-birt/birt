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

package org.eclipse.birt.report.model.api.filterExtension.interfaces;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * IBirtFilterOperatorConstants
 */

public interface IBirtFilterOperatorConstants {

	int FILTER_OPERATOR_EQ = DesignChoiceConstants.FILTER_OPERATOR_EQ.toLowerCase().hashCode();

	int FILTER_OPERATOR_NE = DesignChoiceConstants.FILTER_OPERATOR_NE.toLowerCase().hashCode();

	int FILTER_OPERATOR_LT = DesignChoiceConstants.FILTER_OPERATOR_LT.toLowerCase().hashCode();

	int FILTER_OPERATOR_LE = DesignChoiceConstants.FILTER_OPERATOR_LE.toLowerCase().hashCode();

	int FILTER_OPERATOR_GE = DesignChoiceConstants.FILTER_OPERATOR_GE.toLowerCase().hashCode();

	int FILTER_OPERATOR_GT = DesignChoiceConstants.FILTER_OPERATOR_GT.toLowerCase().hashCode();

	int FILTER_OPERATOR_BETWEEN = DesignChoiceConstants.FILTER_OPERATOR_BETWEEN.toLowerCase().hashCode();

	int FILTER_OPERATOR_NOT_BETWEEN = DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN.toLowerCase().hashCode();

	int FILTER_OPERATOR_NULL = DesignChoiceConstants.FILTER_OPERATOR_NULL.toLowerCase().hashCode();

	int FILTER_OPERATOR_NOT_NULL = DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL.toLowerCase().hashCode();

	int FILTER_OPERATOR_TRUE = DesignChoiceConstants.FILTER_OPERATOR_TRUE.toLowerCase().hashCode();

	int FILTER_OPERATOR_FALSE = DesignChoiceConstants.FILTER_OPERATOR_FALSE.toLowerCase().hashCode();

	int FILTER_OPERATOR_LIKE = DesignChoiceConstants.FILTER_OPERATOR_LIKE.toLowerCase().hashCode();

	int FILTER_OPERATOR_TOP_N = DesignChoiceConstants.FILTER_OPERATOR_TOP_N.toLowerCase().hashCode();

	int FILTER_OPERATOR_BOTTOM_N = DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N.toLowerCase().hashCode();

	int FILTER_OPERATOR_TOP_PERCENT = DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT.toLowerCase().hashCode();

	int FILTER_OPERATOR_BOTTOM_PERCENT = DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT.toLowerCase().hashCode();

	int FILTER_OPERATOR_NOT_IN = DesignChoiceConstants.FILTER_OPERATOR_NOT_IN.toLowerCase().hashCode();

	int FILTER_OPERATOR_MATCH = DesignChoiceConstants.FILTER_OPERATOR_MATCH.toLowerCase().hashCode();

	int FILTER_OPERATOR_NOT_LIKE = DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE.toLowerCase().hashCode();

	int FILTER_OPERATOR_NOT_MATCH = DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH.toLowerCase().hashCode();

	int FILTER_OPERATOR_IN = DesignChoiceConstants.FILTER_OPERATOR_IN.toLowerCase().hashCode();
}
