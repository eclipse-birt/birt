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
