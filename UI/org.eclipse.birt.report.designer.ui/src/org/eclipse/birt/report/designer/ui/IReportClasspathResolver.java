/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui;

/**
 * IReportClasspathProvider
 */
public interface IReportClasspathResolver {

	/**
	 * Resolves the class path based on settings on given adaptable object. If no
	 * adaptable object is provided, it only populates the class path based on
	 * global settings.
	 *
	 * @param adaptable The object that used to identify the setting scope.
	 * @return
	 */
	String[] resolveClasspath(Object adaptable);
}
