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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ExpressionPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ExpressionSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormTextSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Bookmark expreesion page.
 *
 */
public class BookMarkExpressionPage extends AttributePage {
	private static String MESSAGE_GENERAL = Messages.getString("BookMarkPage.Modified.Note"); //$NON-NLS-1$
	private static String MESSAGE_WITHOUT_QUOTES = Messages.getString("BookMarkPage.Modified.WithoutQuotes"); //$NON-NLS-1$

	private FormTextSection noteSection;
	private ExpressionSection bookMarkSection;
	private ExpressionPropertyDescriptorProvider bookMarkProvider;

	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(2, 15));

		noteSection = new FormTextSection("", container, true); //$NON-NLS-1$
		noteSection.setWidth(450);
		noteSection.setFillText(false);
		noteSection.setText(generateNoteSectionText(MESSAGE_GENERAL));
		noteSection.setImage("image", //$NON-NLS-1$
				JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_WARNING));
		noteSection.setColor("color", ColorManager.getColor(127, 127, 127)); //$NON-NLS-1$
		addSection(PageSectionId.GENERAL_LIBRARY_NOTE, noteSection);

		bookMarkProvider = new ExpressionPropertyDescriptorProvider(IReportItemModel.BOOKMARK_PROP,
				ReportDesignConstants.REPORT_ITEM);
		bookMarkSection = new ExpressionSection(bookMarkProvider.getDisplayName(), container, true);
		bookMarkSection.setProvider(bookMarkProvider);
		bookMarkSection.setWidth(500);
		addSection(PageSectionId.BOOKMARKEXPRESSION_BOOKMARK, bookMarkSection);
		createSections();
		layoutSections();

	}

	@Override
	public void createSections() {
		super.createSections();
		bookMarkSection.getExpressionControl().getTextControl().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				refreshMessage();
			}
		});
	}

	@Override
	public void refresh() {
		refreshMessage();
		super.refresh();
	}

	private void refreshMessage() {
		Text text = bookMarkSection.getExpressionControl().getTextControl();
		if (text != null && !text.isDisposed()) {
			boolean isHidden = noteSection.getTextControl().isVisible();
			if (!validateBookMark(text.getText().trim())) {
				noteSection.setHidden(false);
			} else {
				noteSection.setHidden(true);
			}
			if (noteSection.getTextControl().isVisible() != isHidden) {
				FormWidgetFactory.getInstance().paintFormStyle(container);
				FormWidgetFactory.getInstance().adapt(container);
				container.layout(true);
				container.redraw();
			}
		}
	}

	private boolean validateBookMark(String text) {
		boolean result = validateQuotes(text);
		if (result) {
			setNoteSectionMessage(MESSAGE_GENERAL);
			result = validateContents(text);
		}
		return result;
	}

	private boolean validateContents(String text) {
		text = DEUtil.removeQuote(text).trim();
		if (text.length() > 0) {
			return text.matches("[a-zA-Z0-9_\\-\\:\\.]+");
		} else {
			return true;
		}
	}

	private boolean validateQuotes(String text) {
		String textTemp = DEUtil.removeQuote(text).trim();
		if (textTemp.length() > 0 && textTemp.equals(text)) {
			setNoteSectionMessage(MESSAGE_WITHOUT_QUOTES);
			return false;
		}
		return true;
	}

	private void setNoteSectionMessage(String message) {
		noteSection.setStringValue(generateNoteSectionText(message));
	}

	private String generateNoteSectionText(String detailMessage) {
		return "<form><p><img href=\"image\"/><span color=\"color\">" + //$NON-NLS-1$
				detailMessage + "</span></p></form>";//$NON-NLS-1$
	}
}
