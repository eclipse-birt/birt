/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.report.engine.api.DataSetID;

public interface IBaseResultSet {

	static int QUERY_RESULTSET = 0;
	static int CUBE_RESULTSET = 1;

	int getType();

	IBaseQueryResults getQueryResults();

	/**
	 * return the Raw ID
	 * 
	 * @return
	 */
	String getRawID() throws BirtException;

	DataSetID getID();

	IBaseResultSet getParent();

	public Object evaluate(String expr) throws BirtException;

	/**
	 * evaluate the expression with specified script language.
	 * 
	 * @param language
	 * @param expr
	 * @return
	 * @throws BirtException
	 */
	public Object evaluate(String language, String expr) throws BirtException;

	public Object evaluate(IBaseExpression expr) throws BirtException;

	void close();
}
