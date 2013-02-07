/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data.dte;


import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

public abstract class AbstractDataEngine extends AbstractDataEngineImpl implements IDataEngine
{

	public AbstractDataEngine( DataEngineFactory factory, ExecutionContext context ) throws BirtException
	{
		super( factory, context );
	}

}