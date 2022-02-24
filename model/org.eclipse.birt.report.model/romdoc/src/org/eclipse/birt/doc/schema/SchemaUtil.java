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

import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.SystemPropertyDefn;

/**
 * 
 * Schema utility
 * 
 */
public class SchemaUtil {

	/**
	 * Check string is null or length of string is zero.
	 * 
	 * @param astr
	 * @return boolean <code>true</code>if string is blank, else return
	 *         <code>false</code>
	 */

	public static boolean isBlank(String astr) {
		if ((null == astr) || (astr.trim().length() == 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Transform html to value. For example : change 'fontFamily' to 'font-family'
	 * 
	 * @param input
	 * @return value after converting
	 */

	private static String transform(String input) {
		if (input == null)
			return null;
		StringBuffer buffer = new StringBuffer();
		input = input.trim();
		int len = input.length();
		for (int i = 0; i < len; ++i) {
			char c = input.charAt(i);
			if ('A' <= c && c <= 'Z') {
				// split and change upper case to low case
				buffer.append("-"); //$NON-NLS-1$
				c = (char) (c - 'A' + 'a');
			}
			buffer.append(c);
		}

		return buffer.toString();
	}

	/**
	 * Pre write schema.
	 * 
	 * @param styleDefn
	 * @param writer
	 */

	private static void preWrite(Map cssMap, IElementDefn styleDefn, ISchemaWriter writer) {

	}

	/**
	 * Post write schem.
	 * 
	 * @param styleDefn
	 * @param writer
	 */

	private static void postWrite(Map cssMap, IElementDefn styleDefn, ISchemaWriter writer) {
		// add background-position
		String allowedValue = "[ [ left | center | right ] || [ top | center | bottom ] ]"; //$NON-NLS-1$
		CssType css = new CssType();
		String name = "background-position";//$NON-NLS-1$
		css.setName(name);
		css.setBirtChoiceValues(allowedValue);
		css.setInitialValues(null);
		String cssValue = (String) cssMap.get(name);
		css.setValues(cssValue);
		writer.writeRow(css);

		// add text-decoration
		allowedValue = "underline | overline | line-through "; //$NON-NLS-1$
		CssType textDecoCss = new CssType();
		name = "text-decoration";//$NON-NLS-1$
		textDecoCss.setName(name);
		textDecoCss.setBirtChoiceValues(allowedValue);
		textDecoCss.setInitialValues(null);
		cssValue = (String) cssMap.get(name);
		textDecoCss.setValues(cssValue);
		writer.writeRow(textDecoCss);
	}

	/**
	 * Write xml schema.
	 * 
	 * @param dictionary
	 * @param writer
	 * @param filter
	 * @param cssMap
	 */

	public static void writeSchema(IMetaDataDictionary dictionary, ISchemaWriter writer, IFilter filter, Map cssMap) {
		assert dictionary != null;
		assert writer != null;
		assert filter != null;
		assert cssMap != null;

		ElementDefn styleDefn = (ElementDefn) dictionary.getElement("Style"); //$NON-NLS-1$
		if (styleDefn == null)
			return;

		writer.startHtml();
		preWrite(cssMap, styleDefn, writer);
		Iterator iterator = styleDefn.propertiesIterator();
		while (iterator.hasNext()) {
			SystemPropertyDefn propDefn = (SystemPropertyDefn) iterator.next();
			if (!propDefn.isStyleProperty())
				continue;

			String propName = propDefn.getName();

			// change 'fontFamily' to 'font-family'

			propName = SchemaUtil.transform(propName);
			Object defaultValue = propDefn.getDefault();
			if (defaultValue != null)
				defaultValue = defaultValue.toString();

			// type is not structure , boolean , name
			if (!filter.filter(propDefn))
				continue;

			IChoiceSet propChoice = propDefn.getChoices();

			CssType css = new CssType();
			css.setName(propName);
			css.setInitialValues((String) defaultValue);
			String cssValue = (String) cssMap.get(propName);
			css.setValues(cssValue);

			if (propChoice == null) {
				writer.writeRow(css);
				continue;
			}

			IChoice[] choices = propChoice.getChoices();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < choices.length; ++i) {
				IChoice choice = choices[i];
				String choiceName = choice.getName();

				buffer.append("|");//$NON-NLS-1$
				buffer.append(choiceName);
			}

			String allowedValue = buffer.toString();
			if (allowedValue.length() != 0) {
				allowedValue = allowedValue.substring(1);
			}

			css.setBirtChoiceValues(allowedValue);
			writer.writeRow(css);
		}
		postWrite(cssMap, styleDefn, writer);

		writer.closeHtml();
		writer.close();
	}
}
