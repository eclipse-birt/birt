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

package org.eclipse.birt.report.model.tests.table;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Implements <code>IReportItemFactory</code> for testing
 */

public class ReportItemFactoryImpl implements IReportItemFactory {

	/**
	 * Messages for I18N
	 */

	static IMessages messages = new MessagesImpl();

	/**
	 * Default constructor
	 */

	public ReportItemFactoryImpl() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.model.extension.IElementFactory#newElement(org.eclipse.birt.
	 * model.api.ReportDesignHandle)
	 */

	@Override
	public IReportItem newReportItem(DesignElementHandle item) {
		return new ReportItemImpl(this, item);
	}

	/**
	 * Returns the localized message.
	 *
	 * @param key the resource key
	 * @return the localized message
	 */

	public static String getMessage(String key) {
		return messages.getMessage(key, ThreadResources.getLocale());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IReportItemFactory#getMessages()
	 */

	@Override
	public IMessages getMessages() {
		return messages;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IReportItemFactory#
	 * getFactoryStyles(java.lang.String)
	 */
	@Override
	public IStyleDeclaration[] getFactoryStyles(String extensionName) {
		return null;
	}
}
