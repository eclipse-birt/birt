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
