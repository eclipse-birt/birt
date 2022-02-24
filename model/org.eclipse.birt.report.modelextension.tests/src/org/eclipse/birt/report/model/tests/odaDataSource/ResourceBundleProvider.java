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

package org.eclipse.birt.report.model.tests.odaDataSource;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.report.model.api.extension.IResourceBundleProvider;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * Implements <code>IResourceBundleProvider</code> for testing
 */

public class ResourceBundleProvider implements IResourceBundleProvider {

	private static final String BASE_NAME = "org.eclipse.birt.report.model.tests.odaDataSource.Messages"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IResourceBundleProvider#
	 * getResourceBundle(java.util.Locale)
	 */
	public ResourceBundle getResourceBundle(Locale locale) {
		return ResourceBundle.getBundle(BASE_NAME, locale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IResourceBundleProvider#
	 * getResourceBundle(java.util.Locale)
	 */
	public UResourceBundle getResourceBundle(ULocale locale) {
		return UResourceBundle.getBundleInstance(BASE_NAME, locale);
	}
}
