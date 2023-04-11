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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderColorDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderStyleDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderToggleDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderWidthDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.BorderSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.swt.widgets.Composite;

/**
 * The borders attribute page of DE element.
 */
public class BordersPage extends ResetAttributePage {

	private static final String[] styles = { DesignChoiceConstants.LINE_STYLE_SOLID,
			DesignChoiceConstants.LINE_STYLE_DOTTED, DesignChoiceConstants.LINE_STYLE_DASHED,
			DesignChoiceConstants.LINE_STYLE_DOUBLE };

	BorderToggleDescriptorProvider[] providers;
	private static final String LABEL_BORDER = Messages.getString("BordersPage.Label.Borders"); //$NON-NLS-1$

	private BorderSection borderSection;

	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil.createGridLayout(1, 15));

		// BorderStyleDescriptorProvider styleProvider = new
		// BorderStyleDescriptorProvider( );
		// styleProvider.setItems( styles );
		// styleProvider.setIndex( styles[0] );
		// StyleComboSection styleSection = new StyleComboSection(
		// styleProvider.getDisplayName( ),
		// container,
		// true );
		// styleSection.setProvider( styleProvider );
		// styleSection.setLayoutNum( 2 );
		// styleSection.setWidth( 200 );
		// styleProvider.setIndex( styles[0] );
		// addSection( PageSectionId.BORDERS_STYLE, styleSection );
		//
		// BorderColorDescriptorProvider colorProvider = new
		// BorderColorDescriptorProvider( );
		// ColorSection colorSection = new ColorSection(
		// colorProvider.getDisplayName( ),
		// container,
		// true );
		// colorSection.setProvider( colorProvider );
		// colorSection.setGridPlaceholder( 2, true );
		// colorSection.setLayoutNum( 4 );
		// colorSection.setWidth( 200 );
		// colorProvider.setIndex( IColorConstants.BLACK );
		// addSection( PageSectionId.BORDERS_COLOR, colorSection );
		//
		// BorderWidthDescriptorProvider widthProvider = new
		// BorderWidthDescriptorProvider( );
		// StyleComboSection widthSection = new StyleComboSection(
		// widthProvider.getDisplayName( ),
		// container,
		// true );
		// widthSection.setProvider( widthProvider );
		// widthSection.setGridPlaceholder( 4, true );
		// widthSection.setWidth( 200 );
		// widthProvider.setIndex( widthProvider.getItems( )[1].toString( ) );
		// addSection( PageSectionId.BORDERS_WIDTH, widthSection );
		//
		// BorderDescriptorProvider[] dependedProviders = new
		// BorderDescriptorProvider[]{
		// styleProvider, colorProvider, widthProvider
		// };

		// diagonal and antidiagonal buttons only added on cell object (cell handle)
		if (input != null && ((List<?>) input).size() >= 1 && ((List<?>) input).get(0) instanceof CellHandle) {
			providers = new BorderToggleDescriptorProvider[] {
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_TOP_STYLE_PROP),
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_BOTTOM_STYLE_PROP),
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_LEFT_STYLE_PROP),
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_RIGHT_STYLE_PROP),
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_DIAGONAL_STYLE_PROP),
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP)
			};

		} else {
			providers = new BorderToggleDescriptorProvider[] {
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_TOP_STYLE_PROP),
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_BOTTOM_STYLE_PROP),
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_LEFT_STYLE_PROP),
					new BorderToggleDescriptorProvider(IStyleModel.BORDER_RIGHT_STYLE_PROP) };
		}

		borderSection = new BorderSection(LABEL_BORDER, container, true);
		BorderStyleDescriptorProvider styleProvider = new BorderStyleDescriptorProvider();
		styleProvider.setItems(styles);
		borderSection.setStyleProvider(styleProvider);

		BorderColorDescriptorProvider colorProvider = new BorderColorDescriptorProvider();
		borderSection.setColorProvider(colorProvider);

		BorderWidthDescriptorProvider widthProvider = new BorderWidthDescriptorProvider();
		borderSection.setWidthProvider(widthProvider);

		for (int i = 0; i < providers.length; i++) {
			providers[i].enableReset(true);
		}
		borderSection.setToggleProviders(providers);

		addSection(PageSectionId.BORDERS_BORDER_STYLE, borderSection);

		createSections();
		layoutSections();
	}

	private boolean checkControl(BorderSection border) {
		return border != null && border.getBorderControl() != null
				&& !border.getBorderControl().getControl().isDisposed();
	}

	@Override
	public void postElementEvent() {
		if (checkControl(borderSection)) {
			borderSection.getBorderControl().postElementEvent();
		}
	}

}
