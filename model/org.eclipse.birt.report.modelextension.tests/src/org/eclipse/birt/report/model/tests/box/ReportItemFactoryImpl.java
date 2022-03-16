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

package org.eclipse.birt.report.model.tests.box;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;

/**
 * Implements <code>IChoiceDefinition</code> for testing
 */

public class ReportItemFactoryImpl implements IReportItemFactory {

	/**
	 * Default constructs.
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IReportItemFactory#getMessages()
	 */

	@Override
	public IMessages getMessages() {
		return null;
	}

	/**
	 * returns the predefined style for box element.
	 */
	@Override
	public IStyleDeclaration[] getFactoryStyles(String extensionName) {
		IStyleDeclaration[] defaultStyles;
		defaultStyles = new BoxStyle[2];

		defaultStyles[0] = new BoxStyle("BoxStyle");

		defaultStyles[1] = new BoxStyle(null);

		return defaultStyles;
	}
}
