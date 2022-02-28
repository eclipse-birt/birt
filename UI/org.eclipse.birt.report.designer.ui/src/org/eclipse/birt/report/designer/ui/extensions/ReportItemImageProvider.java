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
	@Override
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
	@Override
	public void disposeImage(ExtendedItemHandle handle, Image image) {

	}

}
