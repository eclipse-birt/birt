/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.api.series;

import org.eclipse.birt.chart.script.api.component.IValueSeries;

/**
 * Represents an abstract series which can be stacked in the scripting
 * environment
 */

public interface IStackableSeries extends IValueSeries {

	boolean isStacked();

	void setStacked(boolean stacked);
}
