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

package org.eclipse.birt.doc.romdoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

public class DocElement extends DocComposite {
	protected ArrayList methods = new ArrayList();
	protected ArrayList slots = new ArrayList();
	private ArrayList inheritedPropertyNote = new ArrayList();

	public DocElement(ElementDefn d) {
		super(d);
		ElementDefn elementDefn = d;
		Iterator iter = elementDefn.getLocalProperties().iterator();
		while (iter.hasNext()) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next();

			// Workaround because getLocalProperties( ) returns all properties.

			if (propDefn.definedBy() == elementDefn) {
				if (propDefn.getTypeCode() == IPropertyType.SCRIPT_TYPE)
					methods.add(new DocMethod(propDefn));
				else
					properties.add(new DocProperty(propDefn));
			}
		}
		Collections.sort(properties, new DocComparator());
		Collections.sort(methods, new DocComparator());

		for (int i = 0; i < elementDefn.getSlotCount(); i++) {
			slots.add(new DocSlot(elementDefn.getSlot(i)));
		}

		// Leave slots unsorted: they make more sense in the order defined
		// in rom.def. (header, detail, footer for example)
	}

	public ElementDefn getElementDefn() {
		return (ElementDefn) defn;
	}

	public boolean hasMethods() {
		return !methods.isEmpty();
	}

	public ArrayList getMethods() {
		return methods;
	}

	public boolean hasSlots() {
		return !slots.isEmpty();
	}

	public ArrayList getSlots() {
		return slots;
	}

	public String getExtends() {
		if (getElementDefn().getExtends() != null)
			return makeElementLink(getElementDefn().getExtends(), "elements");//$NON-NLS-1$
		return "None";
	}

	public String getExtendable() {
		return yesNo(getElementDefn().canExtend());
	}

	public String getAbstract() {
		return yesNo(getElementDefn().isAbstract());
	}

	public String getNameSpace() {
		return getElementDefn().getNameSpaceID();

		/*
		 * switch ( getElementDefn( ).getNameSpaceID( ) ) { case
		 * Module.STYLE_NAME_SPACE: return "Styles"; case Module.DATA_SET_NAME_SPACE:
		 * return "Data Sets"; case Module.DATA_SOURCE_NAME_SPACE: return
		 * "Data Sources"; case Module.ELEMENT_NAME_SPACE: return "Report Items"; case
		 * Module.PARAMETER_NAME_SPACE: return "Parameters"; case
		 * Module.PAGE_NAME_SPACE: return "Pages"; default: return "None"; }
		 */
	}

	public String getNameRequirement() {
		switch (getElementDefn().getNameOption()) {
		case MetaDataConstants.REQUIRED_NAME:
			return "Required";

		case MetaDataConstants.OPTIONAL_NAME:
			return "Optional";

		default:
			return "Not Supported";
		}
	}

	public String getStyle() {
		String style = getElementDefn().getSelector();
		if (style == null)
			return "None";
		String[] styles = style.split(",");

		StringBuffer link = new StringBuffer();
		for (int i = 0; i < styles.length; i++) {
			if (i > 0)
				link.append(", ");
			link.append("<a href=\"../styles.html#");
			link.append(styles[i]);
			link.append("\">");
			link.append(styles[i]);
			link.append("</a>");
		}
		return link.toString();
	}

	public String getUserProperties() {
		return yesNo(getElementDefn().allowsUserProperties());
	}

	public String getHasStyle() {
		return yesNo(getElementDefn().hasStyle());
	}

	public List getInheritedProperties() {
		ArrayList inherited = new ArrayList();
		Iterator iter = getElementDefn().getProperties().iterator();
		while (iter.hasNext()) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next();
			if (propDefn.definedBy() != defn && propDefn.getTypeCode() != IPropertyType.SCRIPT_TYPE
					&& !propDefn.isStyleProperty()) {
				inherited.add(propDefn);
			}
		}
		Collections.sort(inherited, new PropComparator());
		return inherited;
	}

	public List getStyleProperties() {
		ArrayList inherited = new ArrayList();
		Iterator iter = getElementDefn().getProperties().iterator();
		while (iter.hasNext()) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next();
			if (propDefn.definedBy() != defn && propDefn.getTypeCode() != IPropertyType.SCRIPT_TYPE
					&& propDefn.isStyleProperty()) {
				inherited.add(propDefn);
			}
		}
		Collections.sort(inherited, new PropComparator());
		return inherited;
	}

	public boolean hasStyle() {
		return getElementDefn().hasStyle();
	}

	public List getInheritedMethods() {
		ArrayList inherited = new ArrayList();
		Iterator iter = getElementDefn().getProperties().iterator();
		while (iter.hasNext()) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next();
			if (propDefn.definedBy() != defn && propDefn.getTypeCode() == IPropertyType.SCRIPT_TYPE) {
				inherited.add(propDefn);
			}
		}
		return inherited;
	}

	public List getInheritedSlots() {
		// No inherited slots in Release 1.

		return new ArrayList();
	}

	public String getXmlElement() {
		if (getElementDefn().isAbstract())
			return "None";
		return getElementDefn().getXmlName();
	}

	public void setXmlSummary(String string) {
		xmlSummary = string;
	}

	public void addInheritedPropertyNote(DocInheritedProperty prop) {
		inheritedPropertyNote.add(prop);
	}

	public List getInheritedPropertyNotes() {
		return inheritedPropertyNote;
	}

	public DocMethod getMethod(String name) {
		Iterator iter = methods.iterator();
		while (iter.hasNext()) {
			DocMethod method = (DocMethod) iter.next();
			if (method.getName().equals(name))
				return method;
		}
		return null;
	}

	public DocSlot getSlot(String name) {
		Iterator iter = slots.iterator();
		while (iter.hasNext()) {
			DocSlot slot = (DocSlot) iter.next();
			if (slot.getName().equals(name))
				return slot;
		}
		return null;
	}

	public boolean isElement() {
		return true;
	}

	public String getDefiningElement(String name) {
		return ((ElementPropertyDefn) defn.findProperty(name)).definedBy().getName();
	}

}
