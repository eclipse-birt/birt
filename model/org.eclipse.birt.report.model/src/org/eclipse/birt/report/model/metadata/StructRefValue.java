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

import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Represents a representation to a structure. This class is the reference
 * property value. It can represent either a <em>resolved</em> or
 * <em>unresolved</em> value. A resolved value is one in which we've used a name
 * to look up the actual structure. An unresolved reference is one that has a
 * name, but has not been resolved to a structure.
 * <p>
 * The class holds either a name or a pointer to the target structure, never
 * both. By dropping the name for resolved elements, we avoid the need to fix up
 * references when the name of target structure changes.
 * <p>
 * If a structure can be the target of a reference, then that structure contains
 * a "back pointer" list of the references. This allows the system to perform
 * semantic checks, to clean up references to deleted structures, etc.
 * <p>
 * The structure reference is used in only one way. The use is to record the
 * value of a structure reference property (<code>StructRefPropertyType</code>).
 * In this case, the target must be derived from
 * <code>ReferencableStructure</code> so that the referenced class can cache a
 * back-pointer to the referencing element.
 * 
 */

public class StructRefValue extends ReferenceValue {

	/**
	 * Constructor of an unresolved reference.
	 * 
	 * @param namespace the library name space
	 * @param theName   the unresolved name
	 */

	public StructRefValue(String namespace, String theName) {
		super(namespace, theName);
	}

	/**
	 * Constructor of a resolved reference.
	 * 
	 * @param namespace the library name space
	 * @param structure the resolved structure
	 */

	public StructRefValue(String namespace, Structure structure) {
		super(namespace, structure);
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
			return ((Structure) resolved).getReferencableProperty();
		assert false;
		return null;
	}

	/**
	 * Returns the referenced structure, if the structure is resolved.
	 * 
	 * @return the referenced structure, or null if this reference is not set, or is
	 *         unresolved
	 */

	public Structure getStructure() {
		return (Structure) resolved;
	}

	/**
	 * Returns the target structure as a referenceable structure. This form is used
	 * when caching references.
	 * 
	 * @return the target structure as a referencable structure
	 */

	public ReferencableStructure getTargetStructure() {
		return (ReferencableStructure) resolved;
	}

	/**
	 * Sets the resolved structure.
	 * 
	 * @param structure the resolved structure
	 */

	public void resolve(Object structure) {
		assert structure instanceof Structure;
		name = null;
		resolved = structure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object copy() {
		return new StructRefValue(getLibraryNamespace(), getName());
	}

}
