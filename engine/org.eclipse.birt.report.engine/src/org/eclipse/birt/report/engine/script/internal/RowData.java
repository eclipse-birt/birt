/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * A class representing expression results. Can be used to get values of
 * expressions defined on a report item. Implements lazy lookup; values are not
 * evaluated until they are requested.
 * 
 * Some processing is done to the expressions to make it easier for the user.
 * Example: It is ok to write row[CUSTOMERNAME] even though the correct
 * expression would be row["CUSTOMERNAME"]
 * 
 */

public class RowData implements IRowData {
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(IRowData.class.getName());

	private IBaseResultSet rset;
	private ArrayList bindingNames = new ArrayList();

	private static final Pattern rowWithIndex = Pattern.compile("(row\\[\\d+\\])", Pattern.CASE_INSENSITIVE);

	private static final Pattern rowWithWord = Pattern.compile("(row\\[\\w+\\])", Pattern.CASE_INSENSITIVE);

	public RowData(IBaseResultSet rset, ReportItemHandle element) {
		this.rset = rset;
		// intialize the bindings and bindingNames
		if (element != null) {
			addColumnBindings(element.columnBindingsIterator());
		}
	}

	private void addColumnBindings(Iterator bindingIter) {
		if (bindingIter != null) {
			while (bindingIter.hasNext()) {
				ComputedColumnHandle binding = (ComputedColumnHandle) bindingIter.next();
				bindingNames.add(binding.getName());
			}
		}
	}

	/**
	 * Get the value of the provided expression. The expression must be defined on
	 * the report item. Some processing is done to the expression to make thing
	 * easier. It is ok to for an expression to contain things like
	 * row[CUSTOMERNAME] for example (will be replaced with row["CUSTOMENAME"]).
	 * row[123] will be kept as row[123] (index lookup). The regex used is to find
	 * things to replace is: row\\[\\w+\\], Pattern.CASE_INSENSITIVE minus
	 * row\\[\\d+\\], Pattern.CASE_INSENSITIVE.
	 * 
	 * @deprecated
	 * @param expression
	 * @return the evaluated value of the provided expression
	 * @throws ScriptException
	 */
	public Object getExpressionValue(String expression) throws ScriptException {
		expression = process(expression);
		try {
			return rset.evaluate(expression);
		} catch (BirtException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @deprecated
	 */
	public Object getExpressionValue(int index) throws ScriptException {
		String name = getColumnName(index);
		if (name != null) {
			return getColumnValue(name);
		}
		return null;
	}

	// Process the expression (replace row[something] with row["something"])
	private String process(String expression) {
		if (expression == null)
			return null;
		expression = expression.trim();
		// Replace row[something] with row["something"]
		Matcher mWord = rowWithWord.matcher(expression);
		StringBuffer sb = new StringBuffer();
		while (mWord.find()) {
			String group = mWord.group(1);
			// TODO: This could probably be merged into the main pattern
			Matcher mIndex = rowWithIndex.matcher(group);
			// Don't replace row[123] with row["123"] (index)
			if (!mIndex.matches()) {
				group = group.replaceAll("\\[", "[\"");
				group = group.replaceAll("\\]", "\"]");
			}
			mWord.appendReplacement(sb, group);
		}
		mWord.appendTail(sb);
		return sb.toString();
	}

	public int getExpressionCount() {
		return getColumnCount();
	}

	public Object getColumnValue(String name) throws ScriptException {
		try {
			if (rset != null) {
				if (rset.getType() == IBaseResultSet.QUERY_RESULTSET) {
					return ((IQueryResultSet) rset).getValue(name);
				} else {
					// FIXME: if the rset is ICubeResultSet
				}
			}
		} catch (BirtException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * get column value by index index start from 0
	 */
	public Object getColumnValue(int index) throws ScriptException {
		String name = getColumnName(index);
		if (name != null) {
			return getColumnValue(name);
		}
		return null;
	}

	/**
	 * get column name by index index start from 0
	 */
	public String getColumnName(int index) {
		if (index < bindingNames.size()) {
			return (String) bindingNames.get(index);
		}
		return null;
	}

	public int getColumnCount() {
		return bindingNames.size();
	}

}
