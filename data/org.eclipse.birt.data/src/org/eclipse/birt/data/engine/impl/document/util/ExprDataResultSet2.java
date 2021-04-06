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
 * This class will offer the same function as ExprDataResultSet1, but there is a
 * little difference is that not all expression rows in report document is
 * valid, and then there is a new input stream of row information input stream
 * which will prvoide the valid index information.
 */
public class ExprDataResultSet2 extends BaseExprDataResultSet {
	private RAInputStream rowIs;
	private RAInputStream rowLenIs;
	private RAInputStream rowInfoIs;

	/**
	 * @param rowIs,       the input stream for expression row
	 * @param rowLenIs,    the input stream for expression row length
	 * @param rowInfoIs,   the input stream for valid row index
	 * @param inExprMetas, the expression meta data
	 * @throws DataException
	 */
	public ExprDataResultSet2(String tempDir, RAInputStream rowIs, RAInputStream rowLenIs, RAInputStream rowInfoIs,
			ExprMetaInfo[] inExprMetas, int version, IDataSetResultSet dataSetResultSet) throws DataException {
		this.rowIs = rowIs;
		this.rowLenIs = rowLenIs;
		this.rowInfoIs = rowInfoIs;

		IExprDataReader exprDataReader = new ExprDataReader2(tempDir, rowIs, rowLenIs, rowInfoIs, version,
				dataSetResultSet);
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
			if (rowIs != null) {
				rowIs.close();
				rowLenIs.close();
				rowIs = null;
			}
			if (rowInfoIs != null) {
				rowInfoIs.close();
			}
		} catch (IOException e) {
			// ignore
		}
	}

}
