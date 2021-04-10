/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
