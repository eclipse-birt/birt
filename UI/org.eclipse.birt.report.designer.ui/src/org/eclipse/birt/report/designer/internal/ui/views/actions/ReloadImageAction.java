/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.io.IOException;

import org.eclipse.birt.report.designer.internal.ui.views.ReportResourceChangeEvent;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;

/**
 * 
 */

public class ReloadImageAction extends AbstractViewAction {

	public ReloadImageAction(Object element) {
		super(element, Messages.getString("ReloadImageAction.Text")); //$NON-NLS-1$
	}

	public void run() {
		IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

		if (synchronizer != null) {
			ImageHandle image = ((ImageHandle) this.getSelection());
			ExpressionHandle uri = (ExpressionHandle) image.getExpressionProperty(IImageItemModel.URI_PROP);
			if (uri != null) {
				String imageUri = (String) uri.getExpression();
				if (ExpressionType.JAVASCRIPT.equals(uri.getType())) {
					if (imageUri != null) {
						imageUri = DEUtil.removeQuote(imageUri);
					}
				}
				if (DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equals(image.getSource())) {
					try {
						ImageManager.getInstance().rloadImage(image.getModuleHandle(), imageUri);
					} catch (IOException e) {

					}
				} else if (DesignChoiceConstants.IMAGE_REF_TYPE_URL.equals(image.getSource())) {
					ImageManager.getInstance().reloadURIImage(image.getModuleHandle(), imageUri);
				}
				synchronizer.notifyResourceChanged(
						new ReportResourceChangeEvent(this, imageUri, IReportResourceChangeEvent.ImageResourceChange));
			}

		}
	}
}
