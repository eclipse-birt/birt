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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ExportElementDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.MessageBox;

/**
 * 
 */

public class ExportElementToLibraryAction extends AbstractViewAction {
	private static final String DISPLAY_TEXT = Messages.getString("ExportToLibraryAction.action.text"); //$NON-NLS-1$

	public ExportElementToLibraryAction(Object selectedObject) {
		super(selectedObject, DISPLAY_TEXT);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isEnabled()
	 */
	public boolean isEnabled() {
		// will implement it later.
		Object selection = getSelection();
		if (selection instanceof StructuredSelection) {
			if (((StructuredSelection) selection).size() > 1) {
				return false;
			}
			selection = ((StructuredSelection) selection).getFirstElement();
		}
		if (selection instanceof ModuleHandle) {
			return false;
		} else if (selection instanceof DesignElementHandle) {
			return ElementExportUtil.canExport((DesignElementHandle) selection, true);

		} else if (selection instanceof StructureHandle) {
			return ElementExportUtil.canExport((StructureHandle) selection, true);
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {

		// ExportReportWizard exportReportWizard = new ExportReportWizard( );
		// WizardDialog wDialog = new WizardDialog( UIUtil.getDefaultShell( ),
		// exportReportWizard );
		// wDialog.setPageSize( 500, 250 );
		// wDialog.open( );

		boolean check = chcekSelectionName();
		if (check) {
			ExportElementDialog dialog = new ExportElementDialog(getSelection());
			dialog.open();
		}

	}

	/**
	 * @return <code>true</code> if the structure or element has a name. Otherwise
	 *         <code>false</code>.
	 */
	private boolean chcekSelectionName() {
		Object selection = getSelection();
		if (selection instanceof StructuredSelection) {
			if (((StructuredSelection) selection).size() > 1) {
				return false;
			}
			selection = ((StructuredSelection) selection).getFirstElement();
		}

		boolean isNameNull = false;
		if (selection instanceof DesignElementHandle) {
			isNameNull = StringUtil.isBlank(((DesignElementHandle) selection).getName());
		} else if (selection instanceof StructureHandle) {
			isNameNull = (ElementExportUtil.canExport((StructureHandle) selection, false) == false)
					&& (ElementExportUtil.canExport((StructureHandle) selection, true) == true);
		}

		if (!isNameNull) {
			return true;
		}

		SetNameAction renameAction = new SetNameAction(selection);
		if (renameAction.isEnabled() == false) {
			MessageBox box = new MessageBox(UIUtil.getDefaultShell());
			box.setText(Messages.getString("ExportElementToLibraryAction.waring"));
			box.setMessage(Messages.getString("ExportElementToLibraryAction.waringMsg"));
			return false;
		}

		renameAction.run();
		if (renameAction.isOkClicked()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * This class represents the rename action
	 */

	public static class SetNameAction extends Action {

		/**
		 * the default text
		 */
		public static final String TEXT = Messages.getString("ExportElementToLibraryAction.SetNameAction.text"); //$NON-NLS-1$

		private Object selectedObj;

		private String originalName;

		private static final String ERROR_TITLE = Messages
				.getString("ExportElementToLibraryAction.DialogTitle.setNameFailed"); //$NON-NLS-1$

		private static final String TRANS_LABEL = Messages.getString("ExportElementToLibraryAction.TransLabel.Setname"); //$NON-NLS-1$

		private boolean reNameSucceed = false;

		private boolean clickOK = false;

		/**
		 * Create a new rename action under the specific viewer
		 * 
		 * @param sourceViewer the source viewer
		 * 
		 */
		public SetNameAction(Object obj) {
			this(obj, TEXT);
			this.selectedObj = obj;
		}

		/**
		 * Create a new rename action under the specific viewer with the given text
		 * 
		 * @param sourceViewer the source viewer
		 * @param text         the text of the action
		 */
		public SetNameAction(Object obj, String text) {
			super(text);
			this.selectedObj = obj;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.IAction#isEnabled()
		 */
		public boolean isEnabled() {

			if (selectedObj instanceof EmbeddedImageHandle) {
				return true;
			}
			if (selectedObj instanceof ReportElementHandle) {
				if (selectedObj instanceof GroupHandle) {
					return !((GroupHandle) selectedObj).getPropertyHandle(IGroupElementModel.GROUP_NAME_PROP)
							.isReadOnly();
				}
				return ((ReportElementHandle) selectedObj).getDefn().getNameOption() != MetaDataConstants.NO_NAME
						&& ((ReportElementHandle) selectedObj).canEdit();
			}
			// No report element selected
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.IAction#run()
		 */
		public void run() {
			doRename();
		}

		private void doRename() {

			if (selectedObj instanceof DesignElementHandle || selectedObj instanceof EmbeddedImageHandle) {
				initOriginalName();
				InputDialog inputDialog = new InputDialog(UIUtil.getDefaultShell(),
						Messages.getString("ExportElementToLibraryAction.DialogTitle"), //$NON-NLS-1$
						Messages.getString("ExportElementToLibraryAction.DialogMessage"), //$NON-NLS-1$
						originalName, null);
				inputDialog.create();
				clickOK = false;
				if (inputDialog.open() == Window.OK) {
					saveChanges(inputDialog.getValue().trim());
					clickOK = true;
				}
			}
		}

		private void initOriginalName() {

			if (selectedObj instanceof DesignElementHandle) {
				originalName = ((DesignElementHandle) selectedObj).getName();
			}
			if (selectedObj instanceof EmbeddedImageHandle) {
				originalName = ((EmbeddedImageHandle) selectedObj).getName();
			}

			if (originalName == null) {
				originalName = ""; //$NON-NLS-1$
			}
		}

		private void saveChanges(String newName) {
			if (!newName.equals(originalName)) {
				reNameSucceed = rename(selectedObj, newName);
				if (!reNameSucceed) {
					// failed to rename, do again
					doRename();
					return;
				}
			}
		}

		public boolean reNameSucceed() {
			return reNameSucceed;
		}

		public boolean isOkClicked() {
			return clickOK;
		}

		/**
		 * Perform renaming
		 * 
		 * @param handle  the handle of the element to rename
		 * @param newName the newName to set
		 * @return Returns true if perform successfully,or false if failed
		 */
		private boolean rename(Object handle, String newName) {
			if (newName.length() == 0) {
				newName = null;
			}
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(TRANS_LABEL + " " + DEUtil.getDisplayLabel(handle)); //$NON-NLS-1$
			try {
				if (handle instanceof DesignElementHandle) {
					((DesignElementHandle) handle).setName(newName);
				}

				if (handle instanceof EmbeddedImageHandle) {
					((EmbeddedImageHandle) handle).setName(newName);
				}
				stack.commit();
			} catch (NameException e) {
				ExceptionHandler.handle(e, ERROR_TITLE, e.getLocalizedMessage());
				stack.rollback();
				return false;
			} catch (SemanticException e) {
				ExceptionHandler.handle(e, ERROR_TITLE, e.getLocalizedMessage());
				stack.rollback();
				// If set EmbeddedImage name error, then use former name;
				return true;
			}
			return true;
		}
	}

}
