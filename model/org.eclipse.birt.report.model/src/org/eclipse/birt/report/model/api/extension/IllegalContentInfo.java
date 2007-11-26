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

package org.eclipse.birt.report.model.api.extension;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.extension.SimplePeerExtensibilityProvider.UndefinedChildInfo;

/**
 * 
 */
public class IllegalContentInfo
{

	private UndefinedChildInfo info = null;
	private Module module = null;

	/**
	 * 
	 * @param infor
	 * @param module
	 */
	public IllegalContentInfo( UndefinedChildInfo infor, Module module )
	{
		this.info = infor;
		this.module = module;
	}

	/**
	 * 
	 * @return
	 */
	public DesignElementHandle getContent( )
	{
		return info.getChild( ).getHandle( module );
	}

	/**
	 * 
	 * @return
	 */
	public int getIndex( )
	{
		return info.getIndex( );
	}
}
