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

import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.palette.DesignerPaletteFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;

/**
 * <p>
 * Report design layout graphical editor.
 * </p>
 */
public abstract class ReportLayoutEditor extends ReportEditorWithRuler {

	private IEditorPart parentEditorPart;

	public ReportLayoutEditor() {
		super();
	}

	/**
	 * @param parent
	 */
	public ReportLayoutEditor(IEditorPart parent) {
		super(parent);
		this.parentEditorPart = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.
	 * GraphicalEditorWithFlyoutPalette
	 * #performRequest(org.eclipse.birt.report.designer
	 * .core.util.mediator.request.ReportRequest)
	 */
	public void performRequest(IMediatorRequest request) {
		if (ReportRequest.OPEN_EDITOR.equals(request.getType())
				&& (((ReportRequest) request).getSelectionModelList().size() == 1)
				&& ((ReportRequest) request).getSelectionModelList().get(0) instanceof SlotHandle) {
			SlotHandle slt = (SlotHandle) (((ReportRequest) request).getSelectionModelList().get(0));
			if (slt.getSlotID() == ReportDesignHandle.BODY_SLOT) {
				handleOpenDesigner((ReportRequest) request);
			}
			return;
		}
		super.performRequest(request);
	}

	/**
	 * @param request
	 */
	private void handleOpenDesigner(ReportRequest request) {
		// if ( ( (LayoutEditor) editingDomainEditor ).isVisible( ) )
		// {
		// ( (LayoutEditor) editingDomainEditor ).setActivePage( 0 );
		// ( (LayoutEditor) editingDomainEditor ).pageChange( 0 );
		// }
	}

	/**
	 * Returns an object which is an instance of the given class associated with
	 * this object. Returns <code>null</code> if no such object can be found.
	 * 
	 * @param adapter the adapter class to look up
	 * @return a object castable to the given class, or <code>null</code> if this
	 *         object does not have an adapter for the given class
	 */
	public Object getAdapter(Class adapter) {
		// if ( adapter == DataViewPage.class )
		// {
		// // TODO garbage code
		// // important: this code is for fixing a bug in emergency.
		// // Must shift to mediator structure after R1
		// DataViewPage page = (DataViewPage) super.getAdapter( adapter );
		// if ( page == null )
		// {
		// return null;
		// }
		// return page;
		// }

		return super.getAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.schematic.layout.
	 * AbstractReportGraphicalEditorWithFlyoutPalette#getPaletteRoot()
	 */
	protected PaletteRoot getPaletteRoot() {
		if (paletteRoot == null) {
			paletteRoot = DesignerPaletteFactory.createPalette();
		}
		return paletteRoot;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.
	 * GraphicalEditorWithFlyoutPalette#getMultiPageEditor()
	 */
	protected IEditorPart getMultiPageEditor() {
		return parentEditorPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.layout.
	 * ReportEditorWithPalette#createActions()
	 */
	protected void createActions() {
		super.createActions();
		IAction action = new SelectAllAction(this);
		getActionRegistry().registerAction(action);
	}
}