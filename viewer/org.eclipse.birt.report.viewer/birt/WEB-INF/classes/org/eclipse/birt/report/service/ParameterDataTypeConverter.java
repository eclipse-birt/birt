
package org.eclipse.birt.report.service;

import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

public class ParameterDataTypeConverter
{

	/**
	 * Parameter data type convertion from string to int.
	 * 
	 * @param type
	 *            String
	 * @return
	 */
	public static final int ConvertDataType( String type )
	{
		if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_BOOLEAN;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_DATE_TIME;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_DECIMAL;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_FLOAT;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_STRING;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( type ) )
		{
			return IScalarParameterDefn.TYPE_INTEGER;
		}
		return IScalarParameterDefn.TYPE_ANY;
	}

	/**
	 * Parameter data type convertion from int to string.
	 * 
	 * @param type
	 *            String
	 * @return
	 */
	public static final String ConvertDataType( int type )
	{
		String dataType = DesignChoiceConstants.PARAM_TYPE_ANY;

		switch ( type )
		{
			case IScalarParameterDefn.TYPE_BOOLEAN :
				dataType = DesignChoiceConstants.PARAM_TYPE_BOOLEAN;
				break;
			case IScalarParameterDefn.TYPE_DATE_TIME :
				dataType = DesignChoiceConstants.PARAM_TYPE_DATETIME;
				break;
			case IScalarParameterDefn.TYPE_DECIMAL :
				dataType = DesignChoiceConstants.PARAM_TYPE_DECIMAL;
				break;
			case IScalarParameterDefn.TYPE_FLOAT :
				dataType = DesignChoiceConstants.PARAM_TYPE_FLOAT;
				break;
			case IScalarParameterDefn.TYPE_STRING :
				dataType = DesignChoiceConstants.PARAM_TYPE_STRING;
				break;
			case IScalarParameterDefn.TYPE_INTEGER :
				dataType = DesignChoiceConstants.PARAM_TYPE_INTEGER;
				break;
		}

		return dataType;
	}
}
