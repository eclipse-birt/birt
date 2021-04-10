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

package org.eclipse.birt.report.model.api.extension;

import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Represents the peer extension itself. Provides metadata about the extension.
 * It has two parts: the extension (<code>IReportItemFactory</code>) and an
 * instance of the extension (<code>IReportItem</code>). This class defines the
 * meta-data that is the same across all instances. The default constructor must
 * be provided because extension loader will instantiate the class with default
 * constructor.
 */

public interface IReportItemFactory {

	/**
	 * Returns whether this element supports BIRT report element inheritance.
	 * Elements that don't support inheritance cannot appear in a library or in the
	 * components area of the design file.
	 * 
	 * @return true if inheritance is supported, false if not
	 */

	// boolean canInherit( );
	/**
	 * Creates a new instance of the extended element given a handle to the BIRT
	 * report element that represents the extended element.
	 * 
	 * @param extendedItemHandle the BIRT report item that represents the extended
	 *                           element
	 * @return a new peer element
	 */

	IReportItem newReportItem(DesignElementHandle extendedItemHandle);

	/**
	 * Returns whether the peer element supports BIRT styles. If true, then all BIRT
	 * style properties are assumed available on the item, filtered according to the
	 * <code>getStyleProperties</code> method. If false, then none of the style
	 * properties are available, neither is the style name.
	 * 
	 * @return true if the element support styles, false otherwise
	 */

	// boolean hasStyle( );
	/**
	 * Returns the set of style properties that this element supports. The filter is
	 * optional. If null, BIRT assumes that the element supports all style
	 * properties. If the list is provided, BIRT assumes that the element supports
	 * only the style properties that appear in the list.
	 * 
	 * @return the list of style properties that this element supports
	 */

	// Collection getStyleFilter( );
	/**
	 * Extended element can define additional style properties that are added to all
	 * style elements. For example, a stoplight dashboard item may define a new
	 * property for the shades of red, yellow and green to be used in the stoplight.
	 * The list is optional. If the list is null, no new style properties are
	 * defined. The names in the list must be unique. Use the IPropertyDefinition
	 * interface to define the properties. Should return null if the element does
	 * not support styles.
	 * 
	 * @return a collection of custom style properties defined as
	 *         IPropertyDefinition objects
	 */

	// Collection getStyleProperties( );
	/**
	 * Element Extension that support styles can define a custom selector for this
	 * element. The Factory will automatically apply the BIRT style cascade rules to
	 * the element using a style of this name.
	 * 
	 * @return the internal name of the default style for this element
	 */

	// String styleSelector( );
	/**
	 * Returns the <code>IMessages</code>, which can get localized message for the
	 * given resource key and locale.
	 * 
	 * @return the I18N class implementing <code>IMessages</code>
	 */

	IMessages getMessages();

	/**
	 * Returns the list of default styles that defined for the extension element.
	 * 
	 * @param extensionName the extension name of the element
	 * @return default style list.
	 */
	IStyleDeclaration[] getFactoryStyles(String extensionName);
}