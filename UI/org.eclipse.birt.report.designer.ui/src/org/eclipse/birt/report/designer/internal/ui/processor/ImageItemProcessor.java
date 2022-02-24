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

package org.eclipse.birt.report.designer.internal.ui.processor;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ImageBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.dialogs.Dialog;

/**
 * The processor for image items
 */

public class ImageItemProcessor extends AbstractElementProcessor {

	/**
	 * Constructor
	 * 
	 * Creates a new instance of the processor for image items
	 */
	ImageItemProcessor() {
		super(ReportDesignConstants.IMAGE_ITEM);
	}

	public DesignElementHandle createElement(Object extendedData) {
//		ImageBuilder dialog = new ImageBuilder( UIUtil.getDefaultShell( ),
//				ImageBuilder.DLG_TITLE_NEW );
//		if ( dialog.open( ) == Dialog.OK )
//		{
//			return (DesignElementHandle) dialog.getResult( );
//		}
//		return null;

		return DesignElementFactory.getInstance().newImage(null);
	}

	public boolean editElement(DesignElementHandle handle) {
		ImageBuilder dialog = new ImageBuilder(UIUtil.getDefaultShell(), ImageBuilder.DLG_TITLE_EDIT);
		dialog.setInput(handle);
		return (dialog.open() == Dialog.OK);
	}
}
