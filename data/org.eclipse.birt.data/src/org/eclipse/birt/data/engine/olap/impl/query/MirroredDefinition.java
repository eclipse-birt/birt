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

package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition;

/**
 * Implementation of the IMirroredDefinition
 *
 */
public class MirroredDefinition implements IMirroredDefinition {

	private ILevelDefinition level;
	private boolean breakHierarchy;

	public MirroredDefinition(ILevelDefinition level, boolean breakHierarchy) {
		this.level = level;
		this.breakHierarchy = breakHierarchy;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition#
	 * getMirrorStartingLevel()
	 */
	@Override
	public ILevelDefinition getMirrorStartingLevel() {
		return this.level;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition#
	 * isBreakHierarchy()
	 */
	@Override
	public boolean isBreakHierarchy() {
		return this.breakHierarchy;
	}

	/**
	 * Clone itself.
	 */
	@Override
	public IMirroredDefinition clone() {
		MirroredDefinition cloned = new MirroredDefinition(this.level != null ? this.level.clone() : null,
				this.breakHierarchy);

		return cloned;
	}

}
