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

package org.eclipse.birt.report.designer.ui;

import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerView;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeView;
import org.eclipse.birt.report.designer.ui.views.data.DataView;
import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * JRPPerspective generates the initial page layout and visible action set for
 * birt.
 * 
 * 
 */
public class ReportPerspective implements IPerspectiveFactory
{

	public static final String BIRT_REPORT_PERSPECTIVE = "org.eclipse.birt.report.designer.ui.ReportPerspective"; //$NON-NLS-1$

	public static final String NEW_REPORT_ID = "org.eclipse.birt.report.designer.ui.ide.wizards.NewReportWizard";//$NON-NLS-1$

	public static final String NEW_TEMPLATE_ID = "org.eclipse.birt.report.designer.ui.ide.wizards.NewTemplateWizard";//$NON-NLS-1$

	private IReportPerspectiveExtra extra;

	/**
	 * Constructs a new Default layout engine.
	 */
	public ReportPerspective( )
	{
		super( );

		Object adapter = ElementAdapterManager.getAdapter( this,
				IReportPerspectiveExtra.class );

		if ( adapter instanceof IReportPerspectiveExtra )
		{
			extra = (IReportPerspectiveExtra) adapter;
		}
	}

	/**
	 * Defines the initial layout for a perspective.
	 * 
	 * Implementors of this method may add additional views to a perspective.
	 * The perspective already contains an editor folder with
	 * <code>ID = ILayoutFactory.ID_EDITORS</code>. Add additional views to the
	 * perspective in reference to the editor folder.
	 * 
	 * This method is only called when a new perspective is created. If an old
	 * perspective is restored from a persistence file then this method is not
	 * called.
	 * 
	 * @param layout
	 *            the factory used to add views to the perspective
	 */
	public void createInitialLayout( IPageLayout layout )
	{
		defineLayout( layout );
		defineActions( layout );
	}

	private void addNewWizardShortcut( IPageLayout layout, String id )
	{
		if ( extra == null || !extra.obsoleteNewWizardShortcut( id ) )
		{
			layout.addNewWizardShortcut( id );
		}
	}

	private void addShowViewShortcut( IPageLayout layout, String id )
	{
		if ( extra == null || !extra.obsoleteShowViewShortcut( id ) )
		{
			layout.addShowViewShortcut( id );
		}
	}

	/**
	 * Defines the Actions
	 */
	private void defineActions( IPageLayout layout )
	{
		// Add "new wizards".
		addNewWizardShortcut( layout, "org.eclipse.ui.wizards.new.folder" );//$NON-NLS-1$
		addNewWizardShortcut( layout, NEW_REPORT_ID );
		addNewWizardShortcut( layout, NEW_TEMPLATE_ID );

		// Add "show views".
		addShowViewShortcut( layout, IPageLayout.ID_RES_NAV );
		addShowViewShortcut( layout, IPageLayout.ID_OUTLINE );
		addShowViewShortcut( layout, PaletteView.ID );
		addShowViewShortcut( layout, AttributeView.ID );
		addShowViewShortcut( layout, DataView.ID );
		addShowViewShortcut( layout, LibraryExplorerView.ID );
		addShowViewShortcut( layout, IPageLayout.ID_PROP_SHEET );
		addShowViewShortcut( layout, IPageLayout.ID_PROBLEM_VIEW );
		// Error log view is not activated by default for better usability
		// addShowViewShortcut( layout, "org.eclipse.pde.runtime.LogView" );
		// //$NON-NLS-1$

		layout.addPerspectiveShortcut( BIRT_REPORT_PERSPECTIVE );

		if ( extra != null )
		{
			String[] ids = extra.getExtraNewWizardShortcut( );

			if ( ids != null )
			{
				for ( String id : ids )
				{
					layout.addNewWizardShortcut( id );
				}
			}

			ids = extra.getExtraShowViewShortcut( );

			if ( ids != null )
			{
				for ( String id : ids )
				{
					layout.addShowViewShortcut( id );
				}
			}
		}
	}

	private void addLayoutView( IFolderLayout folder, int layoutPos, String id )
	{
		if ( extra == null || !extra.obsoleteLayoutView( layoutPos, id ) )
		{
			folder.addView( id );
		}
	}

	/**
	 * Defines the initial layout for a page.
	 */
	private void defineLayout( IPageLayout layout )
	{
		// Editors are placed for free.
		String editorArea = layout.getEditorArea( );
		// Top left.
		IFolderLayout topLeft = layout.createFolder( "topLeft", IPageLayout.LEFT, (float) 0.26, editorArea );//$NON-NLS-1$

		addLayoutView( topLeft,
				IReportPerspectiveExtra.LAYOUT_TOP_LEFT,
				PaletteView.ID );
		addLayoutView( topLeft,
				IReportPerspectiveExtra.LAYOUT_TOP_LEFT,
				DataView.ID );
		addLayoutView( topLeft,
				IReportPerspectiveExtra.LAYOUT_TOP_LEFT,
				LibraryExplorerView.ID );

		// Bottom left.
		IFolderLayout bottomLeft = layout.createFolder( "bottomLeft", IPageLayout.BOTTOM, (float) 0.50,//$NON-NLS-1$
				"topLeft" );//$NON-NLS-1$
		addLayoutView( bottomLeft,
				IReportPerspectiveExtra.LAYOUT_BOTTOM_LEFT,
				IPageLayout.ID_RES_NAV );
		addLayoutView( bottomLeft,
				IReportPerspectiveExtra.LAYOUT_BOTTOM_LEFT,
				IPageLayout.ID_OUTLINE );

		// Bottom right.
		IFolderLayout bottomRight = layout.createFolder( "bottomRight", IPageLayout.BOTTOM, (float) 0.66, editorArea );//$NON-NLS-1$
		addLayoutView( bottomRight,
				IReportPerspectiveExtra.LAYOUT_BOTTOM_RIGHT,
				AttributeView.ID );
		addLayoutView( bottomRight,
				IReportPerspectiveExtra.LAYOUT_BOTTOM_RIGHT,
				IPageLayout.ID_PROBLEM_VIEW );
		// Error log view is not activated by default for better usability
		// addLayoutView( bottomRight,
		// IReportPerspectiveExtra.LAYOUT_BOTTOM_RIGHT,
		// "org.eclipse.pde.runtime.LogView" ); //$NON-NLS-1$

		if ( extra != null )
		{
			String[] ids = extra.getExtraLayoutView( IReportPerspectiveExtra.LAYOUT_TOP_LEFT );

			if ( ids != null )
			{
				for ( String id : ids )
				{
					topLeft.addView( id );
				}
			}

			ids = extra.getExtraLayoutView( IReportPerspectiveExtra.LAYOUT_BOTTOM_LEFT );

			if ( ids != null )
			{
				for ( String id : ids )
				{
					bottomLeft.addView( id );
				}
			}

			ids = extra.getExtraLayoutView( IReportPerspectiveExtra.LAYOUT_BOTTOM_RIGHT );

			if ( ids != null )
			{
				for ( String id : ids )
				{
					bottomRight.addView( id );
				}
			}

			ids = extra.getExtraLayoutPlaceholder( IReportPerspectiveExtra.LAYOUT_TOP_LEFT );

			if ( ids != null )
			{
				for ( String id : ids )
				{
					topLeft.addPlaceholder( id );
				}
			}

			ids = extra.getExtraLayoutPlaceholder( IReportPerspectiveExtra.LAYOUT_BOTTOM_LEFT );

			if ( ids != null )
			{
				for ( String id : ids )
				{
					bottomLeft.addPlaceholder( id );
				}
			}

			ids = extra.getExtraLayoutPlaceholder( IReportPerspectiveExtra.LAYOUT_BOTTOM_RIGHT );

			if ( ids != null )
			{
				for ( String id : ids )
				{
					bottomRight.addPlaceholder( id );
				}
			}
		}
	}
}