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

package org.eclipse.birt.report.model.api.scripts;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;

/**
 * 
 */

public interface IScriptableObjectClassInfo {

	/**
	 * Returns the class information for the given class name.
	 * 
	 * @param className the class name
	 * @return the class information
	 */

	IClassInfo getScriptableClass(String className);
}
