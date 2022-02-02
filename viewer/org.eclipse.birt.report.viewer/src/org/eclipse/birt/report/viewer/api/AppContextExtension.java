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

package org.eclipse.birt.report.viewer.api;

import java.util.Map;

/**
 * This class can be extended to pass user-defined appcontext into report design
 * 
 */
public class AppContextExtension {

	/**
	 * Returns the name
	 * 
	 * @return
	 */
	public String getName() {
		return "ViewerAppContext"; //$NON-NLS-1$
	}

	/**
	 * Returns the appcontext object
	 * 
	 * @param appContext
	 * @return
	 */
	public Map getAppContext(Map appContext) {
		return appContext;
	}
}
