
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.script.functionservice;

/**
 * This is an inner interface that defines common properties for APIs that needs
 * UI awareness.
 */

interface IDescribable {
	/**
	 * Get the description.
	 * 
	 * @return
	 */
	public String getDescription();

}
