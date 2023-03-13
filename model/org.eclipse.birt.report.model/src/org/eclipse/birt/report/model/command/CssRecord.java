/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.CssEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssNameManager;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;

/**
 *
 * Records to add/drop css.
 *
 */

public class CssRecord extends SimpleRecord {

	/**
	 * The target module
	 */

	protected Module module;

	/**
	 * Design element
	 */

	private DesignElement element;

	/**
	 * The css to operate
	 */

	private CssStyleSheet css;

	/**
	 * Whether to add or remove the css.
	 */

	private boolean add = true;

	/**
	 * Position of css file
	 */

	private int position = -1;

	/**
	 * Constructors the css record.
	 *
	 * @param module  the module
	 * @param element design element
	 * @param css     the css style sheet to add/drop
	 * @param add     whether the given css is for adding
	 */

	CssRecord(Module module, DesignElement element, CssStyleSheet css, boolean add) {
		this.module = module;
		this.element = element;
		this.css = css;
		this.add = add;
	}

	/**
	 * Constructors the css record.
	 *
	 * @param module
	 * @param element
	 * @param css
	 * @param add
	 * @param pos
	 */

	CssRecord(Module module, DesignElement element, CssStyleSheet css, boolean add, int pos) {
		this.module = module;
		this.element = element;
		this.css = css;
		this.add = add;
		this.position = pos;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	@Override
	protected void perform(boolean undo) {
		assert element instanceof ICssStyleSheetOperation;

		ICssStyleSheetOperation operation = (ICssStyleSheetOperation) element;
		if (add && !undo || !add && undo) {
			if (position == -1) {
				operation.addCss(css);
				setContainer(element, css);
				int size = operation.getCsses().size();

				// re-resolve
				CssNameManager.adjustStylesForAdd(module, operation, css, size - 1);

			} else {
				// insert css into position

				operation.insertCss(css, position);
				setContainer(element, css);
				// re-resolve
				CssNameManager.adjustStylesForAdd(module, operation, css, position);
			}
		} else {
			operation.dropCss(css);
			setContainer(null, css);
			// unresolve
			CssNameManager.adjustStylesForRemove(css);
		}
	}

	/**
	 * Sets container of CssStyleSheet. container must be report design / theme.
	 *
	 * @param element
	 * @param sheet
	 */

	private void setContainer(DesignElement element, CssStyleSheet sheet) {
		if (sheet == null) {
			return;
		}
		sheet.setContainer(element);
		List<CssStyle> styles = sheet.getStyles();
		Iterator<CssStyle> iter = styles.iterator();
		while (iter.hasNext()) {
			CssStyle style = iter.next();
			style.setCssStyleSheet(sheet);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	@Override
	public NotificationEvent getEvent() {
		if (add && state != UNDONE_STATE || !add && state == UNDONE_STATE) {
			return new CssEvent(css, CssEvent.ADD);
		}

		return new CssEvent(css, CssEvent.DROP);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	@Override
	public DesignElement getTarget() {
		return element;
	}
}
