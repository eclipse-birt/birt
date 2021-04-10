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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public interface IReportImageDecorator {
	/**
	 * @param image
	 * @param element
	 * @return
	 */
	Image decorateImage(Image image, Object element);

}
