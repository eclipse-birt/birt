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

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * The element can be referred by other elements. For examples: table uses
 * style, data set uses data source, etc. The class implements this interface
 * must extend from <code>DesignElement</code>.
 * 
 */

public interface IReferencableElement {

	/**
	 * Adds a client. Should be called only from
	 * {@link DesignElement#setProperty(ElementPropertyDefn, Object )}.
	 * 
	 * @param client   The client to add.
	 * @param propName the property name.
	 */

	void addClient(DesignElement client, String propName);

	/**
	 * Adds a client. Should be called only from
	 * {@link DesignElement#setProperty(ElementPropertyDefn, Object )}.
	 * 
	 * @param struct   The client to add.
	 * @param propName the member name
	 * 
	 */

	void addClient(Structure struct, String propName);

	/**
	 * Drops a client. Should be called only from
	 * {@link DesignElement#setProperty(ElementPropertyDefn, Object )}.
	 * 
	 * @param client The client to drop.
	 */

	void dropClient(DesignElement client);

	/**
	 * Drops a client.
	 * 
	 * @param client   The client to drop.
	 * @param propName the property name
	 */

	void dropClient(DesignElement client, String propName);

	/**
	 * Drops a client.
	 * 
	 * @param struct   the structure
	 * @param propName the member name
	 */

	void dropClient(Structure struct, String propName);

	/**
	 * Returns the list of clients for this element.
	 * 
	 * @return The list of clients.
	 */

	List<BackRef> getClientList();

	/**
	 * Checks whether the element is referred by other elements.
	 * 
	 * @return <code>true</code> if the element is referred. Otherwise
	 *         <code>false</code>.
	 */

	boolean hasReferences();

	/**
	 * Updates the element reference which refers to the given referenceable
	 * element.
	 * 
	 */

	void updateClientReferences();

	/**
	 * Clears all clients.
	 */

	void clearClients();

}
