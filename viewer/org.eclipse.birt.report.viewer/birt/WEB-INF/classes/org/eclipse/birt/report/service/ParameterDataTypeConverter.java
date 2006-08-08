package org.eclipse.birt.report.service;

import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

public class ParameterDataTypeConverter
{
	/**
	 * Parameter typer convertion.
	 * 
	 * @param type
	 * @return
	 */
	public static final int getEngineDataType( String type )
	{
		if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_BOOLEAN;
		} else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_DATE_TIME;
		} else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_DECIMAL;
		} else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_FLOAT;
		} else if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_STRING;
		} else if ( DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_INTEGER;
		}
		return IScalarParameterDefn.TYPE_ANY;
	}
}
