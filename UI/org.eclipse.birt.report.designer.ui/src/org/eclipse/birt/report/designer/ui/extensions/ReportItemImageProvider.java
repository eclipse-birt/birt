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
import org.eclipse.swt.graphics.Image;

/**
 */
public class ReportItemImageProvider implements IReportItemImageProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemImageProvider#
	 * getImage(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public Image getImage(ExtendedItemHandle handle) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemImageProvider#
	 * disposeImage(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 * org.eclipse.swt.graphics.Image)
	 */
	public void disposeImage(ExtendedItemHandle handle, Image image) {

	}

}
