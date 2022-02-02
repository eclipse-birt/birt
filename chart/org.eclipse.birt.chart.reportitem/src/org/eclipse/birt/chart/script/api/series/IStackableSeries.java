/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
