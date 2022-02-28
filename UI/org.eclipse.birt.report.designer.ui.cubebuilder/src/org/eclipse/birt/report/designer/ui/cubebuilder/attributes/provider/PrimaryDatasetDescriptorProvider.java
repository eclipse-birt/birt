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

package org.eclipse.birt.report.designer.ui.cubebuilder.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ITextDescriptorProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;

public class PrimaryDatasetDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public String getDisplayName() {
		return Messages.getString("PrimaryDatasetDescriptorProvider.Display.Primary.Dataset.Name"); //$NON-NLS-1$
	}

	@Override
	public Object load() {
		if (DEUtil.getInputSize(input) != 1) {
			return null;
		}
		DataSetHandle dataset = ((TabularCubeHandle) DEUtil.getInputFirstElement(input)).getDataSet();
		if (dataset != null) {
			return dataset.getName();
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	@Override
	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

	private Object input;

	@Override
	public void setInput(Object input) {
		this.input = input;
	}

}
