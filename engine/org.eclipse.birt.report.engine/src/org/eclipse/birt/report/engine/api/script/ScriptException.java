/*******************************************************************************
 * Copyright (c) 2005,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script;

import org.eclipse.birt.core.exception.BirtException;

/** Exception thrown in scripting. */
public class ScriptException extends BirtException {

	private String message;
	private static final long serialVersionUID = -8895956245804505077L;

	public ScriptException(String message) {
		super(message);
		this.message = message;
	}

	public ScriptException(Throwable root) {
		this(root.getMessage());
		initCause(root);
	}

	public ScriptException(String message, Throwable root) {
		this(message);
		initCause(root);
	}

	@Override
	public String getLocalizedMessage() {
		return message;
	}

	@Override
	protected String getLocalizedMessage(String errorCode) {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
