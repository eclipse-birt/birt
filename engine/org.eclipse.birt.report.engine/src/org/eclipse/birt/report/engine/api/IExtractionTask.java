
package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;

public interface IExtractionTask extends IEngineTask {

	/**
	 * the filter conditions
	 * 
	 * @param filters
	 */
	void setFilters(IFilterDefinition[] filters);

	/**
	 * set sorting conditions
	 * 
	 * @param sorts
	 */
	void setSorts(ISortDefinition[] sorts);

	/**
	 * set sorting conditions
	 * 
	 * @param simpleSortExpression
	 * @param overrideExistingSorts
	 */
	void setSorts(ISortDefinition[] simpleSortExpression, boolean overrideExistingSorts);

	/**
	 * execute this extraction task
	 * 
	 * @return the extraction result
	 * @throws BirtException
	 */
	Object extract() throws BirtException;

	/**
	 * execute this extraction task
	 * 
	 * @param option the extraction option
	 * @throws BirtException
	 */
	void extract(IExtractionOption option) throws BirtException;

}
