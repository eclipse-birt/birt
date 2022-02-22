/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.build.ant;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

public class TaskLogger extends Logger {

	Task task;

	protected TaskLogger(Task task) {
		super("", null);
		this.task = task;
	}

	protected int getMessageLevel(LogRecord record) {
		Level level = record.getLevel();
		if (level.equals(Level.SEVERE)) {
			return Project.MSG_ERR;
		}
		if (level.equals(Level.WARNING)) {
			return Project.MSG_WARN;
		}
		if (level.equals(Level.INFO)) {
			return Project.MSG_INFO;
		}
		return Project.MSG_VERBOSE;
	}

	protected String getMessage(LogRecord record) {
		String format = record.getMessage();
		Object[] params = record.getParameters();
		if (params == null || params.length == 0) {
			return format;
		}
		try {
			return MessageFormat.format(format, params);
		} catch (Exception ex) {
			return format;
		}
	}

	@Override
	public void log(LogRecord record) {
		int level = getMessageLevel(record);
		String message = getMessage(record);
		Throwable thrown = record.getThrown();
		task.log(message, thrown, level);
	}
}
