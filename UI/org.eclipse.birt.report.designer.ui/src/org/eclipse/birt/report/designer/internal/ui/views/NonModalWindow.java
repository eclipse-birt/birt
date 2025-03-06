package org.eclipse.birt.report.designer.internal.ui.views;

import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogBlockedHandler;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @since 3.3
 *
 */
public class NonModalWindow extends Window {
	/**
	 * Image registry key for error image (value <code>"dialog_error_image"</code>).
	 *
	 * @deprecated use
	 *             org.eclipse.swt.widgets.Display.getSystemImage(SWT.ICON_ERROR)
	 */
	@Deprecated(forRemoval = true, since = "2023-12")
	public static final String DLG_IMG_ERROR = "dialog_error_image"; //$NON-NLS-1$

	/**
	 * Image registry key for info image (value <code>"dialog_info_image"</code>).
	 *
	 * @deprecated use
	 *             org.eclipse.swt.widgets.Display.getSystemImage(SWT.ICON_INFORMATION)
	 */
	@Deprecated(forRemoval = true, since = "2023-12")
	public static final String DLG_IMG_INFO = "dialog_info_imageg"; //$NON-NLS-1$

	/**
	 * Image registry key for question image (value
	 * <code>"dialog_question_image"</code>).
	 *
	 * @deprecated org.eclipse.swt.widgets.Display.getSystemImage(SWT.ICON_QUESTION)
	 */
	@Deprecated(forRemoval = true, since = "2023-12")
	public static final String DLG_IMG_QUESTION = "dialog_question_image"; //$NON-NLS-1$

	/**
	 * Image registry key for warning image (value
	 * <code>"dialog_warning_image"</code>).
	 *
	 * @deprecated use
	 *             org.eclipse.swt.widgets.Display.getSystemImage(SWT.ICON_WARNING)
	 */
	@Deprecated
	public static final String DLG_IMG_WARNING = "dialog_warning_image"; //$NON-NLS-1$

	/**
	 * Image registry key for info message image (value
	 * <code>"dialog_messasge_info_image"</code>).
	 *
	 * @since 2.0
	 */
	public static final String DLG_IMG_MESSAGE_INFO = "dialog_messasge_info_image"; //$NON-NLS-1$

	/**
	 * Image registry key for info message image (value
	 * <code>"dialog_messasge_warning_image"</code>).
	 *
	 * @since 2.0
	 */
	public static final String DLG_IMG_MESSAGE_WARNING = "dialog_messasge_warning_image"; //$NON-NLS-1$

	/**
	 * Image registry key for info message image (value
	 * <code>"dialog_message_error_image"</code>).
	 *
	 * @since 2.0
	 */
	public static final String DLG_IMG_MESSAGE_ERROR = "dialog_message_error_image"; //$NON-NLS-1$

	/**
	 * Image registry key for help image (value <code>"dialog_help_image"</code>).
	 *
	 * @since 3.2
	 */
	public static final String DLG_IMG_HELP = "dialog_help_image"; //$NON-NLS-1$

	/**
	 * The ellipsis is the string that is used to represent shortened text.
	 *
	 * @since 3.0
	 */
	public static final String ELLIPSIS = "..."; //$NON-NLS-1$

	/**
	 * The dialog settings key name for stored dialog x location.
	 *
	 * @since 3.2
	 */
	private static final String DIALOG_ORIGIN_X = "DIALOG_X_ORIGIN"; //$NON-NLS-1$

	/**
	 * The dialog settings key name for stored dialog y location.
	 *
	 * @since 3.2
	 */
	private static final String DIALOG_ORIGIN_Y = "DIALOG_Y_ORIGIN"; //$NON-NLS-1$

	/**
	 * The dialog settings key name for stored dialog width.
	 *
	 * @since 3.2
	 */
	private static final String DIALOG_WIDTH = "DIALOG_WIDTH"; //$NON-NLS-1$

	/**
	 * The dialog settings key name for stored dialog height.
	 *
	 * @since 3.2
	 */
	private static final String DIALOG_HEIGHT = "DIALOG_HEIGHT"; //$NON-NLS-1$

	/**
	 * The dialog settings key name for the font used when the dialog height and
	 * width was stored.
	 *
	 * @since 3.2
	 */
	private static final String DIALOG_FONT_DATA = "DIALOG_FONT_NAME"; //$NON-NLS-1$

	/**
	 * A value that can be used for stored dialog width or height that indicates
	 * that the default bounds should be used.
	 *
	 * @since 3.2
	 */
	public static final int DIALOG_DEFAULT_BOUNDS = -1;

	/**
	 * Constants that can be used for specifying the strategy for persisting dialog
	 * bounds. These constants represent bit masks that can be used together.
	 *
	 * @since 3.2
	 */

	/**
	 * Persist the last location of the dialog.
	 *
	 * @since 3.2
	 */
	public static final int DIALOG_PERSISTLOCATION = 0x0001;
	/**
	 * Persist the last known size of the dialog.
	 *
	 * @since 3.2
	 */
	public static final int DIALOG_PERSISTSIZE = 0x0002;

	/**
	 * The dialog area; <code>null</code> until dialog is layed out.
	 */
	protected Control dialogArea;

	/**
	 * The button bar; <code>null</code> until dialog is layed out.
	 */
	public Control buttonBar;

	/**
	 * Collection of buttons created by the <code>createButton</code> method.
	 */
	private HashMap<Integer, Button> buttons = new HashMap<>();

	/**
	 * Font metrics to use for determining pixel sizes.
	 */
	private FontMetrics fontMetrics;

	/**
	 * Number of horizontal dialog units per character, value <code>4</code>.
	 */
	private static final int HORIZONTAL_DIALOG_UNIT_PER_CHAR = 4;

	/**
	 * Number of vertical dialog units per character, value <code>8</code>.
	 */
	private static final int VERTICAL_DIALOG_UNITS_PER_CHAR = 8;

	/**
	 * Returns the number of pixels corresponding to the height of the given number
	 * of characters.
	 * <p>
	 * The required <code>FontMetrics</code> parameter may be created in the
	 * following way:
	 * </p>
	 *
	 * <pre>
	 * GC gc = new GC(control);
	 * gc.setFont(control.getFont());
	 * fontMetrics = gc.getFontMetrics();
	 * gc.dispose();
	 * </pre>
	 *
	 * @param fontMetrics used in performing the conversion
	 * @param chars       the number of characters
	 * @return the number of pixels
	 * @since 2.0
	 */
	public static int convertHeightInCharsToPixels(FontMetrics fontMetrics, int chars) {
		return fontMetrics.getHeight() * chars;
	}

	/**
	 * Returns the number of pixels corresponding to the given number of horizontal
	 * dialog units.
	 * <p>
	 * The required <code>FontMetrics</code> parameter may be created in the
	 * following way:
	 * </p>
	 *
	 * <pre>
	 * GC gc = new GC(control);
	 * gc.setFont(control.getFont());
	 * fontMetrics = gc.getFontMetrics();
	 * gc.dispose();
	 * </pre>
	 *
	 * @param fontMetrics used in performing the conversion
	 * @param dlus        the number of horizontal dialog units
	 * @return the number of pixels
	 * @since 2.0
	 */
	public static int convertHorizontalDLUsToPixels(FontMetrics fontMetrics, int dlus) {
		// round to the nearest pixel
		return (int) ((fontMetrics.getAverageCharacterWidth() * dlus + HORIZONTAL_DIALOG_UNIT_PER_CHAR / 2)
				/ HORIZONTAL_DIALOG_UNIT_PER_CHAR);
	}

	/**
	 * Returns the number of pixels corresponding to the given number of vertical
	 * dialog units.
	 * <p>
	 * The required <code>FontMetrics</code> parameter may be created in the
	 * following way:
	 * </p>
	 *
	 * <pre>
	 * GC gc = new GC(control);
	 * gc.setFont(control.getFont());
	 * fontMetrics = gc.getFontMetrics();
	 * gc.dispose();
	 * </pre>
	 *
	 * @param fontMetrics used in performing the conversion
	 * @param dlus        the number of vertical dialog units
	 * @return the number of pixels
	 * @since 2.0
	 */
	public static int convertVerticalDLUsToPixels(FontMetrics fontMetrics, int dlus) {
		// round to the nearest pixel
		return (fontMetrics.getHeight() * dlus + VERTICAL_DIALOG_UNITS_PER_CHAR / 2) / VERTICAL_DIALOG_UNITS_PER_CHAR;
	}

	/**
	 * Returns the number of pixels corresponding to the width of the given number
	 * of characters.
	 * <p>
	 * The required <code>FontMetrics</code> parameter may be created in the
	 * following way:
	 * </p>
	 *
	 * <pre>
	 * GC gc = new GC(control);
	 * gc.setFont(control.getFont());
	 * fontMetrics = gc.getFontMetrics();
	 * gc.dispose();
	 * </pre>
	 *
	 * @param fontMetrics used in performing the conversion
	 * @param chars       the number of characters
	 * @return the number of pixels
	 * @since 2.0
	 */
	public static int convertWidthInCharsToPixels(FontMetrics fontMetrics, int chars) {
		return (int) (fontMetrics.getAverageCharacterWidth() * chars);
	}

	/**
	 * Try to shorten the given text <code>textValue</code> so that its width in
	 * pixels does not exceed the width of the given control. Overrides characters
	 * in the center of the original string with an ellipsis ("...") if necessary.
	 * If a <code>null</code> value is given, <code>null</code> is returned.
	 * <p>
	 * <b>Note:</b> if the text cannot be shortened because the width of control is
	 * too low the full original string is returned.
	 * </p>
	 *
	 * @param textValue the original string or <code>null</code>
	 * @param control   the control the string will be displayed on
	 * @return the string to display, or <code>null</code> if null was passed in
	 *
	 * @since 3.0
	 */
	public static String shortenText(String textValue, Control control) {
		if (textValue == null) {
			return null;
		}
		GC gc = new GC(control);
		int maxWidth = control.getBounds().width - 5;
		int maxExtent = gc.textExtent(textValue).x;
		if (maxExtent < maxWidth) {
			gc.dispose();
			return textValue;
		}
		int length = textValue.length();
		int charsToClip = Math.round(0.95f * length * (1 - ((float) maxWidth / maxExtent)));
		int pivot = length / 2;
		int start = pivot - (charsToClip / 2);
		int end = pivot + (charsToClip / 2) + 1;
		while (start >= 0 && end < length) {
			String s1 = textValue.substring(0, start);
			String s2 = textValue.substring(end, length);
			String s = s1 + ELLIPSIS + s2;
			int l = gc.textExtent(s).x;
			if (l < maxWidth) {
				gc.dispose();
				return s;
			}
			start--;
			end++;
		}
		gc.dispose();
		return textValue;
	}

	/**
	 * Create a default instance of the blocked handler which does not do anything.
	 */
	public static IDialogBlockedHandler blockedHandler = new IDialogBlockedHandler() {

		@Override
		public void clearBlocked() {
			// No default behavior
		}

		@Override
		public void showBlocked(IProgressMonitor blocking, IStatus blockingStatus, String blockedName) {
			// No default behavior
		}

		@Override
		public void showBlocked(Shell parentShell, IProgressMonitor blocking, IStatus blockingStatus,
				String blockedName) {
			// No default behavior
		}
	};

	/**
	 * Creates a dialog instance. Note that the window will have no visual
	 * representation (no widgets) until it is told to open. By default,
	 * <code>open</code> blocks for dialogs.
	 *
	 * @param parentShell the parent shell, or <code>null</code> to create a
	 *                    top-level shell
	 */
	protected NonModalWindow(Shell parentShell) {
		this(new SameShellProvider(parentShell));
		if (parentShell == null && Policy.DEBUG_DIALOG_NO_PARENT) {
			Policy.getLog()
					.log(new Status(IStatus.INFO, Policy.JFACE, IStatus.INFO,
							this.getClass() + " created with no shell", //$NON-NLS-1$
							new Exception()));
		}
	}

	/**
	 * Creates a dialog with the given parent.
	 *
	 * @param parentShell object that returns the current parent shell
	 *
	 * @since 3.1
	 */
	protected NonModalWindow(IShellProvider parentShell) {
		super(parentShell);
		if (isResizable()) {
			setShellStyle(SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE | getDefaultOrientation());
		} else {
			setShellStyle(SWT.DIALOG_TRIM | getDefaultOrientation());
		}
		setBlockOnOpen(false);
	}

	/**
	 * Notifies that this dialog's button with the given id has been pressed.
	 * <p>
	 * The <code>NonModalWindow</code> implementation of this framework method calls
	 * <code>okPressed</code> if the ok button is the pressed, and
	 * <code>cancelPressed</code> if the cancel button is the pressed. All other
	 * button presses are ignored. Subclasses may override to handle other buttons,
	 * but should call <code>super.buttonPressed</code> if the default handling of
	 * the ok and cancel buttons is desired.
	 * </p>
	 *
	 * @param buttonId the id of the button that was pressed (see
	 *                 <code>IDialogConstants.*_ID</code> constants)
	 */
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			okPressed();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			cancelPressed();
		}
	}

	/**
	 * Notifies that the cancel button of this dialog has been pressed.
	 * <p>
	 * The <code>NonModalWindow</code> implementation of this framework method sets
	 * this dialog's return code to <code>Window.CANCEL</code> and closes the
	 * dialog. Subclasses may override if desired.
	 * </p>
	 */
	protected void cancelPressed() {
		setReturnCode(CANCEL);
		close();
	}

	/**
	 * Returns the number of pixels corresponding to the height of the given number
	 * of characters.
	 * <p>
	 * This method may only be called after <code>initializeDialogUnits</code> has
	 * been called.
	 * </p>
	 * <p>
	 * Clients may call this framework method, but should not override it.
	 * </p>
	 *
	 * @param chars the number of characters
	 * @return the number of pixels
	 */
	protected int convertHeightInCharsToPixels(int chars) {
		// test for failure to initialize for backward compatibility
		if (fontMetrics == null) {
			return 0;
		}
		return convertHeightInCharsToPixels(fontMetrics, chars);
	}

	/**
	 * Returns the number of pixels corresponding to the given number of horizontal
	 * dialog units.
	 * <p>
	 * This method may only be called after <code>initializeDialogUnits</code> has
	 * been called.
	 * </p>
	 * <p>
	 * Clients may call this framework method, but should not override it.
	 * </p>
	 *
	 * @param dlus the number of horizontal dialog units
	 * @return the number of pixels
	 */
	protected int convertHorizontalDLUsToPixels(int dlus) {
		// test for failure to initialize for backward compatibility
		if (fontMetrics == null) {
			return 0;
		}
		return convertHorizontalDLUsToPixels(fontMetrics, dlus);
	}

	/**
	 * Returns the number of pixels corresponding to the given number of vertical
	 * dialog units.
	 * <p>
	 * This method may only be called after <code>initializeDialogUnits</code> has
	 * been called.
	 * </p>
	 * <p>
	 * Clients may call this framework method, but should not override it.
	 * </p>
	 *
	 * @param dlus the number of vertical dialog units
	 * @return the number of pixels
	 */
	protected int convertVerticalDLUsToPixels(int dlus) {
		// test for failure to initialize for backward compatibility
		if (fontMetrics == null) {
			return 0;
		}
		return convertVerticalDLUsToPixels(fontMetrics, dlus);
	}

	/**
	 * Returns the number of pixels corresponding to the width of the given number
	 * of characters.
	 * <p>
	 * This method may only be called after <code>initializeDialogUnits</code> has
	 * been called.
	 * </p>
	 * <p>
	 * Clients may call this framework method, but should not override it.
	 * </p>
	 *
	 * @param chars the number of characters
	 * @return the number of pixels
	 */
	protected int convertWidthInCharsToPixels(int chars) {
		// test for failure to initialize for backward compatibility
		if (fontMetrics == null) {
			return 0;
		}
		return convertWidthInCharsToPixels(fontMetrics, chars);
	}

	/**
	 * Creates a new button with the given id.
	 * <p>
	 * The <code>NonModalWindow</code> implementation of this framework method
	 * creates a standard push button, registers it for selection events including
	 * button presses, and registers default buttons with its shell. The button id
	 * is stored as the button's client data. If the button id is
	 * <code>IDialogConstants.CANCEL_ID</code>, the new button will be accessible
	 * from <code>getCancelButton()</code>. If the button id is
	 * <code>IDialogConstants.OK_ID</code>, the new button will be accesible from
	 * <code>getOKButton()</code>. Note that the parent's layout is assumed to be a
	 * <code>GridLayout</code> and the number of columns in this layout is
	 * incremented. Subclasses may override.
	 * </p>
	 * <p>
	 * Note: The common button order is: <b>{other buttons}</b>, <b>OK</b>,
	 * <b>Cancel</b>. On some platforms, {@link #initializeBounds()} will move the
	 * default button to the right.
	 * </p>
	 *
	 * @param parent        the parent composite
	 * @param id            the id of the button (see
	 *                      <code>IDialogConstants.*_ID</code> constants for
	 *                      standard dialog button ids)
	 * @param label         the label from the button
	 * @param defaultButton <code>true</code> if the button is to be the default
	 *                      button, and <code>false</code> otherwise
	 *
	 * @return the new button
	 *
	 * @see #getCancelButton
	 * @see #getOKButton()
	 */
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = WidgetFactory.button(SWT.PUSH).text(label).font(JFaceResources.getDialogFont())
				.data(Integer.valueOf(id))
				.onSelect(event -> buttonPressed(((Integer) event.widget.getData()).intValue())).create(parent);
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		buttons.put(Integer.valueOf(id), button);
		setButtonLayoutData(button);
		return button;
	}

	/**
	 * Creates and returns the contents of this dialog's button bar.
	 * <p>
	 * The <code>NonModalWindow</code> implementation of this framework method lays
	 * out a button bar and calls the <code>createButtonsForButtonBar</code>
	 * framework method to populate it. Subclasses may override.
	 * </p>
	 * <p>
	 * The returned control's layout data must be an instance of
	 * <code>GridData</code>.
	 * </p>
	 *
	 * @param parent the parent composite to contain the button bar
	 * @return the button bar control
	 */
	protected Control createButtonBar(Composite parent) {
		// create a layout with spacing and margins appropriate for the font
		// size.
		GridLayout layout = new GridLayout();
		layout.numColumns = 0; // this is incremented by createButton
		layout.makeColumnsEqualWidth = true;
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER);

		Composite composite = WidgetFactory.composite(SWT.NONE).layout(layout).layoutData(data).font(parent.getFont())
				.create(parent);

		// Add the buttons to the button bar.
		createButtonsForButtonBar(composite);
		return composite;
	}

	/**
	 * Adds buttons to this dialog's button bar.
	 * <p>
	 * The <code>NonModalWindow</code> implementation of this framework method adds
	 * standard ok and cancel buttons using the <code>createButton</code> framework
	 * method. These standard buttons will be accessible from
	 * <code>getCancelButton</code>, and <code>getOKButton</code>. Subclasses may
	 * override.
	 * </p>
	 * <p>
	 * Note: The common button order is: <b>{other buttons}</b>, <b>OK</b>,
	 * <b>Cancel</b>. On some platforms, {@link #initializeBounds()} will move the
	 * default button to the right.
	 * </p>
	 *
	 * @param parent the button bar composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The implementation in {@link #NonModalWindow} also moves the
	 * {@link Shell#getDefaultButton() default button} in the
	 * {@link #createButtonBar(Composite) button bar} to the right if that's
	 * required by the {@link Display#getDismissalAlignment() platform convention}.
	 * </p>
	 */
	@Override
	protected void initializeBounds() {
		// UI guidelines:
		// https://developer.gnome.org/hig/stable/dialogs.html.en#primary-buttons
		// https://developer.apple.com/library/mac/documentation/UserExperience/Conceptual/OSXHIGuidelines/WindowDialogs.html#//apple_ref/doc/uid/20000957-CH43-SW5
		// https://msdn.microsoft.com/en-us/library/windows/desktop/dn742499(v=vs.85).aspx#win_dialog_box_image25
		Shell shell = getShell();
		if (shell != null) {
			if (shell.getDisplay().getDismissalAlignment() == SWT.RIGHT) {
				// make the default button the right-most button
				Button defaultButton = shell.getDefaultButton();
				if (defaultButton != null && isContained(buttonBar, defaultButton)) {
					defaultButton.moveBelow(null);
					defaultButton.getParent().layout();
				}
			}
		}

		super.initializeBounds();
	}

	/**
	 * Returns true if the given Control is a direct or indirect child of container.
	 *
	 * @param container the potential parent
	 * @return boolean <code>true</code> if control is a child of container
	 */
	private boolean isContained(Control container, Control control) {
		Composite parent;
		while ((parent = control.getParent()) != null) {
			if (parent == container) {
				return true;
			}
			control = parent;
		}
		return false;
	}

	/**
	 * The <code>NonModalWindow</code> implementation of this <code>Window</code>
	 * method creates and lays out the top level composite for the dialog, and
	 * determines the appropriate horizontal and vertical dialog units based on the
	 * font size. It then calls the <code>createDialogArea</code> and
	 * <code>createButtonBar</code> methods to create the dialog area and button
	 * bar, respectively. Overriding <code>createDialogArea</code> and
	 * <code>createButtonBar</code> are recommended rather than overriding this
	 * method.
	 */
	@Override
	protected Control createContents(Composite parent) {
		// create the top level composite for the dialog
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		Composite composite = WidgetFactory.composite(0).layout(layout).layoutData(new GridData(GridData.FILL_BOTH))
				.create(parent);
		applyDialogFont(composite);
		// initialize the dialog units
		initializeDialogUnits(composite);
		// create the dialog area and button bar
		dialogArea = createDialogArea(composite);
		buttonBar = createButtonBar(composite);

		return composite;
	}

	/**
	 * Creates and returns the contents of the upper part of this dialog (above the
	 * button bar).
	 * <p>
	 * The <code>NonModalWindow</code> implementation of this framework method
	 * creates and returns a new <code>Composite</code> with standard margins and
	 * spacing.
	 * </p>
	 * <p>
	 * The returned control's layout data must be an instance of
	 * <code>GridData</code>. This method must not modify the parent's layout.
	 * </p>
	 * <p>
	 * Subclasses must override this method but may call <code>super</code> as in
	 * the following example:
	 * </p>
	 *
	 * <pre>
	 * Composite composite = (Composite) super.createDialogArea(parent);
	 * //add controls to composite as necessary
	 * return composite;
	 * </pre>
	 *
	 * @param parent the parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	protected Control createDialogArea(Composite parent) {
		// create a composite with standard margins and spacing
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		Composite composite = WidgetFactory.composite(SWT.NONE).layout(layout)
				.layoutData(new GridData(GridData.FILL_BOTH)).create(parent);
		applyDialogFont(composite);
		return composite;
	}

	/**
	 * Returns the button created by the method <code>createButton</code> for the
	 * specified ID as defined on <code>IDialogConstants</code>. If
	 * <code>createButton</code> was never called with this ID, or if
	 * <code>createButton</code> is overridden, this method will return
	 * <code>null</code>.
	 *
	 * @param id the id of the button to look for
	 *
	 * @return the button for the ID or <code>null</code>
	 *
	 * @see #createButton(Composite, int, String, boolean)
	 * @since 2.0
	 */
	protected Button getButton(int id) {
		return buttons.get(Integer.valueOf(id));
	}

	/**
	 * Returns the button bar control.
	 * <p>
	 * Clients may call this framework method, but should not override it.
	 * </p>
	 *
	 * @return the button bar, or <code>null</code> if the button bar has not been
	 *         created yet
	 */
	protected Control getButtonBar() {
		return buttonBar;
	}

	/**
	 * Returns the button created when <code>createButton</code> is called with an
	 * ID of <code>IDialogConstants.CANCEL_ID</code>. If <code>createButton</code>
	 * was never called with this parameter, or if <code>createButton</code> is
	 * overridden, <code>getCancelButton</code> will return <code>null</code>.
	 *
	 * @return the cancel button or <code>null</code>
	 *
	 * @see #createButton(Composite, int, String, boolean)
	 * @since 2.0
	 * @deprecated Use <code>getButton(IDialogConstants.CANCEL_ID)</code> instead.
	 *             This method will be removed soon.
	 */
	@Deprecated
	protected Button getCancelButton() {
		return getButton(IDialogConstants.CANCEL_ID);
	}

	/**
	 * Returns the dialog area control.
	 * <p>
	 * Clients may call this framework method, but should not override it.
	 * </p>
	 *
	 * @return the dialog area, or <code>null</code> if the dialog area has not been
	 *         created yet
	 */
	protected Control getDialogArea() {
		return dialogArea;
	}

	/**
	 * Returns the standard dialog image with the given key. Note that these images
	 * are managed by the dialog framework, and must not be disposed by another
	 * party.
	 *
	 * @param key one of the <code>NonModalWindow.DLG_IMG_* </code> constants
	 * @return the standard dialog image
	 *
	 *         NOTE: NonModalWindow does not use the following images in the
	 *         registry DLG_IMG_ERROR DLG_IMG_INFO DLG_IMG_QUESTION DLG_IMG_WARNING
	 *
	 *         They are now coming directly from SWT, see ImageRegistry. For
	 *         backwards compatibility they are still supported, however new code
	 *         should use SWT for these.
	 *
	 * @see Display#getSystemImage(int)
	 */
	public static Image getImage(String key) {
		return JFaceResources.getImageRegistry().get(key);
	}

	/**
	 * Returns the button created when <code>createButton</code> is called with an
	 * ID of <code>IDialogConstants.OK_ID</code>. If <code>createButton</code> was
	 * never called with this parameter, or if <code>createButton</code> is
	 * overridden, <code>getOKButton</code> will return <code>null</code>.
	 *
	 * @return the OK button or <code>null</code>
	 *
	 * @see #createButton(Composite, int, String, boolean)
	 * @since 2.0
	 * @deprecated Use <code>getButton(IDialogConstants.OK_ID)</code> instead. This
	 *             method will be removed soon.
	 */
	@Deprecated
	protected Button getOKButton() {
		return getButton(IDialogConstants.OK_ID);
	}

	/**
	 * Initializes the computation of horizontal and vertical dialog units based on
	 * the size of current font.
	 * <p>
	 * This method must be called before any of the dialog unit based conversion
	 * methods are called.
	 * </p>
	 *
	 * @param control a control from which to obtain the current font
	 */
	protected void initializeDialogUnits(Control control) {
		// Compute and store a font metric
		GC gc = new GC(control);
		gc.setFont(JFaceResources.getDialogFont());
		fontMetrics = gc.getFontMetrics();
		gc.dispose();
	}

	/**
	 * Notifies that the ok button of this dialog has been pressed.
	 * <p>
	 * The <code>NonModalWindow</code> implementation of this framework method sets
	 * this dialog's return code to <code>Window.OK</code> and closes the dialog.
	 * Subclasses may override.
	 * </p>
	 */
	protected void okPressed() {
		setReturnCode(OK);
		close();
	}

	/**
	 * Set the layout data of the button to a GridData with appropriate heights and
	 * widths.
	 *
	 * @param button The button which layout data is to be set.
	 */
	protected void setButtonLayoutData(Button button) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
	}

	/**
	 * Set the layout data of the button to a FormData with appropriate heights and
	 * widths.
	 *
	 * @param button The button which layout data is to be set.
	 */
	protected void setButtonLayoutFormData(Button button) {
		FormData data = new FormData();
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.width = Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
	}

	/**
	 * @see org.eclipse.jface.window.Window#close()
	 */
	@Override
	public boolean close() {
		if (getShell() != null && !getShell().isDisposed()) {
			saveDialogBounds(getShell());
		}

		boolean returnValue = super.close();
		if (returnValue) {
			buttons = new HashMap<>();
			buttonBar = null;
			dialogArea = null;
		}
		return returnValue;
	}

	/**
	 * Applies the dialog font to all controls that currently have the default font.
	 *
	 * @param control the control to apply the font to. Font will also be applied to
	 *                its children. If the control is <code>null</code> nothing
	 *                happens.
	 */
	public static void applyDialogFont(Control control) {
		if (control == null || dialogFontIsDefault()) {
			return;
		}
		Font dialogFont = JFaceResources.getDialogFont();
		applyDialogFont(control, dialogFont);
	}

	/**
	 * Sets the dialog font on the control and any of its children if their font is
	 * not otherwise set.
	 *
	 * @param control    the control to apply the font to. Font will also be applied
	 *                   to its children.
	 * @param dialogFont the dialog font to set
	 */
	private static void applyDialogFont(Control control, Font dialogFont) {
		if (hasDefaultFont(control)) {
			control.setFont(dialogFont);
		}
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			for (Control element : children) {
				applyDialogFont(element, dialogFont);
			}
		}
	}

	/**
	 * Return whether or not this control has the same font as it's default.
	 *
	 * @param control Control
	 * @return boolean
	 */
	private static boolean hasDefaultFont(Control control) {
		FontData[] controlFontData = control.getFont().getFontData();
		FontData[] defaultFontData = getDefaultFont(control).getFontData();
		if (controlFontData.length == defaultFontData.length) {
			for (int i = 0; i < controlFontData.length; i++) {
				if (controlFontData[i].equals(defaultFontData[i])) {
					continue;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Get the default font for this type of control.
	 *
	 * @return the default font
	 */
	private static Font getDefaultFont(Control control) {
		String fontName = "DEFAULT_FONT_" + control.getClass().getName(); //$NON-NLS-1$
		if (JFaceResources.getFontRegistry().hasValueFor(fontName)) {
			return JFaceResources.getFontRegistry().get(fontName);
		}
		Font cached = control.getFont();
		control.setFont(null);
		Font defaultFont = control.getFont();
		control.setFont(cached);
		JFaceResources.getFontRegistry().put(fontName, defaultFont.getFontData());
		return defaultFont;
	}

	/**
	 * Return whether or not the dialog font is currently the same as the default
	 * font.
	 *
	 * @return boolean if the two are the same
	 */
	protected static boolean dialogFontIsDefault() {
		FontData[] dialogFontData = JFaceResources.getFontRegistry().getFontData(JFaceResources.DIALOG_FONT);
		FontData[] defaultFontData = JFaceResources.getFontRegistry().getFontData(JFaceResources.DEFAULT_FONT);
		return Arrays.equals(dialogFontData, defaultFontData);
	}

	@Override
	public void create() {
		super.create();
		applyDialogFont(buttonBar);
	}

	/**
	 * Get the IDialogBlockedHandler to be used by WizardDialogs and ModalContexts.
	 *
	 * @return Returns the blockedHandler.
	 */
	public static IDialogBlockedHandler getBlockedHandler() {
		return blockedHandler;
	}

	/**
	 * Set the IDialogBlockedHandler to be used by WizardDialogs and ModalContexts.
	 *
	 * @param blockedHandler The blockedHandler for the dialogs.
	 */
	public static void setBlockedHandler(IDialogBlockedHandler blockedHandler) {
		NonModalWindow.blockedHandler = blockedHandler;
	}

	/**
	 * Gets the dialog settings that should be used for remembering the bounds of of
	 * the dialog, according to the dialog bounds strategy.
	 *
	 * @return settings the dialog settings used to store the dialog's location
	 *         and/or size, or <code>null</code> if the dialog's bounds should never
	 *         be stored.
	 *
	 * @since 3.2
	 * @see NonModalWindow#getDialogBoundsStrategy()
	 */
	protected IDialogSettings getDialogBoundsSettings() {
		return null;
	}

	/**
	 * Get the integer constant that describes the strategy for persisting the
	 * dialog bounds. This strategy is ignored if the implementer does not also
	 * specify the dialog settings for storing the bounds in
	 * NonModalWindow.getDialogBoundsSettings().
	 *
	 * @return the constant describing the strategy for persisting the dialog
	 *         bounds.
	 *
	 * @since 3.2
	 * @see NonModalWindow#DIALOG_PERSISTLOCATION
	 * @see NonModalWindow#DIALOG_PERSISTSIZE
	 * @see NonModalWindow#getDialogBoundsSettings()
	 */
	protected int getDialogBoundsStrategy() {
		return DIALOG_PERSISTLOCATION | DIALOG_PERSISTSIZE;
	}

	/**
	 * Saves the bounds of the shell in the appropriate dialog settings. The bounds
	 * are recorded relative to the parent shell, if there is one, or display
	 * coordinates if there is no parent shell.
	 *
	 * @param shell The shell whose bounds are to be stored
	 *
	 * @since 3.2
	 */
	private void saveDialogBounds(Shell shell) {
		IDialogSettings settings = getDialogBoundsSettings();
		if (settings != null) {
			Point shellLocation = shell.getLocation();
			Point shellSize = shell.getSize();
			Shell parent = getParentShell();
			if (parent != null) {
				Point parentLocation = parent.getLocation();
				shellLocation.x -= parentLocation.x;
				shellLocation.y -= parentLocation.y;
			}
			int strategy = getDialogBoundsStrategy();
			if ((strategy & DIALOG_PERSISTLOCATION) != 0) {
				settings.put(DIALOG_ORIGIN_X, shellLocation.x);
				settings.put(DIALOG_ORIGIN_Y, shellLocation.y);
			}
			if ((strategy & DIALOG_PERSISTSIZE) != 0) {
				settings.put(DIALOG_WIDTH, shellSize.x);
				settings.put(DIALOG_HEIGHT, shellSize.y);
				FontData[] fontDatas = JFaceResources.getDialogFont().getFontData();
				if (fontDatas.length > 0) {
					settings.put(DIALOG_FONT_DATA, fontDatas[0].toString());
				}
			}
		}
	}

	/**
	 * Returns the initial size to use for the shell. Overridden to check whether a
	 * size has been stored in dialog settings. If a size has been stored, it is
	 * returned.
	 *
	 * @return the initial size of the shell
	 *
	 * @since 3.2
	 * @see #getDialogBoundsSettings()
	 * @see #getDialogBoundsStrategy()
	 */
	@Override
	protected Point getInitialSize() {
		Point result = super.getInitialSize();

		// Check the dialog settings for a stored size.
		if ((getDialogBoundsStrategy() & DIALOG_PERSISTSIZE) != 0) {
			IDialogSettings settings = getDialogBoundsSettings();
			if (settings != null) {
				// Check that the dialog font matches the font used
				// when the bounds was stored. If the font has changed,
				// we do not honor the stored settings.
				// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=132821
				boolean useStoredBounds = true;
				String previousDialogFontData = settings.get(DIALOG_FONT_DATA);
				// There is a previously stored font, so we will check it.
				// Note that if we haven't stored the font before, then we will
				// use the stored bounds. This allows restoring of dialog bounds
				// that were stored before we started storing the fontdata.
				if (previousDialogFontData != null && previousDialogFontData.length() > 0) {
					FontData[] fontDatas = JFaceResources.getDialogFont().getFontData();
					if (fontDatas.length > 0) {
						String currentDialogFontData = fontDatas[0].toString();
						useStoredBounds = currentDialogFontData.equalsIgnoreCase(previousDialogFontData);
					}
				}
				if (useStoredBounds) {
					try {
						// Get the stored width and height.
						int width = settings.getInt(DIALOG_WIDTH);
						if (width != DIALOG_DEFAULT_BOUNDS) {
							result.x = width;
						}
						int height = settings.getInt(DIALOG_HEIGHT);
						if (height != DIALOG_DEFAULT_BOUNDS) {
							result.y = height;
						}

					} catch (NumberFormatException e) {
					}
				}
			}
		}
		// No attempt is made to constrain the bounds. The default
		// constraining behavior in Window will be used.
		return result;
	}

	/**
	 * Returns the initial location to use for the shell. Overridden to check
	 * whether the bounds of the dialog have been stored in dialog settings. If a
	 * location has been stored, it is returned.
	 *
	 * @param initialSize the initial size of the shell, as returned by
	 *                    <code>getInitialSize</code>.
	 * @return the initial location of the shell
	 *
	 * @since 3.2
	 * @see #getDialogBoundsSettings()
	 * @see #getDialogBoundsStrategy()
	 */
	@Override
	protected Point getInitialLocation(Point initialSize) {
		Point result = super.getInitialLocation(initialSize);
		if ((getDialogBoundsStrategy() & DIALOG_PERSISTLOCATION) != 0) {
			IDialogSettings settings = getDialogBoundsSettings();
			if (settings != null) {
				try {
					int x = settings.getInt(DIALOG_ORIGIN_X);
					int y = settings.getInt(DIALOG_ORIGIN_Y);
					result = new Point(x, y);
					// The coordinates were stored relative to the parent shell.
					// Convert to display coordinates.
					Shell parent = getParentShell();
					if (parent != null) {
						Point parentLocation = parent.getLocation();
						result.x += parentLocation.x;
						result.y += parentLocation.y;
					}
				} catch (NumberFormatException e) {
				}
			}
		}
		// No attempt is made to constrain the bounds. The default
		// constraining behavior in Window will be used.
		return result;
	}

	/**
	 * Returns a boolean indicating whether the dialog should be considered
	 * resizable when the shell style is initially set.
	 * <p>
	 * This method is used to ensure that all style bits appropriate for resizable
	 * dialogs are added to the shell style. Individual dialogs may always set the
	 * shell style to ensure that a dialog is resizable, but using this method
	 * ensures that resizable dialogs will be created with the same set of style
	 * bits.
	 * </p>
	 * <p>
	 * Style bits will never be removed based on the return value of this method.
	 * For example, if a dialog returns <code>false</code>, but also sets a style
	 * bit for a SWT.RESIZE border, the style bit will be honored.
	 * </p>
	 *
	 * @return a boolean indicating whether the dialog is resizable and should have
	 *         the default style bits for resizable dialogs
	 *
	 * @since 3.4
	 */
	protected boolean isResizable() {
		return false;
	}
}
