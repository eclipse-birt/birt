/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.mediator;

import java.util.Map;

/**
 * IMediatorState
 */
public interface IMediatorState {

	/**
	 * Gets the type.
	 */
	String getType();

	/**
	 * Gets the data object.
	 */
	Object getData();

	/**
	 * Gets the source object.
	 */
	Object getSource();

	/**
	 * Returns the extra data.
	 */
	Map<?, ?> getExtras();
}
