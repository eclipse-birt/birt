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

package org.eclipse.birt.report.designer.ui.editors;

import org.eclipse.birt.report.designer.ui.editors.actions.MultiPageEditorActionBarContributor;

/**
 * Action bar contributor for report editor
 */

public class ReportActionBarContributor extends MultiPageEditorActionBarContributor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.actions.
	 * EditorsActionBarContributor#getEditorId()
	 */
	public String getEditorId() {
		return ReportEditorProxy.REPROT_EDITOR_ID;
	}

}
