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

package org.eclipse.birt.report.designer.ui.editors;

import java.util.List;

import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.lib.commands.SetCurrentEditModelCommand;
import org.eclipse.birt.report.designer.internal.lib.editparts.LibraryGraphicalPartFactory;
import org.eclipse.birt.report.designer.internal.lib.palette.LibraryTemplateTransferDropTargetListener;
import org.eclipse.birt.report.designer.internal.lib.providers.LibraryBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.lib.views.outline.LibraryOutlinePage;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.EditorBreadcrumb;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.ReportLayoutEditorBreadcrumb;
import org.eclipse.birt.report.designer.internal.ui.editors.layout.ReportEditorWithPalette;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.palette.DesignerPaletteFactory;
import org.eclipse.birt.report.designer.internal.ui.views.property.ReportPropertySheetPage;
import org.eclipse.birt.report.designer.ui.views.attributes.IAttributeViewPage;
import org.eclipse.birt.report.designer.ui.views.data.IDataViewPage;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * <p>
 * Report design graphical editor. This editor is the main editor of JRP ERD.
 * </p>
 * 
 * 
 */
public abstract class LibraryLayoutEditor extends ReportEditorWithPalette {

	private IEditorPart parentEditorPart;

	public LibraryLayoutEditor() {
		super();
	}

	/**
	 * @param parent
	 */
	public LibraryLayoutEditor(IEditorPart parent) {
		super(parent);
		this.parentEditorPart = parent;
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	public void performRequest(IMediatorRequest request) {
		ReportRequest rq = (ReportRequest) request;

		if (ReportRequest.OPEN_EDITOR.equals(request.getType()) && (rq.getSelectionModelList().size() == 1)
				&& rq.getSelectionModelList().get(0) instanceof SlotHandle) {
			SlotHandle slt = (SlotHandle) rq.getSelectionModelList().get(0);
			if (slt.getSlotID() == ReportDesignHandle.BODY_SLOT) {
				handleOpenDesigner(rq);
			}
			return;
		}

		super.performRequest(request);
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		super.selectionChanged(part, selection);

		IEditorPart report = getSite().getPage().getActiveEditor();
		if (report != null) {
			updateActions(getSelectionActions());
		}
	}

	protected void handleSelectionChange(ReportRequest request) {
		List list = request.getSelectionModelList();
		// should be change the reuqest.getSource() interface, recode the source
		// type.added by gao
		if ((request.getSource() instanceof LibraryOutlinePage || request.getSource() instanceof TableEditPart
				|| request.getSource() instanceof EditorBreadcrumb) && !isInContainer(list)) {
			int size = list.size();
			Object obj = null;
			if (size != 0) {
				obj = list.get(size - 1);
				SetCurrentEditModelCommand command = new SetCurrentEditModelCommand(obj);
				command.execute();
				return;
			}

		}
		super.handleSelectionChange(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.
	 * GraphicalEditorWithFlyoutPalette #handleCreateElement(org.eclipse.birt.report
	 * .designer.core.util.mediator.request.ReportRequest)
	 */
	protected void handleCreateElement(ReportRequest request) {
		List list = request.getSelectionModelList();
		// should be change the reuqest.getSource() interface, recode the source
		// type.added by gao

		int size = list.size();
		Object obj = null;
		if (size != 0) {
			obj = list.get(size - 1);
		}
		SetCurrentEditModelCommand command = new SetCurrentEditModelCommand(obj);
		command.execute();

		super.handleCreateElement(request);
	}

	private boolean isInContainer(List list) {
		boolean retValue = false;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (obj instanceof RowHandle || obj instanceof CellHandle) {
				retValue = true;
			} else {
				retValue = false;
				break;
			}
		}
		return retValue;
	}

	protected TemplateTransferDropTargetListener createTemplateTransferDropTargetListener(EditPartViewer viewer) {
		return new LibraryTemplateTransferDropTargetListener(viewer);
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
		if (adapter == IContentOutlinePage.class) {
			// ( (NonGEFSynchronizerWithMutiPageEditor)
			// getSelectionSynchronizer( ) ).add( (NonGEFSynchronizer)
			// outlinePage.getAdapter( NonGEFSynchronizer.class ) );

			// Add JS Editor as a selection listener to Outline view selections.
			// outlinePage.addSelectionChangedListener( jsEditor );
			LibraryOutlinePage outline = new LibraryOutlinePage(getModel());
			getModelEventManager().addModelEventProcessor(outline.getModelProcessor());
			return outline;
		} else if (adapter == IDataViewPage.class) {
			return super.getAdapter(adapter);
		} else if (adapter == IAttributeViewPage.class) {
			return super.getAdapter(adapter);
		} else if (adapter == IPropertySheetPage.class) {
			ReportPropertySheetPage sheetPage = new ReportPropertySheetPage(getModel());
			return sheetPage;
		}

		return super.getAdapter(adapter);
	}

	protected PaletteRoot getPaletteRoot() {
		if (paletteRoot == null) {
			paletteRoot = DesignerPaletteFactory.createPalette();
		}
		return paletteRoot;

	}

	protected IEditorPart getMultiPageEditor() {
		return parentEditorPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.schematic.layout.
	 * AbstractReportGraphicalEditorWithFlyoutPalette#getFileType()
	 */
	protected int getFileType() {
		return SessionHandleAdapter.LIBRARYFILE;
	}

	protected EditPartFactory getEditPartFactory() {
		return new LibraryGraphicalPartFactory();
	}

	protected ReportLayoutEditorBreadcrumb createBreadcrumb() {
		ReportLayoutEditorBreadcrumb breadcrumb = new ReportLayoutEditorBreadcrumb(this);
		breadcrumb.setBreadcrumbNodeProvider(new LibraryBreadcrumbNodeProvider());
		return breadcrumb;
	}
}
