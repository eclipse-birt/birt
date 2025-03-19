/*******************************************************************************
 * Copyright (c) 2009, 2025 Actuate Corporation and others
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

package org.eclipse.birt.report.engine.emitter.ppt.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;

/**
 * Utility class of power point
 *
 * @since 3.3
 *
 */
public class PPTUtil {

	private static final Logger logger = Logger.getLogger(PPTUtil.class.getName());

	/**
	 * Get the hyperlink information
	 *
	 * @param area           object which contains the link
	 * @param services       emitter service
	 * @param reportRunnable report runnable
	 * @param context        report context
	 * @return the hyperlink information
	 */
	public static HyperlinkDef getHyperlink(IArea area, IEmitterServices services, IReportRunnable reportRunnable,
			IReportContext context) {
		return getHyperlink(area, services, reportRunnable, context, false);
	}

	/**
	 * Get the hyperlink information
	 *
	 * @param area                object which contains the link
	 * @param services            emitter service
	 * @param reportRunnable      report runnable
	 * @param context             report context
	 * @param linkImageToBookmark image line is a bookmark
	 * @return the hyperlink information
	 */
	public static HyperlinkDef getHyperlink(IArea area, IEmitterServices services, IReportRunnable reportRunnable,
			IReportContext context, boolean linkImageToBookmark) {
		IHyperlinkAction hyperlinkAction = area.getAction();
		if (hyperlinkAction != null) {
			try {
				// hyperlink definition not for type bookmark,
				// exceptional bookmark links of images
				if (hyperlinkAction.getType() != IHyperlinkAction.ACTION_BOOKMARK || linkImageToBookmark) {
					String link = hyperlinkAction.getHyperlink();
					String tooltip = hyperlinkAction.getTooltip();
					Object handler = services.getOption(IRenderOption.ACTION_HANDLER);
					if (handler instanceof IHTMLActionHandler) {
						if (linkImageToBookmark && hyperlinkAction.getBookmark() != null)
							link = hyperlinkAction.getBookmark();
						else {
							IHTMLActionHandler actionHandler = (IHTMLActionHandler) handler;
							String systemId = reportRunnable == null ? null : reportRunnable.getReportName();
							Action action = new Action(systemId, hyperlinkAction);
							link = actionHandler.getURL(action, context);
						}
					}
					// hyperlink decoration option
					IStyle computedStyle = null;
					boolean hasHyperlinkDecoration = true;
					if (area instanceof ContainerArea) {
						computedStyle = ((ContainerArea) area).getContent().getComputedStyle();
						if (computedStyle != null) {
							hasHyperlinkDecoration = !(computedStyle.getProperty(
									StyleConstants.STYLE_TEXT_HYPERLINK_STYLE) == CSSValueConstants.UNDECORATED);
						}
					}
					return new HyperlinkDef(link, tooltip, hasHyperlinkDecoration, hyperlinkAction.getType());
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * Information call of the hyperlink information
	 *
	 * @since 3.3
	 */
	public static class HyperlinkDef {

		private String link;
		private String tooltip;
		private boolean hasHyperlinkDecoration = true;
		private int hyperlinkActionType = -1;

		/**
		 * Constructor
		 *
		 * @param link                   link URL
		 * @param tooltip                link tooltip text
		 * @param hasHyperlinkDecoration hyperlink use text decoration
		 */
		public HyperlinkDef(String link, String tooltip, boolean hasHyperlinkDecoration) {
			this(link, tooltip, hasHyperlinkDecoration, -1);
		}

		/**
		 * Constructor
		 *
		 * @param link                   link URL
		 * @param tooltip                link tooltip text
		 * @param hasHyperlinkDecoration hyperlink use text decoration
		 * @param hyperlinkActionType    action type for the hyperlink
		 */
		public HyperlinkDef(String link, String tooltip, boolean hasHyperlinkDecoration, int hyperlinkActionType) {
			this.link = link;
			this.tooltip = tooltip;
			this.hasHyperlinkDecoration = hasHyperlinkDecoration;
			this.hyperlinkActionType = hyperlinkActionType;
		}

		/**
		 * Get the hyperlink URL
		 *
		 * @return the hyperlink URL
		 */
		public String getLink() {
			return link;
		}

		/**
		 * Get the hyperlink tooltip text
		 *
		 * @return the hyperlink tooltip text
		 */
		public String getTooltip() {
			return tooltip;
		}

		/**
		 * Is the hyperlink decoration in use
		 *
		 * @return is the hyperlink decoration in use
		 */
		public boolean isHasHyperlinkDecoration() {
			return hasHyperlinkDecoration;
		}

		/**
		 * Get the action type of the hyperlink
		 *
		 * @return the action type of the hyperlink
		 */
		public int getHyperlinkActionType() {
			return hyperlinkActionType;
		}
	}
}
