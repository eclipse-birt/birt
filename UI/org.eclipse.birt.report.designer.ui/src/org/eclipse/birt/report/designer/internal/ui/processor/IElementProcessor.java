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

import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * The interface used to process element
 */

public interface IElementProcessor extends IProcessorConstants {

	/**
	 * Creates a new element
	 *
	 * @param extendedData the extended data for the creation
	 * @return Returns the new element, or null if the creation failed.
	 */
	DesignElementHandle createElement(Object extendedData);

	/*
	 * Gets the label for the transaction of creation
	 *
	 * @return Returns the label for the transaction of creation
	 */
	String getCreateTransactionLabel();

	/**
	 * Initializes a new element.
	 *
	 * @param handle     The handle of the new element to initialize
	 * @param extendData The extend data for initialize
	 * @return Returns true if the initialization succeeded, or false if it failed
	 *         or cancelled.
	 */
	// public boolean initElement( DesignElementHandle handle );
	/**
	 * Edit an element.
	 *
	 * @param handle The handle of the element to edit
	 * @return Returns true if the edit succeeded, or false if it failed or
	 *         cancelled.
	 */
	boolean editElement(DesignElementHandle handle);

	/**
	 * Gets the label for the transaction of edit
	 *
	 * @param handle The handle of the element to edit
	 *
	 * @return Returns the label for the transaction of edit
	 */
	String getEditeTransactionLabel(DesignElementHandle handle);
}
