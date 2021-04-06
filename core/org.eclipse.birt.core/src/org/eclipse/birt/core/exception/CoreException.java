/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.core.exception;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

import org.eclipse.birt.core.i18n.ResourceHandle;

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
