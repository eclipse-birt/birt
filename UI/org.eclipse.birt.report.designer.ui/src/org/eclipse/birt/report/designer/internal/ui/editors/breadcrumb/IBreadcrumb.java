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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.viewers.ISelectionProvider;

/**
 * Implementors can provide a breadcrumb inside an editor.
 *
 * <p>
 * Clients should not implement this interface. They should subclass
 * {@link EditorBreadcrumb} instead if possible
 * </p>
 *
 * @since 2.6.2
 */
public interface IBreadcrumb {

	/**
	 * Create breadcrumb content.
	 *
	 * @param parent the parent of the content
	 * @return the control containing the created content
	 */
	public Control createContent(Composite parent);

	/**
	 * Returns the selection provider for this breadcrumb.
	 *
	 * @return the selection provider for this breadcrumb
	 */
	public ISelectionProvider getSelectionProvider();

	/**
	 * Activates the breadcrumb. This sets the keyboard focus inside this breadcrumb
	 * and retargets the editor actions.
	 */
	public void activate();

	/**
	 * A breadcrumb is active if it either has the focus or another workbench part
	 * has the focus and the breadcrumb had the focus before the other workbench
	 * part was made active.
	 *
	 * @return <code>true</code> if this breadcrumb is active
	 */
	public boolean isActive();

	/**
	 * Set the input of the breadcrumb to the given element
	 *
	 * @param element the input element can be <code>null</code>
	 */
	public void setInput(Object element);

	/**
	 * Dispose all resources hold by this breadcrumb.
	 */
	public void dispose();

}
