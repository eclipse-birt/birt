/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for the TableViewer
 */

public class ElementNameLabelProvider extends LabelProvider implements ITableLabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.
	 * Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 * int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		String result = ""; //$NON-NLS-1$
		ItemContent content = (ItemContent) element;
		switch (columnIndex) {
		case 0: // Get default Name
			result = content.getDisplayName();
			break;
		case 1: // Get Custom Name
			result = content.getCustomName();
			break;
		case 2: // Get the description
			result = content.getDescription();
			break;
		default:
			break;
		}
		return result;
	}

}
