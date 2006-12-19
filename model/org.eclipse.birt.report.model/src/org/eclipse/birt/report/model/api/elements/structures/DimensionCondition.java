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
import java.util.List;

import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.olap.Hierarchy;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * The DimensionCondition structure defines a list of join conditions between
 * cube and hierarchy.
 */

public class DimensionCondition extends Structure
{

	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public final static String DIMENSION_CONDITION_STRUCT = "DimensionCondition"; //$NON-NLS-1$

	/**
	 * The property name of the data set parameters definitions.
	 */

	public static final String PRIMARY_KEYS_MEMBER = "primaryKeys"; //$NON-NLS-1$

	/**
	 * Member name of the cached result set(output columns).
	 */

	public final static String HIERARCHY_MEMBER = "hierarchy"; //$NON-NLS-1$

	/**
	 * The file name of the included library.
	 */

	protected List primaryKeys;

	/**
	 * The namespace of the included library.
	 */

	protected ElementRefValue hierarchy;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.String)
	 */
	protected Object getIntrinsicProperty( String propName )
	{
		if ( PRIMARY_KEYS_MEMBER.equalsIgnoreCase( propName ) )
			return primaryKeys;
		else if ( HIERARCHY_MEMBER.equalsIgnoreCase( propName ) )
			return hierarchy;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
	 */
	protected StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new DimensionConditionHandle( valueHandle, index );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( PRIMARY_KEYS_MEMBER.equalsIgnoreCase( propName ) )
		{
			primaryKeys = (List) value;
		}
		else if ( HIERARCHY_MEMBER.equalsIgnoreCase( propName ) )
		{
			if ( value instanceof String )
				hierarchy = new ElementRefValue( StringUtil
						.extractNamespace( (String) value ), StringUtil
						.extractName( (String) value ) );
			else if ( value instanceof Hierarchy )
				hierarchy = new ElementRefValue( null, (Hierarchy) value );
			else
				hierarchy = (ElementRefValue) value;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#getStructName()
	 */
	public String getStructName( )
	{
		return DIMENSION_CONDITION_STRUCT;
	}

	/**
	 * Sets the primary key list for this dimension condition.
	 * 
	 * @param keys
	 *            the key list to set
	 */
	public void setPrimaryKeys( List keys )
	{
		setProperty( PRIMARY_KEYS_MEMBER, keys );
	}

	/**
	 * Gets the primary key list for this dimension condition.
	 * 
	 * @return the primary key list if set, otherwise null
	 */

	public List getPrimaryKeys( )
	{
		return primaryKeys;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected Object clone( ) throws CloneNotSupportedException
	{
		DimensionCondition struct = (DimensionCondition) super.clone( );
		if ( primaryKeys != null )
		{
			struct.primaryKeys = new ArrayList();
			for ( int i = 0; i < primaryKeys.size( ); i++ )
			{
				struct.primaryKeys.add( primaryKeys.get( i ) );
			}			
		}
		struct.hierarchy = (ElementRefValue) ( hierarchy == null ? null : hierarchy.copy( ) );
		return struct;
	}
	
	
}
