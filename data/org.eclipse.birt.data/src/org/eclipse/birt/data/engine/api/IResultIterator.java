/*
 *************************************************************************
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;

/**
 * An iterator on a result set from a prepared and executed query. Multiple
 * <code>IResultIterator</code> objects could be associated with the same
 * <code>IQueryResults</code> object, such as in the case of an ODA data set
 * capable of producing multiple result sets.
 */
public interface IResultIterator extends IBaseResultIterator {
	/**
	 * Returns the {@link org.eclipse.birt.data.engine.api.IQueryResults} from which
	 * this result iterator is obtained. If this iterator is that of a subquery,
	 * null is returned.
	 */
	IQueryResults getQueryResults();

	/**
	 * Returns the JavaScript scope associated with this result iterator. All
	 * JavaScript result objects, e.g. rows, row, specific to this result set are
	 * defined within this scope. The returned scope is the same as the one passed
	 * to <code>IPreparedQuery.excute()</code> which produced this iterator's
	 * <code>IQueryResults</code>.
	 *
	 * @return The JavaScript scope associated to this result iterator.
	 */
	Scriptable getScope();

	/**
	 * Each row has its an ID associated with it, and this id will never be changed
	 * no matter when the query is running against a data set or against a report
	 * document.
	 *
	 * @since 2.1
	 * @return row id of current row
	 * @throws BirtException if error occurs in Data Engine
	 */
	int getRowId() throws BirtException;

	/**
	 * Returns the value of a bound column as the Boolean data type. Currently it is
	 * only a dummy implementation.
	 *
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	Boolean getBoolean(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the Integer data type. Currently it is
	 * only a dummy implementation.
	 *
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	Integer getInteger(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the Double data type. Currently it is
	 * only a dummy implementation.
	 *
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	Double getDouble(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the String data type. Currently it is
	 * only a dummy implementation.
	 *
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	String getString(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the BigDecimal data type. Currently it
	 * is only a dummy implementation.
	 *
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	BigDecimal getBigDecimal(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the Date data type. Currently it is
	 * only a dummy implementation.
	 *
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	Date getDate(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the Blob data type. Currently it is
	 * only a dummy implementation.
	 *
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	Blob getBlob(String name) throws BirtException;

	/**
	 * Returns the value of a bound column as the byte[] data type. Currently it is
	 * only a dummy implementation.
	 *
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	byte[] getBytes(String name) throws BirtException;

	/**
	 * Advances the iterator, skipping rows to the last row in the current group at
	 * the specified group level. This is for result sets that do not use detail
	 * rows to advance to next group. Calling next() after skip() would position the
	 * current row to the first row of the next group.
	 *
	 * @param groupLevel An absolute value for group level. A value of 0 applies to
	 *                   the whole result set.
	 * @throws BirtException if error occurs in Data Engine
	 */
	void skipToEnd(int groupLevel) throws BirtException;

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
	int getStartingGroupLevel() throws BirtException;

	/**
	 * Returns the 1-based index of the outermost group in which the current row is
	 * the last row. For example, if a query contain N groups (group with index 1
	 * being the outermost group, and group with index N being the innermost group),
	 * and this function returns a value M, it indicates that the current row is the
	 * last row in groups with indexes (M, M+1, ..., N ).
	 *
	 * @return 1-based index of the outermost group in which the current row is the
	 *         last row; (N+1) if the current row is not at the end of any group; 0
	 *         if the result set has no groups.
	 */
	int getEndingGroupLevel() throws BirtException;

	/**
	 * Returns the secondary result specified by a sub query that was defined in the
	 * prepared <code>IQueryDefinition</code>.
	 *
	 * @throws DataException if error occurs in Data Engine
	 * @param subQueryName name of sub query which defines the secondary result set
	 * @param scope        Javascript scope to be associated with the secondary
	 *                     result set
	 * @deprecated
	 */
	@Deprecated
	IResultIterator getSecondaryIterator(String subQueryName, Scriptable scope) throws BirtException;

	/**
	 * Returns the secondary result specified by a sub query that was defined in the
	 * prepared <code>IQueryDefinition</code>.
	 *
	 * @throws DataException if error occurs in Data Engine
	 * @param subQueryName name of sub query which defines the secondary result set
	 * @param cx           ScriptContext
	 */
	IResultIterator getSecondaryIterator(ScriptContext context, String subQueryName) throws BirtException;

	/**
	 * Move the current position of the iterator to the first element of the group
	 * with matching group key values. To locate the [n]th inner group, values for
	 * all outer groups' keys need to be provided in the array groupKeyValues.
	 * groupKeyValue[0] is the key value for group 1 (outermost group),
	 * groupKeyValue[1] is the key value for group 2, etc.
	 *
	 * @param groupKeyValues Values of group keys
	 * @return true if group located successfully and cursor is re-positioned. False
	 *         if no group is found to match the group key values exactly, and
	 *         iterator cursor is not moved.
	 * @throws DataException
	 */
	boolean findGroup(Object[] groupKeyValues) throws BirtException;

	/**
	 * Retrieves whether the cursor is on the first row of this
	 * <code>IResultIterator</code> object.
	 *
	 * @return <code>true</code> if the cursor is on the first row;
	 *         <code>false</code> otherwise
	 * @exception BirtException
	 */
	boolean isFirst() throws BirtException;

	/**
	 * Retrieves whether the cursor is before the first row in this
	 * <code>IResultIterator</code> object.
	 *
	 * @return <code>true</code> if the cursor is before the first row;
	 *         <code>false</code> if the cursor is at any other position or the
	 *         result set contains no rows
	 * @exception BirtException
	 */
	boolean isBeforeFirst() throws BirtException;

	/**
	 * Moves iterator to the row with given absolute index. Valid index must be both
	 * not less than current row index and not great than the maximum row index.
	 * Presently backward see is not supported.
	 *
	 * @param rowIndex, which index needs to advance to
	 * @throws BirtException, if rowIndex is invalid
	 */
	void moveTo(int rowIndex) throws BirtException;
}
