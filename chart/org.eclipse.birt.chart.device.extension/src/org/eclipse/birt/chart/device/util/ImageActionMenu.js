/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
/**
 * This JavaScript library defines classes and methods to handle interactivity
 * actions on Chart, this library is valid for Image format in html.
 * 
 * @since 2.5.1
 */
function BirtChartConstants() {};
BirtChartConstants.MOUSE_CLICK = 'click';
BirtChartConstants.MOUSE_OVER = 'mouseover';
BirtChartConstants.MOUSE_OUT = 'mouseout';
BirtChartConstants.CLASS_BIRT_CHART_OBJECT = 'BirtChartObject';
BirtChartConstants.CLASS_BIRT_CHART_MENU = 'BirtChartMenu';
BirtChartConstants.CLASS_BIRT_CHART_MENU_ITEM = 'BirtChartMenuItem';
BirtChartConstants.CLASS_BIRT_CHART_MENU_INFO = 'BirtChartMenuInfo';
BirtChartConstants.CLASS_BIRT_CHART_MENU_ITEM_INFO = 'BirtChartMenuItemInfo';
BirtChartConstants.MENU_DELAY_TIME = 2000;
BirtChartConstants.MENU_OBJ = '_menu_obj';

ImageChartUtil = function() { };
ImageChartUtil.prototype = new Object();
ImageChartUtil.createElement = function(docObj, tagName, id, text, cssClass) {
	var e = docObj.createElement(tagName);
	e.id = id;
	if (cssClass) e.className = cssClass;
	else if (id) e.className = id;
	if (text) e.innerHTML = text;
	return e;
};
ImageChartUtil.isIPadIPhone= (function() {
	var userAgent = navigator.userAgent.toLowerCase();
	return userAgent.match(/iPad/i) || userAgent.match(/iPhone/i);
})();
ImageChartUtil.isAndroid = (function() {
	var userAgent = navigator.userAgent.toLowerCase();
	return (userAgent.indexOf("android") > -1);
})();

BirtChartCSSHelper = function() { };
BirtChartCSSHelper.prototype = new Object();
BirtChartCSSHelper.getStylesMap = function(stylesStr, separator) {
	var stylesArray = stylesStr.split(';');
	var total = [];
	for ( var i = 0; i < stylesArray.length; i++) {
		var unit = stylesArray[i].split(':');
		if (unit.length == 2) {
			total[unit[0]] = unit[1];
		} else {
			var value = '';
			for ( var j = 1; j < unit.length; j++) {
				if (j > 1) value += ':';
				value += unit[j];
			}
			total[unit[0]] = value;
		}
	}
	return total;
};

BirtChartCSSHelper.setStyles = function(widget, styles) {
	if (widget == undefined || styles == undefined) return;
	var all = styles;
	if (typeof styles == 'string') {
		all = this.getStylesMap(styles, ';');
	}

	for ( var property in all) {
		if (property == undefined || (typeof all[property]) == 'function')
			continue;
		if (all[property].charAt(0) == '\'' || all[property].charAt(0) == '\"')
			eval('widget.style.' + property + ' = ' + all[property] + ';');
		else
			eval('widget.style.' + property + ' = \"' + all[property] + '\";');
	}
};

BirtChartObject = function() { };
BirtChartObject.prototype = {
	widget : undefined,
	className : BirtChartConstants.CLASS_BIRT_CHART_OBJECT,
	getClassName : function() {
		return this.className;
	},
	getWidget : function() {
		return this.widget;
	},
	getId : function() {
		return this.widget.id;
	},
	getCSSClass : function() {
		return this.widget.className;
	},
	show : function() {
		if (this.widget)
			this.widget.style.display = 'block';
	},
	hide : function() {
		if (this.widget)
			this.widget.style.display = 'none';
	}
};

BirtChartMenuHelper = function() { };
BirtChartMenuHelper.prototype = new Object();
BirtChartMenuHelper.menu = null;
BirtChartMenuHelper.menuHideTimer = undefined;
BirtChartMenuHelper.reset = function() {
	// Remove menu from document.
	if (this.menu != null && document.getElementById(this.menu.getId()) != null) {
		document.getElementsByTagName('body')[0].removeChild(this.menu
				.getWidget());
	}
	// Reset static variables.
	this.menu = null;
	this.menuHideTimer = undefined;
	return;
};
BirtChartMenuHelper.createPopupMenu = function(evt, menuInfo) {
	this.reset();
	if ( menuInfo.menuItemNames.length == 0 )
		return null;

	// Create 'div' menu.
	var bcm = new BirtChartMenu(menuInfo);
	this.menu = bcm;
	menuInfo.evt = evt;
	menuInfo.targetChart = evt.target || evt.srcElement;
	// Add menu to document.
	var htmlBody = document.getElementsByTagName('body')[0];
	htmlBody.oncontextmenu = bcm.hide;
	htmlBody.appendChild(bcm.getWidget());
	// Set location and styles of menu.
	bcm.setLocation(evt, htmlBody);
	BirtChartCSSHelper.setStyles(bcm.getWidget(), bcm.menuInfo.menuStyles);
	return bcm;
};
BirtChartMenuHelper.registerHideTimer = function(time) {
	if (this.menuHideTimer != undefined)
		this.unregisterHideTimer();
	this.menuHideTimer = window.setTimeout('if ( BirtChartMenuHelper.menu ) BirtChartMenuHelper.menu.hide();',
			time);
};
BirtChartMenuHelper.unregisterHideTimer = function() {
	try {
		if (this.menuHideTimer != undefined)
			window.clearTimeout(BirtChartMenuHelper.menuHideTimer);
	} catch (e) { }
};
BirtChartMenuHelper.dispatchEvent = function(event) {
	var evt = event ? event : window.event;
	var source = event ? event.target : window.event.srcElement;
	if (typeof source[BirtChartConstants.MENU_OBJ] == 'undefined') return;
	var birtChartObj = source[BirtChartConstants.MENU_OBJ];
	var isMenuItem = (birtChartObj.getClassName() == BirtChartConstants.CLASS_BIRT_CHART_MENU_ITEM);
	if (birtChartObj) {
		var type = evt.type;
		switch (type) {
		case BirtChartConstants.MOUSE_CLICK:
			if (isMenuItem) {
				BirtChartMenuHelper.menu.hide();
				BirtChartMenuHelper.executeMenuAction(evt,
						birtChartObj.itemInfo, birtChartObj.parent.menuInfo);
				break;
			}
			break;

		case BirtChartConstants.MOUSE_OVER:
			BirtChartMenuHelper.unregisterHideTimer();
			if (isMenuItem && !birtChartObj.itemInfo.isTooltipItem( ) )
				birtChartObj
						.setOnMouseOverStyles(birtChartObj.parent.menuInfo.mouseOverStyles);
			break;

		case BirtChartConstants.MOUSE_OUT:
			BirtChartMenuHelper.registerHideTimer(300);
			if (isMenuItem && !birtChartObj.itemInfo.isTooltipItem( ) )
				birtChartObj
						.setOnMouseOutStyles(birtChartObj.parent.menuInfo.mouseOutStyles);
			break;
		}
	}
	return;
};
BirtChartMenuHelper.executeMenuAction = function(evt, itemInfo, menuInfo) {
	switch (itemInfo.actionType) {
	case BirtChartInteractivityActions.HYPER_LINK: // Hyperlink
		var url = itemInfo.actionValue;
		var target = '_blank';
		if (itemInfo.target)
			target = itemInfo.target;
		if (!target || target == '')
			target = '_self';
		if (url.indexOf('#', 0) == 0) {
			window.location = url;
			return;
		}
		if (url.indexOf('javascript:', 0) == 0) {
			eval(url.substring(11, url.length - 1));
			return;
		}
		try {
			window.open(url, target);
		} catch (e) {
			redirect(target, url);
		}

		break;

	case BirtChartInteractivityActions.INVOKE_SCRIPTS: // Invoke scripts
		var scripts = itemInfo.actionValue;
		if (scripts != undefined) {
			var f = BirtChartMenuHelper.callScripts;
			f(scripts, evt, menuInfo.categoryData, menuInfo.valueData,
					menuInfo.valueSeriesName, menuInfo.legendItemData,
					menuInfo.legendItemText, menuInfo.legendItemValue,
					menuInfo.axisLabel, menuInfo);
		}
		break;
	}
};
BirtChartMenuHelper.callScripts = function(scripts, evt, categoryData,
		valueData, valueSeriesName, legendItemData, legendItemText,
		legendItemValue, axisLabel, menuInfo) {
	eval(scripts);
};

BirtChartMenuItem = function(parent, index, menuItemInfo) {
	this.parent = parent;
	this.itemInfo = menuItemInfo;
	var id = this.itemInfo.text + '_' + this.parent.menuInfo.id + '_' + index;
	var text = this.itemInfo.text;
	var cssClass = this.itemInfo.cssClass;

	this.widget = ImageChartUtil.createElement(document, 'Div', id, text,
			cssClass);
	this.widget[BirtChartConstants.MENU_OBJ] = this;
	this.widget.onclick = BirtChartMenuHelper.dispatchEvent;
	if( !ImageChartUtil.isIPadIPhone && !ImageChartUtil.isAndroid ) { // Avoid to register mouse over and mouse out events for mobile browser.
		this.widget.onmouseover = BirtChartMenuHelper.dispatchEvent;
		this.widget.onmouseout = BirtChartMenuHelper.dispatchEvent;
	}
	if (typeof this.itemInfo.tooltip != 'undefined')
		this.setTooltip(this.itemInfo.tooltip);
	this.setStyles(this.parent.menuInfo.menuItemStyles);
};
BirtChartMenuItem.prototype = new BirtChartObject();
BirtChartMenuItem.prototype.constructor = BirtChartMenuItem;
BirtChartMenuItem.prototype.className = BirtChartConstants.CLASS_BIRT_CHART_MENU_ITEM;
BirtChartMenuItem.prototype.setTooltip = function(tooltip) {
	this.widget.title = tooltip;
};
BirtChartMenuItem.prototype.setStyles = function(styles) {
	this.widget.style.cursor = 'default';
	BirtChartCSSHelper.setStyles(this.widget, styles);
};
BirtChartMenuItem.prototype.setOnMouseOverStyles = function(styles) {
	BirtChartCSSHelper.setStyles(this.widget, styles);
};
BirtChartMenuItem.prototype.setOnMouseOutStyles = function(styles) {
	BirtChartCSSHelper.setStyles(this.widget, styles);
};

BirtChartMenu = function(menuInfo) {
	this.menuInfo = menuInfo;
	this.widget = ImageChartUtil.createElement(document, 'Div', menuInfo.id,
			menuInfo.text, menuInfo.cssClass);
	this.widget[BirtChartConstants.MENU_OBJ] = this;
	if( !ImageChartUtil.isIPadIPhone && !ImageChartUtil.isAndroid ) { // Avoid to register mouse over and mouse out events for mobile browser.
		this.widget.onmouseover = BirtChartMenuHelper.dispatchEvent;
		this.widget.onmouseout = BirtChartMenuHelper.dispatchEvent;
	}
	this.styles = undefined;
	this.setMenuStyles(this.menuInfo.menuStyles);

	// Create menu items;
	for ( var i = 0; i < this.menuInfo.menuItemNames.length; i++) {
		var item = this.menuInfo.menuItemNames[i];
		var bcmi = new BirtChartMenuItem(this, i, this.menuInfo.menuItems[item]);
		this.addMenuItem(bcmi);
	}
};
BirtChartMenu.prototype = new BirtChartObject();
BirtChartMenu.prototype.constructor = BirtChartMenu;
BirtChartMenu.prototype.className = BirtChartConstants.CLASS_BIRT_CHART_MENU;
BirtChartMenu.prototype.menuItems = new Array();
BirtChartMenu.prototype.addMenuItem = function(menuItem) {
	if (menuItem.getClassName() != BirtChartConstants.CLASS_BIRT_CHART_MENU_ITEM) return;
	this.menuItems.push(menuItem);
	this.widget.appendChild(menuItem.getWidget());
};
BirtChartMenu.prototype.setMenuStyles = function(styles) {
	this.styles = styles;
};
BirtChartMenu.prototype.show = function() {
	BirtChartMenuHelper.registerHideTimer(BirtChartConstants.MENU_DELAY_TIME);
	var f = BirtChartObject.prototype.show;
	f.call(this);
};
BirtChartMenu.prototype.hide = function() {
	BirtChartMenuHelper.unregisterHideTimer();
	var f = BirtChartObject.prototype.hide;
	f.call(this);
};
BirtChartMenu.prototype.setLocation = function(evt, htmlBody) {
	// Adjust menu position.
	this.widget.style.left = htmlBody.scrollLeft + evt.clientX + 'px';
	this.widget.style.top = htmlBody.scrollTop
			- htmlBody.getBoundingClientRect().top + evt.clientY + 'px';
};
BirtChartMenu.prototype.adjustLocation = function(evt, htmlBody) {
	// Adjust menu position.
	var redge = htmlBody.clientWidth - evt.clientX;
	var bedge = htmlBody.clientHeight - evt.clientY;
	if (redge < this.widget.offsetWidth) {
		this.widget.style.left = htmlBody.scrollLeft + evt.clientX
				- this.widget.offsetWidth + 'px';
	} else {
		this.widget.style.left = htmlBody.scrollLeft + evt.clientX + 'px';
	}
	if (bedge < this.widget.offsetHeight) {
		this.widget.style.top = htmlBody.scrollTop
				- htmlBody.getBoundingClientRect().top + evt.clientY
				- this.widget.offsetHeight + 'px';
	} else {
		this.widget.style.top = htmlBody.scrollTop
				- htmlBody.getBoundingClientRect().top + evt.clientY + 'px';
	}
};

BirtChartMenuInfo = function() {
	this.className = BirtChartConstants.CLASS_BIRT_CHART_MENU_INFO;

	this.id = undefined;
	this.text = undefined;
	this.cssClass = undefined;

	this.categoryData = undefined;
	this.valueData = undefined;
	this.valueSeriesName = undefined;

	this.legendItemData = undefined;
	this.legendItemText = undefined;
	this.legendItemValue = undefined;
	this.axisLabel = undefined;

	this.menuStyles = undefined;
	this.menuItemStyles = undefined;
	this.mouseOverStyles = undefined;
	this.mouseOutStyles = undefined;

	this.menuItemNames = [];
	this.menuItems = [];
	this.menuCount = 0;
};
BirtChartMenuInfo.prototype = new Object();
BirtChartMenuInfo.prototype = {
	addItemInfo : function(itemInfo) {
		var item = '' + this.menuCount;
		this.menuItemNames.push(item);
		this.menuItems[item] = itemInfo;
		this.menuCount = this.menuCount + 1;
	}
};
BirtChartMenuItemInfo = function() {
	this.className = BirtChartConstants.CLASS_BIRT_CHART_MENU_ITEM_INFO;
	this.text = undefined;
	this.target = undefined;
	this.cssClass = undefined;
	this.tooltip = undefined;

	this.actionType = undefined;
	this.actionValue = undefined;
};
BirtChartMenuItemInfo.prototype = new Object();
BirtChartMenuItemInfo.prototype.isTooltipItem = function() {
	return ( this.actionType == BirtChartInteractivityActions.SHOW_TOOLTIP );
}
function BirtChartInteractivityActions() { };
BirtChartInteractivityActions.HYPER_LINK = 1;
BirtChartInteractivityActions.INVOKE_SCRIPTS = 2;
BirtChartInteractivityActions.SHOW_TOOLTIP = 6;