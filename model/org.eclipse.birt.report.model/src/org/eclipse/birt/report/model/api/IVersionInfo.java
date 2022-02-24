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

package org.eclipse.birt.report.model.api;

/**
 * Holds instrustions that inform the user if opening the old version design
 * file may cause some auto-conversion to the original design file.
 */

public interface IVersionInfo {

	/**
	 * Returns the localized message about version infomation.
	 * 
	 * @return the localized message.
	 */

	public String getLocalizedMessage();

	/**
	 * Returns the version.
	 * 
	 * @return the the version.
	 */

	public String getDesignFileVersion();
}
