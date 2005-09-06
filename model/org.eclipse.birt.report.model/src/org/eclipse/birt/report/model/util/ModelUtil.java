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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.activity.LayoutActivityTask;
import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * The utility class which provides many static methods used in Model.
 */

public class ModelUtil
{

	/**
	 * Clone the structure list, a list value contains a list of
	 * <code>IStructure</code>.
	 * 
	 * @param list
	 *            The structure list to be cloned.
	 * @return The cloned structure list.
	 */

	public static ArrayList cloneStructList( ArrayList list )
	{
		if ( list == null )
			return null;

		ArrayList returnList = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object item = list.get( i );
			if ( item instanceof IStructure )
			{
				returnList.add( ( (IStructure) item ).copy( ) );
			}
			else
			{
				assert false;
			}
		}
		return returnList;
	}

	/**
	 * Clones the value.
	 * <ul>
	 * <li>If the value is of simple type, like integer, or string, the
	 * original value will be returned.
	 * <li>If the value is strcuture list, the cloned structure list will be
	 * cloned.
	 * <li>If the value is structure, the cloned structure will be cloned.
	 * <li>If the value is element/strucuture reference value, the
	 * element/structure name will be returned.
	 * </ul>
	 * 
	 * @param propDefn
	 *            definition of property
	 * @param value
	 *            value to clone
	 * @return new value
	 */

	public static Object copyValue( IPropertyDefn propDefn, Object value )
	{

		if ( value == null )
			return null;

		switch ( propDefn.getTypeCode( ) )
		{
			case PropertyType.STRUCT_TYPE :

				if ( propDefn.isList( ) )
					return ModelUtil.cloneStructList( (ArrayList) value );

				return ( (Structure) value ).copy( );

			case PropertyType.ELEMENT_REF_TYPE :

				ElementRefValue refValue = (ElementRefValue) value;
				return new ElementRefValue( null, refValue.getName( ) );

			case PropertyType.STRUCT_REF_TYPE :

				StructRefValue structRefValue = (StructRefValue) value;
				return new StructRefValue( structRefValue.getName( ) );
		}

		return value;
	}

	/**
	 * Filtrates the notification tasks.
	 * 
	 * @param tasks
	 *            the notification tasks
	 * @return a list contained filtrated notification tasks
	 */

	public static List filterNotificationTasks( List tasks )
	{
		List notifications = new ArrayList( );
		for ( int i = 0; i < tasks.size( ); i++ )
		{
			RecordTask task = (RecordTask) tasks.get( i );
			if ( task instanceof NotificationRecordTask )
				notifications.add( task );
		}

		return EventFilter.getInstance( ).filter( notifications );
	}

	/**
	 * Filtrates the table layout tasks.
	 * 
	 * @param tasks
	 *            the table layout tasks
	 * @return a list contained filtrated table layout tasks
	 */

	public static List filterTableLayoutTasks( List tasks )
	{
		List retList = new ArrayList( );
		Set tables = new LinkedHashSet( );

		for ( int i = 0; i < tasks.size( ); i++ )
		{
			RecordTask task = (RecordTask) tasks.get( i );

			if ( task instanceof LayoutActivityTask )
			{
				DesignElement table = (DesignElement) ( (LayoutActivityTask) task )
						.getTarget( );
				if ( !tables.contains( table ) )
				{
					retList.add( task );
					tables.add( table );
				}
			}
		}

		return retList;
	}

}
