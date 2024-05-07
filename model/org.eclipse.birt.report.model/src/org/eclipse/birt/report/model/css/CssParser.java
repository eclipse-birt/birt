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

package org.eclipse.birt.report.model.css;

import java.io.IOException;
import java.util.Properties;
import java.util.Stack;

import org.eclipse.birt.report.model.util.SecurityUtil;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * Implements all the parser operations of the CSS2 related.
 */

public class CssParser {

	/**
	 * The core implementation of the lexical analysis of CSS2 grammar.
	 */

	private Parser parser = null;

	/**
	 * The error handler of the parser of CSS2.
	 */

	private CssErrorHandler errorHandler = null;

	/**
	 * Default constructor.
	 *
	 */

	public CssParser() {
		parser = ParserFactory.createCSS2Parser();
		errorHandler = ParserFactory.createErrorHandler();
	}

	/**
	 * Parses a CSS resource and get the CSSStyleSheet as the output.
	 *
	 * @param source the source of the CSS resource
	 * @return the CSSStyleSheet if succeed
	 * @throws IOException if the resource is not well-located
	 */

	public CSSStyleSheet parseStyleSheet(InputSource source) throws IOException {
		CssHandler handler = new CssHandler();
		parser.setDocumentHandler(handler);
		parser.setErrorHandler(errorHandler);
		try {
			parser.parseStyleSheet(source);
		} catch (StringIndexOutOfBoundsException e) {
			throw new CSSException(CSSException.SAC_SYNTAX_ERR);
		}
		return (StyleSheet) handler.getRoot();
	}

	/**
	 * Gets the error handler.
	 *
	 * @return the error handler
	 */

	public CssErrorHandler getErrorHandler() {
		return this.errorHandler;
	}

	static class CssHandler implements DocumentHandler {

		private Stack<Object> nodeStack;
		private Object root = null;

		public CssHandler(Stack<Object> nodeStack) {
			this.nodeStack = nodeStack;
		}

		public CssHandler() {
			this.nodeStack = new Stack<Object>();
		}

		public Object getRoot() {
			return root;
		}

		@Override
		public void startDocument(InputSource source) throws CSSException {
			if (nodeStack.empty()) {
				StyleSheet ss = new StyleSheet();

				nodeStack.push(ss);
			} else {
				// Error
			}
		}

		@Override
		public void endDocument(InputSource source) throws CSSException {

			// Pop style sheet nodes
			root = nodeStack.pop();
		}

		@Override
		public void comment(String text) throws CSSException {
		}

		/**
		 * Creates an unsupported rule and adds it to the list.
		 *
		 * @param atRule the rule to handle
		 */

		private void unsupportedRule(String atRule) {
			// Create the unknown rule and add it to the rule list

			UnSupportedRule ir = new UnSupportedRule(atRule);
			if (!nodeStack.empty()) {
				((StyleSheet) nodeStack.peek()).add(ir);
			} else {
				// nodeStack.push(ir);
				root = ir;
			}
		}

		@Override
		public void ignorableAtRule(String atRule) throws CSSException {
			unsupportedRule(atRule);
		}

		@Override
		public void namespaceDeclaration(String prefix, String uri) throws CSSException {
		}

		@Override
		public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI) throws CSSException {
			unsupportedRule(uri);
		}

		@Override
		public void startMedia(SACMediaList media) throws CSSException {
			unsupportedRule(media.toString());
		}

		@Override
		public void endMedia(SACMediaList media) throws CSSException {

		}

		@Override
		public void startPage(String name, String pseudo_page) throws CSSException {
			unsupportedRule(name + pseudo_page);
		}

		@Override
		public void endPage(String name, String pseudo_page) throws CSSException {

		}

		@Override
		public void startFontFace() throws CSSException {
			unsupportedRule(null);
		}

		@Override
		public void endFontFace() throws CSSException {

		}

		@Override
		public void startSelector(SelectorList selectors) throws CSSException {
			// Create the style rule and add it to the rule list

			StyleRule sr = new StyleRule(selectors);
			if (!nodeStack.empty()) {
				((StyleSheet) nodeStack.peek()).add(sr);
			}

			// Create the style declaration
			StyleDeclaration decl = new StyleDeclaration();
			sr.setStyle(decl);
			nodeStack.push(sr);
			nodeStack.push(decl);
		}

		@Override
		public void endSelector(SelectorList selectors) throws CSSException {

			// Pop both the style declaration and the style rule nodes
			nodeStack.pop();
			root = nodeStack.pop();
		}

		@Override
		public void property(String name, LexicalUnit value, boolean important) throws CSSException {
			StyleDeclaration decl = (StyleDeclaration) nodeStack.peek();
			decl.addProperty(new Property(name, new CSSValue(value)));
		}
	}

	/**
	 * Set property of the parser
	 *
	 * @param key key string
	 * @param val key value
	 */
	public static void setProperty(String key, String val) {
		Properties props = SecurityUtil.getSystemProperties();
		props.put(key, val);
		System.setProperties(props);
	}
}
