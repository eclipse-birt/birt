
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

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class HierarchyDefinition extends NamedObject implements IHierarchyDefinition {
	private List levels;
	private IDimensionDefinition dim;

	public HierarchyDefinition(IDimensionDefinition dim, String hierarchyName) {
		super(hierarchyName);
		this.levels = new ArrayList();
		this.dim = dim;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition#createLevel(
	 * java.lang.String)
	 */
	@Override
	public ILevelDefinition createLevel(String levelName) {
		ILevelDefinition level = new LevelDefiniton(this, levelName);
		this.levels.add(level);
		return level;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition#getDimension
	 * ()
	 */
	@Override
	public IDimensionDefinition getDimension() {
		return this.dim;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition#getLevels()
	 */
	@Override
	public List getLevels() {
		return this.levels;
	}

	/**
	 * Clone itself
	 */
	@Override
	public IHierarchyDefinition clone() {
		HierarchyDefinition cloned = new HierarchyDefinition(this.dim.clone(), this.getName());
		cloneFields(cloned);

		return cloned;
	}

	/*
	 * Clone fields. Separate this method for extension classes.
	 */
	protected void cloneFields(HierarchyDefinition cloned) {
		cloned.levels.addAll(this.levels);
	}

}
