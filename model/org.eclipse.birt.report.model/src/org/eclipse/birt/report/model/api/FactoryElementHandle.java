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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.core.PropertySearchStrategy.PropertyValueInfo;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * Factory element handle class to retrieve some factory styles.
 */
public class FactoryElementHandle {

	private static final String INHERIT_STYLE_NAME = "inherit-style"; //$NON-NLS-1$
	private static final String RELATED_CONTAINER_STYLE_NAME = "related-container-style"; //$NON-NLS-1$

	private DesignElementHandle elementHandle;

	/**
	 * Constructs the factory element handle with the specified element handle.
	 * 
	 * @param elementHandle
	 */
	FactoryElementHandle(DesignElementHandle elementHandle) {
		assert elementHandle != null;
		this.elementHandle = elementHandle;

	}

	/**
	 * Gets all the factory style handle. The list contains the shared style defined
	 * by 'style' property, the computed style that stores all the value inherited
	 * from all the ancestors of this element, all the selectors defined by this
	 * element and the style that stores the value computed from some related
	 * container. The list does not contain the private style that stores the local
	 * value set by this element itself and the cascading computed style from
	 * container.
	 * 
	 * @return all the factory style handles
	 */
	public List<StyleHandle> getAllFactoryStyles() {
		if (elementHandle == null)
			return Collections.emptyList();

		DesignElement element = elementHandle.getElement();
		if (!(element instanceof StyledElement))
			return Collections.emptyList();

		Module module = elementHandle.getModule();

		IElementDefn styleDefn = MetaDataDictionary.getInstance().getStyle();

		StyleElement inheritStyle = null;
		StyleElement relatedContainerStyle = null;
		List<StyleElement> selfSelectors = null;
		List<StyleElement> slotSelectors = null;
		PropertyValueInfo valueInfo = null;
		Object value = null;

		boolean readSelectos = false;

		List<IElementPropertyDefn> props = styleDefn.getProperties();
		for (int i = 0; i < props.size(); i++) {
			ElementPropertyDefn prop = (ElementPropertyDefn) props.get(i);

			// handle only style property, except name property and other
			// non-style properties
			if (!prop.isStyleProperty())
				continue;

			if (!readSelectos) {
				// get the self selectors
				valueInfo = PropertySearchStrategy.getInstance().createPropertyValueInfo();
				value = element.getPropertySearchStrategy().getPropertyFromSelfSelector(module, element, prop,
						valueInfo);
				selfSelectors = valueInfo.getSelectorStyles();

				// get slot selectors
				valueInfo = PropertySearchStrategy.getInstance().createPropertyValueInfo();
				value = element.getPropertySearchStrategy().getPropertyFromSlotSelector(module, element, prop,
						valueInfo);
				slotSelectors = valueInfo.getSelectorStyles();

				readSelectos = true;
			}

			// get value from the inherit ancestors
			value = element.getPropertySearchStrategy().getPropertyFromParent(module, element, prop);
			if (value != null) {
				if (inheritStyle == null)
					inheritStyle = new Style(INHERIT_STYLE_NAME);
				Object clonedValue = ModelUtil.copyValue(prop, value);
				inheritStyle.setProperty(prop, clonedValue);

				// for structure type value, must set up the structure context
				if (prop.getTypeCode() == IPropertyType.STRUCT_TYPE) {
					StructureContextUtil.setStructureContext(prop, clonedValue, element);
				}
			}

			// get value from the related container
			value = element.getPropertySearchStrategy().getPropertyRelatedToContainer(module, element, prop);
			if (value != null) {
				if (relatedContainerStyle == null)
					relatedContainerStyle = new Style(RELATED_CONTAINER_STYLE_NAME);
				Object clonedValue = ModelUtil.copyValue(prop, value);
				relatedContainerStyle.setProperty(prop, clonedValue);

				// for structure type value, must set up the structure context
				if (prop.getTypeCode() == IPropertyType.STRUCT_TYPE) {
					StructureContextUtil.setStructureContext(prop, clonedValue, element);
				}
			}
		}

		List<StyleHandle> styles = new ArrayList<StyleHandle>();

		// add the shared style
		SharedStyleHandle sharedStyle = elementHandle.getStyle();
		if (sharedStyle != null)
			styles.add(sharedStyle);

		// first add inherit style
		if (inheritStyle != null)
			styles.add((StyleHandle) inheritStyle.getHandle(module));

		// second, add self selectors
		if (selfSelectors != null) {
			for (int i = 0; i < selfSelectors.size(); i++) {
				styles.add((StyleHandle) selfSelectors.get(i).getHandle(module));
			}
		}

		// third, add slot selectors
		if (slotSelectors != null) {
			for (int i = 0; i < slotSelectors.size(); i++) {
				styles.add((StyleHandle) slotSelectors.get(i).getHandle(module));
			}
		}

		// last, add related container style
		if (relatedContainerStyle != null)
			styles.add((StyleHandle) relatedContainerStyle.getHandle(module));
		return styles;
	}

	/**
	 * Returns a handle for a top-level property for use in preparing the Factory
	 * data structures. This handle follows specialized rules:
	 * <p>
	 * <ul>
	 * <li>Optimized to get each property value only once.</li>
	 * <li>Indicates if the value is a style property.</li>
	 * <li>Indicates if a style property is set on the element's private style or a
	 * shared style.</li>
	 * <li>Performs property conversions as needed for the Factory context.</li>
	 * </ul>
	 * 
	 * @param propName the name of the property to get
	 * @return the factory property handle, or <code>null</code> if either 1) no
	 *         property exists with the given name or 2) the property is a style
	 *         property and is not set in a private style.
	 */

	public FactoryPropertyHandle getFactoryPropertyHandle(String propName) {
		return elementHandle.getFactoryPropertyHandle(propName);
	}
}
