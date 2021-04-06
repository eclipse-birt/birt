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

package org.eclipse.birt.report.designer.ui.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.preference.IPreferenceChangeListener;
import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.core.preference.PreferenceChangeEvent;
import org.eclipse.birt.report.designer.core.mediator.IMediatorColleague;
import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.mediator.MediatorManager;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptObjectNode;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.FileReportProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.IAdvanceReportEditorPage;
import org.eclipse.birt.report.designer.internal.ui.editors.IRelatedFileChangeResolve;
import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.birt.report.designer.internal.ui.editors.LibraryProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.ReportMultiBookPage;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.ReportMultiBookPage.EmptyPage;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.IResourceEditPart;
import org.eclipse.birt.report.designer.internal.ui.extension.EditorContributorManager;
import org.eclipse.birt.report.designer.internal.ui.extension.FormPageDef;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ILibraryProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.GlobalActionFactory;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewTreeViewerPage;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeListener;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.designer.ui.views.attributes.IAttributeViewPage;
import org.eclipse.birt.report.designer.ui.views.data.IDataViewPage;
import org.eclipse.birt.report.designer.ui.widget.ITreeViewerBackup;
import org.eclipse.birt.report.designer.ui.widget.TreeViewerBackup;
import org.eclipse.birt.report.model.api.IVersionInfo;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.INestableKeyBindingService;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * 
 * Base multipage editor for report editors. Clients can subclass this class to
 * create customize report editors. Report editor pages can contributed through
 * Extendtion Point
 * org.eclipse.birt.report.designer.ui.editors.multiPageEditorContributor.
 * 
 * @see IReportEditorPage
 */
public class MultiPageReportEditor extends AbstractMultiPageEditor
		implements IPartListener, IReportEditor, IMediatorColleague, IReportResourceChangeListener {

	public static final String LayoutMasterPage_ID = "org.eclipse.birt.report.designer.ui.editors.masterpage"; //$NON-NLS-1$
	public static final String LayoutEditor_ID = "org.eclipse.birt.report.designer.ui.editors.layout"; //$NON-NLS-1$
	public static final String XMLSourcePage_ID = "org.eclipse.birt.report.designer.ui.editors.xmlsource"; //$NON-NLS-1$
	public static final String ScriptForm_ID = "org.eclipse.birt.report.designer.ui.editors.script"; //$NON-NLS-1$

	public static int PROP_SAVE = 1000;

	private ReportMultiBookPage fPalettePage;

	private ReportMultiBookPage outlinePage;

	private ReportMultiBookPage dataPage;

	private boolean fIsHandlingActivation;

	private long fModificationStamp = -1;;

	protected IReportProvider reportProvider;

	private FormEditorSelectionProvider provider = new FormEditorSelectionProvider(this);
	private boolean isChanging = false;
	private ReportMultiBookPage attributePage;
	private ITreeViewerBackup outlineBackup;
	private ITreeViewerBackup dataBackup;

	private boolean needReload = false;
	private boolean needReset = false;
	private IWorkbenchPart fActivePart;
	private boolean isClose = false;
	// private IRelatedFileChangeResolve resolve;
	private List<IRelatedFileChangeResolve> resolveList = new ArrayList<IRelatedFileChangeResolve>();
	private IPreferences prefs;
	IPreferenceChangeListener preferenceChangeListener = new IPreferenceChangeListener() {

		public void preferenceChange(PreferenceChangeEvent event) {
			if (event.getKey().equals(PreferenceChangeEvent.SPECIALTODEFAULT)
					|| ReportPlugin.RESOURCE_PREFERENCE.equals(event.getKey())) {
				SessionHandleAdapter.getInstance().getSessionHandle()
						.setBirtResourcePath(ReportPlugin.getDefault().getResourcePreference());

				UIUtil.processSessionResourceFolder(getEditorInput(), UIUtil.getProjectFromInput(getEditorInput()),
						getModel());

				refreshGraphicalEditor();
			}
		}
	};

	private IWindowListener windowListener = new IWindowListener() {

		public void windowActivated(IWorkbenchWindow window) {
			if (!(window == getEditorSite().getWorkbenchWindow())) {
				return;
			}
			if (fActivePart != MultiPageReportEditor.this) {
				return;
			}
			window.getShell().getDisplay().asyncExec(new Runnable() {

				public void run() {
					confirmSave();
				}
			});
		}

		public void windowClosed(IWorkbenchWindow window) {

		}

		public void windowDeactivated(IWorkbenchWindow window) {

		}

		public void windowOpened(IWorkbenchWindow window) {

		}

	};

	protected void confirmSave() {

		if (fIsHandlingActivation)
			return;
		if (!isExistModelFile() && !isClose) {
			// Thread.dumpStack( );
			fIsHandlingActivation = true;
			MessageDialog dialog = new MessageDialog(UIUtil.getDefaultShell(),
					Messages.getString("MultiPageReportEditor.ConfirmTitle"), //$NON-NLS-1$
					null, Messages.getString("MultiPageReportEditor.SaveConfirmMessage"), //$NON-NLS-1$
					MessageDialog.QUESTION, new String[] { Messages.getString("MultiPageReportEditor.SaveButton"), //$NON-NLS-1$
							Messages.getString("MultiPageReportEditor.CloseButton") }, //$NON-NLS-1$
					0);
			try {
				if (dialog.open() == 0) {
					doSave(null);
					isClose = false;
				} else {
					Display display = getSite().getShell().getDisplay();
					display.asyncExec(new Runnable() {

						public void run() {
							closeEditor(false);
						}
					});
					isClose = true;

				}
			} finally {
				fIsHandlingActivation = false;
				needReset = false;
				needReload = false;
			}

		}
	}

	// this is a bug because the getActiveEditor() return null, we should change
	// the getActivePage()
	// return the correct current page index.we may delete this class
	// TODO
	private static class FormEditorSelectionProvider extends MultiPageSelectionProvider {

		private ISelection globalSelection;

		/**
		 * @param multiPageEditor
		 */
		public FormEditorSelectionProvider(FormEditor formEditor) {
			super(formEditor);
		}

		public ISelection getSelection() {
			IEditorPart activeEditor = ((FormEditor) getMultiPageEditor()).getActivePageInstance();
			// IEditorPart activeEditor = getActivePageInstance( );
			if (activeEditor != null) {
				ISelectionProvider selectionProvider = activeEditor.getSite().getSelectionProvider();
				if (selectionProvider != null)
					return selectionProvider.getSelection();
			}
			return globalSelection;
		}

		/*
		 * (non-Javadoc) Method declared on <code> ISelectionProvider </code> .
		 */
		public void setSelection(ISelection selection) {
			IEditorPart activeEditor = ((FormEditor) getMultiPageEditor()).getActivePageInstance();
			if (activeEditor != null) {
				ISelectionProvider selectionProvider = activeEditor.getSite().getSelectionProvider();
				if (selectionProvider != null)
					selectionProvider.setSelection(selection);
			} else {
				this.globalSelection = selection;
				fireSelectionChanged(new SelectionChangedEvent(this, globalSelection));
			}
		}
	}

	/**
	 * Constructor
	 */
	public MultiPageReportEditor() {
		super();
		outlineBackup = new TreeViewerBackup();
		dataBackup = new TreeViewerBackup();

		IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

		if (synchronizer != null) {
			synchronizer.addListener(IReportResourceChangeEvent.LibraySaveChange
					| IReportResourceChangeEvent.ImageResourceChange | IReportResourceChangeEvent.DataDesignSaveChange,
					this);
		}

		PlatformUI.getWorkbench().addWindowListener(windowListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);

		// getSite( ).getWorkbenchWindow( )
		// .getPartService( )
		// .addPartListener( this );
		site.setSelectionProvider(provider);

		IReportProvider provider = getProvider();

		if (provider != null && provider.getInputPath(input) != null) {
			setPartName(provider.getInputPath(input).lastSegment());
			firePropertyChange(IWorkbenchPartConstants.PROP_PART_NAME);
		} else {
			setPartName(input.getName());
			firePropertyChange(IWorkbenchPartConstants.PROP_PART_NAME);
		}

		// suport the mediator
		MediatorManager.addGlobalColleague(this);

		IProject project = UIUtil.getProjectFromInput(input);
		prefs = PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault(), project);
		prefs.addPreferenceChangeListener(preferenceChangeListener);
	}

	protected IReportProvider getProvider() {
		if (reportProvider == null) {
			reportProvider = new FileReportProvider();
		}
		return reportProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	protected void addPages() {
		List formPageList = EditorContributorManager.getInstance()
				.getEditorContributor(getEditorSite().getId()).formPageList;
		boolean error = false;

		// For back compatible only.
		// Provide warning message to let user select if the auto convert needs
		// See bugzilla bug 136536 for detail.
		String fileName = getProvider().getInputPath(getEditorInput()).toOSString();
		List message = ModuleUtil.checkVersion(fileName);
		if (message.size() > 0) {
			IVersionInfo info = (IVersionInfo) message.get(0);

			if (!MessageDialog.openConfirm(UIUtil.getDefaultShell(),
					Messages.getString("MultiPageReportEditor.CheckVersion.Dialog.Title"), //$NON-NLS-1$
					info.getLocalizedMessage())) {
				for (Iterator iter = formPageList.iterator(); iter.hasNext();) {
					FormPageDef pagedef = (FormPageDef) iter.next();
					if (XMLSourcePage_ID.equals(pagedef.id))

					{
						try {
							addPage(pagedef.createPage(), pagedef.displayName);
							break;
						} catch (Exception e) {

						}
					}
				}
				return;
			}
		}
		UIUtil.processSessionResourceFolder(getEditorInput(), UIUtil.getProjectFromInput(getEditorInput()), null);
		// load the model first here, so consequent pages can directly use it
		// without reloading
		getProvider().getReportModuleHandle(getEditorInput());

		for (Iterator iter = formPageList.iterator(); iter.hasNext();) {
			FormPageDef pagedef = (FormPageDef) iter.next();
			try {
				addPage(pagedef.createPage(), pagedef.displayName);
			} catch (Exception e) {
				error = true;
			}
		}

		if (error) {
			setActivePage(XMLSourcePage_ID);
		}
	}

	/**
	 * Add a IReportEditorPage to multipage editor.
	 * 
	 * @param page
	 * @param title
	 * @return
	 * @throws PartInitException
	 */
	public int addPage(IReportEditorPage page, String title) throws PartInitException {
		int index = super.addPage(page);
		if (title != null) {
			setPageText(index, title);
		}
		try {
			page.initialize(this);
			page.init(createSite(page), getEditorInput());
		} catch (Exception e) {
			// removePage( index );
			throw new PartInitException(e.getMessage());
		}
		return index;
	}

	/**
	 * Remove report editor page.
	 * 
	 * @param id the page id.
	 */
	public void removePage(String id) {
		IFormPage page = findPage(id);
		if (page != null) {
			removePage(page.getIndex());
		}
	}

	/**
	 * Remove all report editor page.
	 */
	public void removeAllPages() {
		for (int i = pages.toArray().length - 1; i >= 0; i--) {
			if (pages.get(i) != null)
				this.removePage(((IFormPage) pages.get(i)).getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		boolean isReselect = false;
		if (getModel() != null
				&& ModuleUtil.compareReportVersion(ModuleUtil.getReportVersion(), getModel().getVersion()) > 0) {
			if (!MessageDialog.openConfirm(UIUtil.getDefaultShell(),
					Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.Title"), //$NON-NLS-1$
					Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.Message"))) //$NON-NLS-1$
			{
				return;
			} else {
				isReselect = true;
			}
		}
		getCurrentPageInstance().doSave(monitor);
		fireDesignFileChangeEvent();
		if (isReselect) {
			Display.getCurrent().asyncExec(new Runnable() {

				public void run() {
					if (getActivePageInstance() instanceof GraphicalEditorWithFlyoutPalette) {
						if (((GraphicalEditorWithFlyoutPalette) getActivePageInstance()).getGraphicalViewer() != null) {
							GraphicalEditorWithFlyoutPalette editor = (GraphicalEditorWithFlyoutPalette) getActivePageInstance();
							GraphicalViewer view = editor.getGraphicalViewer();

							UIUtil.resetViewSelection(view, true);
						}

					}

				}
			});
		}
	}

	private void fireDesignFileChangeEvent() {
		UIUtil.doFinishSave(getModel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		getActivePageInstance().doSaveAs();
		setInput(getActivePageInstance().getEditorInput());
		// update site name
		IReportProvider provider = getProvider();
		if (provider != null) {
			setPartName(provider.getInputPath(getEditorInput()).lastSegment());
			firePropertyChange(IWorkbenchPartConstants.PROP_PART_NAME);
			getProvider().getReportModuleHandle(getEditorInput())
					.setFileName(getProvider().getInputPath(getEditorInput()).toOSString());
		}

		updateRelatedViews();
		fireDesignFileChangeEvent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		if (getActivePageInstance() != null) {
			return getActivePageInstance().isSaveAsAllowed();
		}
		return false;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see org.eclipse.ui.forms.editor.FormEditor#isDirty()
	// */
	// public boolean isDirty( )
	// {
	// fLastDirtyState = computeDirtyState( );
	// return fLastDirtyState;
	// }
	//
	// private boolean computeDirtyState( )
	// {
	// IFormPage page = getActivePageInstance( );
	// if ( page != null && page.isDirty( ) )
	// return true;
	// return false;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class type) {
		if (type == IReportProvider.class) {
			if (reportProvider == null) {
				reportProvider = new FileReportProvider();
			}
			return reportProvider;
		} else if (type == ILibraryProvider.class) {
			return new LibraryProvider();
		} else if (type == PalettePage.class) {
			Object adapter = getPalettePage();
			updatePaletteView(getActivePageInstance());
			return adapter;
		} else if (type == IContentOutlinePage.class) {
			/*
			 * Update the logic under eclipse 3.7, eclipse always call the
			 * getAdapter(OutlinePage) when handle the ShellActive event. So we don't need
			 * to call updateOutLineView every time, only call it when new a outline page.
			 */
			boolean update = outlinePage == null || outlinePage.isDisposed();
			Object adapter = getOutlinePage();
			if (update) {
				updateOutLineView(getActivePageInstance());
			}
			return adapter;
		} else if (type == IDataViewPage.class) {
			Object adapter = getDataPage();
			updateDateView(getActivePageInstance());
			return adapter;
		} else if (type == IAttributeViewPage.class) {
			Object adapter = getAttributePage();
			updateAttributeView(getActivePageInstance());
			return adapter;
		} else if (getActivePageInstance() != null) {
			return getActivePageInstance().getAdapter(type);
		}

		return super.getAdapter(type);
	}

	private void updateAttributeView(IFormPage activePageInstance) {
		if (attributePage == null) {
			return;
		}

		Object adapter = activePageInstance.getAdapter(IAttributeViewPage.class);
		attributePage.setActivePage((IPageBookViewPage) adapter);
	}

	private void updateDateView(IFormPage activePageInstance) {
		if (dataPage == null) {
			return;
		}

		Object adapter = activePageInstance.getAdapter(IDataViewPage.class);
		if (adapter instanceof DataViewTreeViewerPage) {
			((DataViewTreeViewerPage) adapter).setBackupState(dataBackup);
		}
		dataPage.setActivePage((IPageBookViewPage) adapter);
	}

	private void updateOutLineView(IFormPage activePageInstance) {
		if (outlinePage == null) {
			return;
		}

		if (reloadOutlinePage()) {
			return;
		}
		Object designOutLinePage = activePageInstance.getAdapter(IContentOutlinePage.class);
		if (designOutLinePage instanceof DesignerOutlinePage) {
			((DesignerOutlinePage) designOutLinePage).setBackupState(outlineBackup);
		}
		outlinePage.setActivePage((IPageBookViewPage) designOutLinePage);
	}

	public void outlineSwitch() {
		if (!getActivePageInstance().getId().equals(XMLSourcePage_ID) || outlinePage == null) {
			return;
		}

		if (outlinePage.getCurrentPage() instanceof DesignerOutlinePage) {
			outlinePage.setActivePage((IPageBookViewPage) getActivePageInstance().getAdapter(ContentOutlinePage.class));
		} else {
			outlinePage
					.setActivePage((IPageBookViewPage) getActivePageInstance().getAdapter(IContentOutlinePage.class));
		}
		outlinePage.getSite().getActionBars().updateActionBars();
	}

	public boolean reloadOutlinePage() {
		if (!getActivePageInstance().getId().equals(XMLSourcePage_ID) || outlinePage == null
				|| !getCurrentPageInstance().getId().equals(XMLSourcePage_ID)) {
			return false;
		}

		if (outlinePage.getCurrentPage() instanceof DesignerOutlinePage || outlinePage.getCurrentPage() == null
				|| outlinePage.getCurrentPage() instanceof EmptyPage) {
			outlinePage
					.setActivePage((IPageBookViewPage) getActivePageInstance().getAdapter(IContentOutlinePage.class));
		} else {
			outlinePage.setActivePage((IPageBookViewPage) getActivePageInstance().getAdapter(ContentOutlinePage.class));
		}
		if (outlinePage.getSite() != null) {
			outlinePage.getSite().getActionBars().updateActionBars();
		}
		return true;
	}

	public Object getDataPage() {
		if (dataPage == null || dataPage.isDisposed()) {
			dataPage = new ReportMultiBookPage();
		}
		return dataPage;
	}

	public Object getAttributePage() {
		if (attributePage == null || attributePage.isDisposed()) {
			attributePage = new ReportMultiBookPage();
		}
		return attributePage;
	}

	/**
	 * If new a outline page, should call updateOutLineView( getActivePageInstance(
	 * ) ) method at first.
	 * 
	 * @Since 3.7.2
	 * 
	 */
	public Object getOutlinePage() {
		if (outlinePage == null || outlinePage.isDisposed()) {
			outlinePage = new ReportMultiBookPage();
		}
		return outlinePage;
	}

	public Object getPalettePage() {
		if (fPalettePage == null || fPalettePage.isDisposed()) {
			fPalettePage = new ReportMultiBookPage();
		}
		return fPalettePage;
	}

	private void updatePaletteView(IFormPage activePageInstance) {

		if (fPalettePage == null) {
			return;
		}

		Object palette = activePageInstance.getAdapter(PalettePage.class);
		fPalettePage.setActivePage((IPageBookViewPage) palette);
	}

	public void pageChange(String id) {
		IFormPage page = findPage(id);
		if (page != null) {
			pageChange(page.getIndex());
		}
	}

	protected void pageChange(int newPageIndex) {
		int oldPageIndex = getCurrentPage();

		if (oldPageIndex == newPageIndex) {
			isChanging = false;
			bingdingKey(oldPageIndex);
			return;
		}

		if (oldPageIndex != -1) {
			Object oldPage = pages.get(oldPageIndex);
			Object newPage = pages.get(newPageIndex);

			if (oldPage instanceof IFormPage) {
				if (!((IFormPage) oldPage).canLeaveThePage()) {
					setActivePage(oldPageIndex);
					return;
				}
			}

			// change to new page, must do it first, because must check old page
			// is canleave.
			isChanging = true;
			super.pageChange(newPageIndex);
			// updateRelatedViews( );
			// check new page status
			if (!prePageChanges(oldPage, newPage)) {
				super.setActivePage(oldPageIndex);
				updateRelatedViews();
				return;
			} else if (isChanging) {
				bingdingKey(newPageIndex);
			}
			isChanging = false;
		} else {
			super.pageChange(newPageIndex);
		}
		updateRelatedViews();
		updateAttributeView(getActivePageInstance());
	}

	public void setFocus() {
		if (getActivePageInstance() != null && getActivePageInstance().getPartControl() != null
				&& UIUtil.containsFocusControl(getActivePageInstance().getPartControl()))
			return;
		super.setFocus();
		if (pages == null || getCurrentPage() < 0 || getCurrentPage() > pages.size() - 1) {
			return;
		}
		bingdingKey(getCurrentPage());
	}

	// this is a bug because the getActiveEditor() return null, we should change
	// the getActivePage()
	// return the correct current page index.we may delete this method
	// TODO
	private void bingdingKey(int newPageIndex) {
		final IKeyBindingService service = getSite().getKeyBindingService();
		final IEditorPart editor = (IEditorPart) pages.get(newPageIndex);
		if (editor != null && editor.getEditorSite() != null) {
			editor.setFocus();
			// There is no selected page, so deactivate the active service.
			if (service instanceof INestableKeyBindingService) {
				final INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
				if (editor != null) {
					nestableService.activateKeyBindingService(editor.getEditorSite());
				} else {
					nestableService.activateKeyBindingService(null);
				}
			} else {

			}
		}
	}

	public void updateRelatedViews() {
		updatePaletteView(getCurrentPageInstance());
		updateOutLineView(getCurrentPageInstance());
		updateDateView(getCurrentPageInstance());
	}

	protected boolean prePageChanges(Object oldPage, Object newPage) {

		boolean isNewPageValid = true;
		if (oldPage instanceof IReportEditorPage && newPage instanceof IReportEditorPage) {
			isNewPageValid = ((IReportEditorPage) newPage).onBroughtToTop((IReportEditorPage) oldPage);
			// TODO: HOW TO RESET MODEL?????????
			// model = SessionHandleAdapter.getInstance(
			// ).getReportDesignHandle( );
		}

		return isNewPageValid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#editorDirtyStateChanged()
	 */
	public void editorDirtyStateChanged() {
		super.editorDirtyStateChanged();
		markPageStale();
	}

	private void markPageStale() {
		// int currentIndex = getCurrentPage( );

		IFormPage currentPage = getActivePageInstance();

		if (!(currentPage instanceof IReportEditorPage)) {
			return;
		}

		// if ( currentIndex != -1 )
		// {
		// for ( int i = 0; i < pages.size( ); i++ )
		// {
		// if ( i == currentIndex )
		// {
		// continue;
		// }
		// Object page = pages.get( i );
		// if ( page instanceof IReportEditorPage )
		// {
		// ( (IReportEditorPage) page ).markPageStale( ( (IReportEditorPage)
		// currentPage ).getStaleType( ) );
		// }
		// }
		// }
	}

	/**
	 * Get the current report ModuleHandle.
	 * 
	 * @return
	 */
	public ModuleHandle getModel() {
		if (reportProvider != null) {
			return reportProvider.queryReportModuleHandle();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partActivated(IWorkbenchPart part) {
		fActivePart = part;

		if (part != this) {
			if (part instanceof PageBookView) {
				PageBookView view = (PageBookView) part;
				if (view.getCurrentPage() instanceof DesignerOutlinePage) {
					ISelectionProvider provider = (ISelectionProvider) view.getCurrentPage();
					ReportRequest request = new ReportRequest(view.getCurrentPage());
					List list = new ArrayList();
					if (provider.getSelection() instanceof IStructuredSelection) {
						list = ((IStructuredSelection) provider.getSelection()).toList();
					}
					request.setSelectionObject(list);
					request.setType(ReportRequest.SELECTION);
					// no convert
					// request.setRequestConvert(new
					// EditorReportRequestConvert());
					SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);
					SessionHandleAdapter.getInstance().getMediator().pushState();
				}
			}
			if (getActivePageInstance() instanceof GraphicalEditorWithFlyoutPalette) {
				if (((GraphicalEditorWithFlyoutPalette) getActivePageInstance()).getGraphicalViewer().getEditDomain()
						.getPaletteViewer() != null) {
					GraphicalEditorWithFlyoutPalette editor = (GraphicalEditorWithFlyoutPalette) getActivePageInstance();
					GraphicalViewer view = editor.getGraphicalViewer();
					view.getEditDomain().loadDefaultTool();
				}

			}
			return;
		}

		if (part == this) {
			confirmSave();
			final ModuleHandle oldHandle = getModel();

			if (needReset) {
				if (resolveList != null && resetList(resolveList)) {
					getProvider().getReportModuleHandle(getEditorInput(), true);
				} else {
					needReset = false;
				}
				needReload = false;

			}
			if (needReload) {

				if (resolveList != null && reloadList(resolveList)) {
					// do nothing now
				} else {
					needReload = false;
				}
			}

			if (getEditorInput().exists()) {
				handleActivation();

				ModuleHandle currentModel = getModel();

				SessionHandleAdapter.getInstance().setReportDesignHandle(currentModel);
				String str = SessionHandleAdapter.getInstance().getSessionHandle().getResourceFolder();
				UIUtil.processSessionResourceFolder(getEditorInput(), UIUtil.getProjectFromInput(getEditorInput()),
						currentModel);

				if (!str.equals(SessionHandleAdapter.getInstance().getSessionHandle().getResourceFolder())
						&& getActivePageInstance() instanceof GraphicalEditorWithFlyoutPalette) {
					((GraphicalEditorWithFlyoutPalette) getActivePageInstance()).getGraphicalViewer().getRootEditPart();
					refreshGraphicalEditor();
				}

			}

			if ( // getActivePageInstance( ) instanceof
					// GraphicalEditorWithFlyoutPalette
					// &&
			getActivePageInstance() instanceof IReportEditorPage) {
				boolean isDispatch = false;
				if (getActivePageInstance() instanceof GraphicalEditorWithFlyoutPalette) {
					isDispatch = true;
				} else if (needReload || needReset) {
					isDispatch = true;
				}
				final boolean tempDispatch = isDispatch;
				Display.getCurrent().asyncExec(new Runnable() {

					public void run() {
						IReportEditorPage curPage = (IReportEditorPage) getActivePageInstance();
						if (needReload || needReset) {
							curPage.markPageStale(IPageStaleType.MODEL_RELOAD);

						}
						if (getActivePageInstance() != null) {
							if (curPage instanceof IAdvanceReportEditorPage) {
								if (((IAdvanceReportEditorPage) curPage).isSensitivePartChange()) {
									curPage.onBroughtToTop((IReportEditorPage) getActivePageInstance());
								}
							} else {
								curPage.onBroughtToTop((IReportEditorPage) getActivePageInstance());
							}
						}
						if (!tempDispatch) {
							return;
						}

						// UIUtil.resetViewSelection( view, true );

						if (needReload || needReset) {
							updateRelatedViews();
							// doSave( null );
							UIUtil.refreshCurrentEditorMarkers();
							curPage.markPageStale(IPageStaleType.NONE);

						}
						if (needReset) {
							SessionHandleAdapter.getInstance().resetReportDesign(oldHandle, getModel());
							oldHandle.close();
						}
						needReload = false;
						needReset = false;
						resolveList.clear();
					}
				});
				// UIUtil.resetViewSelection( view, true );
			}

		}
		// if ( getModel( ) != null )
		// {
		// getModel( ).setResourceFolder( getProjectFolder( ) );
		// }
	}

	private boolean reloadList(List<IRelatedFileChangeResolve> list) {
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).reload(getModel())) {
				return false;
			}
		}
		return true;
	}

	private boolean resetList(List<IRelatedFileChangeResolve> list) {
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).reset()) {
				return false;
			}
		}
		return true;
	}

	private void refreshResourceEditPart(EditPart parent) {
		if (parent instanceof IResourceEditPart) {
			((IResourceEditPart) parent).refreshResource();
		}
		List list = parent.getChildren();
		for (int i = 0; i < list.size(); i++) {
			EditPart part = (EditPart) list.get(i);
			refreshResourceEditPart(part);
		}
	}

	public boolean isExistModelFile() {
		if (getModel() == null) {
			return true;
		}
		File file = new File(getModel().getFileName());
		if (file.exists() && file.isFile()) {
			return true;
		}

		return false;
	}

	// private String getProjectFolder( )
	// {
	// IEditorInput input = getEditorInput( );
	// Object fileAdapter = input.getAdapter( IFile.class );
	// IFile file = null;
	// if ( fileAdapter != null )
	// file = (IFile) fileAdapter;
	// if ( file != null && file.getProject( ) != null )
	// {
	// return file.getProject( ).getLocation( ).toOSString( );
	// }
	// if ( input instanceof IPathEditorInput )
	// {
	// File fileSystemFile = ( (IPathEditorInput) input ).getPath( )
	// .toFile( );
	// return fileSystemFile.getParent( );
	// }
	// return null;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart )
	 */
	public void partBroughtToTop(IWorkbenchPart part) {
		if (part instanceof MultiPageReportEditor) {
			MultiPageReportEditor topEditor = (MultiPageReportEditor) part;
			if (topEditor.getModel() != null
					&& topEditor.getModel() != SessionHandleAdapter.getInstance().getModule()) {
				SessionHandleAdapter.getInstance().setModule(topEditor.getModel());
				updateRelatedViews();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partClosed(IWorkbenchPart part) {
		if (part == this && getModel() != null) {
			SessionHandleAdapter.getInstance().clear(getModel());
			if (getModel() != null) {
				GlobalActionFactory.removeStackActions(getModel().getCommandStack());
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
		fActivePart = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partOpened(IWorkbenchPart part) {

	}

	/**
	 * Tell me, i am activated.
	 * 
	 */
	public void handleActivation() {
		// if ( fIsHandlingActivation )
		// return;
		//
		// fIsHandlingActivation = true;
		// try
		// {
		// // TODO: check external changes of file.
		// // sanityCheckState( getEditorInput( ) );
		// }
		// finally
		// {
		// fIsHandlingActivation = false;
		// }
	}

	/**
	 * check the input is modify by file system.
	 * 
	 * @param input
	 */
	protected void sanityCheckState(IEditorInput input) {
		if (fModificationStamp == -1) {
			fModificationStamp = getModificationStamp(input);
		}

		long stamp = getModificationStamp(input);
		if (stamp != fModificationStamp) {
			// reset the stamp whether user choose sync or not to avoid endless
			// snag window.
			fModificationStamp = stamp;

			handleEditorInputChanged();
		}
	}

	/**
	 * Handles an external change of the editor's input element. Subclasses may
	 * extend.
	 */
	protected void handleEditorInputChanged() {

		String title = Messages.getString("ReportEditor.error.activated.outofsync.title"); //$NON-NLS-1$
		String msg = Messages.getString("ReportEditor.error.activated.outofsync.message"); //$NON-NLS-1$

		if (MessageDialog.openQuestion(getSite().getShell(), title, msg)) {
			IEditorInput input = getEditorInput();

			if (input == null) {
				closeEditor(isSaveOnCloseNeeded());
			} else {
				// getInputContext( ).setInput( input );
				// rebuildModel( );
				// superSetInput( input );
			}
		}
	}

	public void closeEditor(boolean save) {
		getSite().getPage().closeEditor(this, save);
	}

	protected long getModificationStamp(Object element) {
		if (element instanceof IEditorInput) {
			IReportProvider provider = getProvider();
			if (provider != null) {
				return computeModificationStamp(provider.getInputPath((IEditorInput) element));
			}
		}

		return 0;
	}

	protected long computeModificationStamp(IPath path) {
		return path.toFile().lastModified();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#dispose()
	 */
	public void dispose() {
		// dispose page
		outlineBackup.dispose();
		dataBackup.dispose();
		List list = new ArrayList(pages);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (obj instanceof IReportEditorPage) {
				((IReportEditorPage) obj).dispose();
				pages.remove(obj);
			}
		}

		// getSite( ).getWorkbenchWindow( )
		// .getPartService( )
		// .removePartListener( this );

		if (fPalettePage != null) {
			fPalettePage.dispose();
		}
		if (outlinePage != null) {
			outlinePage.dispose();
		}
		if (dataPage != null) {
			dataPage.dispose();
		}
		getSite().setSelectionProvider(null);

		// remove the mediator listener
		MediatorManager.removeGlobalColleague(this);

		if (getModel() != null) {
			getModel().close();
		}
		IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

		if (synchronizer != null) {
			synchronizer.removeListener(IReportResourceChangeEvent.LibraySaveChange
					| IReportResourceChangeEvent.ImageResourceChange | IReportResourceChangeEvent.DataDesignSaveChange,
					this);
		}

		PlatformUI.getWorkbench().removeWindowListener(windowListener);
		if (prefs != null) {
			prefs.removePreferenceChangeListener(preferenceChangeListener);
		}
		super.dispose();
	}

	protected void finalize() throws Throwable {
		if (Policy.TRACING_PAGE_CLOSE) {
			System.out.println("Report multi page finalized"); //$NON-NLS-1$
		}
		super.finalize();
	}

	public IEditorPart getEditorPart() {
		return this;
	}

	public boolean isInterested(IMediatorRequest request) {
		return request instanceof ReportRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.editors.parts.
	 * GraphicalEditorWithFlyoutPalette
	 * #performRequest(org.eclipse.birt.report.designer
	 * .core.util.mediator.request.ReportRequest)
	 */
	public void performRequest(IMediatorRequest request) {
		ReportRequest rqt = (ReportRequest) request;

		if (ReportRequest.OPEN_EDITOR.equals(request.getType()) && (rqt.getSelectionModelList().size() == 1)) {
			if (rqt.getSelectionModelList().get(0) instanceof MasterPageHandle) {
				handleOpenMasterPage(rqt);
				return;
			}

			if (rqt.getSelectionModelList().get(0) instanceof ScriptObjectNode) {
				ScriptObjectNode node = (ScriptObjectNode) rqt.getSelectionModelList().get(0);
				if (node.getParent() instanceof PropertyHandle) {
					PropertyHandle proHandle = (PropertyHandle) node.getParent();
					if (proHandle.getElementHandle().getModuleHandle().equals(getModel())) {
						handleOpenScriptPage(rqt);
					}
				}
				// handleOpenScriptPage( request );
				return;
			}

		}

		// super.performRequest( request );
	}

	/**
	 * @param request
	 */
	protected void handleOpenScriptPage(final ReportRequest request) {
		if (this.getContainer().isVisible()) {
			setActivePage(ScriptForm_ID);

			Display.getCurrent().asyncExec(new Runnable() {

				public void run() {
					ReportRequest r = new ReportRequest();
					r.setType(ReportRequest.SELECTION);

					r.setSelectionObject(request.getSelectionModelList());
					SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);
				}
			});
		}
	}

	/**
	 * @param request
	 */
	protected void handleOpenMasterPage(final ReportRequest request) {
		if (this.getContainer().isVisible()) {
			setActivePage(LayoutMasterPage_ID);

			Display.getCurrent().asyncExec(new Runnable() {

				public void run() {
					ReportRequest r = new ReportRequest();
					r.setType(ReportRequest.LOAD_MASTERPAGE);

					r.setSelectionObject(request.getSelectionModelList());
					SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);
				}
			});
		}
	}

	/**
	 * Returns current page instance if the currently selected page index is not -1,
	 * or <code>null</code> if it is.
	 * 
	 * @return active page instance if selected, or <code>null</code> if no page is
	 *         currently active.
	 */

	public IFormPage getCurrentPageInstance() {
		int index = getCurrentPage();
		if (index != -1) {
			Object page = pages.get(index);
			if (page instanceof IFormPage)
				return (IFormPage) page;
		}
		return null;
	}

	private void refreshGraphicalEditor() {
		for (int i = 0; i < pages.size(); i++) {
			Object page = pages.get(i);
			if (page instanceof IFormPage) {

				if (isGraphicalEditor(page)) {
					if (((GraphicalEditorWithFlyoutPalette) page).getGraphicalViewer() != null) {
						EditPart root = ((GraphicalEditorWithFlyoutPalette) page).getGraphicalViewer()
								.getRootEditPart();
						refreshResourceEditPart(root);
					}
				}
			}
		}
	}

	private boolean isGraphicalEditor(Object obj) {
		return obj instanceof GraphicalEditorWithFlyoutPalette;
		// return LayoutEditor_ID.equals( id ) || LayoutMasterPage_ID.equals( id
		// );
	}

	protected void setActivePage(int pageIndex) {
		super.setActivePage(pageIndex);
		// setFocus( );
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				setFocus();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.views.IReportResourceChangeListener
	 * #resourceChanged
	 * (org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent)
	 */
	public void resourceChanged(IReportResourceChangeEvent event) {
		if ((event.getType() == IReportResourceChangeEvent.ImageResourceChange)) {
			refreshGraphicalEditor();
			return;
		}
		if (event.getSource().equals(getModel())) {
			return;
		}
		Object[] resolves = ElementAdapterManager.getAdapters(getModel(), IRelatedFileChangeResolve.class);
		if (resolves == null) {
			return;
		}
		Path targetPath = (Path) event.getData();
		for (int i = 0; i < resolves.length; i++) {
			IRelatedFileChangeResolve find = (IRelatedFileChangeResolve) resolves[i];
			if (find.acceptType(event.getType())) {
				/**
				 * Check whether the resolveList already contains the same type of the new
				 * IRelatedFileChangeResolve. Add the new resolve to the resolveList only if the
				 * list doesn't contain objects have the same type of the new resolve. This
				 * judgment call tries to prevent duplicate refresh action when several
				 * resources change happens.
				 */
				if (!checkResolveListContainsType(find)) {
					if (targetPath != null) {
						if (targetPath.toOSString().equals(getModel().getFileName())) {
							addToResolveList(event, find);
						}
					} else {// if targetPath is null, follow the old logic
						addToResolveList(event, find);
					}
				}
				break;
			}
		}
	}

	private void addToResolveList(IReportResourceChangeEvent event, IRelatedFileChangeResolve find) {
		resolveList.add(find);
		needReload = find.isReload(event, getModel());
		needReset = find.isReset(event, getModel());
	}

	/**
	 * Check whether the resolveList already contains the same type of the new
	 * IRelatedFileChangeResolve.
	 * 
	 * @param find
	 * @return
	 */
	private boolean checkResolveListContainsType(IRelatedFileChangeResolve find) {
		for (int i = 0; i < resolveList.size(); i++) {
			IRelatedFileChangeResolve obj = resolveList.get(i);
			if (find.getClass().equals(obj.getClass())) {
				return true;
			}
		}
		return false;
	}
}
