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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Represents the property state for OdaDataSet.
 * 
 * <pre>
 * 
 *  
 *        &lt;oda-data-set name=&quot;myDataSet1&quot;&gt;
 *          &lt;property name=&quot;type&quot;&gt;JdbcSelectDataSet&lt;/property&gt;
 *        &lt;/oda-data-set&gt;
 *   
 *  
 * </pre>
 */

public class CompatibleOdaDataSetPropertyState extends PropertyState
{

	final static String JDBC_EXTENSION_ID = "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet"; //$NON-NLS-1$
	final static String FLAT_FILE_EXTENSION_ID = "org.eclipse.birt.report.data.oda.flatfile.dataSet"; //$NON-NLS-1$

	CompatibleOdaDataSetPropertyState( DesignParserHandler theHandler,
			DesignElement element )
	{
		super( theHandler, element );

		assert element instanceof OdaDataSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#setProperty(java.lang.String,
	 *      java.lang.String)
	 */

	protected void setProperty( String propName, String value )
	{
		assert propName != null;

		if ( propName.equalsIgnoreCase( DesignElement.NAME_PROP )
				|| propName.equalsIgnoreCase( DesignElement.EXTENDS_PROP ) )
		{
			DesignParserException e = new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_INVALID_PROPERTY_SYNTAX );
			handler.semanticError( e );
			valid = false;
			return;
		}

		// The property definition is not found, including user
		// properties.

		ElementPropertyDefn propDefn = element.getPropertyDefn( propName );
		if ( propDefn == null )
		{
			if ( "type".equals( propName ) ) //$NON-NLS-1$ 
			{
				String convertedValue = convertToExtensionID( value );

				setProperty( OdaDataSet.EXTENSION_ID_PROP, convertedValue );
				return;
			}
		}
		if ( propDefn == null )
		{
			DesignParserException e = new DesignParserException( null,
					new String[]{propName},
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY );
			RecoverableError.dealUndefinedProperty( handler, e );
			valid = false;
			return;
		}

		doSetProperty( propDefn, value );
	}

	/**
	 * Convert type name to extension ID.
	 * 
	 * @param value
	 *            type name
	 * @return extension ID
	 */

	private String convertToExtensionID( String value )
	{
		if ( "JdbcSelectDataSet".equalsIgnoreCase( value ) ) //$NON-NLS-1$
			return JDBC_EXTENSION_ID;
		else if ( "FlatFileSelectDataSet".equalsIgnoreCase( value ) ) //$NON-NLS-1$
			return FLAT_FILE_EXTENSION_ID;

		return null;
	}
}
