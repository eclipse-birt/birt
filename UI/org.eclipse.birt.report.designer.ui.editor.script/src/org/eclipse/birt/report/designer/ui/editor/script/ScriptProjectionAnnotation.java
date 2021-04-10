/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editor.script;

import org.eclipse.jface.text.source.projection.ProjectionAnnotation;

/**
 * Annotation used to represent the script projection.
 */
public class ScriptProjectionAnnotation extends ProjectionAnnotation {

	/** The style of comment. */
	public static final int SCRIPT_COMMENT = 1;

	/** The style of method. */
	public static final int SCRIPT_METHOD = 2;

	/** The style of current annotation. */
	private final int style;

	/**
	 * Consttracts an annotation of script projection.
	 */
	public ScriptProjectionAnnotation() {
		this(0);
	}

	/**
	 * Consttracts an annotation of script projection with the specified style.
	 * 
	 * @param style the style of current annotation.
	 */
	public ScriptProjectionAnnotation(int style) {
		super();
		this.style = style;
	}

	/**
	 * Checks the current style with the specified style.
	 * 
	 * @param style the specified style to check.
	 * @return
	 */
	public boolean isStyle(int style) {
		return (this.style & style) != 0;
	}
}
