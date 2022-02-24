
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.api.query;

import org.eclipse.birt.data.engine.api.IFilterDefinition;

/**
 * 
 */

public interface ICubeFilterDefinition extends IFilterDefinition {
	/**
	 * Return the targeting level that this sort/filter definition will act against.
	 */
	public ILevelDefinition getTargetLevel();

	/**
	 * Return the Axis qualifier levels.
	 * 
	 * @return
	 */
	public ILevelDefinition[] getAxisQualifierLevels();

	/**
	 * Return the Axis qualifier values.
	 * 
	 * @return
	 */
	public Object[] getAxisQualifierValues();
}
