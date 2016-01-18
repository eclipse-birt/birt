/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/
package org.eclipse.birt.report.designer.ui;

import org.eclipse.birt.report.model.api.ReportElementHandle;

/**
 * 
 */

public interface ICrosstabBindingHelper
{
	boolean isMeasure( ReportElementHandle extendedData, String name );

	String getMeasureDataType( ReportElementHandle extendedData,
			String measureName );

}
