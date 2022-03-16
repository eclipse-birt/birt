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

package org.eclipse.birt.report.debug.internal.script.model;

/**
 * IScriptConstants
 */
public interface IScriptConstants {
	/**
	 * Script debug ID.
	 */
	String SCRIPT_DEBUG_MODEL = "org.eclipse.birt.report.debug.script.model"; //$NON-NLS-1$

	/**
	 * Script debug program
	 */
	String ATTR_REPORT_PROGRAM = SCRIPT_DEBUG_MODEL + ".ATTR_REPORT_PROGRAM"; //$NON-NLS-1$
}
