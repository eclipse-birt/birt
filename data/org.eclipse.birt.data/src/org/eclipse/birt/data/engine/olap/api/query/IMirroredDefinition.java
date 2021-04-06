/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public ILevelDefinition getMirrorStartingLevel();

	/**
	 * whether to break the hierarchy when show the empty row, the default value is
	 * 'false'
	 * 
	 * @return
	 */
	public boolean isBreakHierarchy();

	/**
	 * Clone itself.
	 */
	public IMirroredDefinition clone();
}
