/***********************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.data.bidi.utils.core.BidiFormat;
import org.eclipse.birt.report.data.bidi.utils.core.BidiTransform;

/**
 * This class performs various SQL Bidi transformations.
 * 
 * @author Lina Kemmel
 * 
 */

public abstract class BidiSQLTransform implements ISQLSyntax {

	enum ACTION {
		NONE, CHECK, TRANSFORM_ID, TRANSFORM_LITERAL
	}

	static class RuleData {

		String regex;
		ACTION action;
		int flags;

		public RuleData(String regex, ACTION action, int flags) {
			this.regex = regex;
			this.action = action;
			this.flags = flags;
		}
	}

	static class RuleComparator implements Comparator<ImplementedRule> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(ImplementedRule o1, ImplementedRule o2) {
			return Integer.valueOf(o1.startOffset).compareTo(o2.startOffset);
		}
	}

	static class ImplementedRule {
		ACTION action;
		Matcher matcher;
		int startOffset;
		int endOffset;

		ImplementedRule(String text, RuleData data) {
			this.action = data.action;
			this.matcher = Pattern.compile(data.regex, data.flags).matcher(text);
			getNextToken(0);
		}

		void getNextToken(int offset) {
			if (matcher.find(offset)) {
				startOffset = matcher.start();
				endOffset = matcher.end();
			} else
				startOffset = endOffset = Integer.MAX_VALUE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			return o instanceof ImplementedRule && comparator.compare(this, (ImplementedRule) o) == 0;
		}

		public int hashCode() {
			return this.action.hashCode() * 11 + startOffset + endOffset;
		}

		boolean isValid(int offset) {
			return startOffset < endOffset && startOffset >= offset;
		}
	}

	private static RuleComparator comparator = new RuleComparator();

	private static final RuleData[] RULE_DATA = new RuleData[] { new RuleData("'.+?'", ACTION.TRANSFORM_LITERAL, 0), //$NON-NLS-1$
			new RuleData("\"[^\"]+\"", ACTION.TRANSFORM_ID, 0), //$NON-NLS-1$
			new RuleData("\\s+", ACTION.NONE, 0), //$NON-NLS-1$
			new RuleData("//.*", ACTION.NONE, 0), //$NON-NLS-1$
			new RuleData("--.*", ACTION.NONE, 0), //$NON-NLS-1$
			new RuleData("/\\*.*?\\*/", ACTION.NONE, Pattern.DOTALL), //$NON-NLS-1$
			new RuleData("[\\p{L}_][\\p{L}\\p{N}_]+", ACTION.CHECK, 0) //$NON-NLS-1$
	};

	private static String[] ALL_KEYWORDS = null;

	static {
		mergeKeywords(reservedwords, constants, predicates, types, functions);
	}

	/**
	 * Merges all keywords to facilitate the search - lb(k1 + k2 + ... + kN) vs.
	 * lb(k1) + lb(k2) + ... + lb(kN) at max
	 * 
	 * @param keywords Variable-length arrays list to merge
	 */
	private static void mergeKeywords(String[]... keywords) {
		if (keywords != null) {
			int i = 0;
			int offset = 0;
			int size = 0;
			int[] sizes = new int[keywords.length];

			for (String[] array : keywords) {
				sizes[i] = array == null ? 0 : array.length;
				size += sizes[i++];
			}
			ALL_KEYWORDS = new String[size];

			for (i = 0, size = sizes.length; i < size; i++) {
				if (sizes[i] > 0) {
					System.arraycopy(keywords[i], 0, ALL_KEYWORDS, offset, sizes[i]);
					offset += sizes[i];
				}
			}
			Arrays.sort(ALL_KEYWORDS);
		} else
			ALL_KEYWORDS = new String[0];
	}

	private static boolean isKeyword(String word) {
		return Arrays.binarySearch(ALL_KEYWORDS, word) >= 0;
	}

	private static List<ImplementedRule> initRules(String text) {
		List<ImplementedRule> rules = new ArrayList<ImplementedRule>();

		for (int i = 0, n = RULE_DATA.length; i < n; i++) {
			ImplementedRule rule = new ImplementedRule(text, RULE_DATA[i]);
			if (rule.isValid(0))
				rules.add(rule);
		}
		if (!rules.isEmpty())
			Collections.sort(rules, comparator);

		return rules;
	}

	private static ImplementedRule getBestMatch(List<ImplementedRule> rules, String text, int offset) {
		if (rules == null || rules.isEmpty())
			return null;

		if (offset > 0) {
			Iterator<ImplementedRule> it = rules.iterator();
			while (it.hasNext()) {
				ImplementedRule rule = it.next();

				if (rule.startOffset < offset)
					rule.getNextToken(offset);

				if (rule.startOffset == offset)
					return rule;

				if (!rule.isValid(offset))
					it.remove();
			}
			if (rules.isEmpty())
				return null;

			Collections.sort(rules, comparator);
		}
		return rules.get(0);
	}

	/**
	 * Performs transformation of the given SQL query from one Bidi format to
	 * another.
	 * 
	 * @param sql               The SQL query to format
	 * @param inContentFormat   Input content Bidi format
	 * @param outContentFormat  Input content Bidi format
	 * @param inMetadataFormat  Input metadata Bidi format
	 * @param outMetadataFormat Input metadata Bidi format
	 * 
	 * @return Transformed query string
	 */

	public static String transform(String sql, String inContentFormat, String outContentFormat, String inMetadataFormat,
			String outMetadataFormat) {
		if (!BidiFormat.isValidBidiFormat(inContentFormat) || !BidiFormat.isValidBidiFormat(outContentFormat)
				|| !BidiFormat.isValidBidiFormat(inMetadataFormat) || !BidiFormat.isValidBidiFormat(outMetadataFormat)
				|| inContentFormat.equals(outContentFormat) && inMetadataFormat.equals(outMetadataFormat)) {
			return sql;
		}
		int offset = 0;
		int length = sql.length();
		StringBuffer buf = new StringBuffer(length);

		List<ImplementedRule> rules = initRules(sql);

		if (rules.isEmpty())
			return sql;

		while (offset < length) {
			ImplementedRule rule = getBestMatch(rules, sql, offset);
			if (null == rule)
				break;

			if (rule.startOffset > offset)
				buf.append(sql.substring(offset, rule.startOffset));

			String token = sql.substring(rule.startOffset, rule.endOffset);

			if (rule.action == ACTION.TRANSFORM_ID || rule.action == ACTION.CHECK && !isKeyword(token)) {
				buf.append(BidiTransform.transform(token, inMetadataFormat, outMetadataFormat));
			} else if (rule.action == ACTION.TRANSFORM_LITERAL) {
				buf.append(BidiTransform.transform(token, inContentFormat, outContentFormat));
			} else
				buf.append(token);

			offset = rule.endOffset;
		}
		if (offset < length)
			buf.append(sql.substring(offset, length));

		return buf.toString();
	}

}
