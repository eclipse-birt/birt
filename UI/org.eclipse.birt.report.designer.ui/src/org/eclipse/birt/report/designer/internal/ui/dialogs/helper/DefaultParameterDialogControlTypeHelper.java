/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs.helper;

import java.util.List;

import org.eclipse.birt.report.designer.ui.dialogs.ParameterDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * DefaultParameterDialogControlTypeHelper
 */
public class DefaultParameterDialogControlTypeHelper extends AbstractDialogHelper {

	protected static final IChoiceSet CONTROL_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getChoiceSet(DesignChoiceConstants.CHOICE_PARAM_CONTROL);

	protected Combo controlTypeChooser;

	@Override
	public void createContent(Composite parent) {
		controlTypeChooser = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
		controlTypeChooser.setVisibleItemCount(30);
		controlTypeChooser.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				List<Listener> listeners = DefaultParameterDialogControlTypeHelper.this.listeners.get(SWT.Selection);
				if (listeners == null) {
					return;
				}
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).handleEvent(e);
				}
			}
		});
	}

	@Override
	public Control getControl() {
		return controlTypeChooser;
	}

	protected boolean isStatic() {
		return (Boolean) this.getProperty(ParameterDialog.STATIC_VALUE);
	}

	protected String getDataType() {
		return (String) this.getProperty(ParameterDialog.DATATYPE_VALUE);
	}

	protected String getInputControlType() {
		return (String) this.getProperty(ParameterDialog.CONTROLTYPE_INPUTVALUE);
	}

	@Override
	public void update(boolean inward) {
		if (inward) {
			inwardUpdate();
		} else {
			outwardUpdate();
		}
	}

	protected void outwardUpdate() {
		String displayText = controlTypeChooser.getText();
		if (StringUtil.isBlank(displayText)) {
			return;
		}
		if (ParameterDialog.DISPLAY_NAME_CONTROL_COMBO.equals(displayText)) {
			this.setProperty(ParameterDialog.CONTROLTYPE_VALUE, ParameterDialog.PARAM_CONTROL_COMBO);
			return;
		}
		if (ParameterDialog.DISPLAY_NAME_CONTROL_LIST.equals(displayText)) {
			this.setProperty(ParameterDialog.CONTROLTYPE_VALUE, ParameterDialog.PARAM_CONTROL_LIST);
			return;
		}
		this.setProperty(ParameterDialog.CONTROLTYPE_VALUE,
				CONTROL_TYPE_CHOICE_SET.findChoiceByDisplayName(displayText).getName());
	}

	protected void inwardUpdate() {
		String[] choices = new String[4];

		String originalSelection = controlTypeChooser.getText();
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(getDataType())) {
			choices[0] = CONTROL_TYPE_CHOICE_SET.findChoice(DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX)
					.getDisplayName();
		} else {
			choices[0] = CONTROL_TYPE_CHOICE_SET.findChoice(DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX)
					.getDisplayName();
		}
		choices[1] = ParameterDialog.DISPLAY_NAME_CONTROL_COMBO;
		choices[2] = ParameterDialog.DISPLAY_NAME_CONTROL_LIST;

		choices[3] = CONTROL_TYPE_CHOICE_SET.findChoice(DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON)
				.getDisplayName();

		controlTypeChooser.setItems(choices);
		if (originalSelection.length() == 0) {// initialize
			controlTypeChooser.setText(getInputControlDisplayName());
		} else {
			int index = controlTypeChooser.indexOf(originalSelection);
			if (index == -1) {// The original control type cannot be
								// supported
				controlTypeChooser.select(0);
				controlTypeChooser.notifyListeners(SWT.Selection, new Event());
			}
			controlTypeChooser.setText(originalSelection);
		}

	}

	protected String getInputControlDisplayName() {
		String type = getInputControlType();
		String displayName = null;
		if (CONTROL_TYPE_CHOICE_SET.findChoice(type) != null) {
			displayName = CONTROL_TYPE_CHOICE_SET.findChoice(type).getDisplayName();
		} else if (ParameterDialog.PARAM_CONTROL_COMBO.equals(type)) {
			displayName = ParameterDialog.DISPLAY_NAME_CONTROL_COMBO;
		} else if (ParameterDialog.PARAM_CONTROL_LIST.equals(type)) {
			displayName = ParameterDialog.DISPLAY_NAME_CONTROL_LIST;
		}
		return displayName;
	}
}
