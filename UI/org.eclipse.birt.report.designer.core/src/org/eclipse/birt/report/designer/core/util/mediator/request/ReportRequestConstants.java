/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.core.util.mediator.request;

/**
 * ReportRequestConstants
 */
public interface ReportRequestConstants {

	/**
	 * Selection request <code>SELECTION</code>
	 */
	String SELECTION = "selection"; //$NON-NLS-1$

	/**
	 * Open editor request. <code>OPEN_EDITOR</code>
	 */
	String OPEN_EDITOR = "open editor"; //$NON-NLS-1$

	/**
	 * Open editor request. <code>OPEN_EDITOR</code>
	 */
	String LOAD_MASTERPAGE = "load masterpage"; //$NON-NLS-1$

	/**
	 * Create element request. <code>CREATE_ELEMENT</code>
	 */
	String CREATE_ELEMENT = "create element"; //$NON-NLS-1$

	/**
	 * Added for fixing bugs 144165 and 151317 Create scalarparameter or
	 * resultsetcolumn request.
	 */
	String CREATE_SCALARPARAMETER_OR_RESULTSETCOLUMN = "create scalarparameter or resultsetcolumn"; //$NON-NLS-1$

}
