package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.StyleUtil;

public abstract class LibraryHandleImpl extends LayoutModuleHandle implements ILibraryModel {

	public LibraryHandleImpl(Module module) {
		super(module);
	}

	/**
	 * Returns the host handle which includes the library.
	 * 
	 * @return the host handle which include this library.
	 */
	public ModuleHandle getHostHandle() {
		Module host = ((Library) getElement()).getHost();
		if (host == null)
			return null;
		return (ModuleHandle) host.getHandle(getModule());
	}

	/**
	 * Returns the library namespace, which identifies one library unqiuely in one
	 * design file.
	 * 
	 * @return the library namespace
	 */
	public String getNamespace() {
		return ((Library) module).getNamespace();
	}

	/**
	 * Returns a slot handle to work with the themes within the library. Note that
	 * the order of the data sets within the slot is unimportant.
	 * 
	 * @return A handle for working with the themes.
	 */
	public SlotHandle getThemes() {
		return getSlot(THEMES_SLOT);
	}

	/**
	 * Returns a slot handle to work with the styles within the library.
	 * 
	 * @return A handle for working with the styles. Or <code>null</code> if the
	 *         library has no values for the theme property
	 * @deprecated uses the theme instead
	 */
	public SlotHandle getStyles() {
		ThemeHandle theme = getTheme();
		if (theme == null)
			return null;

		return theme.getStyles();
	}

	/**
	 * Import css file to theme.
	 * 
	 * @param stylesheet     the style sheet handle that contains all the selected
	 *                       styles
	 * @param selectedStyles the selected style list
	 */
	public void importCssStyles(CssStyleSheetHandle stylesheet, List selectedStyles) {
		String themeName = ((Module) getElement()).getThemeName();

		if (themeName == null)
			themeName = CommandLabelFactory.getCommandLabel(IThemeModel.DEFAULT_THEME_NAME);

		importCssStyles(stylesheet, selectedStyles, themeName);

	}

	/**
	 * Creates the theme with the given name if it does not exist. Sets the theme
	 * value of the library to the given name if this value was <code>null</code>.
	 * 
	 * @param themeName the theme name
	 * @return the theme handle
	 */
	private ThemeHandle setupTheme(String themeName) {
		Library libElement = (Library) getElement().getRoot();
		Theme theme = libElement.findNativeTheme(themeName);

		if (theme == null) {
			ThemeHandle newTheme = getModuleHandle().getElementFactory().newTheme(themeName);
			try {
				getThemes().add(newTheme);
			} catch (ContentException e) {
				assert false;
			} catch (NameException e) {
				assert false;
			}
			theme = (Theme) newTheme.getElement();
		}

		try {
			if (libElement.getThemeName() == null)
				setThemeName(themeName);
		} catch (SemanticException e) {
			assert false;
		}

		return (ThemeHandle) theme.getHandle(module);
	}

	/**
	 * Imports the selected styles in a <code>CssStyleSheetHandle</code> to the
	 * given theme of the library. Each in the list is instance of
	 * <code>SharedStyleHandle</code> .If any style selected has a duplicate name
	 * with that of one style already existing in the report design, this method
	 * will rename it and then add it to the design.
	 * 
	 * @param stylesheet     the style sheet handle that contains all the selected
	 *                       styles
	 * @param selectedStyles the selected style list
	 * @param themeName      the name of the theme to put styles
	 */
	public void importCssStyles(CssStyleSheetHandle stylesheet, List selectedStyles, String themeName) {
		if (StringUtil.isBlank(themeName))
			return;

		ActivityStack stack = module.getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.IMPORT_CSS_STYLES_MESSAGE));

		// creates the theme if it does not exist

		ThemeHandle themeHandle = setupTheme(themeName);

		for (int i = 0; i < selectedStyles.size(); i++) {
			SharedStyleHandle style = (SharedStyleHandle) selectedStyles.get(i);
			if (stylesheet.findStyle(style.getName()) != null) {
				try {
					// Copy CssStyle to Style

					SharedStyleHandle newStyle = StyleUtil.transferCssStyleToSharedStyle(module, style);

					if (newStyle == null)
						continue;
					newStyle.getElement().setName(themeHandle.makeUniqueStyleName(newStyle.getName()));

					themeHandle.getStyles().add(newStyle);
				} catch (ContentException e) {
					assert false;
				} catch (NameException e) {
					assert false;
				}
			}
		}

		stack.commit();
	}

	public SlotHandle getCubes() {
		return getSlot(CUBE_SLOT);
	}

	/**
	 * If this library is included by a module, return the relative file name that
	 * is defined in the host's xml file.
	 * 
	 * @return the relative file name that is defined in the host's xml file
	 */
	public String getRelativeFileName() {
		ModuleHandle hostHandle = getHostHandle();
		if (hostHandle == null)
			return null;

		Module host = (Module) hostHandle.getElement();
		if (host == null)
			return null;

		IncludedLibrary libStruct = host.findIncludedLibrary(getNamespace());
		if (libStruct != null)
			return libStruct.getFileName();

		return null;
	}

	public boolean isDirectionRTL() {
		return false;
	}

}