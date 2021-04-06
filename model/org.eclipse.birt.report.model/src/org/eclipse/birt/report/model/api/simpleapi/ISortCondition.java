/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;

/**
 * Represents the design of an SortCondition in the scripting environment
 * 
 */

public interface ISortCondition {

	/**
	 * Returns sort direction.
	 * 
	 * @return sort direction.
	 */

	public String getDirection();

	/**
	 * Sets sort direction.
	 * <ul>
	 * <li>asc
	 * <li>desc
	 * </ul>
	 * 
	 * @param direction
	 * @throws SemanticException
	 */

	public void setDirection(String direction) throws SemanticException;

	/**
	 * Returns sort key
	 * 
	 * @return sort key
	 */

	public String getKey();

	/**
	 * Sets sort key.
	 * 
	 * @param key
	 * @throws SemanticException
	 */

	public void setKey(String key) throws SemanticException;

	/**
	 * Returns structure.
	 * 
	 * @return structure
	 */

	public IStructure getStructure();
}
