/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.report.designer.core.commands;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.gef.commands.Command;


/**
 * add comment here
 * 
 */
public class MoveGuideCommand extends Command
{
	private int pDelta;
	private String propertyName; 
	
	/**
	 * @param delta
	 * @param propertyName
	 */
	public MoveGuideCommand( int delta, String propertyName )
	{
		super( );
		pDelta = delta;
		this.propertyName = propertyName;
	}
	
	public void execute() 
	{
		ReportDesignHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		MasterPageHandle page = SessionHandleAdapter.getInstance().getMasterPageHandle();
		String unit = handle.getDefaultUnits();
		
		double value = MetricUtility.pixelToPixelInch( pDelta );
		if (value < 0.0)
		{
			value = 0.0;
		}
		DimensionValue dim = DimensionUtil.convertTo(value,DesignChoiceConstants.UNITS_IN, unit );
	
		
		try
		{
			page.setProperty(propertyName, dim);
		}
		catch ( SemanticException e )
		{
			e.printStackTrace();
		}
		
	}
}
