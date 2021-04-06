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
 * The interface for cascading parameter group element to store the constants.
 */

public interface IIntenalCascadingParameterGroupModel extends IParameterGroupModel {

	/**
	 * Name of the data set property. This references a data set within the report.
	 * Parameters grouped in a cascading parameter group share the same data set.
	 */

	public static final String DATA_SET_PROP = "dataSet"; //$NON-NLS-1$

	/**
	 * Name of the data set mode property. The group can be in single data set or
	 * multiple data set.
	 */

	public static final String DATA_SET_MODE_PROP = "dataSetMode"; //$NON-NLS-1$

}
