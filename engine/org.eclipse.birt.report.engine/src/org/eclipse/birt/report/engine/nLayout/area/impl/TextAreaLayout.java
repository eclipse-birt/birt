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
package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;
import org.eclipse.birt.report.engine.nLayout.area.style.AreaConstants;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;

/**
 * Implementation of the text area layout
 *
 * @since 3.3
 *
 */
public class TextAreaLayout implements ILayout {
	protected static Logger logger = Logger.getLogger(TextAreaLayout.class.getName());
	/**
	 * the parent Layout manager. LineLM for block text and InlineContainerLM for
	 * inline text.
	 */
	private InlineStackingArea parentLM;

	private TextCompositor comp = null;

	private ITextContent textContent = null;

	private static HashSet<Character> splitChar = new HashSet<Character>();

	private ArrayList<ITextListener> listenerList = null;

	/**
	 * Denotes if the text content is empty (in this case a single blank space is
	 * used as a replacement text).
	 */
	private boolean blankText = false;

	static {
		splitChar.add(Character.valueOf(' '));
		splitChar.add(Character.valueOf('\r'));
		splitChar.add(Character.valueOf('\n'));
	}

	/**
	 * Constructor
	 *
	 * @param parent  parent
	 * @param context layout context
	 * @param content content
	 */
	public TextAreaLayout(ContainerArea parent, LayoutContext context, IContent content) {
		parentLM = (InlineStackingArea) parent;
		ITextContent textContent = (ITextContent) content;
		parentLM.setTextIndent(textContent);
		String text = textContent.getText();
		if (text != null && text.length() != 0) {
			transform(textContent);
		} else {
			textContent.setText(" ");
			blankText = true;
		}

		this.textContent = textContent;
		comp = new TextCompositor(textContent, context.getFontManager(), context, blankText);
		// checks whether the current line is empty or not.
		boolean isEmptyLine = isEmptyLine();
		comp.setNewLineStatus(isEmptyLine);
	}

	protected boolean isEmptyLine() {
		ContainerArea p = parentLM;
		while (!(p instanceof LineArea)) {
			if (p.getChildrenCount() > 0) {
				return false;
			}
			p = p.getParent();
		}
		return p.getChildrenCount() == 0;
	}

	protected LineArea getLineParent() {
		ContainerArea ancestor = parentLM;
		do {
			if (ancestor instanceof LineArea) {
				return (LineArea) ancestor;
			}
			ancestor = ancestor.getParent();
		} while (true);
	}

	/**
	 * Add listener
	 *
	 * @param listener text listener
	 */
	public void addListener(ITextListener listener) {
		if (listenerList == null) {
			listenerList = new ArrayList<>();
		}
		listenerList.add(listener);
	}

	/**
	 * Built the text style
	 *
	 * @param content  content
	 * @param fontInfo font info
	 * @return Return the created text style
	 */
	public static TextStyle buildTextStyle(IContent content, FontInfo fontInfo) {
		IStyle style = content.getComputedStyle();
		TextStyle textStyle = new TextStyle(fontInfo);
		CSSValue direction = style.getProperty(StyleConstants.STYLE_DIRECTION);
		if (CSSValueConstants.RTL_VALUE.equals(direction)) {
			textStyle.setDirection(AreaConstants.DIRECTION_RTL);
		}
		textStyle.setFontSize(PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_FONT_SIZE)));
		textStyle.setLetterSpacing(
				PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_LETTER_SPACING)));
		textStyle.setWordSpacing(PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_WORD_SPACING)));
		textStyle.setLineThrough(
				style.getProperty(StyleConstants.STYLE_TEXT_LINETHROUGH) == CSSValueConstants.LINE_THROUGH_VALUE);
		textStyle
				.setOverLine(style.getProperty(StyleConstants.STYLE_TEXT_OVERLINE) == CSSValueConstants.OVERLINE_VALUE);
		CSSValue underLine = style.getProperty(StyleConstants.STYLE_TEXT_UNDERLINE);
		if (underLine == CSSValueConstants.UNDERLINE_VALUE) {
			textStyle.setUnderLine(true);
		}
		textStyle.setAlign(style.getProperty(StyleConstants.STYLE_TEXT_ALIGN));
		IStyle s = content.getStyle();
		Color color = PropertyUtil.getColor(s.getProperty(StyleConstants.STYLE_COLOR));
		if (color != null) {
			textStyle.setColor(color);
		} else if (content.getHyperlinkAction() != null) {
			textStyle.setColor(Color.BLUE);
		} else {
			textStyle.setColor(PropertyUtil.getColor(style.getProperty(StyleConstants.STYLE_COLOR)));
		}
		if (content.getHyperlinkAction() != null) {
			textStyle.setHasHyperlink(true);
		}
		return textStyle;
	}

	@Override
	public void layout() throws BirtException {
		layoutChildren();
	}

	protected void layoutChildren() throws BirtException {
		if (null == textContent) {
			return;
		}
		while (comp.hasNextArea()) {
			TextArea area = comp.getNextArea(getFreeSpace());
			// for a textArea which just has a line break. We should not add TextArea into
			// the line.
			if (area != null) {
				addTextArea(area);
				comp.setNewLineStatus(false);
				if (area.isLineBreak()) {
					newLine(area.blankLine);
					comp.setNewLineStatus(true);
				}
			}
		}
	}

	protected boolean checkAvailableSpace() {
		return false;
	}

	/**
	 * Add text area
	 *
	 * @param textArea text area
	 * @throws BirtException
	 */
	public void addTextArea(TextArea textArea) throws BirtException {
		parentLM.add(textArea);
		textArea.setParent(parentLM);
		parentLM.update(textArea);

		if (listenerList != null) {
			for (Iterator<ITextListener> i = listenerList.iterator(); i.hasNext();) {
				ITextListener listener = i.next();
				listener.onAddEvent(textArea);
			}
		}
	}

	/**
	 * Set new line, true if succeed to new a line.
	 *
	 * @param endParagraph end paragraph
	 * @throws BirtException
	 */
	public void newLine(boolean endParagraph) throws BirtException {
		parentLM.endLine(endParagraph);
		if (listenerList != null) {
			for (Iterator<ITextListener> i = listenerList.iterator(); i.hasNext();) {
				ITextListener listener = i.next();
				listener.onNewLineEvent();
			}
		}
	}

	/**
	 * Get the free space
	 *
	 * @return Return the free space
	 */
	public int getFreeSpace() {
		return parentLM.getCurrentMaxContentWidth();
	}

	/**
	 * Transform the text content
	 *
	 * @param textContent text content
	 */
	public void transform(ITextContent textContent) {
		String transformType = textContent.getComputedStyle().getTextTransform();
		if (transformType.equalsIgnoreCase("uppercase")) //$NON-NLS-1$
		{
			textContent.setText(textContent.getText().toUpperCase());
		} else if (transformType.equalsIgnoreCase("lowercase")) //$NON-NLS-1$
		{
			textContent.setText(textContent.getText().toLowerCase());
		} else if (transformType.equalsIgnoreCase("capitalize")) //$NON-NLS-1$
		{
			textContent.setText(capitalize(textContent.getText()));
		}

		ArabicShaping shaping = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
		try {
			String shapingText = shaping.shape(textContent.getText());
			textContent.setText(shapingText);
		} catch (ArabicShapingException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	private String capitalize(String text) {
		boolean capitalizeNextChar = true;
		char[] array = text.toCharArray();
		for (int i = 0; i < array.length; i++) {
			Character c = text.charAt(i);
			if (splitChar.contains(c)) {
				capitalizeNextChar = true;
			} else if (capitalizeNextChar) {
				array[i] = Character.toUpperCase(array[i]);
				capitalizeNextChar = false;
			}
		}
		return new String(array);
	}

	/**
	 * Add listener to the end of the text area
	 */
	public void close() {
		if (listenerList != null) {
			for (Iterator<ITextListener> i = listenerList.iterator(); i.hasNext();) {
				ITextListener listener = i.next();
				listener.onTextEndEvent();
			}
		}
	}

	/**
	 * Initialize method
	 */
	public void initialize() {
	}

}
