/*******************************************************************************
 * Copyright (c) 2004, 2005, 2024 Actuate Corporation and others
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

package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.report.designer.data.ui.util.IHelpConstants;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;

/**
 * Utility class to handle properties
 *
 */
abstract class PropertyHandleInputDialog extends StatusDialog {

	private Object structureOrHandle = null;

	private static final String NEW_ACTION = Messages.getString("PropertyHandleInputDialog.Action.New");//$NON-NLS-1$
	private static final String EDIT_ACTION = Messages.getString("PropertyHandleInputDialog.Action.Edit");//$NON-NLS-1$

	protected PropertyHandleInputDialog(Object structureOrHandle) {
		super(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		setHelpAvailable(false);

		this.structureOrHandle = structureOrHandle;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.window.Window#create()
	 */
	@Override
	public void create() {
		super.create();

		Point pt = getShell().computeSize(-1, -1);
		pt.x = Math.max(pt.x, 520);
		pt.y = Math.max(pt.y, 250);
		getShell().setSize(pt);
		getShell().setText(getTitle());
	}

	protected String getTitle() {
		return structureOrHandle instanceof Structure ? NEW_ACTION : EDIT_ACTION;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.
	 * Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		composite.setLayout(layout);

		createCustomControls(composite);
		validateSyntax();

		addListeners();

		setSystemHelp(composite);

		return composite;
	}

	protected void setSystemHelp(Composite composite) {
		Utility.setSystemHelp(composite, IHelpConstants.CONEXT_ID_PROPERTYHANDLE_DIALOG);
	}

	/**
	 * Create customized controls
	 *
	 * @param parent
	 */
	protected abstract void createCustomControls(Composite parent);

	protected void validateSyntax() {
		IStatus status = validateSyntax(structureOrHandle);

		if (status != null) {
			updateStatus(status);
		}
	}

	/**
	 * Syntax check which determines whether to enable the Ok button
	 *
	 * @param structureOrHandle
	 * @return the validation status
	 */
	protected abstract IStatus validateSyntax(Object structureOrHandle);

	private void addListeners() {
		getShell().addListener(SWT.Close, new Listener() {

			@Override
			public void handleEvent(Event event) {
				rollback();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if (validateSemantics()) {
			super.okPressed();
		}
	}

	protected boolean validateSemantics() {
		IStatus status = validateSemantics(structureOrHandle);

		if (status == null) {
			return true;
		}

		updateStatus(status);

		return status.getSeverity() == IStatus.OK;
	}

	/**
	 * Semantics check which determines whether to close the window
	 *
	 * @param structureOrHandle
	 * @return the validation status
	 */
	protected abstract IStatus validateSemantics(Object structureOrHandle);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
	 */
	@Override
	protected void cancelPressed() {
		rollback();

		super.cancelPressed();
	}

	/**
	 * Roll back to the original status when necessary.
	 *
	 */
	protected abstract void rollback();

	protected Object getStructureOrHandle() {
		return structureOrHandle;
	}

	/**
	 * Is property blank
	 *
	 * @param value property value
	 * @return true, if the property is blank
	 */
	protected boolean isBlankProperty(String value) {
		return Utility.getNonNullString(value).trim().length() == 0;
	}

	/**
	 * Get the property status for blank value
	 *
	 * @param cellLabel cell label
	 * @return the property status for blank value
	 */
	protected Status getBlankPropertyStatus(String cellLabel) {
		return getMiscStatus(IStatus.ERROR,
				Messages.getFormattedString("PropertyHandleInputDialog.messages.error.blankProperty", //$NON-NLS-1$
						new String[] { cellLabel.replace("&", "") }));
	}

	/**
	 * Get the ok status
	 *
	 * @return the ok status
	 */
	protected Status getOKStatus() {
		return getMiscStatus(IStatus.OK, ""); //$NON-NLS-1$
	}

	/**
	 * Get miscellaneous status
	 *
	 * @param severity status level
	 * @param message  status message
	 * @return miscellaneous status
	 */
	protected Status getMiscStatus(int severity, String message) {
		return new Status(severity, PlatformUI.PLUGIN_ID, IStatus.OK, message, null);
	}

	/**
	 * Set property value
	 *
	 * @param obj          object
	 * @param propertyName property name
	 * @param value        property value
	 * @return the ok status after property set
	 */
	protected Status setProperty(Object obj, String propertyName, Object value) {
		try {
			Utility.setProperty(obj, propertyName, value);
		} catch (Exception e) {
			return getMiscStatus(IStatus.ERROR, Utility.getNonNullString(e.getMessage()));
		}

		return getOKStatus();
	}

	/**
	 * Get the property
	 *
	 * @param obj          object
	 * @param propertyName property name
	 * @return the property
	 */
	protected Object getProperty(Object obj, String propertyName) {
		try {
			return Utility.getProperty(obj, propertyName);
		} catch (Exception e) {
			updateStatus(getMiscStatus(IStatus.ERROR, Utility.getNonNullString(e.getMessage())));
		}

		return null;
	}

}
