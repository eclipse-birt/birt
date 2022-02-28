/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

public interface PreviewPropertyDescriptorProvider extends IDescriptorProvider {

	DesignElementHandle getDesignElementHandle();

	@Override
	void setInput(Object input);

	LabelProvider getLabelProvider();

	IStructuredContentProvider getContentProvider(IModelEventProcessor processor);

	String getColumnText(Object element, int columnIndex);

	String getText(int index);

	boolean moveDown(int index);

	boolean edit(Object data, int itemCount);

	boolean moveUp(int index);

	boolean delete(int idx);

	boolean add(int itemCount);

	boolean duplicate(int idx);

}
