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

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;

public interface IQueryResultSet extends IBaseResultSet {

	IResultIterator getResultIterator();

	long getRowIndex();

	boolean skipTo(long rowIndex) throws BirtException;

	boolean next() throws BirtException;

	String getGroupId(int groupLevel);

	/**
	 * Returns the 1-based index of the outermost group in which the current row is
	 * the last row. For example, if a query contain N groups (group with index 1
	 * being the outermost group, and group with index N being the innermost group),
	 * and this function returns a value M, it indicates that the current row is the
	 * last row in groups with indexes (M, M+1, ..., N ). -1 represents current row
	 * is a detail row. 0 represents the end of whole resultset
	 * 
	 * @return 1-based index of the outermost group in which the current row is the
	 *         first row; (N+1) if the current row is not at the start of any group;
	 *         0 if the result set has no groups.
	 */
	int getStartingGroupLevel() throws BirtException;

	/**
	 * Returns the 1-based index of the outermost group in which the current row is
	 * the first row. For example, if a query contain N groups (group with index 1
	 * being the outermost group, and group with index N being the innermost group),
	 * and this function returns a value M, it indicates that the current row is the
	 * first row in groups with indexes (M, M+1, ..., N ).
	 * 
	 * @return 1-based index of the outermost group in which the current row is the
	 *         first row; (N+1) if the current row is not at the start of any group;
	 *         0 if the result set has no groups.
	 */
	int getEndingGroupLevel() throws BirtException;

	/**
	 * Returns the metadata of this result set's detail row.
	 * 
	 * @return The result metadata of a detail row.
	 */
	public IResultMetaData getResultMetaData() throws BirtException;

	/**
	 * Returns the value of a bound column. Currently it is only a dummy
	 * implementation.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public Object getValue(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the Boolean data type. Currently it is
	 * only a dummy implementation.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public Boolean getBoolean(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the Integer data type. Currently it is
	 * only a dummy implementation.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public Integer getInteger(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the Double data type. Currently it is
	 * only a dummy implementation.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public Double getDouble(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the String data type. Currently it is
	 * only a dummy implementation.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public String getString(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the BigDecimal data type. Currently it
	 * is only a dummy implementation.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public BigDecimal getBigDecimal(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the Date data type. Currently it is
	 * only a dummy implementation.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public Date getDate(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the Blob data type. Currently it is
	 * only a dummy implementation.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public Blob getBlob(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the byte[] data type. Currently it is
	 * only a dummy implementation.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public byte[] getBytes(String name) throws BirtException;

	/**
	 * Judges if the IResultSet is empty or not.
	 * 
	 * @return true if IResultSet is empty. false if it is not empty.
	 */
	boolean isEmpty() throws BirtException;

	/**
	 * Judges if the IResultSet is on the first row.
	 * 
	 * @return true if IResultSet is on the first row.
	 * @throws BirtException
	 */
	boolean isFirst() throws BirtException;

	/**
	 * Judges if the IResultSet's cursor on before the first row.
	 * 
	 * @return true if the cursor is before the first row.
	 * @throws BirtException
	 */
	boolean isBeforeFirst() throws BirtException;
}
