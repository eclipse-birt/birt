package org.eclipse.birt.data.engine.olap.api.query;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;

public interface IDerivedMeasureDefinition extends IMeasureDefinition {
	/**
	 * Return the expression of the derived measure.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IBaseExpression getExpression() throws DataException;

}
