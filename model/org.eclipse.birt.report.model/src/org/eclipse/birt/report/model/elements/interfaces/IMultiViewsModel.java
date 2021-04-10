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
