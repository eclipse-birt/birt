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

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Represents an element or a structure that can be referenced using an element
 * reference or a property of name type. This object maintains a cached set of
 * back-references to the "clients" so that changes can be automatically
 * propagated.
 * 
 */

public interface IReferencable {

	/**
	 * Adds a client. Should be called only from
	 * {@link DesignElement#setProperty(ElementPropertyDefn, Object )}.
	 * 
	 * @param client   The client to add.
	 * @param propName the property name.
	 */

	public void addClient(DesignElement client, String propName);

	/**
	 * Drops a client. Should be called only from
	 * {@link DesignElement#setProperty(ElementPropertyDefn, Object )}.
	 * 
	 * @param client The client to drop.
	 */

	public void dropClient(DesignElement client);

	/**
	 * Returns the list of clients for this element.
	 * 
	 * @return The list of clients.
	 */

	public List<BackRef> getClientList();

	/**
	 * Checks if this referencable object is referenced by others.
	 * 
	 * @return true if it has client, otherwise return false.
	 * 
	 */

	public boolean hasReferences();
}