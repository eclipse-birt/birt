
package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.xml.sax.SAXException;

/**
 * TODO
 */

public class CompatibleColumnDataTypeState extends PropertyState
{

	/**
	 * Default constructor.
	 * 
	 * @param theHandler
	 *            the parser handler
	 * @param element
	 *            the element to parse
	 * @param propDefn
	 *            the property definition
	 * @param struct
	 *            the structure of OdaDataSetParameter
	 */

	CompatibleColumnDataTypeState( ModuleParserHandler theHandler,
			DesignElement element, PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element, propDefn, struct );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */

	public void end( ) throws SAXException
	{
		String value = text.toString( );
		
		doEnd( converToParamType( value ) );
	}

	private static String converToParamType( String columnType )
	{
		if ( columnType.length( ) == 0 )
			return null;

		if ( DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals( columnType ) )
			return DesignChoiceConstants.PARAM_TYPE_STRING;
		if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME
				.equals( columnType ) )
			return DesignChoiceConstants.PARAM_TYPE_DATETIME;
		if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals( columnType ) )
			return DesignChoiceConstants.PARAM_TYPE_DECIMAL;
		if ( DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals( columnType ) )
			return DesignChoiceConstants.PARAM_TYPE_FLOAT;
		if ( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals( columnType ) )
			return DesignChoiceConstants.PARAM_TYPE_INTEGER;
		if ( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING.equals( columnType ) )
			return DesignChoiceConstants.PARAM_TYPE_STRING;

		return null;
	}

}
