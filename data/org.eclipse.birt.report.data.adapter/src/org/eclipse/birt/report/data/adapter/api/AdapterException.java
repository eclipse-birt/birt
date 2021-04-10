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

package org.eclipse.birt.report.data.adapter.api;

import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.i18n.AdapterResourceHandle;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * Implementation of BirtException in DtE project.
 */
public class AdapterException extends BirtException {
	/** static ResourceHandle */
	static protected UResourceBundle dftRb = new AdapterResourceHandle(ULocale.getDefault()).getUResourceBundle();

	static protected ThreadLocal threadLocal = new ThreadLocal();

	protected static HashMap resourceBundles = new HashMap();

	/** pluginId, probably this value should be obtained externally */
	private final static String _pluginId = "org.eclipse.birt.report.data.adapter";

	/** serialVersionUID */
	private static final long serialVersionUID = 8571109940669957243L;

	static public void setULocale(ULocale locale) {
		if (locale == null) {
			return;
		}
		UResourceBundle rb = (UResourceBundle) threadLocal.get();
		if (rb != null) {
			ULocale rbLocale = rb.getULocale();
			if (locale.equals(rbLocale)) {
				return;
			}
		}
		rb = getResourceBundle(locale);
		threadLocal.set(rb);
	}

	protected synchronized static UResourceBundle getResourceBundle(ULocale locale) {
		/* ulocale has overides the hashcode */
		UResourceBundle rb = (UResourceBundle) resourceBundles.get(locale);
		if (rb == null) {
			rb = new AdapterResourceHandle(locale).getUResourceBundle();
			if (rb != null) {
				resourceBundles.put(locale, rb);
			}
		}
		return rb;
	}

	static UResourceBundle getResourceBundle() {
		UResourceBundle rb = (UResourceBundle) threadLocal.get();
		if (rb == null) {
			return dftRb;
		}
		return rb;
	}

	/*
	 * @see BirtException(errorCode)
	 */
	public AdapterException(String errorCode) {
		super(_pluginId, errorCode, getResourceBundle());
	}

	/**
	 * Support provided additional parameter
	 * 
	 * @param errorCode
	 * @param argv
	 */
	public AdapterException(String errorCode, Object argv) {
		super(_pluginId, errorCode, argv, getResourceBundle());
	}

	/**
	 * Support provided additional parameter
	 * 
	 * @param errorCode
	 * @param argv[]
	 */
	public AdapterException(String errorCode, Object argv[]) {
		super(_pluginId, errorCode, argv, getResourceBundle());
	}

	/*
	 * @see BirtException(message, errorCode)
	 */
	public AdapterException(String errorCode, Throwable cause) {
		super(_pluginId, errorCode, getResourceBundle(), cause);
	}

	public AdapterException(String errorCode, Throwable cause, Object argv) {
		super(_pluginId, errorCode, argv, getResourceBundle(), cause);
	}

	public AdapterException(String errorCode, Throwable cause, Object argv[]) {
		super(_pluginId, errorCode, argv, getResourceBundle(), cause);
	}

	/*
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		return getMessage();
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

}
