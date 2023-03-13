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

package org.eclipse.birt.report.debug.internal.ui.script.outline;

import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * Script outline provider.
 */

public class ScriptOutlineTreeProvider implements ITreeContentProvider, ILabelProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		return ScriptProviderFactory.createProvider(parentElement).getChildren(parentElement);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		return ScriptProviderFactory.createProvider(element).getParent(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return ScriptProviderFactory.createProvider(element).hasChildren(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.
	 * Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[]) {
			return (Object[]) inputElement;

		}
		return getChildren(inputElement);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.
	 * viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// System.out.println("in put change");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return ScriptProviderFactory.createProvider(element).getNodeIcon(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		return ScriptProviderFactory.createProvider(element).getNodeDisplayName(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.
	 * viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
		// do notning
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.
	 * Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return (element instanceof LabelHandle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface
	 * .viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
		// do nothing
	}
}
