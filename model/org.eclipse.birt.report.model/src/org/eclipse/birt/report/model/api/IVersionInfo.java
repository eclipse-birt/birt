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
