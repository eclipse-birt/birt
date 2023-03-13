/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.chart.examples.view.ChartExamples;
import org.eclipse.birt.chart.examples.view.description.Messages;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

public class SaveXMLAction extends Action {

	private Composite cmp;

	public SaveXMLAction(Tools tool, Composite parent) {
		super();
		this.cmp = parent;
		String id = tool.group + '.' + tool.name;
		setId(id);

		setEnabled(tool.isEnabled());
		setImageDescriptor(UIHelper.getImageDescriptor(ExampleConstants.IMAGE_ENABLE_EXPORT)); // $NON-NLS-1$
		setDisabledImageDescriptor(UIHelper.getImageDescriptor(ExampleConstants.IMAGE_DISABLE_EXPORT));
		setToolTipText(Messages.getDescription("SaveXMLAction.Text.ToolTip")); //$NON-NLS-1$
		setDescription(Messages.getDescription("SaveXMLAction.Text.Description")); //$NON-NLS-1$
	}

	/**
	 * When the action is invoked, pop up a File Dialog to designate the directory.
	 */
	@Override
	public void run() {
		Chart cm = ChartExamples.getChartModel().copyInstance();
		if (cm != null) {
			final FileDialog saveDialog = new FileDialog(cmp.getShell(), SWT.SAVE);
			saveDialog.setFilterExtensions(new String[] { "*.chart" }); //$NON-NLS-1$
			try {
				saveDialog.open();
				String name = saveDialog.getFileName();
				if (name != null && name != "") //$NON-NLS-1$
				{
					Serializer serializer;
					final File file = new File(saveDialog.getFilterPath(), name);
					if (file.exists()) {
						MessageBox box = new MessageBox(cmp.getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
						box.setText(Messages.getDescription("SaveXMLAction.MessageBox.Text")); //$NON-NLS-1$
						box.setMessage(Messages.getDescription("SaveXMLAction.MessageBox.Message")); //$NON-NLS-1$
						if (box.open() != SWT.YES) {
							return;
						}
					}

					serializer = SerializerImpl.instance();
					try (OutputStream os = new FileOutputStream(file)) {
						serializer.write(cm, os);
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
