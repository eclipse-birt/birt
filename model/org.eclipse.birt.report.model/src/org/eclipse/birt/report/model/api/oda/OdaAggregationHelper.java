package org.eclipse.birt.report.model.api.oda;

import java.util.List;

import org.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn;

public class OdaAggregationHelper extends OdaAggregationHelperImpl {

	public static List<IAggregationDefn> getAggregationDefinitions(String dataSetExtId, String dataSourceExtId) {
		return birtAggregationDefinitions;
	}

	public static IAggregationDefn getAggregationDefn(String birtAggregationId, String datasetExtId,
			String datasourceExtId) {

		if (!birtPredefinedAggregationConstants.contains(birtAggregationId))
			throw new IllegalArgumentException("The Birt filter expression Id is not valid.");

		List aggregationDefns = birtAggregationDefinitions;
		if (aggregationDefns.size() > 0) {
			for (int i = 0; i < aggregationDefns.size(); i++) {
				IAggregationDefn fed = (IAggregationDefn) aggregationDefns.get(i);
				if (fed.getBirtAggregationId().equals(birtAggregationId))
					return fed;
			}
		}
		return new AggregationDefn(birtAggregationId);
	}

}
