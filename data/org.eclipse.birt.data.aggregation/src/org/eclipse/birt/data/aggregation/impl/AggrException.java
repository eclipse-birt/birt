/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.aggregation.impl;

import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;

/**
 *
 */

public class AggrException extends BirtException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1336188710963460825L;
	private static final String _pluginId = "org.eclipse.birt.data.aggregation";//$NON-NLS-1$
	private static final String BUNDLE_NAME = "org.eclipse.birt.data.aggregation.i18n.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * @param errorCode
	 */
	public AggrException(String errorCode) {
		super(_pluginId, errorCode, RESOURCE_BUNDLE);
	}

	/**
	 * @param errorCode
	 * @param cause
	 */
	public AggrException(String errorCode, Throwable cause) {
		super(_pluginId, errorCode, RESOURCE_BUNDLE, cause);
	}

	/**
	 * @param errorCode
	 * @param args
	 * @param cause
	 */
	public AggrException(String errorCode, Object[] args, Throwable cause) {
		super(_pluginId, errorCode, args, RESOURCE_BUNDLE, cause);
	}

	/**
	 * @param errorCode
	 * @param arg0
	 * @param bundle
	 * @param cause
	 */
	public AggrException(String errorCode, Object arg0, Throwable cause) {
		super(_pluginId, errorCode, arg0, RESOURCE_BUNDLE, cause);
	}

	/**
	 * @param errorCode
	 * @param args
	 */
	public AggrException(String errorCode, Object[] args) {
		super(_pluginId, errorCode, args, RESOURCE_BUNDLE);
	}

	/**
	 * @param errorCode
	 * @param arg0
	 * @param bundle
	 */
	public AggrException(String errorCode, Object arg0) {
		super(_pluginId, errorCode, arg0, RESOURCE_BUNDLE);
	}

}
