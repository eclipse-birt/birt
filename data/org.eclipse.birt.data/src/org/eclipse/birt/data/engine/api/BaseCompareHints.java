/**************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
 *  
 **************************************************************************/
package org.eclipse.birt.data.engine.api;

import java.util.Comparator;

/**
 * This class is used to indicate the hints when doing the comparison.
 * 
 * @since 4.8
 */
public class BaseCompareHints {
	private Comparator comparator;
	private String nullStringType;

	public BaseCompareHints(Comparator comparator, String nullStringType) {
		this.comparator = comparator;
		this.nullStringType = nullStringType;
	}

	/**
	 * 
	 * @return
	 */
	public Comparator getComparator() {
		return this.comparator;
	}

	/**
	 * 
	 * @return
	 */
	public String getNullType() {
		return this.nullStringType;
	}
}
