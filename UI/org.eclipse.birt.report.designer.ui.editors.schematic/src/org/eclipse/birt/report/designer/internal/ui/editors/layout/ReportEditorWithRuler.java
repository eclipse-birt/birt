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

package org.eclipse.birt.report.designer.internal.ui.editors.layout;

import org.eclipse.birt.report.designer.core.util.mediator.IColleague;
import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite;
import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerProvider;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Report graphical editor with ruler
 */
abstract public class ReportEditorWithRuler extends
		ReportEditorWithPalette implements IColleague
{

	private EditorRulerProvider topRuler;
	private EditorRulerProvider leftRuler;
	private EditorRulerComposite rulerComp;

	/**
	 * Constructor
	 */
	public ReportEditorWithRuler( )
	{
		super( );
	}

	/**
	 * Constructor
	 * @param parent
	 */
	public ReportEditorWithRuler( IEditorPart parent )
	{
		super( parent );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette#createGraphicalViewer(org.eclipse.swt.widgets.Composite)
	 */
	protected void createGraphicalViewer( Composite parent )
	{
		rulerComp = new EditorRulerComposite( parent, SWT.NONE );
		super.createGraphicalViewer( rulerComp );
		rulerComp.setGraphicalViewer( (ScrollingGraphicalViewer) getGraphicalViewer( ) );

		// addAction( new ToggleRulerVisibilityAction( getGraphicalViewer( ) ) {
		//
		// public boolean isChecked( )
		// {
		// return ( (LayoutEditor) editingDomainEditor ).getRulerState( );
		// }
		//
		// public void run( )
		// {
		// // if ( getButtonPane( ) != null )
		// // {
		// // getButtonPane( ).setButtonSelection( ButtonPaneComposite.BUTTON3,
		// // !isChecked( ) );
		// // }
		// getGraphicalViewer( ).setProperty(
		// RulerProvider.PROPERTY_RULER_VISIBILITY,
		// Boolean.valueOf( !isChecked( ) ) );
		// ( (LayoutEditor) editingDomainEditor ).setRulerState( !isChecked( ),
		// LayoutEditor.DESIGNER_INDEX );
		// }
		// } );
		//
		// addAction( new ToggleMarginVisibilityAction( getGraphicalViewer( ) )
		// );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.AbstractReportDesigner#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer( )
	{
		super.configureGraphicalViewer( );
		createRulers( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette#getGraphicalControl()
	 */
	protected Control getGraphicalControl( )
	{
		return rulerComp;
	}

	private void createRulers( )
	{
		// Ruler properties
		if ( topRuler == null )
		{
			topRuler = new EditorRulerProvider( null, true );
		}

		getGraphicalViewer( ).setProperty( RulerProvider.PROPERTY_HORIZONTAL_RULER,
				topRuler );

		if ( leftRuler == null )
		{
			leftRuler = new EditorRulerProvider( null, false );
		}
		getGraphicalViewer( ).setProperty( RulerProvider.PROPERTY_VERTICAL_RULER,
				leftRuler );
		getGraphicalViewer( ).setProperty( RulerProvider.PROPERTY_RULER_VISIBILITY,
				new Boolean( true ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose( )
	{
		super.dispose( );
		rulerComp = null;
		topRuler = null;
		leftRuler = null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged( IWorkbenchPart part, ISelection selection )
	{
		super.selectionChanged( part, selection );

		IEditorPart report = getSite( ).getPage( ).getActiveEditor( );
		if ( report != null )
		{
			updateActions( getSelectionActions( ) );
		}
	}
}
