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

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.core.exception.BirtException;

/**
 * The locker manager used by the system.
 * 
 * The user should register the lock mangager to the report engine.
 * 
 * @version $Revision:$ $Date:$
 */
public class ReportDocumentLockManager implements IReportDocumentLockManager
{

	static protected IReportDocumentLockManager instance = null;

	public static IReportDocumentLockManager getInstance( )
	{
		if ( instance != null )
		{
			return instance;
		}
		synchronized ( ReportDocumentLockManager.class )
		{
			if ( instance == null )
			{
				instance = new IReportDocumentLockManager( ) {

					public void lock( String document, boolean share )
							throws BirtException
					{
					}

					public void unlock( String document, boolean share )
					{
					}

				};
			}
		}
		return instance;
	}

	private ReportDocumentLockManager( )
	{
	}

	public void lock( String document, boolean share ) throws BirtException
	{
	}

	public void unlock( String document, boolean share )
	{
	}

}
