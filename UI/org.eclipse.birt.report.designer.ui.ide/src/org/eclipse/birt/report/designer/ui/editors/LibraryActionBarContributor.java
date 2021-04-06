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

package org.eclipse.birt.report.designer.ui.editors;

import org.eclipse.birt.report.designer.ui.editors.actions.MultiPageEditorActionBarContributor;

/**
 * Action bar contributor for library editor
 */

public class LibraryActionBarContributor extends MultiPageEditorActionBarContributor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.actions.
	 * EditorsActionBarContributor#getEditorId()
	 */
	public String getEditorId() {
		return ReportEditorProxy.LIBRARY_EDITOR_ID;
	}

}
