/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui;

import java.util.Locale;

import org.eclipse.birt.chart.factory.IMessageLookup;
import org.eclipse.birt.report.model.api.DesignElementHandle;

import com.ibm.icu.util.ULocale;

/**
 * An {@link IMessageLookup} implementation for use in BIRT designer.
 */
public class BIRTDesignerMessageLookup implements IMessageLookup {

	private DesignElementHandle extendedHandle;

	public BIRTDesignerMessageLookup(DesignElementHandle handle) {
		this.extendedHandle = handle;
		if (handle == null)
			throw new IllegalArgumentException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IMessageLookup#getMessageValue(java.lang.
	 * String, java.util.Locale)
	 */
	public String getMessageValue(String sKey, Locale lcl) {
		return extendedHandle.getModuleHandle().getMessage(sKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IMessageLookup#getMessageValue(java.lang.
	 * String, com.ibm.icu.util.ULocale)
	 */
	public String getMessageValue(String sKey, ULocale lcl) {
		return extendedHandle.getModuleHandle().getMessage(sKey);
	}

}
