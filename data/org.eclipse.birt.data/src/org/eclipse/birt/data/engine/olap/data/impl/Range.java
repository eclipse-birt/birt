
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
package org.eclipse.birt.data.engine.olap.data.impl;

import java.util.logging.Logger;

/**
 * 
 */

public class Range {

	private static Logger logger = Logger.getLogger(Range.class.getName());

	private Object start;
	private Object end;

	/**
	 * 
	 * @param start
	 * @param end
	 */
	public Range(Object start, Object end) {
		Object[] params = { start, end };
		logger.entering(Range.class.getName(), "Range", params);
		this.start = start;
		this.end = end;
		logger.exiting(Range.class.getName(), "Range");
	}

	public Object getStart() {
		return start;
	}

	public void setStart(Object start) {
		this.start = start;
	}

	public Object getEnd() {
		return end;
	}

	public void setEnd(Object end) {
		this.end = end;
	}
}
