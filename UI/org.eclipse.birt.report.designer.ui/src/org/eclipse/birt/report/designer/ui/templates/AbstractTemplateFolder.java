/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.templates;

import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;

/**
 * Abstract class for the ITemplateFolder
 */

public abstract class AbstractTemplateFolder implements ITemplateFolder {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.templates.ITemplateEntry#getImage()
	 */
	public Image getImage() {
		return ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJ_FOLDER);
	}
}
