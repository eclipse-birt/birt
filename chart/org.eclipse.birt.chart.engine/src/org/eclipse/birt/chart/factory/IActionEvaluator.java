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

package org.eclipse.birt.chart.factory;

import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.model.data.Action;

/**
 * This interface defines the capability to manipulate action based expressions.
 */
public interface IActionEvaluator {

	/**
	 * Returns the expressions contained in the action. Could be null if not
	 * containing any expression.
	 */
	String[] getActionExpressions(Action action, StructureSource source);
}
