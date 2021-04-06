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

	public IReportItem newReportItem(DesignElementHandle item) {
		return new ReportItemImpl(this, item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IReportItemFactory#getMessages()
	 */

	public IMessages getMessages() {
		return null;
	}

	/**
	 * returns the predefined style for box element.
	 */
	public IStyleDeclaration[] getFactoryStyles(String extensionName) {
		IStyleDeclaration[] defaultStyles = null;
		defaultStyles = new BoxStyle[2];

		defaultStyles[0] = new BoxStyle("BoxStyle");

		defaultStyles[1] = new BoxStyle(null);

		return defaultStyles;
	}
}
