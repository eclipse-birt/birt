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

package org.eclipse.birt.report.debug.internal.ui.script.editor;

import org.eclipse.birt.report.debug.internal.ui.script.ScriptDebugHover;
import org.eclipse.birt.report.designer.internal.ui.script.JSPartitionScanner;
import org.eclipse.birt.report.designer.internal.ui.script.JSSyntaxContext;
import org.eclipse.birt.report.designer.ui.editor.script.ScriptSourceViewerConfiguration;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * DebugScriptSourceViewerConfiguration
 */
public class DebugScriptSourceViewerConfiguration extends ScriptSourceViewerConfiguration {

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public DebugScriptSourceViewerConfiguration(JSSyntaxContext context) {
		super(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.source.SourceViewerConfiguration#getTextHover(org.
	 * eclipse.jface.text.source.ISourceViewer, java.lang.String, int)
	 */
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType, int stateMask) {

		if (!(JSPartitionScanner.JS_COMMENT.equals(contentType)
				// || JSPartitionScanner.JS_KEYWORD.equals( contentType )
				|| JSPartitionScanner.JS_STRING.equals(contentType))) {
			return new ScriptDebugHover();
		}
		return super.getTextHover(sourceViewer, contentType, stateMask);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#
	 * getConfiguredTextHoverStateMasks(org.eclipse.jface.text.source.ISourceViewer,
	 * java.lang.String)
	 */
	public int[] getConfiguredTextHoverStateMasks(ISourceViewer sourceViewer, String contentType) {
		return new int[] { 0 };
	}
}
