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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.HighlightRuleBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 * Helper class for highlight rule handle operation.
 *
 * @since 2.5
 */
public class HighlightHandleProvider {

	private static final HighlightRuleHandle[] EMPTY = {};

	public static final int EXPRESSION_TYPE_ROW = 0;
	public static final int EXPRESSION_TYPE_DATA = 1;

	protected DesignElementHandle elementHandle;

	private int expressionType;

	public HighlightHandleProvider() {
		this.expressionType = EXPRESSION_TYPE_ROW;
	}

	public HighlightHandleProvider(int expressionType) {
		this.expressionType = expressionType;
	}

	public int getExpressionType() {
		return expressionType;
	}

	public void setExpressionType(int expressionType) {
		this.expressionType = expressionType;
	}

	/**
	 * Returns the final font family name.
	 *
	 * @param fontFamily
	 * @return
	 */
	public static String getFontFamily(String fontFamily) {
		String destFontName = (String) DesignerConstants.familyMap.get(fontFamily);

		if (destFontName == null) {
			destFontName = fontFamily;
		}

		return destFontName;
	}

	/**
	 * Returns the style handle for current design element.
	 *
	 * @return
	 */
	public StyleHandle getStyleHandle() {
		return elementHandle.getStyle();
	}

	/**
	 * Returns the current design element handle.
	 *
	 * @return
	 */
	public DesignElementHandle getDesignElementHandle() {
		return elementHandle;
	}

	/**
	 * Returns the column text for each highlight item.
	 *
	 * @param element     highlight rule handle element.
	 * @param columnIndex
	 * @return
	 */
	public String getColumnText(Object element, int columnIndex) {
		HighlightRuleHandle handle = (HighlightRuleHandle) element;

		switch (columnIndex) {
		case 0:
			return Messages.getString("HighlightHandleProvider.text.Preview") //$NON-NLS-1$
					+ DEUtil.getFontSize(handle.getFontSize().getStringValue()) + ")"; //$NON-NLS-1$

		case 1:
			// String exp = resolveNull( getTestExpression( ) )
			StringBuilder exp = new StringBuilder().append(resolveNull(handle.getTestExpression())).append(" " //$NON-NLS-1$
			).append(HighlightRuleBuilder.getNameForOperator(handle.getOperator()));

			int vv = HighlightRuleBuilder.determineValueVisible(handle.getOperator());

			if (vv == 1) {
				exp.append(" ").append(resolveNull(handle.getValue1())); //$NON-NLS-1$
			} else if (vv == 2) {
				exp.append(" " //$NON-NLS-1$
				).append(resolveNull(handle.getValue1())).append(" , " //$NON-NLS-1$
				).append(resolveNull(handle.getValue2()));
			} else if (vv == 3) {
				exp.append(" "); //$NON-NLS-1$
				int count = handle.getValue1List().size();
				for (int i = 0; i < count; i++) {
					if (i == 0) {
						exp.append(handle.getValue1List().get(i).toString());
					} else {
						exp.append("; ").append(handle.getValue1List().get(i).toString()); //$NON-NLS-1$
					}
				}
			}
			return exp.toString();

		default:
			return ""; //$NON-NLS-1$
		}
	}

	private String resolveNull(String src) {
		if (src == null) {
			return ""; //$NON-NLS-1$
		}

		return src;
	}

	/**
	 * Swaps the two neighbour-items, no edge check.
	 *
	 * @param pos       item position.
	 * @param direction negative - UP or LEFT, positive or zero - BOTTOM or RIGHT
	 * @return
	 * @throws PropertyValueException
	 */
	public boolean doSwapItem(int pos, int direction) throws PropertyValueException {
		PropertyHandle phandle = elementHandle.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);

		if (direction < 0) {
			phandle.moveItem(pos, pos - 1);
		} else {
			/**
			 * Original code: phandle.moveItem( pos, pos + 1 );
			 *
			 * Changes due to model api changes. since property handle now treats moving
			 * from 0-0, 0-1 as the same.
			 */
			phandle.moveItem(pos, pos + 1);
		}

		return true;
	}

	/**
	 * Deletes specified highlight rule item from current highlight rules property.
	 *
	 * @param pos item position.
	 * @return
	 * @throws PropertyValueException
	 */
	public boolean doDeleteItem(int pos) throws PropertyValueException {
		PropertyHandle phandle = elementHandle.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);

		phandle.removeItem(pos);

		try {
			if (phandle.getListValue() == null || phandle.getListValue().size() == 0) {
				elementHandle.setProperty(StyleHandle.HIGHLIGHT_RULES_PROP, null);
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}

		return true;
	}

	/**
	 * Adds new highlight rule item to current highlight rules property.
	 *
	 * @param rule new highlight rule item.
	 * @param pos  current highlight rule items count.
	 * @return new created highlight rule handle.
	 */
	public HighlightRuleHandle doAddItem(HighlightRule rule, int pos) {
		PropertyHandle phandle = elementHandle.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);

		try {
			phandle.addItem(rule);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}

		StructureHandle handle = rule.getHandle(phandle, pos);

		return (HighlightRuleHandle) handle;
	}

	/**
	 * Returns all highlight rule items from current DesignElement.
	 *
	 * @param inputElement design element handle.
	 * @return
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			if (((List) inputElement).size() > 0) {
				inputElement = ((List) inputElement).get(0);
			} else {
				inputElement = null;
			}
		}

		if (inputElement instanceof DesignElementHandle) {
			elementHandle = (DesignElementHandle) inputElement;

			PropertyHandle highRules = elementHandle.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);

			ArrayList list = new ArrayList();

			for (Iterator itr = highRules.iterator(); itr.hasNext();) {
				Object o = itr.next();

				list.add(o);
			}

			return (HighlightRuleHandle[]) list.toArray(new HighlightRuleHandle[0]);
		}

		return EMPTY;
	}

}
