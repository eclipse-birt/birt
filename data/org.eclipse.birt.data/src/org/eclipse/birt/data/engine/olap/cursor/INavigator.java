/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.cursor;

import javax.olap.OLAPException;

interface INavigator
{

	public boolean next( ) throws OLAPException;

	public boolean previous( ) throws OLAPException;

	public boolean relative( int arg0 ) throws OLAPException;

	public boolean first( ) throws OLAPException;

	public boolean last( ) throws OLAPException;

	public boolean isBeforeFirst( );

	public boolean isAfterLast( ) throws OLAPException;

	public boolean isFirst( ) throws OLAPException;

	public boolean isLast( ) throws OLAPException;

	public void afterLast( ) throws OLAPException;

	public void beforeFirst( ) throws OLAPException;

	public void setPosition( long position ) throws OLAPException;

	public long getPosition( ) throws OLAPException;

	public void close( );

	public long getExtend( );

	public int getType( );

	public Object getCurrentMemeber( ) throws OLAPException;

}
