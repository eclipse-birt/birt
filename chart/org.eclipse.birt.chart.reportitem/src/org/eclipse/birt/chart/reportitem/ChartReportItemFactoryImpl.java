/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.Locale;

import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItemFactory;

import com.ibm.icu.util.ULocale;

/**
 * ChartReportItemFactoryImpl
 */
public class ChartReportItemFactoryImpl extends ReportItemFactory implements IMessages {

	/**
	 * The constructor.
	 */
	public ChartReportItemFactoryImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IReportItemFactory#newReportItem(org.
	 * eclipse.birt.report.model.api.ReportDesignHandle)
	 */
	public IReportItem newReportItem(DesignElementHandle item) {
		assert item instanceof ExtendedItemHandle;
		return ChartReportItemUtil.instanceChartReportItem((ExtendedItemHandle) item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItemFactory#getMessages()
	 */
	public IMessages getMessages() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IMessages#getMessage(java.lang.
	 * String, java.util.Locale)
	 */
	public String getMessage(String key, Locale locale) {
		return Messages.getString(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IMessages#getMessage(java.lang.
	 * String, com.ibm.icu.util.ULocale)
	 */

	public String getMessage(String key, ULocale locale) {
		return Messages.getString(key);
	}
}