
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
package org.eclipse.birt.data.engine.cache;

import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;

/**
 *
 */

public class SimpleCachedObject implements IStructure {
	private Object[] fields;

	public SimpleCachedObject(Object[] fields) {
		this.fields = fields;
	}

	@Override
	public Object[] getFieldValues() {
		return fields;
	}

	public static IStructureCreator getCreator() {
		return new SimpleCachedObjectCreator();
	}
}

class SimpleCachedObjectCreator implements IStructureCreator {

	@Override
	public IStructure createInstance(Object[] fields) {
		return new SimpleCachedObject(fields);
	}
}
