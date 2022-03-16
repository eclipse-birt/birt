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
import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewTreeViewerPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage;
import org.eclipse.birt.report.designer.ui.views.attributes.IAttributeViewPage;
import org.eclipse.birt.report.designer.ui.views.data.IDataViewPage;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.MultiPageEditorSite;

/**
 *
 */

public class LibraryLayoutEditorFormPage extends LibraryLayoutEditor implements IReportEditorPage {

	public static final String ID = "org.eclipse.birt.report.designer.ui.editors.library.layout"; //$NON-NLS-1$

	private int index;

	private FormEditor editor;

	private Control control;

	private int staleType;

	private boolean alreadyShow = false;

	private ActivityStackListener commandStackListener = new ActivityStackListener() {

		@Override
		public void stackChanged(ActivityStackEvent event) {
			updateStackActions();
			getEditor().editorDirtyStateChanged();
			staleType = IPageStaleType.MODEL_CHANGED;
		}
	};

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack();
		if (stack != null) {
			stack.addCommandStackListener(getCommandStackListener());
		}
	}

	/**
	 * returns command stack listener.
	 */
	public ActivityStackListener getCommandStackListener() {
		return commandStackListener;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.forms.editor.IFormPage#initialize(org.eclipse.ui.forms
	 * .editor.FormEditor)
	 */
	@Override
	public void initialize(FormEditor editor) {
		this.editor = editor;
	}

	@Override
	public FormEditor getEditor() {
		return editor;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		initialize((FormEditor) ((MultiPageEditorSite) site).getMultiPageEditor());
		// Initializes command stack
		// WrapperCommandStack stack = (WrapperCommandStack) getCommandStack( );
		// if ( stack != null )
		// {
		// stack.addCommandStackListener( getCommandStackListener( ) );
		// }
	}

	@Override
	public IManagedForm getManagedForm() {
		return null;
	}

	@Override
	public void setActive(boolean active) {
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public boolean canLeaveThePage() {
		return true;
	}

	@Override
	public Control getPartControl() {
		return control;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public boolean isEditor() {
		return true;
	}

	@Override
	public boolean selectReveal(Object object) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		Control[] children = parent.getChildren();
		control = children[children.length - 1];
	}

	@Override
	public boolean onBroughtToTop(IReportEditorPage page) {
		String prompt = ReportPlugin.getDefault().getPreferenceStore()
				.getString(ReportPlugin.LIBRARY_WARNING_PREFERENCE);

		if (!alreadyShow && (prompt == null || (!ReportPlugin.getDefault().getPreferenceStore()
				.getString(ReportPlugin.LIBRARY_WARNING_PREFERENCE).equals(MessageDialogWithToggle.NEVER)))) {
			alreadyShow = true;
			MessageDialogWithToggle dialog = MessageDialogWithToggle.openInformation(UIUtil.getDefaultShell(),
					Messages.getString("LibraryLayoutEditorFormPage.warning.title"), //$NON-NLS-1$
					Messages.getString("LibraryLayoutEditorFormPage.warning.message"), //$NON-NLS-1$
					Messages.getString("LibraryLayoutEditorFormPage.warning.prompt"), //$NON-NLS-1$
					false, ReportPlugin.getDefault().getPreferenceStore(), ReportPlugin.LIBRARY_WARNING_PREFERENCE);
			// if dialog.getToggleState() == true then means not show again.
			if (dialog.getToggleState()) {
				ReportPlugin.getDefault().getPreferenceStore().setValue(ReportPlugin.LIBRARY_WARNING_PREFERENCE,
						MessageDialogWithToggle.NEVER);
			}
		}

		// the three classes has the logic to rebuild the model, should be
		// refactor.
		ModuleHandle newModel = getProvider().queryReportModuleHandle();
		boolean reload = false;
		if (getStaleType() == IPageStaleType.MODEL_RELOAD) {
			setModel(null);
			doSave(null);
			reload = true;
		}
		if ((newModel != null && getModel() != newModel) || reload) {
			ModuleHandle oldModel = getModel();

			setModel(newModel);

			rebuildReportDesign(oldModel);
			if (getModel() != null) {
				this.getGraphicalViewer().setContents(getModel());
				hookModelEventManager(getModel());
				markPageStale(IPageStaleType.NONE);
			}
			updateStackActions();
		}
		// reselect the selection
		GraphicalViewer view = getGraphicalViewer();

		if (view != null) {
			UIUtil.resetViewSelection(view, true);
		}
		return true;
	}

	/**
	 * Rebuild report design model.
	 *
	 * @param oldModel
	 */
	protected void rebuildReportDesign(ModuleHandle oldModel) {
		// Initializes command stack
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack();
		if (stack != null) {
			stack.removeCommandStackListener(getCommandStackListener());
			stack.setActivityStack(getModel().getCommandStack());
			stack.addCommandStackListener(getCommandStackListener());
		}

		// Resets the mediator
		SessionHandleAdapter.getInstance().resetReportDesign(oldModel, getModel());

		SessionHandleAdapter.getInstance().setReportDesignHandle(getModel());
		UIUtil.processSessionResourceFolder(getEditorInput(), UIUtil.getProjectFromInput(getEditorInput()), getModel());

	}

	@Override
	public void dispose() {
		if (getCommandStack() != null && getCommandStack() instanceof WrapperCommandStack) {
			WrapperCommandStack stack = (WrapperCommandStack) getCommandStack();
			stack.removeCommandStackListener(getCommandStackListener());
		}
		super.dispose();
	}

	@Override
	public void markPageStale(int type) {
		staleType = type;
	}

	@Override
	public int getStaleType() {
		return staleType;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IDataViewPage.class) {
			DataViewTreeViewerPage page = new DataViewTreeViewerPage(getModel());
			getModelEventManager().addModelEventProcessor(page.getModelProcessor());
			return page;
		} else if (adapter == IAttributeViewPage.class) {
			AttributeViewPage page = new AttributeViewPage(getModel());
			return page;
		}
		return super.getAdapter(adapter);
	}

	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
	}

	@Override
	protected IReportProvider getProvider() {
		return (IReportProvider) editor.getAdapter(IReportProvider.class);
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
}
