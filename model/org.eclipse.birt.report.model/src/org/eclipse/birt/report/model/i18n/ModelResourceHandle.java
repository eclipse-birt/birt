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

package org.eclipse.birt.report.model.i18n;

import com.ibm.icu.util.ULocale;

/**
 * Represents one resource bundle for specific locale. This class is just the
 * convenient wrapper for <code>UResourceBundle</code>.
 * 
 * @see ThreadResources
 */

class ModelResourceHandle extends ResourceHandle {

	/**
	 * Constructs the resource handle with the empty locale.
	 * 
	 */

	protected ModelResourceHandle() {
		super();
	}

	/**
	 * Constructs the resource handle with a specific resource bundle, which is
	 * associated with locale.
	 * 
	 * @param locale the locale of <code>ULocale</code> type
	 */

	protected ModelResourceHandle(ULocale locale) {
		super(locale);
	}
}