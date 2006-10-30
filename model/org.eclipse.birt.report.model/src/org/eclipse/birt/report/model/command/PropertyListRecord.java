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

package org.eclipse.birt.report.model.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.Structure.StructureContext;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Records adding or removing an item from a property list.
 * 
 */

public class PropertyListRecord extends SimpleRecord
{

	/**
	 * The element that contains the property list.
	 */

	protected DesignElement element = null;

	/**
	 * Reference to the property list.
	 */

	protected MemberRef listRef = null;

	/**
	 * The property list itself.
	 */

	protected List list = null;

	/**
	 * The item to add or remove.
	 */

	protected IStructure value = null;

	/**
	 * Whether the operation is an add or remove.
	 */

	protected final boolean isAdd;

	/**
	 * Constructor for an add operation.
	 * 
	 * @param obj
	 *            the element that contains the property list
	 * @param ref
	 *            reference to the property list
	 * @param theList
	 *            the property list itself
	 * @param struct
	 *            the item to add
	 */

	public PropertyListRecord( DesignElement obj, MemberRef ref, List theList,
			IStructure struct )
	{
		assert obj != null;
		assert ref != null;
		assert theList != null;
		assert struct != null;

		assert obj.getPropertyDefn( ref.getPropDefn( ).getName( ) ) == ref
				.getPropDefn( );

		this.element = obj;
		this.listRef = ref;
		this.list = theList;
		this.value = struct;
		this.isAdd = true;

	}

	/**
	 * Constructor for a remove operation. Removes the item given by the member
	 * reference.
	 * 
	 * @param obj
	 *            the element that contains the property list
	 * @param ref
	 *            reference to the structure in the list.
	 * @param theList
	 *            the property list itself
	 */

	public PropertyListRecord( DesignElement obj, MemberRef ref, List theList )
	{
		assert obj != null;
		assert ref != null;
		assert theList != null;

		assert obj.getPropertyDefn( ref.getPropDefn( ).getName( ) ) == ref
				.getPropDefn( );

		assert ref.getIndex( ) >= 0 && ref.getIndex( ) < theList.size( );

		this.element = obj;
		this.listRef = ref;
		this.list = theList;
		this.isAdd = false;

		this.value = (IStructure) list.get( listRef.getIndex( ) );

		label = ModelMessages.getMessage(
				MessageConstants.CHANGE_PROPERTY_MESSAGE, new String[]{listRef
						.getPropDefn( ).getDisplayName( )} );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		boolean doAdd = ( undo && !isAdd || !undo && isAdd );
		if ( doAdd )
		{
			list.add( listRef.getIndex( ), value );

			// setup the context for the structure.

			StructureContext structContext = new Structure.StructureContext(
					element, listRef.getPropDefn( ).getName( ) );
			( (Structure) value ).setContext( structContext );

		}
		else
		{
			list.remove( listRef.getIndex( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		// Use the same notification for the done/redone and undone states.

		return new PropertyEvent( element, listRef.getPropDefn( ).getName( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List getPostTasks( )
	{
		List retList = new ArrayList( );
		retList.addAll( super.getPostTasks( ) );

		retList.add( new NotificationRecordTask( element, getEvent( ) ) );

		// if the structure is referencable, then send notification to the
		// clients

		if ( value != null && value.isReferencable( ) )
		{
			ReferencableStructure refValue = (ReferencableStructure) value;
			retList.add( new NotificationRecordTask( refValue, getEvent( ) ) );
		}

		return retList;
	}
}