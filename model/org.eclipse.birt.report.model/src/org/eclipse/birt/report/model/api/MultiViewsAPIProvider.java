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

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IMultiViewsModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Implements functions to deal with API-level views operations. Through these
 * APIs, the caller does not need to anything about
 * <code>AbstractMultiViewHandle</code> or <code>MultiViewHandle</code>.
 */

class MultiViewsAPIProvider implements IMultiViewsModel {

	/**
	 * The element.
	 */

	private ReportItemHandle element;

	/**
	 * The name of the property of which instance is a subclass of
	 * <code>AbstractMultiViewHandle</code>. If the value is <code>null</code>, the
	 * property definition on the given element is <code>null</code>. For such case,
	 * should avoid NPE in function call.
	 */

	private String propertyName;

	/**
	 * The constructor.
	 * 
	 * @param element  the element
	 * @param propName the property name. Corresponding property value must be is a
	 *                 subclass of <code>AbstractMultiViewHandle</code>.
	 */

	public MultiViewsAPIProvider(ReportItemHandle element, String propName) {
		this.element = element;
		propertyName = propName;

		if (this.element == null)
			throw new IllegalArgumentException("Must provide a NON-NULL element."); //$NON-NLS-1$

		if (propName == null)
			throw new IllegalArgumentException("Must provide the name for the views property."); //$NON-NLS-1$

		IPropertyDefn propDefn = element.getPropertyDefn(propName);

		// for special cases like crosstab cells, this could be null.

		if (propDefn == null) {
			propertyName = null;
			return;
		}

		if (propDefn.getTypeCode() != IPropertyType.ELEMENT_TYPE)
			throw new IllegalArgumentException("The views property must defined as element type."); //$NON-NLS-1$

	}

	/**
	 * Returns the view that is being used.
	 * 
	 * @return the view that is being used
	 */

	public DesignElementHandle getCurrentView() {
		if (propertyName == null)
			return null;

		AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element.getProperty(propertyName);
		if (multiView == null || multiView.getCurrentViewIndex() == MultiViewsHandle.HOST)
			return null;

		MultiViewsElementProvider subProvider = new MultiViewsElementProvider(multiView);
		return subProvider.getCurrentView();
	}

	/**
	 * Returns the view that is being used.
	 * 
	 * @return the view that is being used
	 */

	public List getViews() {
		if (propertyName == null)
			return Collections.EMPTY_LIST;

		AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element.getProperty(propertyName);
		if (multiView == null)
			return Collections.EMPTY_LIST;

		MultiViewsElementProvider subProvider = new MultiViewsElementProvider(multiView);
		return subProvider.getViews();
	}

	/**
	 * Adds a new element as the view.
	 * 
	 * @param viewElement the element
	 * @throws SemanticException
	 */

	public void addView(DesignElementHandle viewElement) throws SemanticException {
		if (propertyName == null)
			return;

		AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element.getProperty(propertyName);

		ModuleHandle module = element.getModuleHandle();
		CommandStack stack = module.getCommandStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.ADD_ELEMENT_MESSAGE));
		try {
			if (multiView == null) {
				multiView = module.getElementFactory().newMultiView();
				element.setProperty(propertyName, multiView);
			}

			MultiViewsElementProvider subProvider = new MultiViewsElementProvider(multiView);
			subProvider.addView(viewElement);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}
		stack.commit();
	}

	/**
	 * Deletes the given view.
	 * 
	 * @param viewElement the element
	 * @throws SemanticException
	 */

	public void dropView(DesignElementHandle viewElement) throws SemanticException {
		if (propertyName == null)
			return;

		AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element.getProperty(propertyName);
		if (multiView == null)
			return;

		MultiViewsElementProvider subProvider = new MultiViewsElementProvider(multiView);
		subProvider.dropView(viewElement);
	}

	/**
	 * Sets the index for the view to be used. If the given element is not in the
	 * multiple view, it will be added and set as the active view.
	 * 
	 * @param viewElement the view element
	 * 
	 * @throws SemanticException if the given element resides in the other elements.
	 */

	public void setCurrentView(DesignElementHandle viewElement) throws SemanticException {
		if (propertyName == null)
			return;

		// if the viewElement is in the design tree and not in table, throw
		// exception

		DesignElement internalElement = element.getElement();

		if (viewElement != null && viewElement.getContainer() != null
				&& !viewElement.getElement().isContentOf(internalElement)) {
			throw new PropertyValueException(internalElement, element.getPropertyDefn(propertyName), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);
		}

		// cannot set the host as the current view

		if (viewElement != null && viewElement == element) {
			throw new PropertyValueException(internalElement, element.getPropertyDefn(propertyName), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);
		}

		ModuleHandle module = element.getModuleHandle();
		CommandStack stack = module.getCommandStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[] { VIEWS_PROP }));
		try {
			AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element.getProperty(propertyName);
			if (multiView == null) {
				multiView = module.getElementFactory().newMultiView();
				element.setProperty(propertyName, multiView);
			}

			// if the viewElement is in the table and not in multiple view,
			// throw exception

			if (viewElement != null && viewElement.getContainer() != null
					&& !viewElement.getElement().isContentOf(multiView.getElement())) {
				throw new PropertyValueException(internalElement, element.getPropertyDefn(propertyName), null,
						PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND);
			}

			// add to the multiple view

			if (viewElement != null && viewElement.getContainer() == null) {
				MultiViewsElementProvider subProvider = new MultiViewsElementProvider(multiView);
				subProvider.addView(viewElement);
			}

			// set index

			int newIndex = MultiViewsHandle.HOST;
			assert viewElement != element;

			if (viewElement != null) {
				ContainerContext context = new ContainerContext(multiView.getElement(), MultiViewsHandle.VIEWS_PROP);
				newIndex = context.indexOf(viewElement.getElement());

				// the viewElement is either added to the view or already in the
				// view

				assert newIndex != -1;
			}

			multiView.setCurrentViewIndex(newIndex);

		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}
		stack.commit();
	}

	/**
	 * Determines whether this report item can add a view with the specified
	 * extension type or not.
	 * 
	 * @return
	 */
	public boolean canAddView(String extensionType) {
		if (propertyName == null)
			return false;

		AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element.getProperty(propertyName);
		ModuleHandle module = element.getModuleHandle();

		if (multiView == null) {
			multiView = module.getElementFactory().newMultiView();
			return element.canContain(propertyName, multiView);
		}

		ExtendedItemHandle itemHandle = module.getElementFactory().newExtendedItem(null, extensionType);
		if (itemHandle == null)
			return false;
		return multiView.canContain(IMultiViewsModel.VIEWS_PROP, itemHandle);

	}
}
