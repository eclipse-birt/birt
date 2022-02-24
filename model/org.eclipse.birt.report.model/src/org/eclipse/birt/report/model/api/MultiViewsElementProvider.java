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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.interfaces.IMultiViewsModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Implements functions to deal with functions for multiple-views element.
 */

final class MultiViewsElementProvider implements IMultiViewsModel {

	private AbstractMultiViewsHandle element;

	/**
	 * The constructor.
	 * 
	 * @param element the multiple-views element
	 */

	MultiViewsElementProvider(AbstractMultiViewsHandle element) {
		this.element = element;
	}

	/**
	 * Returns a list containing views.
	 * 
	 * @return a list containing views. Each item is an
	 *         <code>ReportItemHandle</code>.
	 */

	protected List getViews() {
		List list = element.getListProperty(VIEWS_PROP);
		if (list == null)
			return Collections.EMPTY_LIST;

		List retList = new ArrayList();
		retList.addAll(list);
		return Collections.unmodifiableList(retList);
	}

	/**
	 * Returns the view that is being used.
	 * 
	 * @return the view that is being used
	 */

	public DesignElementHandle getCurrentView() {
		int currentViewIndex = element.getCurrentViewIndex();
		if (currentViewIndex == MultiViewsHandle.HOST)
			return null;

		List views = getViews();
		if (views.isEmpty() || views.size() <= currentViewIndex)
			return null;

		return (DesignElementHandle) views.get(currentViewIndex);
	}

	/**
	 * Sets the index for the view to be used.
	 * 
	 * @param index a 0-based integer
	 * 
	 * @throws SemanticException
	 */

	protected void setCurrentViewIndex(int index) throws SemanticException {
		if (index > MultiViewsHandle.HOST) {
			List views = getViews();
			if (views.isEmpty() || views.size() <= index)
				return;
		} else if (index < MultiViewsHandle.HOST)
			index = MultiViewsHandle.HOST;

		element.setProperty(INDEX_PROP, Integer.valueOf(index));
	}

	/**
	 * Adds a new element as the view.
	 * 
	 * @param viewElement the element
	 * @throws SemanticException
	 */

	public void addView(DesignElementHandle viewElement) throws SemanticException {
		if (viewElement == null)
			return;

		element.add(VIEWS_PROP, viewElement);
	}

	/**
	 * Deletes the given view. If the given element was named as the current view,
	 * this method also set the current view to <code>HOST</code>.
	 * 
	 * @param viewElement the view element
	 * @throws SemanticException
	 */

	public void dropView(DesignElementHandle viewElement) throws SemanticException {
		if (viewElement == null)
			return;

		CommandStack cmdStack = element.getModuleHandle().getCommandStack();
		cmdStack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.DROP_ELEMENT_MESSAGE));
		try {
			DesignElementHandle currentView = getCurrentView();
			if (currentView == viewElement)
				setCurrentViewIndex(MultiViewsHandle.HOST);

			element.drop(VIEWS_PROP, viewElement);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}
}
