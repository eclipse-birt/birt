/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.fieldassist;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.fieldassist.preferences.FieldAssistPreferenceInitializer;
import org.eclipse.birt.chart.ui.swt.fieldassist.preferences.PreferenceConstants;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * The class provides functions to control and play field assist.
 * 
 * @since 2.5
 */

public final class FieldAssistHelper {

	/** The single instance of the class. */
	private static FieldAssistHelper foInstance = null;

	// Our own content assist decorator (which adds the key binding)
	private String DEC_CONTENTASSIST_ID = "org.eclipse.birt.chart.ui.swt.fieldassist.contentAssistDecoration"; //$NON-NLS-1$

	/** Default decoration margin. */
	private static final int DECORATION_MARGIN = IDialogConstants.HORIZONTAL_MARGIN;

	/** The handle of preference properties store. */
	private IPreferenceStore soPreferenceStore = null;

	/**
	 * Return only instance of the class.
	 * 
	 * @return instance of <code>FieldAssistHelper</code>
	 */
	public static FieldAssistHelper getInstance() {
		if (foInstance == null) {
			foInstance = new FieldAssistHelper();
		}
		return foInstance;
	}

	/**
	 * Private constructor.
	 */
	private FieldAssistHelper() {
		if (soPreferenceStore == null) {
			if (ChartUIExtensionPlugin.getDefault() == null) {
				soPreferenceStore = new PreferenceStore();
				FieldAssistPreferenceInitializer.setDefaultPreferences(soPreferenceStore);
				return;
			}

			soPreferenceStore = ChartUIExtensionPlugin.getDefault().getPreferenceStore();
		}
	}

	private IPreferenceStore getPreferenceStore() {
		return soPreferenceStore;
	}

	private String getTriggerKey() {
		IPreferenceStore store = getPreferenceStore();
		String triggerKey = store.getString(PreferenceConstants.PREF_CONTENTASSISTKEY);
		if (triggerKey.equals(PreferenceConstants.PREF_CONTENTASSISTKEYAUTO)) {
			// Null means automatically assist when character typed
			return null;
		}
		if (triggerKey.equals(PreferenceConstants.PREF_CONTENTASSISTKEYCUSTOM)) {
			return getPreferenceStore().getString(PreferenceConstants.PREF_CONTENTASSISTKEYCUSTOMKEY);
		}

		return triggerKey;
	}

	private String getTriggerKeyText() {
		IPreferenceStore store = getPreferenceStore();
		String triggerKey = store.getString(PreferenceConstants.PREF_CONTENTASSISTKEY);
		if (triggerKey.equals(PreferenceConstants.PREF_CONTENTASSISTKEYAUTO)) {
			// It means automatically assist when character typed
			return "alphanumeric key"; //$NON-NLS-1$
		}
		if (triggerKey.equals(PreferenceConstants.PREF_CONTENTASSISTKEYCUSTOM)) {
			return getPreferenceStore().getString(PreferenceConstants.PREF_CONTENTASSISTKEYCUSTOMKEY);
		}

		return triggerKey;
	}

	private int getMarginWidth() {
		IPreferenceStore store = getPreferenceStore();
		return store.getInt(PreferenceConstants.PREF_DECORATOR_MARGINWIDTH);
	}

	/**
	 * Create a ControlDecoration for decorating specified control. Render the
	 * decoration only on the specified Composite or its children.
	 * 
	 * @param control   the control to be decorated.
	 * @param composite The SWT composite within which the decoration should be
	 *                  rendered. The decoration will be clipped to this composite,
	 *                  but it may be rendered on a child of the composite. The
	 *                  decoration will not be visible if the specified composite or
	 *                  its child composites are not visible in the space relative
	 *                  to the control, where the decoration is to be rendered. If
	 *                  this value is null, then the decoration will be rendered on
	 *                  whichever composite (or composites) are located in the
	 *                  specified position.
	 * @return instance of <code>ControlDecoration</code>
	 */
	public ControlDecoration createControlDecoration(Control control, Composite composite) {
		ControlDecoration cd = new ControlDecoration(control, getDecorationLocationBits(), composite);
		cd.setMarginWidth(getMarginWidth());
		return cd;
	}

	/**
	 * @param control
	 * @param contentAdapter
	 * @param values
	 */
	public void installContentProposalAdapter(Control control, IControlContentAdapter contentAdapter, String[] values) {
		IPreferenceStore store = getPreferenceStore();
		boolean propagate = store.getBoolean(PreferenceConstants.PREF_CONTENTASSISTKEY_PROPAGATE);
		KeyStroke keyStroke = null;
		char[] autoActivationCharacters = null;
		int autoActivationDelay = store.getInt(PreferenceConstants.PREF_CONTENTASSISTDELAY);
		String triggerKey = getTriggerKey();
		if (triggerKey == null) {
			keyStroke = null;
		} else {
			keyStroke = getKeyStroke(triggerKey);
		}

		ContentProposalAdapter adapter = new ContentProposalAdapter(control, contentAdapter,
				getContentProposalProvider(values), keyStroke, autoActivationCharacters);
		adapter.setAutoActivationDelay(autoActivationDelay);
		adapter.setPropagateKeys(propagate);
		adapter.setFilterStyle(getContentAssistFilterStyle());
		adapter.setProposalAcceptanceStyle(getContentAssistAcceptance());
	}

	/**
	 * @param keyStroke
	 * @param triggerKey
	 * @return
	 */
	private KeyStroke getKeyStroke(String triggerKey) {
		try {
			return KeyStroke.getInstance(triggerKey);
		} catch (Exception e) // Catch all exceptions to avoid breaking UI.
		{
			return KeyStroke.getInstance(SWT.F10);
		}
	}

	private IContentProposalProvider getContentProposalProvider(final String[] values) {
		return new IContentProposalProvider() {

			public IContentProposal[] getProposals(String contents, int position) {
				IContentProposal[] proposals = new IContentProposal[values.length];
				for (int i = 0; i < values.length; i++) {
					final String user = values[i];
					proposals[i] = new IContentProposal() {

						public String getContent() {
							return user;
						}

						public String getLabel() {
							return user;
						}

						public String getDescription() {
							return null;
						}

						public int getCursorPosition() {
							return user.length();
						}
					};
				}
				return proposals;
			}
		};
	}

	private int getContentAssistAcceptance() {
		IPreferenceStore store = getPreferenceStore();
		String acceptanceStyle = store.getString(PreferenceConstants.PREF_CONTENTASSISTRESULT);
		if (acceptanceStyle.equals(PreferenceConstants.PREF_CONTENTASSISTRESULT_INSERT))
			return ContentProposalAdapter.PROPOSAL_INSERT;
		if (acceptanceStyle.equals(PreferenceConstants.PREF_CONTENTASSISTRESULT_REPLACE))
			return ContentProposalAdapter.PROPOSAL_REPLACE;
		return ContentProposalAdapter.PROPOSAL_IGNORE;
	}

	private int getContentAssistFilterStyle() {
		IPreferenceStore store = getPreferenceStore();
		String acceptanceStyle = store.getString(PreferenceConstants.PREF_CONTENTASSISTFILTER);
		if (acceptanceStyle.equals(PreferenceConstants.PREF_CONTENTASSISTFILTER_CHAR))
			return ContentProposalAdapter.FILTER_CHARACTER;
		if (acceptanceStyle.equals(PreferenceConstants.PREF_CONTENTASSISTFILTER_CUMULATIVE))
			return ContentProposalAdapter.FILTER_CUMULATIVE;
		return ContentProposalAdapter.FILTER_NONE;
	}

	/**
	 * @return
	 */
	private int getDecorationLocationBits() {
		IPreferenceStore store = getPreferenceStore();
		int bits = 0;
		String vert = store.getString(PreferenceConstants.PREF_DECORATOR_VERTICALLOCATION);
		if (vert.equals(PreferenceConstants.PREF_DECORATOR_VERTICALLOCATION_BOTTOM)) {
			bits = SWT.BOTTOM;
		} else if (vert.equals(PreferenceConstants.PREF_DECORATOR_VERTICALLOCATION_CENTER)) {
			bits = SWT.CENTER;
		} else {
			bits = SWT.TOP;
		}

		String horz = store.getString(PreferenceConstants.PREF_DECORATOR_HORIZONTALLOCATION);
		if (horz.equals(PreferenceConstants.PREF_DECORATOR_HORIZONTALLOCATION_RIGHT)) {
			bits |= SWT.RIGHT;
		} else {
			bits |= SWT.LEFT;
		}
		return bits;
	}

	/**
	 * Handle the field modified event, valid the value of field and show correct
	 * decoration.
	 * 
	 * @param assistField
	 */
	public void handleFieldModify(AssistField assistField) {
		// Error indicator supercedes all others
		if (!assistField.isValid()) {
			showError(assistField);
		} else {
			hideError(assistField);
			if (assistField.isWarning()) {
				showWarning(assistField);
			} else {
				hideWarning(assistField);
				if (assistField.hasContentAssist()) {
					showContentAssistDecoration(assistField, true);
				}
			}
		}
	}

	/**
	 * Create a quick fix menu to specified assist field.
	 * 
	 * @param field
	 * @return instance of <code>Menu</code>.
	 */
	public Menu createQuickFixMenu(final AssistField field) {
		Menu newMenu = new Menu(field.control);
		MenuItem item = new MenuItem(newMenu, SWT.PUSH);
		item.setText(Messages.getString("ssDecorationMenuItem")); //$NON-NLS-1$
		item.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				field.quickFix();
			}

			public void widgetDefaultSelected(SelectionEvent event) {

			}
		});
		return newMenu;
	}

	private void showErrorDecoration(AssistField smartField, boolean show) {
		FieldDecoration dec = smartField.getErrorDecoration();
		ControlDecoration cd = smartField.controlDecoration;
		if (show) {
			cd.setImage(dec.getImage());
			cd.setDescriptionText(dec.getDescription());
			cd.setShowOnlyOnFocus(false);
			cd.show();
		} else {
			cd.hide();
		}
	}

	private void showWarningDecoration(AssistField smartField, boolean show) {
		FieldDecoration dec = smartField.getWarningDecoration();
		ControlDecoration cd = smartField.controlDecoration;
		if (show) {
			cd.setImage(dec.getImage());
			cd.setDescriptionText(dec.getDescription());
			cd.setShowOnlyOnFocus(false);
			cd.show();
		} else {
			cd.hide();
		}
	}

	private void showContentAssistDecoration(AssistField smartField, boolean show) {
		FieldDecoration dec = getCueDecoration();
		ControlDecoration cd = smartField.controlDecoration;
		if (show) {
			cd.setImage(dec.getImage());
			cd.setDescriptionText(dec.getDescription());
			cd.setShowOnlyOnFocus(true);
			cd.show();
		} else {
			cd.hide();
		}
	}

	private void showError(AssistField smartField) {
		showErrorDecoration(smartField, true);
	}

	private void hideError(AssistField smartField) {
		showErrorDecoration(smartField, false);
	}

	private void showWarning(AssistField smartField) {
		showWarningDecoration(smartField, true);
	}

	private void hideWarning(AssistField smartField) {
		showWarningDecoration(smartField, false);
	}

	private boolean isDecorationRight() {
		return (getDecorationLocationBits() & SWT.RIGHT) == SWT.RIGHT;
	}

	private boolean isDecorationLeft() {
		return (getDecorationLocationBits() & SWT.LEFT) == SWT.LEFT;
	}

	private FieldDecoration getCueDecoration() {
		// We use our own decoration which is based on the JFace version.
		FieldDecorationRegistry registry = FieldDecorationRegistry.getDefault();
		FieldDecoration dec = registry.getFieldDecoration(DEC_CONTENTASSIST_ID);
		if (dec == null) {
			// Get the standard one. We use its image and our own customized
			// text.
			FieldDecoration standardDecoration = registry
					.getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
			registry.registerFieldDecoration(DEC_CONTENTASSIST_ID,
					Messages.getFormattedString("ssDecoratorContentAssist", //$NON-NLS-1$
							getTriggerKeyText()),
					standardDecoration.getImage());
			dec = registry.getFieldDecoration(DEC_CONTENTASSIST_ID);
		} else {
			dec.setDescription(Messages.getFormattedString("ssDecoratorContentAssist", //$NON-NLS-1$
					getTriggerKeyText()));
		}
		return dec;
	}

	/**
	 * Returns required horizontal margin for displaying decoration.
	 * 
	 * @return
	 */
	private int getHorizontialDecorationMargin() {
		return DECORATION_MARGIN + getMarginWidth();
	}

	/**
	 * Initializes the margin settings on layout.
	 * 
	 * @param gl
	 */
	public void initDecorationMargin(GridLayout gl) {
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = isDecorationLeft() ? getHorizontialDecorationMargin() : 0;
		gl.marginRight = isDecorationRight() ? getHorizontialDecorationMargin() : 0;
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
	}

	/**
	 * Add a required field indicator to related label component.
	 * 
	 * @param assistField
	 * @param label
	 */
	public void addRequiredFieldIndicator(AssistField assistField, Label label) {
		boolean showRequiredFieldLabelIndicator = isShowingRequiredFieldIndicator();
		if (showRequiredFieldLabelIndicator && assistField.isRequiredField()) {
			String text = label.getText();
			// This concatenation could be done by a field assist helper.
			text = text.concat("*"); //$NON-NLS-1$
			label.setText(text);
		}
	}

	/**
	 * Add a required field indicator to related label component.
	 * 
	 * @param assistField
	 * @param label
	 */
	public void addRequiredFieldIndicator(Label label) {
		boolean showRequiredFieldLabelIndicator = isShowingRequiredFieldIndicator();
		if (showRequiredFieldLabelIndicator) {
			String text = label.getText();
			// This concatenation could be done by a field assist helper.
			text = text.concat("*"); //$NON-NLS-1$
			label.setText(text);
		}
	}

	public boolean isShowingRequiredFieldIndicator() {
		return getPreferenceStore().getBoolean(PreferenceConstants.PREF_SHOWREQUIREDFIELDLABELINDICATOR);
	}
}
