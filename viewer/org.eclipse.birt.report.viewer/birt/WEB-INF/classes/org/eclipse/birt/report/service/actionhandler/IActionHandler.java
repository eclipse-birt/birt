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

public interface IActionHandler
{
	public abstract void execute( ) throws RemoteException;
	public abstract boolean canExecute( );
	public abstract boolean canUndo( );
	public abstract boolean canRedo( );
	public abstract void undo( );
	public abstract void redo( );	
	
	abstract boolean prepare( ) throws Exception;
}
