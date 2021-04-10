package org.eclipse.birt.report.model.api.oda;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn;
import org.eclipse.birt.report.model.api.oda.interfaces.IBirtAggregationConstants;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

public class AggregationDefn implements IAggregationDefn {

	/**
	 * The initial value for max/min arguments.
	 */
	protected static final int UNDEFINED = -1;

	/**
	 * BIRT aggregation function id.
	 */
	protected String birtAggregationId = null;

	/**
	 * BIRT aggregation display name.
	 */
	protected String birtAggregationDisplayName = null;

	/**
	 * The minimum number of input variable for this aggregation function.
	 */
	protected int minInputVar = UNDEFINED;

	/**
	 * The max number of input variable for this aggregation function.
	 */
	protected int maxInputVar = UNDEFINED;

	/**
	 * Indicate if this aggregation support unlimited number of input variables.
	 */
	protected boolean supportsUnboundedMaxInputVar = false;

	/**
	 * Indicate if this aggregation can ignore duplicated values.
	 */
	protected boolean canIgnoreDuplicateValues = false;

	/**
	 * Indicate if this aggregation can ignore null values.
	 */
	protected boolean canIgnoreNullValues = false;

	private static Logger logger = Logger.getLogger(AggregationDefn.class.getName());

	AggregationDefn() {
	}

	/**
	 * Construct aggregation definition based on BIRT predefined aggregation id.
	 * 
	 * @param birtAggregationId birt predefined aggregation id.
	 * @throws IllegalArgumentException exception when the passed in birt
	 *                                  aggregation id is not predefined.
	 */
	public AggregationDefn(String birtAggregationId) throws IllegalArgumentException {

		this.birtAggregationId = birtAggregationId;
		if (!OdaAggregationHelper.birtPredefinedAggregationConstants.contains(birtAggregationId))
			throw new IllegalArgumentException("The Birt filter expression Id" + birtAggregationId + " is not valid.");
		initBirtAggregation(birtAggregationId.toLowerCase().hashCode(), birtAggregationId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * canIgnoreDuplicateValues()
	 */
	public boolean canIgnoreDuplicateValues() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * canIgnoreNullValues()
	 */
	public boolean canIgnoreNullValues() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * getBirtAggregationDisplayName()
	 */
	public String getBirtAggregationDisplayName() {
		return this.birtAggregationDisplayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * getBirtAggregationId()
	 */
	public String getBirtAggregationId() {

		return this.birtAggregationId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * getMaxInputVariables()
	 */
	public Integer getMaxInputVariables() {

		if (maxInputVar == UNDEFINED)
			return null;

		return this.maxInputVar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * getMinInputVariables()
	 */
	public Integer getMinInputVariables() {

		if (minInputVar == UNDEFINED)
			return null;

		return this.minInputVar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * getODAAggregationDisplayName()
	 */
	public String getODAAggregationDisplayName() {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * getODAAggregationId()
	 */
	public String getODAAggregationId() {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * getProviderExtensionId()
	 */
	public String getProviderExtensionId() {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.oda.interfaces.IAggregationDefn#
	 * supportsUnboundedMaxInputVariables()
	 */
	public boolean supportsUnboundedMaxInputVariables() {

		return false;
	}

	protected void initBirtAggregation(int aggregationId, String id) {

		if (IBirtAggregationConstants.AGGREGATION_FUNCTION_AVERAGE == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_COUNT == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_COUNTDISTINCT == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_FIRST == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_IRR == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_IRR;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_IS_TOP_N == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_LAST == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_LAST;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_MAX == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_MAX;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_MEDIAN == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_MEDIAN;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_MIN == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_MIN;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_MIRR == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_MIRR;
			minInputVar = 3;
			maxInputVar = 3;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_MODE == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_MODE;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_MOVINGAVE == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_MOVINGAVE;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_NPV == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_NPV;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_PERCENT_RANK == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_RANK;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_PERCENT_SUM == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_SUM;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_PERCENTILE == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENTILE;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_RANK == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_RANK;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_RUNNINGNPV == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGNPV;
			minInputVar = 2;
			maxInputVar = 2;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_RUNNINGSUM == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGSUM;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_STDDEV == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_STDDEV;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_SUM == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_SUM;
			minInputVar = 1;
			maxInputVar = 1;
		}

		else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_TOP_QUARTILE == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_TOP_QUARTILE;
			minInputVar = 1;
			maxInputVar = 1;

		} else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_VARIANCE == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_VARIANCE;
			minInputVar = 1;
			maxInputVar = 1;
		}

		else if (IBirtAggregationConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG == aggregationId) {

			birtAggregationId = DesignChoiceConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG;
			minInputVar = 2;
			maxInputVar = 2;

		} else {

			logger.log(Level.SEVERE, "The Birt filter expression Id: " + id + " is not valid.");

		}
		// throw new IllegalArgumentException("The Birt filter expression Id"
		// + id + "is not valid.");

		if (birtAggregationId != null)
			birtAggregationDisplayName = getAggregationDisplayName(birtAggregationId);

	}

	/**
	 * @param aggregationId
	 * @return
	 */
	private String getAggregationDisplayName(String aggregationId) {
		IChoiceSet allowedChoices = MetaDataDictionary.getInstance()
				.getChoiceSet(DesignChoiceConstants.CHOICE_AGGREGATION_FUNCTION);

		assert allowedChoices != null;

		IChoice choice = allowedChoices.findChoice(birtAggregationId);
		if (choice != null)
			return choice.getDisplayName();

		return null;
	}

}
