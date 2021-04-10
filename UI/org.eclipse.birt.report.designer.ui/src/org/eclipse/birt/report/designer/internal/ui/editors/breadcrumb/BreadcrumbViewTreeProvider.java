/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * The provider class used by views
 */

public class BreadcrumbViewTreeProvider implements ITreeContentProvider, IBreadcrumbLabelProvider, IColorProvider {

	private GraphicalEditorWithFlyoutPalette viewer;

	public BreadcrumbViewTreeProvider(GraphicalEditorWithFlyoutPalette viewer) {
		this.viewer = viewer;
	}

	/**
	 * Returns the child elements of the given parent element.
	 * 
	 * @param parentElement the parent element
	 * @return an array of child elements
	 */
	public Object[] getChildren(Object parentElement) {
		Object children[] = ((ReportLayoutEditorBreadcrumb) viewer.getBreadcrumb()).getBreadcrumbNodeProvider(viewer)
				.getChildren(parentElement);
		return children;
	}

	/**
	 * Returns the parent for the given element, or <code>null</code> indicating
	 * that the parent can't be computed. In this case the tree-structured viewer
	 * can't expand a given node correctly if requested.
	 * 
	 * @param element the element
	 * @return the parent element, or <code>null</code> if it has none or if the
	 *         parent cannot be computed
	 */
	public Object getParent(Object element) {
		return ((ReportLayoutEditorBreadcrumb) viewer.getBreadcrumb()).getBreadcrumbNodeProvider(viewer)
				.getParent(element);
	}

	/**
	 * Returns whether the given element has children.
	 * <p>
	 * Intended as an optimization for when the viewer does not need the actual
	 * children. Clients may be able to implement this more efficiently than
	 * <code>getChildren</code>.
	 * </p>
	 * 
	 * @param element the element
	 * @return <code>true</code> if the given element has children, and
	 *         <code>false</code> if it has no children
	 */
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	/**
	 * Returns the elements to display in the viewer when its input is set to the
	 * given element. These elements can be presented as rows in a table, items in a
	 * list, etc. The result is not modified by the viewer.
	 * 
	 * @param inputElement the input element
	 * @return the array of elements to display in the viewer
	 */
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
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// viewer.setInput(newInput);
	}

	/**
	 * Returns the image for the label of the given element. The image is owned by
	 * the label provider and must not be disposed directly. Instead, dispose the
	 * label provider when no longer needed.
	 * 
	 * @param element the element for which to provide the label image
	 * @return the image used to label the element, or <code>null</code> if there is
	 *         no image for the given object
	 */
	public Image getImage(Object element) {
		if (element == null)
			return null;

		return ((ReportLayoutEditorBreadcrumb) viewer.getBreadcrumb()).getBreadcrumbNodeProvider(viewer)
				.getImage(element);
	}

	/**
	 * Returns the text for the label of the given element.
	 * 
	 * @param element the element for which to provide the label text
	 * @return the text string used to label the element, or <code>null</code> if
	 *         there is no text label for the given object
	 */
	public String getText(Object element) {
		if (element == null)
			return null;
		// if ( element instanceof RowHandle )
		// {
		// return ProviderFactory.createProvider( element )
		// .getNodeDisplayName( element );
		// }
		// if ( getDirectParnt( element ) != element )
		// return null;
		// return ProviderFactory.createProvider( getDirectParnt( element ) )
		// .getNodeDisplayName( getDirectParnt( element ) );

		return ((ReportLayoutEditorBreadcrumb) viewer.getBreadcrumb()).getBreadcrumbNodeProvider(viewer)
				.getText(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.
	 * jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {// Do nothing
	}

	/**
	 * Returns whether the label would be affected by a change to the given property
	 * of the given element. This can be used to optimize a non-structural viewer
	 * update. If the property mentioned in the update does not affect the label,
	 * then the viewer need not update the label.
	 * 
	 * @param element  the element
	 * @param property the property
	 * @return <code>true</code> if the label would be affected, and
	 *         <code>false</code> if it would be unaffected
	 */
	public boolean isLabelProperty(Object element, String property) {
		return (element instanceof LabelHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
	 */
	public Color getBackground(Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
	 */
	public Color getForeground(Object element) {
		return null;
	}

	public String getTooltipText(Object element) {
		if (element == null)
			return null;

		return ((ReportLayoutEditorBreadcrumb) viewer.getBreadcrumb()).getBreadcrumbNodeProvider(viewer)
				.getTooltipText(element);
	}
}
