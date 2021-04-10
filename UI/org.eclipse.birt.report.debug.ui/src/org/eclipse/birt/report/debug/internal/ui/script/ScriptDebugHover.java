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

package org.eclipse.birt.report.debug.internal.ui.script;

import org.eclipse.birt.report.debug.internal.script.model.ScriptDebugTarget;
import org.eclipse.birt.report.debug.internal.script.model.ScriptModelPresentation;
import org.eclipse.birt.report.debug.internal.script.model.ScriptStackFrame;
import org.eclipse.birt.report.debug.internal.script.model.ScriptValue;
import org.eclipse.birt.report.debug.internal.ui.script.util.ScriptDebugUtil;
import org.eclipse.birt.report.designer.internal.ui.script.JSPartitionScanner;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;

/**
 * Gets the script debug hover string.
 */
public class ScriptDebugHover implements ITextHoverExtension, ITextHover {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.ITextHoverExtension#getHoverControlCreator()
	 */
	public IInformationControlCreator getHoverControlCreator() {
		return new IInformationControlCreator() {

			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, EditorsUI.getTooltipAffordanceString());
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.
	 * ITextViewer, org.eclipse.jface.text.IRegion)
	 */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		ScriptStackFrame frame = getFrame();
		if (frame == null) {
			return null;
		}
		IDocument document = textViewer.getDocument();
		if (document == null) {
			return null;
		}
		try {
			String str = TextUtilities.getContentType(document, IDocumentExtension3.DEFAULT_PARTITIONING,
					hoverRegion.getOffset() + 1, true);

			String variableName = document.get(hoverRegion.getOffset(), hoverRegion.getLength());

			if (JSPartitionScanner.JS_KEYWORD.equals(str) && !"this".equals(variableName)) //$NON-NLS-1$
			{
				return null;
			}
			ScriptValue var = ((ScriptDebugTarget) frame.getDebugTarget()).evaluate(frame, variableName);
			if (var != null) {
				return getVariableText(var);
			}
		} catch (BadLocationException e) {
			return null;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.ITextHover#getHoverRegion(org.eclipse.jface.text.
	 * ITextViewer, int)
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return ScriptDebugUtil.findWord(textViewer.getDocument(), offset);
	}

	private static String replaceHTMLChars(String variableText) {
		StringBuffer buffer = new StringBuffer(variableText.length());
		char[] characters = variableText.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			char character = characters[i];
			switch (character) {
			case '<':
				buffer.append("&lt;"); //$NON-NLS-1$
				break;
			case '>':
				buffer.append("&gt;"); //$NON-NLS-1$
				break;
			case '&':
				buffer.append("&amp;"); //$NON-NLS-1$
				break;
			case '"':
				buffer.append("&quot;"); //$NON-NLS-1$
				break;
			default:
				buffer.append(character);
			}
		}
		return buffer.toString();
	}

	private static String getVariableText(ScriptValue variable) {
		StringBuffer buffer = new StringBuffer();
		ScriptModelPresentation modelPresentation = new ScriptModelPresentation();
		buffer.append("<p><pre>"); //$NON-NLS-1$
		String variableText = modelPresentation.getVariableText(variable);
		buffer.append(replaceHTMLChars(variableText));
		buffer.append("</pre></p>"); //$NON-NLS-1$
		modelPresentation.dispose();
		if (buffer.length() > 0) {
			return buffer.toString();
		}
		return null;
	}

	private ScriptStackFrame getFrame() {
		IAdaptable adaptable = DebugUITools.getDebugContext();
		if (adaptable != null) {
			return (ScriptStackFrame) adaptable.getAdapter(ScriptStackFrame.class);
		}
		return null;
	}
}
