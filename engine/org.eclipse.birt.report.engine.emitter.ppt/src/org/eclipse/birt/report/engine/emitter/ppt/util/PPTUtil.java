/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.nLayout.area.IArea;

public class PPTUtil {

	private static final Logger logger = Logger.getLogger(PPTUtil.class.getName());

	public static HyperlinkDef getHyperlink(IArea area, IEmitterServices services, IReportRunnable reportRunnable,
			IReportContext context) {
		IHyperlinkAction hyperlinkAction = area.getAction();
		if (hyperlinkAction != null) {
			try {
				if (hyperlinkAction.getType() != IHyperlinkAction.ACTION_BOOKMARK) {
					String link = hyperlinkAction.getHyperlink();
					String tooltip = hyperlinkAction.getTooltip();
					Object handler = services.getOption(RenderOption.ACTION_HANDLER);
					if (handler != null && handler instanceof IHTMLActionHandler) {
						IHTMLActionHandler actionHandler = (IHTMLActionHandler) handler;
						String systemId = reportRunnable == null ? null : reportRunnable.getReportName();
						Action action = new Action(systemId, hyperlinkAction);
						link = actionHandler.getURL(action, context);
					}
					return new HyperlinkDef(link, tooltip);
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
		return null;
	}

	public static class HyperlinkDef {

		String link;
		String tooltip;

		public HyperlinkDef(String link, String tooltip) {
			this.link = link;
			this.tooltip = tooltip;
		}

		public String getLink() {
			return link;
		}

		public String getTooltip() {
			return tooltip;
		}

	}
}
