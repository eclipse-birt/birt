/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.helper;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

/**
 * This interface proivdes functions to help dialog creation
 */
public interface IDialogHelper {

	void setContainer(Object container);

	void createContent(Composite parent);

	void update(boolean inward);

	void validate();

	String[] getErrors();

	void setProperty(String key, Object value);

	Object getProperty(String key);

	void addListener(int eventType, Listener listener);

	void removeListener(int eventType, Listener listener);

	Control getControl();
}
