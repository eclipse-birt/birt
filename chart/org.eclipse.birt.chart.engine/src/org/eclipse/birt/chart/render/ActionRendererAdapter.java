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

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.data.Action;

/**
 * An adapter class for IActionRenderer
 */
public class ActionRendererAdapter implements IActionRenderer
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.IActionRenderer#processAction(org.eclipse.birt.chart.model.data.Action,
	 *      org.eclipse.birt.chart.event.StructureSource)
	 */
	public void processAction( Action action, StructureSource source,
			RunTimeContext rtc )
	{
		// Doing nothing.
	}

}
