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

package org.eclipse.birt.chart.script.internal.scale;

import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.script.api.scale.ILogarithmicScale;

/**
 * 
 */

public class LogarithmicScaleImpl extends LinearScaleImpl implements ILogarithmicScale {

	protected LogarithmicScaleImpl(Axis axis) {
		super(axis);
	}

}
