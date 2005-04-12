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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.PropertyStructure;

/**
 * Structure used to cache data set information that include output column
 * information when it gets from databases, input/output parameter definitions.
 */

public class CachedMetaData extends PropertyStructure
{

	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public final static String CACHED_METADATA_STRUCT = "CachedMetaData"; //$NON-NLS-1$

	/**
	 * The property name of the data set parameters definitions.
	 */

	public static final String PARAMETERS_MEMBER = "parameters"; //$NON-NLS-1$

	/**
	 * Member name of the cached input parameters.
	 * 
	 * @deprecated by {@link #PARAMETERS_MEMBER}
	 */

	public final static String INPUT_PARAMETERS_MEMBER = "inputParameters"; //$NON-NLS-1$

	/**
	 * Member name of the cached output parameters.
	 * 
	 * @deprecated by {@link #PARAMETERS_MEMBER}
	 */

	public final static String OUTPUT_PARAMETERS_MEMBER = "outputParameters"; //$NON-NLS-1$

	/**
	 * Member name of the cached result set(output columns).
	 */

	public final static String RESULT_SET_MEMBER = "resultSet"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
	 */

	protected StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		assert false;
		return null;
	}

	/**
	 * Return an <code>CachedMetaDataHandle</code> to deal with the structure.
	 *  
	 */

	public StructureHandle getHandle( SimpleValueHandle valueHandle )
	{
		return new CachedMetaDataHandle( valueHandle.getElementHandle( ),
				valueHandle.getReference( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return CACHED_METADATA_STRUCT;
	}

	/**
	 * Sets the values of obsolete input parameter and output parameter
	 * properties.
	 * 
	 * @param memberName
	 *            the member name. Must be <code>INPUT_PARAMETERS_MEMBER</code>
	 *            or <code>OUTPUT_PARAMETERS_MEMBER</code>
	 * @param value
	 *            the property value
	 * 
	 * @deprecated 
	 */

	public void setObsoleteProperty( String memberName, Object value )
	{
		assert ( INPUT_PARAMETERS_MEMBER.equals( memberName ) || OUTPUT_PARAMETERS_MEMBER
				.equals( memberName ) );

		Object obj = super.getProperty( null, PARAMETERS_MEMBER );

		if ( value instanceof List )
		{
			// set isInput and isOutput flag here.

			for ( Iterator iter = ( (List) value ).iterator( ); iter.hasNext( ); )
			{
				DataSetParameter param = (DataSetParameter) iter.next( );
				if ( INPUT_PARAMETERS_MEMBER.equals( memberName ) )
					param.setIsInput( true );
				else
					param.setIsOutput( true );
			}

			if ( obj == null )
				super.setProperty( PARAMETERS_MEMBER, value );
			else
			{
				List list = (List) obj;
				list.addAll( (List) value );
			}
		}
		else if ( value == null )
		{
			if ( obj == null )
				super.setProperty( PARAMETERS_MEMBER, value );
			else
			{
				List list = (List) obj;
				List removedParams = new ArrayList( );

				for ( Iterator iter = list.iterator( ); iter.hasNext( ); )
				{
					DataSetParameter param = (DataSetParameter) iter.next( );
					if ( INPUT_PARAMETERS_MEMBER.equals( memberName )
							&& param.isInput( ) )
						removedParams.add( param );
					if ( OUTPUT_PARAMETERS_MEMBER.equals( memberName )
							&& param.isOutput( ) )
						removedParams.add( param );
				}

				list.removeAll( removedParams );
			}
		}
	}

}