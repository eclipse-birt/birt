/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *******************************************************************************/
package org.eclipse.birt.report.data.oda.excel.impl.util.querytextutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.birt.report.data.oda.excel.impl.util.ExcelFileSource;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Utility class for splitting query text into query and columns information
 */

public class QueryTextUtil {

	private static final char QUERY_TEXT_DELIMITER = ':';

	private static final char COLUMNSINFO_BEGIN_DELIMITER = '{';

	private String query;

	private String columnsInfo;

	/**
	 *
	 */
	public QueryTextUtil(String queryText) throws OdaException {
		assert queryText != null;
		String[] splits = splitQueryText(queryText);
		query = splits[0];
		columnsInfo = splits[1];
	}

	public String getQuery() {
		return query;
	}

	public String getColumnsInfo() {
		return columnsInfo;
	}

	/**
	 *
	 */
	private String[] splitQueryText(String queryText) throws OdaException {
		int delimiterIndex = -1;
		int columnsInfoBeginIndex = -1;

		String trimmedQueryText = queryText.trim();

		String[] splittedQueryText = { "", "" //$NON-NLS-1$ //$NON-NLS-2$
		};
		boolean inQuote = false;
		boolean isEscaped = false;
		char[] chars = trimmedQueryText.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '"') {
				if (!isEscaped)
					inQuote = !inQuote;
				else
					isEscaped = !isEscaped;
			} else if (chars[i] == '\\') {
				isEscaped = !isEscaped;
			} else if ((!inQuote) && chars[i] == QUERY_TEXT_DELIMITER)
				delimiterIndex = i;
			else if ((!inQuote) && chars[i] == COLUMNSINFO_BEGIN_DELIMITER) {
				columnsInfoBeginIndex = i;
				break;
			}
		}

		if (inQuote)
			throw new OdaException(Messages.getString("query_text_error")); //$NON-NLS-1$

		if (delimiterIndex != -1 && columnsInfoBeginIndex != -1) {
			splittedQueryText[0] = trimmedQueryText.substring(0, delimiterIndex).trim();
			splittedQueryText[1] = trimmedQueryText.substring(columnsInfoBeginIndex + 1, trimmedQueryText.length() - 1)
					.trim();
		} else if (delimiterIndex == -1 && columnsInfoBeginIndex == -1)
			splittedQueryText[0] = trimmedQueryText;
		else
			throw new OdaException(Messages.getString("query_text_error")); //$NON-NLS-1$

		return splittedQueryText;
	}

	/**
	 * Strip off keyword "SELECT" from the given query.
	 *
	 * @param formattedQuery a trimed query text; cannot be null.
	 * @return the given text stripped the SELECT keyword
	 * @throws OdaException
	 */
	protected static String stripSELECTKeyword(String formattedQuery) throws OdaException {
		// This array stores two values: "SELECT" keyword and other part of a
		// command
		String[] array = formattedQuery.split(ExcelODAConstants.DELIMITER_SPACE, 2);
		if (array == null || array.length != 2 || !array[0].trim().equalsIgnoreCase(ExcelODAConstants.KEYWORD_SELECT))
			throw new OdaException(Messages.getString("query_COMMAND_NOT_VALID")); //$NON-NLS-1$

		return array[1];
	}

	/**
	 * Split the given query fragments before and after the FROM keyword
	 *
	 * @param query Query text without heading SELECT keyword
	 * @return A String array with two elements: column names (may include alias
	 *         with the AS keyword), and table names after the FROM clause.
	 * @throws OdaException
	 */
	protected static String[] stripFROMKeyword(String query) throws OdaException {
		char[] chars = query.toCharArray();
		List<Integer> indiceList = new ArrayList<Integer>();
		boolean inQuote = false;
		boolean isEscaped = false;
		LookAheadMacher matcher = new LookAheadMacher("FROM ", " ", true);
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '"') {
				if (!isEscaped)
					inQuote = !inQuote;
				else
					isEscaped = !isEscaped;
			} else if (chars[i] == '\\') {
				isEscaped = !isEscaped;
			} else if (inQuote) {
				continue;
			} else {
				if (matcher.match(chars, i)) {
					indiceList.add(i - 1);
				}
			}
		}

		String[] result = new String[2];
		if (indiceList.size() > 0) {
			int splitInd = indiceList.get(indiceList.size() - 1);
			result[0] = query.substring(0, splitInd);
			result[1] = getUnQuotedName(query.substring(splitInd + matcher.getPatternLength()));
		} else
			throw new OdaException(Messages.getString("query_COMMAND_NOT_VALID")); //$NON-NLS-1$

		return result;
	}

	public static String getQuotedName(String name) {
		if (name == null || (name.charAt(0) == '\"' && name.charAt(name.length() - 1) == '\"'))
			return name;

		StringBuffer sb = new StringBuffer("\"").append(name).append("\"");
		return sb.toString();
	}

	public static String getUnQuotedName(String name) {
		if (name == null || name.length() == 0)
			return name;

		int head = 0;
		int end = name.length();
		if (name.charAt(head) == '\"')
			head++;
		if (name.charAt(name.length() - 1) == '\"')
			end--;

		return name.substring(head, end);
	}

	static class LookAheadMacher {

		private String pattern = "";
		private String heading = "";
		private boolean caseInsensitive = false;

		LookAheadMacher(String pattern, String heading, boolean caseInsensitive) {
			this.pattern = pattern;
			this.heading = heading;
			this.caseInsensitive = caseInsensitive;
		}

		int getPatternLength() {
			return pattern.length() + heading.length();
		}

		boolean match(char[] ch, int ind) {
			boolean first = false;
			if (caseInsensitive) {
				first = Character.toUpperCase(pattern.charAt(0)) == Character.toUpperCase(ch[ind]);
			} else {
				first = pattern.charAt(0) == ch[ind];
			}

			if (!first)
				return false;

			return match(String.valueOf(ch), ind);
		}

		boolean match(String toMatch, int start) {
			if (toMatch == null || (toMatch.length() - start) < pattern.length())
				return false;

			if (start < heading.length() - 1)
				return false;

			String to = toMatch.substring(start - heading.length(), start);
			if (caseInsensitive) {
				if (!heading.equalsIgnoreCase(to))
					return false;
			} else if (!heading.equals(to)) {
				return false;
			}

			to = toMatch.substring(start, start + pattern.length());
			if (to.length() != pattern.length())
				return false;

			if (caseInsensitive) {
				if (!pattern.equalsIgnoreCase(to))
					return false;
			} else if (!pattern.equals(to)) {
				return false;
			}

			return true;
		}
	}

	/**
	 * Split the column name from alias, stripping the "AS" keyword, from given
	 * query fragments.
	 *
	 * @param querySelectAndFromFragments
	 * @return a String array with three elements: first element contains column
	 *         names separated by comma, second element contains column
	 *         aliases(labels) separated by comma, third element contains the table
	 *         name(s) in FROM clause
	 */
	protected static String[] stripASKeyword(String[] querySelectAndFromFragments) {
		String[] result = new String[3];
		// store the table name in given last element as the third element
		result[2] = querySelectAndFromFragments[1];

		// split the columns specified in the SELECT clause
		String selectedColumns = querySelectAndFromFragments[0];
		if (!isWildCard(selectedColumns)) {
			String[] columns = ExcelFileSource.getStringArrayFromList(getQueryColumnNamesVector(selectedColumns));

			for (int i = 0; i < columns.length; i++) {
				String[] columnNameAlias = columns[i].split(ExcelODAConstants.DELIMITER_SPACE
						+ ExcelODAConstants.KEYWORD_AS + ExcelODAConstants.DELIMITER_SPACE);
				if (columnNameAlias != null) {
					// append column name to comma-separated column names in
					// result[0]
					result[0] = (i == 0 ? columnNameAlias[0]
							: result[0] + ExcelODAConstants.DELIMITER_COMMA_VALUE + columnNameAlias[0].trim());

					// append column alias, if exists, or null to
					// comma-separated column aliases in result[1]
					if (columnNameAlias.length == 2)
						result[1] = (i == 0 ? columnNameAlias[1]
								: result[1] + ExcelODAConstants.DELIMITER_COMMA_VALUE + columnNameAlias[1].trim());
					else
						result[1] = (i == 0 ? null : result[1] + ExcelODAConstants.DELIMITER_COMMA_VALUE + null);
				}
			}
		} else {
			result[0] = ExcelODAConstants.KEYWORD_ASTERISK;
			result[1] = null;
		}

		return result;
	}

	public static String[] getQueryMetaData(String query) throws OdaException {
		return stripASKeyword(stripFROMKeyword(stripSELECTKeyword(query)));
	}

	/**
	 * @param cCN
	 * @return
	 */
	static boolean isWildCard(String cCN) {
		if (cCN.equalsIgnoreCase(ExcelODAConstants.KEYWORD_ASTERISK))
			return true;
		return false;
	}

	/**
	 *
	 * @param queryColumnNames
	 * @return
	 */
	static Vector<String> getQueryColumnNamesVector(String queryColumnNames) {
		Vector<String> result = new Vector<String>();
		char[] chars = queryColumnNames.toCharArray();
		List<Integer> indiceList = new ArrayList<Integer>();
		boolean inQuote = false;
		boolean isEscaped = false;
		int beginIndex = 0;
		int endIndex = 0;

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '"') {
				if (!isEscaped)
					inQuote = !inQuote;
				else
					isEscaped = !isEscaped;
			} else if (chars[i] == '\\') {
				isEscaped = !isEscaped;
			} else if (chars[i] == ',') {
				if (inQuote)
					continue;
				else
					indiceList.add(new Integer(i));
			}
		}

		if (indiceList.size() > 0) {
			for (int j = 0; j < indiceList.size(); j++) {

				endIndex = ((Integer) indiceList.get(j)).intValue();

				result.add(queryColumnNames.substring(beginIndex, endIndex).trim());
				beginIndex = endIndex + 1;

				if (j == indiceList.size() - 1) {
					result.add(queryColumnNames.substring(beginIndex, queryColumnNames.length()).trim());
				}
			}
		} else
			result.add(queryColumnNames);

		return result;
	}
}
