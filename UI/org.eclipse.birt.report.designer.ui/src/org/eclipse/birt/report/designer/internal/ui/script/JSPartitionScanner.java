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

package org.eclipse.birt.report.designer.internal.ui.script;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/**
 *
 * Partition scanner for javascript editor
 */
public class JSPartitionScanner extends RuleBasedPartitionScanner {

	public final static String JS_DEFAULT = "__js_default"; //$NON-NLS-1$
	public final static String JS_COMMENT = "__js_comment"; //$NON-NLS-1$
	public final static String JS_KEYWORD = "__js_keyword"; //$NON-NLS-1$
	public final static String JS_STRING = "__js_string"; //$NON-NLS-1$

	public final static IToken TOKEN_STRING = new Token(JS_STRING);
	public final static IToken TOKEN_COMMENT = new Token(JS_COMMENT);
	public final static IToken TOKEN_DEFAULT = new Token(JS_DEFAULT);
	public final static IToken TOKEN_KEYWORD = new Token(JS_KEYWORD);

	/**
	 * Array of keyword token strings.
	 */
	private static String[] keywordTokens = { "break", //$NON-NLS-1$
			"case", //$NON-NLS-1$
			"catch", //$NON-NLS-1$
			"continue", //$NON-NLS-1$
			"default", //$NON-NLS-1$
			"do", //$NON-NLS-1$
			"else", //$NON-NLS-1$
			"for", //$NON-NLS-1$
			"function", //$NON-NLS-1$
			"goto", //$NON-NLS-1$
			"if", //$NON-NLS-1$
			"in", //$NON-NLS-1$
			"new", //$NON-NLS-1$
			"return", //$NON-NLS-1$
			"switch", //$NON-NLS-1$
			"this", //$NON-NLS-1$
			"throw", //$NON-NLS-1$
			"try", //$NON-NLS-1$
			"var", //$NON-NLS-1$
			"void", //$NON-NLS-1$
			"while", //$NON-NLS-1$
			"with" //$NON-NLS-1$
	};

	/**
	 * Array of constant token strings.
	 */
	private static String[] constantTokens = { "false", "null", "true" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	};

	/**
	 * Creates a new JSPartitionScanner object.
	 */
	public JSPartitionScanner() {
		List rules = new ArrayList();

		rules.add(new MultiLineRule("/*", "*/", TOKEN_COMMENT)); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new EndOfLineRule("//", TOKEN_COMMENT)); //$NON-NLS-1$
		rules.add(new SingleLineRule("\"", "\"", TOKEN_STRING, '\\')); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new SingleLineRule("'", "'", TOKEN_STRING, '\\')); //$NON-NLS-1$ //$NON-NLS-2$

		PredicateWordRule keywordRule = new PredicateWordRule(new IWordDetector() {

			@Override
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}

			@Override
			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}

		}, TOKEN_DEFAULT);
		keywordRule.addWords(keywordTokens, TOKEN_KEYWORD);
		keywordRule.addWords(constantTokens, TOKEN_KEYWORD);
		rules.add(keywordRule);

		setRuleList(rules);
	}

	private void setRuleList(List rules) {
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}

	protected void addWords(WordRule rule, String[] tokens, IToken token) {
		for (int i = 0; i < tokens.length; i++) {
			rule.addWord(tokens[i], token);
		}

	}

}
