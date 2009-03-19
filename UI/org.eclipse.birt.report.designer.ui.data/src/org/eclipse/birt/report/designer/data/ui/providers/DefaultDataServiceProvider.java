/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.providers;

import org.eclipse.birt.report.designer.data.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.internal.ui.data.IDataServiceProvider;

/**
 * DefaultDataServiceProvider
 */
public class DefaultDataServiceProvider implements IDataServiceProvider
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.data.IDataServiceProvider
	 * #createDataSet()
	 */
	public void createDataSet( )
	{
		new NewDataSetAction( ).run( );
	}

}
