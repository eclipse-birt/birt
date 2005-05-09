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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.actions.ActionFactory;

/**
 * The factory to create all global actions
 */

public class GlobalActionFactory
{

	public final static String COPY = ActionFactory.COPY.getId( );
	public final static String CUT = ActionFactory.CUT.getId( );
	public final static String PASTE = ActionFactory.PASTE.getId( );
	public final static String DELETE = ActionFactory.DELETE.getId( );
	public final static String UNDO = ActionFactory.UNDO.getId( );
	public final static String REDO = ActionFactory.REDO.getId( );

	public final static String[] GLOBAL_SELECTION_ACTIONS = {
			COPY, CUT, PASTE, DELETE
	};

	public final static String[] GLOBAL_STACK_ACTIONS = {
			UNDO, REDO
	};

	public static IAction createSelectionAction( String id,
			ISelectionProvider provider )
	{
		Assert.isNotNull( id );
		Assert.isNotNull( provider );
		if ( COPY.equals( id ) )
		{
			return new GlobalCopyAction( provider );
		}
		else if ( CUT.equals( id ) )
		{
			return new GlobalCutAction( provider );
		}
		else if ( PASTE.equals( id ) )
		{
			return new GlobalPasteAction( provider );
		}
		else if ( DELETE.equals( id ) )
		{
			return new GlobalDeleteAction( provider );
		}
		return null;
	}

	public static IAction createStackAction( String id, CommandStack stack )
	{
		Assert.isNotNull( id );
		Assert.isNotNull( stack );
		GlobalStackActionEntry entry = (GlobalStackActionEntry) stackActionEntrys.get( stack );
		if ( entry == null )
		{
			entry = new GlobalStackActionEntry( stack );
			stackActionEntrys.put( stack, entry );
		}
		return entry.getAction( id );
	}

	private static Map stackActionEntrys = new HashMap( );

	private static class GlobalStackActionEntry
	{

		private GlobalUndoAction undoAction = null;
		private GlobalRedoAction redoAction = null;
		private CommandStack stack;

		public GlobalStackActionEntry( CommandStack stack )
		{
			this.stack = stack;
		}

		public GlobalStackAction getAction( String id )
		{
			GlobalStackAction action = null;
			if ( UNDO.equals( id ) )
			{
				if ( undoAction == null )
				{
					undoAction = new GlobalUndoAction( stack );
				}
				action = undoAction;
			}
			else if ( REDO.equals( id ) )
			{
				if ( redoAction == null )
				{
					redoAction = new GlobalRedoAction( stack );
				}
				action = redoAction;
			}
			if ( action != null )
			{
				action.update( );
			}
			return action;
		}
	}

}