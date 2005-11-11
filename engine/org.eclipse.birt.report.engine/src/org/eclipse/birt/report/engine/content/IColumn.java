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

import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * column definition used by table content.
 * 
 * 
 * @version $Revision: 1.3 $ $Date: 2005/11/10 08:55:20 $
 */
public interface IColumn
{

	/**
	 * @return Returns the width.
	 */
	public DimensionType getWidth( );

	public String getStyleClass();
}