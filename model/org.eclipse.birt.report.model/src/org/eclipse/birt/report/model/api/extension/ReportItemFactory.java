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

package org.eclipse.birt.report.model.api.extension;

import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Adapter class for the IReportItemFactory interface.
 */

abstract public class ReportItemFactory implements IReportItemFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IReportItemFactory#newReportItem(org.
	 * eclipse.birt.report.model.api.ReportDesignHandle)
	 */

	abstract public IReportItem newReportItem(DesignElementHandle extendedItemHandle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItemFactory#getMessages()
	 */
	abstract public IMessages getMessages();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IReportItemFactory#
	 * getFactoryStyles(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public IStyleDeclaration[] getFactoryStyles(String extensionName) {
		return null;
	}

}
