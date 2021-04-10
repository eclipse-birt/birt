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

import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * The default implementation of IElementProcessor
 */

public class DefaultElementProcessor extends AbstractElementProcessor {

	/**
	 * Constructor
	 * 
	 * Creates a new instance of the default processor
	 */
	DefaultElementProcessor(String elementType) {
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
		// DesignElementHandle handle = getElementFactory( ).newElement(
		// getElementType( ),
		// getNewName( extendedData ) );
		DesignElementHandle handle = DesignElementFactory.getInstance().newElement(getElementType(),
				getNewName(extendedData));
		if (initElement(handle, extendedData)) {
			return handle;
		}
		return null;
	}

	/**
	 * Initializes a new element. The default implementation does nothing.
	 * Subclasses can extend this method to do the initialization.
	 * 
	 * @param handle     The handle of the new element to initialize
	 * @param extendData The extend data for initialize
	 * @return Returns true if the initialization succeeded, or false if it failed
	 *         or cancelled.
	 */
	protected boolean initElement(DesignElementHandle handle, Object extendedData) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.processor.IElementProcessor#
	 * editElement(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public boolean editElement(DesignElementHandle handle) {
		return false;
	}

}
