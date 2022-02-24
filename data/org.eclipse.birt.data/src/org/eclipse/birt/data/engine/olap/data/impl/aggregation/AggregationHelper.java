
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;

/**
 * 
 */

public class AggregationHelper {
	private static AggregationHelper instance = new AggregationHelper();

	public static AggregationHelper getInstance() {
		return instance;
	}

	private static boolean isRunningFunction(AggregationDefinition aggrDef) throws DataException {
		AggregationFunctionDefinition[] aggregationFunction = aggrDef.getAggregationFunctions();

		if (aggregationFunction != null) {
			IAggrFunction aggregation = AggregationManager.getInstance()
					.getAggregation(aggregationFunction[0].getFunctionName());
			if (aggregation == null) {
				throw new DataException(
						DataResourceHandle.getInstance().getMessage(ResourceConstants.UNSUPPORTED_FUNCTION)
								+ aggregationFunction[0].getFunctionName());
			}
			if (aggregation.getType() == IAggrFunction.RUNNING_AGGR) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static IAggregationResultSet[] execute(IAggregationResultSet aggrResultSet,
			AggregationDefinition[] aggregations, StopSign stopSign) throws IOException, DataException {
		IAggregationResultSet[] resultSets = new IAggregationResultSet[aggregations.length];
		IAggregationCalculator aggregationCalculator;
		for (int i = 0; i < aggregations.length; i++) {
			if (isRunningFunction(aggregations[i])) {
				aggregationCalculator = new RunningFunctionCalculator(aggregations[i], aggrResultSet);
			} else {
				aggregationCalculator = new SimpleFunctionCalculator(aggregations[i], aggrResultSet);
			}
			resultSets[i] = aggregationCalculator.execute(stopSign);
		}
		return resultSets;
	}

}
