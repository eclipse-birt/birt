/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.layout;

import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite;
import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerProvider;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Report graphical editor with ruler
 */
abstract public class ReportEditorWithRuler extends ReportEditorWithPalette {

	private EditorRulerProvider topRuler;
	private EditorRulerProvider leftRuler;
	private EditorRulerComposite rulerComp;

	/**
	 * Constructor
	 */
	public ReportEditorWithRuler() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 */
	public ReportEditorWithRuler(IEditorPart parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.
	 * GraphicalEditorWithFlyoutPalette#createGraphicalViewer(org.eclipse.swt.
	 * widgets.Composite)
	 */
	protected void createGraphicalViewer(Composite parent) {
		// bidi_hcg start
		/*
		 * If Bidi support is enabled - check model orientation and set the view
		 * orientation accordingly
		 */

		if (getModel().isDirectionRTL())
			rulerComp = new EditorRulerComposite(parent, SWT.RIGHT_TO_LEFT);
		else
			rulerComp = new EditorRulerComposite(parent, SWT.LEFT_TO_RIGHT);

//		else
//		// bidi_hcg end
//			rulerComp = new EditorRulerComposite( parent, SWT.NONE );
		super.createGraphicalViewer(rulerComp);
		if (Constants.OS_LINUX.equalsIgnoreCase(Platform.getOS())) {// Linux and Windows has different color
																	// behavior.Add OS judgment to set rulerCompsite
																	// background color.
			rulerComp.setBackground(ColorManager.getColor(240, 240, 240));
		}
		rulerComp.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer(), getModel());

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
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.
	 * AbstractReportDesigner#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		createRulers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.
	 * GraphicalEditorWithFlyoutPalette#getGraphicalControl()
	 */
	protected Control getGraphicalControl() {
		return rulerComp;
	}

	private void createRulers() {
		// Ruler properties
		if (topRuler == null) {
			topRuler = new EditorRulerProvider(getModel(), true);
		}

		getGraphicalViewer().setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, topRuler);

		if (leftRuler == null) {
			leftRuler = new EditorRulerProvider(getModel(), false);
		}
		getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, leftRuler);
		getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.valueOf(true));

	}

	@Override
	protected void setModel(ModuleHandle model) {
		super.setModel(model);

		if (model != null) {
			rulerComp.resetReportDesignHandle(model);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		super.dispose();
		rulerComp = null;
		topRuler = null;
		leftRuler = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditor#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		super.selectionChanged(part, selection);

		IEditorPart report = getSite().getPage().getActiveEditor();
		if (report != null) {
			updateActions(getSelectionActions());
		}
	}
}
