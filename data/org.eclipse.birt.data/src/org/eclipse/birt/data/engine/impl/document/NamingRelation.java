/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl.document;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class NamingRelation {

	Map bookmarkMap;
	Map elementIdMap;

	/**
	 *
	 * @param bookmarkMap
	 * @param elementIdMap
	 */
	public NamingRelation(Map bookmarkMap, Map elementIdMap) {
		this.bookmarkMap = bookmarkMap;
		this.elementIdMap = elementIdMap;
	}

	/**
	 *
	 */
	public NamingRelation() {
		this(new HashMap(), new HashMap());
	}

	/**
	 * @return the bookmarkMap
	 */
	public Map getBookmarkMap() {
		return bookmarkMap;
	}

	/**
	 * @param bookmarkMap the bookmarkMap to set
	 */
	public void setBookmarkMap(Map bookmarkMap) {
		this.bookmarkMap = bookmarkMap;
	}

	/**
	 * @return the elementIdMap
	 */
	public Map getElementIdMap() {
		return elementIdMap;
	}

	/**
	 * @param elementIdMap the elementIdMap to set
	 */
	public void setElementIdMap(Map elementIdMap) {
		this.elementIdMap = elementIdMap;
	}

}
