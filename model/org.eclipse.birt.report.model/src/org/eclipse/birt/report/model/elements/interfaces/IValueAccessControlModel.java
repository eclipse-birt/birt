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
 * Lists all constants that an value access control element may use.
 * 
 */

public interface IValueAccessControlModel extends IAccessControlModel {

	/**
	 * Name of the member which defines values.
	 */

	String VALUES_PROP = "values"; //$NON-NLS-1$
}
