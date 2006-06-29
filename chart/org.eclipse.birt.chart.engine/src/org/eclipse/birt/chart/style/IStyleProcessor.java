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

package org.eclipse.birt.chart.style;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.StyledComponent;

/**
 * This interface allows access/manipulation to styles for granular chart
 * components.
 */
public interface IStyleProcessor
{

	/**
	 * Returns the style as per given component name.
	 * 
	 * @param name
	 * @return
	 */
	IStyle getStyle( Chart model, StyledComponent name );
}
