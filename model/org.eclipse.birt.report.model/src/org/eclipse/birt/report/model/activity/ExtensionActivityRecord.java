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

package org.eclipse.birt.report.model.activity;

import org.eclipse.birt.report.model.api.extension.IElementCommand;

/**
 * The activity record provides the mechanism for performing a low-level change
 * to the ROM extension elements.
 */

public final class ExtensionActivityRecord extends ActivityRecord
{

	/**
	 * The effective extended element command this record has.
	 */

	private IElementCommand extRecord = null;

	//	/**
	//	 * The design element, that is, a report element. All report element
	// classes
	//	 * derives from DesignElement
	//	 */
	//
	//	private DesignElement element = null;
	//	
	//	/**
	//	 * The extension property name.
	//	 */
	//	
	//	private String propName = null;

	/**
	 * Constructs the extension activity record with the effective extended
	 * element command.
	 * 
	 * @param extCommand
	 *            the effective extended element command
	 */

	public ExtensionActivityRecord( IElementCommand extCommand )
	{
		assert extCommand != null;
		extRecord = extCommand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#canRedo()
	 */

	public boolean canRedo( )
	{
		return extRecord.canRedo( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#canUndo()
	 */

	public boolean canUndo( )
	{
		return extRecord.canUndo( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#destroy()
	 */

	public void destroy( )
	{
		super.destroy( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getTransNo()
	 */

	public int getTransNo( )
	{
		return super.getTransNo( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#execute()
	 */

	public void execute( )
	{
		extRecord.execute( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#undo()
	 */

	public void undo( )
	{
		extRecord.undo( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#redo()
	 */

	public void redo( )
	{
		extRecord.redo( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#sendNotifcations(
	 *      boolean transactionStarted )
	 */

	public void sendNotifcations( boolean transactionStarted )
	{
		return;
	}

	//	/*
	//	 * (non-Javadoc)
	//	 *
	//	 * @see
	// org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	//	 */
	//	public DesignElement getTarget( )
	//	{
	//		return element;
	//	}

	//	/*
	//	 * (non-Javadoc)
	//	 *
	//	 * @see
	// org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	//	 */
	//	
	//	public NotificationEvent getEvent( )
	//	{
	//		// Use the same notification for the done/redone and undone states.
	//		
	//		assert element instanceof ExtendedItem;
	//		
	//		IElement extElement = ((ExtendedItem)element).getExtendedElement( );
	//		assert extElement != null;
	//		
	//		if ( extElement.refreshPropertyDefinition( ) )
	//		{
	//			return new ExtensionPropertyDefinitionEvent( element );
	//		}
	//
	//		return new PropertyEvent( element, propName );
	//	}
}