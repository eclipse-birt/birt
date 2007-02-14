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

import org.eclipse.birt.report.model.activity.LayoutRecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TableGroup;
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
	 * 
	 */
	protected Module module = null;

	/**
	 * The content element to move.
	 */

	protected DesignElement content = null;

	/**
	 * The slot within the container that holds the content.
	 */

	protected ContainerContext focus = null;

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
	 * @param theModule
	 * 
	 * @param containerInfor
	 *            the container information.
	 * @param obj
	 *            the content element to move.
	 * @param posn
	 *            the new position of the content element.
	 */

	public MoveContentRecord( Module theModule,
			ContainerContext containerInfor, DesignElement obj, int posn )
	{
		module = theModule;
		focus = containerInfor;
		content = obj;
		newPosn = posn;

		assert focus != null;
		assert focus.getContainerDefn( ) != null;
		assert content != null;
		assert focus.canContain( module, content );
		assert newPosn >= 0 && newPosn < focus.getContentCount( module );

		oldPosn = focus.indexOf( module, content );

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
		focus.move( module, from, to );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		if ( eventTarget != null )
			return eventTarget.getElement( );

		return focus.getElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		// if the element works like properties, return property event instead
		// of content event.

		if ( eventTarget != null )
		{
			return new PropertyEvent( eventTarget.getElement( ), eventTarget
					.getPropName( ) );
		}

		return new ContentEvent( focus, content, ContentEvent.SHIFT );
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

		ReportItem compoundElement = LayoutUtil.getCompoundContainer( focus
				.getElement( ) );
		if ( compoundElement == null )
			return retValue;

		retValue.add( new LayoutRecordTask( compoundElement.getRoot( ),
				compoundElement ) );
		return retValue;
	}

}