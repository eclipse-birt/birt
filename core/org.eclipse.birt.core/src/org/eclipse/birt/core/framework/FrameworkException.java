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

package org.eclipse.birt.core.framework;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.i18n.ResourceHandle;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * A checked exception representing a failure.
 * <p>
 * Core exceptions contain a status object describing the cause of the
 * exception.
 * </p>
 * 
 * @see IStatus
 */
public class FrameworkException extends BirtException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9032025140135814484L;

	protected static String pluginId = "org.eclipse.birt.core";

	static protected UResourceBundle urb = new ResourceHandle(ULocale.getDefault()).getUResourceBundle();

	public FrameworkException(String errorCode) {
		super(pluginId, errorCode, urb);
	}

	public FrameworkException(String errorCode, Throwable cause) {
		super(pluginId, errorCode, urb, cause);
	}

	public FrameworkException(String errorCode, Object[] args) {
		super(pluginId, errorCode, args, urb);
	}

	public FrameworkException(String errorCode, Object[] args, Throwable cause) {
		super(pluginId, errorCode, args, urb, cause);
	}
}
