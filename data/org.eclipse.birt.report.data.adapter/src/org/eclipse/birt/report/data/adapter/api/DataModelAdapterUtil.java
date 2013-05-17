package org.eclipse.birt.report.data.adapter.api;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public class DataModelAdapterUtil 
{
	public static boolean isAggregationBinding( ComputedColumnHandle computed, ReportItemHandle handle )
	{
		return LinkedDataSetUtil.isAggregationBinding( computed, handle );
	}
}
