
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

	public Object[] getFieldValues() {
		return fields;
	}

	public static IStructureCreator getCreator() {
		return new SimpleCachedObjectCreator();
	}
}

class SimpleCachedObjectCreator implements IStructureCreator {

	public IStructure createInstance(Object[] fields) {
		return new SimpleCachedObject(fields);
	}
}