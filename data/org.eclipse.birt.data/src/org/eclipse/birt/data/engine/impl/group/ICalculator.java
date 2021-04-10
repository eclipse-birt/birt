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

package org.eclipse.birt.data.engine.impl.group;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Use ICalculator interface whenever you wish to calculator a value.
 */
public interface ICalculator {

	/**
	 * 
	 * @param value
	 * @return
	 */
	Object calculate(Object value) throws BirtException;
}
