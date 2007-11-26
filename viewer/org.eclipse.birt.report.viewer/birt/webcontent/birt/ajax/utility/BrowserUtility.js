// Copyright 1994-2006, Actuate Software Corp., All rights reserved.

BrowserUtility = Class.create();

BrowserUtility.prototype = {
	
	initialize: function()
	{
		this.isIE = this.__isIE();
		this.isSafari = this.__isSafari();
	},
		
	__isSafari: function()
	{
		return navigator.appVersion.match(/Safari/);		
	},
	
	__isIE: function()
	{
		var userAgent = navigator.userAgent.toLowerCase();
		var useIFrame;
		if(userAgent.indexOf('msie') > -1)
		{
			//Internet Explorer
			return true;
			
		}
		else 
		{
			return false;
		}
	},
	
	useIFrame: function()
	{
		return this.isIE;
	}
}
