/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.swt.graphics.Image;

/**
 * @author Actuate Corporation
 */
public interface IChartSubType {

	public String getName();

	public Image getImage();

	public String getDescription();

	/**
	 * Returns the display name of the chart subType.
	 *
	 * @return Chart subType display name.
	 * @since Version 2.1
	 */
	public String getDisplayName();
}