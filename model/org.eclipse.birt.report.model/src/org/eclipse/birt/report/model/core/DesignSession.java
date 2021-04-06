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

package org.eclipse.birt.report.model.core;

import com.ibm.icu.util.ULocale;

/**
 * Represents a design session for a user of the application based on the Design
 * Engine. Tracks the user's set of open designs, the user's locale, the
 * application units and so on. Also provides methods for opening and creating
 * designs.
 * <p>
 * <code>DesignSession</code> allows to specify customized resource locator to
 * search a file. This resource locator must be set before opening any
 * <code>ReportDesign</code>, so for example, when encountering a library tag in
 * the design file, the parser needs to know where to get the library file by
 * this resource locator, the code in the parser might be like the following.
 * 
 * @see org.eclipse.birt.report.model.api.SessionHandle
 */

public class DesignSession extends DesignSessionImpl {

	/**
	 * Constructor.
	 * 
	 * @param theLocale the user's locale. If null, use the system locale.
	 */

	public DesignSession(ULocale theLocale) {
		super(theLocale);
	}
}