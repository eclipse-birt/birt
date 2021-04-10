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

package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.AbstractMultiViews;
import org.eclipse.birt.report.model.elements.interfaces.IMultiViewsModel;

/**
 * Represents a multiple view element. A view element can contains multiple
 * report items. The container of the view can use inner report items to
 * represents its appearance.
 */

public class MultiViewsHandle extends AbstractMultiViewsHandle implements IMultiViewsModel {

	/**
	 * 
	 */

	private MultiViewsElementProvider provider = null;

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public MultiViewsHandle(Module module, AbstractMultiViews element) {
		super(module, element);
		provider = new MultiViewsElementProvider(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getElement()
	 */

	public DesignElement getElement() {
		return element;
	}

	/**
	 * Returns a list containing views.
	 * 
	 * @return a list containing views. Each item is an
	 *         <code>ReportItemHandle</code>.
	 */

	protected List getViews() {
		return provider.getViews();
	}

	/**
	 * Returns the view that is being used.
	 * 
	 * @return the view that is being used
	 */

	public DesignElementHandle getCurrentView() {
		return provider.getCurrentView();
	}

	/**
	 * Sets the index for the view to be used.
	 * 
	 * @param index a 0-based integer
	 * 
	 * @throws SemanticException
	 */

	public void setCurrentViewIndex(int index) throws SemanticException {
		provider.setCurrentViewIndex(index);
	}

	/**
	 * Adds a new element as the view.
	 * 
	 * @param viewElement the element
	 * @throws SemanticException
	 */

	public void addView(DesignElementHandle viewElement) throws SemanticException {
		provider.addView(viewElement);
	}

	/**
	 * Deletes the given view. If the given element was named as the current view,
	 * this method also set the current view to <code>HOST</code>.
	 * 
	 * @param viewElement the view element
	 * @throws SemanticException
	 */

	public void dropView(DesignElementHandle viewElement) throws SemanticException {
		provider.dropView(viewElement);
	}
}
