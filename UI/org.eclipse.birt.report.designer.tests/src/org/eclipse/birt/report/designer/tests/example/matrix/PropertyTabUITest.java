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

package org.eclipse.birt.report.designer.tests.example.matrix;

import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.birt.report.designer.ui.extensions.IPropertyListener;
import org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI;
import org.eclipse.birt.report.designer.ui.extensions.IPropertyValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 */
public class PropertyTabUITest implements IPropertyTabUI {
	private Text text;
	private HashSet listeners = new HashSet();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#
	 * addPropertyListener(org.eclipse.birt.report.designer.ui.extensions.
	 * IPropertyListener)
	 */
	public void addPropertyListener(IPropertyListener listener) {
		listeners.add(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#
	 * removePropertyListener(org.eclipse.birt.report.designer.ui.extensions.
	 * IPropertyListener)
	 */
	public void removePropertyListener(IPropertyListener listener) {
		listeners.remove(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#buildUI(org.
	 * eclipse.swt.widgets.Composite)
	 */
	public void buildUI(Composite composite) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		Label label = new Label(composite, SWT.NONE);
		label.setText("Test1:");
		text = new Text(composite, SWT.NONE);
		text.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				for (Iterator iter = listeners.iterator(); iter.hasNext();) {
					((IPropertyListener) iter.next()).propertyChanged("test1", ((Text) e.getSource()).getText());
				}

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#
	 * getTabDisplayName()
	 */
	public String getTabDisplayName() {
		return "TabTest";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#
	 * setPropertyValue(java.lang.String,
	 * org.eclipse.birt.report.designer.ui.extensions.IPropertyValue)
	 */
	public void setPropertyValue(String propertyName, IPropertyValue value) {
		if (propertyName.equals("test1")) {
			text.setText(value.getStringValue());
		}

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Control getControl() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setInput(Object elements) {
		// TODO Auto-generated method stub

	}

}
