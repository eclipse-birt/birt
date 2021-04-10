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

package org.eclipse.birt.report.designer.internal.ui.views.outline;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Sorts outline tree's treeNode
 * 
 * 
 */
public class ItemSorter extends ViewerSorter {

	/**
	 * Returns a negative, zero, or positive number depending on whether the first
	 * element is less than, equal to, or greater than the second element.
	 * <p>
	 * The default implementation of this method is based on comparing the elements'
	 * categories as computed by the <code>category</code> framework method.
	 * Elements within the same category are further subjected to a case insensitive
	 * compare of their label strings, either as computed by the content viewer's
	 * label provider, or their <code>toString</code> values in other cases.
	 * Subclasses may override.
	 * </p>
	 * 
	 * @param viewer the viewer
	 * @param e1     the first element
	 * @param e2     the second element
	 * @return a negative number if the first element is less than the second
	 *         element; the value <code>0</code> if the first element is equal to
	 *         the second element; and a positive number if the first element is
	 *         greater than the second element
	 */

	public int compare(Viewer viewer, Object e1, Object e2) {
		/*
		 * if ( e1 instanceof ITreeItemNode && e2 instanceof ITreeItemNode ) { int type1
		 * = ( (ITreeItemNode) e1 ).getType(); int type2 = ( (ITreeItemNode) e2
		 * ).getType(); if ( type1 == type2 && type1 == ITreeItemNode.ITEM ) { return
		 * super.compare( viewer, e1, e2 ); } }
		 */
		if (e1 instanceof DataSourceHandle && e2 instanceof DataSourceHandle) {
			return super.compare(viewer, e1, e2);
		}
		if (e1 instanceof DataSetHandle && e2 instanceof DataSetHandle) {
			return super.compare(viewer, e1, e2);
		}
		if (e1 instanceof CubeHandle && e2 instanceof CubeHandle) {
			return super.compare(viewer, e1, e2);
		}
		return 0;
	}

}