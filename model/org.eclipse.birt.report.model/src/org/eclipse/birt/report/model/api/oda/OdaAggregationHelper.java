/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.model.api.oda;

import java.util.List;

import org.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn;

public class OdaAggregationHelper extends OdaAggregationHelperImpl {

	public static List<IAggregationDefn> getAggregationDefinitions(String dataSetExtId, String dataSourceExtId) {
		return birtAggregationDefinitions;
	}

	public static IAggregationDefn getAggregationDefn(String birtAggregationId, String datasetExtId,
			String datasourceExtId) {

		if (!birtPredefinedAggregationConstants.contains(birtAggregationId)) {
			throw new IllegalArgumentException("The Birt filter expression Id is not valid.");
		}

		List aggregationDefns = birtAggregationDefinitions;
		if (aggregationDefns.size() > 0) {
			for (int i = 0; i < aggregationDefns.size(); i++) {
				IAggregationDefn fed = (IAggregationDefn) aggregationDefns.get(i);
				if (fed.getBirtAggregationId().equals(birtAggregationId)) {
					return fed;
				}
			}
		}
		return new AggregationDefn(birtAggregationId);
	}

}
