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

import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Abstract class to represent the reference type property value.
 */
public abstract class ReferenceValue {

	/**
	 * The delimiter between the namespace and the name.
	 */

	public static final String NAMESPACE_DELIMITER = "."; //$NON-NLS-1$

	/**
	 * Library namespace that indicats which library this reference is using
	 */

	String libraryNamespace;

	/**
	 * Unresolved name of the target element.
	 */

	String name;

	/**
	 * Resolved pointer to the target element.
	 */

	Object resolved;

	/**
	 * Constructor of an unresolved reference.
	 *
	 * @param namespace the namespace to indicate which included library this value
	 *                  refers to
	 * @param theName   the unresolved name
	 */
	public ReferenceValue(String namespace, String theName) {

		assert theName != null;
		name = theName;
		libraryNamespace = namespace;

	}

	/**
	 * Constructor of a resolved reference.
	 *
	 * @param namespace the namespace to indicate which included library this value
	 *                  refers to
	 * @param value     the resolved element or structure
	 */
	public ReferenceValue(String namespace, Object value) {

		assert value != null;
		resolved = value;
		libraryNamespace = namespace;
	}

	/**
	 * Gets the name of the reference value.
	 *
	 * @return the name of the reference value
	 */
	abstract public String getName();

	/**
	 *
	 * @param value
	 */
	abstract public void resolve(Object value);

	/**
	 * Sets the unresolved element or structure name.
	 *
	 * @param theName the unresolved element name
	 */
	public void unresolved(String theName) {
		resolved = null;
		name = theName;
	}

	/**
	 * Determines if this reference is resolved.
	 *
	 * @return true if this element is resolved, false if it is unset, or set to an
	 *         unresolved name
	 */
	public boolean isResolved() {

		assert !(name != null && resolved != null);
		return resolved != null;
	}

	/**
	 * Determines if this reference is set.
	 *
	 * @return true if the reference is set, false if not
	 */

	public boolean isSet() {
		return name != null || resolved != null;
	}

	/**
	 * Returns the library namespace.
	 *
	 * @return the library namespace
	 */

	public String getLibraryNamespace() {
		return libraryNamespace;
	}

	/**
	 * Returns the qualified reference of this reference value, which is made up
	 * with library namespace and element name. If no library namespace is
	 * available, only element name is returned.
	 * <p>
	 * For example,
	 * <ul>
	 * <li>The library namespace is "LibA", and element name is "style1".
	 * "LibA.style1" is retured.
	 * <li>If it has no library namespace,
	 * </ul>
	 *
	 * @return the qualified reference
	 */

	public String getQualifiedReference() {
		return StringUtil.buildQualifiedReference(getLibraryNamespace(), getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		if (!StringUtil.isBlank(getName())) {
			return getQualifiedReference();
		}

		return super.toString();
	}

	/**
	 * Sets the library name space for the reference.
	 *
	 * @param libraryNamespace The libraryNamespace to set.
	 */

	public void setLibraryNamespace(String libraryNamespace) {
		this.libraryNamespace = libraryNamespace;
	}

	/**
	 *
	 * @return the deep cloned reference value
	 */
	abstract public Object copy();

}
