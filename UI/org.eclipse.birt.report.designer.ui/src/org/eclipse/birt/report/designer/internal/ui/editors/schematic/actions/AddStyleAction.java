/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.ICommandParameterNameContants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Add style rule action
 */

public class AddStyleAction extends ContextSelectionAction {

	private static final String ACTION_MSG_ADD_STYLE_RULE = Messages.getString("AddStyleAction.actionMsg.addStyleRule"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "AddStyleAction"; //$NON-NLS-1$

	private AbstractThemeHandle themeHandle;

	/**
	 * Contructor
	 *
	 * @param part
	 */
	public AddStyleAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_ADD_STYLE_RULE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	/**
	 * Runs action.
	 *
	 */
	@Override
	public void run() {
		// if ( Policy.TRACING_ACTIONS )
		// {
		// System.out.println( "Add Style rule action >> Run ..." );
		// //$NON-NLS-1$
		// }
		// CommandStack stack = getActiveCommandStack( );
		// stack.startTrans( STACK_MSG_ADD_STYLE );
		//
		// ModuleHandle reportDesignHandle = SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( );
		// // StyleHandle styleHandle = reportDesignHandle.getElementFactory( )
		// // .newStyle( null );
		// StyleHandle styleHandle = DesignElementFactory.getInstance(
		// reportDesignHandle )
		// .newStyle( null );
		//
		// try
		// {
		// StyleBuilder dialog = new StyleBuilder( PlatformUI.getWorkbench( )
		// .getDisplay( )
		// .getActiveShell( ), styleHandle, StyleBuilder.DLG_TITLE_NEW );
		// if ( dialog.open( ) == Window.OK )
		// {
		// if ( themeHandle != null )
		// {
		// themeHandle.getStyles( ).add( styleHandle );
		// }
		// else
		// {
		// reportDesignHandle.getStyles( ).add( styleHandle );
		// }
		// if ( !styleHandle.isPredefined( ) )
		// {
		// applyStyle( (SharedStyleHandle) styleHandle );
		// }
		// stack.commit( );
		// }
		// }
		// catch ( Exception e )
		// {
		// stack.rollbackAll( );
		// ExceptionHandler.handle( e );
		// }

		boolean hasTheme = false;

		if (themeHandle != null) {
			hasTheme = true;
			CommandUtils.setVariable(ICommandParameterNameContants.NEW_STYLE_THEME_HANDLE_NAME, themeHandle);
		} else {
			ModuleHandle module = SessionHandleAdapter.getInstance().getReportDesignHandle();

			if (module instanceof LibraryHandle) {
				ThemeHandle theme = ((LibraryHandle) module).getTheme();

				if (theme != null) {
					hasTheme = true;
					CommandUtils.setVariable(ICommandParameterNameContants.NEW_STYLE_THEME_HANDLE_NAME, theme);
				}
			}
		}

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.newStyleCommand"); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			if (hasTheme) {
				CommandUtils.removeVariable(ICommandParameterNameContants.NEW_STYLE_THEME_HANDLE_NAME);
			}
		}

	}

	public void setThemeHandle(AbstractThemeHandle themeHandle) {
		this.themeHandle = themeHandle;
	}

}
