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
