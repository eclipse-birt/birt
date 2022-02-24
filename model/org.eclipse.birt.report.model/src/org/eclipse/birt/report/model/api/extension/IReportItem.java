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

package org.eclipse.birt.report.model.api.extension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;

/**
 * Represents an instance of a extended report element. There is a one-to-one
 * correspondence between the BIRT-defined report item and this IReportItem.
 * <p>
 * 
 * <pre>
 *                         BIRT Extended Item &lt;--&gt; ExtendedItem
 * </pre>
 * 
 * 
 * IReportItem is responsible for storing model data, and for serializing the
 * model state. They can optionally provide generic property access.
 */

public interface IReportItem {

	/**
	 * Serializes the model given the property name. The property is visible to BIRT
	 * and its type is
	 * {@link org.eclipse.birt.report.model.metadata.XMLPropertyType}. The
	 * serialized data must be Unicode text. XML is preferred, but any text format
	 * is legal. If the model is binary, then one solution is to serialize the model
	 * as Base64 encoded data.
	 * 
	 * @param propName the model property name to serialize
	 * @return a byte array that represents the serialized extended element model.
	 */

	public ByteArrayOutputStream serialize(String propName);

	/**
	 * Deserializes the model. The property is visible to BIRT and its type is
	 * {@link org.eclipse.birt.report.model.metadata.XMLPropertyType}. The data
	 * provided will be that created by the <code>serialize</code> method. A
	 * well-written extension will handle two exceptional cases. First, it will
	 * handle all previous versions of this same extension. Second, it will handle
	 * erroneous input, perhaps created when a human editied the saved state by
	 * hand.
	 * 
	 * @param propName the model property name to deserialize
	 * @param data     a byte array stream containing the serialized data
	 * @throws ExtendedElementException if the serialized model is invalid
	 */

	public void deserialize(String propName, ByteArrayInputStream data) throws ExtendedElementException;

	/**
	 * Gets the list of all the property definitions that the extended element model
	 * has.
	 * 
	 * @return the dynamic property list of the extended element model
	 */

	public IPropertyDefinition[] getPropertyDefinitions();

	/**
	 * Returns the value of a public property. Returns null if the property is not
	 * defined. If the <code>IReportItemFactory</code> class defines a property,
	 * then this method must accept that property name.
	 * <p>
	 * Property names can be separated with dots and can include array indexes. For
	 * example: barColors[1].pattern. This will occur if a property is defined as a
	 * list or structure.
	 * <p>
	 * If the report element support styles or inheritance, then a return value of
	 * <code>null</code> means that the value should be inherited. BIRT will use its
	 * normal inheritance mechanisms to locate the value. If the extended element
	 * does not support inheritance or styles, then this method must return the
	 * value of each property, including defaults if the value has not yet been set.
	 * 
	 * @param propName the name of the property to get
	 * @return the property value as one of the supported BIRT property types:
	 *         String, Integer, Double, BigDecimal, etc.
	 */

	public Object getProperty(String propName);

	/**
	 * Checks the value of a property prior to setting it. BIRT calls this operation
	 * before setting the value. See the <code>setProperty</code> method for details
	 * on the process for setting a property value.
	 * 
	 * @param propName the name of the property to check
	 * @param value    the new property value as one of the supported BIRT types
	 * @see #getProperty
	 * @throws ExtendedElementException if the property value is invalid or the
	 *                                  property name is invalid
	 */

	public void checkProperty(String propName, Object value) throws ExtendedElementException;

	/**
	 * Sets the value of a property. This operation is done in response to a
	 * BIRT-defined command. That is, this method call is the result of an
	 * application using the BIRT Model API to set a property. BIRT will have
	 * validated the property using the <code>checkProperty</code> method, and will
	 * have created a command to set the property.
	 * <p>
	 * If this element supports styles or inheritance, then the value can be
	 * <code>null</code>, which means to clear the property value so that it will
	 * inherit from the base element or the style.
	 * 
	 * @param propName the name of the property to set
	 * @param value    the new property value as one of the supported BIRT types
	 * @see #getProperty
	 * @see #checkProperty
	 */

	public void setProperty(String propName, Object value);

	/**
	 * Performs a semantic check on the report item. The extended element can use
	 * this to validate the values of properties, to ensure that a set of values are
	 * consistent, etc.
	 * 
	 * @return List List contains ExtendedElementException.
	 */

	public List<SemanticException> validate();

	/**
	 * Creates deep copy of the IReportItem and return the new element.
	 * 
	 * @return the deep copied IReportItem
	 */

	public IReportItem copy();

	/**
	 * Justifies whether the property list of the extended element is changed.
	 * 
	 * @return true if the property list of the extended element is changed,
	 *         otherwise false
	 */

	boolean refreshPropertyDefinition();

	/**
	 * Returns the method definition list of extension elements. Each object in the
	 * list is instance of {@link IElementPropertyDefn}.
	 * 
	 * @return the method definition list.
	 * 
	 * @deprecated by {@link #getMethods(String)}
	 */

	public IPropertyDefinition[] getMethods();

	/**
	 * Gets the script property definition of this report item.
	 * 
	 * @return the script property definition of this report item
	 */

	public IPropertyDefinition getScriptPropertyDefinition();

	/**
	 * Gets list of the predefined styles. Each one in the list can be one of the
	 * following instance:
	 * <p>
	 * 
	 * <ui>
	 * <li><code>String</code>
	 * <li><code>IStyleDeclaration</code> </ui>
	 * 
	 * @return
	 */
	public List<Object> getPredefinedStyles();

	/**
	 * Returns the interface for script operations. If the extension element want to
	 * provide multi row data function, need to extend simpleapi.IMultiRowItem and
	 * extension.MultiRowItem; If not, need to extend simpleapi.IReportItem and
	 * extension.SimpleRowItem.
	 * 
	 * @return the simple interface
	 */

	public org.eclipse.birt.report.model.api.simpleapi.IReportItem getSimpleElement();

	/**
	 * Returns the method info list of extension elements. Each object in the list
	 * is instance of {@link IMethodInfo}.
	 * 
	 * @param methodName the method name
	 * 
	 * @return the method definition list.
	 */

	public IMethodInfo[] getMethods(String methodName);

	/**
	 * 
	 * Sets the handle of this report item.
	 * 
	 * @param handle extended item handle
	 */
	public void setHandle(ExtendedItemHandle handle);

	/**
	 * Indicates whether the report item can be allowed to export to library.
	 * 
	 * @return <code>true</code> if the report item can be allowed to export to
	 *         library; <code>false</code> otherwise.
	 */
	public boolean canExport();

	/**
	 * Gets the iterator of all available bindings.
	 * 
	 * @return the iterator
	 */
	public Iterator availableBindings();

	public StyleHandle[] getReferencedStyle();

	public void updateStyleReference(Map<String, String> styleMap);

	/**
	 * Indicates whether the report item has fixed size.
	 * 
	 * @return
	 */
	public boolean hasFixedSize();

}
