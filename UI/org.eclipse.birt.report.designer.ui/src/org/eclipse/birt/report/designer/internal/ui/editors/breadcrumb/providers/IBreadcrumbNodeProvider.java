/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public interface IBreadcrumbNodeProvider {

	Object getParent(Object element);

	Object[] getChildren(Object element);

	boolean hasChildren(Object element);

	Image getImage(Object element);

	String getText(Object element);

	String getTooltipText(Object element);

	void createContextMenu(Object element, IMenuManager menu);

}
