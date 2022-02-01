/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.HashSet;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.TextArea;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;

public class TextAreaLayout extends ContainerLayout {
	/**
	 * the parent Layout manager. LineLM for block text and InlineContainerLM for
	 * inline text.
	 */
	private InlineStackingLayout parentLM;

	private TextCompositor comp = null;

	private ITextContent textContent = null;

	private static HashSet splitChar = new HashSet();

	static {
		splitChar.add(Character.valueOf(' '));
		splitChar.add(Character.valueOf('\r'));
		splitChar.add(Character.valueOf('\n'));
	};

	public TextAreaLayout(LayoutEngineContext context, ContainerLayout parentContext, IContent content) {
		super(context, parentContext, content);
		parentLM = (InlineStackingLayout) parentContext;

		ITextContent textContent = (ITextContent) content;
		parentLM.setTextIndent(textContent);
		String text = textContent.getText();
		if (text != null && text.length() != 0) {
			transform(textContent);
			this.textContent = textContent;
			comp = new TextCompositor(textContent, context.getFontManager(), context.getBidiProcessing(),
					context.getFontSubstitution(), context.getTextWrapping(), context.isEnableWordbreak(),
					context.getLocale());
			// checks whether the current line is empty or not.
			ContainerLayout ancestor = parentLM;
			do {
				if (null == ancestor) {
					// should never reach here.
					comp.setNewLineStatus(true);
					return;
				}
				if (!ancestor.isRootEmpty()) {
					comp.setNewLineStatus(false);
					return;
				}
				if (ancestor instanceof LineLayout) {
					comp.setNewLineStatus(ancestor.isRootEmpty());
					return;
				}
				ancestor = ancestor.getParent();
			} while (true);
		}
	}

	public boolean addArea(AbstractArea area) {
		return false;
	}

	protected void createRoot() {

	}

	public void layout() throws BirtException {
		while (layoutChildren())
			;
	}

	protected boolean layoutChildren() throws BirtException {
		if (null == textContent)
			return false;
		while (comp.hasNextArea()) {
			TextArea area = comp.getNextArea(getFreeSpace());
			// for a textArea which just has a line break. We should not add TextArea into
			// the line.
			addTextArea(area);
			comp.setNewLineStatus(false);
			if (area.isLineBreak()) {
				if (newLine()) {
					comp.setNewLineStatus(true);
				} else {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean checkAvailableSpace() {
		return false;
	}

	public void addTextArea(AbstractArea textArea) {
		parentLM.addToRoot(textArea);
	}

	/**
	 * true if succeed to new a line.
	 */
	public boolean newLine() throws BirtException {
		return parentLM.endLine();
	}

	public int getFreeSpace() {
		return parentLM.getCurrentMaxContentWidth();
	}

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
			Character c = Character.valueOf(text.charAt(i));
			if (splitChar.contains(c))
				capitalizeNextChar = true;
			else if (capitalizeNextChar) {
				array[i] = Character.toUpperCase(array[i]);
				capitalizeNextChar = false;
			}
		}
		return new String(array);
	}

	protected void closeLayout() {
	}

	protected void initialize() {
	}

	protected void closeLayout(ContainerContext currentContext, int index, boolean finished) {
	}

}
