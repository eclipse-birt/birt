
package org.eclipse.birt.chart.aggregate;

/**
 * This interface defines the extension interface for all chart aggregate
 * functions.
 */
public interface IAggregateFunction
{

	static final int UNKNOWN = 0;

	static final int NULL = 1;

	static final int DATE = 2;

	static final int CALENDAR = 3;

	static final int NUMBER = 4;

	static final int BIGDECIMAL = 5;

	static final int TEXT = 6;

	static final int CUSTOM = 7;

	/**
	 * An internally generated notification indicating that a function
	 * implementer should accumulate another value (to be subsequently
	 * aggregated)
	 * 
	 * @param oValue
	 *            The numeric value to be accumulated
	 */
	public void accumulate( Object oValue ) throws IllegalArgumentException;

	/**
	 * Returns the aggregated value as determined by the function
	 * implementation.
	 * 
	 * @return The aggregated value as determined by the function
	 *         implementation.
	 */
	public Object getAggregatedValue( );

	/**
	 * Sends out a notification to a function implementation subclass to
	 * initialize local member variables.
	 */
	public void initialize( );

}