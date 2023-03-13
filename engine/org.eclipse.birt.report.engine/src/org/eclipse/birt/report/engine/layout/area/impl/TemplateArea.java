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
package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.ITemplateArea;

public class TemplateArea extends ContainerArea implements ITemplateArea {
	TemplateArea(IAutoTextContent autoText) {
		super(autoText);
	}

	@Override
	public void accept(IAreaVisitor visitor) {
		visitor.visitAutoText(this);
	}

}
