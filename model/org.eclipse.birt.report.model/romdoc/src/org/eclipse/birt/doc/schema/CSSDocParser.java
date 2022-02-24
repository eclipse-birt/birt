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

package org.eclipse.birt.doc.schema;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.doc.util.HTMLParser;
import org.eclipse.birt.doc.util.HtmlDocReader;

/**
 * Parser css html document.
 *
 */

public class CSSDocParser extends HtmlDocReader {
	/**
	 * css map, key is css property name , value is css property value.
	 */

	protected Map cssMap = new HashMap();

	String templateDir = "romdoc/docs/css/CssProperty.html"; //$NON-NLS-1$

	/**
	 * Parser css property html.
	 * 
	 * @throws ParseException
	 */

	public void parse() throws ParseException {
		try {
			parser.open(templateDir);
		} catch (FileNotFoundException e) {
			return;
		}

		parseElement();
	}

	/**
	 * Parser element. format is :
	 * <tr>
	 * <td>name
	 * <td>values
	 * 
	 * @throws ParseException
	 * 
	 */
	private void parseElement() throws ParseException {
		skipTo("table");//$NON-NLS-1$

		for (;;) {
			int token = getToken();
			if (token == HTMLParser.EOF)
				return;

			if (isElement(token, "/table"))//$NON-NLS-1$
				return;
			assert (isElement(token, "tr"));//$NON-NLS-1$

			token = getToken();
			assert (isElement(token, "td"));//$NON-NLS-1$

			token = getToken();
			assert (token == HTMLParser.TEXT);
			String name = parser.getTokenText().trim();

			token = getToken();
			assert (isElement(token, "td"));//$NON-NLS-1$

			token = getToken();
			assert (token == HTMLParser.TEXT);
			String values = parser.getTokenText().trim();

			// parser template name, the seperate is ','.

			if (name.indexOf(",") == -1)//$NON-NLS-1$
			{
				cssMap.put(name, values);
				continue;
			}
			String[] nameList = name.split(",");//$NON-NLS-1$
			for (int i = 0; i < nameList.length; ++i) {
				String subName = nameList[i].trim();
				cssMap.put(subName, values);
			}
		}
	}

	static class ParseException extends Exception {

		/**
		 * UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 * 
		 * @param msg exception message
		 */
		public ParseException(String msg) {
			super(msg);
		}
	}

}
