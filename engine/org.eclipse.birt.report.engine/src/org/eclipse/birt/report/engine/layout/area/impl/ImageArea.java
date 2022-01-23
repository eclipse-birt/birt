/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IImageArea;

public class ImageArea extends AbstractArea implements IImageArea {

	ImageArea(IImageContent image) {
		super(image);
	}

	public void accept(IAreaVisitor visitor) {
		visitor.visitImage(this);
	}

}
