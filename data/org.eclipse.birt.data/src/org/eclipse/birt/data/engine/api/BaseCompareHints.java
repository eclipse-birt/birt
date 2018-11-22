/**************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 **************************************************************************/ 
package org.eclipse.birt.data.engine.api;

import java.util.Comparator;

/**
 * This class is used to indicate the hints when doing the comparison.
 *	@since 4.8
 */
public class BaseCompareHints
{
	private Comparator comparator;
	private String nullStringType;

	public BaseCompareHints( Comparator comparator, String nullStringType )
	{
		this.comparator = comparator;
		this.nullStringType = nullStringType;
	}

	/**
	 * 
	 * @return
	 */
	public Comparator getComparator( )
	{
		return this.comparator;
	}

	/**
	 * 
	 * @return
	 */
	public String getNullType( )
	{
		return this.nullStringType;
	}
}
