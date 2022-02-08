/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.ods;

import java.util.Stack;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.ods.layout.ContainerSizeInfo;
import org.eclipse.birt.report.engine.emitter.ods.layout.OdsContainer;
import org.eclipse.birt.report.engine.emitter.ods.layout.OdsLayoutEngine;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.odf.style.StyleBuilder;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.odf.style.StyleManager;

/**
 * This class is used to calculate styles for spreadsheets.
 * 
 * 
 */
public class StyleEngine {
	private OdsLayoutEngine engine;
	private Stack<StyleEntry> containerStyles = new Stack<StyleEntry>();

	private StyleManager styleManager;

	/**
	 * 
	 * @param dataMap layout data
	 * @return a StyleEngine instance
	 */
	public StyleEngine(OdsLayoutEngine engine, StyleManager styleManager) {
		this.engine = engine;
		this.styleManager = styleManager;
	}

	public StyleEntry getDefaultEntry(int id) {
		StyleEntry entry = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_TABLE_CELL);
		/*
		 * if ( id == DEFAULT_DATE_STYLE ) { entry.setProperty(
		 * StyleConstant.DATE_FORMAT_PROP, "yyyy-M-d HH:mm:ss AM/PM" );
		 * entry.setProperty( StyleConstant.DATA_TYPE_PROP, SheetData.DATE ); }
		 */
		styleManager.addStyle(entry);
		return entry;
	}

	public StyleEntry createEntry(ContainerSizeInfo sizeInfo, IStyle style, StyleEntry parent) {
		if (style == null) {
			return StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_TABLE_CELL);
		}

		StyleEntry entry = initStyle(style, sizeInfo, parent);
		return entry;
	}

	public StyleEntry createCellEntry(ContainerSizeInfo sizeInfo, IStyle style, String diagonalLineColor,
			String diagonalLineStyle, int diagonalLineWidth, StyleEntry parent) {
		StyleEntry entry;
		if (style == null) {
			entry = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_TABLE_CELL);
		} else
			entry = initStyle(style, sizeInfo, parent);

		StyleBuilder.applyDiagonalLine(entry, PropertyUtil.getColor(diagonalLineColor), diagonalLineStyle,
				diagonalLineWidth);

		return entry;
	}

	public StyleEntry createHorizontalStyle(ContainerSizeInfo rule) {
		StyleEntry entry = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_TABLE_CELL);

		if (engine.getContainers().size() > 0) {
			OdsContainer container = engine.getCurrentContainer();
			ContainerSizeInfo crule = container.getSizeInfo();
			StyleEntry cEntry = container.getStyle();

			StyleBuilder.mergeInheritableProp(cEntry, entry);

			if (rule.getStartCoordinate() == crule.getStartCoordinate()) {
				StyleBuilder.applyLeftBorder(cEntry, entry);
			}

			if (rule.getEndCoordinate() == crule.getEndCoordinate()) {
				StyleBuilder.applyRightBorder(cEntry, entry);
			}
		}

		return entry;
	}

	public StyleEntry getStyle(IStyle style, ContainerSizeInfo rule, StyleEntry parent) {
		// This style associated element is not in any container.
		return initStyle(style, null, rule, parent);
	}

	public StyleEntry getStyle(IStyle style, ContainerSizeInfo childSizeInfo, ContainerSizeInfo parentSizeInfo,
			StyleEntry parent) {
		return initStyle(style, childSizeInfo, parentSizeInfo, parent);
	}

	private StyleEntry initStyle(IStyle style, ContainerSizeInfo childSizeInfo, ContainerSizeInfo parentSizeInfo,
			StyleEntry parent) {
		StyleEntry entry = StyleBuilder.createStyleEntry(style, StyleConstant.TYPE_TABLE_CELL);

		if (!containerStyles.isEmpty()) {
			StyleEntry centry = containerStyles.peek();
			StyleBuilder.mergeInheritableProp(centry, entry);
		}
		if (engine.getContainers().size() > 0) {
			OdsContainer container = engine.getCurrentContainer();
			StyleEntry cEntry = container.getStyle();
			StyleBuilder.mergeInheritableProp(cEntry, entry);
			if (childSizeInfo == null) {
				childSizeInfo = container.getSizeInfo();
			}
			applyHBorders(cEntry, entry, childSizeInfo, parentSizeInfo);
			applyTopBorderStyle(entry);
		}
		return entry;
	}

	private void applyTopBorderStyle(StyleEntry childStyle) {
		OdsContainer container = engine.getCurrentContainer();
		int rowIndex = container.getRowIndex();
		OdsContainer parent = container;
		while (parent != null && parent.getStartRowId() == rowIndex) {
			StyleBuilder.applyTopBorder(parent.getStyle(), childStyle);
			parent = parent.getParent();
		}
	}

	private void applyHBorders(StyleEntry centry, StyleEntry entry, ContainerSizeInfo crule, ContainerSizeInfo rule) {
		if (crule == null || rule == null) {
			return;
		}
		if (crule.getStartCoordinate() == rule.getStartCoordinate()) {
			StyleBuilder.applyLeftBorder(centry, entry);
		}

		if (crule.getEndCoordinate() == rule.getEndCoordinate()) {
			StyleBuilder.applyRightBorder(centry, entry);
		}
	}

	private StyleEntry initStyle(IStyle style, ContainerSizeInfo rule, StyleEntry parent) {
		return initStyle(style, null, rule, parent);
	}

	public void addContainderStyle(IStyle computedStyle, StyleEntry parent) {
		StyleEntry entry = StyleBuilder.createStyleEntry(computedStyle,
				(parent != null) ? parent.getType() : StyleConstant.TYPE_TABLE_CELL);
		if (!containerStyles.isEmpty()) {
			StyleEntry centry = containerStyles.peek();
			StyleBuilder.mergeInheritableProp(centry, entry);
		}
		styleManager.addStyle(entry);
		containerStyles.add(entry);
	}

	public void removeForeignContainerStyle() {
		if (!containerStyles.isEmpty())
			containerStyles.pop();
	}

	/**
	 * 
	 */
	public void applyContainerBottomStyle() {
		applyContainerBottomStyle(engine.getCurrentContainer());
	}

	public void applyContainerBottomStyle(OdsContainer container) {
		ContainerSizeInfo rule = container.getSizeInfo();
		StyleEntry entry = container.getStyle();
		int start = rule.getStartCoordinate();
		int col = engine.getAxis().getColumnIndexByCoordinate(start);
		int span = engine.getAxis().getColumnIndexByCoordinate(rule.getEndCoordinate());
		for (int i = col; i < span; i++) {
			SheetData data = engine.getColumnLastData(i);

			if (data == null) {
				continue;
			}

			StyleEntry styleId = data.getStyleId();
			if (styleId != null) {
				StyleEntry originalStyle = styleId;
				StyleEntry newStyle = (StyleEntry) originalStyle.clone();
				boolean isChanged = StyleBuilder.applyBottomBorder(entry, newStyle);
				if (isChanged) {
					styleManager.addStyle(newStyle);
				}
			}
		}
	}
}
