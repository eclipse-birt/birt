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

package org.eclipse.birt.report.designer.tests.example.matrix;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItemFactory;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Implements the factory of the extension element.
 */

public class ROMExtension extends ReportItemFactory {

	/**
	 * Resource key of display name
	 */

	private static String displayNameKey = "Element.TestingBall"; //$NON-NLS-1$

	/**
	 * Messages for I18N
	 */

	static IMessages messages = new MatrixMessages();

	/**
	 * Default constructor
	 */

	public ROMExtension() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IElementFactory#getIcon()
	 */

	public Object getIcon() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.model.extension.IElementFactory#newElement(org.eclipse.birt.
	 * model.api.ReportDesignHandle)
	 */

	public IReportItem newReportItem(DesignElementHandle item) {
		return new ExtendedElement(this, item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElementFactory#getName()
	 */

	public String getName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElementFactory#getDisplayName()
	 */

	public String getDisplayName() {
		assert displayNameKey != null;

		return getMessage(displayNameKey);
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

	public IMessages getMessages() {
		return messages;
	}
}