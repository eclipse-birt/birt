/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.nLayout.area;

public interface IAreaVisitor {

	void visitText(ITextArea textArea);

	void visitAutoText(ITemplateArea templateArea);

	void visitImage(IImageArea imageArea);

	void visitContainer(IContainerArea containerArea);
}
