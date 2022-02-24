/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt;

import java.util.Locale;

import org.eclipse.birt.chart.factory.IMessageLookup;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;

import com.ibm.icu.util.ULocale;

/**
 * An {@link IMessageLookup} implementation for use in chart builder.
 */
public class ChartBuilderMessageLookup implements IMessageLookup {

	private IUIServiceProvider serviceProvider;

	public ChartBuilderMessageLookup(IUIServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IMessageLookup#getMessageValue(java.lang.
	 * String, java.util.Locale)
	 */
	public String getMessageValue(String sKey, Locale lcl) {
		if (serviceProvider == null)
			return sKey;
		return serviceProvider.getValue(sKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IMessageLookup#getMessageValue(java.lang.
	 * String, com.ibm.icu.util.ULocale)
	 */
	public String getMessageValue(String sKey, ULocale lcl) {
		if (serviceProvider == null)
			return sKey;
		return serviceProvider.getValue(sKey);
	}

}
