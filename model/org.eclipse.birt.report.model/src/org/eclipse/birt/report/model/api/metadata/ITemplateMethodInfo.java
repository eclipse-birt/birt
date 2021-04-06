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

package org.eclipse.birt.report.model.api.metadata;

/**
 * Represents the method information that can provide code template.
 */

public interface ITemplateMethodInfo extends IMethodInfo {

	/**
	 * Returns the template code snippet for the method.
	 * 
	 * @return the code template in string
	 */

	public String getCodeTemplate();
}
