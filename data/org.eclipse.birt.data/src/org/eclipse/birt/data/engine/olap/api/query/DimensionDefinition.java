
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

public class DimensionDefinition extends NamedObject implements IDimensionDefinition {
	private List hierarchies;

	public DimensionDefinition(String name) {
		super(name);
		this.hierarchies = new ArrayList();
	}

	public IHierarchyDefinition createHierarchy(String name) {
		IHierarchyDefinition hier = new HierarchyDefinition(this, name);
		this.hierarchies.add(hier);
		return hier;
	}

	public List getHierarchy() {
		return this.hierarchies;
	}

	/**
	 * Clone itself
	 */
	public IDimensionDefinition clone() {
		DimensionDefinition cloned = new DimensionDefinition(this.getName());
		cloneFields(cloned);

		return cloned;
	}

	/*
	 * Clone fields. Separate this method for extension classes.
	 */
	protected void cloneFields(DimensionDefinition cloned) {
		cloned.hierarchies.addAll(this.hierarchies);
	}

}
