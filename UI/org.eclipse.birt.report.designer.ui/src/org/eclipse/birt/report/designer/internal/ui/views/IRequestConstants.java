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

package org.eclipse.birt.report.designer.internal.ui.views;

/**
 * The costants used by request
 */

public interface IRequestConstants {

	String REQUEST_KEY_RESULT = "result"; //$NON-NLS-1$

	String REQUEST_TYPE_INSERT = "insert"; //$NON-NLS-1$

	String REQUEST_TYPE_EDIT = "edit"; //$NON-NLS-1$

	String REQUEST_CREATE_PLACEHOLDER = "create-placeholder"; //$NON-NLS-1$

	String REQUEST_TRANSFER_PLACEHOLDER = "transfer-placeholder"; //$NON-NLS-1$

	// String REQUST_REVERT_TEMPLATE ="revert-template"; //$NON-NLS-1$

	String REQUST_REVERT_TO_REPORTITEM = "revert-to-reportitem"; //$NON-NLS-1$
	String REQUST_REVERT_TO_TEMPLATEITEM = "revert-to-templateitem"; //$NON-NLS-1$

	/**
	 * @deprecated
	 */
	String REQUEST_TYPE_RENAME = "rename"; //$NON-NLS-1$

	String REQUEST_TYPE_DELETE = "delete"; //$NON-NLS-1$

	String REQUEST_KEY_INSERT_SLOT = "insert-slot"; //$NON-NLS-1$

	String REQUEST_KEY_INSERT_PROPERTY = "insert-property"; //$NON-NLS-1$

	String REQUEST_KEY_INSERT_TYPE = "insert-type"; //$NON-NLS-1$

	String REQUEST_KEY_INSERT_POSITION = "insert-position"; //$NON-NLS-1$

	/**
	 * @deprecated
	 */
	String REQUEST_KEY_RENAME_NEWNAME = "rename-newname"; //$NON-NLS-1$

	String REQUEST_CHANGE_DATA_COLUMN = "change-data-column"; //$NON-NLS-1$
}
