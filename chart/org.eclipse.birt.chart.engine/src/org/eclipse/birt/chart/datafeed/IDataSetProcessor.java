/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.datafeed;

import java.util.List;
import java.util.Locale;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;

import com.ibm.icu.util.ULocale;

/**
 * Provides a generic definition of a data set processor capable of building or
 * scanning through data sets associated with specific series types.
 */
public interface IDataSetProcessor {

	/**
	 * Attempts to parse a line of text representing multiple values of a specific
	 * data element type and updates an existing data set or creates a new data set
	 * filled with these values.
	 * 
	 * @param sDataSetRepresentation A line of text containing a list of data
	 *                               element values that may be parsed.
	 * @param ds                     An existing data set (or null for a new one)
	 *                               that needs to be filled with data elements
	 *                               created by parsing the line of text as per the
	 *                               expected string format.
	 * 
	 * @return The existing data set definition passed in as an argument or a new
	 *         one if the 'ds' argument was null
	 * @throws ChartException
	 */
	public DataSet fromString(String sDataSetRepresentation, DataSet ds) throws ChartException;

	/**
	 * Attempts to format a column of data to string. For example, an array
	 * Object[Double(1), Double(2)] will be converted into String "1,2"
	 * 
	 * @param columnData array of column data. All elements have same class type.
	 * @throws ChartException
	 */
	public String toString(Object[] columnData) throws ChartException;

	/**
	 * @return The expected format of a line of text that will be used by the
	 *         fromString(...) method to parse and create data elements to be
	 *         populated into a data set
	 */
	public String getExpectedStringFormat();

	/**
	 * Populates a data set with data element values retrieved from a result set
	 * created as a result of query execution
	 * 
	 * @param oResultSetDef An instance of a generic result set that is host
	 *                      application specific. For BIRT, the result set class is
	 *                      ResultSetDataSet
	 * @param ds            An existing data set (or null for a new one) that needs
	 *                      to be filled with with data elements extracted from the
	 *                      result set definition
	 * 
	 * @return The existing data set definition passed in as an argument or a new
	 *         one if the 'ds' argument was null
	 * @throws ChartException
	 */
	public DataSet populate(Object oResultSetDef, DataSet ds) throws ChartException;

	/**
	 * Causes implementation specific data set processor classes to compute the
	 * minimum value in a data set of a specific series type.
	 * 
	 * @param ds The data set for which the minimum value needs to be computed
	 * 
	 * @return The minimum value found in the data set
	 * @throws ChartException
	 */
	public Object getMinimum(DataSet ds) throws ChartException;

	/**
	 * Causes implementation specific data set processor classes to compute the
	 * maximum value in a data set of a specific series type.
	 * 
	 * @param ds The data set for which the maximum value needs to be computed
	 * 
	 * @return The maximum value found in the data set
	 * @throws ChartException
	 */
	public Object getMaximum(DataSet ds) throws ChartException;

	/**
	 * Provides the locale to device renderer implementations as needed to retrieve
	 * localized resources for presentation.
	 * 
	 * @return The locale to be used
	 * @deprecated use {@link #getULocale()} instead.
	 * 
	 */
	public Locale getLocale();

	/**
	 * Provides the locale to device renderer implementations as needed to retrieve
	 * localized resources for presentation.
	 * 
	 * @return The locale to be used
	 * @since 2.1
	 */
	public ULocale getULocale();

	/**
	 * Provides a list of data definitions for grouping. The data after evaluating
	 * will be aggregated by aggregation expression.
	 * 
	 * @param series series to get data definitions
	 * @return a list of data definitions for grouping
	 * @since 2.2
	 */
	public List<Query> getDataDefinitionsForGrouping(Series series);
}
