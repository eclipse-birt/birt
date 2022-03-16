/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
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

	@Override
	public IResultObject next() throws DataException {
		currentPos++;
		if (currentPos >= distinctValues.length) {
			return null;
		}
		Object[] objs = new Object[1];
		objs[0] = distinctValues[currentPos];
		return new ResultObject(resultClass, objs);
	}

}
