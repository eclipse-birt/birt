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

package org.eclipse.birt.data.engine.core;

import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

import com.ibm.icu.util.ULocale;

/**
 * Implementation of BirtException in DtE project.
 */
public class DataException extends BirtException {

	private static ULocale currentLocale;

	/** pluginId, probably this value should be obtained externally */
	private final static String _pluginId = "org.eclipse.birt.data";

	/** serialVersionUID */
	private static final long serialVersionUID = 8571109940669957243L;

	private static ResourceBundle resourceBundle;

	/*
	 * @see BirtException(errorCode)
	 */
	public DataException(String errorCode) {
		super(_pluginId, errorCode, getResourceBundle());
	}

	/**
	 * Support provided additional parameter
	 * 
	 * @param errorCode
	 * @param argv
	 */
	public DataException(String errorCode, Object argv) {
		super(_pluginId, errorCode, argv, getResourceBundle());
	}

	/**
	 * Support provided additional parameter
	 * 
	 * @param errorCode
	 * @param argv[]
	 */
	public DataException(String errorCode, Object argv[]) {
		super(_pluginId, errorCode, argv, getResourceBundle());

	}

	/*
	 * @see BirtException(message, errorCode)
	 */
	public DataException(String errorCode, Throwable cause) {
		super(_pluginId, errorCode, getResourceBundle(), cause);
	}

	public DataException(String errorCode, Throwable cause, Object argv) {
		super(_pluginId, errorCode, argv, getResourceBundle(), cause);
	}

	public DataException(String errorCode, Throwable cause, Object argv[]) {
		super(_pluginId, errorCode, argv, getResourceBundle(), cause);
	}

	/*
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		return getMessage();
	}

	public Object[] getArgument() {
		return this.oaMessageArguments;
	}

	/*
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		String msg = super.getMessage();

		// Dte frequently wraps exceptions
		// Concatenate error from initCause if available
		if (this.getCause() != null) {
			String extraMsg = this.getCause().getLocalizedMessage();
			if (extraMsg != null && extraMsg.length() > 0)
				msg += "\n" + extraMsg;
		}
		return msg;
	}

	/**
	 * Wraps a BirtException in a DataException
	 */
	public static DataException wrap(BirtException e) {
		if (e instanceof DataException)
			return (DataException) e;
		return new DataException(ResourceConstants.WRAPPED_BIRT_EXCEPTION, e);
	}

	/**
	 * Set the locale info
	 * 
	 * @param locale
	 */
	public static void setLocale(ULocale locale) {
		currentLocale = locale;
		if (resourceBundle != null) {
			synchronized (DataException.class) {
				if (resourceBundle == null)
					return;

				if ((locale == null && !ULocale.getDefault().toLocale().equals(resourceBundle.getLocale()))
						|| (locale != null && !locale.toLocale().equals(resourceBundle.getLocale()))) {
					resourceBundle = null;
				}
			}
		}
	}

	/**
	 * Get resourceBundle based on given locale
	 * 
	 * @return
	 */
	private static ResourceBundle getResourceBundle() {
		if (resourceBundle == null) {
			synchronized (DataException.class) {
				if (resourceBundle != null)
					return resourceBundle;
				if (currentLocale != null)
					resourceBundle = DataResourceHandle.getInstance(currentLocale).getUResourceBundle();
				else
					resourceBundle = DataResourceHandle.getInstance().getUResourceBundle();
			}
		}
		return resourceBundle;
	}
}
