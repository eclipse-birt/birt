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

package org.eclipse.birt.report.designer.ui.extensions;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.window.Window;

/**
 */
public class ReportItemBuilderUI implements IReportItemBuilderUI {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI#open(org.
	 * eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public int open(ExtendedItemHandle handle) {
		return Window.OK;
	}

}
