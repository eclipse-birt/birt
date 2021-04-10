package org.eclipse.birt.report.model.api.oda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn;

public class OdaAggregationHelperImpl {

	private static boolean initBirtAggregation = true;

	protected final static Set birtPredefinedAggregationConstants = new HashSet();

	protected final static List<IAggregationDefn> birtAggregationDefinitions = new ArrayList();

	static {

		if (initBirtAggregation) {

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_FIRST);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_FIRST);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_IRR);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_IRR);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_LAST);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_LAST);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_MAX);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_MAX);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_MEDIAN);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_MEDIAN);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_MIN);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_MIN);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_MIRR);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_MIRR);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_MODE);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_MODE);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_MOVINGAVE);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_MOVINGAVE);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_NPV);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_NPV);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_RANK);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_RANK);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_SUM);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_SUM);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENTILE);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENTILE);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_RANK);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_RANK);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGNPV);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGNPV);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGSUM);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGSUM);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_STDDEV);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_STDDEV);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_SUM);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_SUM);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_TOP_QUARTILE);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_TOP_QUARTILE);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_VARIANCE);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_VARIANCE);

			birtPredefinedAggregationConstants.add(DesignChoiceConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG);
			addToList(DesignChoiceConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG);

			initBirtAggregation = false;
		}
	}

	private static void addToList(String key) {

		IAggregationDefn aggDef = new AggregationDefn(key);
		birtAggregationDefinitions.add(aggDef);
	}
}
