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

import org.eclipse.birt.report.model.activity.LayoutTableActivityTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Moves a content element within its container.
 * 
 */

public class MoveContentRecord extends SimpleRecord
{

	/**
	 * The container element.
	 */

	protected DesignElement container = null;

	/**
	 * The content element to move.
	 */

	protected DesignElement content = null;

	/**
	 * The slot within the container that holds the content.
	 */

	protected int slot = 0;

	/**
	 * The new position of the content.
	 */

	protected int newPosn = 0;

	/**
	 * The original position of the content.
	 */

	protected int oldPosn = 0;

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the container element.
	 * @param theSlot
	 *            the slot id that contains the content.
	 * @param obj
	 *            the content element to move.
	 * @param posn
	 *            the new position of the content element.
	 */

	public MoveContentRecord( DesignElement element, int theSlot,
			DesignElement obj, int posn )
	{
		container = element;
		slot = theSlot;
		content = obj;
		newPosn = posn;

		assert container != null;
		assert container.getSlot( slot ) != null;
		assert content != null;
		assert container.getDefn( ).getSlot( slot ).canContain( content );
		assert newPosn >= 0 && newPosn < container.getSlot( slot ).getCount( );

		oldPosn = container.getSlot( slot ).findPosn( content );

		label = ModelMessages
				.getMessage( MessageConstants.MOVE_CONTENT_MESSAGE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform()
	 */

	protected void perform( boolean undo )
	{
		int from = undo ? newPosn : oldPosn;
		int to = undo ? oldPosn : newPosn;
		container.getSlot( slot ).moveContent( from, to );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		return new ContentEvent( container, content, slot, ContentEvent.SHIFT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List getPostTasks( )
	{
		List retValue = new ArrayList( );
		retValue.addAll( super.getPostTasks( ) );

		if ( !( content instanceof TableGroup || content instanceof TableRow || content instanceof Cell ) )
			return retValue;

		TableItem table = LayoutUtil.getTableContainer( container );
		if ( table == null )
			return retValue;

		retValue.add( new LayoutTableActivityTask( table.getRoot( ), table ) );
		return retValue;
	}

}