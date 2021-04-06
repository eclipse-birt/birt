/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.script;

import java.text.ParseException;
import java.util.Iterator;

import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.custom.StyledText;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Parser;

/**
 * The validator for script viewer.
 */
public class ScriptValidator {

	/** the script viewer to validate */
	private final ISourceViewer scriptViewer;

	/**
	 * Constructs an validator with the specified script viewer.
	 * 
	 * @param viewer the script viewer to validate
	 */
	public ScriptValidator(ISourceViewer viewer) {
		scriptViewer = viewer;
		init();
	}

	/**
	 * Initializes the validator.
	 */
	public void init() {
		clearAnnotations();
	}

	/**
	 * Validates the current script, and selects the error if the specified falg is
	 * <code>true</code>.
	 * 
	 * @param isFunctionBody  <code>true</code> if a function body is validated,
	 *                        <code>false</code> otherwise.
	 * @param isErrorSelected <code>true</code> if error will be selected after
	 *                        validating, <code>false</code> otherwise.
	 * @throws ParseException if an syntax error is found.
	 */
	public void validate(boolean isFunctionBody, boolean isErrorSelected) throws ParseException {
		if (scriptViewer == null) {
			return;
		}

		clearAnnotations();

		StyledText textField = scriptViewer.getTextWidget();

		if (textField == null || !textField.isEnabled()) {
			return;
		}

		String functionTag = "function(){"; //$NON-NLS-1$
		IDocument document = scriptViewer.getDocument();
		String text = document == null ? null : scriptViewer.getDocument().get();

		String script = text;

		if (isFunctionBody) {
			script = functionTag + script + "\n}"; //$NON-NLS-1$
		}

		try {
			validateScript(script);
		} catch (ParseException e) {
			int offset = e.getErrorOffset();

			if (isFunctionBody) {
				offset -= functionTag.length();
				while (offset >= text.length()) {
					offset--;
				}
			}

			String errorMessage = e.getLocalizedMessage();
			Position position = getErrorPosition(text, offset);

			if (position != null) {
				IAnnotationModel annotationModel = scriptViewer.getAnnotationModel();

				if (annotationModel != null) {
					annotationModel.addAnnotation(
							new Annotation(IReportGraphicConstants.ANNOTATION_ERROR, true, errorMessage), position);
				}
				if (isErrorSelected) {
					if (scriptViewer instanceof SourceViewer) {
						((SourceViewer) scriptViewer)
								.setSelection(new TextSelection(position.getOffset(), position.getLength()));
					}
					scriptViewer.revealRange(position.getOffset(), position.getLength());
				}
			}
			throw new ParseException(e.getLocalizedMessage(), position.offset);
		}
	}

	/**
	 * Clears all annotations.
	 */
	private void clearAnnotations() {
		IAnnotationModel annotationModel = scriptViewer.getAnnotationModel();

		if (annotationModel != null) {
			for (Iterator iterator = annotationModel.getAnnotationIterator(); iterator.hasNext();) {
				Annotation annotation = (Annotation) iterator.next();

				if (annotation != null && IReportGraphicConstants.ANNOTATION_ERROR.equals(annotation.getType())) {
					annotationModel.removeAnnotation(annotation);
				}
			}
		}
	}

	/**
	 * Validates the specified script.
	 * 
	 * @param script the script to validate.
	 * @throws ParseException if an syntax error is found.
	 */
	protected void validateScript(String script) throws ParseException {
		if (script == null) {
			return;
		}

		CompilerEnvirons compilerEnv = new CompilerEnvirons();
		Parser jsParser = new Parser(compilerEnv, compilerEnv.getErrorReporter());

		try {
			jsParser.parse(script, null, 0);
		} catch (EvaluatorException e) {
			int offset = -1;

			if (scriptViewer != null) {
				String[] lines = script.split("\n"); //$NON-NLS-1$

				for (int i = 0; i < e.lineNumber(); i++) {
					offset += lines[i].length() + 1;
				}
				offset += e.columnNumber();
			}
			throw new ParseException(e.getLocalizedMessage(), offset);
		}
	}

	/**
	 * Returns the error's position with the specified script and offset.
	 * 
	 * @param script the script to check
	 * @param offset the end point
	 * @return the error's position.
	 */
	protected Position getErrorPosition(String script, int offset) {
		int end = offset;

		while (end >= script.length()
				|| (end >= 0 && (Character.isWhitespace(script.charAt(end))) || isCommentLine(script, end))) {
			end--;
		}

		int start = end;

		while (start >= 0 && !Character.isWhitespace(script.charAt(start))
				&& !Character.isJavaIdentifierPart(script.charAt(start))) {
			start--;
		}

		return new Position(start + 1, end - start);
	}

	/**
	 * Returns <code>true</code> if current index is in a comment line,
	 * <code>false</code> otherwise.
	 * 
	 * @param script the script text.
	 * @param index  the current index.
	 * @return <code>true</code> if current index is in a comment line,
	 *         <code>false</code> otherwise.
	 */

	private boolean isCommentLine(String script, int index) {
		int start = index;

		while (start >= 0 && script.charAt(start) != '\n') {
			if (start + 1 < script.length()) {
				if (script.charAt(start) == '/' && script.charAt(start + 1) == '/') {
					return true;
				}
			}
			start--;
		}
		return false;
	}
}
