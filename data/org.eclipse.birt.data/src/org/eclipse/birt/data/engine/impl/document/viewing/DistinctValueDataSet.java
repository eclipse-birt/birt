package org.eclipse.birt.data.engine.impl.document.viewing;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

public class DistinctValueDataSet implements IDataSetPopulator {
	private IResultClass resultClass;
	private Object[] distinctValues;
	int currentPos;

	public DistinctValueDataSet(IResultClass resultClass, Object[] distinctValues) {
		this.resultClass = resultClass;
		this.distinctValues = distinctValues;
		this.currentPos = -1;
	}

	public IResultObject next() throws DataException {
		currentPos++;
		if (currentPos >= distinctValues.length)
			return null;
		Object[] objs = new Object[1];
		objs[0] = distinctValues[currentPos];
		return new ResultObject(resultClass, objs);
	}

}
