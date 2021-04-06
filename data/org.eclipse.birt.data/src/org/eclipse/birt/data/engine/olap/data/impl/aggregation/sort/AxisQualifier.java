
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
