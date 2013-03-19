package org.eclipse.birt.report.data.adapter.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.birt.report.model.api.ReportItemHandle;


public class LinkedDataSetUtil
{
	private static String GET_LINKED_DATA_MODEL_METHOD = "getLinkedDataModel";
	
	public static boolean bindToLinkedDataSet( ReportItemHandle reportItemHandle )
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException
	{
		Method[] methods = reportItemHandle.getClass( ).getMethods( );
		for ( int i = 0; i < methods.length; i++ )
		{
			String name = methods[i].getName( );
			if ( name.equals( GET_LINKED_DATA_MODEL_METHOD ) )
			{
				Object result = methods[i].invoke( reportItemHandle );
				if ( result != null )
					return true;
			}
		}
		return false;
	}
}
