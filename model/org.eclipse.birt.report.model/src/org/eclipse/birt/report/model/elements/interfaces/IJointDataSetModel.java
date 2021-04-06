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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for JointDataset element to store the constants on JointDataSet
 * element.
 */

public interface IJointDataSetModel {

	/**
	 * Name of the source data sets property which gives all the source datasets in
	 * this joint dataset.
	 */

	public static final String DATA_SETS_PROP = "dataSets"; //$NON-NLS-1$

	/**
	 * Name of the joint conditions property which gives all the join conditions in
	 * this joint dataset.
	 */

	public static final String JOIN_CONDITONS_PROP = "joinConditions"; //$NON-NLS-1$

}
