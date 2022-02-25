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

package org.eclipse.birt.report.engine.api;

public class UnsupportedFormatException extends EngineException {

	private static final long serialVersionUID = 8679478013338620162L;

	/**
	 * constructor
	 *
	 * @param errorCode
	 * @param arg0
	 */
	public UnsupportedFormatException(String errorCode, Object arg0) {
		super(errorCode, arg0);
	}

}
