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

import java.util.Locale;

import com.ibm.icu.util.ULocale;

/**
 * Represents the design state -- a session for a user. In the Eclipse
 * environment, this represents the set of open designs. In the web environment,
 * this represents open designs and locale for the session. A session has a set
 * of default values for style properties and a default unit. The session also
 * has methods to create and open designs.
 * <p>
 * A session can also provides the mechanism for specified file searching
 * algorithm. After get an new instance of SessionHandle, an algorithm of how to
 * search a file should be set by calling
 * <code>{@link #setResourceLocator(IResourceLocator)}</code> if the default
 * resource locator is not the expected one.
 * <p>
 * The default resource locator will search in the folder where the design file
 * is located.
 * 
 * @see org.eclipse.birt.report.model.util.ResourceLocatorImpl
 * @see org.eclipse.birt.report.model.core.DesignSession
 */

public class SessionHandle extends SessionHandleImpl {

	/**
	 * Constructs a handle for the session with the given locale.
	 * 
	 * @param locale the user's locale. If null, then the system locale is assumed.
	 * 
	 * @deprecated to use ICU4J, this method is replaced by: SessionHandle(ULocale
	 *             locale)
	 */

	public SessionHandle(Locale locale) {
		super(locale);
	}

	/**
	 * Constructs a handle for the session with the given locale.
	 * 
	 * @param locale the user's locale which is <code>ULocale</code>. If null, then
	 *               the system locale is assumed.
	 */

	public SessionHandle(ULocale locale) {
		super(locale);
	}
}