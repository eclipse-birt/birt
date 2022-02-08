/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.swt.graphics.Image;

/**
 * @author Actuate Corporation
 */
public class DefaultChartSubTypeImpl implements IChartSubType {

	private String sTypeName = null;

	private String sDescription = null;

	private Image imgType = null;

	private String sTypeDisplayName = null;

	public DefaultChartSubTypeImpl(String sTypeName, Image imgType, String sDescription) {
		this.sTypeName = sTypeName;
		this.sDescription = sDescription;
		this.imgType = imgType;
	}

	public DefaultChartSubTypeImpl(String sTypeName, Image imgType, String sDescription, String sTypeDisplayName) {
		this.sTypeName = sTypeName;
		this.sDescription = sDescription;
		this.imgType = imgType;
		this.sTypeDisplayName = sTypeDisplayName;
	}

	public DefaultChartSubTypeImpl(String sTypeName, String sDescription, Image imgType) {
		this.sTypeName = sTypeName;
		this.sDescription = sDescription;
		this.imgType = imgType;
	}

	public void setDescription(String sDescription) {
		this.sDescription = sDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartSubType#getName()
	 */
	public String getName() {
		return sTypeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartSubType#getImage()
	 */
	public Image getImage() {
		return imgType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartSubType#getDescription()
	 */
	public String getDescription() {
		return sDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType#getDisplayName()
	 */
	public String getDisplayName() {
		return sTypeDisplayName;
	}

}
