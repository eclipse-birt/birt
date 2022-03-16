
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort;

import java.util.logging.Logger;

/**
 *
 */
public class AxisQualifier {
	private int[] levelIndex;
	private Object[] value;
	private static Logger logger = Logger.getLogger(AxisQualifier.class.getName());

	//
	public AxisQualifier(int[] levelIndex, Object[] value) {
		Object[] params = { levelIndex, value };
		logger.entering(AxisQualifier.class.getName(), "AxisQualifier", params);
		this.levelIndex = levelIndex;
		this.value = value;
		logger.exiting(AxisQualifier.class.getName(), "AxisQualifier");
	}

	/**
	 *
	 * @return
	 */
	public int[] getLevelIndex() {
		return this.levelIndex;
	}

	/**
	 *
	 * @return
	 */
	public Object[] getLevelValue() {
		return this.value;
	}
}
