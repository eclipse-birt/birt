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

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Factory class to create a display label for transaction command.
 */
public class CommandLabelFactory implements MessageConstants {

	/**
	 * Gets a command label for the given key and arguments. This method will check
	 * the argument length for the specified message key.
	 * 
	 * @param messageKey
	 * @param args
	 * @return command label
	 */
	public static String getCommandLabel(String messageKey, String[] args) {
		if (StringUtil.isEmpty(messageKey))
			return null;

		// message without any arguments
		if (messageKey.equalsIgnoreCase(NAME_PREFIX_NEW_MESSAGE)
				|| messageKey.equalsIgnoreCase(CHANGE_PROPERTY_DEFINITION_MESSAGE)
				|| MOVE_ITEM_MESSAGE.equalsIgnoreCase(messageKey)
				|| messageKey.equalsIgnoreCase(ADD_TRANSLATION_MESSAGE)
				|| messageKey.equalsIgnoreCase(DROP_TRANSLATION_MESSAGE)
				|| messageKey.equalsIgnoreCase(CHANGE_TRANSLATION_MESSAGE)
				|| messageKey.equalsIgnoreCase(MOVE_CONTENT_MESSAGE)
				|| messageKey.equalsIgnoreCase(ADD_PROPERTY_MESSAGE)
				|| messageKey.equalsIgnoreCase(DROP_PROPERTY_MESSAGE) || messageKey.equalsIgnoreCase(SET_NAME_MESSAGE)
				|| messageKey.equalsIgnoreCase(ADD_ELEMENT_MESSAGE) || messageKey.equalsIgnoreCase(DROP_ELEMENT_MESSAGE)
				|| messageKey.equalsIgnoreCase(SET_STYLE_MESSAGE) || messageKey.equalsIgnoreCase(REPLACE_ITEM_MESSAGE)
				|| messageKey.equalsIgnoreCase(SET_LOCALE_MESSAGE)
				|| messageKey.equalsIgnoreCase(SET_TRANSLATION_TEXT_MESSAGE)
				|| messageKey.equalsIgnoreCase(MOVE_ELEMENT_MESSAGE)
				|| messageKey.equalsIgnoreCase(INSERT_ELEMENT_MESSAGE)
				|| messageKey.equalsIgnoreCase(DELETE_ELEMENT_MESSAGE)
				|| messageKey.equalsIgnoreCase(SET_EXTENDS_MESSAGE) || messageKey.equalsIgnoreCase(CHANGE_ITEM_MESSAGE)
				|| messageKey.equalsIgnoreCase(ADD_ITEM_MESSAGE) || messageKey.equalsIgnoreCase(INSERT_ITEM_MESSAGE)
				|| messageKey.equalsIgnoreCase(REMOVE_ITEM_MESSAGE)
				|| messageKey.equalsIgnoreCase(REPLACE_ELEMENT_MESSAGE)
				|| messageKey.equalsIgnoreCase(CREATE_TEMPLATE_ELEMENT_MESSAGE)
				|| messageKey.equalsIgnoreCase(TRANSFORM_TO_DATA_SET_MESSAGE)
				|| messageKey.equalsIgnoreCase(TRANSFORM_TO_REPORT_ITEM_MESSAGE)
				|| messageKey.equalsIgnoreCase(SET_THEME_MESSAGE)
				|| messageKey.equalsIgnoreCase(RENAME_CSS_FILE_MESSAGE)
				|| messageKey.equalsIgnoreCase(IMPORT_CSS_STYLES_MESSAGE)
				|| messageKey.equalsIgnoreCase(INSERT_AND_PASTE_COLUMN_BAND_MESSAGE)
				|| messageKey.equalsIgnoreCase(PASTE_COLUMN_BAND_MESSAGE)
				|| messageKey.equalsIgnoreCase(SHIFT_COLUMN_BAND_MESSAGE)
				|| messageKey.equalsIgnoreCase(INSERT_COLUMN_BAND_MESSAGE)
				|| messageKey.equalsIgnoreCase(INSERT_ROW_MESSAGE)
				|| messageKey.equalsIgnoreCase(INSERT_AND_PASTE_ROW_MESSAGE)
				|| messageKey.equalsIgnoreCase(PASTE_ROW_MESSAGE) || messageKey.equalsIgnoreCase(SHIFT_ROW_MESSAGE)
				|| messageKey.equalsIgnoreCase(CLEAR_PROPERTIES_MESSAGE))
			return ModelMessages.getMessage(messageKey);

		if (messageKey.equalsIgnoreCase(CHANGE_PROPERTY_MESSAGE)
				|| messageKey.equalsIgnoreCase(CHANGE_PROPERTY_ENCRYPTION_MESSAGE)) {
			if (args == null || args.length != 1) {
				assert false;
				throw new IllegalArgumentException("message argument length is not correct!"); //$NON-NLS-1$
			}
			return ModelMessages.getMessage(messageKey, args);
		}

		return null;
	}

	/**
	 * Gets the command label for the given message key.
	 * 
	 * @param messageKey
	 * @return command label
	 */
	public static String getCommandLabel(String messageKey) {
		return getCommandLabel(messageKey, null);
	}
}
