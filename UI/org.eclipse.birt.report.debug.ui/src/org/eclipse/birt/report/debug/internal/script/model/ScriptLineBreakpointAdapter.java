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

package org.eclipse.birt.report.debug.internal.script.model;

import org.eclipse.birt.report.debug.internal.ui.script.util.ScriptDebugUtil;
import org.eclipse.birt.report.designer.ui.editor.script.DecoratedScriptEditor;
import org.eclipse.birt.report.designer.ui.editors.IReportScriptLocation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Adapter to create the brea point.
 */
public class ScriptLineBreakpointAdapter implements IToggleBreakpointsTarget
{

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleLineBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleLineBreakpoints( IWorkbenchPart part,
			ISelection selection )
	{
		return getEditor( part ) != null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleMethodBreakpoints( IWorkbenchPart part,
			ISelection selection )
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleWatchpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleWatchpoints( IWorkbenchPart part,
			ISelection selection )
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleLineBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleLineBreakpoints( IWorkbenchPart part, ISelection selection )
			throws CoreException
	{
		ITextEditor textEditor = getEditor( part );
		if ( textEditor != null )
		{
			IResource resource = (IResource) textEditor.getEditorInput( )
					.getAdapter( IResource.class );
			if ( resource == null )
			{
				resource = ScriptDebugUtil.getDefaultResource( );
			}

			ITextSelection textSelection = (ITextSelection) selection;
			int lineNumber = textSelection.getStartLine( );
			IReportScriptLocation location = (IReportScriptLocation) textEditor.getAdapter( IReportScriptLocation.class );
			if ( location == null )
			{
				return;
			}
			if ( location.getLineNumber( ) > 0 )
			{
				lineNumber = location.getLineNumber( );
			}
			IBreakpoint[] breakpoints = DebugPlugin.getDefault( )
					.getBreakpointManager( )
					.getBreakpoints( IScriptConstants.SCRIPT_DEBUG_MODEL );
			for ( int i = 0; i < breakpoints.length; i++ )
			{
				IBreakpoint breakpoint = breakpoints[i];
				if ( resource.equals( breakpoint.getMarker( ).getResource( ) ) )
				{
					if ( ( (ScriptLineBreakpoint) breakpoint ).getLineNumber( ) == ( lineNumber + 1 )
							&& ( (ScriptLineBreakpoint) breakpoint ).getFileName( )
									.equals( location.getReportFileName( ) )
							&& ( (ScriptLineBreakpoint) breakpoint ).getSubName( )
									.equals( location.getID( ) ) )
					{
						breakpoint.delete( );
						return;
					}
				}
			}
			// create line breakpoint (doc line numbers start at 0)
			ScriptLineBreakpoint lineBreakpoint = new ScriptLineBreakpoint( resource,
					location.getReportFileName( ),
					location.getID( ),
					lineNumber + 1 );
			lineBreakpoint.setDisplayName( location.getDisplayName( ) );
			DebugPlugin.getDefault( )
					.getBreakpointManager( )
					.addBreakpoint( lineBreakpoint );
		}

	}


	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleMethodBreakpoints( IWorkbenchPart part,
			ISelection selection ) throws CoreException
	{
		// don't support
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleWatchpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleWatchpoints( IWorkbenchPart part, ISelection selection )
			throws CoreException
	{
		// don't support
	}

	private ITextEditor getEditor( IWorkbenchPart part )
	{
		if ( part instanceof DecoratedScriptEditor )
		{
			return (ITextEditor) part;
		}
		return null;
	}
}
