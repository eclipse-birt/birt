/**
 * 
 */
package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 *
 */

public interface IMultiRowItem extends IReportItem {

	/**
	 * Adds filter condition.expr of IFilterCondition is required.
	 * 
	 * @param condition
	 * @throws SemanticException
	 */

	void addFilterCondition(IFilterCondition condition) throws SemanticException;

	/**
	 * Adds sort condition.key of ISortCondition is required.
	 * 
	 * @param condition
	 * 
	 * @throws SemanticException
	 */

	void addSortCondition(ISortCondition condition) throws SemanticException;

	/**
	 * Returns all filter conditions
	 * 
	 * @return all filter conditions
	 */

	IFilterCondition[] getFilterConditions();

	/**
	 * Returns all sort conditions.
	 * 
	 * @return all sort conditions.
	 */

	ISortCondition[] getSortConditions();

	/**
	 * Removes filter condition.
	 * 
	 * @param condition
	 * @throws SemanticException
	 */

	void removeFilterCondition(IFilterCondition condition) throws SemanticException;

	/**
	 * Removes filter condition.
	 * 
	 * @throws SemanticException
	 */

	void removeFilterConditions() throws SemanticException;

	/**
	 * Removes sort condition.
	 * 
	 * @param condition
	 * @throws SemanticException
	 */

	void removeSortCondition(ISortCondition condition) throws SemanticException;

	/**
	 * Removes all sort conditions
	 * 
	 * @throws SemanticException
	 */

	void removeSortConditions() throws SemanticException;

}
