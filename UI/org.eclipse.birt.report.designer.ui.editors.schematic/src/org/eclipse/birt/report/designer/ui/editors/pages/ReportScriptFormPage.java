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

package org.eclipse.birt.report.designer.ui.editors.pages;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.mediator.IMediatorState;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventManager;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.ModelEventManager;
import org.eclipse.birt.report.designer.internal.ui.editors.script.JSEditor;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewTreeViewerPage;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.birt.report.designer.internal.ui.views.property.ReportPropertySheetPage;
import org.eclipse.birt.report.designer.ui.editors.IPageStaleType;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.data.IDataViewPage;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * ReportScriptFormPage
 */
public class ReportScriptFormPage extends ReportFormPage {

	protected static final Logger logger = Logger.getLogger(ReportScriptFormPage.class.getName());

	public static final String ID = MultiPageReportEditor.ScriptForm_ID;

	protected IEditorPart jsEditor;

	private IModelEventManager manager = getModelEventManager();

	private Control control;

	private int staleType;

	private ModuleHandle model;

	private IReportEditorPage previouPage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.pages.ReportFormPage#init
	 * (org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		jsEditor = createJSEditor();
		jsEditor.init(site, input);
	}

	/**
	 * Creates javascript editor for the page.
	 * 
	 * @return the javascript editor
	 */
	protected IEditorPart createJSEditor() {
		return new JSEditor(this);
	}

	protected void hookModelEventManager(Object model) {
		getModelEventManager().hookRoot(model);
		if (model instanceof ModuleHandle) {
			getModelEventManager().hookCommandStack(new WrapperCommandStack(((ModuleHandle) model).getCommandStack()));
		} else {
			getModelEventManager().hookCommandStack(new WrapperCommandStack());
		}
	}

	protected void unhookModelEventManager(Object model) {
		getModelEventManager().unhookRoot(model);
	}

	/**
	 * @return
	 */
	protected IModelEventManager getModelEventManager() {
		if (manager == null) {
			manager = new ModelEventManager();
		}
		return manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#onBroughtToTop
	 * (org.eclipse.birt.report.designer.ui.editors.IReportEditorPage)
	 */
	public boolean onBroughtToTop(IReportEditorPage prePage) {
		boolean notify = true;
		if (prePage == this) {
			notify = false;
		}
		if (getEditorInput() != prePage.getEditorInput()) {
			setInput(prePage.getEditorInput());
		}
		if (getStaleType() == IPageStaleType.MODEL_RELOAD) {
			reloadEditorInput();
			doSave(null);
		}
		ModuleHandle newModel = getModel();

		if (newModel != null && model != null && model != newModel) {
			hookModelEventManager(newModel);
			markPageStale(IPageStaleType.NONE);

			SessionHandleAdapter.getInstance().resetReportDesign(model, newModel);

			unhookModelEventManager(model);

			model = newModel;

			SessionHandleAdapter.getInstance().setReportDesignHandle(newModel);

			if (jsEditor instanceof JSEditor) {
				((JSEditor) jsEditor).connectRoot(newModel);
			}

			// reloadEditorInput( );
			// UIUtil.processSessionResourceFolder( getEditorInput( ),
			// UIUtil.getProjectFromInput( getEditorInput( ) ),
			// getModel( ) );

			IMediatorState state = SessionHandleAdapter.getInstance().getMediator(newModel).getState();
			ReportRequest request = new ReportRequest(state.getSource());
			List list = new ArrayList();
			list.add(newModel);
			request.setSelectionObject(list);
			request.setType(ReportRequest.SELECTION);

			// SessionHandleAdapter.getInstance().getMediator().pushState();
			SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);
			previouPage = prePage;
			jsEditor.setFocus();
			return true;

		}

		jsEditor.setFocus();
		previouPage = prePage;
		// if ( prePage != null && jsEditor != null )
		// {
		// jsEditor.setIsModified( prePage.isDirty( ) );
		// }

		// ISelection selection = new StructuredSelection(
		// SessionHandleAdapter.getInstance( )
		// .getMediator( )
		// .getCurrentState( )
		// .getSelectionObject( ) );
		if (notify) {
			IMediatorState state = SessionHandleAdapter.getInstance().getMediator().getState();
			ReportRequest request = new ReportRequest(state.getSource());
			Object data = state.getData();

			List list = new ArrayList();

			if (data instanceof List) {
				list.addAll((List) data);
			} else if (data != null) {
				list.add(data);
			}

			if (list.isEmpty()) {
				list.add(new Object());
			}
			request.setSelectionObject(list);
			request.setType(ReportRequest.SELECTION);

			// SessionHandleAdapter.getInstance().getMediator().pushState();
			SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);
		}

		// jsEditor.handleSelectionChanged( selection );

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#markPageStale
	 * (int)
	 */
	public void markPageStale(int type) {
		this.staleType = type;
	}

	private void reloadEditorInput() {

		if (jsEditor instanceof JSEditor) {
			((JSEditor) jsEditor).resetText();
		}
		getEditor().editorDirtyStateChanged();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#getStaleType ()
	 */
	public int getStaleType() {
		return staleType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	public Control getPartControl() {
		return this.control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getId()
	 */
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createPartControl(Composite parent) {
		try {
			jsEditor.createPartControl(parent);
			if (jsEditor instanceof JSEditor) {
				((JSEditor) jsEditor).connectRoot(getModel());
			}
			Control[] children = parent.getChildren();
			control = children[children.length - 1];
			//
			if (previouPage != null) {
				onBroughtToTop(previouPage);
			}

			model = getModel();

			hookModelEventManager(model);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		if (jsEditor instanceof JSEditor) {
			((JSEditor) jsEditor).doSave(monitor, false);
		} else {
			jsEditor.doSave(monitor);
		}
		IReportProvider provider = getProvider();
		if (provider != null) {
			provider.saveReport(getModel(), getEditorInput(), monitor);
			firePropertyChange(PROP_DIRTY);
		}
		markPageStale(IPageStaleType.MODEL_CHANGED);
		getEditor().editorDirtyStateChanged();

		UIUtil.doFinishSave(getModel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		IReportProvider provider = getProvider();
		if (provider != null) {
			IPath path = provider.getSaveAsPath(getEditorInput());

			if (path == null) {
				return;
			}

			final IEditorInput input = provider.createNewEditorInput(path);

			setInput(input);

			IRunnableWithProgress op = new IRunnableWithProgress() {

				public synchronized final void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					final InvocationTargetException[] iteHolder = new InvocationTargetException[1];
					try {
						IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable() {

							public void run(IProgressMonitor pm) throws CoreException {
								try {
									execute(pm);
								} catch (InvocationTargetException e) {
									// Pass it outside the workspace runnable
									iteHolder[0] = e;
								} catch (InterruptedException e) {
									// Re-throw as OperationCanceledException,
									// which
									// will be
									// caught and re-thrown as
									// InterruptedException
									// below.
									throw new OperationCanceledException(e.getMessage());
								}
								// CoreException and OperationCanceledException
								// are
								// propagated
							}
						};

						ResourcesPlugin.getWorkspace().run(workspaceRunnable, ResourcesPlugin.getWorkspace().getRoot(),
								IResource.NONE, monitor);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					} catch (OperationCanceledException e) {
						throw new InterruptedException(e.getMessage());
					}
					// Re-throw the InvocationTargetException, if any occurred
					if (iteHolder[0] != null) {
						throw iteHolder[0];
					}
				}

				public void execute(final IProgressMonitor monitor)
						throws CoreException, InvocationTargetException, InterruptedException {

					try {
						doSave(monitor);
					}

					catch (Exception e) {
						ExceptionUtil.handle(e);
					}
				}
			};

			try {
				new ProgressMonitorDialog(getSite().getWorkbenchWindow().getShell()).run(false, true, op);
			}

			catch (Exception e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty() {
		boolean isModelDirty = false;

		ModuleHandle reportModel = getModel();
		if (reportModel != null) {
			isModelDirty = reportModel.needsSave();
		}
		return isModelDirty || jsEditor.isDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		unhookModelEventManager(model);
		if (jsEditor instanceof JSEditor) {
			((JSEditor) jsEditor).disConnectRoot(model);
		}
		super.dispose();

		jsEditor.dispose();
		jsEditor = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(ITextEditor.class)) {
			if (jsEditor != null) {
				return jsEditor.getAdapter(adapter);
			}
			return null;
		} else if (adapter == ActionRegistry.class) {
			return jsEditor.getAdapter(ActionRegistry.class);
		} else if (adapter == PalettePage.class) {
			return jsEditor.getAdapter(PalettePage.class);
		} else if (adapter == IContentOutlinePage.class) {
			DesignerOutlinePage outlinePage = new DesignerOutlinePage(getModel());
			getModelEventManager().addModelEventProcessor(outlinePage.getModelProcessor());
			return outlinePage;
		} else if (adapter == IDataViewPage.class) {
			DataViewTreeViewerPage page = new DataViewTreeViewerPage(getModel());
			getModelEventManager().addModelEventProcessor(page.getModelProcessor());
			return page;
		} else if (adapter == IPropertySheetPage.class) {
			ReportPropertySheetPage sheetPage = new ReportPropertySheetPage(getModel());
			return sheetPage;
		}
		return jsEditor.getAdapter(adapter);
	}

	/**
	 * Get JS Script editor.
	 * 
	 * @return
	 */
	public IEditorPart getScriptEditor() {
		return jsEditor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.editors.pages.ReportFormPage#
	 * canLeaveThePage()
	 */
	public boolean canLeaveThePage() {
		jsEditor.doSave(null);
		return super.canLeaveThePage();
	}

	protected void finalize() throws Throwable {
		if (Policy.TRACING_PAGE_CLOSE) {
			System.out.println("Report script page finalized"); //$NON-NLS-1$
		}
		super.finalize();
	}
}
