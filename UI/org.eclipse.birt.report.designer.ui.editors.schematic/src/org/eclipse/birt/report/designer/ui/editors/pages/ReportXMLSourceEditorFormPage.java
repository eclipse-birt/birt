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

package org.eclipse.birt.report.designer.ui.editors.pages;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.xml.XMLEditor;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.IPageStaleType;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * XML editor for report source file.
 */
public class ReportXMLSourceEditorFormPage extends XMLEditor implements IReportEditorPage {

	public static final String ID = MultiPageReportEditor.XMLSourcePage_ID;

	private ActionRegistry registry;

	private FormEditor editor;
	private Control control;
	private int staleType;

	private int index;
	private ErrorDetail errorDetail;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.forms.editor.IFormPage#initialize(org.eclipse.ui.forms.editor.
	 * FormEditor)
	 */
	@Override
	public void initialize(FormEditor editor) {
		this.editor = editor;
		setRangeIndicator(new Annotation() {
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#getEditor()
	 */
	@Override
	public FormEditor getEditor() {
		return editor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.texteditor.AbstractTextEditor#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
		IReportProvider provider = getProvider();
		if (provider != null && getErrorLIine(false) == -1) {
			ModuleHandle model = provider.getReportModuleHandle(getEditorInput(), true);
			SessionHandleAdapter.getInstance().setReportDesignHandle(model);

			UIUtil.processSessionResourceFolder(getEditorInput(), UIUtil.getProjectFromInput(getEditorInput()), model);

			UIUtil.doFinishSave(model);
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
		} catch (Exception e) {
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
		if (list != null) {
			for (int i = 0, m = list.size(); i < m; i++) {
				Object obj = list.get(i);
				if (obj instanceof ErrorDetail) {
					ErrorDetail errorDetail = (ErrorDetail) list.get(i);
					this.errorDetail = errorDetail;
					return errorDetail.getLineNo();
				}
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#getManagedForm()
	 */
	@Override
	public IManagedForm getManagedForm() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#setActive(boolean)
	 */
	@Override
	public void setActive(boolean active) {
		int errorLine = getErrorLIine(true);

		if (errorLine > -1) {
			setFocus();
			setHighlightLine(errorLine);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#isActive()
	 */
	@Override
	public boolean isActive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#canLeaveThePage()
	 */
	@Override
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
				getEditor().doSave(null);
				break;
			case 1:
				if (getEditorInput() != null) {
					this.setInput(getEditorInput());
				}
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

	private void setHighlightLine(int errorLine) {
		try {
			IRegion region = getDocumentProvider().getDocument(getEditorInput()).getLineInformation(errorLine);
			setHighlightRange(region.getOffset(), region.getLength(), true);
		} catch (BadLocationException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	@Override
	public Control getPartControl() {
		return control;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#getIndex()
	 */
	@Override
	public int getIndex() {
		return index;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#setIndex(int)
	 */
	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#isEditor()
	 */
	@Override
	public boolean isEditor() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#selectReveal(java.lang.Object)
	 */
	@Override
	public boolean selectReveal(Object marker) {
		// int start = MarkerUtilities.getCharStart( (IMarker) marker );
		// int end = MarkerUtilities.getCharEnd( (IMarker) marker );
		//
		// boolean selectLine = start < 0 || end < 0;
		//
		// // look up the current range of the marker when the document has been
		// // edited
		// IAnnotationModel model = getDocumentProvider( ).getAnnotationModel(
		// getEditorInput( ) );
		// if ( model instanceof AbstractMarkerAnnotationModel )
		// {
		// AbstractMarkerAnnotationModel markerModel = (AbstractMarkerAnnotationModel)
		// model;
		// Position pos = markerModel.getMarkerPosition( (IMarker) marker );
		// if ( pos != null )
		// {
		// if ( !pos.isDeleted( ) )
		// {
		// // use position instead of marker values
		// start = pos.getOffset( );
		// end = pos.getOffset( ) + pos.getLength( );
		// }
		// else
		// {
		// return false;
		// }
		// }
		// }
		//
		// IDocument document = getDocumentProvider( ).getDocument( getEditorInput( ) );
		// if ( selectLine )
		// {
		// int line;
		// try
		// {
		// if ( start >= 0 )
		// line = document.getLineOfOffset( start );
		// else
		// {
		// line = MarkerUtilities.getLineNumber( (IMarker) marker );
		// // Marker line numbers are 1-based
		// if ( line >= 1 )
		// {
		// line--;
		// }
		// start = document.getLineOffset( line );
		// }
		// end = start + document.getLineLength( line ) - 1;
		// }
		// catch ( BadLocationException e )
		// {
		// return false;
		// }
		// }
		//
		// int length = document.getLength( );
		// if ( end - 1 < length && start < length )
		// selectAndReveal( start, end - start );
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		Control[] children = parent.getChildren();
		control = children[children.length - 1];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#firePropertyChange(int)
	 */
	// protected void firePropertyChange( int type )
	// {
	// if ( type == PROP_DIRTY )
	// {
	// markPageStale( IPageStaleType.CODE_CHANGED );
	// getEditor( ).editorDirtyStateChanged( );
	// }
	// else
	// super.firePropertyChange( type );
	// }
	@Override
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
			UIUtil.doFinishSave(model);
			prePage.markPageStale(IPageStaleType.NONE);
			refreshDocument();
			markPageStale(IPageStaleType.NONE);
		}
		// ser the attribute view disedit.
		ReportRequest request = new ReportRequest(ReportXMLSourceEditorFormPage.this);
		List list = new ArrayList();
		list.add(new Object());
		request.setSelectionObject(list);
		request.setType(ReportRequest.SELECTION);

		// SessionHandleAdapter.getInstance().getMediator().pushState();
		SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);

		setFocus();
		return true;
	}

	private void reloadEditorInput() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			getModel().serialize(out);
			String newInput = out.toString(getModel().getFileEncoding());
			getDocumentProvider().getDocument(getEditorInput()).set(newInput);

			getEditor().editorDirtyStateChanged();
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 * Get the MultiPageReportEditor.
	 *
	 * @return
	 */
	public MultiPageReportEditor getReportEditor() {
		return (MultiPageReportEditor) getEditor();
	}

	/**
	 * Get editing report ModuleHandle.
	 *
	 * @return
	 */
	public ModuleHandle getModel() {
		IReportProvider provider = getProvider();

		if (provider != null) {
			return provider.queryReportModuleHandle();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#markPageStale(
	 * int)
	 */
	@Override
	public void markPageStale(int type) {
		staleType = type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#getStaleType()
	 */
	@Override
	public int getStaleType() {
		return staleType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#firePropertyChange(int)
	 */
	@Override
	protected void firePropertyChange(int type) {
		super.firePropertyChange(type);
		if (type == PROP_DIRTY) {
			if (editor != null) {
				markPageStale(IPageStaleType.CODE_CHANGED);
				editor.editorDirtyStateChanged();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class required) {
		if (required.equals(ActionRegistry.class)) {
			if (registry == null) {
				registry = new ActionRegistry();
			}
			return registry;
		}
		return super.getAdapter(required);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.xml.XMLEditor#
	 * getProvider()
	 */
	@Override
	protected IReportProvider getProvider() {
		return (IReportProvider) editor.getAdapter(IReportProvider.class);
	}

	@Override
	protected void finalize() throws Throwable {
		if (Policy.TRACING_PAGE_CLOSE) {
			System.out.println("Report source page finalized"); //$NON-NLS-1$
		}
		super.finalize();
	}

}
