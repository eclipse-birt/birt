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
