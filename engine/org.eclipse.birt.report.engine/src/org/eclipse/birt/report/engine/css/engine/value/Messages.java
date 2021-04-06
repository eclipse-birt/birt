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

package org.eclipse.birt.report.engine.css.engine.value;

import com.ibm.icu.text.MessageFormat;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;

import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;

//TODO: review use birt localize.
/**
 * This class manages the message for the css.engine.value module.
 */
public class Messages {

	/**
	 * This class does not need to be instantiated.
	 */
	protected Messages() {
	}

	/**
	 * The error messages bundle class name.
	 */
	protected final static String RESOURCES = "org.eclipse.birt.report.css.engine.value.resources.Messages";

	/**
	 * The localizable support for the error messages.
	 */
	static protected UResourceBundle rb = new EngineResourceHandle(ULocale.getDefault()).getUResourceBundle();

	/**
	 * set the locale of message.
	 * 
	 * @param l locale used to format the message.
	 */
	public static void setLocale(Locale l) {
	}

	/**
	 * get the locale of message.
	 * 
	 * @return locale of the message.
	 */
	public static Locale getLocale() {
		return rb.getLocale();
	}

	/**
	 * format the message.
	 * 
	 * @param key  message key.
	 * @param args messsage arguments.
	 * @return the message.
	 * @throws MissingResourceException
	 */
	public static String formatMessage(String key, Object[] args) throws MissingResourceException {

		String localizedMessage = rb.getString(key);
		MessageFormat form = new MessageFormat(localizedMessage);
		return form.format(args);
	}
}
