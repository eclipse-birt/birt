/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editor.script;

import org.eclipse.birt.report.designer.internal.ui.script.JSSourceViewerConfiguration;
import org.eclipse.birt.report.designer.internal.ui.script.JSSyntaxContext;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * Subclass of <code>JSSourceViewerConfiguration</code>, provides two hovers.
 */
public class ScriptSourceViewerConfiguration extends JSSourceViewerConfiguration {

	/**
	 * Constracts a configuration for source viewer.
	 * 
	 * @param context the context for javascript syntax.
	 */
	public ScriptSourceViewerConfiguration(JSSyntaxContext context) {
		super(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#
	 * getOverviewRulerAnnotationHover(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IAnnotationHover getOverviewRulerAnnotationHover(ISourceViewer sourceViewer) {
		IAnnotationHover hover = super.getOverviewRulerAnnotationHover(sourceViewer);

		if (hover == null) {
			hover = new DefaultAnnotationHover();
		}
		return hover;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.source.SourceViewerConfiguration#getAnnotationHover(
	 * org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		IAnnotationHover hover = super.getAnnotationHover(sourceViewer);

		if (hover == null) {
			hover = new DefaultAnnotationHover();
		}
		return hover;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.source.SourceViewerConfiguration#getReconciler(org.
	 * eclipse.jface.text.source.ISourceViewer)
	 */
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		// Creates an instance of MonoReconciler with the specified strategy,
		// and is not incremental.
		return new MonoReconciler(new ScriptReconcilingStrategy(sourceViewer), false);
	}
}
