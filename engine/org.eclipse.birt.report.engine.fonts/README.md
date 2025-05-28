# Eclipse BIRT 
Introduction of the font configuration options.

## Reason
BIRT provides several options to define fonts, fonts mapping, control font handling and further individual font settings.  
The font setup can be done configuration files named **fontsConfig*.xml**


### Location of fonts configuration files
	
The font configuration files are located at different locations dependent at the runtime version of BIRT which is used.  
*	**BIRT runtime**  
   
	- **location:**	.../lib/org.eclipse.birt.runtime_*version*.jar  
	- the configuration files fontsConfg*.xml located into the jar file, the processing is:  
			(1) unzip the jar file  
			(2) change the fonts.Config.xml settings  
			(3) pack the jar file with the changed configuration  
   
* **BIRT runtime OSGi**  
   
	- **location:** .../WEB-INF/platform/plugins/org.eclipse.birt.report.engine.fonts_*version*
   
	- the configuration can be entered into the xml files  
   
* **BIRT engine configuration**  
   
	- With the usage of code the font configuration can be located independent from the original file location.  
	To use this option the path of the configuration file can be set with `engineConfig.setFontConfig(fontsConfigurationURL)`   
	Code example for configuration path "C:\conf\birt\font\fontsConfig.xml":  
	  	
	(1) setup a global system property at JVM start
		-Dbirt.font.config.xml=file:///C:/conf/birt/font  

	(2) code sequence to read the config file
	
		fontsConfigurationURL = new URI(System.getProperty("birt.font.config.xml") + "/fontsConfig.xml").toURL();
		engineConfig.setFontConfig(fontsConfigurationURL);

### Handling of fonts configuration files

**Priority of configuration files**

Two major levels of configuration files:  

(1) Configuration files without format in name are divided into three sub-levels:  
- default configuration file: fontsConfig.xml  
- configuration files specified for special platform: fontsConfig_win32.xml fontsConfig_linux.xml  
- configuration files specified for special platform and special locale: fontsConfig_win32_zh.xml/fontsConfig_win32_zh_CN.xml  

(2) Configuration files with format in name are also divided into 3 sub-levels:  
- default configuration files for a format: fontsConfig_pdf.xml  
- configuration files for a format under special platform: fontsConfig_pdf_win32.xml  
- configuration files for a format under special platform: fontsConfig_pdf_win32_zh.xml  

From level a to level f, the priority of configuration files increase.  

   
**Logic of configuration loading**

(1)	PDF layout engine loads the font files from the fonts folder of the plug-in "org.eclipse.birt.report.engine.fonts"  
and then the system defined font folder. If the section `<font-paths>` is set in fontsConfig.xml, the system  
defined font folder will be ignored, and the font files specified in section `<font-paths>` will be loaded instead.    
  
(2) PDF layout engine tries to use the font specified in design-time to render the pdf/postscript.  
If the font is a generic family, it will be mapped to a PDF embedded type1 font. 	
- "serif" to "Times-Roman" 
- "fantasy" to "Times-Roman" 
- "sans-serif" to "Helvetica"
- "monospace" to "Courier"
- "cursive" to "Times-Roman"

If the font can not be built, it will go to the UNICODE block of that character and try the font defined in that block.  
If this also fails, the character will be replaced with '?' using the default font, currently "Times-Roman" to denote a place of missing character.  
    
    
**Font aliases**

In `<font-aliases>` section, you can:  
- Define a mapping from a generic family to a font family for example:   
the following defines a mapping from generic family "serif" to a Type1 font family "Times-Roman"   
`<mapping name="serif" font-family="Times-Roman"/>`  
     
- Define a mapping from a font family to another font family. This is useful if you want to use a font   
for PDF rendering which differs from the font used in design-time for example:   
the following defines to replace "simsun" with "Arial Unicode MS"  
`<mapping name="simsun" font-family="Arial Unicode MS"/>`
   	
In previous version, name "font-mapping" is used for "font-aliases". Currently, "font-mapping" entry  
also works in the same way as "font-aliases" entry does. When "font-mapping" and "font-aliases" are both available,   
the different mappings are merged, for the same entries in the both, those in "font-aliases" will override those in "font-mapping".  
    
    
**Composite font**

`<composite-font>` section is used to define a composite font, which is a font consists   
of many physical fonts used for different characters. The component fonts are defined by `<block>` entries.   
Each `<block>` entry defines a mapping from a UNICODE range to a font family name, which means the font family  
will be applied for the characters with UNICODE in the range.  
    	
Characters in one block may need different fonts. One example is, in block "Currency Symbols",  
characters are currency symbols in different language, the fonts specified for which should be fonts  
for the corresponding languages. For these characters, special fonts can be specified for characters
one by one using <character> entry:

	<composite-font>
	    ......
		<character value="฿" font-family="Angsana New"/>
		<character value="\u0068" font-family="Time Roman"/>
	    ......
	</composite-font>

Note that characters are represented by attribute "value", which can be of two forms,   
the character itself or its UNICODE.  
  
A composite font named "all-fonts" will be applied as default font, that is, when a character is not   
defined in desired font, the font defined in "all-fonts" will be used.  
    
    
**Font path**
  
In this section, you can set the font path you want PDF layout engine to read, ranging from one font  
file to a whole font folder, for example:  
- add a font folder:  
  
	<path path="c:/windows/fonts"/>
	<path path="/usr/X11R6/lib/X11/fonts/special"/>

- add a font file:  

	<path path="c:/windows/fonts/Hanuman-Regular.ttf"/>
	<path path="/usr/X11R6/lib/X11/fonts/TTF/arial.ttf"/>

But please NOTE: If this section is set, PDF layout engine will ONLY load the font files in these paths  
and ignore the system defined font folder. If you want to use the system font folder as well, include it in this section.   
   	
On some systems, BIRT PDF layout engine may not recognize the system defined font folder.   
If you encounter this issue, add the font path to this section.  
   
   
**Font encodings**  
  
For `<font-encodings>` section, in most cases, you need not use this section,  
unless you are trying to use a non true-type font for PDF rendering.  

	<font-encodings>
		<encoding font-family="Times-Roman" encoding="Cp1252" />
		<encoding font-family="Helvetica" encoding="Cp1252" />
		<encoding font-family="Courier" encoding="Cp1252" />
		<encoding font-family="Symbol" encoding="Cp1252" />
		...
		<encoding font-family="STSong-Light" encoding="UniGB-UCS2-H" />
		<encoding font-family="STSongStd-Light" encoding="UniGB-UCS2-H" />
		<encoding font-family="MHei-Medium" encoding="UniCNS-UCS2-H" />
		<encoding font-family="MSung-Light" encoding="UniCNS-UCS2-H" />
		..
	</font-encodings>
   
   
**Font Kerning and Ligatures**  
  
The font handling of kerning and ligatures handling will be provided through the pdf library openPDF  
and the component "layout processor". This component provide several options to provide the special font handling.
THe kerning amnd ligatures are only supported it the font himself provide according glyph options.
  
The configuration file contains an option to enable the kerning and ligatures.  
This option can only be enabled or disabled (default: disabled).
  
- kerning and ligatures, enabled:  

	<kerning-and-ligatures enabled="true" />
	
- kerning and ligatures, disabled:  

	<kerning-and-ligatures enabled="false" />
