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

package org.eclipse.birt.report.designer.ui.cubebuilder.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ITextDescriptorProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;

public class PrimaryDatasetDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	public boolean isEditable() {
		return false;
	}

	public String getDisplayName() {
		return Messages.getString("PrimaryDatasetDescriptorProvider.Display.Primary.Dataset.Name"); //$NON-NLS-1$
	}

	public Object load() {
		if (DEUtil.getInputSize(input) != 1)
			return null;
		DataSetHandle dataset = ((TabularCubeHandle) DEUtil.getInputFirstElement(input)).getDataSet();
		if (dataset != null)
			return dataset.getName();
		else
			return ""; //$NON-NLS-1$
	}

	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

	private Object input;

	public void setInput(Object input) {
		this.input = input;
	}

}
