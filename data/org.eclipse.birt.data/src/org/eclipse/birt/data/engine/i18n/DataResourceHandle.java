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

package org.eclipse.birt.data.engine.i18n;

import java.util.Locale;

import org.eclipse.birt.core.i18n.ResourceHandle;

/**
 * Implementation of ResourceHandle in DtE project
 */

public class DataResourceHandle extends ResourceHandle
{

	/**
	 * @param locale
	 */
	public DataResourceHandle( Locale locale )
	{
		super( locale );
	}

}
