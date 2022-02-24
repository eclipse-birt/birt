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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.birt.report.designer.internal.ui.views.actions.GlobalActionFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.part.NullEditorInput;
import org.eclipse.ui.part.EditorPart;

/**
 * ReportEditorProxy is a editor proxy, which use in eclipse IDE enviroment.
 * 
 * ReportEditorProxy determines editor input, then create a proper editor
 * instance to represents the editor behaivors.
 */

public class ReportEditorProxy extends EditorPart implements IPartListener, IPropertyListener, IReportEditor {

	/**
	 * The ID of the Report Editor
	 */
	public static final String REPROT_EDITOR_ID = "org.eclipse.birt.report.designer.ui.editors.ReportEditor"; //$NON-NLS-1$
	/**
	 * The ID of the Template Editor
	 */
	public static final String TEMPLATE_EDITOR_ID = "org.eclipse.birt.report.designer.ui.editors.TemplateEditor"; //$NON-NLS-1$
	/**
	 * The ID of the Library Editor
	 */
	public static final String LIBRARY_EDITOR_ID = "org.eclipse.birt.report.designer.ui.editors.LibraryEditor"; //$NON-NLS-1$

	MultiPageReportEditor instance;

	private String title = ""; //$NON-NLS-1$

	/**
	 * This is to adapte change for Bug 103810, which required site access even
	 * after part disposed.
	 */
	private IEditorSite cachedSite;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#getEditorInput()
	 */
	@SuppressWarnings("restriction")
	public IEditorInput getEditorInput() {
		if (instance != null) {
			return instance.getEditorInput();
		}
		return new NullEditorInput();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#getEditorSite()
	 */
	public IEditorSite getEditorSite() {
		if (instance != null) {
			return instance.getEditorSite();
		}
		return cachedSite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		cachedSite = site;

		if (instance != null) {
			getSite().getWorkbenchWindow().getPartService().removePartListener(instance);
			instance.dispose();
		}

		if (input instanceof IFileEditorInput || input instanceof IURIEditorInput) {
			instance = new IDEMultiPageReportEditor();
		} else {
			instance = new MultiPageReportEditor();
		}

		// must add property listener before init.
		instance.addPropertyListener(this);

		instance.init(site, input);
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createPartControl(Composite parent) {
		instance.createPartControl(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		if (instance != null) {
			instance.dispose();
			getSite().getWorkbenchWindow().getPartService().removePartListener(this);
			instance.removePropertyListener(this);
		}
		instance = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getSite()
	 */
	public IWorkbenchPartSite getSite() {
		if (instance != null) {
			return instance.getSite();
		}
		return cachedSite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getTitle()
	 */
	public String getTitle() {
		return this.title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#getTitleToolTip()
	 */
	public String getTitleToolTip() {
		if (instance != null) {
			return instance.getTitleToolTip();
		}
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		if (instance != null) {
			instance.setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (instance != null) {
			return instance.getAdapter(adapter);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		if (instance != null) {
			instance.doSave(monitor);
			firePropertyChange(PROP_DIRTY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		if (instance != null) {
			instance.doSaveAs();
			firePropertyChange(PROP_DIRTY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty() {
		if (instance != null) {
			return instance.isDirty();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		if (instance != null) {
			return instance.isSaveAsAllowed();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveOnCloseNeeded()
	 */
	public boolean isSaveOnCloseNeeded() {
		if (instance != null) {
			return instance.isSaveOnCloseNeeded();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setPartName(java.lang.String)
	 */
	protected void setPartName(String partName) {
		this.title = partName;
		super.setPartName(partName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partActivated(IWorkbenchPart part) {
		if (part instanceof ReportEditorProxy) {
			instance.partActivated(((ReportEditorProxy) part).getEditorPart());
		} else {
			instance.partActivated(part);
		}
		// if ( part != this )
		// {
		// if ( part instanceof PageBookView )
		// {
		// PageBookView view = (PageBookView) part;
		// if ( view.getCurrentPage( ) instanceof DesignerOutlinePage )
		// {
		// ISelectionProvider provider = (ISelectionProvider)
		// view.getCurrentPage( );
		// ReportRequest request = new ReportRequest( view.getCurrentPage( ) );
		// List list = new ArrayList( );
		// if ( provider.getSelection( ) instanceof IStructuredSelection )
		// {
		// list = ( (IStructuredSelection) provider.getSelection( ) ).toList( );
		// }
		// request.setSelectionObject( list );
		// request.setType( ReportRequest.SELECTION );
		// // no convert
		// // request.setRequestConvert(new
		// // EditorReportRequestConvert());
		// SessionHandleAdapter.getInstance( )
		// .getMediator( )
		// .notifyRequest( request );
		// SessionHandleAdapter.getInstance( )
		// .getMediator( )
		// .pushState( );
		// }
		// }
		// if ( instance.getActiveEditor( ) instanceof
		// GraphicalEditorWithFlyoutPalette )
		// {
		// if ( ( (GraphicalEditorWithFlyoutPalette) instance.getActiveEditor( )
		// ).getGraphicalViewer( )
		// .getEditDomain( )
		// .getPaletteViewer( ) != null )
		// {
		// GraphicalEditorWithFlyoutPalette editor =
		// (GraphicalEditorWithFlyoutPalette) instance.getActiveEditor( );
		// GraphicalViewer view = editor.getGraphicalViewer( );
		// view.getEditDomain( ).loadDefaultTool( );
		// }
		//
		// }
		// return;
		// }
		//
		// if ( part == this )
		// {
		// // use the asynchronized execution to ensure correct active page
		// // index.
		// Display.getCurrent( ).asyncExec( new Runnable( ) {
		//
		// public void run( )
		// {
		// // if ( instance.getActivePageInstance( ) instanceof
		// GraphicalEditorWithFlyoutPalette )
		// // {
		// // GraphicalEditorWithFlyoutPalette editor =
		// (GraphicalEditorWithFlyoutPalette) instance.getActivePageInstance( );
		// // GraphicalViewer view = editor.getGraphicalViewer( );
		// //
		// // UIUtil.resetViewSelection( view, true );
		// // }
		// };
		//
		// } );
		//
		// if ( getEditorInput( ).exists( ) )
		// {
		// instance.handleActivation( );
		//
		// SessionHandleAdapter.getInstance( )
		// .setReportDesignHandle( instance.getModel( ) );
		// DataSetManager.initCurrentInstance( getEditorInput( ) );
		// }
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart )
	 */
	public void partBroughtToTop(IWorkbenchPart part) {
		if (instance == null) {
			return;
		}

		if (part instanceof ReportEditorProxy) {
			instance.partBroughtToTop(((ReportEditorProxy) part).getEditorPart());
		} else {
			instance.partBroughtToTop(part);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partClosed(IWorkbenchPart part) {
		if (instance == null) {
			return;
		}

		if (part instanceof ReportEditorProxy) {
			instance.partClosed(((ReportEditorProxy) part).getEditorPart());
		} else {
			instance.partClosed(part);
		}
		// instance.partClosed( part );
		// FIXME ugly code
		if (part == this) {
			SessionHandleAdapter.getInstance().clear(instance.getModel());
			if (instance.getModel() != null) {
				GlobalActionFactory.removeStackActions(instance.getModel().getCommandStack());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart )
	 */
	public void partDeactivated(IWorkbenchPart part) {
		if (instance == null) {
			return;
		}

		if (part instanceof ReportEditorProxy) {
			instance.partDeactivated(((ReportEditorProxy) part).getEditorPart());
		} else {
			instance.partDeactivated(part);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partOpened(IWorkbenchPart part) {
		if (instance == null) {
			return;
		}

		if (part instanceof ReportEditorProxy) {
			instance.partOpened(((ReportEditorProxy) part).getEditorPart());
		} else {
			instance.partOpened(part);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object, int)
	 */
	public void propertyChanged(Object source, int propId) {
		if (propId == IWorkbenchPartConstants.PROP_PART_NAME) {
			setPartName(instance.getPartName());
		}

		firePropertyChange(propId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor#
	 * getEditorPart()
	 */
	public IEditorPart getEditorPart() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == instance) {
			return true;
		}
		return super.equals(obj);
	}
}
