
package org.eclipse.birt.data.engine.olap.util.filter;

import org.eclipse.birt.data.engine.core.DataException;

public interface IJSDimensionFilterHelper extends IJSFilterHelper
{

	/**
	 * This method is used to evaluate the filter expression.
	 *  
	 * @param expr
	 * @param resultRow
	 * @return
	 * @throws DataException
	 */
	public boolean evaluateFilter( IResultRow resultRow )
			throws DataException;
}