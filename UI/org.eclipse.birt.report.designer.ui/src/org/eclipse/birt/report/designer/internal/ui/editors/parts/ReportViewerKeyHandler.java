/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Provides key events for report viewer.
 * <p>
 * COPY, PASTE, UNDO, REDO, DELETE functions are provided currently.
 * <p>
 */

public class ReportViewerKeyHandler extends GraphicalViewerKeyHandler
{

	private ActionRegistry actionRegistry;

	private TableCellKeyDelegate tableDelgate;

	final static public int NO_MASK = 0;

	/**
	 * Constructor of KeyHandler
	 * 
	 * @param viewer
	 * @param actionRegistry
	 */
	public ReportViewerKeyHandler( GraphicalViewer viewer,
			ActionRegistry actionRegistry )
	{
		super( viewer );
		this.actionRegistry = actionRegistry;

		put( SWT.DEL, SWT.DEL, NO_MASK, ActionFactory.DELETE.getId( ) );

		tableDelgate = new TableCellKeyDelegate( viewer );
	}

	/**
	 * Bounds actions with key events
	 * 
	 * @param character
	 * @param keyCode
	 * @param stateMask
	 * @param actionID
	 */
	public void put( char character, int keyCode, int stateMask, String actionID )
	{
		IAction action = actionRegistry.getAction( actionID );
		if ( action != null )
		{
			put( KeyStroke.getReleased( character, keyCode, stateMask ), action );
		}
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public boolean keyPressed( KeyEvent event )
	{
		GraphicalEditPart part = getFocusEditPart( );

		/**
		 * Hacks Table Cell key behaviors.
		 */
		if ( part instanceof TableCellEditPart )
		{
			return tableCellKeyPressed( event );
		}

		return super.keyPressed( event );
	}

	protected boolean tableCellKeyPressed( KeyEvent event )
	{
		return tableDelgate.keyPressed( event );
	}
}