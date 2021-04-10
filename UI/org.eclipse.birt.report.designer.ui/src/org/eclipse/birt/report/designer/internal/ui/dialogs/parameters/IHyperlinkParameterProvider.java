/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.parameters;

/**
 * IHyperlinkParameterProvider
 */
public interface IHyperlinkParameterProvider {

	String CATEGORY_DEFAULT = "default"; //$NON-NLS-1$

	String[] getCategories();

	IHyperlinkParameter[] getParameters(String category, String format);
}
