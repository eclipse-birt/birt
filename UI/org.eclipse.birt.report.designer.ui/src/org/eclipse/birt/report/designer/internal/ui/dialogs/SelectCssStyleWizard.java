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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;

/**
 * Wizard for Selecting Css styles from CSS file.
 */

public class SelectCssStyleWizard extends Wizard {

	private static final String WIZARD_PAGE_DESCRIPTION_REPORT = Messages
			.getString("SelectCssStyleWizard.wizardPage.description.report"); //$NON-NLS-1$

	private static final String WIZARD_PAGE_DESCRIPTION_LIBRARY = Messages
			.getString("SelectCssStyleWizard.wizardPage.description.library"); //$NON-NLS-1$

	private static final String WIZARD_PAGE_TITLE = Messages.getString("SelectCssStyleWizard.wizardPage.title"); //$NON-NLS-1$

	private static final String WIZARD_PAGE_NAME = Messages.getString("SelectCssStyleWizard.wizardPage.name"); //$NON-NLS-1$

	private static final String WIZARD_TITLE = Messages.getString("SelectCssStyleWizard.wizard.title"); //$NON-NLS-1$

	private Object selection;

	private WizardSelectCssStylePage stylePage;

	public SelectCssStyleWizard(Object selection) {
		setWindowTitle(WIZARD_TITLE);
		this.selection = selection;
	}

	public Image getDefaultPageImage() {
		// return ReportPlatformUIImages.getImage(
		// IReportGraphicConstants.ICON_ELEMENT_STYLE );
		return super.getDefaultPageImage();
	}

	public void addPages() {
		stylePage = new WizardSelectCssStylePage(WIZARD_PAGE_NAME);

		stylePage.setTitle(WIZARD_PAGE_TITLE);

		String pageDesc = WIZARD_PAGE_DESCRIPTION_REPORT;
		if (selection != null && selection instanceof DesignElementHandle) {
			DesignElementHandle element = (DesignElementHandle) selection;
			if (element instanceof AbstractThemeHandle || element.getContainer() instanceof AbstractThemeHandle) {
				pageDesc = WIZARD_PAGE_DESCRIPTION_LIBRARY;
				ReportItemThemeHandle theme = null;
				if (element instanceof ReportItemThemeHandle) {
					theme = ((ReportItemThemeHandle) element);
				} else if (element.getContainer() instanceof ReportItemThemeHandle) {
					theme = ((ReportItemThemeHandle) element.getContainer());
				}

				if (theme != null)
					stylePage.setTheme(theme);
			}
		}
		stylePage.setDescription(pageDesc);
		addPage(stylePage);
	}

	public boolean canFinish() {
		return stylePage.isPageComplete();
	}

	public boolean performFinish() {
		CssStyleSheetHandle cssHandle = stylePage.getCssHandle();
		if (cssHandle != null) {
			List styleList = stylePage.getStyleList();
			ModuleHandle module = SessionHandleAdapter.getInstance().getReportDesignHandle();

			if (selection != null && selection instanceof DesignElementHandle) {
				DesignElementHandle element = (DesignElementHandle) selection;
				if (selection instanceof ThemeHandle)// selection is Theme
				// node.
				{
					LibraryHandle libraryHandle = (LibraryHandle) module;
					libraryHandle.importCssStyles(cssHandle, styleList, element.getName());
				} else if (element.getContainer() instanceof ThemeHandle)
				// selection is a Style node under Theme node.
				{
					LibraryHandle libraryHandle = (LibraryHandle) module;
					libraryHandle.importCssStyles(cssHandle, styleList, element.getContainer().getName());
				} else if (selection instanceof ReportItemThemeHandle) {
					ReportItemThemeHandle theme = ((ReportItemThemeHandle) selection);
					try {
						theme.importCssStyles(cssHandle, styleList);
					} catch (SemanticException e) {
						ExceptionHandler.handle(e);
					}
				} else if (element.getContainer() instanceof ReportItemThemeHandle) {
					ReportItemThemeHandle theme = ((ReportItemThemeHandle) element.getContainer());
					try {
						theme.importCssStyles(cssHandle, styleList);
					} catch (SemanticException e) {
						ExceptionHandler.handle(e);
					}
				} else if (element instanceof StyleHandle)// selection is a
				// Style node in
				// Report.
				{
					module.importCssStyles(cssHandle, styleList);
				}
			} else {
				// no selection.
				if (module instanceof LibraryHandle) {
					LibraryHandle libraryHandle = (LibraryHandle) module;
					libraryHandle.importCssStyles(cssHandle, styleList);
				} else if (module instanceof ReportDesignHandle) {
					module.importCssStyles(cssHandle, styleList);
				}
			}
		}
		return true;
	}
}
