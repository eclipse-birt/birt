/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.script;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Event handler for a script data source
 *
 */
public interface IScriptDataSourceEventHandler extends IBaseDataSourceEventHandler {
	public void handleOpen(IDataSourceInstanceHandle dataSource) throws BirtException;

	public void handleClose(IDataSourceInstanceHandle dataSource) throws BirtException;
}
