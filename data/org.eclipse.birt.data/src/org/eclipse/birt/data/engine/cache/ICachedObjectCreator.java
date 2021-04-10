
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.cache;

/**
 * A interface defined a method which is used to create ICachedObject.
 */

public interface ICachedObjectCreator {
	/**
	 * 
	 * @param fields
	 * @return
	 */
	public ICachedObject createInstance(Object[] fields);
}
