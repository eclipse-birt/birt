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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MapDescriptorProvider;
import org.eclipse.birt.report.designer.util.ColorManager;

public class MapPropertyDescriptor extends PreviewPropertyDescriptor {

	public MapPropertyDescriptor(boolean formStyle) {
		super(formStyle);
	}

	protected MapDescriptorProvider mapProvider;

	@Override
	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);
		if (provider instanceof MapDescriptorProvider) {
			this.mapProvider = (MapDescriptorProvider) provider;
		}
	}

	@Override
	protected void updatePreview(Object handle) {

		if (handle != null && mapProvider != null) {
			previewLabel.setText(mapProvider.getDisplayText(handle));
			previewLabel.updateView();
		} else {
			previewLabel.restoreDefaultState();

			previewLabel.setForeground(ColorManager.getColor(-1));
			previewLabel.setBackground(ColorManager.getColor(-1));

			previewLabel.setText(""); //$NON-NLS-1$
			previewLabel.updateView();

			if (isFormStyle()) {
				FormWidgetFactory.getInstance().paintFormStyle(previewLabel);
				FormWidgetFactory.getInstance().adapt(previewLabel);
			}
		}
	}

}
