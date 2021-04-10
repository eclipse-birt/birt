/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.odp.util;

import java.awt.Color;
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
import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;

import com.lowagie.text.pdf.BaseFont;

public class OdpUtil {

	private static final Logger logger = Logger.getLogger(OdpUtil.class.getName());

	public static HyperlinkInfo getHyperlink(IArea area, IEmitterServices services, IReportRunnable reportRunnable,
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
					return new HyperlinkInfo(HyperlinkInfo.BOOKMARK, link, tooltip);
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
		return null;
	}

	public static void appendComponent(StringBuffer buffer, int component) {
		String hex = Integer.toHexString(component);
		if (hex.length() == 1) {
			buffer.append('0');
		}
		buffer.append(hex);
	}

	public static String getColorString(Color color) {
		StringBuffer buffer = new StringBuffer("#"); //$NON-NLS-1$
		appendComponent(buffer, color.getRed());
		appendComponent(buffer, color.getGreen());
		appendComponent(buffer, color.getBlue());
		return buffer.toString();
	}

	public static String getFontName(BaseFont baseFont) {
		String[][] familyFontNames = baseFont.getFamilyFontName();
		String[] family = familyFontNames[familyFontNames.length - 1];
		return family[family.length - 1];
	}
}
