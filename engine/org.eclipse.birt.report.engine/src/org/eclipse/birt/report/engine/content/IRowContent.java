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
 * Provides the interfaces for the Row AbstractContent
 * 
 * 
 * @version $Revision: 1.10 $ $Date: 2006/06/13 15:37:17 $
 */
public interface IRowContent extends IContainerContent
{
	public int getRowID( );

	public void setRowID( int rowID );

	public ITableContent getTable( );

	public String getGroupId( );
	
	public void setGroupId( String groupId );

	public IGroupContent getGroup( );

	public IBandContent getBand( );

}