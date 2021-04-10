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

package org.eclipse.birt.report.designer.ui.viewer;

import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 */

public class TOCContentProvider implements ITreeContentProvider {

	private static Object[] EMPTY = new Object[0];

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof TOCNode) {
			return ((TOCNode) parentElement).getChildren().toArray();
		}
		return EMPTY;
	}

	public Object getParent(Object element) {
		if (element instanceof TOCNode) {
			return ((TOCNode) element).getParent();
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof TOCNode) {
			return ((TOCNode) element).getChildren().size() > 0;
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// viewer.setSelection( null );
	}

}
