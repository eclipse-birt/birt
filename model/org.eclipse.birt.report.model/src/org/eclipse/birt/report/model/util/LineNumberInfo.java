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

package org.eclipse.birt.report.model.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.VariableElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Holds line number information for a module.
 */

public class LineNumberInfo {

	/**
	 * Checks whether the given structure supports line number.
	 *
	 * @param struct the structure
	 * @return true if it supports line number, otherwise false;
	 */
	public static boolean isLineNumberSuppoerted(IStructure struct) {
		if (struct instanceof IncludedCssStyleSheet || struct instanceof IncludedLibrary
				|| struct instanceof EmbeddedImage || struct instanceof ResultSetColumn
				|| struct instanceof LevelAttribute) {
			return true;
		}

		return false;
	}

	/**
	 * The module.
	 */
	private Module module;
	/**
	 * The hash map for the element id-to-lineNumber lookup. Key is the id of design
	 * element. Value is the line number.
	 */

	private Map<Long, Integer> elementMap = null;

	/**
	 * The hash map for the xpath string-to-lineNumber lookup. Key is the xPath of
	 * the slot or property and value is the line number.
	 */

	private Map<String, Integer> xpathMap = null;

	/**
	 * The hash map for the <code>IncludeLibrary</code> structures
	 * namespace-to-lineNumber lookup. Key is the namespace string of included
	 * library.
	 */

	private Map<String, Integer> includeLibStructMap = null;

	/**
	 * The hash map for the <code>EmbeddedImage</code> structures name-to-lineNumber
	 * lookup. Key is the name of the embedded image.
	 */

	private Map<String, Integer> embeddedImageStructMap = null;

	/**
	 * Hash map for the <code>CssStyleSheet</code> structures namespace. Key is the
	 * file name of the included css style sheet.
	 */
	private Map<String, Integer> includedCssStyleSheetStructMap = null;

	/**
	 * Hash map for the <code>Variable</code> elements namespace. Key is the file
	 * name of the variable element.
	 */
	private Map<String, Integer> variablesMap = null;

	/**
	 * The line number for theme property in report design.
	 */

	private int themeProp = 1;

	/**
	 * Constructor.
	 *
	 * @param module
	 */

	public LineNumberInfo(Module module) {
		this.module = module;
		elementMap = Collections.synchronizedMap(new HashMap<Long, Integer>());
		includeLibStructMap = Collections.synchronizedMap(new HashMap<String, Integer>());
		embeddedImageStructMap = Collections.synchronizedMap(new HashMap<String, Integer>());
		includedCssStyleSheetStructMap = Collections.synchronizedMap(new HashMap<String, Integer>());
		xpathMap = Collections.synchronizedMap(new HashMap<String, Integer>());
		variablesMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	}

	/**
	 * Puts the line number of the object.
	 *
	 * Note: currently, only support put line number of DesignElement,
	 * EmbeddedImage, IncludeLibrary property and theme property.
	 *
	 * @param obj    the object
	 * @param lineNo line number
	 */

	public void put(Object obj, Integer lineNo) {
		if (obj instanceof PropertyDefn) {
			themeProp = lineNo == null ? 1 : lineNo.intValue();
		} else if (obj instanceof DesignElement) {
			if (obj instanceof VariableElement) {
				variablesMap.put(((VariableElement) obj).getName(), lineNo);
			} else {
				elementMap.put(((DesignElement) obj).getID(), lineNo);
			}
		} else if (obj instanceof IStructure) {
			putStructure((IStructure) obj, lineNo);
		} else if (obj instanceof ContainerContext || obj instanceof PropertyHandle || obj instanceof SlotHandle) {
			putXPathLineNo(obj, lineNo);
		}
	}

	private void putXPathLineNo(Object obj, Integer lineNo) {
		String xpath = getXPath(obj);
		if (xpath == null) {
			assert false;
			return;
		}
		xpathMap.put(xpath, lineNo);
	}

	/**
	 * Puts the line number for the given structure.
	 *
	 * @param struct the structure
	 * @param lineNo the line number
	 */
	private void putStructure(IStructure struct, Integer lineNo) {
		if (!isLineNumberSuppoerted(struct)) {
			return;
		}

		if (struct instanceof IncludedLibrary) {
			includeLibStructMap.put(((IncludedLibrary) struct).getNamespace(), lineNo);
		} else if (struct instanceof EmbeddedImage) {
			embeddedImageStructMap.put(((EmbeddedImage) struct).getName(), lineNo);
		} else if (struct instanceof IncludedCssStyleSheet) {
			includedCssStyleSheetStructMap.put(((IncludedCssStyleSheet) struct).getFileName(), lineNo);
		} else {
			Structure structure = (Structure) struct;
			StructureHandle handle = new StructureHandle(structure.getElement().getHandle(module),
					structure.getContext());
			putXPathLineNo(handle, lineNo);
		}
	}

	/**
	 * Gets the line number of object.
	 *
	 * Note: currently, only support get line number of DesignElement,
	 * EmbeddedImage, IncludeLibrary property and theme property.
	 *
	 * @param obj object
	 * @return line number
	 */

	public int get(Object obj) {
		Module tmpModule = null;

		if (obj instanceof Theme && (tmpModule = ((Theme) obj).getRoot()) instanceof Library
				&& ((Library) tmpModule).getHost() != null) {
			return themeProp;
		} else if (obj instanceof Library && ((Library) obj).getHost() != null) {
			return intValue(includeLibStructMap.get(((Library) obj).getNamespace()));
		} else if (obj instanceof DesignElement) {
			if (obj instanceof VariableElement) {
				return variablesMap.get(((VariableElement) obj).getName());
			}
			return getElementLineNo(((DesignElement) obj).getID());
		} else if (obj instanceof StructureHandle) {
			StructureHandle structHandle = (StructureHandle) obj;
			if (isLineNumberSuppoerted(structHandle.getStructure())) {
				return getStructureLineNo(structHandle);
			}
		} else if (obj instanceof SlotHandle || obj instanceof PropertyHandle) {
			return getXPathLineNo(obj);
		}

		return 1;
	}

	/**
	 * Gets the line number for the given structure.
	 *
	 * @param structHandle the handle of the structure
	 * @return the line number
	 */
	private int getStructureLineNo(StructureHandle structHandle) {
		IStructure struct = structHandle.getStructure();
		if (struct instanceof EmbeddedImage) {
			return intValue(embeddedImageStructMap.get(((EmbeddedImage) struct).getName()));
		} else if (struct instanceof IncludedCssStyleSheet) {
			return intValue(includedCssStyleSheetStructMap.get(((IncludedCssStyleSheet) struct).getFileName()));
		} else if (struct instanceof IncludedLibrary) {
			return intValue(includeLibStructMap.get(((IncludedLibrary) struct).getNamespace()));
		}
		return getXPathLineNo(structHandle);
	}

	/**
	 * Returns the line number for the given object.
	 *
	 * @param obj the object
	 * @return line number
	 */

	private int getXPathLineNo(Object obj) {
		String xpath = getXPath(obj);
		if (xpath == null) {
			assert false;
			return 1;
		}
		return intValue(xpathMap.get(xpath));
	}

	/**
	 * This method is for a deprecated method.
	 *
	 * @param id the id
	 * @return line number
	 */

	public int getElementLineNo(long id) {
		return intValue(elementMap.get(Long.valueOf(id)));
	}

	/**
	 * Gets int value of an integer.
	 *
	 * @param obj Integer object
	 * @return int value
	 */

	private int intValue(Integer obj) {
		return obj == null ? 1 : obj.intValue();
	}

	/**
	 * Gets xpath for the given object.
	 *
	 * @param obj the object
	 * @return the xpath for the object
	 */
	private String getXPath(Object obj) {
		if (obj instanceof ContainerContext) {
			ContainerContext context = (ContainerContext) obj;
			DesignElementHandle handle = context.getElement().getHandle(module);
			if (context.isROMSlot()) {
				obj = handle.getSlot(context.getSlotID());
			} else {
				obj = handle.getPropertyHandle(context.getPropertyName());
			}
		}
		if (obj instanceof Structure) {
			Structure struct = (Structure) obj;
			obj = new StructureHandle(struct.getElement().getHandle(module), struct.getContext());
		}
		if (obj instanceof PropertyHandle || obj instanceof StructureHandle || obj instanceof SlotHandle) {
			return XPathUtil.getXPath(obj);
		}

		return null;
	}
}
