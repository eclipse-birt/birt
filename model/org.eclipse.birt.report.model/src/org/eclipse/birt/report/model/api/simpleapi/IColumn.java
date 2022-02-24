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

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Script wrapper of ColumnHandle.
 * 
 */

public interface IColumn extends IDesignElement {

	/**
	 * Removes all hide rules that matches formatType.
	 * 
	 * @param rule
	 * @exception SemanticException
	 */

	void removeHideRule(IHideRule rule) throws SemanticException;

	/**
	 * Removes all hide rules
	 * 
	 * @throws SemanticException
	 */

	void removeHideRules() throws SemanticException;

	/**
	 * Returns array of hide rule expression
	 * 
	 * @return array of hide rule expression
	 */

	IHideRule[] getHideRules();

	/**
	 * Add HideRule
	 * 
	 * @param rule
	 * @throws SemanticException
	 */

	void addHideRule(IHideRule rule) throws SemanticException;
}
