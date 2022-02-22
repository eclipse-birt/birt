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

package org.eclipse.birt.report.model.api.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Utility class to provide some methods about the style element, styled element
 * and style properties.
 *
 */
public class StyleUtil {

	/**
	 * Gets the design element handle with all the style properties are copied to
	 * the returned value. If <code>isCascaded</code> is set to TRUE, the copied
	 * style property value will be the cascaded value, otherwise will be the
	 * factory value.
	 *
	 * @param source
	 * @param isCascaded
	 * @return
	 */
	public static DesignElementHandle copyStyles(DesignElementHandle source, DesignElementHandle target,
			boolean isCascaded) {
		return copyStyleProperties(source, target, isCascaded, false);
	}

	/**
	 * Gets the design element handle with all the style properties are copied to
	 * the returned value. The copied style property value will be the factory value
	 * and not cascaded.
	 *
	 * @param source
	 * @return
	 */
	public static DesignElementHandle copyStyles(DesignElementHandle source, DesignElementHandle target) {
		return copyStyleProperties(source, target, false, false);
	}

	/**
	 * Gets the design element handle with all the style properties are copied to
	 * the returned value. If <code>isCascaded</code> is set to TRUE, the copied
	 * style property value will be the cascaded value, otherwise will be the
	 * factory value.
	 *
	 * @param source
	 * @param isCascaded
	 * @return
	 */
	public static DesignElementHandle copyLocalStyles(DesignElementHandle source, DesignElementHandle target) {
		return copyStyleProperties(source, target, false, true);
	}

	/**
	 * Gets the design element handle with all the style properties are copied to
	 * the returned value. If <code>isCascaded</code> is set to TRUE, the copied
	 * style property value will be the cascaded value, otherwise will be the
	 * factory value.
	 *
	 * @param source
	 * @param isCascaded
	 * @return
	 */
	private static DesignElementHandle copyStyleProperties(DesignElementHandle source, DesignElementHandle target,
			boolean isCascaded, boolean isLocal) {
		if (source == null) {
			return null;
		}

		IElementDefn elementDefn = source.getDefn();
		if (target == null) {
			target = source.getElementFactory().newElement(elementDefn.getName(), null);
		}

		// if the two elements are different types or same element, do nothing
		// and return directly
		// if this element can not define style properties, return directly
		if (target.getDefn() != elementDefn || source == target || !elementDefn.hasStyle()) {
			return target;
		}

		DesignElement copiedElement = target.getElement();
		DesignElement sourceElement = source.getElement();
		Module module = source.getModule();

		// handle style name
		ElementPropertyDefn propDefn = sourceElement.getPropertyDefn(IStyledElementModel.STYLE_PROP);
		copiedElement.setProperty(propDefn, sourceElement.getProperty(module, propDefn));

		// handle style properties
		IElementDefn styleDefn = MetaDataDictionary.getInstance().getStyle();
		List<IElementPropertyDefn> styleProps = styleDefn.getProperties();
		for (int i = 0; i < styleProps.size(); i++) {
			propDefn = (ElementPropertyDefn) styleProps.get(i);
			if (!propDefn.isStyleProperty()) {
				continue;
			}

			// must get the property definition by element itself, for the
			// element may be extended-item or it defines override attribute for
			// this property definition
			ElementPropertyDefn sourcePropDefn = sourceElement.getPropertyDefn(propDefn.getName());
			ElementPropertyDefn targetPropDefn = copiedElement.getPropertyDefn(propDefn.getName());

			if (sourcePropDefn == null || targetPropDefn == null) {
				continue;
			}

			Object value = null;
			if (isLocal) {
				value = sourceElement.getLocalProperty(module, sourcePropDefn);
			} else if (isCascaded) {
				value = sourceElement.getProperty(module, sourcePropDefn);
			} else {
				value = sourceElement.getFactoryProperty(module, sourcePropDefn);
			}

			// set the value to the copied one
			if (value != null) {
				boolean needCopy = false;
				if (value instanceof IStructure) {
					needCopy = true;
				} else if (value instanceof List) {
					needCopy = !((List) value).isEmpty();
					for (Object item : (List) value) {
						if (!(item instanceof Structure)) {
							needCopy = false;
							break;
						}
					}
				}
				if (needCopy) {
					value = ModelUtil.copyValue(targetPropDefn, value);
				}
				copiedElement.setProperty(targetPropDefn, value);
			}
		}

		return target;
	}

	/**
	 * Adds selectors for extended elements to the report design. This action will
	 * be non-undoable, that is, once the selectors are inserted to the design
	 * handle, it will not be removed by undo action.
	 *
	 * @param designHandle
	 */
	public static void addExtensionSelectors(ReportDesignHandle designHandle) {
		if (designHandle == null) {
			return;
		}

		DesignSession.addExtensionDefaultStyles((ReportDesign) designHandle.getModule(), true);
	}

	private static boolean hasExternalCSSURI(Iterator<IncludedCssStyleSheetHandle> iter) {
		while (iter != null && iter.hasNext()) {
			IncludedCssStyleSheetHandle includedCssStyleSheet = (IncludedCssStyleSheetHandle) iter.next();
			String externalCSSURI = includedCssStyleSheet.getExternalCssURI();
			boolean useExternalCSS = includedCssStyleSheet.isUseExternalCss();
			if (externalCSSURI != null || useExternalCSS) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasExternalCSSURI(Module module) {
		if (module instanceof ReportDesign) {
			ReportDesignHandle handle = (ReportDesignHandle) module.getHandle(module);
			Iterator<IncludedCssStyleSheetHandle> iter = handle.includeCssesIterator();
			if (hasExternalCSSURI(iter)) {
				return true;
			}
		}

		Theme theme = module.getTheme();
		if (theme != null) {
			ThemeHandle themeHandle = (ThemeHandle) theme.getHandle(module);
			Iterator<IncludedCssStyleSheetHandle> iter = themeHandle.includeCssesIterator();
			return hasExternalCSSURI(iter);
		}
		return false;
	}

}
