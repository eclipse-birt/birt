/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IPreparationContext;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;

public class PreparationContext extends ReportContextImpl
		implements
			IPreparationContext
{

	DesignVisitor visitor = null;

	public PreparationContext( ExecutionContext context, DesignVisitor visitor )
	{
		super( context );
		this.visitor = visitor;
	}

	public ClassLoader getApplicationClassLoader( )
	{
		return context.getApplicationClassLoader( );
	}

	public void prepare( DesignElementHandle handle )
	{
		visitor.apply( handle );
	}
}
