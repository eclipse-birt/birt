package org.eclipse.birt.report.data.adapter.api;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;


public class LinkedDataSetUtil
{
	public static boolean bindToLinkedDataSet( ReportItemHandle reportItemHandle )
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException
	{
		return false;
	}
	
	public static boolean isLinkedDataSetCube( CubeHandle handle )
	{
		return false;
	}
}
