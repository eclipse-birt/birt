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

abstract class AbstractMultiViewsHandle extends DesignElementHandle implements IMultiViewsModel {

	/**
	 * Represents the container of the view does not use any inner view.
	 */

	public static final int HOST = -1;

	/**
	 * The target report element.
	 */

	protected AbstractMultiViews element;

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public AbstractMultiViewsHandle(Module module, AbstractMultiViews element) {
		super(module);
		this.element = element;

		initializeSlotHandles();
		cachePropertyHandles();
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
	 * Returns the index for the current view.
	 * 
	 * @return a 0-based integer
	 */

	public int getCurrentViewIndex() {
		return getIntProperty(INDEX_PROP);
	}

	/**
	 * Sets the index for the view to be used.
	 * 
	 * @param index a 0-based integer
	 * 
	 * @throws SemanticException
	 */

	public void setCurrentViewIndex(int index) throws SemanticException {
		if (index < HOST)
			index = HOST;

		setProperty(INDEX_PROP, Integer.valueOf(index));
	}
}
