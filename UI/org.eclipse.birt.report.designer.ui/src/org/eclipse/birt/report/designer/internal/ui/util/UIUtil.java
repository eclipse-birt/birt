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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.ui.editors.ReportEditor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Utility class for UI related routines.
 */

public class UIUtil
{

	/**
	 * Returns if current active editor is reportEditor.
	 * 
	 * @return Returns if current active editor is reportEditor.
	 */
	public static boolean isReportEditorActivated( )
	{
		return getActiveEditor( ) != null;
	}

	/**
	 * Returns the current active report editor.
	 * 
	 * @return Returns the current active report editor, or null if no report
	 *         editor is active.
	 */
	public static ReportEditor getActiveEditor( )
	{
		IWorkbenchWindow window = PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( );

		if ( window != null )
		{
			IWorkbenchPage pg = window.getActivePage( );

			if ( pg != null )
			{
				IEditorPart editor = pg.getActiveEditor( );

				if ( editor != null
						&& editor.getEditorInput( ) instanceof FileEditorInput )
				{
					if ( editor instanceof ReportEditor )
					{
						return (ReportEditor) editor;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the default shell used by dialogs
	 * 
	 * @return Returns the active shell of the current display
	 */
	public static Shell getDefaultShell( )
	{
		return PlatformUI.getWorkbench( ).getDisplay( ).getActiveShell( );
	}

}