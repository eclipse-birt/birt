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

package org.eclipse.birt.report.engine.content;

/**
 * Provides the interfaces for Container content
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2005/02/25 06:02:24 $
 */
public interface IContainerContent extends IReportItemContent
{

	/**
	 * @return the container type
	 */
	public int getType( );
}