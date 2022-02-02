/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.ui.impl.providers;

import java.util.List;

import org.eclipse.birt.data.oda.pojo.ui.impl.models.ClassPathElement;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class ClassPathTableProvider implements ITableLabelProvider, IStructuredContentProvider {

	public ClassPathTableProvider() {

	}

	public Image getColumnImage(Object arg0, int arg1) {
		if (arg1 == 0 && (arg0 instanceof ClassPathElement)) {
			return ((ClassPathElement) arg0).getIcon();
		}
		return null;
	}

	public String getColumnText(Object arg0, int arg1) {
		if (arg1 == 0 && (arg0 instanceof ClassPathElement)) {
			return getDisplayText((ClassPathElement) arg0);
		}
		return null;
	}

	private String getDisplayText(ClassPathElement element) {
		String value = element.getValue();
		if (value != null) {
			value = value + " - " + element.getFullPath(); //$NON-NLS-1$
		}
		return value;
	}

	public void addListener(ILabelProviderListener arg0) {

	}

	public void dispose() {

	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {

	}

	public Object[] getElements(Object arg0) {
		if (arg0 instanceof List)
			return ((List) arg0).toArray();
		return new Object[0];
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

	}

}
