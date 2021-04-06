
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class TextAndTwoButtonSection extends TextAndButtonSection {
	private Button secondButton;
	private String secondButtonText;
	private SelectionListener secondButtonListener;
	private int secondButtonWidth = 60;
	private String secondButtonTooltipText;

	public TextAndTwoButtonSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
	}

	public void createSection() {
		super.createSection();
		getSecondButtonControl(parent);
	}

	public void layout() {
		GridData gd = (GridData) textField.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 3 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 3 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillText;

		gd = (GridData) button.getLayoutData();
		if (buttonWidth > -1) {
			if (!isComputeSize)
				gd.widthHint = Math.max(button.computeSize(-1, -1).x, buttonWidth);
			else
				gd.widthHint = button.computeSize(-1, -1).x;
		}

		gd = (GridData) secondButton.getLayoutData();
		if (secondButtonWidth > -1) {
			if (!isComputeSize)
				gd.widthHint = Math.max(secondButton.computeSize(-1, -1).x, secondButtonWidth);
			else
				gd.widthHint = secondButton.computeSize(-1, -1).x;
		}
	}

	protected Button getSecondButtonControl(Composite parent) {
		if (secondButton == null) {
			secondButton = FormWidgetFactory.getInstance().createButton(parent, SWT.PUSH, isFormStyle);
			secondButton.setFont(parent.getFont());

			secondButton.setLayoutData(new GridData());
			String text = getSecondButtonText();
			if (text != null) {
				secondButton.setText(text);
			}

			text = getSecondButtonTooltipText();
			if (text != null) {
				secondButton.setToolTipText(text);
			}

			secondButton.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					secondButton = null;
				}
			});

			if (secondButtonListener == null) {
				secondButtonListener = new SelectionAdapter() {

					public void widgetSelected(SelectionEvent e) {
						onClickSecondButton();
					}
				};
			}
			secondButton.addSelectionListener(secondButtonListener);
		} else {
			checkParent(secondButton, parent);
		}
		return secondButton;
	}

	private String getSecondButtonTooltipText() {
		return secondButtonTooltipText;
	}

	private String getSecondButtonText() {
		return secondButtonText;
	}

	/**
	 * if use this method , you couldn't use the onClickButton method.
	 */
	public void addSecondSelectionListener(SelectionListener listener) {
		if (secondButton != null) {
			secondButton.removeSelectionListener(secondButtonListener);
			secondButton.addSelectionListener(listener);
		}
		secondButtonListener = listener;
	}

	public void removeSecondSelectionListener(SelectionListener listener) {
		if (secondButton != null) {
			secondButton.removeSelectionListener(listener);
		}
		if (secondButtonListener == listener) {
			secondButtonListener = null;
		}
	}

	protected void onClickSecondButton() {
	};

	public void setSecondButtonWidth(int buttonWidth) {
		this.secondButtonWidth = buttonWidth;
		if (secondButton != null) {
			GridData data = new GridData();
			data.widthHint = Math.max(secondButton.computeSize(-1, -1).x, buttonWidth);
			;
			data.grabExcessHorizontalSpace = false;
			secondButton.setLayoutData(data);
		}
	}

	public void setSecondButtonTooltipText(String string) {
		this.secondButtonTooltipText = string;
		if (secondButton != null)
			secondButton.setToolTipText(secondButtonTooltipText);

	}

	public void setSecondButtonText(String buttonText) {
		this.secondButtonText = buttonText;
		if (secondButton != null)
			secondButton.setText(buttonText);
	}
}
