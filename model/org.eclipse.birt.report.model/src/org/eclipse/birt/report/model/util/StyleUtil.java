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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 *
 */
public class StyleUtil {

	/**
	 *
	 * @param defn
	 * @param fontFamily
	 * @return string value of font family
	 */
	public static String handleFontFamily(PropertyDefn defn, String fontFamily) {
		String value = StringUtil.trimString(fontFamily);
		if (StringUtil.isBlank(value)) {
			return value;
		}
		String splitter = ","; //$NON-NLS-1$
		String[] families = value.split(splitter);
		List<String> values = new ArrayList<>();
		for (int i = 0; i < families.length; i++) {
			String family = families[i];
			family = StringUtil.trimString(family);
			family = StringUtil.trimQuotes(family);
			IChoiceSet choiceSet = defn.getAllowedChoices();
			assert choiceSet != null;

			// general font family need not double quotes
			if (choiceSet.findChoice(family) != null) {
				values.add(family);
			} else { // custom font family need double quotes
				values.add("\"" + family + "\""); //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < values.size(); i++) {
			if (i != 0) {
				result.append(splitter).append(" ");
			}
			result.append(values.get(i));
		}

		return result.toString();
	}

	/**
	 * Transfer Css Style to Customer Style.
	 *
	 * @param module
	 * @param cssStyleHandle
	 * @return the shared style handle from the css style handle
	 */
	public static SharedStyleHandle transferCssStyleToSharedStyle(Module module, SharedStyleHandle cssStyleHandle) {
		if (cssStyleHandle == null) {
			return null;
		}
		Style newStyle = new Style(cssStyleHandle.getName());
		SharedStyleHandle styleHandle = newStyle.handle(module);
		ModelUtil.duplicateProperties(cssStyleHandle, styleHandle, false, false);
		return styleHandle;
	}

	/**
	 * Checks all style names in styles list exist in styleList or not
	 *
	 * @param styleList style list , each item is <code>StyleElement</code>
	 * @param name      style name
	 * @return if exist return true; else return false;
	 */

	public static int getStylePosition(List<? extends StyleElement> styleList, String name) {
		for (int i = 0; i < styleList.size(); ++i) {
			StyleElement style = styleList.get(i);
			String styleName = style.getName();

			if (styleName.equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}
}
