/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation, 2024 others
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
 *  Thomas Gutmann       - additional syntax highlighting
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.eclipse.birt.core.script.functionservice.impl.FunctionProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
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
 * Partition scanner for javascript editor
 *
 */
public class JSPartitionScanner extends RuleBasedPartitionScanner {

	/** property: javascript default key */
	public final static String JS_DEFAULT = "__js_default"; //$NON-NLS-1$
	/** property: javascript comment key */
	public final static String JS_COMMENT = "__js_comment"; //$NON-NLS-1$
	/** property: javascript keyword key */
	public final static String JS_KEYWORD = "__js_keyword"; //$NON-NLS-1$
	/** property: javascript string key */
	public final static String JS_STRING = "__js_string"; //$NON-NLS-1$
	/** property: javascript method key */
	public final static String JS_METHOD = "__js_method"; //$NON-NLS-1$
	/** property: javascript object key */
	public final static String JS_OBJECT = "__js_object"; //$NON-NLS-1$

	/** token: javascript string */
	public final static IToken TOKEN_STRING = new Token(JS_STRING);
	/** token: javascript comment */
	public final static IToken TOKEN_COMMENT = new Token(JS_COMMENT);
	/** token: javascript default */
	public final static IToken TOKEN_DEFAULT = new Token(JS_DEFAULT);
	/** token: javascript keyword */
	public final static IToken TOKEN_KEYWORD = new Token(JS_KEYWORD);
	/** token: javascript method */
	public final static IToken TOKEN_METHOD = new Token(JS_METHOD);
	/** token: javascript object */
	public final static IToken TOKEN_OBJECT = new Token(JS_OBJECT);

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
			"with", //$NON-NLS-1$
			"const", //$NON-NLS-1$
			"let", //$NON-NLS-1$
			"of", //$NON-NLS-1$
	};

	/**
	 * Array of constant token strings.
	 */
	private static String[] constantTokens = { "false", "null", "true" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	};

	/**
	 * Array of global object strings
	 */
	private ArrayList<String> globalObjectTokens = new ArrayList<String>();

	/**
	 * Array of method names
	 */
	private ArrayList<String> keywordMethods = new ArrayList<String>();

	/**
	 * Creates a new JSPartitionScanner object.
	 */
	public JSPartitionScanner() {
		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		fetchJSCommonObjectsMethods();

		rules.add(new MultiLineRule("/*", "*/", TOKEN_COMMENT)); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new EndOfLineRule("//", TOKEN_COMMENT)); //$NON-NLS-1$
		rules.add(new MultiLineRule("`", "`", TOKEN_STRING)); //$NON-NLS-1$ //$NON-NLS-2$
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

		String[] keywordArray = new String[keywordMethods.size()];
		keywordArray = keywordMethods.toArray(keywordArray);
		keywordRule.addWords(keywordArray, TOKEN_METHOD);

		String[] globalObjectArray = new String[globalObjectTokens.size()];
		globalObjectArray = globalObjectTokens.toArray(globalObjectArray);
		keywordRule.addWords(globalObjectArray, TOKEN_OBJECT);
		rules.add(keywordRule);

		setRuleList(rules);
	}

	private void setRuleList(List<IPredicateRule> rules) {
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}

	protected void addWords(WordRule rule, String[] tokens, IToken token) {
		for (int i = 0; i < tokens.length; i++) {
			rule.addWord(tokens[i], token);
		}

	}

	/*
	 * parse all classes and method from JS libraries and the according methods for
	 * syntax highlights
	 */
	private void fetchJSCommonObjectsMethods() {
		this.globalObjectTokens.add("reportContext"); //$NON-NLS-1$
		this.globalObjectTokens.add("params"); //$NON-NLS-1$
		this.globalObjectTokens.add("vars"); //$NON-NLS-1$
		this.globalObjectTokens.add("row"); //$NON-NLS-1$
		this.globalObjectTokens.add("dataSetRow"); //$NON-NLS-1$
		this.globalObjectTokens.add("importPackage"); //$NON-NLS-1$
		this.globalObjectTokens.add("Packages"); //$NON-NLS-1$
		this.keywordMethods.add("__rownum"); //$NON-NLS-1$
		this.keywordMethods.add("value"); //$NON-NLS-1$
		this.keywordMethods.add("data"); //$NON-NLS-1$
		this.keywordMethods.add("displayText"); //$NON-NLS-1$

		try {
			// analysis of static javascript classes and methods
			List<IClassInfo> list = DEUtil.getClasses();
			for (Iterator<IClassInfo> cIter = list.iterator(); cIter.hasNext();) {
				IClassInfo classInfo = cIter.next();
				if (classInfo.isNative() == true && !classInfo.getName().equals("Total")) {
					this.globalObjectTokens.add(classInfo.getName());

					List<IMethodInfo> resultMethodList = classInfo.getMethods();
					for (Iterator<IMethodInfo> methodIter = resultMethodList.iterator(); methodIter.hasNext();) {
						IMethodInfo methodInfo = methodIter.next();
						this.keywordMethods.add(methodInfo.getName());
					}
					List<IMemberInfo> resultMemberList = classInfo.getMembers();
					for (Iterator<IMemberInfo> memberIter = resultMemberList.iterator(); memberIter.hasNext();) {
						IMemberInfo memberInfo = memberIter.next();
						this.keywordMethods.add(memberInfo.getName());
					}
				}
			}

			// analysis of project implemented javascript classes and methods
			IScriptFunctionCategory[] classInfo = FunctionProvider.getCategories();
			for (int i = 0; i < classInfo.length; i++) {
				this.globalObjectTokens.add(classInfo[i].getName());

				IScriptFunction[] methodInfo = classInfo[i].getFunctions();
				for (int j = 0; j < methodInfo.length; j++) {
					this.keywordMethods.add(methodInfo[j].getName());
				}
			}
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
		}

	}
}
