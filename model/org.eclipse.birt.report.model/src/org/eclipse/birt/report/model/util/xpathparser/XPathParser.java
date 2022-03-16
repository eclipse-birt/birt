/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 *
 */

package org.eclipse.birt.report.model.util.xpathparser;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.util.xpathparser.XDepthParser.DepthInfo;

/**
 * To parse xpath string.
 *
 */

public class XPathParser {

	// ******** the type of current element *****

	private static final int INVALID = XDepthParser.INVALID;
	private static final int ELEMENT = XDepthParser.ELEMENT;
	private static final int SLOT = XDepthParser.SLOT;
	private static final int PROPERTY = XDepthParser.PROPERTY;
	private static final int VALUE = 16;
	private static final int STRUCTURE = 32;

	// ******** keep the property *****

	// private CachedMemberRef ref = null;

	/**
	 * The object that represents the xpath string parsed.
	 */
	private Object parsedHandle = null;

	/**
	 * The current parsed element.
	 */

	private DesignElementHandle currentElement = null;

	/**
	 * The root node.
	 */

	private ModuleHandle module = null;

	/**
	 * The current parsed tag type.
	 */

	private int valueType = INVALID;

	// ******** the slot id if the current element should be slot *****

	private int slotID = DesignElement.NO_SLOT;

	private String propertyName = null;

	private boolean isValid = true;

	/**
	 * Default constructor.
	 *
	 * @param module the report/library
	 */

	public XPathParser(ModuleHandle module) {
		this.module = module;
	}

	/**
	 * Returns the instance that matches input xpath in string.
	 *
	 * @param input the xpath in string
	 *
	 * @return the corresponding instance. Can be
	 *         <code>DesignElementHandle</code>/<code>SlotHandle</code>/
	 *         <code>StructureHandle</code>.
	 */

	public Object getObject(String input) {

		XDepthParser parser_1 = new XDepthParser(input);

		try {
			parser_1.parse();
		} catch (ParseException | TokenMgrError e) {
			return null;
		}

		List depthInfo = parser_1.getDepthInfo();

		// ******** the index in the XPath tags *****

		int index = -1;
		currentElement = module;
		int lastValueType = INVALID;

		for (int i = 0; i < depthInfo.size(); i++) {
			DepthInfo oneDepth = (DepthInfo) depthInfo.get(i);

			lastValueType = valueType;
			valueType = getNextValueType(valueType);

			String tagName = oneDepth.getTagName();
			index = oneDepth.getIndex();

			// ******** the property name and value in the XPath tags *****

			String attrName = oneDepth.getPropertyName();
			String attrValue = oneDepth.getPropertyValue();

			ElementDefn elementDefn = (ElementDefn) currentElement.getDefn();

			if (i == 0 && tagName.equalsIgnoreCase(elementDefn.getXmlName()) && module == currentElement) {
				valueType = ELEMENT;
				continue;
			}

			if (((valueType & VALUE) != 0) && tagName != null
					&& tagName.equalsIgnoreCase(DesignSchemaConstants.VALUE_TAG)) {
				valueType = VALUE;
				continue;
			}

			// try slot first.

			if ((valueType & SLOT) != 0) {
				slotID = getSlotId(elementDefn, tagName, true);
				if (slotID != DesignElement.NO_SLOT) {
					valueType = SLOT;
					continue;
				}
			}

			if ((valueType & ELEMENT) != 0) {
				// current item should be item, then last item may be slot.

				if (lastValueType != SLOT) {
					slotID = getSlotId(elementDefn, tagName);
					if (slotID != DesignElement.NO_SLOT) {
						valueType = ELEMENT;
					}
				}

				// current item should be item, then last item may be property.

				if (lastValueType == PROPERTY && propertyName != null) {
					int typeCode = elementDefn.getProperty(propertyName).getTypeCode();
					if (typeCode == IPropertyType.ELEMENT_TYPE || typeCode == IPropertyType.CONTENT_ELEMENT_TYPE) {
						valueType = ELEMENT;
					}
				}

				// try contents then.

				if (valueType == ELEMENT) {
					if (attrName == null || DesignSchemaConstants.ID_ATTRIB.equalsIgnoreCase(attrName)
							|| XPathUtil.SLOT_NAME_PROPERTY.equalsIgnoreCase(attrName)) {
						currentElement = findElement(tagName, attrName, attrValue);

						// if element is not found, then the iid is invalid and
						// break searching
						if (currentElement == null) {
							valueType = INVALID;
							break;
						}

						if (XPathUtil.SLOT_NAME_PROPERTY.equalsIgnoreCase(attrName)) {
							slotID = getSlotId(currentElement.getDefn(), attrValue, false);
							if (slotID != DesignElement.NO_SLOT) {
								valueType = SLOT;
							}
						}
						continue;
					}
				}
			}

			if ((valueType & STRUCTURE) != 0 && isStructureTag(tagName)) {
				valueType = STRUCTURE;
				parsedHandle = parsePropertyValue(tagName, attrValue, index);
			}

			if (((valueType & ELEMENT) != 0 || (valueType & PROPERTY) != 0) && isPropertyTag(tagName)) {
				valueType = PROPERTY;
				parsedHandle = parsePropertyValue(tagName, attrValue, index);
			}

			if (valueType == INVALID) {
				break;
			}

		}

		switch (valueType) {
		case SLOT:
			return currentElement.getSlot(slotID);
		case ELEMENT:
			return currentElement;
		case PROPERTY:
		case STRUCTURE:
			return getRespectivePropertyHandle(index);
		case VALUE:
			if (lastValueType == PROPERTY) {
				Object temp = getRespectivePropertyHandle(-1);
				assert temp instanceof PropertyHandle;
				return ((PropertyHandle) temp).get(index);
			}
			return null;
		default:
			return null;
		}
	}

	/**
	 * Returns the index of the slot that can contain elements of which the report
	 * tag is <code>tagName</code>.
	 *
	 * @param defn    the element definition
	 * @param tagName the tag in the report design/library
	 * @return the index of matched slot
	 */

	private static int getSlotId(IElementDefn defn, String tagName) {
		for (int j = 0; j < defn.getSlotCount(); j++) {
			SlotDefn slot = (SlotDefn) defn.getSlot(j);
			if (isContentType(slot, tagName)) {
				return j;
			}
		}
		return DesignElement.NO_SLOT;
	}

	/**
	 * Returns the index of the slot that matches the slot name/xmlName.
	 *
	 * @param defn     the element definition
	 * @param slotName the slot name/xmlName
	 * @return the index of matched slot
	 */

	private int getSlotId(IElementDefn defn, String slotName, boolean isXmlName) {
		for (int j = 0; j < defn.getSlotCount(); j++) {
			SlotDefn slot = (SlotDefn) defn.getSlot(j);
			String name = null;
			if (isXmlName) {
				name = slot.getXmlName();
			} else {
				name = slot.getName();
			}
			if (slotName.equalsIgnoreCase(name)) {
				return j;
			}
		}

		return DesignElement.NO_SLOT;
	}

	private static int getNextValueType(int tmpValueType) {

		int retValueType = INVALID;

		switch (tmpValueType) {
		case SLOT:
			retValueType = ELEMENT;
			break;
		case ELEMENT:
			retValueType = SLOT | PROPERTY | ELEMENT | STRUCTURE;
			break;
		case PROPERTY:
			retValueType = PROPERTY | ELEMENT | VALUE | STRUCTURE;
			break;
		case STRUCTURE:
			retValueType = PROPERTY;
			break;
		default:
			retValueType = ELEMENT | SLOT | PROPERTY;
			break;
		}
		return retValueType;

	}

	private Object getRespectivePropertyHandle(int index) {
		// if something is wrong during the parser, that is 'isValid' is set to
		// FALSE, return null;
		if (!isValid) {
			return null;
		}

		if (parsedHandle == null) {
			if (propertyName == null) {
				return null;
			}

			return new PropertyHandle(currentElement, propertyName);
		}

		if (valueType == STRUCTURE) {
			if (parsedHandle instanceof PropertyHandle) {

				PropertyHandle propHandle = (PropertyHandle) parsedHandle;
				if (index < 0 || index >= propHandle.getListValue().size()) {
					return null;
				}
				return propHandle.getAt(index);
			} else if (parsedHandle instanceof MemberHandle) {
				MemberHandle memberHandle = (MemberHandle) parsedHandle;
				if (index < 0 || index >= memberHandle.getListValue().size()) {
					return null;
				}
				return memberHandle.getAt(index);
			} else if (parsedHandle instanceof StructureHandle) {
				return parsedHandle;
			}
			return null;
		}

		// can not return MemberHandle, so add this constraint, think only
		// PropertyHandle and strucrureHandle is valid
		if (parsedHandle instanceof PropertyHandle || parsedHandle instanceof StructureHandle) {
			return parsedHandle;
		}

		return null;
	}

	private Object parsePropertyValue(String tagName, String propName, int index) {
		// if something has wrong before, need do nothing now
		if (!isValid) {
			return parsedHandle;
		}

		if (propertyName == null) {
			IPropertyDefn propDefn = currentElement.getPropertyDefn(propName);
			if (propDefn == null) {
				isValid = false;
				return null;
			}

			propertyName = propName;

			if (propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE) {
				parsedHandle = new PropertyHandle(currentElement, (ElementPropertyDefn) propDefn);
			}
			return parsedHandle;
		}

		// case for the resource key

		if (parsedHandle == null && propertyName != null
				&& DesignSchemaConstants.KEY_ATTRIB.equalsIgnoreCase(propName)) {
			String newPropName = propertyName + XPathUtil.RESOURCE_KEY_SUFFIX;
			ElementDefn elementDefn = (ElementDefn) currentElement.getDefn();
			IPropertyDefn propDefn = elementDefn.getProperty(newPropName);
			if (propDefn != null) {
				propertyName = newPropName;
			} else {
				propertyName = null;
			}

			return null;
		}

		// case for the simple property list

		if (parsedHandle == null && propertyName != null
				&& DesignSchemaConstants.SIMPLE_PROPERTY_LIST_TAG.equalsIgnoreCase(tagName)) {
			IPropertyDefn propDefn = currentElement.getPropertyDefn(propName);
			if (propDefn == null) {
				isValid = false;
				return null;
			}

			propertyName = propName;

			if (index > 0 && propDefn.getTypeCode() == IPropertyType.LIST_TYPE) {
				parsedHandle = new PropertyHandle(currentElement, (ElementPropertyDefn) propDefn);
			}

			return parsedHandle;
		}

		// assert parsedHandle != null;

		if (parsedHandle instanceof PropertyHandle && DesignSchemaConstants.STRUCTURE_TAG.equalsIgnoreCase(tagName)) {
			PropertyHandle propHandle = (PropertyHandle) parsedHandle;
			if (index < 0 || index >= propHandle.getListValue().size()) {
				isValid = false;
			} else {
				parsedHandle = propHandle.getAt(index);
			}

			return parsedHandle;
		}

		if (DesignSchemaConstants.LIST_PROPERTY_TAG.equalsIgnoreCase(tagName)) {
			if (parsedHandle instanceof PropertyHandle) {
				PropertyHandle propHandle = (PropertyHandle) parsedHandle;
				Object value = propHandle.getValue();
				if (value instanceof Structure) {
					Structure struct = (Structure) value;
					StructureHandle structHandle = struct.getHandle(propHandle);
					parsedHandle = structHandle.getMember(propName);
					if (parsedHandle == null) {
						isValid = false;
					}
				}
			} else if (parsedHandle instanceof StructureHandle) {
				StructureHandle structHandle = (StructureHandle) parsedHandle;
				parsedHandle = structHandle.getMember(propName);
				if (parsedHandle == null) {
					isValid = false;
				}
			}

			return parsedHandle;
		}

		if (parsedHandle instanceof MemberHandle && DesignSchemaConstants.STRUCTURE_TAG.equalsIgnoreCase(tagName)) {
			MemberHandle memberHandle = (MemberHandle) parsedHandle;
			if (index < 0 || index >= memberHandle.getListValue().size()) {
				isValid = false;
			} else {
				parsedHandle = memberHandle.getAt(index);
			}

			return parsedHandle;
		}

		return null;
	}

	/**
	 * Checks whether the element can be contained in the given slot.
	 *
	 * @param slot            the slot definition
	 * @param elementDefnName the name of the element definition
	 * @return <code>true</code> if the element can be contained in the given slot.
	 *         Otherwise <code>false</code>.
	 */

	private static boolean isContentType(SlotDefn slot, String elementDefnName) {

		List tmpElementDefns = slot.getContentElements();
		for (int i = 0; i < tmpElementDefns.size(); i++) {
			ElementDefn tmpContentDefn = (ElementDefn) tmpElementDefns.get(i);
			if (elementDefnName.equalsIgnoreCase(tmpContentDefn.getXmlName())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the element that is contained by the current element.
	 *
	 * @param contentDefnName the element xml name
	 * @param attrName        the element property name
	 * @param attrValue       the element property value
	 * @return the matched element
	 */

	private DesignElementHandle findElement(String contentDefnName, String attrName, String attrValue) {
		long id = DesignElement.NO_ID;
		if (DesignSchemaConstants.ID_ATTRIB.equalsIgnoreCase(attrName)) {
			try {
				id = Long.parseLong(attrValue);
			} catch (NumberFormatException e) {
				return null;
			}

		}

		if (slotID != DesignElement.NO_SLOT) {
			SlotHandle slot = currentElement.getSlot(slotID);
			for (int i = 0; i < slot.getCount(); i++) {
				DesignElementHandle tmpElement = slot.get(i);
				ElementDefn tmpDefn = (ElementDefn) tmpElement.getDefn();

				if ((DesignElement.NO_ID == id || tmpElement.getID() == id)
						&& tmpDefn.getXmlName().equalsIgnoreCase(contentDefnName)) {
					return tmpElement;
				}
			}
		}

		PropertyHandle prop = currentElement.getPropertyHandle(propertyName);
		if (propertyName != null && prop != null) {
			List contents = prop.getContents();
			for (int i = 0; i < contents.size(); i++) {
				DesignElementHandle tmpElement = (DesignElementHandle) contents.get(i);
				ElementDefn tmpDefn = (ElementDefn) tmpElement.getDefn();
				if ((DesignElement.NO_ID == id || tmpElement.getID() == id)
						&& tmpDefn.getXmlName().equalsIgnoreCase(contentDefnName)) {
					return tmpElement;
				}
			}
		}

		return null;
	}

	/**
	 * Checks if tag is structure tag
	 *
	 * @param tagName tag name
	 * @return if tag is property tag or list property tag return true; else return
	 *         false.
	 */
	private static boolean isStructureTag(String tagName) {
		if (DesignSchemaConstants.STRUCTURE_TAG.equalsIgnoreCase(tagName)) {
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the input tag is the property tag.
	 *
	 * @param tagName the tag
	 * @return <code>true</code> if it is property tag. Otherwise
	 *         <code>false</code>.
	 */

	private static boolean isPropertyTag(String tagName) {
		if (tagName == null) {
			return true;
		}

		if (DesignSchemaConstants.PROPERTY_TAG.equalsIgnoreCase(tagName)) {
			return true;
		} else if (DesignSchemaConstants.LIST_PROPERTY_TAG.equalsIgnoreCase(tagName)) {
			return true;
		} else if (DesignSchemaConstants.METHOD_TAG.equalsIgnoreCase(tagName)) {
			return true;
		} else if (DesignSchemaConstants.EXPRESSION_TAG.equalsIgnoreCase(tagName)) {
			return true;
		} else if (DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG.equalsIgnoreCase(tagName)) {
			return true;
		} else if (DesignSchemaConstants.TEXT_PROPERTY_TAG.equalsIgnoreCase(tagName)) {
			return true;
		} else if (DesignSchemaConstants.HTML_PROPERTY_TAG.equalsIgnoreCase(tagName)) {
			return true;
		} else if (DesignSchemaConstants.XML_PROPERTY_TAG.equalsIgnoreCase(tagName)) {
			return true;
		} else if (DesignSchemaConstants.SIMPLE_PROPERTY_LIST_TAG.equalsIgnoreCase(tagName)) {
			return true;
		} else if (DesignSchemaConstants.EX_PROPERTY_TAG.equalsIgnoreCase(tagName)) {
			return true;
		}

		return false;
	}
}
