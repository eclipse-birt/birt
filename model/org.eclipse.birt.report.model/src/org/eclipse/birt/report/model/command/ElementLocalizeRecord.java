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

package org.eclipse.birt.report.model.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ElementLocalizeEvent;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Record used to localize a virtual element inside a child element.
 * 
 */

public class ElementLocalizeRecord extends SimpleRecord {

	/**
	 * The module.
	 */

	private Module module = null;

	/**
	 * The virtual child element, the current element to localize.
	 */

	private DesignElement element = null;

	/**
	 * The virtual parent element, which is referenced by the base id of the child
	 * element.
	 */

	private DesignElement parent = null;

	/**
	 * Original base id of the child element.
	 */

	private long baseId;

	/**
	 * Map used to catch all the overridden properties of the child element.
	 */

	private Map<String, Object> propValues = new HashMap<String, Object>();

	/**
	 * Constructor.
	 * 
	 * @param module        the module
	 * @param virtualChild  the element to localize.
	 * @param virtualParent the corresponding parent element.
	 */

	public ElementLocalizeRecord(Module module, DesignElement virtualChild, DesignElement virtualParent) {
		assert virtualChild.isVirtualElement();
		assert virtualChild.getBaseId() == virtualParent.getID();
		assert virtualChild.getDefn() == virtualParent.getDefn();

		this.module = module;
		this.element = virtualChild;
		this.parent = virtualParent;
		this.baseId = virtualChild.getBaseId();
		this.collectOverriddenProperties();
	}

	/**
	 * Collect all the overridden properties of the child element, catch the values
	 * in a hash map.
	 * 
	 */

	private void collectOverriddenProperties() {
		assert element != null;

		Iterator<IElementPropertyDefn> iter = element.getPropertyDefns().iterator();
		while (iter.hasNext()) {
			PropertyDefn propDefn = (PropertyDefn) iter.next();

			// virtual child can not define "extends" property.

			if (IDesignElementModel.EXTENDS_PROP.equals(propDefn.getName()))
				continue;

			Object value = element.getLocalProperty(module, propDefn.getName());
			if (value == null)
				continue;

			propValues.put(propDefn.getName(), value);
		}
	}

	/**
	 * Localize the <code>from</code> element using the parent element. All
	 * properties that can extends from the parent are set locally on the child
	 * element itself
	 * 
	 * @param from the child element
	 * @param to   the parent element
	 */

	private void localizeElement(DesignElement from, DesignElement to) {
		Iterator<IElementPropertyDefn> iter = from.getDefn().getProperties().iterator();
		while (iter.hasNext()) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next();
			String propName = propDefn.getName();

			if (!propDefn.canInherit())
				continue;

			// Style property and extends property will be removed.
			// The properties inherited from style or parent will be
			// flatten to new element.

			if (IStyledElementModel.STYLE_PROP.equals(propName) || IDesignElementModel.EXTENDS_PROP.equals(propName)
					|| IDesignElementModel.USER_PROPERTIES_PROP.equals(propName))
				continue;

			Object localValue = to.getLocalProperty(module, propDefn);
			Object parentValue = from.getStrategy().getPropertyFromElement(module, from, propDefn);

			if (localValue == null && parentValue != null) {
				Object valueToSet = ModelUtil.copyValue(propDefn, parentValue);
				to.setProperty(propDefn, valueToSet);
			}
		}
	}

	/**
	 * Recover the element to the original virtual element state. The original
	 * cached properties will be recovered.
	 * 
	 * @param obj the element to recover.
	 */

	private void recoverProperties(DesignElement obj) {
		// first clear all the new the values.

		obj.clearAllProperties();

		// recover the original overridden properties.

		Iterator<String> propNames = propValues.keySet().iterator();
		while (propNames.hasNext()) {
			String propName = propNames.next();
			assert !IDesignElementModel.EXTENDS_PROP.equals(propName);

			if (IStyledElementModel.STYLE_PROP.equals(propName)) {
				ElementRefValue refValue = (ElementRefValue) propValues.get(propName);
				if (refValue == null)
					continue;

				if (refValue.isResolved())
					((StyledElement) obj).setStyle((Style) refValue.getElement());
				else
					((StyledElement) obj).setStyleName(refValue.getName());

				continue;
			}

			obj.setProperty(propName, propValues.get(propName));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform(boolean undo) {
		if (undo) {
			recoverProperties(element);
			element.setBaseId(baseId);
		} else {
			localizeElement(parent, element);
			element.setBaseId(DesignElement.NO_BASE_ID);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent() {
		return new ElementLocalizeEvent(element);
	}
}
