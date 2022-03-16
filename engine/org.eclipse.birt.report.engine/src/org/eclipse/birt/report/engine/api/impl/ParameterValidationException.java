/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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
package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;

/**
 * Define an engine parameter validation exception that clients of the engine
 * need to handle. If there is any exception concerns to report parameter,
 * ParameterValidationException will be thrown out.
 */
public class ParameterValidationException extends EngineException {

	private static final long serialVersionUID = 1L;

	public ParameterValidationException(String errorCode, Object[] args) {
		super(errorCode, args);
	}

	public ParameterValidationException(BirtException be) {
		super(be);
	}
}
