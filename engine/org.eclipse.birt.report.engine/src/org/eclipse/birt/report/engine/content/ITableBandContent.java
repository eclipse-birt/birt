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

package org.eclipse.birt.report.engine.content;

/**
 * Provides the interfaces for the Table Band Content
 * 
 * 
 * @version $Revision$ $Date$
 */
public interface ITableBandContent extends IReportElementContent
{

	public static final int BAND_HEADER = 0;
	public static final int BAND_BODY = 1;
	public static final int BAND_FOOTER = 2;

	/**
	 * get type
	 * 
	 * @return the type
	 */
	public int getType( );
}