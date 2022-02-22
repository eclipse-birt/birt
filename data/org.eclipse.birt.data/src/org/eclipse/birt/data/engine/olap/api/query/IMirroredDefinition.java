/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

/**
 * This interface is to define the mirror on edge definition
 *
 */
public interface IMirroredDefinition {

	/**
	 * get the mirror start level definition
	 *
	 * @return
	 */
	ILevelDefinition getMirrorStartingLevel();

	/**
	 * whether to break the hierarchy when show the empty row, the default value is
	 * 'false'
	 *
	 * @return
	 */
	boolean isBreakHierarchy();

	/**
	 * Clone itself.
	 */
	IMirroredDefinition clone();
}
