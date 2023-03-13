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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.core.IPropertySet;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Interface for objects that appear in a property list. Provides methods for
 * generically accessing or updating object members, and provides a meta-data
 * definition for the object. This interface allows an object to participate in
 * the generic property type, command and related mechanisms.
 *
 */

public interface IStructure extends IPropertySet {

	/**
	 * Returns the name of the structure definition. The name is the one used to
	 * define the structure in the meta-data dictionary.
	 *
	 * @return the internal name of the structure a defined in the meta-data
	 *         dictionary.
	 */

	String getStructName();

	/**
	 * Creates a deep copy of this structure.
	 *
	 * @return a copy of this structure.
	 */

	IStructure copy();

	/**
	 * Returns the structure definition from the meta-data dictionary.
	 *
	 * @return the structure definition
	 */

	IStructureDefn getDefn();

	/**
	 * Gets the locale value of a property.
	 *
	 * @param module   the module
	 *
	 * @param propDefn definition of the property to get
	 * @return value of the item as an object, or null if the item is not set
	 *         locally or is not found.
	 *
	 * @deprecated by {@link #getProperty(Module, String)}
	 */

	@Deprecated
	Object getLocalProperty(Module module, PropertyDefn propDefn);

	/**
	 * Gets the locale value of a property.
	 *
	 * @param module   the module
	 *
	 * @param propName the name of the property definition
	 * @return value of the item as an object, or null if the item is not set
	 *         locally or is not found.
	 *
	 */

	Object getProperty(Module module, String propName);

	/**
	 * Justifies whether the structure can be referred by other design elements.
	 *
	 * @return true if the structure is referencable, otherwise false
	 */

	boolean isReferencable();

	/**
	 * Justifies whether the structure is generated in design time or not.
	 *
	 * @return <true> if the structure is generated in design time, otherwise return
	 *         <false>.
	 */
	boolean isDesignTime();
}
