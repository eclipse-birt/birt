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
package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.EngineException;

/**
 * Define an engine parameter validation exception that clients of the engine
 * need to handle. If there is any exception concerns to report parameter,
 * ParameterValidationException will be thrown out.
 */
public class ParameterValidationException extends EngineException
{

	public ParameterValidationException( String errorCode, Object[] args )
	{
		super( errorCode, args );
	}
}
