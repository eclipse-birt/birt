/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Actuate Corporation - Copy and change to fit BIRT requirement 
 *******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.editors.script;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionTreeSupport;
import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportScriptFormPage;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.Page;

/**
 * The default page for the PaletteView that works in conjunction with a
 * PaletteViewerProvider.
 */
public class TreeViewPalettePage extends Page implements
		PalettePage,
		IAdaptable
{

	/** Tool tip separator between Usage and Description */
	// private static final String TOOL_TIP_SEP = ": "; //$NON-NLS-1$
	/**
	 * The PaletteViewerProvider that is used to create the PaletteViewer
	 */
	protected PaletteViewerProvider provider;

	/**
	 * The PaletteViewer created for this page
	 */
	protected Tree tree;

	private ExpressionTreeSupport treeCommon;

	/**
	 * Constructor
	 * 
	 */
	public TreeViewPalettePage( )
	{
		treeCommon = new ExpressionTreeSupport( );
	}

	/**
	 * Creates the palette viewer and its control.
	 * 
	 * @see Page#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl( Composite parent )
	{
		if(getViewer()==null)
		{
			return;
		}
		tree = new Tree( parent, SWT.NONE );
		treeCommon.setTree( tree );
		treeCommon.setExpressionViewer( getViewer( ) );

		treeCommon.createDefaultExpressionTree( SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getVisibleDataSets( ) );

		treeCommon.addMouseTrackListener( );
		treeCommon.addMouseListener( );
		treeCommon.addDragSupportToTree( );
		treeCommon.addDropSupportToViewer( );

		// Add tool tips
		tree.setToolTipText( "" ); //$NON-NLS-1$
	}

	/**
	 * get tool tip text from a string array that contains the text, usage and
	 * description
	 * 
	 * @param tuple
	 * @return
	 */
	// private static String getToolTip( String[] tuple )
	// {
	// return tuple[1] + TOOL_TIP_SEP + tuple[2];
	// }
	/**
	 * Releases the palette viewer from the edit domain
	 * 
	 * @see Page#dispose()
	 */
	public void dispose( )
	{
		super.dispose( );
		tree.dispose( );
		treeCommon.dispose( );
	}

	/**
	 * @see IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class adapter )
	{
		return null;
	}

	/**
	 * @return the palette viewer's control
	 * @see Page#getControl()
	 */
	public Control getControl( )
	{
		return tree;
	}

	/**
	 * Sets focus on the palette's control
	 * 
	 * @see Page#setFocus()
	 */
	public void setFocus( )
	{
		tree.setFocus( );
	}

	public ExpressionTreeSupport getSupport( )
	{
		return this.treeCommon;
	}

	private SourceViewer getViewer( )
	{
		IEditorPart activeEditor = PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( )
				.getActivePage( )
				.getActiveEditor( );
		IFormPage page = null;
		if ( activeEditor instanceof MultiPageReportEditor )
		{

			page = ( (MultiPageReportEditor) activeEditor ).getCurrentPageInstance( );
		}
		else if ( activeEditor instanceof IReportEditor )
		{
			IEditorPart editor = ( (IReportEditor) activeEditor ).getEditorPart( );
			if ( editor instanceof MultiPageReportEditor )
			{
				page = ( (MultiPageReportEditor) editor ).getCurrentPageInstance( );					
			}
		}
		if ( page instanceof ReportScriptFormPage )
		{
			if ( ( (ReportScriptFormPage) page ).getScriptEditor( ) instanceof JSEditor )
			{
				return ( (JSEditor) ( (ReportScriptFormPage) page ).getScriptEditor( ) ).getViewer( );
			}
		}
		return null;
	}

}