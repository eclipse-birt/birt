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

package org.eclipse.birt.core.framework;


/**
 * Defines the Platform interface that allows BIRT to be run in Eclipse and 
 * server environments 
 * 
 * @version $Revision: 1.2 $ $Date: 2005/02/07 02:16:26 $
 */
public interface IPlatform
{
	/**
	 * @return the global extension registry
	 */
	IExtensionRegistry getExtensionRegistry();
	IBundle getBundle(String symblicName);
}
