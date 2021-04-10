/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;

public class InlineTextLayout extends InlineStackingLayout {
	public InlineTextLayout(LayoutEngineContext context, ContainerLayout parentContext, IContent content) {
		super(context, parentContext, content);
	}

	public void layout() throws BirtException {
		ContainerLayout inlineTextContainer = null;
		if (parent instanceof LineLayout) {
			inlineTextContainer = new InlineContainerLayout(context, parent, content);
			inlineTextContainer.initialize();
			TextAreaLayout inlineText = new TextAreaLayout(context, inlineTextContainer, content);
			inlineText.initialize();
			inlineText.layout();
			inlineText.closeLayout();
			inlineTextContainer.closeLayout();
		} else {
			TextAreaLayout inlineText = new TextAreaLayout(context, parent, content);
			inlineText.initialize();
			inlineText.layout();
			inlineText.closeLayout();
		}
	}
}
