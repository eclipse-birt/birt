package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents a the design of a DataSet in the scripting environment
 */
public interface IDataSet extends IReportElement
{

	/**
	 * Returns the name of the data source for this data set.
	 * 
	 * @return the data source name as a string
	 * 
	 * @see #setDataSource(String)
	 */
	String getDataSourceName( );

	/**
	 * Sets the name of the data source for this data set. This method checks
	 * whether the data source name exists in the report design.
	 * 
	 * @param name
	 *            the data source name
	 * @throws SemanticException
	 *             if the data source does not exist in the report design, or
	 *             the property if locked.
	 * @see #getDataSource()
	 */
	void setDataSource( String name ) throws SemanticException;

}