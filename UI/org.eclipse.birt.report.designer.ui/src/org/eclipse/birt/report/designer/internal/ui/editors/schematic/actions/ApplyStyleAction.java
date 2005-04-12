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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.command.StyleException;

/**
 * Applies style to selected elements.
 */

public class ApplyStyleAction extends DynamicItemAction
{

	/** action ID */
	public static final String ID = "ApplyStyleAction"; //$NON-NLS-1$

	private static final String STACK_MSG_APPLY_STYLE = Messages.getString( "ApplyStyleAction.stackMsg.applyStyle" ); //$NON-NLS-1$

	private SharedStyleHandle handle;

	private List selectionHandles;

	/**
	 * @param handle
	 */
	public ApplyStyleAction( SharedStyleHandle handle )
	{
		this.handle = handle;
		setId( ID );
		if ( handle == null )
		{
			setText( Messages.getString( "ApplyStyleAction.actionLabel.none" ) ); //$NON-NLS-1$
		}
		else
		{
			setText( DEUtil.getEscapedMenuItemText( handle.getDisplayLabel( ) ) );
		}
	}

	/**
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled( )
	{
		List handles = getElementHandles( );
		if ( handles.isEmpty( ) )
			return false;
		for ( int i = 0; i < handles.size( ); i++ )
		{
			if ( !( handles.get( i ) instanceof ReportElementHandle ) )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getActivityStack( );
		stack.startTrans( STACK_MSG_APPLY_STYLE );

		try
		{
			List handles = getElementHandles( );
			for ( int i = 0; i < handles.size( ); i++ )
			{
				( (DesignElementHandle) handles.get( i ) ).setStyle( isChecked( )
						? handle : null );
			}
			stack.commit( );
		}
		catch ( StyleException e )
		{
			stack.rollbackAll( );
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Gets models of selected elements
	 *  
	 */
	protected List getElementHandles( )
	{
		if ( selectionHandles == null )
			selectionHandles = DNDUtil.editPart2Model( getSelection( ) )
					.toList( );
		return selectionHandles;
	}
}