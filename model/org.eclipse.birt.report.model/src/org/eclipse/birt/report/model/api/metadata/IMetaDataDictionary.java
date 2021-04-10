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

package org.eclipse.birt.report.model.api.metadata;

import java.util.List;

import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * Global, shared dictionary of design meta data. Meta-data describes each
 * design element and its properties. The information is shared because all
 * designs share the same BIRT-provided set of design elements. See the
 * {@link IElementDefn}class for more detailed information.
 * 
 * <h2>Meta-data Information</h2> The application must first populate the
 * elements from a meta-data XML file using a parser. The meta-data defined here
 * includes:
 * 
 * <p>
 * <dl>
 * <dt><strong>Property Types </strong></dt>
 * <dd>The set of data types supported for properties. BIRT supports a rich
 * variety of property types that include the basics such as strings and
 * numbers, as well as specialized types such as dimensions, points and colors.
 * See the {@link PropertyType PropertyType}class.</dd>
 * 
 * <dt><strong>Element Definitions </strong></dt>
 * <dd>Describes the BIRT-defined elements. The element definition includes the
 * list of properties defined on that type, and optional properties "inherited"
 * from the style. See the {@link IElementDefn}class.</dd>
 * 
 * <dt><strong>Standard Styles </strong></dt>
 * <dd>BIRT defines a set of standard styles. The set of styles goes along with
 * the set of elements. For example, a list header has a standard style as does
 * a list footer.</dd>
 * 
 * <dt><strong>Class Definitions </strong></dt>
 * <dd>Describes the object types that are defined by JavaScript and BIRT. The
 * class definition includes constructor, members and methods. See the
 * {@link IClassInfo}class.</dd>
 * </dl>
 * <p>
 */

public interface IMetaDataDictionary {

	/**
	 * Name of the class for 'Total'.
	 */
	String TOTAL_CLASS_NAME = "Total"; //$NON-NLS-1$

	/**
	 * Name of the class for 'Finance'.
	 */
	String FINANCE_CLASS_NAME = "Finance"; //$NON-NLS-1$

	/**
	 * Finds the element definition by its internal name.
	 * 
	 * @param name The internal element definition name.
	 * @return The element definition, or null if the name was not found in the
	 *         dictionary.
	 */

	public IElementDefn getElement(String name);

	/**
	 * Gets the metadata for a property type.
	 * 
	 * @param type numeric type code
	 * @return property type definition
	 */

	public PropertyType getPropertyType(int type);

	/**
	 * Gets the metadata for a property type given the type's XML name.
	 * 
	 * @param xmlName XML name for the property type
	 * 
	 * @return property type definition
	 */

	public PropertyType getPropertyType(String xmlName);

	/**
	 * Returns the meta-data element that defines the style element.
	 * 
	 * @return the definition of the style element
	 */

	public IElementDefn getStyle();

	/**
	 * Enables the use of element IDs.
	 * 
	 * @deprecated
	 */

	public void enableElementID();

	/**
	 * Reports whether element IDs are in use.
	 * 
	 * @return True if new elements should use element IDs.
	 */

	public boolean useID();

	/**
	 * Determines if the meta data dictionary is empty (uninitialized).
	 * 
	 * @return true if empty, false if it contains content
	 */

	public boolean isEmpty();

	/**
	 * Finds a choice set by name.
	 * 
	 * @param choiceSetName the name of the choice set
	 * @return the choice set, or null if the choice set was not found
	 */

	public IChoiceSet getChoiceSet(String choiceSetName);

	/**
	 * Finds a structure definition by name.
	 * 
	 * @param name the structure name
	 * @return the structure, or null if the structure is not found
	 */

	public IStructureDefn getStructure(String name);

	/**
	 * Returns the element list. Each one is the instance of
	 * <code>IElementDefn</code>.
	 * 
	 * @return the element list.
	 */

	public List<IElementDefn> getElements();

	/**
	 * Returns the structure list. Each one is the instance of
	 * <code>IStructureDefn</code>.
	 * 
	 * @return the structure list.
	 */

	public List<IStructureDefn> getStructures();

	/**
	 * Gets the predefined style list. Each one is the instance of
	 * <code>IPredefinedStyle</code>;
	 * 
	 * @return the predefined style list.
	 */

	public List<IPredefinedStyle> getPredefinedStyles();

	/**
	 * Returns the class list. Each one is the instance of <code>IClassInfo</code>.
	 * 
	 * @return the class list.
	 */

	public List<IClassInfo> getClasses();

	/**
	 * Returns the class definition given the class name.
	 * 
	 * @param name name of the class to get.
	 * @return the class definition if found.
	 */

	public IClassInfo getClass(String name);

	/**
	 * Returns the extension list. Each one is the instance of {@link IElementDefn}.
	 * 
	 * @return the extension definition list. Return empty list if no extension is
	 *         found.
	 */

	public List<IElementDefn> getExtensions();

	/**
	 * Returns the extension definition given the extension name.
	 * 
	 * @param name name of the extension to get
	 * @return the extension definition if found
	 */

	public IElementDefn getExtension(String name);

	/**
	 * Gets a list of rom-defined property types.
	 * 
	 * @return a list of rom-defined property types.
	 */

	public List<IPropertyType> getPropertyTypes();

	/**
	 * Returns the function list. Each one is the instance of
	 * <code>IMethodInfo</code>.
	 * 
	 * @return the method list.
	 */

	public List<IMethodInfo> getFunctions();

	/**
	 * 
	 * @param type
	 * @return
	 */
	public List<IPredefinedStyle> getPredefinedStyles(String type);

	/**
	 * Gets all the supported report item theme types.
	 * 
	 * @return
	 */
	public List<String> getReportItemThemeTypes();

	/**
	 * Finds the element definition with the specified report item theme type. If
	 * the element definition defines the identical theme type with the given value,
	 * then return it; otherwise return null if not found or report item theme is
	 * not valid.
	 * 
	 * @param themeType
	 * @return
	 */
	public IElementDefn findElementByThemeType(String themeType);

	/**
	 * Gets the default encryption id.
	 * 
	 * @return the ID of the default encryption helper
	 */
	public String getDefaultEncryptionHelperID();

	/**
	 * Gets all encryption id.
	 * 
	 * @return the list of all ID of the encryption helpers
	 */
	public List<String> getEncryptionHelperIDs();

}
