/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.Stack;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.dom.CompositeStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.wpml.writer.DocWriter;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;

public class DocEmitterImpl extends AbstractEmitterImpl {

	private static final String OUTPUT_FORMAT = "doc";

	private Stack<IStyle> inlineStyles = new Stack<IStyle>();

	private boolean inForeign = false;

	private boolean hasPInside = false;

	public DocEmitterImpl(ContentEmitterVisitor contentVisitor) {
		this.contentVisitor = contentVisitor;
	}

	public void initialize(IEmitterServices service) throws EngineException {
		super.initialize(service);
		wordWriter = new DocWriter(out);
	}

	public String getOutputFormat() {
		return OUTPUT_FORMAT;
	}

	public void endContainer(IContainerContent container) {
		boolean flag = hasForeignParent(container);

		if (flag) {
			if (!CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase(container.getComputedStyle().getDisplay())) {
				adjustInline();
			}
			if (!CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase(container.getComputedStyle().getDisplay())) {
				if (!styles.isEmpty()) {
					styles.pop();
				}
			} else {
				if (!inlineStyles.isEmpty()) {
					inlineStyles.pop();
				}
			}

			if (!CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase(container.getComputedStyle().getDisplay())) {
				if (inForeign && hasPInside) {
					context.addContainer(false);
					hasPInside = false;
				} else if (!inForeign) {
					context.addContainer(true);
				}
				context.setIsAfterTable(true);
			}
		}
	}

	public void startContainer(IContainerContent container) {
		boolean flag = hasForeignParent(container);

		if (flag) {
			if (!CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase(container.getComputedStyle().getDisplay())) {
				adjustInline();
			}

			if (!CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase(container.getComputedStyle().getDisplay())) {
				styles.push(container.getComputedStyle());
			} else {
				inlineStyles.push(container.getComputedStyle());
			}
		}
	}

	private boolean hasForeignParent(IContainerContent container) {
		IContainerContent con = container;
		while (con != null) {
			if (con.getParent() instanceof IForeignContent) {
				return true;
			}
			con = (IContainerContent) con.getParent();
		}
		return false;
	}

	public void endTable(ITableContent table) {
		hasPInside = false;
		endTable();
		decreaseTOCLevel(table);
	}

	public void startForeign(IForeignContent foreign) throws BirtException {
		if (IForeignContent.HTML_TYPE.equalsIgnoreCase(foreign.getRawType())) {
			inForeign = true;
			// store the inline state before the HTML foreign.
			boolean inlineBrother = !context.isFirstInline();
			// the inline state needs be recalculated in the HTML foreign.

			// if the foreign itself is not inline
			if (!"inline".equalsIgnoreCase(foreign.getComputedStyle() //$NON-NLS-1$
					.getDisplay())) {
				// stop the inline mode completely
				adjustInline();
				inlineBrother = false;
			} else {
				// only store the state to restore it later
				context.endInline();
			}

			HTML2Content.html2Content(foreign);

			context.startCell();

			if (context.isAfterTable()) {
				wordWriter.insertHiddenParagraph();
				context.setIsAfterTable(false);
			}
			int width = WordUtil.convertTo(foreign.getWidth(), context.getCurrentWidth(), reportDpi);
			width = Math.min(width, context.getCurrentWidth());
			wordWriter.startTable(foreign.getComputedStyle(), width, inForeign);
			wordWriter.startTableRow(-1);
			wordWriter.startTableCell(width, foreign.getComputedStyle(), null);
			writeBookmark(foreign);
			writeToc(foreign);
			contentVisitor.visitChildren(foreign, null);

			adjustInline();

			wordWriter.endTableCell(context.needEmptyP());

			context.endCell();
			wordWriter.endTableRow();
			wordWriter.endTable();
			context.setIsAfterTable(true);
			context.addContainer(true);
			hasPInside = false;
			// restore the inline state after the HTML foreign.
			if (inlineBrother) {
				context.startInline();
			}
			inForeign = false;
		} else {
			Object rawValue = foreign.getRawValue();
			String text = rawValue == null ? "" : rawValue.toString();
			writeContent(DocEmitterImpl.NORMAL, text, foreign);
		}
	}

	protected void writeContent(int type, String txt, IContent content) {
		if (inForeign) {
			hasPInside = true;
		}
		context.addContainer(false);

		InlineFlag inlineFlag = InlineFlag.BLOCK;
		IStyle computedStyle = content.getComputedStyle();
		IStyle inlineStyle = null;

		String textAlign = null;
		if ("inline".equalsIgnoreCase(content.getComputedStyle().getDisplay())) {
			if (context.isFirstInline()) {
				context.startInline();
				inlineFlag = InlineFlag.FIRST_INLINE;
				if (!styles.isEmpty()) {
					computedStyle = new CompositeStyle(styles.peek(), content.getStyle());
				}
			} else
				inlineFlag = InlineFlag.MIDDLE_INLINE;
			if (!inlineStyles.isEmpty()) {
				inlineStyle = mergeStyles(inlineStyles);
			}
			IContent parent = (IContent) content.getParent();
			if (parent != null && parent.getComputedStyle() != null)
				textAlign = parent.getComputedStyle().getTextAlign();
		} else {
			adjustInline();
		}

		writeBookmark(content);
		writeToc(content, inlineFlag == InlineFlag.MIDDLE_INLINE); // element with Toc contains bookmark
		writeText(type, txt, content, inlineFlag, computedStyle, inlineStyle, textAlign);
		context.setIsAfterTable(false);
	}

	private IStyle mergeStyles(Stack<IStyle> inlineStyles) {
		IStyle style = inlineStyles.peek();

		for (int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++) {
			if (isNullValue(style.getProperty(i))) {
				style.setProperty(i, null);

				for (int p = inlineStyles.size() - 1; p >= 0; p--) {
					IStyle pstyle = (IStyle) inlineStyles.get(p);

					if (!isNullValue(pstyle.getProperty(i))) {
						style.setProperty(i, pstyle.getProperty(i));
						break;
					}
				}
			}
		}
		return style;
	}
}
