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

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.data.Action;

/**
 * An adapter class for IActionRenderer
 */
public class ActionRendererAdapter implements IActionRenderer {

	@Override
	public void processAction(Action action, StructureSource source, RunTimeContext rtc) {
		// To override.
	}

	@Override
	public void processAction(Action action, StructureSource source) {
		this.processAction(action, source, new RunTimeContext());
	}

}
