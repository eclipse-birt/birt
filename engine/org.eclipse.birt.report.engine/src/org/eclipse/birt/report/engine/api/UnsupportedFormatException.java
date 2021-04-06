/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
