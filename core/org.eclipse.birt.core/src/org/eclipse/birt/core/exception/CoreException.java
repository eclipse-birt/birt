/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.core.exception;

import org.eclipse.birt.core.i18n.ResourceHandle;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * Exception thrown by birt.core classes
 */
public class CoreException extends BirtException {
	/**
	 *
	 */
	private static final long serialVersionUID = 6243070026365508547L;
	static protected UResourceBundle rb = new ResourceHandle(ULocale.getDefault()).getUResourceBundle();
	static protected String PLUGIN_ID = "org.eclipse.birt.core";

	public CoreException(String errCode) {
		super(PLUGIN_ID, errCode, rb);
	}

	public CoreException(String errCode, Object arg0) {
		super(PLUGIN_ID, errCode, arg0, rb);
	}

	public CoreException(String errCode, Object[] args) {
		super(PLUGIN_ID, errCode, args, rb);
	}

	public CoreException(String errCode, Object arg0, Throwable cause) {
		super(PLUGIN_ID, errCode, arg0, rb, cause);
	}

	public CoreException(String errCode, Object[] args, Throwable cause) {
		super(PLUGIN_ID, errCode, args, rb, cause);
	}
}
