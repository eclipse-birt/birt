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


package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.data.ui.util.ReportDataHandle;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.ReportDataNodeProvider;
import org.eclipse.birt.report.model.api.ModuleHandle;


public class DataCubeNodeProvider extends ReportDataNodeProvider
{

	

	public Object[] getChildren( Object object )
	{
		ModuleHandle handle = ( (ReportDataHandle) object ).getModuleHandle( );
		ArrayList list = new ArrayList( );

		list.add( handle.getCubes( )  );

		return list.toArray( );
	}

}
