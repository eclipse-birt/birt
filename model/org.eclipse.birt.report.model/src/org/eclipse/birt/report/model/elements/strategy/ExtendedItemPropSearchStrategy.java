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

package org.eclipse.birt.report.model.elements.strategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.MultiViews;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.SystemPropertyDefn;

/**
 * Provides the specific property searching route for <code>ExtendedItem</code>.
 */

public class ExtendedItemPropSearchStrategy extends ReportItemPropSearchStrategy {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(ExtendedItemPropSearchStrategy.class.getName());

	/**
	 * Data binding properties for the report items.
	 */

	protected final static Set<Integer> hostViewRelatedProps;

	static {
		Set<Integer> tmpSet = new HashSet<Integer>();

		// add all section style properties
		tmpSet.add(Integer.valueOf(IStyleModel.DISPLAY_PROP.hashCode()));
		tmpSet.add(Integer.valueOf(IStyleModel.MASTER_PAGE_PROP.hashCode()));
		tmpSet.add(Integer.valueOf(IStyleModel.PAGE_BREAK_BEFORE_PROP.hashCode()));
		tmpSet.add(Integer.valueOf(IStyleModel.PAGE_BREAK_AFTER_PROP.hashCode()));
		tmpSet.add(Integer.valueOf(IStyleModel.PAGE_BREAK_INSIDE_PROP.hashCode()));
		tmpSet.add(Integer.valueOf(IStyleModel.SHOW_IF_BLANK_PROP.hashCode()));

		// add: toc, bookmark
		tmpSet.add(Integer.valueOf(IReportItemModel.TOC_PROP.hashCode()));
		tmpSet.add(Integer.valueOf(IReportItemModel.BOOKMARK_PROP.hashCode()));

		// add: visibility rules
		tmpSet.add(Integer.valueOf(IReportItemModel.VISIBILITY_PROP.hashCode()));

		// add: allExport
		tmpSet.add(Integer.valueOf(IReportItemModel.ALLOW_EXPORT_PROP.hashCode()));

		// add: zIndex
		tmpSet.add(Integer.valueOf(IReportItemModel.Z_INDEX_PROP.hashCode()));

		hostViewRelatedProps = Collections.unmodifiableSet(tmpSet);
	}

	private final static ExtendedItemPropSearchStrategy instance = new ExtendedItemPropSearchStrategy();

	/**
	 * Protected constructor.
	 */

	protected ExtendedItemPropSearchStrategy() {
	}

	/**
	 * Returns the instance of <code>ExtendedItemPropSearchStrategy</code> which
	 * provide the specific property searching route for <code>ExtendedItem</code>.
	 * 
	 * @return the instance of <code>ExtendedItemPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.core.PropertySearchStrategy#
	 * getPropertyFromSelfSelector(org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn, org.eclipse.birt
	 * .report.model.core.PropertySearchStrategy.PropertyValueInfo)
	 */

	public Object getPropertyFromSelfSelector(Module module, DesignElement element, ElementPropertyDefn prop,
			PropertyValueInfo valueInfo) {
		ExtendedItem extendedItem = (ExtendedItem) element;
		Object value = null;
		Object selectorValue = null;

		// find the selector defined in extension definition
		IElementDefn elementDefn = extendedItem.getExtDefn();
		if (elementDefn != null) {
			String selector = extendedItem.getExtDefn().getSelector();
			selectorValue = getPropertyFromSelector(module, extendedItem, prop, selector, valueInfo);
			if (selectorValue != null) {
				if (value == null)
					value = selectorValue;

				// if valueInfo is null, then do the short search; otherwise, we
				// must do full search to collect all the selectors
				if (valueInfo == null)
					return value;

			}
		}

		// find other pre-defined styles, such as selector : x-tab header, x-tab
		// detail, it has the highest priority than other selector
		selectorValue = getPropertyFromPredefinedStyles(module, extendedItem, prop, valueInfo);
		if (selectorValue != null) {
			if (value == null)
				value = selectorValue;

			// if valueInfo is null, then do the short search; otherwise, we
			// must do full search to collect all the selectors
			if (valueInfo == null)
				return value;
		}

		// "extended-item" selector is not enabled in ROM. There is no need to
		// search this selector.
		return value;
	}

	/**
	 * Gets the property value from some predefined-styles in this extended item.
	 * Such as x-tab header, x-tab footer.
	 * 
	 * @param module
	 * @param extendedItem
	 * @param prop
	 * @return
	 */

	private Object getPropertyFromPredefinedStyles(Module module, ExtendedItem extendedItem, ElementPropertyDefn prop,
			PropertyValueInfo valueInfo) {

		List<Object> predefinedStyles = extendedItem.getReportItemDefinedSelectors(module);
		Object value = null;

		if (predefinedStyles == null || predefinedStyles.isEmpty())
			return null;
		for (int i = 0; i < predefinedStyles.size(); i++) {
			Object predefinedStyle = predefinedStyles.get(i);

			// if the item is String, then search the named style in the
			// module and then find property value in it
			if (predefinedStyle instanceof String) {
				String styleName = (String) predefinedStyle;
				Object selectorValue = getPropertyFromSelector(module, extendedItem, prop, styleName, valueInfo);
				if (selectorValue != null) {
					if (value == null)
						value = selectorValue;

					// if valueInfo is null, then do the short search;
					// otherwise, we
					// must do full search to collect all the selectors
					if (valueInfo == null)
						return value;
				}
			} else if (predefinedStyle instanceof IStyleDeclaration) {
				// if the item is a StyleHandle, then read local property
				// value set in this style directly
				IStyleDeclaration style = (IStyleDeclaration) predefinedStyle;

				if (valueInfo != null) {
					StyleElement createdStyle = new Style(style.getName());
					module.makeUniqueName(createdStyle);

					copyValues(module, createdStyle, style);

					valueInfo.addSelectorStyle(createdStyle);
				}

				Object selectorValue = style.getProperty(prop.getName());
				if (selectorValue != null) {
					// do some validation for the value
					try {
						selectorValue = prop.validateValue(module, null, selectorValue);
						if (selectorValue != null) {
							if (value == null)
								value = selectorValue;

							// if valueInfo is null, then do the short search;
							// otherwise, we must do full search to collect all
							// the selectors
							if (valueInfo == null)
								return value;

						}
					} catch (PropertyValueException e) {
						logger.log(Level.WARNING, "property( " + prop.getName() + " ) value " + value + "is invalid"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}
				}
			} else
				assert false;
		}

		return null;
	}

	private void copyValues(Module module, StyleElement target, IStyleDeclaration source) {
		IElementDefn styleDefn = MetaDataDictionary.getInstance().getStyle();
		List<IElementPropertyDefn> props = styleDefn.getProperties();
		for (int i = 0; i < props.size(); i++) {
			ElementPropertyDefn prop = (ElementPropertyDefn) props.get(i);
			if (!prop.isStyleProperty())
				continue;
			Object value = source.getProperty(prop.getName());
			try {
				value = prop.validateValue(module, target, value);
				target.setProperty(prop, value);
			} catch (PropertyValueException e) {
				logger.log(Level.WARNING, "property( " + prop.getName() + " ) value " + value + "is invalid"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy
	 * #getPropertyFromSelf(org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected Object getPropertyFromSelf(Module module, DesignElement element, ElementPropertyDefn prop) {
		if (!(element.getContainer() instanceof MultiViews))
			return super.getPropertyFromSelf(module, element, prop);

		String propName = prop.getName();
		if (!isDataBindingProperty(element, propName) && !isHostViewProperty(element, propName))
			return super.getPropertyFromSelf(module, element, prop);

		DesignElement grandContainer = element.getContainer().getContainer();
		if (grandContainer == null)
			return super.getPropertyFromSelf(module, element, prop);

		// the 'filter' property may be different type, one is structure list
		// and another is content element list
		ElementPropertyDefn grandPropDefn = grandContainer.getPropertyDefn(prop.getName());
		if (grandPropDefn != null && prop.getTypeCode() == grandPropDefn.getTypeCode())
			return grandContainer.getProperty(module, grandPropDefn);
		return super.getPropertyFromSelf(module, element, prop);
	}

	/**
	 * Returns properties that are bound to data related values.
	 * 
	 * @param tmpElement the design element
	 * @return a set containing property names in string
	 */

	private static Set<Integer> getHostViewProperties(DesignElement tmpElement) {
		if (tmpElement instanceof ReportItem)
			return hostViewRelatedProps;

		return Collections.emptySet();

	}

	/**
	 * Checks if the property is the host view property.
	 * 
	 * @param element  the design element.
	 * @param propName the property name.
	 * @return true if the property is the host view property, otherwise return
	 *         false.
	 */
	public static boolean isHostViewProperty(DesignElement element, String propName) {
		if (!(element instanceof ExtendedItem) || StringUtil.isBlank(propName))
			return false;
		return getHostViewProperties(element).contains(Integer.valueOf(propName.hashCode()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.core.PropertySearchStrategy#
	 * getNonIntrinsicPropertyFromElement
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected Object getNonIntrinsicPropertyFromElement(Module module, DesignElement element,
			ElementPropertyDefn prop) {
		ExtendedItem extendedItem = (ExtendedItem) element;

		// find useOwnModel property.

		ElementPropertyDefn propDefn = extendedItem.getPropertyDefn(prop.getName());
		if (propDefn == null)
			return null;

		if (!propDefn.enableContextSearch())
			return super.getNonIntrinsicPropertyFromElement(module, element, prop);

		IReportItem reportItem = extendedItem.getExtendedElement();
		if (reportItem != null)
			return reportItem.getProperty(prop.getName());

		return null;
	}

	/**
	 * Returns the factory property value for the overridden property by the
	 * extension.
	 * 
	 * @param module  the module
	 * @param element the element
	 * @param prop    the property definition
	 * @return the property value
	 */

	public Object getMetaFactoryProperty(Module module, DesignElement element, ElementPropertyDefn prop) {
		assert (prop.isSystemProperty() && ((SystemPropertyDefn) prop).enableContextSearch());
		return super.getNonIntrinsicPropertyFromElement(module, element, prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.PropertySearchStrategy#getStyleContainer
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected DesignElement getStyleContainer(DesignElement designElement) {
		DesignElement tmpContainer = designElement.getContainer();
		if (tmpContainer == null || !(tmpContainer instanceof MultiViews))
			return tmpContainer;

		tmpContainer = tmpContainer.getContainer();

		if (tmpContainer == null)
			return null;

		return tmpContainer.getContainer();

	}
}
