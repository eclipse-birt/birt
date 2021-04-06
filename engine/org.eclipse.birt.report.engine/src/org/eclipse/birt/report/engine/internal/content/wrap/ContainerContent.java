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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;

public class ContainerContent extends AbstractContentWrapper implements IContainerContent {
	public ContainerContent(IContainerContent container) {
		super(container);
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitContainer(this, value);
	}
}
