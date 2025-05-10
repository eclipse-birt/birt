/*******************************************************************************
 * Copyright (c) 2025 Thomas Gutmann
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Thomas Gutmann  - initial implementation
 *******************************************************************************/

/**
 *	BirtToolbarTheme, Theme handling for the report viewer
 */
 /**
  *	BirtToolbar
  *	...
  */
 BirtToolbarTheme = Class.create( );

 BirtToolbarTheme.prototype = 
 {
 	/**
 	 *	Initialization routine required by "ProtoType" lib.
 	 *	@return, void
 	 */
	initialize : function( id )
	{
	},
	initToggle : function() {
		this.toggleSwitch("initTheme");
	}
	,
	/**
	 *	Handle the switch of the theme between "light" & "dark"
	 *
	 *	@param mode, execution mode "initTheme", "toggleButton"
	 */
	toggleSwitch : function(mode) {
		const theme_layout_key = "birt_theme_layout_key";
		var nodeToggleTrackIcon		= document.getElementById("toggle-track-icon");
		var nodeToggleTrackCheckbox	= document.getElementById("toggle-track-checkbox");
		var themeValue				= "light";
		var themeValueRestored		= null;

		if (nodeToggleTrackIcon && nodeToggleTrackCheckbox) {
			if (mode === "initTheme") {
				themeValueRestored = this.getThemeMode(theme_layout_key);
			}
			if (mode === "initTheme" && themeValueRestored != null) {
				themeValue = themeValueRestored;
				if (themeValueRestored === "light") {
					nodeToggleTrackCheckbox.checked = false;
					nodeToggleTrackIcon.classList.remove("toggle-track-icon-checked");
				} else {
					nodeToggleTrackCheckbox.checked = true;
					nodeToggleTrackIcon.classList.add("toggle-track-icon-checked");
				}
			} else {
				if (nodeToggleTrackCheckbox.checked) {
					themeValue = "light";
					nodeToggleTrackCheckbox.checked = false;
					nodeToggleTrackIcon.classList.remove("toggle-track-icon-checked");

				} else {
					themeValue = "dark";
					nodeToggleTrackCheckbox.checked = true;
					nodeToggleTrackIcon.classList.add("toggle-track-icon-checked");
				}
				this.saveThemeMode(theme_layout_key, themeValue);
			}
			this.themeDarkSwitch(themeValue);
		}
	}
	,
	/**
	 *	Switch of the css classes to handle the layout "light" & "dark"
	 *
	 *	@param theme, which display theme is to be used
	 */
	themeDarkSwitch : function(theme) {
		var themeDark = (theme === "dark");
		var nodeCaption = document.querySelectorAll("[class*='body_caption'], [class*='birtviewer_navbar'], [class*='dialogBackground'], [class*='birtviewer_progressbar']");
		for (caption of nodeCaption	) {
			if(themeDark)
				caption.classList.add("dark_theme_caption");
			else
				caption.classList.remove("dark_theme_caption");
		}
		var nodeToolBar		= document.querySelectorAll("[class*='birtviewer_toolbar'], [class*='body_caption_top'], [class*='dialogTitleBar']");
		for (button of nodeToolBar	) {
			if(themeDark)
				button.classList.add("dark_theme_toolbar");
			else
				button.classList.remove("dark_theme_toolbar");
		}
		var nodeDocument = document.querySelectorAll("[id='documentView'], [id='Document'], [id='display0'], [id='frameset-page']");
		for (doc of nodeDocument) {
			if(themeDark)
				doc.classList.add("dark_theme_document");
			else
				doc.classList.remove("dark_theme_document");
		}
		var nodeExceptoinDialog = document.querySelectorAll("[class*='birtviewer_exception_dialog']");
		for (dialog of nodeExceptoinDialog) {
			if(themeDark)
				dialog.classList.add("dark_theme_exception_dialog");
			else
				dialog.classList.remove("dark_theme_exception_dialog");
		}
	}
	,
	/**
	 *	Store the selected mode as cookie
	 *
	 *	@param cname, key name of the cookie
	 *	@param cvalue, value of the cookie
	 */
	saveThemeMode : function(cname, cvalue) {
		document.cookie = cname + "=" + cvalue;
	}
	,
	/**
	 *	Get the selected mode as cookie
	 *
	 *	@param cname, key name of the cookie
	 *	@return value of the key
	 */
	getThemeMode : function(cname) {
		let name	= cname + "=";
		let ca		= document.cookie.split(';');
		let cvalue	= null;
		for(let i = 0; i < ca.length; i++) {
			let c = ca[i];
			while (c.charAt(0) == ' ') {
				c = c.substring(1);
			}
			if (c.indexOf(name) == 0) {
				cvalue = c.substring(name.length, c.length);
			}
		}
		return cvalue;
	}
}