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

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.editors.script.ScriptParser;
import org.eclipse.birt.report.designer.internal.ui.script.ScriptValidator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Display;

/**
 * Reconciling strategy for script editor. This is a composite strategy
 * containing the validating reconciler and the folding strategy.
 */
public class ScriptReconcilingStrategy implements IReconcilingStrategy {

	/** The source viewer. */
	private final ISourceViewer viewer;

	/** The script validator. */
	private final ScriptValidator validator;

	/**
	 * Constructs reconciler for script editor with the specified source viewer.
	 *
	 * @param sourceViewer the specified source viewer.
	 */
	public ScriptReconcilingStrategy(ISourceViewer sourceViewer) {
		viewer = sourceViewer;
		validator = new ScriptValidator(sourceViewer);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.
	 * jface.text.IRegion)
	 */
	@Override
	public void reconcile(IRegion partition) {
		Display.getDefault().asyncExec(new Runnable() {

			/*
			 * (non-Javadoc)
			 *
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				validate();
				updateFoldingStructure();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.
	 * jface.text.reconciler.DirtyRegion, org.eclipse.jface.text.IRegion)
	 */
	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(subRegion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#setDocument(org.
	 * eclipse.jface.text.IDocument)
	 */
	@Override
	public void setDocument(IDocument document) {
	}

	/**
	 * Validates current script.
	 */
	protected void validate() {
		try {
			validator.validate(true, false);
		} catch (ParseException e) {
			return;
		}
	}

	/**
	 * Updates folding structure.
	 */
	protected void updateFoldingStructure() {
		if (!(viewer instanceof ProjectionViewer)) {
			return;
		}

		ProjectionAnnotationModel annotationModel = ((ProjectionViewer) viewer).getProjectionAnnotationModel();

		if (annotationModel == null) {
			return;
		}

		Collection positions = new HashSet();
		IDocument document = viewer.getDocument();
		ScriptParser parser = new ScriptParser(document == null ? null : document.get());

		Collection comments = parser.getCommentPositions();
		Collection methods = parser.getMethodPositions();

		positions.addAll(comments);
		positions.addAll(methods);

		for (Iterator iterator = annotationModel.getAnnotationIterator(); iterator.hasNext();) {
			Annotation annotation = (Annotation) iterator.next();

			if (annotation instanceof ScriptProjectionAnnotation
					&& !positions.remove(annotationModel.getPosition(annotation))) {
				annotationModel.removeAnnotation(annotation);
			}
		}

		for (Iterator iterator = positions.iterator(); iterator.hasNext();) {
			Position position = (Position) iterator.next();
			ProjectionAnnotation annotation = new ScriptProjectionAnnotation(
					comments.contains(position) ? ScriptProjectionAnnotation.SCRIPT_COMMENT
							: methods.contains(position) ? ScriptProjectionAnnotation.SCRIPT_METHOD : 0);

			annotationModel.addAnnotation(annotation, position);
		}
	}
}
