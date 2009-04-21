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

package org.eclipse.birt.report.engine.emitter.config;

import java.util.Map;

/**
 * AbstractEmitterDescriptor
 */
public abstract class AbstractEmitterDescriptor implements IEmitterDescriptor
{

	protected Map initParams = null;

	public void setInitParameters( Map params )
	{
		this.initParams = params;
	}

	public IConfigurableOptionObserver createOptionObserver( )
	{
		return null;
	}

	public String getDescription( )
	{
		return null;
	}

	public String getDisplayName( )
	{
		return null;
	}

	public String getID( )
	{
		return null;
	}

}
