/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.PaletteEntryExtension;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.window.Window;

/**
 * Provides creation function for extended element
 *
 */
public class ExtendedElementToolExtends extends AbstractToolHandleExtends {

	private String extensionName;

	/**
	 * @param builder
	 */
	public ExtendedElementToolExtends(String extensionName) {
		super();
		this.extensionName = extensionName;
	}

	@Override
	public boolean postHandleCreation() {
		// TODO check extension setting here to decide if popup the builder
		IReportItemBuilderUI builder = getbuilder();
		if (builder != null) {
			// Open the builder for new element
			if (builder.open((ExtendedItemHandle) getModel()) == Window.CANCEL) {
				return false;
			}
		} else {
			PaletteEntryExtension[] extensions = EditpartExtensionManager.getPaletteEntries();
			for (int i = 0; i < extensions.length; i++) {
				if (extensions[i].getLabel().equals(this.extensionName)) {
					try {
						CommandUtils.setVariable("targetEditPart", //$NON-NLS-1$
								getTargetEditPart());
						setModel(extensions[i].executeCreate());
						return super.preHandleMouseUp();
					} catch (Exception e) {
						ExceptionHandler.handle(e);
					}

					return false;
				}
			}
		}

		return super.postHandleCreation();
	}

	@Override
	public boolean preHandleMouseUp() {
		ExtendedItemHandle handle = DesignElementFactory.getInstance().newExtendedItem(null, extensionName);
		if (handle == null) {
			return false;
		}
		setModel(handle);
		return super.preHandleMouseUp();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.tools.
	 * AbstractToolHandleExtends#preHandleMouseDown()
	 */

	@Override
	public boolean preHandleMouseDown() {
		return false;
	}

	/**
	 * Gets the builder
	 *
	 * @return
	 */
	private IReportItemBuilderUI getbuilder() {
		ExtendedElementUIPoint point = ExtensionPointManager.getInstance().getExtendedElementPoint(extensionName);
		if (point != null) {
			return point.getReportItemBuilderUI();
		}
		return null;
	}
}
