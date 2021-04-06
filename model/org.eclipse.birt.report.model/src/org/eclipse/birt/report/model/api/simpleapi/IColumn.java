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
