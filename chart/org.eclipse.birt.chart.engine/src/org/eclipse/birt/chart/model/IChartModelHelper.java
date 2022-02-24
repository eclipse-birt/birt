/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.model;

import java.util.List;

import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.emf.common.util.EList;

/**
 * This helper class is used to update chart model or return model properties.
 */

public interface IChartModelHelper {

	/**
	 * Updates extended properties during model and UI initialization
	 * 
	 * @param properties
	 */
	void updateExtendedProperties(EList<ExtendedProperty> properties);

	/**
	 * Gets built-in extended properties which can't be removed in UI.
	 * 
	 * @return extended properties which can't be removed in UI
	 */
	List<String> getBuiltInExtendedProperties();

	/**
	 * Create an instance of ExpressionCodec.
	 * 
	 * @return the ExpressionCodec
	 */
	ExpressionCodec createExpressionCodec();

}
