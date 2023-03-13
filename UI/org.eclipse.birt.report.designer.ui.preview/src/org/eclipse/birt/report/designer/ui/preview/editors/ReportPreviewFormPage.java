/*************************************************************************************
 * Copyright (c) 2006 Actuate Corporation and others.
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

package org.eclipse.birt.report.designer.ui.preview.editors;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.preview.extension.IViewer;
import org.eclipse.birt.report.designer.ui.preview.extension.ViewerExtensionManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.EditorPart;

/**
 * This class is an <code>EditorPart</code> for preview report page.
 */
public class ReportPreviewFormPage extends EditorPart implements IReportEditorPage {

	/** The ID of current plug-in */
	public static final String ID = "org.eclipse.birt.report.designer.ui.editors.preview"; //$NON-NLS-1$

	/** The ID of default viewer */
	private static final String VIEWER_ID = "org.eclipse.birt.report.designer.ui.preview.static_html"; //$NON-NLS-1$

	/** The stale type */
	private int staleType;

	/** The form editor */
	private FormEditor editor;

	/** The report viewer */
	private final IViewer reportViewer;

	/**
	 * Creates <code>ReportPreviewFormPage</code>
	 *
	 * @throws FrameworkException if occurs error when create viewer
	 */
	public ReportPreviewFormPage() throws FrameworkException {
		super();
		ViewerExtensionManager manager = (ViewerExtensionManager) Platform
				.createFactoryObject(ViewerExtensionManager.VIEWER_EXTENSION_MANAGER_ID);

		reportViewer = manager.createViewer(VIEWER_ID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#onBroughtToTop(
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage)
	 */
	@Override
	public boolean onBroughtToTop(IReportEditorPage prePage) {
		startRender();
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	@Override
	public Control getPartControl() {
		if (reportViewer instanceof SWTAbstractViewer) {
			return ((SWTAbstractViewer) reportViewer).getUI();
			// reportViewer.setReportDesignFile( getReportDesignFilePath( ) );
		}
		return null;
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
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		if (reportViewer instanceof SWTAbstractViewer) {
			((SWTAbstractViewer) reportViewer).createUI(parent);
			// reportViewer.setReportDesignFile( getReportDesignFilePath( ) );
		}
	}

	private void startRender() {
		if (reportViewer == null) {
			return;
		}
		reportViewer.setInput(getReportDesignFilePath());
		reportViewer.render();
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
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return false;
	}

	protected IReportProvider getProvider() {
		return (IReportProvider) editor.getAdapter(IReportProvider.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(ActionRegistry.class)) {
			return new ActionRegistry();
		}
		return super.getAdapter(adapter);
	}

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
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#getIndex()
	 */
	@Override
	public int getIndex() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#setIndex(int)
	 */
	@Override
	public void setIndex(int index) {
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
	public boolean selectReveal(Object object) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#firePropertyChange(int)
	 */
	@Override
	protected void firePropertyChange(int type) {
		if (type == PROP_DIRTY) {
			editor.editorDirtyStateChanged();
		} else {
			super.firePropertyChange(type);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		if (Policy.TRACING_PAGE_CLOSE) {
			System.out.println("Report preview page finalized"); //$NON-NLS-1$
		}
		super.finalize();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	private String getReportDesignFilePath() {
		IEditorInput input = getEditorInput();

		if (input != null) {
			IReportProvider provider = getProvider();

			if (provider != null) {
				return provider.getInputPath(input).toOSString();
			}
		}
		return null;
	}
}
