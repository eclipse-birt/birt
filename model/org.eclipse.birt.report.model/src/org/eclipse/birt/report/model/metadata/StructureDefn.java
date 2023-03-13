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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;

/**
 * Definition of a property structure: an object that is "managed" by the model
 * to allow generic member access and undo/redo support for updates.
 *
 */

public class StructureDefn extends ObjectDefn implements IStructureDefn {
	private String javaClass;

	/**
	 * Constructs a struct definition given its name.
	 *
	 * @param theName the structure name
	 */

	public StructureDefn(String theName) {
		super(theName);
	}

	/**
	 * Default constructor.
	 *
	 */
	protected StructureDefn() {

	}

	/**
	 * Gets a structure member by name.
	 *
	 * @param name the name of the member to fine
	 * @return the member definition, or null if the member was not found
	 */

	@Override
	public IPropertyDefn getMember(String name) {
		return findProperty(name);
	}

	/**
	 * Build the structure definition.
	 *
	 * @throws MetaDataException if exception occurs during the build process.
	 */

	protected void build() throws MetaDataException {
		buildDefn();
		Iterator<IPropertyDefn> iter = properties.values().iterator();
		while (iter.hasNext()) {
			PropertyDefn prop = (PropertyDefn) iter.next();
			prop.build();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.ObjectDefn#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		// if it is a structure definition for extension
		// then there is no name, displayname and .. for it

		if (displayNameKey == null) {
			return null;
		}
		return super.getDisplayName();

	}

	/**
	 * Gets the java class of this element.
	 *
	 * @return The java class of this element.
	 */

	public String getJavaClass() {
		return javaClass;
	}

	/**
	 * Sets the java class to construct this element
	 *
	 * @param clazz class name
	 */
	public void setJavaClass(String clazz) {
		this.javaClass = clazz;
	}

}
