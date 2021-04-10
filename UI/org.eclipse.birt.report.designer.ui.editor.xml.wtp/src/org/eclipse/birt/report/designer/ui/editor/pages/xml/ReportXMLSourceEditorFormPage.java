/*************************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editor.pages.xml;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.mediator.IMediator;
import org.eclipse.birt.report.designer.core.mediator.IMediatorColleague;
import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventManager;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.ModelEventManager;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.IPageStaleType;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportFormPage;
import org.eclipse.birt.report.designer.ui.editors.schematic.action.TextSaveAction;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * XML editor for report source file.
 */
public class ReportXMLSourceEditorFormPage extends ReportFormPage implements IMediatorColleague {

	private IModelEventManager manager;
	public static final String ID = "org.eclipse.birt.report.designer.ui.editors.xmlsource"; //$NON-NLS-1$
	private static final String switchAction_ID = "switch"; //$NON-NLS-1$
	private ActionRegistry registry;
	private static final String SWITCH_REPORT_OUTLINE = Messages
			.getString("ContentOutlinePage.action.text.reportOutline"); //$NON-NLS-1$
	private static final String SWITCH_REPORT_XML_OUTLINE = Messages
			.getString("ContentOutlinePage.action.text.reportXMLSourceOutline"); //$NON-NLS-1$

	// Leverage from WST
	private StructuredTextEditor reportXMLEditor;
	private Control control;
	private int staleType;
	private boolean isModified = false;
	private boolean isLeaving = false;
	private boolean registered = false;

	private ActivityStackListener commandStackListener;
	private OutlineSwitchAction outlineSwitchAction;

	private ErrorDetail errorDetail;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editor.pages.xml.ReportFormPage#init
	 * (org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, final IEditorInput input) throws PartInitException {
		super.init(site, input);
		try {
			reportXMLEditor = new StructuredTextEditor() {

				@Override
				protected void createActions() {
					super.createActions();
					setAction(ITextEditorActionConstants.SAVE, new TextSaveAction(this));
				}

				@Override
				public void doSave(IProgressMonitor progressMonitor) {
					super.doSave(progressMonitor);
					clearDirtyFlag();
					try {
						getReportEditor().refreshMarkers(input);
					} catch (CoreException e) {
					}
				}
			};
			reportXMLEditor.init(site, input);
		} catch (Exception e) {
		}
	}

	private int getErrorLIine(boolean checkReport) {
		errorDetail = null;

		IEditorInput input = getEditorInput();

		try {
			IPath path = getProvider().getInputPath(input);

			if (path.toOSString().endsWith(IReportEditorContants.LIBRARY_FILE_EXTENTION)) {
				LibraryHandle library = null;
				try {
					library = SessionHandleAdapter.getInstance().getSessionHandle().openLibrary(path.toOSString());
					if (checkReport) {
						return getErrorLineFromModuleHandle(library);
					}

				} catch (DesignFileException e) {
					return getExpetionErrorLine(e);
				} finally {
					if (library != null) {
						library.close();
					}
				}
			} else if (path.toOSString().endsWith(IReportEditorContants.DESIGN_FILE_EXTENTION)
					|| path.toOSString().endsWith(IReportEditorContants.TEMPLATE_FILE_EXTENTION)) {
				ReportDesignHandle report = null;
				try {
					report = SessionHandleAdapter.getInstance().getSessionHandle().openDesign(path.toOSString(),
							// No need to close the stream here, the report
							// design parser will automaically close it.
							new FileInputStream(path.toFile()));
					if (checkReport) {
						return getErrorLineFromModuleHandle(report);
					}
				} catch (DesignFileException e) {
					return getExpetionErrorLine(e);
				} finally {
					if (report != null) {
						report.close();
					}
				}
			} else {
				ModuleHandle report = null;
				try {
					report = SessionHandleAdapter.getInstance().getSessionHandle().openModule(path.toOSString(),
							// No need to close the stream here, the report
							// design parser will automaically close it.
							new FileInputStream(path.toFile()));
					if (checkReport) {
						return getErrorLineFromModuleHandle(report);
					}
				} catch (DesignFileException e) {
					return getExpetionErrorLine(e);
				} finally {
					if (report != null) {
						report.close();
					}
				}
			}
		} catch (FileNotFoundException e) {
			return 0;
		}
		return -1;
	}

	private int getExpetionErrorLine(DesignFileException e) {
		List errorList = e.getErrorList();
		for (Iterator iter = errorList.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof ErrorDetail) {
				errorDetail = (ErrorDetail) element;
				return ((ErrorDetail) element).getLineNo();
			}
		}
		return 0;
	}

	private int getErrorLineFromModuleHandle(ModuleHandle handle) {
		handle.checkReport();
		List list = handle.getErrorList();
		if (list != null)
			for (int i = 0, m = list.size(); i < m; i++) {
				Object obj = list.get(i);
				if (obj instanceof ErrorDetail) {
					ErrorDetail errorDetail = (ErrorDetail) list.get(i);
					this.errorDetail = errorDetail;
					return errorDetail.getLineNo();
				}
			}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#canLeaveThePage()
	 */
	public boolean canLeaveThePage() {
		if (isDirty()) {
			MessageDialog prefDialog = new MessageDialog(UIUtil.getDefaultShell(),
					Messages.getString("XMLSourcePage.Error.Dialog.title"), //$NON-NLS-1$
					null, Messages.getString("XMLSourcePage.Error.Dialog.Message.PromptMsg"), //$NON-NLS-1$
					MessageDialog.INFORMATION,
					new String[] { Messages.getString("XMLSourcePage.Error.Dialog.Message.Yes"), //$NON-NLS-1$
							Messages.getString("XMLSourcePage.Error.Dialog.Message.No"), //$NON-NLS-1$
							Messages.getString("XMLSourcePage.Error.Dialog.Message.Cancel") }, //$NON-NLS-1$
					0);

			int ret = prefDialog.open();
			switch (ret) {
			case 0:
				isLeaving = true;
				getReportEditor().doSave(null);
				break;
			case 1:
				if (getEditorInput() != null) {
					this.setInput(getEditorInput());
				}
				clearDirtyFlag();
				break;
			case 2:
				return false;
			}
		}

		int errorLine = getErrorLIine(false);

		if (errorLine > -1) {
			if (errorDetail != null
					&& errorDetail.getErrorCode().equals(ErrorDetail.DESIGN_EXCEPTION_UNSUPPORTED_VERSION)) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						Messages.getString("XMLSourcePage.Error.Dialog.title"), //$NON-NLS-1$
						errorDetail.getMessage());
			} else {
				// Display.getCurrent( ).beep( );
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						Messages.getString("XMLSourcePage.Error.Dialog.title"), //$NON-NLS-1$
						Messages.getString("XMLSourcePage.Error.Dialog.Message.InvalidFile")); //$NON-NLS-1$
			}
			setFocus();
			setHighlightLine(errorLine);

			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.pages.ReportFormPage#setInput
	 * (org.eclipse.ui.IEditorInput)
	 */
	public void setInput(IEditorInput input) {
		super.setInput(input);
		reportXMLEditor.setInput(input);
	}

	private void setHighlightLine(int line) {
		try {
			IRegion region = reportXMLEditor.getDocumentProvider().getDocument(getEditorInput())
					.getLineInformation(--line);
			reportXMLEditor.setHighlightRange(region.getOffset(), region.getLength(), true);
		} catch (BadLocationException e) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.editor.pages.xml.ReportFormPage#
	 * selectReveal(java.lang.Object)
	 */
	public boolean selectReveal(Object marker) {
		int start = MarkerUtilities.getCharStart((IMarker) marker);
		int end = MarkerUtilities.getCharEnd((IMarker) marker);

		boolean selectLine = start < 0 || end < 0;

		// look up the current range of the marker when the document has been
		// edited
		IAnnotationModel model = reportXMLEditor.getDocumentProvider()
				.getAnnotationModel(reportXMLEditor.getEditorInput());
		if (model instanceof AbstractMarkerAnnotationModel) {
			AbstractMarkerAnnotationModel markerModel = (AbstractMarkerAnnotationModel) model;
			Position pos = markerModel.getMarkerPosition((IMarker) marker);
			if (pos != null) {
				if (!pos.isDeleted()) {
					// use position instead of marker values
					start = pos.getOffset();
					end = pos.getOffset() + pos.getLength();
				} else {
					return false;
				}
			}
		}

		IDocument document = reportXMLEditor.getDocumentProvider().getDocument(reportXMLEditor.getEditorInput());
		if (selectLine) {
			int line;
			try {
				if (start >= 0)
					line = document.getLineOfOffset(start);
				else {
					line = MarkerUtilities.getLineNumber((IMarker) marker);
					// Marker line numbers are 1-based
					if (line >= 1) {
						line--;
					}
					start = document.getLineOffset(line);
				}
				end = start + document.getLineLength(line) - 1;
			} catch (BadLocationException e) {
				return false;
			}
		}

		int length = document.getLength();
		if (end - 1 < length && start < length)
			reportXMLEditor.selectAndReveal(start, end - start);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	public Control getPartControl() {
		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getId()
	 */
	public String getId() {
		return ID;
	}

	private ActivityStackListener getCommandStackListener() {
		if (commandStackListener == null) {
			commandStackListener = new ActivityStackListener() {

				public void stackChanged(ActivityStackEvent event) {
					if (isActive() && event.getAction() != ActivityStackEvent.ROLL_BACK) {
						reloadEditorInput();
					}
				}
			};
		}
		return commandStackListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createPartControl(Composite parent) {
		reportXMLEditor.createPartControl(parent);
		Control[] children = parent.getChildren();
		control = children[children.length - 1];

		ModuleHandle model = getModel();

		// suport the mediator
		SessionHandleAdapter.getInstance().getMediator(model).addColleague(this);

		// Add Command Stack Listener
		if (model != null && model.getCommandStack() != null) {
			getCommandStack(model).addCommandStackListener(getCommandStackListener());
			hookModelEventManager(model);
		}

		reportXMLEditor.getTextViewer().addTextListener(new ITextListener() {

			public void textChanged(TextEvent event) {
				if (!isTextModified() && event.getOffset() != 0) {
					markDirty();
				}
			}
		});

		reportXMLEditor.getTextViewer().getTextWidget().addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				markDirty();
			}
		});
	}

	protected void registerOutlineSwitchAction() {
		if (registered == true)
			return;
		// Register the switch action onto outline page
		Page reportMultiBookPage = (Page) ((MultiPageReportEditor) getEditor()).getOutlinePage();
		if (reportMultiBookPage.getSite() != null) {
			if (reportMultiBookPage.getSite().getActionBars().getMenuManager()
					.find(getOutlineSwitchAction().getId()) == null)
				reportMultiBookPage.getSite().getActionBars().getMenuManager().add(getOutlineSwitchAction());
			registered = true;
		}
	}

	private void removeOutlineSwitchAction() {
		Page reportMultiBookPage = (Page) ((MultiPageReportEditor) getEditor()).getOutlinePage();
		if (reportMultiBookPage.getSite() != null) {
			reportMultiBookPage.getSite().getActionBars().getMenuManager().remove(switchAction_ID);
			registered = false;
		}
	}

	public void setIsModified(boolean modified) {
		isModified = modified;
	}

	private boolean isTextModified() {
		return isModified;
	}

	protected void markDirty() {
		setIsModified(true);
		getEditor().editorDirtyStateChanged();
	}

	private WrapperCommandStack getCommandStack(ModuleHandle model) {
		if (model != null) {
			return new WrapperCommandStack(model.getCommandStack());
		}
		return null;
	}

	private void reloadEditorInput() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			getModel().serialize(out);
			String newInput = out.toString(getModel().getFileEncoding());

			reportXMLEditor.getDocumentProvider().getDocument(getEditorInput()).set(newInput);
			markDirty();
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		}
	}

	public boolean isInterested(IMediatorRequest request) {
		return request instanceof ReportRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.core.util.mediator.IColleague#performRequest
	 * ( org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest )
	 */
	public void performRequest(IMediatorRequest request) {
		if (ReportRequest.SELECTION.equals(request.getType())
				&& !(request.getSource() instanceof ReportXMLSourceEditorFormPage) && isActive()
				&& request.getSource() instanceof DesignerOutlinePage) {
			handleSelectionChange((ReportRequest) request);
		}
	}

	private void handleSelectionChange(ReportRequest request) {
		List selectedObjects = request.getSelectionObject();
		ModuleHandle model = ((DesignerOutlinePage) request.getSource()).getRoot();
		if (!selectedObjects.isEmpty()) {
			setHighlightLine(model.getLineNo(selectedObjects.get(0)));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#onBroughtToTop
	 * (org.eclipse.birt.report.designer.ui.editors.IReportEditorPage)
	 */
	public boolean onBroughtToTop(IReportEditorPage prePage) {
		if (getEditorInput() != prePage.getEditorInput()) {
			setInput(prePage.getEditorInput());
		}
		if (getStaleType() == IPageStaleType.MODEL_RELOAD) {
			reloadEditorInput();
			doSave(null);
		} else if (prePage != this && (prePage.isDirty() || prePage.getStaleType() != IPageStaleType.NONE)) {
			ModuleHandle model = getModel();

			if (model != null
					&& ModuleUtil.compareReportVersion(ModuleUtil.getReportVersion(), model.getVersion()) > 0) {
				if (!MessageDialog.openConfirm(UIUtil.getDefaultShell(),
						Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.Title"), //$NON-NLS-1$
						Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.Message"))) //$NON-NLS-1$
				{
					return false;
				}
			}
			prePage.doSave(null);
			UIUtil.doFinishSave(getModel());
			prePage.markPageStale(IPageStaleType.NONE);
			refreshDocument();
			markPageStale(IPageStaleType.NONE);
		}

		ModuleHandle model = getModel();

		hookModelEventManager(model);
		// Fix bug 276266
		SessionHandleAdapter.getInstance().getMediator(model).addColleague(this);
		// ser the attribute view disedit.

		IMediator mediator = SessionHandleAdapter.getInstance().getMediator(model);
		ReportRequest request = new ReportRequest(ReportXMLSourceEditorFormPage.this);
		Object data = mediator.getState().getData();

		List list = new ArrayList();
		if (data instanceof List) {
			list.addAll((List) data);
		} else if (data != null) {
			list.add(data);
		}

		if (list.isEmpty()) {
			list.add(new Object());
		} else {
			setHighlightLine(model.getLineNo(list.get(0)));
		}
		request.setSelectionObject(list);
		request.setType(ReportRequest.SELECTION);

		mediator.notifyRequest(request);

		reportXMLEditor.setFocus();
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
		staleType = type;
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
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class required) {
		if (required.equals(ITextEditor.class)) {
			return reportXMLEditor;
		}
		if (required.equals(ActionRegistry.class)) {
			if (registry == null) {
				registry = new ActionRegistry();
			}
			return registry;
		} else if (IContentOutlinePage.class.equals(required)) {
			if (getModel() != null) {
				DesignerOutlinePage outlinePage = new DesignerOutlinePage(getModel());
				getModelEventManager().addModelEventProcessor(outlinePage.getModelProcessor());
				registerOutlineSwitchAction();
				getOutlineSwitchAction().setText(SWITCH_REPORT_OUTLINE);
				return outlinePage;
			}
		} else if (ContentOutlinePage.class.equals(required)) {
			getOutlineSwitchAction().setText(SWITCH_REPORT_XML_OUTLINE);
			return reportXMLEditor.getAdapter(IContentOutlinePage.class);
		}
		return super.getAdapter(required);
	}

	protected IModelEventManager getModelEventManager() {
		if (manager == null) {
			return manager = createModelEventManager();
		}
		return manager;
	}

	protected IModelEventManager createModelEventManager() {
		return new ModelEventManager();
	}

	protected void hookModelEventManager(ModuleHandle model) {
		getModelEventManager().hookRoot(model);
		getModelEventManager().hookCommandStack(getCommandStack(model));
	}

	protected void unhookModelEventManager(ModuleHandle model) {
		getModelEventManager().unhookRoot(model);
		// getModelEventManager( ).unhookCommandStack( getCommandStack( ) );
	}

	protected void finalize() throws Throwable {
		if (Policy.TRACING_PAGE_CLOSE) {
			System.out.println("Report source page finalized"); //$NON-NLS-1$
		}
		super.finalize();
	}

	public void refreshDocument() {
		setInput(getEditorInput());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#doSave(org.eclipse.core.
	 * runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor progressMonitor) {
		reportXMLEditor.doSave(progressMonitor);
		UIUtil.doFinishSave(getModel());
	}

	/**
	 * Clears the dirty flag.
	 */
	private void clearDirtyFlag() {
		IReportProvider provider = getProvider();
		if (provider != null && getErrorLIine(false) == -1) {
			ModuleHandle oldModel = getModel();
			unhookModelEventManager(oldModel);
			getCommandStack(oldModel).removeCommandStackListener(getCommandStackListener());

			SessionHandleAdapter.getInstance().getMediator(oldModel).removeColleague(this);

			// refersh the model
			ModuleHandle newModel = provider.getReportModuleHandle(getEditorInput(), true);
			SessionHandleAdapter.getInstance().setReportDesignHandle(newModel);
			UIUtil.processSessionResourceFolder(getEditorInput(), UIUtil.getProjectFromInput(getEditorInput()),
					newModel);

			SessionHandleAdapter.getInstance().getMediator(newModel).addColleague(this);
			hookModelEventManager(newModel);
			getCommandStack(newModel).addCommandStackListener(getCommandStackListener());

			setIsModified(false);
			getEditor().editorDirtyStateChanged();
			if (isActive() && !isLeaving) {
				getReportEditor().reloadOutlinePage();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		// Do nothing, SavaAs is not allowed by default
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.editor.pages.xml.ReportFormPage#
	 * isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty() {
		return reportXMLEditor.isDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		super.dispose();
		((MultiPageEditorSite) getSite()).dispose();

		reportXMLEditor.dispose();
		reportXMLEditor = null;

		unhookModelEventManager(getModel());

		SessionHandleAdapter.getInstance().getMediator(getModel()).removeColleague(this);
	}

	protected TextEditor getTextEditor() {
		return reportXMLEditor;
	}

	protected OutlineSwitchAction getOutlineSwitchAction() {
		if (outlineSwitchAction == null) {
			outlineSwitchAction = new OutlineSwitchAction();
		}
		return outlineSwitchAction;
	}

	protected class OutlineSwitchAction extends Action {

		public OutlineSwitchAction() {
			setText(SWITCH_REPORT_OUTLINE);
			setEnabled(true);
			setId(switchAction_ID);
		}

		public void run() {
			getReportEditor().outlineSwitch();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#setActive(boolean)
	 */
	public void setActive(boolean active) {
		super.setActive(active);
		if (active == false) {
			removeOutlineSwitchAction();
		}
	}
}
