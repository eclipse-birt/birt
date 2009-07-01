/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.model;

import org.eclipse.emf.ecore.EObject;

/**
 * Base interface of all EObjects which are used chart's model.
 */

public interface IChartObject extends EObject
{

	IChartObject copyInstance( );
}
