/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.actionhandler;

import java.rmi.RemoteException;

public interface IActionHandler {

	public void execute() throws RemoteException;

	public boolean canExecute();

	public boolean canUndo();

	public boolean canRedo();

	public void undo();

	public void redo();

	public boolean prepare() throws Exception;
}
