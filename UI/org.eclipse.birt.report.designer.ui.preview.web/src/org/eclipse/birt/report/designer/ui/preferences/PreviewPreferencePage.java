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

package org.eclipse.birt.report.designer.ui.preferences;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;

import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.browsers.BrowserManager;
import org.eclipse.birt.report.viewer.utilities.AppContextUtil;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

import com.ibm.icu.util.ULocale;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage </samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */
public class PreviewPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Button alwaysExternal;
	private Button svgFlag;
	private Button masterPageContent;
	private Button customBrowserRadio;
	private Label customBrowserPathLabel;
	private Text customBrowserPath;
	private Button customBrowserBrowse;
	private Combo localeCombo, timeZoneCombo;
	private Button appContextExt;
	private Combo appContextExtCombo;
	private Combo bidiCombo;

	private static final String WBROWSER_PAGE_ID = "org.eclipse.ui.browser.preferencePage";//$NON-NLS-1$
	private static final String PREFERENCE_HELPER_ID = "PreviewPreferencePage";//$NON-NLS-1$

	private static final String BIDI_CHOICE_NAMES[] = { WebViewer.BIDI_ORIENTATION_AUTO, WebViewer.BIDI_ORIENTATION_LTR,
			WebViewer.BIDI_ORIENTATION_RTL };

	private static final String BIDI_CHOICE_DISPLAYNAMES[] = {
			Messages.getString("designer.preview.preference.bidiOrientation.auto"), //$NON-NLS-1$
			Messages.getString("designer.preview.preference.bidiOrientation.ltr"), //$NON-NLS-1$
			Messages.getString("designer.preview.preference.bidiOrientation.rtl"), //$NON-NLS-1$
	};

	public static TreeMap<String, String> timeZoneTable_idKey = null;

	/**
	 * Creates preference page controls on demand.
	 *
	 * @param parent the parent for the preference page
	 */
	@Override
	protected Control createContents(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.PREFERENCE_BIRT_PREVIEW_ID);
		Composite mainComposite = new Composite(parent, SWT.NULL);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		mainComposite.setLayoutData(data);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainComposite.setLayout(layout);

		// Description
		Label description = new Label(mainComposite, SWT.NULL);
		description.setText(Messages.getString("designer.preview.preference.browser.description")); //$NON-NLS-1$

		createSpacer(mainComposite);

		Composite composite = new Composite(mainComposite, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalIndent = 0;
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 0;
		layout.marginWidth = 0;
		composite.setLayoutData(data);
		composite.setLayout(layout);

		Label localeDescription = new Label(composite, SWT.NULL);
		localeDescription.setText(Messages.getString("designer.preview.preference.locale.description")); //$NON-NLS-1$

		localeCombo = new Combo(composite, SWT.DROP_DOWN);
		localeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		localeCombo.setVisibleItemCount(30);
		assert WebViewer.LOCALE_TABLE != null;
		String[] localeDisplayNames = new String[WebViewer.LOCALE_TABLE.size()];
		WebViewer.LOCALE_TABLE.keySet().toArray(localeDisplayNames);
		localeCombo.setItems(localeDisplayNames);
		String defaultLocale = ViewerPlugin.getDefault().getPluginPreferences().getString(WebViewer.USER_LOCALE);
		if (defaultLocale == null || defaultLocale.trim().length() <= 0) {
			assert ULocale.getDefault() != null;
			defaultLocale = ULocale.getDefault().getDisplayName();
		} else if (WebViewer.LOCALE_TABLE.containsValue(defaultLocale)) {
			Iterator iter = WebViewer.LOCALE_TABLE.entrySet().iterator();
			while (iter.hasNext()) {
				Entry entry = (Entry) iter.next();
				if (defaultLocale.equals(entry.getValue())) {
					defaultLocale = (String) entry.getKey();
					break;
				}
			}
		}
		localeCombo.setText(defaultLocale);

		createTimeZoneChoice(composite);

		createBIDIChoice(composite);

		createSpacer(mainComposite);

		// Enable svg or not.
		svgFlag = new Button(mainComposite, SWT.CHECK);
		svgFlag.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
		svgFlag.setText(Messages.getString("designer.preview.preference.browser.svg")); //$NON-NLS-1$
		svgFlag.setSelection(
				Platform.getPreferencesService().getBoolean(ViewerPlugin.PLUGIN_ID, WebViewer.SVG_FLAG, true, null));

		// Show mastet page or not.
		masterPageContent = new Button(mainComposite, SWT.CHECK);
		masterPageContent.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
		masterPageContent.setText(Messages.getString("designer.preview.preference.masterpagecontent")); //$NON-NLS-1$
		masterPageContent.setSelection(
				ViewerPlugin.getDefault().getPluginPreferences().getBoolean(WebViewer.MASTER_PAGE_CONTENT));
		if (needAddItem()) {
			String[] appExtNames = (String[]) AppContextUtil.getAppContextExtensionNames().toArray(new String[0]);
			String appKey = ViewerPlugin.getDefault().getPluginPreferences()
					.getString(WebViewer.APPCONTEXT_EXTENSION_KEY);

			Composite appContextComposite = new Composite(mainComposite, SWT.NONE);
			appContextComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			gridLayout.marginHeight = gridLayout.marginWidth = 0;
			appContextComposite.setLayout(gridLayout);

			appContextExt = new Button(appContextComposite, SWT.CHECK);

			appContextExt.setSelection(appKey != null && appKey.length() > 0);
			appContextExt.setEnabled(appExtNames.length != 0);
			appContextExt.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					appContextExtCombo.setEnabled(appContextExt.getSelection());
					if (appContextExt.getSelection()) {
						if (appContextExtCombo.getSelectionIndex() != -1) {
							ViewerPlugin.getDefault().getPluginPreferences()
									.setValue(WebViewer.APPCONTEXT_EXTENSION_KEY, appContextExtCombo.getText());
						}
					} else {
						ViewerPlugin.getDefault().getPluginPreferences().setValue(WebViewer.APPCONTEXT_EXTENSION_KEY,
								"");//$NON-NLS-1$
					}
				}

			});

			Label label = new Label(appContextComposite, SWT.NONE);
			label.setText(Messages.getString("designer.preview.preference.appcontextkey"));//$NON-NLS-1$
			label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
			new Label(appContextComposite, SWT.NONE);
			appContextExtCombo = new Combo(appContextComposite, SWT.READ_ONLY | SWT.SINGLE);
			appContextExtCombo.setVisibleItemCount(30);
			appContextExtCombo.setItems(appExtNames);
			if (appContextExt.getSelection()) {
				appContextExtCombo.setEnabled(appContextExt.isEnabled());
				appContextExtCombo.setText(appKey);
			} else {
				appContextExtCombo.setEnabled(false);
			}
			GridData extGd = new GridData();
			int width = appContextExtCombo.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			extGd.widthHint = width < 200 ? 200 : width;
			appContextExtCombo.setLayoutData(extGd);
			appContextExtCombo.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					ViewerPlugin.getDefault().getPluginPreferences().setValue(WebViewer.APPCONTEXT_EXTENSION_KEY,
							appContextExtCombo.getText());
				}

			});
		}

		createSpacer(mainComposite);

		// Always use external browsers
		alwaysExternal = new Button(mainComposite, SWT.CHECK);
		alwaysExternal.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
		alwaysExternal.setText(Messages.getString("designer.preview.preference.browser.useExternal")); //$NON-NLS-1$
		alwaysExternal.setSelection(ViewerPlugin.getDefault().getPluginPreferences()
				.getBoolean(BrowserManager.ALWAYS_EXTERNAL_BROWSER_KEY));
		if (!BrowserManager.getInstance().isEmbeddedBrowserPresent()) {
			alwaysExternal.setSelection(true);
			alwaysExternal.setEnabled(false);
		}

		createLinkArea(mainComposite);

		createSpacer(mainComposite);

		// // Current external browser adapters
		// Label tableDescription = new Label( mainComposite, SWT.NULL );
		// tableDescription.setText( Messages.getString(
		// "designer.preview.preference.browser.currentBrowsers" ) ); //$NON-NLS-1$
		//
		// // Grid for browser adapters
		// Color bgColor = parent.getDisplay( )
		// .getSystemColor( SWT.COLOR_LIST_BACKGROUND );
		// Color fgColor = parent.getDisplay( )
		// .getSystemColor( SWT.COLOR_LIST_FOREGROUND );
		// final ScrolledComposite externalBrowsersScrollable = new
		// ScrolledComposite( mainComposite,
		// SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL );
		// GridData gd = new GridData( GridData.FILL_BOTH );
		// gd.heightHint = convertHeightInCharsToPixels( 2 );
		// externalBrowsersScrollable.setLayoutData( gd );
		// externalBrowsersScrollable.setBackground( bgColor );
		// externalBrowsersScrollable.setForeground( fgColor );
		//
		// Composite externalBrowsersComposite = new Composite(
		// externalBrowsersScrollable,
		// SWT.NONE );
		// externalBrowsersScrollable.setContent( externalBrowsersComposite );
		// GridLayout layout2 = new GridLayout( );
		// externalBrowsersComposite.setLayout( layout2 );
		// externalBrowsersComposite.setBackground( bgColor );
		// externalBrowsersComposite.setForeground( fgColor );
		//
		// // List of browser adapters
		// BrowserDescriptor[] descriptors = BrowserManager.getInstance( )
		// .getBrowserDescriptors( );
		// externalBrowsers = new Button[descriptors.length];
		//
		// for ( int i = 0; i < descriptors.length; i++ )
		// {
		// Button radio = new Button( externalBrowsersComposite, SWT.RADIO );
		// org.eclipse.jface.dialogs.Dialog.applyDialogFont( radio );
		// radio.setBackground( bgColor );
		// radio.setForeground( fgColor );
		// radio.setText( descriptors[i].getLabel( ) );
		//
		// if ( BrowserManager.getInstance( )
		// .getCurrentBrowserID( )
		// .equals( descriptors[i].getID( ) ) )
		// {
		// radio.setSelection( true );
		// }
		// else
		// {
		// radio.setSelection( false );
		// }
		//
		// radio.setData( descriptors[i] );
		// externalBrowsers[i] = radio;
		//
		// if ( BrowserManager.BROWSER_ID_CUSTOM.equals( descriptors[i].getID( )
		// ) )
		// {
		// customBrowserRadio = radio;
		// radio.addSelectionListener( new SelectionListener( ) {
		//
		// public void widgetSelected( SelectionEvent selEvent )
		// {
		// setCustomBrowserPathEnabled( );
		// }
		//
		// public void widgetDefaultSelected( SelectionEvent selEvent )
		// {
		// widgetSelected( selEvent );
		// }
		// } );
		// }
		// }
		//
		// externalBrowsersComposite.setSize(
		// externalBrowsersComposite.computeSize( SWT.DEFAULT,
		// SWT.DEFAULT ) );
		//
		// Custom browser
		// createCustomBrowserPathPart( mainComposite );
		org.eclipse.jface.dialogs.Dialog.applyDialogFont(mainComposite);

		createSpacer(mainComposite);

		return mainComposite;
	}

	private boolean needAddItem() {
		IDialogHelper helper = getPreviewPrefrence();
		if (helper == null) {
			return true;
		}
		return false;
	}

	private IDialogHelper getPreviewPrefrence() {
		Object[] helperProviders = ElementAdapterManager.getAdapters(this, IDialogHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					final IDialogHelper helper = helperProvider.createHelper(this, PREFERENCE_HELPER_ID);
					if (helper != null) {
						return helper;
					}
				}
			}
		}
		return null;
	}

	private void createLinkArea(Composite parent) {
		IPreferenceNode node = getPreferenceNode(WBROWSER_PAGE_ID);
		if (node != null) {
			PreferenceLinkArea linkArea = new PreferenceLinkArea(parent, SWT.WRAP, WBROWSER_PAGE_ID,
					Messages.getString("designer.preview.preference.browser.extbrowser.link"), //$NON-NLS-1$
					(IWorkbenchPreferenceContainer) getContainer(), null);
			GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
			linkArea.getControl().setLayoutData(data);
		}
	}

	private IPreferenceNode getPreferenceNode(String pageId) {
		Iterator iterator = PlatformUI.getWorkbench().getPreferenceManager().getElements(PreferenceManager.PRE_ORDER)
				.iterator();
		while (iterator.hasNext()) {
			IPreferenceNode next = (IPreferenceNode) iterator.next();
			if (next.getId().equals(pageId)) {
				return next;
			}
		}
		return null;
	}

	/**
	 * Performs special processing when this page's Defaults button has been
	 * pressed.
	 * <p>
	 * This is a framework hook method for sublcasses to do special things when the
	 * Defaults button has been pressed. Subclasses may override, but should call
	 * <code>super.performDefaults</code>.
	 * </p>
	 */
	@Override
	protected void performDefaults() {
		// String defaultBrowserID = BrowserManager.getInstance( )
		// .getDefaultBrowserID( );
		//
		// for ( int i = 0; i < externalBrowsers.length; i++ )
		// {
		// BrowserDescriptor descriptor = (BrowserDescriptor)
		// externalBrowsers[i].getData( );
		// externalBrowsers[i].setSelection( descriptor.getID( ) ==
		// defaultBrowserID );
		// }
		//
		// customBrowserPath.setText( ViewerPlugin.getDefault( )
		// .getPluginPreferences( )
		// .getDefaultString( CustomBrowser.CUSTOM_BROWSER_PATH_KEY ) );
		// setCustomBrowserPathEnabled( );

		if (svgFlag != null) {
			svgFlag.setSelection(
					ViewerPlugin.getDefault().getPluginPreferences().getDefaultBoolean(WebViewer.SVG_FLAG));
		}

		if (bidiCombo != null) {
			String defualtBidi = ViewerPlugin.getDefault().getPluginPreferences()
					.getDefaultString(WebViewer.BIDI_ORIENTATION);
			int index = Arrays.asList(BIDI_CHOICE_NAMES).indexOf(defualtBidi);
			if (index < 0) {
				index = 0;
			}
			bidiCombo.select(index);
		}

		if (masterPageContent != null) {
			masterPageContent.setSelection(
					ViewerPlugin.getDefault().getPluginPreferences().getDefaultBoolean(WebViewer.MASTER_PAGE_CONTENT));
		}

		if (alwaysExternal != null) {
			alwaysExternal.setSelection(ViewerPlugin.getDefault().getPluginPreferences()
					.getDefaultBoolean(BrowserManager.ALWAYS_EXTERNAL_BROWSER_KEY));
		}

		if (localeCombo != null) {
			ULocale defaultLocale = ULocale.getDefault();
			assert defaultLocale != null;
			localeCombo.setText(defaultLocale.getDisplayName());
		}

		if (timeZoneCombo != null) {
			String displayName = TimeZone.getDefault().getDisplayName();
			if (displayName == null) {
				displayName = "";
			}
			timeZoneCombo.setText(displayName);
		}

		super.performDefaults();
	}

	/**
	 * @see IPreferencePage
	 */
	@Override
	public boolean performOk() {
		Preferences pref = ViewerPlugin.getDefault().getPluginPreferences();

		// for ( int i = 0; i < externalBrowsers.length; i++ )
		// {
		// if ( externalBrowsers[i].getSelection( ) )
		// {
		// // set new current browser
		// String browserID = ( (BrowserDescriptor) externalBrowsers[i].getData(
		// ) ).getID( );
		// BrowserManager.getInstance( ).setCurrentBrowserID( browserID );
		// // save id in help preferences
		// pref.setValue( BrowserManager.DEFAULT_BROWSER_ID_KEY, browserID );
		// break;
		// }
		// }
		//
		// customBrowserPath.getText( );
		// pref.setValue( CustomBrowser.CUSTOM_BROWSER_PATH_KEY,
		// customBrowserPath.getText( ) );

		if (svgFlag != null) {
			pref.setValue(WebViewer.SVG_FLAG, svgFlag.getSelection());
		}

		if (masterPageContent != null) {
			pref.setValue(WebViewer.MASTER_PAGE_CONTENT, masterPageContent.getSelection());
		}

		if (alwaysExternal != null) {
			pref.setValue(BrowserManager.ALWAYS_EXTERNAL_BROWSER_KEY, alwaysExternal.getSelection());
			BrowserManager.getInstance().setAlwaysUseExternal(alwaysExternal.getSelection());
		}

		if (timeZoneCombo != null) {
			String timeZoneId = ViewerPlugin.getTimeZoneTable_disKey().get(timeZoneCombo.getText());
			if (timeZoneId == null || timeZoneId.trim().length() <= 0) {
				timeZoneId = TimeZone.getDefault().getID();
			}
			pref.setValue(WebViewer.USER_TIME_ZONE, timeZoneId);
		}

		if (localeCombo != null) {
			if (WebViewer.LOCALE_TABLE.containsKey(localeCombo.getText())) {
				pref.setValue(WebViewer.USER_LOCALE, WebViewer.LOCALE_TABLE.get(localeCombo.getText()));
			} else {
				pref.setValue(WebViewer.USER_LOCALE, localeCombo.getText());
			}
		}

		if (bidiCombo != null) {
			int selection = bidiCombo.getSelectionIndex();
			selection = selection < 0 ? 0 : selection;
			pref.setValue(WebViewer.BIDI_ORIENTATION, BIDI_CHOICE_NAMES[selection]);
		}
		ViewerPlugin.getDefault().savePluginPreferences();

		return true;
	}

	/**
	 * Toggle custom browser enabled or not
	 */
	private void setCustomBrowserPathEnabled() {
		boolean enabled = customBrowserRadio.getSelection();
		customBrowserPathLabel.setEnabled(enabled);
		customBrowserPath.setEnabled(enabled);
		customBrowserBrowse.setEnabled(enabled);
	}

	/**
	 * Creates a horizontal spacer line that fills the width of its container.
	 *
	 * @param parent the parent control
	 */
	private void createSpacer(Composite parent) {
		Label spacer = new Label(parent, SWT.NONE);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		spacer.setLayoutData(data);
	}

	protected Composite createTimeZoneChoice(Composite parent) {
		Label timeZoneDescription = new Label(parent, SWT.NULL);
		timeZoneDescription.setText(Messages.getString("designer.preview.preference.timezone.description")); //$NON-NLS-1$

		timeZoneCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		timeZoneCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		timeZoneCombo.setVisibleItemCount(30);
		assert ViewerPlugin.getTimeZoneTable_disKey() != null;
		String[] timeZoneDisplayNames = new String[ViewerPlugin.getTimeZoneTable_disKey().size()];
		ViewerPlugin.getTimeZoneTable_disKey().keySet().toArray(timeZoneDisplayNames);
		timeZoneCombo.setItems(timeZoneDisplayNames);
		String defaultTimeZone = ViewerPlugin.getDefault().getPluginPreferences().getString(WebViewer.USER_TIME_ZONE);
		if (defaultTimeZone == null || defaultTimeZone.trim().length() <= 0) {
			defaultTimeZone = TimeZone.getDefault().getID();
		}
		TimeZone timeZone = TimeZone.getTimeZone(defaultTimeZone);
		timeZoneCombo.setText(timeZone.getDisplayName());

		return parent;
	}

	protected Composite createBIDIChoice(Composite parent) {
		// Composite composite = new Composite( parent, SWT.NONE );
		// composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		// GridLayout layout = new GridLayout( );
		// layout.numColumns = 2;
		// layout.marginWidth = 0;
		// layout.marginHeight = 0;
		// composite.setLayout( layout );

		Label lb = new Label(parent, SWT.NONE);
		lb.setText(Messages.getString("designer.preview.preference.bidiOrientation.label"));
		bidiCombo = new Combo(parent, SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 100;
		bidiCombo.setLayoutData(gd);
		bidiCombo.setVisibleItemCount(30);
		bidiCombo.setItems(BIDI_CHOICE_DISPLAYNAMES);

		String bidiValue = ViewerPlugin.getDefault().getPluginPreferences().getString(WebViewer.BIDI_ORIENTATION);
		int index = Arrays.asList(BIDI_CHOICE_NAMES).indexOf(bidiValue);
		index = index < 0 ? 0 : index;
		bidiCombo.select(index);
		return parent;
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
