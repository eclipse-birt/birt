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
