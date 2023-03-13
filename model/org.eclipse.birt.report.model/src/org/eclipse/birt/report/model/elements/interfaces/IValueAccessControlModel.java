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
 * Lists all constants that an value access control element may use.
 *
 */

public interface IValueAccessControlModel extends IAccessControlModel {

	/**
	 * Name of the member which defines values.
	 */

	String VALUES_PROP = "values"; //$NON-NLS-1$
}
