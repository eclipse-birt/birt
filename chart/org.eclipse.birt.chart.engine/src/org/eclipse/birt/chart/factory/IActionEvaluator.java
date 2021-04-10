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
