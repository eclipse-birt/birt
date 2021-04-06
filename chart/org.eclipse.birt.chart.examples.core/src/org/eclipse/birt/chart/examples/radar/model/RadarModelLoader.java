/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.radar.model;

import org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage;
import org.eclipse.birt.chart.model.IExtChartModelLoader;
import org.eclipse.emf.ecore.EPackage;

/**
 * This class is responsible to load radar model.
 * 
 * @since 2.6
 */

public class RadarModelLoader implements IExtChartModelLoader {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.IExtChartModelLoader#getChartTypePackage()
	 */
	public EPackage getChartTypePackage() {
		return RadarTypePackage.eINSTANCE;
	}
}
