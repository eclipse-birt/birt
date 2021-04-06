/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
