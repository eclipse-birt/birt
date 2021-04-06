/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.command.CustomMsgCommand;
import org.eclipse.birt.report.model.elements.Translation;

/**
 * Represents a translation message in the design. A translation message is used
 * for the user to save their localized information in the design.
 * 
 * @see org.eclipse.birt.report.model.elements.TranslationTable
 */

public class TranslationHandle extends ElementDetailHandle {

	/**
	 * The translation message.
	 */

	protected Translation translation = null;

	/**
	 * Constructs a handle for a translation message.
	 * 
	 * @param moduleHandle a handle to a module
	 * @param trans        the translation instance to be handled.
	 */

	public TranslationHandle(ModuleHandle moduleHandle, Translation trans) {
		super(moduleHandle);

		assert trans != null;
		this.translation = trans;
	}

	/**
	 * Returns the resource key of the translation.
	 * 
	 * @return the resource key for the translation
	 */

	public String getResourceKey() {
		return translation.getResourceKey();
	}

	/**
	 * Sets the locale of the translation message.
	 * 
	 * @param newLocale new locale of the translation
	 * 
	 * @throws CustomMsgException if the translation message is not found in the
	 *                            design.
	 */

	public void setLocale(String newLocale) throws CustomMsgException {
		CustomMsgCommand command = new CustomMsgCommand(getModule());
		command.setLocale(translation, newLocale);
	}

	/**
	 * Returns the locale of the translation message.
	 * 
	 * @return the locale of the translation message
	 */

	public String getLocale() {
		return translation.getLocale();
	}

	/**
	 * Sets the translated text for the translation message.
	 * 
	 * @param text translated text for the locale.
	 * @throws CustomMsgException if the translation message is not found in the
	 *                            design.
	 */

	public void setText(String text) throws CustomMsgException {
		CustomMsgCommand command = new CustomMsgCommand(getModule());
		command.setText(translation, text);
	}

	/**
	 * Returns translated text for the translation, the text defined for the locale.
	 * 
	 * @return translated text for the translation
	 */

	public String getText() {
		return translation.getText();
	}

}