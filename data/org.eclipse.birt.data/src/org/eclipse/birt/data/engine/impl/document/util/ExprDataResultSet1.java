/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.document.util;

import java.io.IOException;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaInfo;
import org.eclipse.birt.data.engine.impl.document.viewing.IDataSetResultSet;

/**
 * The raw result set which will retrieve the raw data of expression value from
 * the report document. This class is used when the query is running based on a
 * first created report document, which has such a characteristic that all
 * expression rows are valid row, and then there is no row index information.
 */
public class ExprDataResultSet1 extends BaseExprDataResultSet {
	private RAInputStream rowRAIs;

	/**
	 * @param rowIs,       the input stream for expression row
	 * @param inExprMetas, the expression meta data
	 * @throws DataException
	 */
	public ExprDataResultSet1(RAInputStream rowRAIs, ExprMetaInfo[] inExprMetas, int version, IDataSetResultSet dsRSet)
			throws DataException {
		this.rowRAIs = rowRAIs;
		IExprDataReader exprDataReader = new ExprDataReader1(rowRAIs, null, version, dsRSet);
		this.rowCount = exprDataReader.getCount();

		super.init(inExprMetas, exprDataReader);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.viewing.IExprDataResultSet#close()
	 */
	public void close() {
		super.close();

		try {
			if (rowRAIs != null) {
				rowRAIs.close();
				rowRAIs = null;
			}
		} catch (IOException e) {
			// ignore
		}
	}

}
