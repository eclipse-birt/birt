package org.eclipse.birt.report.model.api.oda.interfaces;

public interface IAggregationDefn {

	/**
	 * Returns the BIRT predefined aggregation id.
	 * 
	 * @return the BIRT predefined aggregation id.
	 */
	public String getBirtAggregationId();

	/**
	 * Return the display name of the BIRT predefined aggregation.
	 * 
	 * @return display name of the BIRT predefined aggregation.
	 */
	public String getBirtAggregationDisplayName();

	/**
	 * Returns the oda aggregation provider id.
	 * 
	 * @return oda aggregation provider id.
	 */
	public String getProviderExtensionId();

	/**
	 * Returns the oda provider defined aggregation id.
	 * 
	 * @return oda aggregation id.
	 */
	public String getODAAggregationId();

	/**
	 * Returns the oda provider defined aggregation display name.
	 * 
	 * @return oda aggregation display name.
	 */
	public String getODAAggregationDisplayName();

	/**
	 * Returns the minimum number of arguments required by this aggregation
	 * function.
	 * 
	 * @return minimum number of arguments required by this aggregation.
	 */
	public Integer getMinInputVariables();

	/**
	 * Identify if this aggregation function support unlimited arguments.
	 * 
	 * @return true if this aggregation support unlimited arguments,else false.
	 */
	public boolean supportsUnboundedMaxInputVariables();

	/**
	 * Return the max number of arguments that this aggregation function accept.
	 * 
	 * @return
	 */
	public Integer getMaxInputVariables();

	/**
	 * Identify if this aggregation implementation can ignore duplicated values.
	 * 
	 * @return true if this aggregation can ignore duplicated values, else false.
	 */
	public boolean canIgnoreDuplicateValues();

	/**
	 * Identify if this aggregation implementation can ignore null values.
	 * 
	 * @return true if this aggregation can ignore null values, else false.
	 */
	public boolean canIgnoreNullValues();

}
