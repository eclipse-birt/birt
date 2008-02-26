/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.script.IMeasure;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * MeasureImpl
 */
public class MeasureImpl implements IMeasure
{

	private MeasureHandle mh;

	public MeasureImpl( MeasureViewHandle mv )
	{
		if ( mv != null )
		{
			mh = mv.getCubeMeasure( );
		}
	}

	public String getFunctionName( )
	{
		if ( mh != null )
		{
			return mh.getFunction( );
		}
		return null;
	}

	public String getMeasureExpression( )
	{
		if ( mh != null )
		{
			return mh.getMeasureExpression( );
		}
		return null;
	}

	public String getName( )
	{
		if ( mh != null )
		{
			return mh.getName( );
		}
		return null;
	}

}
