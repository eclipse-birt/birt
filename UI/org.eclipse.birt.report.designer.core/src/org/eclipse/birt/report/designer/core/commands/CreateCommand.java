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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.gef.commands.Command;

/**
 * This command adds an object to the model. The object is not created by this
 * command, but simply attached to the parent object.
 */
public class CreateCommand extends Command
{

	private static Logger logger = Logger.getLogger( CreateCommand.class.getName( ) );
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
		// DesignElementHandle newObject = (DesignElementHandle)
		// extendsData.get( DesignerConstants.KEY_NEWOBJECT );
		DesignElementHandle newObject = getNewObject( );
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
			else if ( newObject instanceof DataSourceHandle )// simply add to
			// datasource
			// slot.
			{
				( (DesignElementHandle) parent ).addElement( newObject,
						ReportDesignHandle.DATA_SOURCE_SLOT );
			}
			else if ( newObject instanceof MasterPageHandle )
			{
				( (DesignElementHandle) parent ).addElement( newObject,
						ReportDesignHandle.PAGE_SLOT );

				ReportRequest r = new ReportRequest( );
				r.setType( ReportRequest.SELECTION );
				List selection = new ArrayList( );
				selection.add( newObject );
				r.setSelectionObject( selection );
				SessionHandleAdapter.getInstance( )
						.getMediator( )
						.notifyRequest( r );

				r = new ReportRequest( );
				r.setType( ReportRequest.OPEN_EDITOR );
				r.setSelectionObject( selection );
				SessionHandleAdapter.getInstance( )
						.getMediator( )
						.notifyRequest( r );
			}
			else if (parent instanceof CellHandle && newObject instanceof TableGroupHandle)
			{
				DesignElementHandle cellHandle = (CellHandle)parent;
				TableHandle tableHandle = null;
				while(cellHandle.getContainer( ) != null)
				{
					cellHandle = cellHandle.getContainer( );
					if (cellHandle instanceof TableHandle)
					{
						tableHandle = (TableHandle)cellHandle;
						break;
					}
				}
				if (tableHandle != null)
				{
					tableHandle.getGroups( ).add( newObject, tableHandle.getGroups( ).getCount( ) );
				}
			}
			else if ( DEUtil.getDefaultSlotID( parent ) != -1 )
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
			else if ( DEUtil.getDefaultSlotID( parent ) == -1 )
			{
				if ( after != null )
				{
					int pos = DEUtil.findInsertPosition( (DesignElementHandle) parent,
							(DesignElementHandle) after,
							DEUtil.getDefaultContentName( parent ) );

					( (DesignElementHandle) parent ).add( DEUtil.getDefaultContentName( parent ),
							newObject,
							pos );
				}
				else
				{
					( (DesignElementHandle) parent ).add( DEUtil.getDefaultContentName( parent ),
							newObject );
				}
			}
			DEUtil.setDefaultTheme( newObject );
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
			logger.log( Level.SEVERE, e.getMessage( ), e );
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
	 * 		The parent to set.
	 */
	public void setParent( Object parent )
	{
		this.parent = parent;
	}

	/**
	 * Gets the edit part the new report element be added after.
	 * 
	 * @param model
	 * 		The model after the new element
	 */
	public void setAfter( Object model )
	{
		this.after = model;

	}

	/**
	 * Get the new object
	 * 
	 * @return Return the object
	 */
	public DesignElementHandle getNewObject( )
	{
		return (DesignElementHandle) extendsData.get( DesignerConstants.KEY_NEWOBJECT );
	}
}