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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.NameEvent;
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents an element that defines a style. An element that uses this style
 * is called a <em>client</em> element. This class manages the inverse
 * style-to-client relationship. It also handles sending notifications to the
 * client elements.
 */

public abstract class StyleElement extends ReferenceableElement {

	private final static String REPORT_SELECTOR = "report"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public StyleElement() {
	}

	/**
	 * Constructor with the element name.
	 * 
	 * @param theName the element name
	 */

	public StyleElement(String theName) {
		super(theName);
	}

	/**
	 * Returns true if the element is style.
	 * 
	 * @return true if the element is style, otherwise return false.
	 */

	public boolean isStyle() {
		return true;
	}

	/**
	 * Gets the value of property.
	 * 
	 * @param module module
	 * @param prop   definition of the property to get
	 * @return the value of the property.
	 */

	public Object getFactoryProperty(Module module, ElementPropertyDefn prop) {
		return getLocalProperty(module, prop);
	}

	/**
	 * Gets the extended element of this element. Always return null cause style
	 * element is not allowed to extend.
	 * 
	 * @return null
	 */
	public DesignElement getExtendsElement() {
		return null;
	}

	/**
	 * Gets the name if the extended element. Always return null cause style element
	 * is not allowed to extend.
	 * 
	 * @return null
	 */

	public String getExtendsName() {
		return null;
	}

	/**
	 * Sets the extended element. This operation is not allowed to do for style
	 * element.
	 * 
	 * @param base the base element to set
	 */

	public void setExtendsElement(DesignElement base) {
		assert false;
	}

	/**
	 * Sets the extended element name. This operation is not allowed to do for style
	 * element.
	 * 
	 * @param name name of the base element to set
	 */

	public void setExtendsName(String name) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.ReferenceableElement#broadcastToClients
	 * (org.eclipse.birt.report.model.activity.NotificationEvent,
	 * org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	protected void broadcastToClients(NotificationEvent ev, Module module) {
		super.broadcastToClients(ev, module);

		// Broad the event to the elements selected by selector style.

		List<String> selectors = new ArrayList<String>();

		if (ev instanceof NameEvent) {
			String oldName = ((NameEvent) ev).getOldName();
			String newName = ((NameEvent) ev).getNewName();

			if (MetaDataDictionary.getInstance().getPredefinedStyle(oldName) != null)
				selectors.add(oldName);
			if (MetaDataDictionary.getInstance().getPredefinedStyle(newName) != null)
				selectors.add(newName);
		} else {
			if (MetaDataDictionary.getInstance().getPredefinedStyle(getName()) != null)
				selectors.add(getName());
		}

		if (selectors.isEmpty()) {
			return;
		}

		DesignElement tmpContainer = getContainer();
		List<Module> modules = new ArrayList<Module>();

		if (getContainer() instanceof Theme) {
			Theme containerTheme = (Theme) tmpContainer;
			if (containerTheme.hasReferences()) {
				List<BackRef> refs = ((Theme) tmpContainer).getClientList();
				for (int i = 0; i < refs.size(); i++)
					modules.add((Module) refs.get(i).getElement());
			}
		} else
			modules.add(module);

		for (int i = 0; i < modules.size(); i++)
			broadcastToModule(modules.get(i), selectors);
	}

	/**
	 * @param module       the module of the style element
	 * @param selectorName the selector name
	 */

	private void broadcastToModule(Module module, List<String> selectorList) {
		assert !selectorList.isEmpty();

		// Work around for renaming selector style.

		Iterator<String> iter = selectorList.iterator();
		while (iter.hasNext()) {
			String selectorName = iter.next();

			if (REPORT_SELECTOR.equals(selectorName)) {
				NotificationEvent event = null;
				event = new StyleEvent(module);
				event.setDeliveryPath(NotificationEvent.STYLE_CLIENT);
				module.broadcast(event);

			} else {
				broadcastToSelectedElementsInSlot(module, new ContainerContext(module, IModuleModel.COMPONENT_SLOT),
						selectorName);
				broadcastToSelectedElementsInSlot(module, new ContainerContext(module, IModuleModel.PAGE_SLOT),
						selectorName);

				// only report design has the body, scratch pad slots.

				if (module instanceof ReportDesign) {
					broadcastToSelectedElementsInSlot(module,
							new ContainerContext(module, IReportDesignModel.BODY_SLOT), selectorName);

					broadcastToSelectedElementsInSlot(module,
							new ContainerContext(module, IReportDesignModel.SCRATCH_PAD_SLOT), selectorName);
				}
			}
		}

	}

	/**
	 * Broadcasts the event to all elements in the given slot if the elements are
	 * selected by selector style.
	 * 
	 * @param module       the module
	 * @param slot         the slot to send
	 * @param selectorName the selector name
	 */

	private void broadcastToSelectedElementsInSlot(Module module, ContainerContext containerInfor,
			String selectorName) {
		Iterator<DesignElement> iter = containerInfor.getContents(module).iterator();

		NotificationEvent event = null;

		while (iter.hasNext()) {
			DesignElement element = iter.next();

			// Broadcast the element which is selected by this style
			event = new StyleEvent(element);
			event.setDeliveryPath(NotificationEvent.STYLE_CLIENT);

			List<String> list = element.getElementSelectors();

			String selector = null;

			for (int i = 0; i < list.size(); i++) {
				String tmpSelector = list.get(i);
				if (tmpSelector.equalsIgnoreCase(selectorName)) {
					selector = tmpSelector;
					break;
				}
			}

			if (selector != null) {
				element.broadcast(event, module);
				continue;
			}

			// check if the element slot has the selector with the same name as
			// the given selector name.

			if (checkSlotSelector(element, selectorName, event, module))
				continue;

			ElementDefn elementDefn = (ElementDefn) element.getDefn();
			if (!elementDefn.isContainer())
				continue;
			for (int i = 0; i < elementDefn.getSlotCount(); i++) {
				broadcastToSelectedElementsInSlot(module, new ContainerContext(element, i), selectorName);
			}
			List<IElementPropertyDefn> properties = elementDefn.getContents();
			for (int i = 0; i < properties.size(); i++) {
				IElementPropertyDefn propDefn = properties.get(i);
				broadcastToSelectedElementsInSlot(module, new ContainerContext(element, propDefn.getName()),
						selectorName);
			}
		}
	}

	private boolean checkSlotSelector(DesignElement element, String selectorName, NotificationEvent event,
			Module module) {

		String selector = element.getContainerInfo().getSelector();

		if (selector != null && selector.equalsIgnoreCase(selectorName)) {
			element.broadcast(event, module);
			return true;
		}
		return false;

	}
}