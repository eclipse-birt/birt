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
 * An adapter class for IActionEvaluator
 */
public class ActionEvaluatorAdapter implements IActionEvaluator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.factory.IActionEvaluator#getActionExpressions(org.
	 * eclipse.birt.chart.model.data.Action)
	 */
	public String[] getActionExpressions(Action action, StructureSource source) {
		return null;
	}
}
