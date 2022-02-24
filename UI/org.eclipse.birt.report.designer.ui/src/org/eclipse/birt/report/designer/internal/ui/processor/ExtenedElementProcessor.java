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

package org.eclipse.birt.report.designer.internal.ui.processor;

import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.window.Window;

/**
 * 
 */

public class ExtenedElementProcessor extends AbstractElementProcessor {

	/**
	 * Creates a new instance of this processor for extension elements
	 * 
	 * @param elementType the type of the extended element
	 */
	ExtenedElementProcessor(String elementType) {
		super(elementType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.processor.IElementProcessor#
	 * createElement(java.lang.Object)
	 */
	public DesignElementHandle createElement(Object extendedData) {
		// ExtendedItemHandle handle = getElementFactory( ).newExtendedItem(
		// getNewName( extendedData ),
		// getElementType( ) );
		ExtendedItemHandle handle = DesignElementFactory.getInstance().newExtendedItem(getNewName(extendedData),
				getElementType());
		return handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.processor.IElementProcessor#
	 * editElement(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public boolean editElement(DesignElementHandle handle) {
		if (getBuilder() != null) {
			return getBuilder().open((ExtendedItemHandle) handle) == Window.OK;
		}
		return true;
	}

	private IReportItemBuilderUI getBuilder() {
		ExtendedElementUIPoint point = ExtensionPointManager.getInstance().getExtendedElementPoint(getElementType());
		if (point != null) {
			return point.getReportItemBuilderUI();
		}
		return null;

	}
}
