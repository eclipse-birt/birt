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
