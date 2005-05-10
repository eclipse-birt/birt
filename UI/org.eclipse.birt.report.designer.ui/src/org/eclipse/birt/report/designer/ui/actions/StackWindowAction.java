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

package org.eclipse.birt.report.designer.ui.actions;

import java.text.MessageFormat;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;
import org.eclipse.birt.report.designer.ui.editors.ReportEditor;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Undo/Redo action for contribution of toolbar or menu.
 */

public abstract class StackWindowAction
		implements
			IWorkbenchWindowActionDelegate
{

	private ActivityStackListener commandStackListener = new ActivityStackListener( ) {

		public void stackChanged( ActivityStackEvent event )
		{
			setAction( iaction, canDo( ) );
		}
	};
	private CommandStack commandStack;
	private ReportDesignHandle designHandle;
	private IAction iaction;

	protected String getLabelForCommand( Command command )
	{
		if ( command == null )
			return "";//$NON-NLS-1$
		if ( command.getLabel( ) == null )
			return "";//$NON-NLS-1$
		return command.getLabel( );
	}

	/**
	 * Returns command stack listener.
	 */
	public ActivityStackListener getCommandStackListener( )
	{
		return commandStackListener;
	}

	/**
	 *  
	 */
	public StackWindowAction( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose( )
	{
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack( );
		if ( stack != null )
		{
			stack.removeCommandStackListener( getCommandStackListener( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init( IWorkbenchWindow window )
	{
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack( );
		if ( stack != null )
		{
			designHandle = getDesignHandle( );

			stack.setActivityStack( getDesignHandle( ).getCommandStack( ) );
			stack.addCommandStackListener( getCommandStackListener( ) );
		}
	}

	private void resetCommandListener( )
	{
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack( );
		if ( stack != null && getDesignHandle( ) != designHandle )
		{
			designHandle = getDesignHandle( );

			stack.removeCommandStackListener( getCommandStackListener( ) );
			stack.setActivityStack( getDesignHandle( ).getCommandStack( ) );
			stack.addCommandStackListener( getCommandStackListener( ) );
		}
	}

	protected CommandStack getCommandStack( )
	{
		if ( commandStack == null )
		{
			commandStack = new WrapperCommandStack( );
		}
		return commandStack;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run( IAction action )
	{
		if ( canDo( ) )
		{
			doStack( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged( IAction action, ISelection selection )
	{
		iaction = action;
		changeEnabled( action );
		resetCommandListener( );
	}

	private void changeEnabled( IAction action )
	{
		IWorkbenchWindow window = PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( );
		IWorkbenchPage[] pages = window.getPages( );

		boolean isEnabled = false;
		for ( int i = 0; i < pages.length; i++ )
		{
			IEditorReference[] refs = pages[i].getEditorReferences( );

			for ( int j = 0; j < refs.length; j++ )
			{
				IEditorPart editor = refs[j].getEditor( false );

				if ( editor != null
						&& editor.getEditorInput( ) instanceof FileEditorInput )
				{
					if ( editor instanceof ReportEditor )
					{
						isEnabled = canDo( );
						break;
					}
				}
			}
		}
		setAction( action, isEnabled );
	}

	private void setAction( IAction action, boolean isEnabled )
	{
		action.setEnabled( isEnabled );
		changeLabel( action );
	}

	protected ReportDesignHandle getDesignHandle( )
	{
		return SessionHandleAdapter.getInstance( ).getReportDesignHandle( );
	}

	abstract protected boolean canDo( );

	abstract protected void doStack( );

	abstract protected void changeLabel( IAction action );

	public static class UndoWindowAction extends StackWindowAction
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#canDo()
		 */
		protected boolean canDo( )
		{
			return getDesignHandle( ).getCommandStack( ).canUndo( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#doStack()
		 */
		protected void doStack( )
		{
			getDesignHandle( ).getCommandStack( ).undo( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#changeLabel(org.eclipse.jface.action.IAction)
		 */
		protected void changeLabel( IAction action )
		{
			Command undoCmd = getCommandStack( ).getUndoCommand( );
			action.setToolTipText( MessageFormat.format( GEFMessages.UndoAction_Tooltip,
					new Object[]{
						getLabelForCommand( undoCmd )
					} )
					.trim( ) );
		}
	}

	public static class RedoWindowAction extends StackWindowAction
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#canDo()
		 */
		protected boolean canDo( )
		{
			return getDesignHandle( ).getCommandStack( ).canRedo( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#doStack()
		 */
		protected void doStack( )
		{
			getDesignHandle( ).getCommandStack( ).redo( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.actions.StackWindowAction#changeLabel(org.eclipse.jface.action.IAction)
		 */
		protected void changeLabel( IAction action )
		{
			Command redoCmd = getCommandStack( ).getRedoCommand( );
			action.setToolTipText( MessageFormat.format( GEFMessages.RedoAction_Tooltip,
					new Object[]{
						getLabelForCommand( redoCmd )
					} )
					.trim( ) );
		}
	}
}