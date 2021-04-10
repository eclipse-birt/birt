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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;

/**
 * Represents a representation to an element. This class is the reference
 * property value. It can represent either a <em>resolved</em> or
 * <em>unresolved</em> value. A resolved value is one in which we've used a name
 * to look up the actual element. An unresolved reference is one that has a
 * name, but has not been resolved to an element.
 * <p>
 * The class holds either a name or a pointer to the target element, never both.
 * By dropping the name for resolved elements, we avoid the need to fix up
 * references when the name of target element changes.
 * <p>
 * If an element can be the target of a reference, then that element contains a
 * "back pointer" list of the references. This allows the system to perform
 * semantic checks, to clean up references to deleted elements, etc.
 * <p>
 * The element reference is used in two key ways. First, it is used to record an
 * "extends" reference of a derived element to its parent element in the
 * inheritance hierarchy. In this case, the reference is to a generic
 * <code>DesignElement</code>. The second use is to record the value of an
 * element reference property (<code>ElementRefPropertyType</code>). In this
 * case, the target must be derived from <code>ReferenceableElement</code> so
 * that the referenced class can cache a back-pointer to the referencing
 * element.
 * 
 */

public class ElementRefValue extends ReferenceValue {

	/**
	 * Constructor of an unresolved reference.
	 * 
	 * @param namespace the namespace to indicate which included library this value
	 *                  refers to
	 * @param theName   the unresolved name
	 */

	public ElementRefValue(String namespace, String theName) {
		super(namespace, theName);
	}

	/**
	 * Constructor of a resolved reference.
	 * 
	 * @param namespace the namespace to indicate which included library this value
	 *                  refers to
	 * @param element   the resolved element
	 */

	public ElementRefValue(String namespace, DesignElement element) {
		super(namespace, element);
	}

	/**
	 * Gets the reference name. The name is either the unresolved name, or the name
	 * of the resolved element.
	 * 
	 * @return the name of the referenced element, or null if this reference is not
	 *         set
	 */

	public String getName() {
		if (name != null)
			return name;
		if (resolved != null)
			return ((DesignElement) resolved).getFullName();
		assert false;
		return null;
	}

	/**
	 * Sets the resolved element.
	 * 
	 * @param element the resolved element.
	 */
	public void resolve(Object element) {

		assert element instanceof DesignElement;
		name = null;
		resolved = element;
	}

	/**
	 * Returns the referenced element, if the element is resolved.
	 * 
	 * @return the referenced element, or null if this reference is not set, or is
	 *         unresolved
	 */

	public DesignElement getElement() {
		return (DesignElement) resolved;
	}

	/**
	 * Returns the target element as a referenceable element. This form is used when
	 * caching references.
	 * 
	 * @return the target element as a referencable element
	 */

	public IReferencableElement getTargetElement() {
		return (IReferencableElement) resolved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	public boolean equals(Object obj) {
		if (!(obj instanceof ElementRefValue))
			return false;

		ElementRefValue value = (ElementRefValue) obj;
		if (isResolved() != value.isResolved())
			return false;

		// both in resolved status.

		if (value.isResolved())
			return getElement().equals(value.getElement());

		// both in unresolved status

		if (!getName().equals(value.getName()))
			return false;

		String myNameSpace = getLibraryNamespace();
		String objNameSpace = value.getLibraryNamespace();

		if (myNameSpace == null && objNameSpace == null)
			return true;

		if (myNameSpace != null && myNameSpace.equals(objNameSpace))
			return true;

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object copy() {
		return new ElementRefValue(getLibraryNamespace(), getName());
	}

}
