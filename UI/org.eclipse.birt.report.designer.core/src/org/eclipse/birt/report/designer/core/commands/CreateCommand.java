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

package org.eclipse.birt.report.designer.core.commands;

import java.util.Map;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.gef.commands.Command;

/**
 * This command adds an object to the model. The object is not created by this
 * command, but simply attached to the parent object.
 * 
 * 
 */

public class CreateCommand extends Command
{

	private Object parent;

	private Object after;

	private Map extendsData;

	/**
	 * Constructor
	 * 
	 * @param extendsData
	 */
	public CreateCommand( Map extendsData )
	{
		this.extendsData = extendsData;
	}

	/**
	 * Executes the Command. This method should not be called if the Command is
	 * not executable.
	 */
	public void execute( )
	{
		DesignElementHandle newObject = (DesignElementHandle) extendsData.get( DesignerConstants.KEY_NEWOBJECT );
		if ( DesignerConstants.TRACING_COMMANDS )
		{
			System.out.println( "CreateCommand >> Starts ... " ); //$NON-NLS-1$
		}
		try
		{
			// For all virtual model node keep the SlotHandle
			if ( parent instanceof SlotHandle )
			{
				if ( after != null )
				{
					int pos = DEUtil.findInsertPosition( ( (SlotHandle) parent ).getElementHandle( ),
							(DesignElementHandle) after,
							( (SlotHandle) parent ).getSlotID( ) );
					( (SlotHandle) parent ).add( newObject, pos );

				}
				else
				{
					( (SlotHandle) parent ).add( newObject );
				}

			}
			else
			{
				// calculate the position of added element
				if ( after != null )
				{
					int pos = DEUtil.findInsertPosition( (DesignElementHandle) parent,
							(DesignElementHandle) after );
					int slotID = DEUtil.findSlotID( parent, after );

					( (DesignElementHandle) parent ).addElement( newObject,
							slotID,
							pos );
				}
				else
				{
					( (DesignElementHandle) parent ).addElement( newObject,
							DEUtil.getDefaultSlotID( parent ) );
				}
			}
			if ( DesignerConstants.TRACING_COMMANDS )
			{
				System.out.println( "CreateCommand >> Finished. " //$NON-NLS-1$
						+ DEUtil.getDisplayLabel( newObject )
						+ " created" ); //$NON-NLS-1$
			}
		}
		catch ( SemanticException e )
		{
			if ( DesignerConstants.TRACING_COMMANDS )
			{
				System.out.println( "CreateCommand >> Failed" ); //$NON-NLS-1$
			}
			e.printStackTrace( );
		}
	}

	/**
	 * Gets the parent edit part the new report element be added on.
	 * 
	 * @return Returns the parent.
	 */
	public Object getParent( )
	{
		return parent;
	}

	/**
	 * Sets the parent edit part the new report element be added on.
	 * 
	 * @param parent
	 *            The parent to set.
	 */
	public void setParent( Object parent )
	{
		this.parent = parent;
	}

	/**
	 * Gets the edit part the new report element be added after.
	 * 
	 * @param model
	 *            The model after the new element
	 */
	public void setAfter( Object model )
	{
		this.after = model;

	}
}