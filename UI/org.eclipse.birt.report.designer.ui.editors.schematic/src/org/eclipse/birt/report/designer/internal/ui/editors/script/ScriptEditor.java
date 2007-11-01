/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.script;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.StatusTextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;

/**
 * The text editor for script.
 */
public class ScriptEditor extends StatusTextEditor implements IScriptEditor
{

	/**
	 * The javascript syntax context, provides methods to access avaible Type
	 * meta-data.
	 */
	private final JSSyntaxContext context = new JSSyntaxContext( );

	/** The editor input for javascript. */
	private IEditorInput input = new JSEditorInput( null );

	/** The action registry */
	private ActionRegistry actionRegistry = null;

	/**
	 * Constracts an script editor.
	 */
	public ScriptEditor( )
	{
		this( null );
	}

	/**
	 * Constracts an script editor with the specfied script.
	 * 
	 * @param script
	 *            the script to edit
	 */
	public ScriptEditor( String script )
	{
		super( );
		setSourceViewerConfiguration( new JSSourceViewerConfiguration( context ) );
		setDocumentProvider( new JSDocumentProvider( ) );
		setScript( script );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.StatusTextEditor#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl( Composite parent )
	{
		setInput( input );
		super.createPartControl( parent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getSite()
	 */
	public IWorkbenchPartSite getSite( )
	{
		IWorkbenchPartSite site = super.getSite( );

		if ( site == null )
		{
			site = PlatformUI.getWorkbench( )
					.getActiveWorkbenchWindow( )
					.getActivePage( )
					.getActiveEditor( )
					.getSite( );
		}
		return site;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#createActions()
	 */
	protected void createActions( )
	{
		super.createActions( );

		IAction contentAssistAction = new TextOperationAction( Messages.getReportResourceBundle( ),
				"ContentAssistProposal_", this, ISourceViewer.CONTENTASSIST_PROPOSALS, true );//$NON-NLS-1$

		contentAssistAction.setActionDefinitionId( ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS );
		setAction( "ContentAssistProposal", contentAssistAction );//$NON-NLS-1$
		// TODO: rewirte those actions
		// Add page actions
		// Action action = LayoutPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		//		
		// action = NormalPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		//
		// action = MasterPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		//
		// action = PreviewPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		//
		// action = CodePageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#setAction(java.lang.String,
	 *      org.eclipse.jface.action.IAction)
	 */
	public void setAction( String actionID, IAction action )
	{
		super.setAction( actionID, action );
		if ( action.getId( ) == null )
		{
			action.setId( actionID );
		}
		getActionRegistry( ).registerAction( action );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#getActionRegistry()
	 */
	public ActionRegistry getActionRegistry( )
	{
		if ( actionRegistry == null )
		{
			actionRegistry = new ActionRegistry( );
		}
		return actionRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#getViewer()
	 */
	public ISourceViewer getViewer( )
	{
		return getSourceViewer( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#getScript()
	 */
	public String getScript( )
	{
		IDocumentProvider provider = getDocumentProvider( );
		String script = ""; //$NON-NLS-1$

		if ( provider != null )
		{
			IDocument document = provider.getDocument( getEditorInput( ) );

			if ( document != null )
			{
				script = document.get( );
			}
		}
		return script;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#setScript(java.lang.String)
	 */
	public void setScript( String script )
	{
		IDocumentProvider provider = getDocumentProvider( );

		if ( provider != null )
		{
			IDocument document = provider.getDocument( input );

			if ( document != null )
			{
				document.set( script );
				return;
			}
		}
		input = new JSEditorInput( script );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#getContext()
	 */
	public JSSyntaxContext getContext( )
	{
		return context;
	}
}
