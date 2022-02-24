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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Translation;

/**
 * Provides the command to create, modify and delete custom-defined messages.
 * 
 */

public class CustomMsgCommand extends AbstractElementCommand {

	/**
	 * Constructs the user-defined message command.
	 * 
	 * @param module the module to change
	 */

	public CustomMsgCommand(Module module) {
		super(module, module);
	}

	/**
	 * Adds a translation to user-defined message.
	 * 
	 * @param resourceKey resource key for the message
	 * @param locale      the string value of a locale for the translation. Locale
	 *                    should be in java-defined format( en, en-US, zh_CN, etc.)
	 * @param text        translated text for the locale.
	 * @throws CustomMsgException if the resource key is duplicate or missing, or
	 *                            locale is not a valid format.
	 */

	public void addTranslation(String resourceKey, String locale, String text) throws CustomMsgException {
		assert module != null;

		// resource key required.

		if (StringUtil.trimString(resourceKey) == null)
			throw new CustomMsgException(element, CustomMsgException.DESIGN_EXCEPTION_RESOURCE_KEY_REQUIRED);

		// check well-form locale e.g: en_US, zh_CN

		if (!StringUtil.isValidLocale(locale))
			throw new CustomMsgException(element, CustomMsgException.DESIGN_EXCEPTION_INVALID_LOCALE);

		// check duplicated locale for one single message.

		if (module.findTranslation(resourceKey, locale) != null)
			throw new CustomMsgException(element, resourceKey, locale,
					CustomMsgException.DESIGN_EXCEPTION_DUPLICATE_LOCALE);

		// Make the changes.

		ActivityStack stack = getActivityStack();
		CustomMsgRecord msgRecord = new CustomMsgRecord((ReportDesign) element,
				new Translation(resourceKey, locale, text), CustomMsgRecord.ADD);
		stack.execute(msgRecord);
	}

	/**
	 * Drops a translation from the design.
	 * <p>
	 * 
	 * @param resourceKey resourceKey for the Translation.
	 * @param locale      locale for the translation.
	 * @throws CustomMsgException if the resource key is not provided.
	 */

	public void dropTranslation(String resourceKey, String locale) throws CustomMsgException {
		assert module != null;

		Translation translation = module.findTranslation(resourceKey, locale);

		if (translation == null)
			throw new CustomMsgException(element, CustomMsgException.DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND);

		// Make the changes.

		ActivityStack stack = getActivityStack();
		CustomMsgRecord msgRecord = new CustomMsgRecord((ReportDesign) element, translation, CustomMsgRecord.DROP);
		stack.execute(msgRecord);
	}

	/**
	 * Modifies the translation with a new locale.
	 * 
	 * @param translation old translation that needs to be changed.
	 * @param newLocale   new locale of the translation.
	 * @throws CustomMsgException   if translation is not found.
	 * @throws NullPointerException if translation is null.
	 */

	public void setLocale(Translation translation, String newLocale) throws CustomMsgException {
		assert module != null;
		assert translation != null;

		// translation should exist in the report.

		if (!module.contains(translation))
			throw new CustomMsgException(element, CustomMsgException.DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND);

		String oldLocale = translation.getLocale();
		if ((oldLocale == null && newLocale == null) || (oldLocale != null && oldLocale.equalsIgnoreCase(newLocale)))
			return;

		// check well-form locale e.g: en_US, zh_CN

		if (!StringUtil.isValidLocale(newLocale))
			throw new CustomMsgException(element, CustomMsgException.DESIGN_EXCEPTION_INVALID_LOCALE);

		// locale duplicates.

		if (module.findTranslation(translation.getResourceKey(), newLocale) != null)
			throw new CustomMsgException(element, translation.getResourceKey(), newLocale,
					CustomMsgException.DESIGN_EXCEPTION_DUPLICATE_LOCALE);

		// Make the changes.

		ActivityStack stack = getActivityStack();
		CustomMsgRecord msgRecord = new CustomMsgRecord((ReportDesign) element, translation, newLocale,
				CustomMsgRecord.CHANGE_LOCALE);
		stack.execute(msgRecord);
	}

	/**
	 * Modifies the translation with a new translation text .
	 * 
	 * @param translation old translation that needs to be changed.
	 * @param newText     new translation text for the translation.
	 * @throws CustomMsgException   if translation is not found.
	 * @throws NullPointerException if translation is null.
	 */

	public void setText(Translation translation, String newText) throws CustomMsgException {
		assert module != null;
		assert translation != null;

		// translation should exist in the report.

		if (!module.contains(translation))
			throw new CustomMsgException(element, CustomMsgException.DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND);

		ActivityStack stack = getActivityStack();
		CustomMsgRecord msgRecord = new CustomMsgRecord((ReportDesign) element, translation, newText,
				CustomMsgRecord.CHANGE_TEXT);
		stack.execute(msgRecord);
	}

}
