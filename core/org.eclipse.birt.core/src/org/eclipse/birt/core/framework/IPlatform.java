/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework;

import java.io.IOException;
import java.net.URL;


/**
 * Defines the Platform interface that allows BIRT to be run in Eclipse and 
 * server environments 
 * 
 * @version $Revision: 1.4 $ $Date: 2005/04/12 03:17:34 $
 */
public interface IPlatform
{
	/**
	 * @return the global extension registry
	 */
	IExtensionRegistry getExtensionRegistry();
	IBundle getBundle(String symblicName);
	URL asLocalURL(URL url) throws IOException;
}
