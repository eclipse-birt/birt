/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.property;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.1 $ $Date: 2005/02/05 06:30:14 $
 */

public final class PropertyContentProvider implements ITreeContentProvider {

	private transient PropertyNode rootNode = null;

	/**
	 *  
	 */
	public PropertyContentProvider() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
		PropertyNode node = (PropertyNode) parentElement;
		return node.getSubNodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		return findParent(rootNode, ((PropertyNode) element).getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		return ((PropertyNode) element).hasSubNodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.
	 * Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (((PropertyNode) inputElement).hasSubNodes()) {
			return ((PropertyNode) inputElement).getSubNodes();
		}
		return new PropertyNode[] {};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.
	 * viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		rootNode = (PropertyNode) newInput;
	}

	private PropertyNode findParent(PropertyNode startNode, String childId) {
		if (startNode == null || childId == null) {
			return null;
		}
		if (childId.trim().equals("/")) //$NON-NLS-1$
		{
			return null;
		}
		PropertyNode found = startNode.getSubNode(childId);
		if (found != null) {
			return startNode;
		}
		if (startNode.hasSubNodes()) {
			PropertyNode[] children = startNode.getSubNodes();
			for (int n = 0; n < children.length; n++) {
				found = findParent(children[n], childId);
				if (found != null) {
					return found;
				}
			}
		}

		return found;
	}

}
