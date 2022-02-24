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

package org.eclipse.birt.doc.legacy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import org.w3c.dom.Element;

public class RomUpdater {

	RomImage rom;
	LegacyLoader loader;
	SpecElement specElement;
	Element romElement;
	PrintStream log;

	public RomUpdater(LegacyLoader theLoader) {
		rom = theLoader.getRom();
		loader = theLoader;
	}

	public void update() throws TransformException, IOException {
		log = new PrintStream(new FileOutputStream("docs/rom-update.log"));
		Iterator iter = loader.getElements().iterator();
		while (iter.hasNext()) {
			specElement = (SpecElement) iter.next();
			applyElementSpec();
		}
		iter = loader.getStructures().iterator();
		while (iter.hasNext()) {
			specElement = (SpecElement) iter.next();
			applyElementSpec();
		}
		log.close();
	}

	private void logNotice(SpecElement element, String action) throws IOException {
		String msg = "Notice: " + element.getTypeName() + " " + element.name + ": " + action;
		log.println(msg);
		System.err.println(msg);
		element.addIssue(msg);
	}

	private void logMismatch(SpecElement element, String attrName, String specValue, String romValue)
			throws IOException {
		String action = attrName + ": \"" + specValue + "\" does not match rom.def value \"" + romValue + "\"";
		String msg = element.getTypeName() + " " + element.name + "." + action;
		log.println("Notice: " + msg);
		System.err.println(msg);
		element.addIssue(action);
	}

	private void logNotice(SpecElement element, SpecProperty prop, String action) throws IOException {
		String msg = element.getTypeName() + " Property " + element.name + "." + prop.name + ": " + action;
		log.println("Notice: " + msg);
		System.err.println(msg);
		prop.addIssue(action);
	}

	private void logMismatch(SpecElement element, SpecProperty prop, String attrName, String specValue, String romValue)
			throws IOException {
		String action = attrName + ": \"" + specValue + "\" does not match rom.def value \"" + romValue + "\"";
		String msg = element.getTypeName() + " Property " + element.name + "." + prop.name + "." + action;
		log.println("Notice: " + msg);
		System.err.println(msg);
		prop.addIssue(action);
	}

	private void logNotice(SpecElement element, String attrib, String action) throws IOException {
		String tail = attrib + ": " + action;
		String msg = element.getTypeName() + " " + element.name + "." + tail;
		log.println("Notice: " + msg);
		System.err.println(msg);
		element.addIssue(tail);
	}

	private void logUpdate(SpecElement element, String attrib, String value) throws IOException {
		String msg = element.getTypeName() + " " + element.name + "." + attrib + ": Updated to " + value;
		log.println(msg);
	}

	private void logUpdate(SpecElement element, SpecProperty prop, String attrName, String value) throws IOException {
		String msg = element.getTypeName() + " Property " + element.name + "." + prop.name + "." + attrName
				+ ": Updated to " + value;
		log.println(msg);
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	private static final String DESIGN_OBJ = "designClass";
	private static final String STATE_OBJ = "stateClass";
	private static final String SINCE = "since";
	private static final String XML_NAME = "xmlName";
	private static final String RUNTIME_SETTABLE = "runtimeSettable";
	private static final String CAN_INHERIT = "canInherit";
	private static final String IS_LIST = "isList";
	private static final String VALUE_REQUIRED = "valueRequired";
	private static final String CONTEXT = "context";
	private static final String PROPERTY_TYPE = "type";

	void applyElementSpec() throws TransformException, IOException {
		if (specElement.type == SpecElement.ELEMENT)
			romElement = rom.findElement(specElement.name);
		else
			romElement = rom.findStructure(specElement.name);
		if (romElement == null) {
			logNotice(specElement, " undefined in rom.def!");
			return;
		}

		applyObject(DESIGN_OBJ, specElement.designObjName);
		applyObject(STATE_OBJ, specElement.stateObjName);
		applyAttrib(SINCE, specElement.since);
		if (specElement.type == SpecElement.ELEMENT)
			applyAttrib(XML_NAME, specElement.xmlElement);

		// TODO: Style names
		// TODO: abstract

		applySpecProperties();
	}

	void applyObject(String attrib, String objName) throws IOException {
		if (isBlank(objName))
			return;

		String value = romElement.getAttribute(attrib);
		if (isBlank(value)) {
			Element jsObj = rom.findClass(objName);
			if (jsObj == null) {
				logNotice(specElement, attrib, "Object name \"" + objName + "\" is not defined in rom.def");
			} else {
				logUpdate(specElement, attrib, objName);
				romElement.setAttribute(attrib, objName);
			}
		} else if (!value.equals(objName)) {
			logMismatch(specElement, objName, objName, value);
		}
	}

	// Since

	private void applyAttrib(String attrib, String newValue) throws IOException {
		if (isBlank(newValue))
			return;

		String value = romElement.getAttribute(attrib);
		if (isBlank(value)) {
			logUpdate(specElement, attrib, newValue);
			romElement.setAttribute(attrib, newValue);
		} else if (!value.equals(newValue)) {
			logMismatch(specElement, attrib, newValue, value);
		}
	}

	void applySpecProperties() throws IOException {
		Iterator iter = specElement.properties.iterator();
		while (iter.hasNext()) {
			SpecProperty prop = (SpecProperty) iter.next();

			// Style is special; skip it.

			if (prop.name.equals("style"))
				continue;

			Element romProp;
			if (specElement.type == SpecElement.ELEMENT)
				romProp = rom.findProperty(romElement, prop.name);
			else
				romProp = rom.findMember(romElement, prop.name);
			if (romProp == null) {
				logNotice(specElement, prop, "Not defined in rom.def!");
				continue;
			}

			applyAttrib(prop, romProp, prop.since, SINCE);
			applyDefault(prop, romProp);
			applyBoolean(prop, romProp, prop.runtimeSettable, RUNTIME_SETTABLE, "true");
			applyBoolean(prop, romProp, prop.required, VALUE_REQUIRED, "false");
			checkBoolean(prop, romProp, prop.isArray, IS_LIST, "false");
			if (specElement.type == SpecElement.ELEMENT) {
				applyBoolean(prop, romProp, prop.inherited, CAN_INHERIT, "true");
				applyContext(prop, romProp);
			}
			applyHidden(prop);

			// TODO: JS type

			// TODO: rom type

			// TODO: expr type

			// TODO: choices
		}
	}

	public void applyAttrib(SpecProperty prop, Element romProp, String attribValue, String attrib) throws IOException {
		// Since

		if (isBlank(attribValue))
			return;

		String value = romProp.getAttribute(attrib);
		if (isBlank(value)) {
			logUpdate(specElement, prop, attrib, attribValue);
			romProp.setAttribute(attrib, attribValue);
		} else if (!value.equals(prop.since)) {
			logMismatch(specElement, prop, attrib, attribValue, value);
		}
	}

	public void applyDefault(SpecProperty prop, Element romProp) throws IOException {
		// Default value

		if (isBlank(prop.defaultValue))
			return;

		String value = rom.getDefaultValue(romProp);
		if (prop.defaultValue.equalsIgnoreCase("None")) {
			if (!isBlank(value)) {
				logNotice(specElement, prop,
						"rom.def has default value \"" + prop.defaultValue + "\" but the spec says None");
			}
		} else if (isBlank(value)) {
			logUpdate(specElement, prop, "Default", prop.since);
			rom.setDefaultValue(romProp, prop.defaultValue);
		} else if (!value.equals(prop.defaultValue)) {
			logMismatch(specElement, prop, "Default", prop.defaultValue, value);
		}
	}

	public void applyBoolean(SpecProperty prop, Element romProp, int attribValue, String attrib, String defaultValue)
			throws IOException {
		// Runtime settable

		if (attribValue == SpecObject.TRI_UNKNOWN)
			return;

		String newValue = attribValue == SpecObject.TRI_TRUE ? "true" : "false";
		String value = romProp.getAttribute(attrib);
		if (isBlank(value)) {
			if (!newValue.equals(defaultValue)) {
				logUpdate(specElement, prop, attrib, newValue);
				romProp.setAttribute(attrib, newValue);
			}
		} else if (!value.equals(newValue)) {
			logMismatch(specElement, prop, attrib, newValue, value);
		}
	}

	public void checkBoolean(SpecProperty prop, Element romProp, int attribValue, String attrib, String defaultValue)
			throws IOException {
		if (attribValue == SpecObject.TRI_UNKNOWN)
			return;

		String newValue = attribValue == SpecObject.TRI_TRUE ? "true" : "false";
		String value = romProp.getAttribute(attrib);
		if (isBlank(value)) {
			value = defaultValue;
		}
		if (!value.equals(newValue)) {
			logMismatch(specElement, prop, attrib, newValue, value);
		}
	}

	public void applyHidden(SpecProperty prop) throws IOException {
		if (prop.hidden == SpecObject.TRI_UNKNOWN)
			return;

		String newValue = prop.hidden == SpecObject.TRI_TRUE ? "hide" : "show";
		Element vis = rom.findPropertyVisibility(romElement, prop.name);
		if (vis != null) {
			String value = vis.getAttribute("visibility");
			if (!value.equals(newValue)) {
				logMismatch(specElement, prop, "Visibility", newValue, value);
			}
		} else if (prop.hidden == SpecObject.TRI_TRUE) {
			logUpdate(specElement, prop, "Visibility", newValue);
			rom.setPropertyVisibility(romElement, prop.name, newValue);
		}

	}

	public void applyContext(SpecProperty prop, Element romProp) throws IOException {
		if (prop.exprContext == null)
			return;

		String type = romProp.getAttribute(PROPERTY_TYPE);
		if (type == null || !type.equals("expression")) {
			logNotice(specElement, prop, "Spec expression context, but ROM propety type is " + type);
			return;
		}

		String newValue = null;
		if (prop.exprContext.equalsIgnoreCase("Factory"))
			newValue = "factory";
		else if (prop.exprContext.equalsIgnoreCase("Presentation"))
			newValue = "presentation";
		else if (prop.exprContext.equalsIgnoreCase("Element"))
			newValue = "element";
		else {
			logNotice(specElement, prop, "Spec has unknown value for expr context: " + prop.exprContext);
			return;
		}
		applyAttrib(prop, romProp, newValue, CONTEXT);
	}

	static class TransformException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TransformException(String msg) {
			super(msg);
		}
	}
}
