/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.document;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.impl.document.util.IExprDataResultSet;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * The populator class for summary result set.
 * 
 */
public class SummaryDataSetPopulator implements IDataSetPopulator {
	private org.eclipse.birt.data.engine.impl.document.ResultIterator docIt;
	private IResultClass resultClass;

	public SummaryDataSetPopulator(IQueryDefinition queryDefn,
			org.eclipse.birt.data.engine.impl.document.ResultIterator docIt, IExprDataResultSet exprResultSet)
			throws DataException {
		this.docIt = docIt;

		this.resultClass = exprResultSet.getResultClass();
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSetPopulator#next()
	 */
	public IResultObject next() throws DataException {
		if (!this.docIt.next())
			return null;

		Object[] field = new Object[this.resultClass.getFieldCount()];

		for (int i = 0; i < field.length; i++) {
			String columnName = this.resultClass.getFieldName(i + 1);

			try {
				if (ExprMetaUtil.POS_NAME.equals(columnName)) {
					field[i] = this.docIt.getRowId();
				} else
					field[i] = this.docIt.getValue(columnName);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}

		return new ResultObject(this.resultClass, field);
	}

	public IResultClass getResultClass() {
		return this.resultClass;
	}
}
