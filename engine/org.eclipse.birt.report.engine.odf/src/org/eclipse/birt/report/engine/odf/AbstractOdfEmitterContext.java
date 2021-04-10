/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.odf;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.odf.pkg.ImageManager;
import org.eclipse.birt.report.engine.odf.pkg.Package;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.odf.style.StyleManager;

import com.ibm.icu.util.ULocale;

public class AbstractOdfEmitterContext {
	protected int dpi;
	protected String tempFileDir;
	protected ULocale locale;

	protected Package pkg;

	protected ImageManager imageManager;

	/**
	 * Style manager for the styles defined in content.xml
	 */
	protected StyleManager styleManager;

	/**
	 * Style manager for the global styles defined in styles.xml (ex: master page)
	 */
	protected StyleManager globalStyleManager;
	protected LinkedList<TableInfo> tables = new LinkedList<TableInfo>();

	protected Stack<Boolean> cellind = new Stack<Boolean>();

	protected MasterPageManager masterPageManager;

	protected AbstractOdfEmitterContext() {
		this.dpi = 96;

		styleManager = new StyleManager(null);
		globalStyleManager = new StyleManager("Global_"); //$NON-NLS-1$
		masterPageManager = new MasterPageManager();
	}

	public ULocale getLocale() {
		return this.locale;
	}

	public void setLocale(ULocale locale) {
		this.locale = locale;
	}

	public void setPackage(Package pkg) {
		this.pkg = pkg;
	}

	public Package getPackage() {
		return pkg;
	}

	public void setTempFileDir(String tempFileDir) {
		this.tempFileDir = tempFileDir;
	}

	public String getTempFileDir() {
		return this.tempFileDir;
	}

	public void startCell() {
		cellind.push(true);
	}

	public void endCell() {
		cellind.pop();
	}

	public boolean needEmptyP() {
		return cellind.peek();
	}

	public void addContainer(boolean isContainer) {
		if (!cellind.isEmpty()) {
			cellind.pop();
			cellind.push(isContainer);
		}
	}

	private boolean lastTable = false;

	public void setLastIsTable(boolean isTable) {
		this.lastTable = isTable;
	}

	public boolean isLastTable() {
		return this.lastTable;
	}

	public StyleManager getGlobalStyleManager() {
		return globalStyleManager;
	}

	public StyleManager getStyleManager() {
		return styleManager;
	}

	public void setReportDpi(int dpi) {
		this.dpi = dpi;
	}

	public int getReportDpi() {
		return dpi;
	}

	public String addStyle(String prefix, StyleEntry style) {
		return getStyleManager().addStyle(prefix, style);
	}

	public String addStyle(StyleEntry style) {
		return getStyleManager().addStyle(style);
	}

	public String addGlobalStyle(StyleEntry style) {
		return getGlobalStyleManager().addStyle(style);
	}

	public String addGlobalStyle(String prefix, StyleEntry style) {
		return getGlobalStyleManager().addStyle(prefix, style);
	}

	public double[] getCurrentTableColmns() {
		return tables.getLast().getColumnWidths();
	}

	public void addTable(double[] cols, IStyle style) {
		tables.addLast(new TableInfo(cols, style));
	}

	public IStyle getTableStyle() {
		return tables.getLast().getTableStyle();
	}

	public void newRow() {
		tables.getLast().newRow();
	}

	public void addSpan(int colmunId, int columnSpan, int rowSpan, StyleEntry style) {
		tables.getLast().addSpan(colmunId, columnSpan, rowSpan, style);
	}

	public void removeTable() {
		tables.removeLast();
	}

	public List<SpanInfo> getSpans(int col) {
		return tables.getLast().getSpans(col);
	}

	public MasterPageManager getMasterPageManager() {
		return masterPageManager;
	}

	public ImageManager getImageManager() {
		if (imageManager == null && pkg != null) {
			imageManager = new ImageManager(pkg);
		}
		return imageManager;
	}

}
