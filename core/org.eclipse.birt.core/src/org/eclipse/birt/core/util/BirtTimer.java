/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A timer class used to measure time taken for a specific operation.
 */
public class BirtTimer {
	protected long startTime;
	protected long endTime;

	public BirtTimer() {

	}

	/**
	 * start timer
	 */
	public void start() {
		startTime = System.currentTimeMillis();
		endTime = startTime;
	}

	/**
	 * stops timer
	 */
	public void stop() {
		endTime = System.currentTimeMillis();
	}

	/**
	 * @return the time difference between timer start and timer stop
	 */
	public int delta() {
		return (int) (endTime - startTime);
	}

	/**
	 * restsrt timer
	 */
	public void restart() {
		start();
	}

	/**
	 * writes "{0} takes {1} Milliseconds." to log
	 *
	 * @param logger        a Java logger object
	 * @param level         log level
	 * @param operationName the operation name
	 */
	public void logTimeTaken(Logger logger, Level level, String operationName) {
		if (logger.isLoggable(level)) {
			logger.log(level, "{0} takes {1} Milliseconds.", // $NON-NLS-1$
					new String[] { operationName, Integer.toString(delta()) });
		}
	}

	/**
	 * writes "{0} takes {1} Milliseconds." to log
	 *
	 * @param logger        a Java logger object
	 * @param level         log level
	 * @param id            task identifier
	 * @param operationName the operation name
	 */
	public void logTimeTaken(Logger logger, Level level, String id, String operationName) {
		if (logger.isLoggable(level)) {
			logger.log(level, "{0}: {1} takes {2} Milliseconds.", // $NON-NLS-1$
					new String[] { id, operationName, Integer.toString(delta()) });
		}
	}
}
