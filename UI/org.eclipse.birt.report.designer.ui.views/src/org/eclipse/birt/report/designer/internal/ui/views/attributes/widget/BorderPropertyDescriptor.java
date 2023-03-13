/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IFastConsumerProcessor;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.BorderCanvas;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.BorderInfomation;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.IComboProvider;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.StyleCombo;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderColorDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderStyleDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderToggleDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderWidthDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IToggleDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.IPropertyDescriptor;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class BorderPropertyDescriptor implements IPropertyDescriptor, IFastConsumerProcessor, Listener {

	private boolean isFormStyle;

	private BorderInfomation restoreInfo;

	public BorderPropertyDescriptor(boolean isFormStyle) {
		this.isFormStyle = isFormStyle;
	}

	@Override
	public Control createControl(Composite parent) {
		content = new Composite(parent, SWT.NONE);
		GridLayout layout = UIUtil.createGridLayoutWithoutMargin(2, false);
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		layout.horizontalSpacing = 10;
		content.setLayout(layout);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite choices = new Composite(content, SWT.NONE);
		GridData data = new GridData(GridData.FILL_BOTH);

		choices.setLayoutData(data);
		layout = WidgetUtil.createGridLayout(2);
		layout.marginHeight = 1;
		layout.marginWidth = 2;
		choices.setLayout(layout);

		Label styleLabel = FormWidgetFactory.getInstance().createLabel(choices, SWT.LEFT, isFormStyle);
		styleLabel.setText(styleProvider.getDisplayName());
		styleLabel.setLayoutData(new GridData());

		if (isFormStyle) {
			styleCombo = FormWidgetFactory.getInstance().createStyleCombo(choices, (IComboProvider) styleProvider);
		} else {
			styleCombo = new StyleCombo(choices, style, (IComboProvider) styleProvider);
		}
		data = new GridData();
		data.widthHint = 200;
		styleCombo.setLayoutData(data);
		styleCombo.setItems(((IComboProvider) styleProvider).getDisplayItems());
		styleProvider.setIndex(styleProvider.getDisplayItems()[0].toString());

		Label colorLabel = FormWidgetFactory.getInstance().createLabel(choices, SWT.LEFT, isFormStyle);
		colorLabel.setText(colorProvider.getDisplayName());
		colorLabel.setLayoutData(new GridData());

		builder = new ColorBuilder(choices, SWT.NONE, isFormStyle);
		builder.setChoiceSet(colorProvider.getElementChoiceSet());
		// colorProvider.setIndex( IColorConstants.BLACK );
		data = new GridData();
		data.widthHint = 200;
		data.heightHint = builder.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		builder.setLayoutData(data);

		Label widthLabel = FormWidgetFactory.getInstance().createLabel(choices, SWT.LEFT, isFormStyle);
		widthLabel.setText(widthProvider.getDisplayName());
		widthLabel.setLayoutData(new GridData());

		if (isFormStyle) {
			widthCombo = FormWidgetFactory.getInstance().createStyleCombo(choices, (IComboProvider) widthProvider);
		} else {
			widthCombo = new StyleCombo(choices, style, (IComboProvider) widthProvider);
		}
		data = new GridData();
		data.widthHint = 200;
		widthCombo.setLayoutData(data);
		widthCombo.setItems(((IComboProvider) widthProvider).getDisplayItems());
		widthProvider.setIndex(widthProvider.getDisplayItems()[1].toString());

		Composite composite = new Composite(choices, SWT.NONE);
		layout = new GridLayout();
		layout.horizontalSpacing = 7;
		layout.numColumns = toggleProviders.length + 2;
		composite.setLayout(layout);
		data = new GridData();
		data.horizontalSpan = 2;
		composite.setLayoutData(data);

		toggles = new Button[toggleProviders.length];
		for (int i = 0; i < toggleProviders.length; i++) {
			Button button = new Button(composite, SWT.TOGGLE);
			toggles[i] = button;
			button.setLayoutData(new GridData());
			button.setToolTipText(toggleProviders[i].getTooltipText());
			button.setImage(ReportPlatformUIImages.getImage(toggleProviders[i].getImageName()));
			final BorderToggleDescriptorProvider provider = toggleProviders[i];
			button.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					Button button = ((Button) e.widget);
					if (button.getSelection()) {
						handleBorderSelection(provider);
					} else {
						handleBorderDeselection(provider, button);
					}
					previewCanvas.redraw();
				}

			});
			button.setData(provider);
			button.getAccessible().addAccessibleListener(new AccessibleAdapter() {
				@Override
				public void getName(AccessibleEvent e) {
					Accessible accessible = (Accessible) e.getSource();
					Button item = (Button) accessible.getControl();
					if (item != null) {
						e.result = item.getToolTipText();
					}
				}
			});
		}

		allButton = new Button(composite, SWT.TOGGLE);
		allButton.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_FRAME));
		allButton.setToolTipText(Messages.getString("BordersPage.Tooltip.All")); //$NON-NLS-1$
		allButton.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				Accessible accessible = (Accessible) e.getSource();
				Button item = (Button) accessible.getControl();
				if (item != null) {
					e.result = item.getToolTipText();
				}
			}
		});
		allButton.setLayoutData(new GridData());
		allButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				RGB selectedColor = null;
				RGB oldColor = null;
				if (((Button) e.widget).getSelection()) {
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(Messages.getString("BordersPage.Trans.SelectAllborders")); //$NON-NLS-1$
					selectedColor = builder.getRGB();
					// if ( selectedColor == null )
					// {
					// selectedColor = autoColor;
					// }
					for (int i = 0; i < toggleProviders.length; i++) {
						BorderInfomation oldInfo = (BorderInfomation) toggleProviders[i].load();
						BorderInfomation information = new BorderInfomation();
						information.setPosition(toggleProviders[i].getPosition());
						information.setColor(selectedColor);
						information.setStyle((String) styleProvider.getItems()[styleCombo.getSelectionIndex()]);
						information.setWidth((String) widthProvider.getItems()[widthCombo.getSelectionIndex()]);
						information.setInheritedColor(oldInfo.getInheritedColor());
						information.setInheritedStyle(oldInfo.getInheritedStyle());
						information.setInheritedWidth(oldInfo.getInheritedWidth());
						information.setDefaultColor(oldInfo.getDefaultColor());
						information.setDefaultStyle(oldInfo.getDefaultStyle());
						information.setDefaultWidth(oldInfo.getDefaultWidth());

						toggles[i].setSelection(true);
						previewCanvas.setBorderInfomation(information);
						restoreInfo = information;
						try {
							toggleProviders[i].save(information);
						} catch (Exception e1) {
							ExceptionUtil.handle(e1);
						}
					}
					// restoreInfo = (BorderInfomation)
					// toggleProviders[toggleProviders.length - 1].load( );
					// restoreInfo.setColor( selectedColor );
					stack.commit();
				} else {
					boolean reset = true;
					for (int i = 0; i < toggleProviders.length; i++) {
						BorderInfomation info = (BorderInfomation) toggleProviders[i].load();
						oldColor = info.getOriginColor();
						selectedColor = builder.getRGB();
						// if ( oldColor == null )
						// {
						// oldColor = autoColor;
						// }
						// if ( selectedColor == null )
						// {
						// selectedColor = autoColor;
						// }
						if (!(info.getOriginStyle()
								.equals((String) styleProvider.getItems()[styleCombo.getSelectionIndex()]))
								|| !((oldColor == null && selectedColor == null)
										|| (oldColor != null && oldColor.equals(selectedColor)))
								|| !(resolveEmptyWidth(info)
										.equals((String) widthProvider.getItems()[widthCombo.getSelectionIndex()]))) {
							reset = false;
							break;
						}
					}
					if (reset) {
						CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
						stack.startTrans(Messages.getString("BordersPage.Trans.UnSelectAllborders")); //$NON-NLS-1$

						for (int i = 0; i < toggleProviders.length; i++) {
							previewCanvas.removeBorderInfomation(toggleProviders[i].getPosition());
							toggles[i].setSelection(false);
							try {
								toggleProviders[i].reset();
							} catch (Exception e1) {
								ExceptionUtil.handle(e1);
							}
						}
						stack.commit();
					} else {
						CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
						stack.startTrans(Messages.getString("BordersPage.Trans.SelectAllborders")); //$NON-NLS-1$

						for (int i = 0; i < toggleProviders.length; i++) {
							BorderInfomation oldInfo = (BorderInfomation) toggleProviders[i].load();
							BorderInfomation information = new BorderInfomation();
							information.setPosition(toggleProviders[i].getPosition());
							information.setColor(builder.getRGB());
							information.setStyle((String) styleProvider.getItems()[styleCombo.getSelectionIndex()]);
							information.setWidth((String) widthProvider.getItems()[widthCombo.getSelectionIndex()]);
							information.setInheritedColor(oldInfo.getInheritedColor());
							information.setInheritedStyle(oldInfo.getInheritedStyle());
							information.setInheritedWidth(oldInfo.getInheritedWidth());
							information.setDefaultColor(oldInfo.getDefaultColor());
							information.setDefaultStyle(oldInfo.getDefaultStyle());
							information.setDefaultWidth(oldInfo.getDefaultWidth());

							previewCanvas.setBorderInfomation(information);
							restoreInfo = information;
							try {
								toggleProviders[i].save(information);
							} catch (Exception e1) {
								ExceptionUtil.handle(e1);
							}
						}
						((Button) e.widget).setSelection(true);
						stack.commit();
					}
				}
				previewCanvas.redraw();
			}
		});

		Button noneButton = new Button(composite, SWT.PUSH);
		noneButton.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_NONE));
		noneButton.setToolTipText(Messages.getString("BordersPage.Tooltip.None")); //$NON-NLS-1$
		noneButton.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				Accessible accessible = (Accessible) e.getSource();
				Button item = (Button) accessible.getControl();
				if (item != null) {
					e.result = item.getToolTipText();
				}
			}
		});
		noneButton.setLayoutData(new GridData());
		noneButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
				stack.startTrans(Messages.getString("BordersPage.Trans.SelectAllborders")); //$NON-NLS-1$
				for (int i = 0; i < toggleProviders.length; i++) {
					BorderInfomation oldInfo = (BorderInfomation) toggleProviders[i].load();
					BorderInfomation information = new BorderInfomation();
					information.setPosition(toggleProviders[i].getPosition());
					information.setStyle(DesignChoiceConstants.LINE_STYLE_NONE);
					information.setInheritedColor(oldInfo.getInheritedColor());
					information.setInheritedStyle(oldInfo.getInheritedStyle());
					information.setInheritedWidth(oldInfo.getInheritedWidth());
					information.setDefaultColor(oldInfo.getDefaultColor());
					information.setDefaultStyle(oldInfo.getDefaultStyle());
					information.setDefaultWidth(oldInfo.getDefaultWidth());

					toggles[i].setSelection(true);
					previewCanvas.setBorderInfomation(information);
					restoreInfo = information;
					try {
						toggleProviders[i].save(information);
					} catch (Exception e1) {
						ExceptionUtil.handle(e1);
					}
				}
				// restoreInfo = (BorderInfomation)
				// toggleProviders[toggleProviders.length - 1].load( );
				// restoreInfo.setColor( selectedColor );
				stack.commit();

				previewCanvas.redraw();
			}
		});

		Composite previewContainer = new Composite(content, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH);
		previewContainer.setLayoutData(data);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 1;
		layout.marginWidth = 10;
		previewContainer.setLayout(layout);

		Label previewLabel = FormWidgetFactory.getInstance().createLabel(previewContainer, SWT.LEFT, isFormStyle);
		data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		previewLabel.setLayoutData(data);
		previewLabel.setText(Messages.getString("BordersPage.text.Preview")); //$NON-NLS-1$

		previewCanvas = new BorderCanvas(previewContainer, SWT.NONE);
		data = new GridData();
		data.widthHint = 130;
		data.heightHint = 130;
		previewCanvas.setLayoutData(data);
		previewCanvas.setListener(this);
		return content;
	}

	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return content;
	}

	protected Object input;

	@Override
	public void setInput(Object input) {
		this.input = input;
		styleProvider.setInput(input);
		colorProvider.setInput(input);
		widthProvider.setInput(input);
		for (int i = 0; i < toggleProviders.length; i++) {
			toggleProviders[i].setInput(input);
		}
	}

	void refreshStyle(String value) {
		styleCombo.setSelectedItem(value);
	}

	void refreshWidth(String value) {
		widthCombo.setSelectedItem(value);
	}

	public void refreshColor(RGB rgb) {
		if (rgb != null) {
			builder.setColorValue(
					ColorUtil.format(ColorUtil.formRGB(rgb.red, rgb.green, rgb.blue), ColorUtil.HTML_FORMAT));
		}
	}

	@Override
	public void load() {
		// for ( int i = toggleProviders.length - 1; i >= 0; i-- )
		for (int i = 0; i < toggleProviders.length; i++) {
			BorderInfomation info = (BorderInfomation) toggleProviders[i].load();
			previewCanvas.setBorderInfomation(info);
			if (!info.getStyle().equals("") && !DesignChoiceConstants.LINE_STYLE_NONE.equals(info.getStyle())) //$NON-NLS-1$
			{
				toggles[i].setSelection(true);
			} else {
				toggles[i].setSelection(false);
			}
		}
		previewCanvas.redraw();
		if (restoreInfo == null) {
			if (styleCombo.getSelectedItem() == null) {
				String borderStyle = styleProvider.load().toString();
				refreshStyle(borderStyle);
			}
			if (widthCombo.getSelectedItem() == null) {
				String borderWidth = widthProvider.load().toString();
				refreshWidth(borderWidth);
			}
			if (builder.getRGB() == null) {
				String borderColor = colorProvider.load().toString();
				refreshColor(borderColor);
			}
		} else {
			refreshStyle(restoreInfo.getStyle());
			refreshWidth(restoreInfo.getWidth());
			if (restoreInfo.getOriginColor() == null) {
				refreshColor((RGB) null);
			} else {
				refreshColor(restoreInfo.getColor());
			}
		}
		checkToggleButtons();
	}

	public void refreshColor(String value) {

		boolean stateFlag = ((value == null) == builder.getEnabled());
		if (stateFlag) {
			builder.setEnabled(value != null);
		}
		builder.setColorValue(value);
	}

	@Override
	public void save(Object obj) throws SemanticException {

	}

	private BorderStyleDescriptorProvider styleProvider = null;

	public void setStyleProvider(IDescriptorProvider provider) {
		if (provider instanceof BorderStyleDescriptorProvider) {
			this.styleProvider = (BorderStyleDescriptorProvider) provider;
		}
	}

	private BorderColorDescriptorProvider colorProvider = null;

	public void setColorProvider(IDescriptorProvider provider) {
		if (provider instanceof BorderColorDescriptorProvider) {
			this.colorProvider = (BorderColorDescriptorProvider) provider;
		}
	}

	private BorderWidthDescriptorProvider widthProvider = null;
	private Composite content;

	public void setWidthProvider(IDescriptorProvider provider) {
		if (provider instanceof BorderWidthDescriptorProvider) {
			this.widthProvider = (BorderWidthDescriptorProvider) provider;
		}
	}

	private int style = SWT.BORDER;
	private StyleCombo styleCombo;
	private StyleCombo widthCombo;

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(content, isHidden);
	}

	public void setVisible(boolean isVisible) {
		content.setVisible(isVisible);
	}

	BorderToggleDescriptorProvider[] toggleProviders;
	private ColorBuilder builder;
	private Button[] toggles;
	private BorderCanvas previewCanvas;
	private Button allButton;

	public IToggleDescriptorProvider[] getToggleProviders() {
		return toggleProviders;
	}

	public void setToggleProviders(BorderToggleDescriptorProvider[] toggleProviders) {
		this.toggleProviders = toggleProviders;
	}

	private void checkToggleButtons() {
		boolean allSelected = true;
		for (int i = 0; i < toggles.length; i++) {
			if (!toggles[i].getSelection()) {
				allSelected = false;
				break;
			}
		}
		if (allSelected) {
			allButton.setSelection(true);
		} else {
			allButton.setSelection(false);
		}
	}

	// public void elementChanged( DesignElementHandle focus, NotificationEvent
	// ev )
	// {
	// PropertyEvent event = (PropertyEvent) ev;
	// String propertyName = event.getPropertyName( );
	// if ( propertyName.equals( StyleHandle.BORDER_BOTTOM_WIDTH_PROP )
	// || propertyName.equals( StyleHandle.BORDER_TOP_WIDTH_PROP )
	// || propertyName.equals( StyleHandle.BORDER_LEFT_WIDTH_PROP )
	// || propertyName.equals( StyleHandle.BORDER_RIGHT_WIDTH_PROP ) )
	// {
	// load( );
	// }
	// else if ( propertyName.equals( StyleHandle.BORDER_BOTTOM_STYLE_PROP )
	// || propertyName.equals( StyleHandle.BORDER_TOP_STYLE_PROP )
	// || propertyName.equals( StyleHandle.BORDER_LEFT_STYLE_PROP )
	// || propertyName.equals( StyleHandle.BORDER_RIGHT_STYLE_PROP ) )
	// {
	// load( );
	// }
	// else if ( propertyName.equals( StyleHandle.BORDER_BOTTOM_COLOR_PROP )
	// || propertyName.equals( StyleHandle.BORDER_LEFT_COLOR_PROP )
	// || propertyName.equals( StyleHandle.BORDER_RIGHT_COLOR_PROP )
	// || propertyName.equals( StyleHandle.BORDER_TOP_COLOR_PROP ) )
	// {
	// load( );
	// }
	// }

	@Override
	public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {

	}

	@Override
	public void clear() {

	}

	@Override
	public boolean isOverdued() {
		return content == null || content.isDisposed();
	}

	@Override
	public void postElementEvent() {
		load();
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	private void handleBorderSelection(final BorderToggleDescriptorProvider provider) {
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(Messages.getString("BordersPage.Trans.SelectBorder")); //$NON-NLS-1$

		BorderInfomation oldInfo = (BorderInfomation) provider.load();
		BorderInfomation information = new BorderInfomation();
		information.setPosition(provider.getPosition());
		information.setColor(builder.getRGB());
		information.setStyle((String) styleProvider.getItems()[styleCombo.getSelectionIndex()]);
		information.setWidth((String) widthProvider.getItems()[widthCombo.getSelectionIndex()]);
		information.setInheritedColor(oldInfo.getInheritedColor());
		information.setInheritedStyle(oldInfo.getInheritedStyle());
		information.setInheritedWidth(oldInfo.getInheritedWidth());
		information.setDefaultColor(oldInfo.getDefaultColor());
		information.setDefaultStyle(oldInfo.getDefaultStyle());
		information.setDefaultWidth(oldInfo.getDefaultWidth());

		previewCanvas.setBorderInfomation(information);
		restoreInfo = information;
		try {
			provider.save(information);
		} catch (Exception e1) {
			ExceptionUtil.handle(e1);
		}
		checkToggleButtons();

		stack.commit();
	}

	private void handleBorderDeselection(final BorderToggleDescriptorProvider provider, Button button) {
		BorderInfomation oldInfo = (BorderInfomation) provider.load();
		RGB oldColor = oldInfo.getOriginColor();
		RGB selectedColor = builder.getRGB();
		// if ( oldColor == null )
		// {
		// oldColor = autoColor;
		// }
		// if ( selectedColor == null )
		// {
		// selectedColor = autoColor;
		// }
		if (!(oldInfo.getOriginStyle().equals((String) styleProvider.getItems()[styleCombo.getSelectionIndex()]))
				|| !((oldColor == null && selectedColor == null)
						|| (oldColor != null && oldColor.equals(selectedColor)))
				|| !(resolveEmptyWidth(oldInfo)
						.equals((String) widthProvider.getItems()[widthCombo.getSelectionIndex()]))) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(Messages.getString("BordersPage.Trans.SelectBorder")); //$NON-NLS-1$

			BorderInfomation information = new BorderInfomation();
			information.setPosition(provider.getPosition());
			information.setColor(selectedColor);
			information.setStyle((String) styleProvider.getItems()[styleCombo.getSelectionIndex()]);
			information.setWidth((String) widthProvider.getItems()[widthCombo.getSelectionIndex()]);
			information.setInheritedColor(oldInfo.getInheritedColor());
			information.setInheritedStyle(oldInfo.getInheritedStyle());
			information.setInheritedWidth(oldInfo.getInheritedWidth());
			information.setDefaultColor(oldInfo.getDefaultColor());
			information.setDefaultStyle(oldInfo.getDefaultStyle());
			information.setDefaultWidth(oldInfo.getDefaultWidth());

			previewCanvas.setBorderInfomation(information);
			restoreInfo = information;
			try {
				provider.save(information);
			} catch (Exception e1) {
				ExceptionUtil.handle(e1);
			}
			button.setSelection(true);
			stack.commit();
		} else {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(Messages.getString("BordersPage.Trans.UnSelectBorder")); //$NON-NLS-1$

			previewCanvas.removeBorderInfomation(provider.getPosition());
			if (allButton.getSelection()) {
				allButton.setSelection(false);
			}
			try {
				provider.reset();
			} catch (Exception e1) {
				ExceptionUtil.handle(e1);
			}
			stack.commit();
		}
	}

	@Override
	public void handleEvent(Event event) {
		String property = null;
		switch (event.detail) {
		case SWT.TOP:
			property = StyleHandle.BORDER_TOP_STYLE_PROP;
			break;
		case SWT.BOTTOM:
			property = StyleHandle.BORDER_BOTTOM_STYLE_PROP;
			break;
		case SWT.LEFT:
			property = StyleHandle.BORDER_LEFT_STYLE_PROP;
			break;
		case SWT.RIGHT:
			property = StyleHandle.BORDER_RIGHT_STYLE_PROP;
			break;
		}
		for (int i = 0; i < toggleProviders.length; i++) {
			if (toggleProviders[i].getProperty().equals(property)) {
				BorderToggleDescriptorProvider provider = (BorderToggleDescriptorProvider) toggleProviders[i];
				for (int j = 0; j < toggles.length; j++) {
					if (toggles[j].getData() != null && toggles[j].getData() == provider) {
						Button button = (Button) toggles[j];
						if (button.getSelection()) {
							button.setSelection(false);
							handleBorderDeselection(provider, button);
						} else {
							button.setSelection(true);
							handleBorderSelection(provider);
						}
						previewCanvas.redraw();
						return;
					}
				}
			}
		}
	}

	@Override
	public void reset() {
		for (int i = 0; i < toggleProviders.length; i++) {
			if (toggleProviders[i] != null && toggleProviders[i].canReset()) {
				try {
					toggleProviders[i].reset();
				} catch (Exception e1) {
					ExceptionUtil.handle(e1);
				}
			}
		}
	}

	private String resolveEmptyWidth(BorderInfomation info) {
		String width = info.getOriginWidth();
		if ("".equals(width)) {
			return DesignChoiceConstants.LINE_WIDTH_MEDIUM;
		}
		return width;
	}
}
