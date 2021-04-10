/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.fieldassist;

import org.eclipse.birt.chart.ui.swt.interfaces.IAssistField;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * The class is used to wrap a control to support field assist function.
 * 
 * @since 2.3
 */

public abstract class AssistField implements IAssistField {

	protected Menu quickFixMenu;

	protected ControlDecoration controlDecoration;

	protected Control control;

	protected IControlContentAdapter contentAdapter;

	protected FieldDecoration errorDecoration, warningDecoration;

	protected boolean hasContentAssist = false;

	/**
	 * Constructor of the class.
	 * 
	 * @param control   the control to be decorated.
	 * @param composite The SWT composite within which the decoration should be
	 *                  rendered. The decoration will be clipped to this composite,
	 *                  but it may be rendered on a child of the composite. The
	 *                  decoration will not be visible if the specified composite or
	 *                  its child composites are not visible in the space relative
	 *                  to the control, where the decoration is to be rendered. If
	 *                  this value is null, then the decoration will be rendered on
	 *                  whichever composite (or composites) are located in the
	 *                  specified position.
	 * @param adapter   a content adapter to be used to set and retrieve content
	 *                  from current control.
	 */
	public AssistField(Control control, Composite composite, IControlContentAdapter adapter) {
		this(control, composite, adapter, null);
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param control   the control to be decorated.
	 * @param composite The SWT composite within which the decoration should be
	 *                  rendered. The decoration will be clipped to this composite,
	 *                  but it may be rendered on a child of the composite. The
	 *                  decoration will not be visible if the specified composite or
	 *                  its child composites are not visible in the space relative
	 *                  to the control, where the decoration is to be rendered. If
	 *                  this value is null, then the decoration will be rendered on
	 *                  whichever composite (or composites) are located in the
	 *                  specified position.
	 * @param adapter   a content adapter to be used to set and retrieve content
	 *                  from current control.
	 * @param values    the available contents of current control.
	 */
	public AssistField(Control control, Composite composite, IControlContentAdapter adapter, String[] values) {
		this.controlDecoration = FieldAssistHelper.getInstance().createControlDecoration(control, composite);
		this.contentAdapter = adapter;
		this.control = control;

		setContent(values);

		initAssistListeners();
	}

	/**
	 * Initialize listeners.
	 */
	protected void initAssistListeners() {
		// extension class should initialize validation listener and quick fix
		// listener in the method.
		initModifyListener();

		// Initialize quick fix menu.
		initQuickFixMenu();
	}

	/**
	 * Initialize modify listener for current field.
	 */
	protected void initModifyListener() {
		// Subclass will implement the method.
	}

	/**
	 * Initialize quick fix menu for content assist.
	 */
	protected void initQuickFixMenu() {
		if (hasQuickFix()) {
			controlDecoration.addMenuDetectListener(new MenuDetectListener() {

				public void menuDetected(MenuDetectEvent event) {
					// no quick fix if we aren't in error state.
					if (isValid()) {
						return;
					}
					if (quickFixMenu == null) {
						quickFixMenu = FieldAssistHelper.getInstance().createQuickFixMenu(AssistField.this);
					}
					quickFixMenu.setLocation(event.x, event.y);
					quickFixMenu.setVisible(true);
				}
			});
		}
	}

	/**
	 * Set contents to the field.
	 * 
	 * @param values
	 */
	public void setContent(String[] values) {
		if (values == null || values.length == 0) {
			return;
		}

		hasContentAssist = true;
		FieldAssistHelper.getInstance().installContentProposalAdapter(control, contentAdapter, values);
	}

	/**
	 * Check if the field is required.
	 * 
	 * @return
	 */
	public boolean isRequiredField() {
		return false;
	}

	/**
	 * Check if the quick fix function exists.
	 * 
	 * @return
	 */
	public boolean hasQuickFix() {
		return false;
	}

	/**
	 * The method executes quick fix.
	 */
	public void quickFix() {
		// do nothing, just implement in subclass.
	}

	/**
	 * check if content assist is enabled.
	 * 
	 * @return
	 */
	public boolean hasContentAssist() {
		return hasContentAssist;
	}

	/**
	 * Dispose resource.
	 */
	public void dispose() {
		if (quickFixMenu != null) {
			quickFixMenu.dispose();
			quickFixMenu = null;
		}
	}

	/**
	 * Returns error decoration.
	 * 
	 * @return
	 */
	public FieldDecoration getErrorDecoration() {
		if (errorDecoration == null) {
			FieldDecoration standardError;
			if (hasQuickFix()) {
				standardError = FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR_QUICKFIX);
			} else {
				standardError = FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
			}
			if (getErrorMessage() == null) {
				errorDecoration = standardError;
			} else {
				errorDecoration = new FieldDecoration(standardError.getImage(), getErrorMessage());
			}
		}

		if (getErrorMessage() != null) {
			errorDecoration.setDescription(getErrorMessage());
		}
		return errorDecoration;

	}

	/**
	 * Returns warning decoration.
	 * 
	 * @return
	 */
	public FieldDecoration getWarningDecoration() {
		if (warningDecoration == null) {
			FieldDecoration standardWarning = FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);
			if (getWarningMessage() == null) {
				warningDecoration = standardWarning;
			} else {
				warningDecoration = new FieldDecoration(standardWarning.getImage(), getWarningMessage());
			}
		}
		return warningDecoration;
	}

	/**
	 * Returns contents.
	 * 
	 * @return
	 */
	public String getContents() {
		return contentAdapter.getControlContents(control);
	}

	/**
	 * Returns content adapter.
	 * 
	 * @return
	 */
	public IControlContentAdapter getContentAdapter() {
		return contentAdapter;
	}

	/**
	 * Set contents.
	 * 
	 * @param contents
	 */
	public void setContents(String contents) {
		contentAdapter.setControlContents(control, contents, contents.length());
	}

	/**
	 * Check if input content is valid.
	 * 
	 * @return
	 */
	public abstract boolean isValid();

	/**
	 * Check if input content has warning.
	 * 
	 * @return
	 */
	public abstract boolean isWarning();

	/**
	 * Returns error message.
	 * 
	 * @return
	 */
	public String getErrorMessage() {
		return null;
	}

	/**
	 * Returns warning message.
	 * 
	 * @return
	 */
	public String getWarningMessage() {
		return null;
	}
}
