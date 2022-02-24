/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * Interface to define some constants for MultiView.
 */

public interface IMultiViewsModel {

	/**
	 * Name of the property that holds a list of extended elements.
	 */

	String VIEWS_PROP = "views"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates which view works.
	 */

	String INDEX_PROP = "index"; //$NON-NLS-1$
}
