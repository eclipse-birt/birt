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

package org.eclipse.birt.report.model.metadata;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;

import org.eclipse.birt.core.util.CommonUtil;

/**
 * Wrapper class for all core tests to access some members and methods, which
 * are invisible to external projects.
 */

public class MetadataTestUtil {

	/**
	 * Sets the name for the element definition.
	 *
	 * @param defn
	 * @param name
	 */

	public static void setName(ObjectDefn defn, String name) {
		if (defn != null) {
			defn.setName(name);
		}
	}

	/**
	 * Sets the name for the element definition.
	 *
	 * @param defn
	 * @param name
	 */

	public static void setName(SlotDefn defn, String name) {
		if (defn != null) {
			defn.setName(name);
		}
	}

	/**
	 *
	 * @param dict
	 * @param defn
	 * @throws MetaDataException
	 */
	public static void addElementDefn(MetaDataDictionary dict, ElementDefn defn) throws MetaDataException {
		if (dict != null) {
			dict.addElementDefn(defn);
		}
	}

	/**
	 *
	 * @param style
	 * @param name
	 */
	public static void setPredefinedStyleName(PredefinedStyle style, String name) {
		if (style != null) {
			style.setName(name);
		}
	}

	/**
	 *
	 * @param style
	 * @param key
	 */

	public static void setPredefinedStyleDisplayNameKey(PredefinedStyle style, String key) {
		if (style != null) {
			style.setDisplayNameKey(key);
		}
	}

	/**
	 *
	 * @param dict
	 * @param style
	 * @throws MetaDataException
	 */
	public static void addPredefinedStyle(MetaDataDictionary dict, PredefinedStyle style) throws MetaDataException {
		if (dict != null) {
			dict.addPredefinedStyle(style);
		}
	}

	/**
	 *
	 * @param defn
	 * @throws MetaDataException
	 */
	public static void buildPropertyDefn(PropertyDefn defn) throws MetaDataException {
		if (defn != null) {
			defn.build();
		}
	}

	/**
	 *
	 * @param dict
	 * @param choices
	 * @throws MetaDataException
	 */
	public static void addChoiceSet(MetaDataDictionary dict, ChoiceSet choices) throws MetaDataException {
		if (dict != null) {
			dict.addChoiceSet(choices);
		}
	}

	/**
	 *
	 * @param dict
	 * @param struct
	 * @throws MetaDataException
	 */
	public static void addStructureDefn(MetaDataDictionary dict, StructureDefn struct) throws MetaDataException {
		if (dict != null) {
			dict.addStructure(struct);
		}
	}

	/**
	 *
	 * @param defn
	 * @param isList
	 */
	public static void setIsList(PropertyDefn defn, boolean isList) {
		if (defn != null) {
			defn.setIsList(isList);
		}
	}

	/**
	 * @param defn
	 * @param value
	 */
	public static void setPropertyDefnDefault(PropertyDefn defn, Object value) {
		if (defn != null) {
			defn.setDefault(value);
		}
	}

	/**
	 *
	 * @param element
	 * @param useProperties
	 */
	public static void setAllowsUserProperties(ElementDefn element, boolean useProperties) {
		if (element != null) {
			element.setAllowsUserProperties(useProperties);
		}
	}

	/**
	 *
	 * @param element
	 * @param canExtends
	 */
	public static void setCanExtends(ElementDefn element, boolean canExtends) {
		if (element != null) {
			element.setCanExtend(canExtends);
		}
	}

	/**
	 *
	 * @param element
	 * @param key
	 */
	public static void setDisplayNameKey(ObjectDefn element, String key) {
		if (element != null) {
			element.setDisplayNameKey(key);
		}
	}

	/**
	 *
	 * @param slot
	 * @param key
	 */
	public static void setDisplayNameKey(SlotDefn slot, String key) {
		if (slot != null) {
			slot.setDisplayNameID(key);
		}
	}

	/**
	 *
	 * @param element
	 * @param base
	 */
	public static void setExtends(ElementDefn element, String base) {
		if (element != null) {
			element.setExtends(base);
		}
	}

	/**
	 *
	 * @param element
	 * @param hasStyle
	 */
	public static void setHasStyle(ElementDefn element, boolean hasStyle) {
		if (element != null) {
			element.setHasStyle(hasStyle);
		}
	}

	/**
	 *
	 * @param element
	 * @param option
	 */
	public static void setNameOption(ElementDefn element, int option) {
		if (element != null) {
			element.setNameOption(option);
		}
	}

	/**
	 *
	 * @param element
	 * @param id
	 */
	public static void setNameSpaceID(ElementDefn element, String id) {
		if (element != null) {
			element.setNameSpaceID(id);
		}
	}

	/**
	 *
	 * @param element
	 * @param isAbstract
	 */
	public static void setAbstract(ElementDefn element, boolean isAbstract) {
		if (element != null) {
			element.setAbstract(isAbstract);
		}
	}

	/**
	 *
	 * @param element
	 * @throws MetaDataException
	 */

	public static void build(ElementDefn element) throws MetaDataException {
		if (element != null) {
			element.build();
		}
	}

	/**
	 *
	 * @param element
	 * @param propName
	 */
	public static void addStyleProp(ElementDefn element, String propName) {
		if (element != null) {
			element.addStyleProperty(propName);
		}
	}

	/**
	 *
	 * @param slot
	 * @param flag
	 */
	public static void setMultipleCardinality(SlotDefn slot, boolean flag) {
		if (slot != null) {
			slot.setMultipleCardinality(flag);
		}
	}

	/**
	 *
	 * @param slot
	 * @param type
	 */
	public static void addType(SlotDefn slot, String type) {
		if (slot != null) {
			slot.addType(type);
		}
	}

	/**
	 *
	 * @param element
	 * @param slot
	 */
	public static void addSlot(ElementDefn element, SlotDefn slot) {
		if (element != null) {
			element.addSlot(slot);
		}
	}

	/**
	 *
	 * @param prop
	 * @param isStyle
	 */

	public static void setStyleProperty(SystemPropertyDefn prop, boolean isStyle) {
		if (prop != null) {
			prop.setStyleProperty(isStyle);
		}
	}

	/**
	 *
	 * @param prop
	 * @param flag
	 */
	public static void setIntrinsic(PropertyDefn prop, boolean flag) {
		if (prop != null) {
			prop.setIntrinsic(flag);
		}
	}

	/**
	 *
	 * @param message
	 */
	public static void log(String message) {
		MetaLogManager.log(message);
	}

	/**
	 *
	 * @param slot
	 * @param id
	 */
	public static void setID(SlotDefn slot, int id) {
		if (slot != null) {
			slot.setSlotID(id);
		}
	}

	/**
	 *
	 * @param slot
	 * @throws MetaDataException
	 */
	public static void build(SlotDefn slot) throws MetaDataException {
		if (slot != null) {
			slot.build();
		}
	}

	/**
	 *
	 * @param defn
	 * @param prop
	 * @throws MetaDataException
	 */
	public static void addPropertyDefn(ObjectDefn defn, PropertyDefn prop) throws MetaDataException {
		if (defn != null) {
			defn.addProperty(prop);
		}
	}

	/**
	 * Only reads the given metadata with the specified stream.
	 *
	 * @param inputStream meta source file stream.
	 * @throws MetaDataParserException
	 */

	public static void readRom(InputStream inputStream) throws MetaDataParserException {
		InputStream internalStream = inputStream;
		MetaDataHandlerImpl handler = new MetaDataHandlerImpl();

		try {
			SAXParser parser = CommonUtil.createSAXParser();
			parser.parse(internalStream, handler);
		} catch (Exception e) {
			MetaLogManager.log("Metadata parsing error", e); //$NON-NLS-1$
			throw new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_PARSER_ERROR);
		}
	}
}
