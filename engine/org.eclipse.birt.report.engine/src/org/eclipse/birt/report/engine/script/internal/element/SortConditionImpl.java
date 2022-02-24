/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.ISortCondition;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of Sort Condition
 * 
 */

public class SortConditionImpl implements ISortCondition {

	private org.eclipse.birt.report.model.api.simpleapi.ISortCondition sortConditionImpl;

	/**
	 * Constructor
	 * 
	 * @param sortHandle
	 */

	public SortConditionImpl() {
		sortConditionImpl = SimpleElementFactory.getInstance().createSortCondition();
	}

	/**
	 * Constructor
	 * 
	 * @param sortHandle
	 */

	public SortConditionImpl(SortKeyHandle sortHandle) {
		sortConditionImpl = SimpleElementFactory.getInstance().createSortCondition(sortHandle);
	}

	/**
	 * Constructor
	 * 
	 * @param sort
	 */
	public SortConditionImpl(SortKey sort) {
		sortConditionImpl = SimpleElementFactory.getInstance().createSortCondition(sort);
	}

	/**
	 * Constructor
	 * 
	 * @param columnHandle
	 */

	public SortConditionImpl(org.eclipse.birt.report.model.api.simpleapi.ISortCondition condition) {
		sortConditionImpl = condition;
	}

	public String getDirection() {
		return sortConditionImpl.getDirection();
	}

	public String getKey() {
		return sortConditionImpl.getKey();
	}

	public void setDirection(String direction) throws ScriptException {
		try {
			sortConditionImpl.setDirection(direction);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	public void setKey(String key) throws ScriptException {
		// key is required
		try {
			sortConditionImpl.setKey(key);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	public IStructure getStructure() {
		return sortConditionImpl.getStructure();
	}

}
