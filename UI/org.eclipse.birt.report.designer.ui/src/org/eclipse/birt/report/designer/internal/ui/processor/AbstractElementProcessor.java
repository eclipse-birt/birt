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

package org.eclipse.birt.report.designer.internal.ui.processor;

import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;

/**
 * The abstract class for processors
 */

public abstract class AbstractElementProcessor implements IElementProcessor {

	private String elementType;

	protected AbstractElementProcessor(String elementType) {
		this.elementType = elementType;
	}

	/**
	 * Gets the type of the element to process
	 * 
	 * @return Returns the type of the element to process
	 */
	public String getElementType() {
		return elementType;
	}

	/**
	 * Gets the element factory to create elements
	 * 
	 * @return Returns the element factory of the current report design.
	 */

	protected static ElementFactory getElementFactory() {
		return SessionHandleAdapter.getInstance().getReportDesignHandle().getElementFactory();
	}

	/**
	 * Gets the new name for the new element from the extended data
	 * 
	 * @param extendedData the extended data for creation
	 * @return Returns the new name for the new element
	 */
	protected static String getNewName(Object extendedData) {
		String newName = null;
		if (extendedData instanceof String) {
			newName = (String) extendedData;
		} else if (extendedData instanceof Map) {
			newName = (String) ((Map) extendedData).get(ELEMENT_NAME);
		}
		return newName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.processor.IElementProcessor#
	 * getCreateTransactionLabel()
	 */
	public String getCreateTransactionLabel() {
		return MessageFormat.format("Create {0}", new Object[] { //$NON-NLS-1$
				elementType });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.processor.IElementProcessor#
	 * getEditeTransactionLabel()
	 */
	public String getEditeTransactionLabel(DesignElementHandle handle) {
		return MessageFormat.format("Edit {0}", new Object[] { //$NON-NLS-1$
				DEUtil.getDisplayLabel(handle) });
	}

}
