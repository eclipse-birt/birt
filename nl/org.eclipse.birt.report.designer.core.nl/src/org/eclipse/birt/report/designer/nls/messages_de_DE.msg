#/*******************************************************************************
# * Copyright (c) 2006, 2012 Actuate Corporation.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v2.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-2.0.html
# *
# * Contributors:
# *  Actuate Corporation - initial API and implementation
# *******************************************************************************/

#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

###########################################################
#AbstractDataSetWizard properties
AbstractDataSetWizard.ModelTrans.Create=Geben Sie den Namen, die Quelle und die Art des Datensatzes an.
###########################################################
#CssErrDialog
CssErrDialog.shellTitle.ImportCssStyleMssageTitle=Importmeldung

CssErrDialog.AreaTitle=CSS-Stile importieren – Meldung

CssErrDialog.AreaMessage=Beim Importieren von CSS-Stilen sind Fehler bzw. Warnungen aufgetreten.

CssErrDialog.Severity=Schweregrad:

CssErrDialog.Message=Meldung:

CssErrDialog.FatalError=Nicht behebbarer Fehler

CssErrDialog.Error=Fehler

CssErrDialog.Warning=Warnung

###########################################################
#AddGroupAction properties
AddGroupAction.actionMsg.addGroup=Gruppe einfügen...

AddGroupAction.Menu.Text=Gruppe einfügen...

AddGroupAction.stackMsg.addGroup=Gruppe einfügen

###########################################################
#AddLibraryAction properties
AddLibraryAction.Error.FileNotFound=Datei ''{0}'' wurde nicht gefunden.

AddLibraryAction.Error.FileIsNotLibrary=Datei ''{0}'' endet nicht mit ''{1}''.

AddLibraryAction.Error.Message=Die Datei ist bereits vorhanden.

AddLibraryAction.Error.Title=Fehler

AddLibraryAction.Text=Bibliothek hinzufügen...

AddLibraryAction.Dialog.Tilte=Bibliothek auswählen

###########################################################
#AddCSSAction properties
AddCSSAction.Text=CSS-Datei hinzufügen...

###########################################################
#AddResourceAction properties
AddResourceAction.Text=&Ressource hinzufügen...

AddResourceAction.Dialog.Tilte=Wählen Sie eine Ressource.

AddResourceAction.Error.Message=Die Datei ist bereits vorhanden.

AddResourceAction.Error.FileNotFound=Datei ''{0}'' wurde nicht gefunden.

AddResourceAction.Error.Title=Fehler

AddResourceAction.Error.FileIsNotResource=Datei ''{0}'' ist keine Ressource.

###########################################################
#AddCSSDialog
AddCSSWizardDialog.ShellText=CSS-Datei hinzufügen

AddCSSWizardDialog.TitleArea=CSS-Datei zu Ressourcenordner hinzufügen

AddCSSWizardDialog.Message=Wählen Sie eine CSS-Datei aus, um Stile in den Ressourcenordner zu importieren

###########################################################
#MoveResourceDialog
MoveResourceDialog.Title=Ressource verschieben

MoveResourceDialog.Message=Wählen Sie einen Ordner aus.

###########################################################
#NewLibraryDialog
NewLibraryDialog.Title=Neue Bibliothek

NewLibraryDialog.Message=Wählen Sie einen Ordner aus.

NewLibraryDialog.label.NewLibrary=Name der neuen Bibliothek

###########################################################
#PublishCSSAction properties
PublishCSSAction.wizard.errorTitle=Fehler

PublishCSSAction.wizard.message=Die CSS-Datei kann nicht in dem Verzeichnis veröffentlicht werden, in dem sie sich befindet. Wählen Sie zur Veröffentlichung ein anderes Verzeichnis.

PublishCSSAction.wizard.msgDirErr=Das Zielverzeichnis ist nicht vorhanden und kann nicht erstellt werden.

PublishCSSAction.wizard.message.SourceFileNotExist=Die Quelldatei ist nicht vorhanden.

PublishCSSAction.wizard.notvalidfolder=Der in der Einstellung definierte Ressourcenordner ist ungültig.


###########################################################
#ResourceAction properties
DeleteLibraryAction.Text=&Löschen

CopyLibraryAction.Text=&Kopieren

PasteLibraryAction.Text=&Einfügen

MoveLibraryAction.Text=&Verschieben...

RenameLibraryAction.Text=Umbe&nennen

NewFolderAction.Text=&Ordner

NewLibraryAction.Text=&Bibliothek

NewResource.MenuGroup.Text=&Neu

RenameResourceAction.nameExists=Eine Datei oder ein Ordner mit diesem Namen ist bereits vorhanden.

RenameResourceAction.inputDialogTitle=Ressourcen umbenennen

RenameResourceAction.inputDialogMessage=Neuer Name:

NewFolderAction.nameExists=Ein Ordner mit diesem Namen ist bereits vorhanden.

NewFolderAction.emptyName=Name darf nicht leer sein

NewFolderAction.inputDialogTitle=Neuer Ordner

NewFolderAction.inputDialogMessage=Ordnername:

DeleteResourceAction.Dialog.Title=Ressource löschen

DeleteResourceAction.Dialog.Message=Möchten Sie die ausgewählte Ressource wirklich löschen?

PasteResourceAction.Dialog.Title=Ressource einfügen

PasteResourceAction.ReadOnlyEncounter.Title=Schreibgeschützte Datei gefunden.

PasteResourceAction.ReadOnlyEncounter.Message=Datei '{0}' ist schreibgeschützt. Heben Sie zuerst den Schreibschutz auf.

PasteResourceAction.Dialog.Message=Ressource ist bereits vorhanden. Geben Sie einen &neuen Namen an:

PasteResourceAction.Dialog.FilenameSuffix.CopyOf=Kopie von

MoveResourceAction.Dialog.Title=Ressource verschieben

MoveResourceAction.Dialog.Message=Ressource ist bereits vorhanden. Ersetzen?

ResourceAction.ConvertToFile.URLIsNull=URL ist Null, Konvertierung in Datei nicht möglich.

ResourceAction.FileRemoveFailure=Fehler beim Entfernen von Datei(en).

LibraryNotExist.Dialog.Title=Bibliothek öffnen

LibraryNotExist.Dialog.Message=Die Bibliothek existiert nicht. Möchten Sie den Ressourcen-Explorer aktualisieren?

DesignerNotExist.Dialog.Title=Berichtdesign öffnen

DesignerNotExist.Dialog.Message=Das Berichtdesign existiert nicht. Möchten Sie den Ressourcen-Explorer aktualisieren?
###########################################################
#LibraryFileFilterAction properties
LibraryFileFilterAction.Text=Filter...

###########################################################
#AddStyleRuleAction properties
AddStyleAction.actionMsg.addStyleRule=&Neuer Stil...

AddStyleAction.stackMsg.addStyle=&Neuer Stil

###########################################################
#ApplyStyleAction properties
ApplyStyleAction.actionLabel.none=Keine

ApplyStyleAction.stackMsg.applyStyle=Stil anwenden

###########################################################
#ApplyThemeAction properties
ApplyThemeAction.actionLabel.none=Kein

ApplyThemeAction.stackMsg.applyStyle=Thema verwenden

###########################################################
#AttributesBuilder properties
AttributesBuilder.Label.Cell=Eigenschaftseditor - Zelle

AttributesBuilder.Label.Column=Eigenschaftseditor - Spalte

AttributesBuilder.Label.Data=Eigenschaftseditor - Daten

AttributesBuilder.Label.Generic=Eigenschaftseditor - {0}

AttributesBuilder.Label.Grid=Eigenschaftseditor - Raster

AttributesBuilder.Label.Label=Eigenschaftseditor - Beschriftung

AttributesBuilder.Label.Library=Eigenschaftseditor - Bibliothek

AttributesBuilder.Label.List=Eigenschaftseditor - Liste

AttributesBuilder.Label.Cube=Eigenschaftseditor - Quader

AttributesBuilder.Label.DynamicFilterParameter=Eigenschaftseditor - Dynamischer Filterparameter

AttributesBuilder.Label.MasterPage=Eigenschaftseditor - Master-Seite

AttributesBuilder.Label.None=Eigenschaftseditor - Keine

AttributesBuilder.Label.Picture=Eigenschaftseditor - Bild

AttributesBuilder.Label.Report=Eigenschaftseditor - Bericht

AttributesBuilder.Label.Row=Eigenschaftseditor - Zeile

AttributesBuilder.Label.Table=Eigenschaftseditor - Tabelle

AttributesBuilder.Label.Text=Eigenschaftseditor - Text

AttributesBuilder.Label.TextData=Eigenschafteneditor - Dynamischer Text

AttributesBuilder.Label.DataSourceHandle=Eigenschaftseditor - Datenquelle

AttributesBuilder.Label.VariableElementHandle=Eigenschaftseditor – Variable

AttributesBuilder.Label.DataSetHandle=Eigenschaftseditor - Datensatz

AttributesBuilder.Label.ScalarParameterHandle=Eigenschaftseditor - Parameter

AttributesBuilder.Label.TemplateReportItemHandle=Eigenschaftseditor - Berichtobjekt für Vorlage

AttributesBuilder.Label.AutoText=Eigenschaftseditor - AutoText

AttributesBuilder.Label.ParameterGroup=Eigenschaftseditor - Parametergruppe

AttributesBuilder.Label.CascadingParameterGroup=Eigenschaftseditor - Verschachtelte Parametergruppe

###########################################################
#AttributeView properties
AttributeView.dialg.Message.Warning=Warnung

AttributeView.dialg.Message.Yes=J&a

AttributeView.dialg.Message.No=N&ein

AttributeView.dialg.Message.Cancel=&Abbrechen

AttributeView.dialg.Message.PromptMsg=Möchten Sie wirklich alle lokalen Eigenschaften löschen?

AttributeView.toolbar.tooltip.EditingToggleAction.ClearMsg=Löschen Sie die lokalen Eigenschaften.

AttributeView.toolbar.tooltip.EditingToggleAction.EnableEditingMsg=Aktivieren Sie das Bearbeiten der Eigenschaften.

AttributeView.toolbar.tooltip.EditingToggleAction.DisabledActionMsg=Diese Aktion ist für lokale Elemente nicht verfügbar.

AttributeView.view.message.Emptypage= {0} ist eine Erweiterung von {1} in Bibliothek {2}. Um die Eigenschaften von {3} in diesem Berichtdesign zu bearbeiten, klicken Sie auf die Schaltfläche 'Bearbeiten' in der Symbolleiste des Eigenschaftseditors.

AttributeView.toolbar.tooltip.RestoreLibraryPropertiesAction.RestoreMsg=Eigenschaften von ursprünglichem Objekt aus Bibliothek wiederherstellen

AttributeView.defaultText.noAttributeView = Der Eigenschaftseditor ist nicht verfügbar.
###########################################################
#BackGroundPreferencePage properties
BackGroundPreferencePage.displayname.Title=Hintergrund

###########################################################
#BasAttributePage properties
BasAttributePage.Label.Attributes=Eigenschaften:

BaseAttributePage.CommandStack.ResetStyle.Message=Standardstil wiederherstellen

###########################################################
#BaseInsertMenuAction properties
BaseInsertMenuAction.stackMsg.insertElement=Element einfügen



###########################################################
#BasePaletteFactory properties
BasePaletteFactory.AutoText.Confidential=Vertraulich

BasePaletteFactory.AutoTextLabel.CreatedBy=Erstellt von:

BasePaletteFactory.AutoTextLabel.CreatedOn=Erstellt am:

BasePaletteFactory.AutoTextLabel.Filename=Dateiname:

BasePaletteFactory.AutoTextLabel.LastPrinted=Zuletzt ausgedruckt:

BasePaletteFactory.AutoTextLabel.Page=Seite:

BasePaletteFactory.AutoTextLabel.PageCount=Seitenzähler:

BasePaletteFactory.AutoTextLabel.PageXofY=Seite X von Y

BasePaletteFactory.categoryName.PointerSelect=Zeigerauswahl

BasePaletteFactory.categoryName.RectangleSelect=Rechteckauswahl

BasePaletteFactory.Group.Items=Elemente

BasePaletteFactory.toolTip.PointerSelect=Klicken Sie auf ein Element, um es auszuwählen, oder klicken Sie bei gedrückter Strg-Taste auf mehrere Elemente, um diese auszuwählen.

BasePaletteFactory.toolTip.RectangleSelect=Ziehen Sie mit gedrückter Maustaste ein Rechteck um mehrere Elemente, um diese auszuwählen.

###########################################################
#BaseStylePreferencePage properties
BaseStylePreferencePage.displayname.Title=Allgemein

###########################################################
#BgImageFieldEditor properties
BgImageFieldEditor.displayname.Browse=&Durchsuchen...

###########################################################
#BindingPage properties
BindingPage.Dataset.Label=Da&tensatz:

BindingPage.DataSetBindingSelector.Title.DataSet=Datensatzbindung

BindingPage.DataSetBindingSelector.Title.LinkModel=Datenmodellbindung:

BindingPage.ReportItem.Label=Ber&ichtobjekt:

BindingPage.ReportItem.NoName=Kein Name

BindingPage.Label.Binding=Bindung

BindingPage.MessageDlg.SaveParamBinding=ParamBinding speichern

BindingPage.None=Keine

BindingPage.TableColumn.DataType=Datentyp

BindingPage.TableColumn.Parameter=Parameter

BindingPage.TableColumn.Value=Wert

ColumnBingingDialog.Label.Warn=Das Ändern des Datensatzes oder des Berichtobjektverweises könnte zur Folge haben, dass alle Ausdrücke in den Ergebnisspalten ungültig werden.

ColumnBindingDialog.Label.DataSet=Datensatz:

ColumnBindingDialog.Column.Expression=Ausdruck

ColumnBindingDialog.DialogTitle=Datenbindung auswählen

ColumnBindingDialog.Column.Name=Name

ColumnBindingDialog.Column.Filter=Filter

ColumnBindingDialog.Column.Function=Funktion

ColumnBindingDialog.Column.DisplayName=Name anzeigen

ColumnBindingDialog.Column.DisplayNameID=Anzeigenamen-ID

ColumnBindingDialog.InputPrompt=<Hier den neuen Wert eingeben...>

ColumnBindingDialog.Button.Generate=Spaltenbindungen generieren

ColumnBindingDialog.Column.DataType=Datentyp

ColumnBindingDialog.Column.AggregateOn=Aggregat in

ColumnBindingDialog.All=Alles

ColumnBindingDialog.NONE=Ohne

ColumnBindingDialog.Choice.DatasetFromContainer=Datensatz des Containers

ColumnBindingDialog.Choice.ReportItemFromContainer=Verwiesenes Berichtobjekt des Containers

ColumnBindingDialog.AGGREGATEON.NONE=N/A
ColumnBindingDialog.Text.Add=&Hinzufügen...

ColumnBindingDialog.Text.Edit=&Bearbeiten...

ColumnBindingDialog.Text.Del=&Entfernen

ColumnBindingDialog.Text.Refresh=A&ktualisieren

ColumnBindingDialog.Text.AddAggr=A&ggregat hinzufügen...

###########################################################
#BlockPreferencePage properties
BlockPreferencePage.displayname.Title=Textblock

###########################################################
#BlockPreferencePage properties
SizePreferencePage.displayname.Title=Größe

###########################################################
#BorderCanvas properties
BorderCanvas.Label.Inherited=(Übernommen)

###########################################################
#BorderPreferencePage properties
BorderPreferencePage.displayname.Color=Farbe

BorderPreferencePage.displayname.Style=Stil

BorderPreferencePage.displayname.Title=Rahmen

BorderPreferencePage.displayname.Width=Breite

BorderPreferencePage.text.Bottom=Unten:

BorderPreferencePage.text.Left=Links:

BorderPreferencePage.text.Right=Rechts:

BorderPreferencePage.text.Top=Oben:

###########################################################
#BordersPage properties
BordersPage.Label.Borders=&Rahmen:

BordersPage.Label.Color=&Farbe:

BordersPage.Label.Presets=Voreinstellungen:

BordersPage.Label.Style=St&il:

BordersPage.Label.Width=&Breite:

BordersPage.Tooltip.All=Alle Rahmen

BordersPage.Tooltip.Bottom=Unterer Rahmen

BordersPage.Tooltip.Left=Linker Rahmen

BordersPage.Tooltip.None=Keine

BordersPage.Tooltip.Right=Rechter Rahmen

BordersPage.Tooltip.Top=Oberer Rahmen

BordersPage.text.Preview=Vorschau:

BordersPage.Trans.SelectBorder=Rahmen auswählen

BordersPage.Trans.UnSelectBorder=Auswahl des Rahmens aufheben

BordersPage.Trans.SelectAllborders=Alle Rahmen auswählen

BordersPage.Trans.UnSelectAllborders=Auswahl aller Rahmen aufheben

###########################################################
#BoxPreferencePage properties
BoxPreferencePage.displayname.Title=Feld

BoxPreferencePage.text.Margin=Rand

BoxPreferencePage.text.Padding=Umrandungsfüllung

###########################################################
#CommentsPreferencePage properties
CommentsPreferencePage.displayname.Title=Kommentare

###########################################################
#ButtonPaneComposite properties
ButtonPaneComposite.tooltip.MasterPage=Master-Seite

ButtonPaneComposite.tooltip.Normal=Normal

ButtonPaneComposite.tooltip.Ruler=Regler

###########################################################
#SettingsPage properties
SettingsPage.dataFetchSetting.label=&Maximale Anzahl der aus Datenquelle abzurufenden Zeilen:

SettingsPage.CachePreference.button.clear=&Cache löschen

SettingsPage.CachePreference.checkBox.text=&Daten aus Datensatz für Berichtvorschau zwischenspeichern

SettingsPage.CachePreference.description=Cache-Größe festlegen:

SettingsPage.CachePreference.Filter.Tooltip=Cache in Datensatz importieren oder daraus exportieren

SettingsPage.CachePreference.label.text=Zeilen verwenden

SettingsPage.CachePreference.messageBox.info=Der Cache wurde gelöscht

SettingsPage.CachePreference.messageBox.title=Cache gelöscht

SettingsPage.CachePreference.radioBox.entireNumText=&Kompletten Datensatz zwischenspeichern

SettingsPage.CachePreference.radioBox.numText=&Zwischenspeichern und zuerst verwenden

###########################################################
#CascadingParametersDialog properties
CascadingParametersDialog.Button.isRequired=er&forderlich

CascadingParametersDialog.default.preview.string=Beispielzeichenfolge

CascadingParametersDialog.display.controlType.comboBox=Kombinationsfeld

CascadingParametersDialog.display.controlType.listBox=Listenfeld

CascadingParametersDialog.Label.button.changeFormat=&Ändern...

CascadingParametersDialog.Label.button.createNew.dataset=&Neu erstellen...

CascadingParametersDialog.Label.cascadingParam.name=Name der Verschachtelungsparamete&rgruppe:

CascadingParametersDialog.Label.column.displayText=Angezeigter Text

CascadingParametersDialog.Label.column.dataSet=Datensatz

CascadingParametersDialog.Label.column.name=Name

CascadingParametersDialog.Label.column.value=Wert

CascadingParametersDialog.Label.createNewParam=<Neuen Parameter erstellen...>

CascadingParametersDialog.Label.dataSets=&Datensatz:

CascadingParametersDialog.Label.dataType=Datent&yp

CascadingParametersDialog.Label.defaultValue=&Standardwert:

CascadingParametersDialog.Label.displayType=An&zeigetyp:

CascadingParametersDialog.Label.formatAs=Formatieren als:

CascadingParametersDialog.Label.group.general=Allgemein

CascadingParametersDialog.Label.group.moreOptions=Weitere Optionen

CascadingParametersDialog.Label.group.properties=Eigenschaften

CascadingParametersDialog.Label.helpText=&Hilfetext:

CascadingParametersDialog.Label.listLimit=&Listenbegrenzung:

CascadingParametersDialog.Label.NoColumnAvailable=<Keine Spalte verfügbar>

CascadingParametersDialog.Label.param.name=&Name:

CascadingParametersDialog.Label.parameters=Parameter

CascadingParametersDialog.Label.preview=Vorschau mit Format

CascadingParametersDialog.Label.groupPromptText=&Aufforderungstext:

CascadingParametersDialog.Label.parameterPromptText=Aufforderungste&xt:

CascadingParametersDialog.Label.selectDataSet=<Datensatz auswählen>

CascadingParametersDialog.Label.selectDisplayColumn=<Anzeigenspalte nicht eingestellt>

CascadingParametersDialog.Label.selectValueColumn=<Wertespalte nicht eingestellt>

CascadingParametersDialog.Label.values=Werte

CascadingParametersDialog.Radio.Single=Ei&nfacher Datensatz

CascadingParametersDialog.Radio.Mutli=&Mehrfacher Datensatz

CascadingParametersDialog.Choice.NullValue=Null-Wert

CascadingParametersDialog.Choice.BlankValue=Leerwert

CascadingParametersDialog.Choice.SelectValue=<Wert auswählen ...>

CascadingParametersDialog.Button.Add=&Hinzufügen

CascadingParametersDialog.Button.Edit=Bearbe&iten

CascadingParametersDialog.Button.Delete=&Löschen

CascadingParametersDialog.Button.IsMultiple=Mehrere &Werte zulassen

CascadingParametersDialog.DefalutValue.Add=&Hinzufügen

CascadingParametersDialog.ExpressionEditor.Title=Ausdruck bearbeiten

CascadingParametersDialog.Title.AddCascadingParameter=Verschachtelungsparametergruppe hinzufügen

CascadingParametersDialog.Title.EditCascadingParameter=Verschachtelungsparametergruppe bearbeiten

CascadingParametersDialog.items.None=<keine>

###########################################################
#CellPageGenerator properties
CellPageGenerator.List.Borders=Rahmen

CellPageGenerator.List.CellPadding=Umrandungsfüllung

CellPageGenerator.List.Font=Schriftart

CellPageGenerator.List.General=Allgemein


###########################################################
#ChoiceSetFactory properties
ChoiceSetFactory.choice.Auto=Automatisch

ChoiceSetFactory.choice.None=Keine

###########################################################
#CodePageAction properties
CodePageAction.actionText.code=Skript

###########################################################
#ColorBuilder properties
ColorBuilder.text.Auto=Automatisch

ColorBuilder.Button.ChooseColor=Farbfeld öffnen

ColorSelector.ColorDialog.Title=Farbe

ColorSelector.Name=Farbauswahl
###########################################################
#ColumnPageGenerator properties
ColumnPageGenerator.List.Borders=Rahmen

ColumnPageGenerator.List.CellPadding=Zellfüllung

ColumnPageGenerator.List.Font=Schriftart

ColumnPageGenerator.List.General=Allgemein

ColumnPageGenerator.List.Visibility=Sichtbarkeit

###########################################################
#Commom properties
Commom.ShortDateTime.Fri=FR

Commom.ShortDateTime.Mon=MO

Commom.ShortDateTime.Sat=SA

Commom.ShortDateTime.Sun=SO

Commom.ShortDateTime.Thu=DO

Commom.ShortDateTime.Tue=DI

Commom.ShortDateTime.Wed=MI

###########################################################
#CopyAction properties
CopyAction.text=&Kopieren

###########################################################
#CreatePlaceHolderAction properties
CreatePlaceHolderAction.text=Berichtobjekt für Vorlage er&stellen...

###########################################################
#CutAction properties
CutAction.text=A&usschneiden

CutAction.trans=Elemente ausschneiden

###########################################################
#dataBinding properties
dataBinding.label.selectBinding=Datenbindung auswählen:

dataBinding.message.changeDataSet=Sie sind im Begriff, die Datenbindung des Elements zu ändern.\n\nMöchten Sie alle bestehenden Datenfeldinformationen löschen?

dataBinding.message.changeReference=Sie sind im Begriff, die Datenbindung des Elements zu ändern.\n\nMöchten Sie den Vorgang ausführen?

dataBinding.title.changeDataSet=Datensatz ändern?

dataBinding.title=Datenbindung

dataBinding.message.haveNoName=Das ausgewählte Element hat keinen Namen.\n\nSie müssen dem Element einen Namen geben, um es auswählen zu können.

dataBinding.title.haveNoName=Kein Name?

dataBinding.button.OK=&OK

parameterBinding.title=Parameter&bindung des Datensatzes...

ParameterBindingDialog.title=Parameterbindung des Datensatzes

dataBinding.title.generateOutputParam=Bindungen generieren?

dataBinding.msg.generateOutputParam=Der Datensatz verfügt über mindestens einen Ausgabeparameter.\n\nSollen für diese(n) Parameter Bindungen generiert werden?

###########################################################
#DataEditPart properties
DataEditPart.Figure.Dafault=Daten

DataEditPart.Figure.Expression=Ausdruck

DataEditPart.stackMsg.edit=Datenbindung bearbeiten

DataEditPart.text.AggregateOn=Aggregat in

###########################################################
#DataEditPart properties
DestroyEditPart.Message=Der Inhalt dieses Berichtobjekts ist beschädigt. Wenn Sie weiter mit diesem Bericht arbeiten möchten, sollten Sie zuerst dieses Objekt löschen. Anderenfalls könnte die Berichtdesigndatei beschädigt werden, oder der Report Designer könnte abstürzen.

###########################################################
#DataPageGenerator properties
DataPageGenerator.List.Bookmark=Lesezeichen

DataPageGenerator.List.Borders=Rahmen

DataPageGenerator.List.Duplicates=Duplikate

DataPageGenerator.List.Expression=Ausdruck

DataPageGenerator.List.Format=Format

DataPageGenerator.List.formatDateTime=Format Datum/Uhrzeit

DataPageGenerator.List.formatNumber=Format Zahl

DataPageGenerator.List.formatString=Format Zeichenfolge

DataPageGenerator.List.General=Allgemein

DataPageGenerator.List.Highlights=Kennzeichnungen

DataPageGenerator.List.HyperLink=Hyperlink

DataPageGenerator.List.Margin=Rand

DataPageGenerator.List.Padding=Umrandungsfüllung

DataPageGenerator.List.Section=Seitenumbruch

DataPageGenerator.List.TOC=Inhaltsverzeichnis

DataPageGenerator.List.Visibility=Sichtbarkeit

###########################################################
#dataset properties
dataset.edit=Datensatz bearbeiten - {0}

dataset.editor.settings=Einstellungen

dataset.editor.settings.dataFetchSetting.errorNumberFormat=Geben Sie für die maximal abzurufende Zeilenanzahl eine Ganzzahl ein, die größer als Null ist.

dataset.editor.settings.dataFetchSetting.errorNumberFormatForResultSet=Geben Sie für die Ergebnisnummer eine positive Ganzzahl ein.

dataset.editor.settings.dataFetchSetting=Einstellung für den Abruf von Daten

dataset.editor.settings.resultsetselection.resultSetSelection=Ergebnisauswahl

dataset.editor.settings.resultsetselection.enableResultSetSelection=Ergebnisauswahl &aktivieren

dataset.editor.settings.resultsetselection.selectResultSetByName=&Ergebnisauswahl nach Namen:

dataset.editor.settings.resultsetselection.selectResultSetByNumber=Ergebnisaus&wahl nach Nummer:

dataset.editor.settings.dataFetchSetting.fetchAll=&Alle Zeilen aus Datenquelle abrufen.

dataset.editor.settings.cachePreference.errorNumberFormat=Geben Sie für die Zeilengröße des Caches eine Zahl ein, die größer als Null ist.

dataset.editor.settings.cachePreference=Voreinstellungen für Cache

dataset.editor.computedColumns=Berechnete Spalten

dataset.editor.dataSource.Tooltip=Datenquelle anzeigen/ändern.

dataset.editor.dataSource=Datenquelle

dataset.editor.error.columnOrAliasNameAlreadyUsed=Der neue Spaltenname oder -alias ({0}) wird bereits von der Spalte bei Index {1} verwendet.

dataset.editor.error.computedColumnNameAlreadyUsed=Der neu berechnete Spaltenname ({0}) wird unter "Ausgabespalten" bereits als Spaltenalias für Spalte ({1}) verwendet.

dataset.editor.error.duplicateParameterName=Parameternamen doppelt vergeben.

dataset.editor.error.nonemptyDefaultvalue=Standardwert darf nicht leer sein.

dataset.editor.error.emptyName=Der Name darf nicht leer sein.

dataset.editor.error.missingComputedColumnExpression=Der Ausdruck der berechneten Spalte {0} darf nicht leer oder gleich Null sein.

dataset.editor.error.missingComputedColumnName=Der berechnete Spaltenname darf nicht leer oder gleich Null sein.

dataset.editor.error.noDataSource=Keine Datenquelle verfügbar für den Datensatz {0}; wählen Sie einen Datenquellen-Eintrag in der Eigenschaftenansicht aus.

dataset.editor.error.nonmatchedDataSource=Datenquelle ohne Übereinstimmung für Datensatz '{0}'. Wählen Sie eine Datenquelle aus, die mit dem Datensatztyp '{1}' übereinstimmt.

dataset.editor.error.noEditorsFound=Keinen Editor für Datensatz gefunden {0}.

dataset.editor.error.noInputParameterDefaultValue=Eingabeparameter {0} muss einen Standardwert aufweisen, der ungleich Null ist.

dataset.editor.error.title=Warnung

dataset.editor.error.unsupportedDataSet=Nicht unterstützter Datensatztyp - {0}.

dataset.editor.error.validationParameter=Für den Eingabeparameter [{0}] liegt kein Wert vor. Soll der Parameter ohne Wert bleiben?

dataset.editor.filters=Filter

dataset.editor.label.selectDataSource=&Datenquelle wählen:

dataset.editor.outputColumns=Ausgabespalten

dataset.editor.parameters=Parameter

dataset.editor.preview=Ergebnisvorschau

dataset.editor.outputparameters=Vorschau der Ausgabeparameter anzeigen

dataset.editor.title.alias=Alias

dataset.editor.title.columnName=Spaltenname

dataset.editor.title.dataType=Datentyp

dataset.editor.title.defaultValue=Standardwert

dataset.editor.title.direction=Richtung

DataSetParameterPage.editor.title.linkToSalarParameter=Mit Berichtparameter verknüpft

dataset.editor.title.displayName=Name anzeigen

dataset.editor.title.displayNameKey=Namensschlüssel anzeigen

dataset.editor.title.expression=Ausdruck

dataset.editor.title.aggrFunc=Aggregat

dataset.editor.title.aggrArgu=A&rgument

dataset.editor.title.filter=Filter

dataset.editor.title.helpText=Hilfetext

dataset.editor.title.isInput=Ist Eingabe

dataset.editor.title.isNullable=Nullwert zulässig

dataset.editor.title.isOptional=Optional

dataset.editor.title.isOutput=Ist Ausgabe

dataset.editor.title.name=Name

dataset.editor.title.nativeName=Programmeigener Name

dataset.editor.title.operator=Operator

dataset.editor.title.type=Typ

dataset.editor.title.value1=Wert 1

dataset.editor.title.value2=Wert 2

dataset.editor.inputDialog.displayName=&Anzeigename

dataset.editor.inputDialog.displayNameKey=Namens&schlüssel anzeigen

dataset.editor.inputDialog.name=&Name

dataset.editor.inputDialog.nativeName=Progr&ammeigener Name

dataset.editor.inputDialog.type=&Typ

dataset.editor.inputDialog.alias=&Alias

dataset.editor.inputDialog.columnName=&Spaltenname

dataset.editor.inputDialog.dataType=Daten&typ

dataset.editor.inputDialog.expression=&Ausdruck

dataset.editor.inputDialog.aggrArgu=A&rgument

dataset.editor.inputDialog.aggrFunc=A&ggregat

dataset.editor.inputDialog.filter=&Filter

dataset.editor.inputDialog.value1=Wert &1

dataset.editor.inputDialog.value2=Wert &2

dataset.editor.inputDialog.operator=&Operator

dataset.editor.inputDialog.defaultValue=Standard&wert

dataset.editor.inputDialog.direction=&Richtung

dataset.editor.historybar.backward=Zurück

dataset.editor.historybar.forward=Vor

dataset.editor.historybar.to=nach

dataset.error.noDataSources=Es ist keine Datenquelle verfügbar. Möchten Sie jetzt eine Datenquelle hinzufügen?

dataset.error.dataSourceMissing=Die Datenquelle dieses Datensatzes ist nicht vorhanden.

dataset.error.title.noDataSources=Keine Datenquellen verfügbar.

dataset.message.create=Erstellen Sie einen neuen Datensatz.

dataset.new.defaultName=Datensatz

dataset.new=Neuer Datensatz

dataset.action.new=Neuer Daten&satz

dataset.join.new=Neuer Verknüpfungsdatensatz

dataset.action.join.new=Neuer &Verknüpfungsdatensatz

dataset.resultset.preview.tooltip=Abfrageergebnis-Vorschau anzeigen.

dataset.resultset.preview.promptMessage.recordsNum=Insgesamt {0} Datensätze angezeigt.

dataset.resultset.preview.promptMessage.MoreRecordsExist=Das Ergebnis beinhaltet mehr Datensätze, die nicht angezeigt werden.

dataset.outputparameters.preview.tooltip=Ausgabeparameter-Vorschau anzeigen.

dataset.wizard.label.datasetName=&Datensatzname:

dataset.wizard.label.datasetType=&Datensatztyp:

dataset.wizard.label.dataSource=&Datenquelle:

dataSourceSelectionPage.title=Verknüpfung mit Datenquelle.

dataset.error.title.noReportDesign=Fehler

dataset.error.msg.noReportDesign=Kein Berichtdesign verfügbar.

dataset.warning.invalidReponseStatus=Ungültige Designantwort von der benutzerdefinierten ODA-Seite. Antwortstatus der ODA-Designsitzung: {0}.

###########################################################
#DataSetBasePage properties
DataSetBasePage.tooltip=Name darf nicht leer sein.

DataSetBasePage.ScriptedDataSet.name=Geskripteter Datensatz

DataSetBasePage.ScriptedDataSource.name=Geskriptete Datenquelle

DataSetBasePage.Group.DataSourceSelection=Datenquellenauswahl

DataSetBasePage.CassandraScriptedDataSet.name=Geskripteter Cassandra-Datensatz

DataSetBasePage.CassandraScriptedDataSource.name=Geskriptete Cassandra-Datenquelle

DataSetBindingSelector.Button.DeselectAll=&Auswahl aufheben

DataSetBindingSelector.Button.SelectAll=&Alles markieren

DataSetBindingSelector.Combo.DataSet=&Datensatz:

DataSetBindingSelector.Label.SelectBindingColumns=Wählen Sie Bindungsspalten aus.

DataSetBindingSelectorPage.Label.SelectBindingColumns=Wählen Sie Bindungsspalten aus.

DataSetBindingSelectorPage.Title=Datensatzbindung

CassandraScriptedDataSource.display.name=Geskriptete Cassandra-Datenquelle

CassandraScriptedDataSource.error.classNotFound=Hector Clientbibliothek JARs zur Unterstützung der geskripteten Datenquelle Cassandra wurden nicht gefunden.

###########################################################
#JointDataSetPage properties
JointDataSetPage.pageName=Verknüpfungsdatensatz

JointDataSetPage.page.detail=Wählen Sie einen Datensatz und eine Spalte von jeder Seite aus, um einen Knotenpunkt zu definieren.

JointDataSetPage.button.innerJoin=&Inner Join

JointDataSetPage.button.leftJoin=&Left Outer Join

JointDataSetPage.button.rightJoin=&Right Outer Join

JointDataSetPage.button.fullJoin=Full &Outer Join

JointDataSetPage.joinType=Knotenpunktarten

JointDataSetPage.error.nodataset.title=Kein Datensatz gefunden.

JointDataSetPage.error.nodataset.right=Der rechte Datensatz wurde nicht gefunden.

JointDataSetPage.error.nodataset.left=Der linke Datensatz wurde nicht gefunden.

JointDataSetPage.pageDescription=Verknüpfungsdatensatz definieren

JointDataSetPage.query=Abfrage

###########################################################
#DerivedDataSet properties
DerivedDataSet.action.error.message.NoDataSet=Kein verfügbarer Datensatz gefunden.

DerivedDataSet.action.error.title.createDerivedDataSet=Fehler beim Erstellen von abgeleitetem Datensatz.

DerivedDataSet.action.error.message.InvalidNumber.DataSet=Die Anzahl der verfügbaren Datensätze ist geringer als zwei.

###########################################################
#DataSetComputedColumnsPage properties
DataSetComputedColumnsPage.ComputedColumns.Tooltip=Geben Sie die berechneten Spalten an.

DataSetComputedColumnsPage.description=Berechnete Spalten definieren:

DataSetComputedColumnsPage.duplicatedName=Doppelter Spaltenname.

DataSetComputedColumnsPage.numberName=Der Spaltenname darf keine Zahl sein.

DataSetComputedColumnsPage.toolTipText.Down=Berechnete Spalte nach unten verschieben.

DataSetComputedColumnsPage.toolTipText.Up=Berechnete Spalte nach oben verschieben.

DataSetComputedColumnsPage.toolTipText.Remove=Berechnete Spalte entfernen.

DataSetComputedColumnsPage.toolTipText.New=Neue berechnete Spalte.

DataSetComputedColumnsPage.toolTipText.Edit=Berechnete Spalte bearbeiten.

DataSetComputedColumnsPage.InputDialog.title.New=Neue berechnete Spalte

DataSetComputedColumnsPage.InputDialog.title.Edit=Berechnete Spalte bearbeiten

DataSetComputedColumnsPage.InputDialog.message.error.AggregationExpression=Der Aggregatausdruck darf keine aggregatberechnete Spalte verwenden!

DataSetComputedColumnsPage.title.ComputedColumnInputDialog=Berechnete Spalte

###########################################################
#DataSetFiltersPage properties
DataSetFiltersPage.description=Filter definieren:

DataSetFiltersPage.exception.msg.operator.notSupport=Der folgende Operator wird nicht unterstützt:

DataSetFiltersPage.Filter.Tooltip=Datenzeilen nach BIRT-Ausdrücken filtern.

DataSetFiltersPage.toolTipText.Down=Filter nach unten verschieben.

DataSetFiltersPage.toolTipText.Up=Filter nach oben verschieben.

DataSetFiltersPage.toolTipText.Remove=Filter entfernen.

DataSetFiltersPage.toolTipText.New=Neuer Filter.

DataSetFiltersPage.toolTipText.Edit=Filter bearbeiten.

DataSetFiltersPage.title.FilterInputDialog=Filter

DataSetFiltersPage.messages.selectVauleUnavailable=Wertliste mit angegebenem Ausdruck nicht verfügbar.

DataSetFiltersPage.DynamicFilter.DynamicParameter.NotFound=Der verknüpfte dynamische Parameter '{0}' des dynamischen Filters ist nicht vorhanden.

###########################################################
#DataSetNodeProvider properties
DataSetNodeProvider.menu.text=&Bearbeiten

###########################################################
#DataSetParametersPage properties
DataSetParametersPage.description=Eingabeparameter definieren:

DataSetParametersPage.Filter.Tooltip=Parameter in Datensatz importieren oder daraus exportieren.

DataSetParameterBindingInputDialog.Title=Parameterbindung des Datensatzes bearbeiten

DataSetParametersPage.ErrorTitle.InvalidInput=Ungültige Eingabe.

DataSetParameterBindingInputDialog.Label.Name=Parametername:

DataSetParametersPage.ErrorMessage.CanootBeNull=Der Wert darf nicht Null sein.

DataSetParameterBindingInputDialog.Title.NewParameter=Neuer Parameter

DataSetParameterBindingInputDialog.Title.EditParameter=Parameter bearbeiten

DataSetParameterBindingInputDialog.Label.DataType=Datentyp:

DataSetParameterBindingInputDialog.Label.Value=&Wert:

DataSetParametersPage.ParameterButton.Tooltip.Edit=Berichtparameter bearbeiten

DataSetParametersPage.ParameterButton.Tooltip.New=Neuer Berichtparameter

DataSetParametersPage.Prompt.new=<New param here...>

DataSetParametersPage.default.None=N/A
DataSetParametersPage.reportParam.None=Ohne

DataSetParametersPage.errorMessage.LinkedReportParamNotFound=Der verknüpfte Berichtparameter "{0}" des Datensatzparameters "{1}" ist nicht vorhanden.

DataSetParametersPage.errorMessage.InvalidType.LinkedReportParam=Wenn ein Benutzer mehrere Werte für den Berichtsparameter festlegt, verwendet der Datensatzparameter nur den ersten Wert.

DataSetParameterPage.cell.linkToSalarParameter=Mit Berichtparameter &verknüpft

DataSetParameterPage.title.ParameterInputDialog=Parameter

DataSetParameterPage.toolTipText.New=Neuer Parameter

DataSetParameterPage.toolTipText.Edit=Parameter bearbeiten

DataSetParameterPage.toolTipText.Remove=Parameter entfernen

DataSetParameterPage.toolTipText.Up=Parameter nach oben verschieben

DataSetParameterPage.toolTipText.Down=Parameter nach unten verschieben

DataSetParameterPage.WarningMessage.updatePosition=Einige oder alle Datensatzparameter besitzen ungültige Positionswerte. Die Positionen werden auf Grundlage der Folgen aktualisiert.

DataSetParameterPage.warning.UnmatchedParamDataType=Parametertyp des Datensatzes stimmt nicht mit Parametertyp des verknüpften Berichts überein.

DataSetParameterPage.multiValuesType=Typ 'Mehrere Werte'

DataSetParameterPage.warningLinkedMultipleValuesParams.title=Dialogfeld 'Warnung'

DataSetParameterPage.updateReportParameter.title=Einstellung zur Aktualisierung der Berichtsparameter

DataSetParameterPage.updateReportParameter.message=Möchten Sie den Parameter des verknüpften Berichts mit den Einstellungen des Datensatzparameters aktualisieren?

DataSetParameterPage.updateReportParameter.promptButtonYes=Ja

DataSetParameterPage.updateReportParameter.promptButtonNo=Nein

DataSetParameterPage.updateReportParameter.propmtText=Antwort merken

PromptParameterDialog.title=Aktualisierung von Designattributen bestätigen

PromptParameterDialog.okButton.text=Übernehmen

PromptParameterDialog.parameterGroup.title=Parameterdesignattribute

PromptParameterDialog.parameterGroup.label=Ausgewählte Parameter mit Attributwert(en) aktualisieren, die durch bearbeitete Datensatzdesigns überarbeitet wurden.

PromptParameterDialog.parameterGroup.nameColumn=Name

PromptParameterDialog.parameterGroup.previousValue=Vorheriger Wert

PromptParameterDialog.parameterGroup.revisedValue=Überarbeiteter Wert

###########################################################
#PropertyBindingpage properties
PropertyBindingPage.datasource.property.empty=Für diese Datenquelle ist keine öffentliche Eigenschaft durch die ODA-Erweiterung definiert.

PropertyBindingPage.dataset.property.empty=Für diesen Datensatz ist keine öffentliche Eigenschaft durch die ODA-Erweiterung definiert.

PropertyBindingPage.dataset.queryText=Abfrage&text

PropertyBindingPage.property.tooltip=Eigenschaftenbindung für Laufzeit definieren.

PropertyBindingPage.button.tooltip=Ausdruckerstellung zum Eingeben des Werts von {0} öffnen.

PropertyBindingPage.label.colon=:
PropertyBindingPage.label.LeftParenthesis=(
PropertyBindingPage.label.RightParenthesis=)

###########################################################
#DataSetProcess properties
DataSetProcess.action.Delete=Datensatz löschen

DataSetProcess.action.Edit=Datensatz bearbeiten

DataSetProcess.action.Preview=Vorschau

DataSetProcess.text.DisplayName=DataSet

DataSetProcess.text.Prefix=Die folgenden Datensätze verwenden diese Datenquelle:

DataSetProcess.text.Show=Blendet die Datensatzeditor-Seite für den Datensatz ein.

DataSetProcess.text.Suffix=Möchten Sie dieses Element wirklich löschen?

###########################################################
#datasource properties
datasource.action.edit=&Bearbeiten

datasource.edit=Datenquelle bearbeiten - {0}

datasource.new.defaultName=Datenquelle

datasource.new=Neue Datenquelle

datasource.action.new=Neue Datenq&uelle

datasource.wizard.label.dataSourceName=&Datenquellenname:

datasource.wizard.label.datasources=Aus einem &Datenquellentyp aus der folgenden Liste erstellen

datasource.wizard.label.connectionfile=Aus einem &Verbindungsprofil im Profilspeicher erstellen

datasource.wizard.message.selectType=Geben Sie alle Einstellungen für eine neue Datenquelle an, oder wählen Sie ein vordefiniertes Verbindungsprofil.

datasource.wizard.title.select=Wählen Sie einen Datenquellentyp, oder wählen Sie ein Verbindungsprofil.

datasource.editor.property=Eigenschaftenbindung

datasource.editor.duplicatedName=Datenquellenname "{0}" ist bereits vorhanden.

datasource.error.title.noReportDesign=Fehler

datasource.error.msg.noReportDesign=Kein Berichtdesign verfügbar.

datasource.exprotToCP.title=Exportieren

datasource.exportToCP.message=Sie sind dabei, '{0}' in den Datenquellen-Explorer zu exportieren.

datasource.exportToCP.checkBox=&Verbindungsprofilspeicher für die exportierte Datenquelle erstellen

datasource.exportToCP.externalCheckBox=Externalisiertes Verbind&ungsprofil in der Datenquelle verwenden

datasource.exportToCP.label.specifyName=V&erbindungsprofilname:

datasource.exportToCP.errorBox.button.OK=OK

datasource.exportToCP.error.emptyFileName=Der Verbindungsprofilname darf nicht leer sein.

datasource.exportToCP.error.invalidFileName=Der angegebene Verbindungsprofilname enthält mindestens ein ungültiges Zeichen.

datasource.exportToCP.error.duplicatedFileName=Der Verbindungsprofilname existierte bereits!

datasource.exportToCP.error.alreadyExported=Die Eigenschaften der Datenquelle '({0})' wurden bereits externalisiert.


###########################################################
#DataSourceProcess properties
DataSourceProcess.action.Delete=Datenquelle löschen

DataSourceProcess.action.Edit=Datenquelle bearbeiten

DataSourceProcess.text.DisplayName=DataSource
DataSourceProcess.text.Prefix=Die folgenden Datensätze verwenden diese Datenquelle

DataSourceProcess.text.Show=Blendet die Datensatzeditor-Seite ein

DataSourceProcess.text.Suffix=Möchten Sie dieses Element wirklich löschen?

###########################################################
#DataSourceSelectionPage properties
DataSourceSelectionPage.ScriptDataSource.DisplayName=Geskriptete Datenquelle

###########################################################
#DataSourcesSlotOutlineProcess properties
DataSourcesSlotOutlineProcess.action.New=Neue Datenquelle

###########################################################
#datatypes properties
datatypes.any=Beliebig

datatypes.auto=Automatisch

datatypes.dateTime=Datum/Uhrzeit

datatypes.decimal=Decimal

datatypes.float=Gleitkomma

datatypes.integer=Ganzzahl

datatypes.string=Zeichenfolge

###########################################################
#DataView properties
DataView.defaultText.noDataView=Keine Datenansicht verfügbar.

###########################################################
#DataViewTreeViewerPage properties
DataViewTreeViewerPage.tooltip.DoubleClickToEdit=Zum Bearbeiten doppelklicken

DataViewTreeViewerPage.tooltip.DragToInsertColumn=Spalte durch Ziehen einfügen

DataViewTreeViewerPage.tooltip.DragToInsertDataSetColumns=Sämtliche Datensatzspalten durch Ziehen einfügen

DataViewTreeViewerPage.tooltip.DragToInsertParameter=Berichtparameter durch Ziehen einfügen;

###########################################################
#DebugUtil properties
DebugUtil.importProject.fail="Import des vorhandenen Projekts fehlgeschlagen."

###########################################################
#DecorationFieldEditor properties
DecorationFieldEditor.text.Linethrough=Durchgestrichen

DecorationFieldEditor.text.Overline=Überstrichen

DecorationFieldEditor.text.Underline=Unterstreichen

###########################################################
#DefaultNodeProvider properties
DefaultNodeProvider.Dlg.Confirm=Klicken Sie auf 'Ja', um fortzufahren und den {0} zu löschen. Klicken Sie andernfalls auf 'Nein'.

DefaultNodeProvider.ParameterGroup.ConfirmMessage=Sie sind im Begriff, eine Parametergruppe zu löschen. Wenn Sie diese Parametergruppe löschen, werden alle Parameter innerhalb der Gruppe gelöscht. Möchten Sie fortfahren?

DefaultNodeProvider.ParameterGroup.ConfirmTitle=Löschen der Parametergruppe bestätigen

DefaultNodeProvider.Tree.Body=Hauptteil

DefaultNodeProvider.Tree.Clients={0} wird von folgenden Elementen verwendet

DefaultNodeProvider.Tree.ColumnHedings=Spaltenüberschriften

DefaultNodeProvider.Tree.Components = Berichtobjekte

DefaultNodeProvider.Tree.DataSets=Datensätze

DefaultNodeProvider.Tree.DataSources=Datenquellen

DefaultNodeProvider.Tree.Detail=Detail

DefaultNodeProvider.Tree.Footer=Fußzeile

DefaultNodeProvider.Tree.Groups=Gruppen

DefaultNodeProvider.Tree.Header=Kopfzeile

DefaultNodeProvider.Tree.Images=Eingebettete Bilder

DefaultNodeProvider.Tree.Invalid=Ungültiger Knoten

DefaultNodeProvider.Tree.MasterPages=MasterPages
DefaultNodeProvider.Tree.PageSetup=Seite einrichten

DefaultNodeProvider.Tree.Parameters=Berichtparameter

DefaultNodeProvider.Tree.Reference=Warnung

DefaultNodeProvider.Tree.Scratch=Skizzenblock

DefaultNodeProvider.Tree.Styles=Stile

DefaultNodeProvider.Tree.Themes=Themen

DefaultNodeProvider.WarningDialog.Title=Warnung

DefaultNodeProvider.WarningDialog.EmptyList=Kein Element zum Einfügen verfügbar.

DefaultNodeProvider.stackMsg.changeBinding=Datenbindung ändern

###########################################################
#DeleteAction properties
DeleteAction.text=&Löschen

DeleteAction.trans=Elemente löschen

###########################################################
#DeleteColumnAction properties
DeleteColumnAction.actionMsg.delete=&Löschen

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.actionMsg.deleteGroup=&Gruppe löschen

###########################################################
#DeleteListGroupAction properties
DeleteListGroupAction.actionMsg.deleteGroup=&Gruppe löschen

###########################################################
#DeleteRowAction properties
DeleteRowAction.actionMsg.delete=&Löschen

###########################################################
#designer properties
designer.preview.browserTitle=Vorschau

designer.preview.preference.browser.autoSave=Automatisch vor Vorschau speichern

designer.preview.preference.browser.browse=&Durchsuchen...

designer.preview.preference.browser.currentBrowsers=&Aktuelle Adapter für externe Webbrowser:

designer.preview.preference.browser.description=Mit dieser Auswahl werden die für die Vorschau von BIRT-Berichten verwendeten Webbrowser festgelegt.\nWurde nur ein Browser-Adapter konfiguriert, kann die Auswahl nicht\ngeändert werden.

designer.preview.preference.browser.details=Browser-Programm auswählen

designer.preview.preference.browser.program=&Benutzerdefinierter Browser-Befehl:

designer.preview.preference.browser.svg=&SVG-Diagramm aktivieren

designer.preview.preference.browser.extbrowser.link=Informationen zur Konfiguration des externen Browsers zur Anzeige von Berichten finden Sie unter <a>"{0}"</a>.

designer.preview.preference.masterpagecontent=&Inhalt der Master-Seite aktivieren

designer.preview.preference.appcontextkey=Benutzerdefinierten Anwendungskontext &verwenden

designer.preview.preference.browser.useExternal=&Immer externe Browser verwenden

designer.preview.preference.elementname.columnwidth=125

designer.preview.preference.elementname.customname.Enabled=wahr

designer.preview.preference.elementname.customname=Standardname

designer.preview.preference.elementname.defaultname.Enabled=falsch

designer.preview.preference.elementname.defaultname=Elementname

designer.preview.preference.elementname.description.Enabled=falsch

designer.preview.preference.elementname.description=Beschreibung

designer.preview.preference.elementname.Enabled=wahr

designer.preview.preference.locale.description=&Gebietsschema auswählen:

designer.preview.preference.timezone.description=&Zeitzone:

designer.preview.preference.resultset.sqldatasetpage.group.title=Abfrageseite

designer.preview.preference.resultset.previewpage.group.title=Vorschauseite

designer.preview.preference.resultset.groupTitle.DispalyRowLimitsSetting=Vorschau für maximale Zeilenzahl

designer.preview.preference.resultset.NoLimitOfDispalyRow=&Keine anzuzeigende maximale Zeilenzahl vorhanden.

designer.preview.preference.resultset.maxNoOfSchema.description=&Maximale Anzahl anzuzeigender Schemata:

designer.preview.preference.resultset.maxNoOfTable.description=&Maximale Anzahl von Tabellen in den anzuzeigenden Schemata:

designer.preview.preference.resultset.maxNoOfSchema.errormessage=Geben Sie eine Zahl zwischen 0 und {0} an, um die maximale Anzahl an Schemata zu definieren.

designer.preview.preference.resultset.maxNoOfTable.errormessage=Geben Sie eine Zahl zwischen 0 und {0} an, um die maximale Anzahl von Tabellen in den Schemata zu definieren.

designer.preview.preference.resultset.maxrow.description=Anzahl anzuzeigender &Zeilen:

designer.preview.preference.resultset.maxrow.errormessage=Geben Sie für die maximale Zeilenanzahl eine Zahl zwischen 1 und {0} ein.

designer.preview.preference.resultset.maxrowlevelmember.description=Anzahl anzuzeigender Mitglieder jeder Zeilen&ebene:

designer.preview.preference.resultset.maxrowlevelmember.errormessage=Geben Sie für die maximale Mitgliederanzahl eine Zahl zwischen 1 und {0} ein.

designer.preview.preference.resultset.maxcolumnlevelmember.description=Anzahl anzuzeigender Mitglieder jeder &Spaltenebene:

designer.preview.preference.resultset.maxcolumnlevelmember.errormessage=Geben Sie für die maximale Mitgliederanzahl eine Zahl zwischen 1 und {0} ein.

designer.preview.preference.resultset.maxinmemorycubesize.description=Quader&größe im Speicher (MB):

designer.preview.preference.resultset.maxinmemorycubesize.errormessage=Geben Sie eine Zahl zwischen 1 und {0} für die maximale Quaderspeichergröße an.

designer.preview.preference.server.description=Konfiguration des internen Anwendungsservers. Der Server wird intern vom\nBIRT Report Designer Preview Framework verwendet.

designer.preview.preference.server.hostDescription=&Host-Name oder IP-Adresse des lokalen Rechners. Leer lassen, damit eine automatische\nFestlegung erfolgt.

designer.preview.preference.server.note=Hinweis:

designer.preview.preference.server.portDescription=&Anschluss für die Serververbindung. Auf 0 stellen, da Zuordnung dynamisch.

designer.preview.preference.server.requireRestart=Wird der Server bereits ausgeführt, werden diese Einstellungen erst\nnach dem Neustart übernommen.

designer.preview.preference.bidiOrientation.label=&Bidirektionale Ausrichtung:

designer.preview.preference.bidiOrientation.auto=Automatisch

designer.preview.preference.bidiOrientation.ltr=Von links nach rechts

designer.preview.preference.bidiOrientation.rtl=Von rechts nach links

designer.preview.preference.updateParameter.confirmMessage=Aktualisierung bestätigen

designer.preview.preference.updateParameter.confirmButton=Beim Synchronisieren von P&arametereigenschaften des Datensatzes mit ODA-Parametereigenschaften immer nach Bestätigung fragen, wenn die Abfrage geändert wurde.

designer.preview.preference.updateReportParameter.confirmButton=Nie nach Bestätigung fragen, um &den Parameter eines verknüpften Berichts zu aktualisieren.

designer.preview.preference.updateReportParameter.confirmOption.always=&Immer:

designer.preview.preference.updateReportParameter.confirmOption.update=Akt&ualisieren

designer.preview.preference.updateReportParameter.confirmOption.ignore=&Ignorieren

designer.preview.previewaction.label.webviewer=Bericht in Web Viewer anzeigen

designer.preview.previewaction.label=Bericht als {0} anzeigen

designer.preview.previewaction.ErrorMessage=Die PDF-Erzeugung ist nicht aktiviert, da iText weder installiert ist noch gefunden wurde.

designer.preview.previewaction.ErrorMessage.title=Warnung

designer.preview.run.webviewer=In Web Viewer

designer.preview.run=Als {0}

###########################################################
#DesignerActionBarContributor properties
DesignerActionBarContributor.element.group=&Gruppe einfügen

DesignerActionBarContributor.element.InsertColumnLeft=Spalten &links einfügen

DesignerActionBarContributor.element.InsertColumnRight=Spalten &rechts einfügen

DesignerActionBarContributor.element.InsertRowAbove=Zeile &oben einfügen

DesignerActionBarContributor.element.InsertRowBelow=Zeile &unten einfügen

DesignerActionBarContributor.element.merge=Zellen &verbinden

DesignerActionBarContributor.element.split=Zelle &teilen

designerActionBarContributor.menu.data-newdataset=Neuer D&atensatz

designerActionBarContributor.menu.data-newdatasource=Neue Dat&enquelle

DesignerActionBarContributor.menu.data=Da&ten

DesignerActionBarContributor.menu.element-EditGroup=Gruppe b&earbeiten

DesignerActionBarContributor.menu.element-LayoutMode=Layout-Mo&dus

DesignerActionBarContributor.menu.element-showRuler=&Lineale anzeigen

DesignerActionBarContributor.menu.element.editDataBinding=Daten&bindung bearbeiten...

DesignerActionBarContributor.menu.element=Ele&ment

DesignerActionBarContributor.menu.format=Format

DesignerActionBarContributor.menu.insert=E&infügen

DesignerActionBarContributor.menu.page=Se&ite

DesignerActionBarContributor.menu.style-rule-apply=Stil an&wenden

DesignerActionBarContributor.menu.style-rule-edit=Stil bea&rbeiten

DesignerActionBarContributor.menu.style-rule-remove=Stil &entfernen

DesignerActionBarContributor.style.new=&Neuer Stil...

DesignerActionBarContributor.menu.data-NewParameter=Neuer Bericht&parameter

DesignerActionBarContributor.menu.data-NewDataSetParent=Neuer Daten&satz

designerActionBarContributor.menu.data-newDataset=Neuer D&atensatz

designerActionBarContributor.menu.data-newJointDataset=Neuer &Verknüpfungsdatensatz

DesignerActionBarContributor.menu.element.insert=E&infügen

DesignerActionBarContributor.menu.element.group=&Gruppieren

DesignerActionBarContributor.menu.element.style=St&il

DesignerActionBarContributor.menu.zoomCombo.tooltip=Zoomebene
###########################################################
#DesignerOutlinePage properties
DesignerOutlinePage.action.Delete=Auswahlen löschen

DesignerOutlinePage.action.Hide=Kopf- und Fußzeile ausblenden

DesignerOutlinePage.action.Redo=Wiederherstellen

DesignerOutlinePage.action.Undo=Rückgängig

DesignerOutlinePage.menu.Filters=Filter

###########################################################
#Content outline page switch action
ContentOutlinePage.action.text.reportOutline=Zu XML-Quellgliederung wechseln

ContentOutlinePage.action.text.reportXMLSourceOutline=Zu Berichtsgliederung wechseln

###########################################################
#DesignerPaletteFactory properties
DesignerPaletteFactory.categoryName.reportItems=Berichtobjekte

DesignerPaletteFactory.elementLabel.data=Daten

DesignerPaletteFactory.elementLabel.grid=Raster

DesignerPaletteFactory.elementLabel.image=Bild

DesignerPaletteFactory.elementLabel.label=Beschriftung

DesignerPaletteFactory.elementLabel.list=Liste

DesignerPaletteFactory.elementLabel.tabel=Tabelle

DesignerPaletteFactory.elementLabel.text=Text

DesignerPaletteFactory.elementName.data=Daten

DesignerPaletteFactory.elementName.grid=Raster

DesignerPaletteFactory.elementName.image=Bild

DesignerPaletteFactory.elementName.label=Beschriftung

DesignerPaletteFactory.elementName.list=Liste

DesignerPaletteFactory.elementName.table=Tabelle

DesignerPaletteFactory.elementName.text=Text

DesignerPaletteFactory.elementName.textdata=Dynamischer Text

DesignerPaletteFactory.toolTip.dataReportItem=Fügen Sie eine Datensatzspalte oder ein Ausdruckergebnis ein.

DesignerPaletteFactory.toolTip.gridReportItem=Fügen Sie ein Zeilen- oder Spalten-Layout ein, um andere Elemente sichtbar anzuordnen.

DesignerPaletteFactory.toolTip.imageReportItem=Fügen Sie ein Bild aus einer Datei oder von einer URL-Adresse ein.

DesignerPaletteFactory.toolTip.labelReportItem=Fügen Sie einen Teil eines statischen Textes ein.

DesignerPaletteFactory.toolTip.listReportItem=Darstellung von Datensatzzeilen mit flexiblem Format in Kopfzeilen-, Detail- oder Fußzeilenbänder einfügen

DesignerPaletteFactory.toolTip.multiLineDataReportItem=Textblock (einfacher Text, HTML oder RTF) aus einer Datei, einer Datensatzspalte oder einem Ausdruckergebnis einfügen.

DesignerPaletteFactory.toolTip.tableReportItem=Darstellung von Datensatzzeilen mit Spalten in Kopfzeilen-, Detail- oder Fußzeilenbänder einfügen

DesignerPaletteFactory.toolTip.textDataReportItem=Textblock mit optionalen CLOB-Ausdrücken einfügen

DesignerPaletteFactory.toolTip.textReportItem=Fügen Sie einen Textblock (einfacher Text oder HTML) mit optionalen Inline-Ausdrücken ein.

DesignerPaletteFactory.quicktool.title=Schnellbearbeitungswerkzeuge

DesignerPaletteFactory.quicktool.agg.title=Aggregat

DesignerPaletteFactory.quicktool.agg.toolTip=Aggregatelement einfügen

DesignerPaletteFactory.quicktool.timeperiod.title=Relatives Zeitintervall

DesignerPaletteFactory.quicktool.timeperiod.toolTip=TimePeriod-Element einfügen

###########################################################
#DimensionBuilderDialog properties
DimensionBuilderDialog.LabelMeasure=&Maßeinheit:

DimensionBuilderDialog.LabelUnit=&Einheiten:

DimensionBuilderDialog.Title=Dimension Builder

###########################################################
#DNDUtil properties
DNDUtil.trans.copy=Elemente kopieren

DNDUtil.trans.cut=Elemente verschieben

###########################################################
#DuplicatesPage properties
DuplicatesPage.Check.HideDuplicates=Elemente duplizieren

DuplicatesPage.Group.Repeat=Duplikation (Wiederholen)

DuplicatesPage.Group.RepeatonPage=Duplikation (Auf Seite wiederholen)

DuplicatesPage.Label.RepeatInfo=Werte, die identisch mit denen der vorigen Datenzeile sind, ausblenden.

DuplicatesPage.Label.RepeatOnPage.1=Werte, die identisch mit denen der vorigen Zeile sind, ausblenden,

DuplicatesPage.Label.RepeatOnPage.2=\\ aber den Wert am oberen Rand jeder neuen Seite einblenden.

DuplicatesPage.Label.ResetonGroup=Bei folgender Gruppe zurücksetzen

DuplicatesPage.Radio.Repeat=Wiederholen

DuplicatesPage.Radio.RepeatonPage=Auf Seite wiederholen

###########################################################
#EditAction properties
EditAction.text=&Bearbeiten

###########################################################
#EditGroupAction properties
EditGroupAction.stackMsg.editGroup=Gruppe bearbeiten

###########################################################
#EditGroupAction properties
InsertGroupHeaderFooterAction.stackMsg.editGroup=Gruppenkopf-/fußzeile einfügen

InsertGroupHeaderFooterAction.Text.Header=Kopfzeile

InsertGroupHeaderFooterAction.Text.Footer=Fuß.zeile

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.stackMsg.deleteGroup=Gruppe löschen

###########################################################
#EditLabelAction properties
EditLabelAction.text=&Bearbeiten

EditorDragGuidePolicy.pixels.label=Pixel

EditorGuide.bottom.label=Unten:

EditorGuide.left.label=Links:

EditorGuide.right.label=Rechts:

EditorGuide.top.label=Oben:

###########################################################
#Element properties
Element.AbstractScalarParameter.dataType=&Datentyp:

Element.AutoText.type=Typ:

Element.Cell.colSpan=Spaltenbereich:

Element.Cell.column=Spalte:

Element.Cell.drop=&Verwerfen:

Element.Cell.height=Höhe:

Element.Cell.rowSpan=Zeilenbereich:

Element.Cell.slot.content=Inhalt:

Element.Cell.verticalAlign=Vertikale Ausrichtung:

Element.Cell.width=Breite:

Element.Column.alignment=Ausrichtung:

Element.Column.repeat=Wiederholen:

Element.Column.width=&Breite:

Element.Column.suppressDuplicates=Duplikate ausblenden

Element.Container.slot.reportItems=Berichtobjekte:

Element.Data.action=Aktion:

Element.Data.helpText=Hilfetext:

Element.Data.helpTextID=Hilfetextschlüssel

Element.TextData.valueExpr=Wer&tausdruck:

Element.DataSet.afterClose.toolTip=Wird direkt nach dem Schließen des Datensatzes aufgerufen.

Element.DataSet.afterClose=Nach dem Schließen:

Element.DataSet.afterOpen.toolTip=Wird direkt nach dem Öffnen des Datensatzes aufgerufen.

Element.DataSet.afterOpen=Nach dem Öffnen:

Element.DataSet.beforeClose.toolTip=Wird direkt vor dem Schließen des Datensatzes aufgerufen.

Element.DataSet.beforeClose=Vor dem Schließen:

Element.DataSet.beforeOpen.toolTip=Wird direkt vor dem Öffnen des Datensatzes aufgerufen.

Element.DataSet.beforeOpen=Vor dem Öffnen:

Element.DataSet.cachedMetaData=Zwischengespeicherte Metadaten:

Element.DataSet.columnHints=Spaltenhinweise:

Element.DataSet.computedColumns=Berechnete Spalten:

Element.DataSet.dataSource=Datenquelle:

Element.DataSet.filter=Filter:

Element.DataSet.inputParameters=Eingabeparameter:

Element.DataSet.onFetch.toolTip=Wird direkt vor dem Abrufen einer Datenzeile aus dem Datensatz aufgerufen.

Element.DataSet.onFetch=Beim Abrufen:

Element.DataSet.outputParameters=Ausgabeparameter:

Element.DataSet.paramBindings=Parameterbindungen:

Element.DataSet.resultSet=Ergebnis:

Element.DataSource.afterClose.toolTip=Wird direkt nach dem Schließen der Datenquelle aufgerufen.

Element.DataSource.afterClose=Nach dem Schließen:

Element.DataSource.afterOpen.toolTip=Wird direkt nach dem Öffnen der Datenquelle aufgerufen.

Element.DataSource.afterOpen=Nach dem Öffnen:

Element.DataSource.beforeClose.toolTip=Wird direkt vor dem Schließen der Datenquelle aufgerufen.

Element.DataSource.beforeClose=Vor dem Schließen:

Element.DataSource.beforeOpen.toolTip=Wird direkt vor dem Öffnen der Datenquelle aufgerufen.

Element.DataSource.beforeOpen=Vor dem Öffnen:

Element.ExtendedItem.extension=Name der Erweiterung:

Element.ExtendedItem.extensionName=Name der Erweiterung:

Element.FreeForm.slot.reportItems=Berichtobjekte:

Element.GraphicMasterPage.columns=Spalten:

Element.GraphicMasterPage.columnSpacing=Spaltenabstand:

Element.GraphicMasterPage.slot.contents=Inhalt:

Element.Grid.slot.columns=Spalten:

Element.Grid.slot.row=Zeile:

Element.Image.action=Aktion:

Element.Image.altText=&Alternativer Text:

Element.Image.altTextID=Alternativer Text&schlüssel:

Element.Image.helpText=Hilfetext:

Element.Image.helpTextID=Hilfetextschlüssel

Element.Image.imageName=Bildname:

Element.Image.scale=Skalieren:

Element.Image.size=Größe:

Element.Image.source=Quelle:

Element.Image.typeExpr=Typausdruck:

Element.Image.uri=uri:
Element.Image.valueExpr=Wertausdruck:

Element.JdbcDataSource.dataSource=Datenquelle:

Element.JdbcDataSource.driver=Treiber:

Element.JdbcDataSource.password=Kennwort:

Element.JdbcDataSource.url=Url:
Element.JdbcDataSource.userName=Benutzername:

Element.JdbcSelectDataSet.getStatement.toolTip=Gibt eine benutzerdefinierte SQL-Anweisung für die Abfrage zurück.

Element.JdbcSelectDataSet.getStatement=Anweisung abrufen:

Element.JdbcSelectDataSet.statement=Anweisung:

Element.Label.action=Aktion:

Element.Label.helpText=Hilfetext:

Element.Label.helpTextID=Hilfetextschlüssel

Element.Label.text=Text

Element.Label.textID=&Textschlüssel:

Element.Line.orientation=Seitenausrichtung:

Element.List.slot.columnHeader=columnHeader:
Element.List.slot.detail=Detail:

Element.List.slot.footer=Fußzeile:

Element.List.slot.group=Gruppe:

Element.List.slot.header=Kopfzeile:

Element.ListGroup.slot.footer=Fußzeile:

Element.ListGroup.slot.header=Kopfzeile:

Element.Listing.filter=Filter:

Element.Listing.onFinish.toolTip=Wird aufgerufen, sobald die letzte Zeile aus dem Datensatz für dieses Element abgerufen wurde, jedoch bevor das Fußzeilenband erstellt wird.

Element.Listing.onFinish=Beim Beenden:

Element.Listing.onRow.toolTip=Wird für jede Zeile aufgerufen, die aus dem Datensatz für dieses Element abgerufen wird, jedoch bevor Inhalt für diese Zeile erstellt wird.

Element.Listing.onRow=Bei Zeile:

Element.Listing.onStart.toolTip=Wird aufgerufen, sobald der Datensatz geöffnet wurde, jedoch bevor das Kopfzeilenband erstellt wird und zwar noch bevor die erste Zeile aus dem Datensatz für dieses Element abgerufen wurde.

Element.Listing.onStart=Beim Starten:

Element.Listing.pageBreakInterval=Interva&ll für Seitenumbruch:

Element.Listing.sorting=Sortierung:

Element.ListingGroup.filter=Filter:

Element.ListingGroup.groupName=Name

Element.ListingGroup.interval=Intervall:

Element.ListingGroup.intervalRange=Intervallbereich:

Element.ListingGroup.keyExpr=Schlüsselausdruck

Element.ListingGroup.onFinish.toolTip=Wird aufgerufen, sobald die letzte Zeile aus dem Datensatz für dieses Element abgerufen wurde, jedoch bevor das Fußzeilenband erstellt wird.

Element.ListingGroup.onFinish=Beim Beenden:

Element.ListingGroup.onRow.toolTip=Wird für jede Zeile aufgerufen, die aus dem Datensatz für dieses Element abgerufen wird, jedoch bevor Inhalt für die Zeile erstellt wird.

Element.ListingGroup.onRow=Bei Zeile:

Element.ListingGroup.onStart.toolTip=Wird aufgerufen, sobald der Datensatz geöffnet wurde, jedoch bevor das Kopfzeilenband erstellt wird und zwar noch bevor die erste Zeile aus dem Datensatz für dieses Element abgerufen wurde.

Element.ListingGroup.onStart=Beim Starten:

Element.ListingGroup.pageBreakAfter=Seitenumbruch nach:

Element.ListingGroup.pageBreakBefore=Seitenumbruch vor:

Element.ListingGroup.sort=Sortieren:

Element.ListingGroup.sortDirection=Sortierreihenfolge:

Element.ListingGroup.toc=Inhaltsverzeichnis:

Element.MasterPage.bottomMargin=&Unterer Rand:

Element.MasterPage.columns=Spalten:

Element.MasterPage.columnSpacing=Ab&stand:

Element.MasterPage.height=Höh&e:

Element.MasterPage.leftMargin=&Linker Rand:

Element.MasterPage.margin=Rand:

Element.MasterPage.orientation=Aus&richtung:

Element.MasterPage.rightMargin=&Rechter Rand:

Element.MasterPage.slot.contents=Inhalt:

Element.MasterPage.topMargin=&Oberer Rand:

Element.MasterPage.type=T&yp:

Element.MasterPage.width=&Breite:

Element.Module.author=&Verfasser:

Element.Module.base=Basis:

Element.Module.colorPalette=Farbpalette:

Element.Module.comments=&Kommentare:

Element.Module.configVars=Konfigurationsvariablen:

Element.Module.createdBy=&Erstellt von:

Element.Module.description=B&eschreibung:

Element.Module.descriptionID=Beschreibungsschlüssel:

Element.Module.helpGuide=Hilfe-Handbuch:

Element.Module.images=Bilder:

Element.Module.includeLibraries=Bibliotheken einschließen:

Element.Module.includeResource=R&essourcendatei:

Element.Module.includeScripts=Skripte einschließen:

Element.Module.initialize.toolTip=Wird zu Beginn der Berichtausführung aufgerufen.

Element.Module.initialize=Initialisieren:

Element.LayoutModule.theme=T&hemen:

Element.Module.title=T&itel:

Element.Module.titleID=Titelschlüssel:

Element.Module.units=&Einheiten:

Element.MultiLineData.contentTypeExpr=Inhaltstypausdruck:

Element.MultiLineData.valueExpr=Wertausdruck:

Element.OdaDataSet.privateDriverDesignState=Designzustand des privaten Treibers:

Element.OdaDataSet.privateDriverProperties=Eigenschaften des privaten Treibers:

Element.OdaDataSet.publicDriverProperties=Eigenschaften des öffentlichen Treibers:

Element.OdaDataSet.queryScript.toolTip=TBD:

Element.OdaDataSet.queryScript=Skript:

Element.OdaDataSet.queryText=Abfrage:

Element.OdaDataSet.resultSetName=Name des Ergebnisses:

Element.OdaDataSet.type=Typ:

Element.OdaDataSource.driverName=Treibername:

Element.OdaDataSource.privateDriverProperties=Eigenschaften des privaten Treibers:

Element.OdaDataSource.publicDriverProperties=Eigenschaften des öffentlichen Treibers:

Element.Parameter.helpText=Hilfetext:

Element.Parameter.helpTextID=Hilfetextsch&lüssel:

Element.Parameter.hidden=Verborgen:

Element.Parameter.promptTextID=Aufforderungstexts&chlüssel:

Element.ParameterGroup.helpText=Hilfetext:

Element.ParameterGroup.helpTextID=Hilfetextsch&lüssel:

Element.ParameterGroup.slot.parameters=Parameter:

Element.ParameterGroup.startExpanded=Erweiterte starten:

Element.ParameterGroup.promptTextID=Aufforderungstexts&chlüssel:

Element.ReportDesign.afterCloseDoc.toolTip=Wird direkt nach dem Schließen des Berichtdokuments in der Erstellungsphase aufgerufen.

Element.ReportDesign.layoutPreference=Layoutvoreinstellungen:

Element.ReportDesign.afterCloseDoc=Nach dem Schließen des Dokuments:

Element.ReportDesign.afterFactory.toolTip=Wird am Ende der Erstellungsphase nach dem Schließen des Berichtdokuments (falls vorhanden) aufgerufen. Dies ist die letzte in der Erstellungsphase aufgerufene Methode.

Element.ReportDesign.afterFactory=Nach Factory:

Element.ReportDesign.afterOpenDoc.toolTip=Wird direkt nach dem Öffnen des Berichtdokuments in der Erstellungsphase aufgerufen.

Element.ReportDesign.afterOpenDoc=Nach dem Öffnen des Dokuments:

Element.ReportDesign.afterRender.toolTip=Wird nach dem Starten einer Aktion während der Präsentation aufgerufen.

Element.ReportDesign.afterRender=Nach dem Rendern:

Element.ReportDesign.beforeCloseDoc.toolTip=Wird direkt vor dem Schließen des Berichtdokuments in der Erstellungsphase aufgerufen.

Element.ReportDesign.beforeCloseDoc=Vor dem Schließen des Dokuments:

Element.ReportDesign.beforeFactory.toolTip=Wird zu Beginn der Erstellungsphase nach der Methode "initialize( )" und vor dem Öffnen des Berichtdokuments (falls vorhanden) aufgerufen.

Element.ReportDesign.beforeFactory=Vor Factory:

Element.ReportDesign.beforeOpenDoc.toolTip=Wird direkt vor dem Öffnen des Berichtdokuments in der Erstellungsphase aufgerufen.

Element.ReportDesign.beforeOpenDoc=Vor Öffnen des Dokuments:

Element.ReportDesign.beforeRender.toolTip=Wird vor dem Starten einer Aktion während der Präsentation aufgerufen.

Element.ReportDesign.beforeRender=Vor dem Rendern:

Element.ReportDesign.displayName=&Anzeigename:

Element.ReportDesign.eventHandlerClass=Ereignis-Handler-&Klasse:

Element.ReportDesign.iconFile=Mini&atur:

Element.ReportDesign.refreshRate=Aktualisierungsrate:

Element.ReportDesign.bidiLayoutOrientation=Be&richtseitenausrichtung:

Element.ReportElement.comments=&Kommentare:

Element.ReportElement.Cube=&Daten:

Element.ReportElement.customXml=Benutzerdefinierte XML:

Element.ReportElement.displayName=&Anzeigename:

Element.ReportElement.displayNameID=Anzeigenamenschlüssel:

Element.ReportElement.extends=Erweitert:

Element.ReportElement.javaClass=Java-Klasse:

Element.ReportElement.name=&Name:

Element.ReportElement.propertyMask=Eigenschaftsmaske:

Element.ReportElement.style=St&il:

Element.ReportItem.bookmark=&Lesezeichen:

Element.ReportItem.dataSet=Datensatz:

Element.ReportItem.height=&Höhe:

Element.ReportItem.onCreate.toolTip=Wird ausgeführt, wenn das Element in der Erstellungsphase erstellt wird.

Element.ReportItem.onCreate=Beim Erstellen:

Element.ReportItem.onRender.toolTip=Wird ausgeführt, wenn das Element in der Präsentationsphase zum Rendern vorbereitet wird.

Element.ReportItem.onRender=Beim Rendern:

Element.ReportItem.paramBindings=Parameterbindungen:

Element.ReportItem.toc=&Inhaltsverzeichnis:

Element.ReportItem.visibility=Sichtbarkeit:

Element.ReportItem.width=&Breite:

Element.ReportItem.x=X:

Element.ReportItem.y=Y:

Element.ReportItem.altText=Alternativtext

Element.ReportItem.altTextID=Schlüssel für Alternativtext

Element.Row.bookmark=Lesezeichen:

Element.Row.height=&Höhe:

Element.Row.slot.cell=Zelle:

Element.Row.visibility=Sichtbarkeit:

Element.ScalarParameter.alignment=Ausrichtung:

Element.ScalarParameter.allowBlank=Leerwert zulässig:

Element.ScalarParameter.allowNull=Nullwert zulässig:

Element.ScalarParameter.concealValue=Wert verbergen:

Element.ScalarParameter.controlType=&Steuerelementtyp:

Element.ScalarParameter.defaultValue=Standardwert:

Element.ScalarParameter.dynamicList.dataSetName=Name des Datensatzes:

Element.ScalarParameter.dynamicList.labelExpr=Beschriftungsausdruck:

Element.ScalarParameter.dynamicList.valueExpr=Wertausdruck:

Element.ScalarParameter.dynamicList=Dynamische Liste:

Element.ScalarParameter.format=Format:

Element.ScalarParameter.selectionList.fixedOrder=Feste Reihenfolge:

Element.ScalarParameter.selectionList.mustMatch=Muss übereinstimmen mit:

Element.ScalarParameter.selectionList.selectionList=Auswahlliste:

Element.ScalarParameter.selectionList=Auswahlliste:

Element.ScalarParameter.promptTextID=Aufforderungstexts&chlüssel:

Element.ScriptDataSet.close.toolTip=Schließt den Datensatz.

Element.ScriptDataSet.close=Schließen:

Element.ScriptDataSet.describe.toolTip=Beschreibt das Ergebnis.

Element.ScriptDataSet.describe=Beschreiben:

Element.ScriptDataSet.fetch.toolTip=Erstellt ein Objekt vom Typ "DataRow", füllt dieses Objekt und gibt es zurück.

Element.ScriptDataSet.fetch=Abrufen:

Element.ScriptDataSet.open.toolTip=Öffnet den Datensatz.

Element.ScriptDataSet.open=Öffnen:

Element.ScriptDataSource.close.toolTip=Beendet die Verbindung zur Datenquelle.

Element.ScriptDataSource.close=Schließen:

Element.ScriptDataSource.open.toolTip=Baut eine Verbindung zur Datenquelle auf.

Element.ScriptDataSource.open=Öffnen:

Element.SimpleMasterPage.floatingFooter=Unverankerte &Fußzeile

Element.SimpleMasterPage.footerHeight=Höhe der &Fußzeile:

Element.SimpleMasterPage.headerHeight=&Höhe der Kopfzeile:

Element.SimpleMasterPage.showFooterOnLast=&Fußzeile auf letzter Seite anzeigen

Element.SimpleMasterPage.showHeaderOnFirst=&Kopfzeile auf erster Seite anzeigen

Element.SimpleMasterPage.slot.pageFooter=Seitenfußzeile:

Element.SimpleMasterPage.slot.pageHeader=Seitenkopfzeile:

Element.Style.background.attachment=&Anlage:

Element.Style.background.color=&Hintergrundfarbe:

Element.Style.background.image=&Hintergrundbild:

Element.Style.background.positionX=&Horizontale Position:

Element.Style.background.positionY=&Vertikale Position:

Element.Style.background.repeat=&Wiederholen:

Element.Style.background=Hintergrund:

Element.Style.border.bottom.color=&Unten:

Element.Style.border.bottom.style=&Unten:

Element.Style.border.bottom.width=Unten&:

Element.Style.border.fillColor=Rahmenfüllfarbe:

Element.Style.border.hmiddle.color=Farbe für horizontalen mittleren Rahmen:

Element.Style.border.hmiddle.style=Stil für horizontalen mittleren Rahmen:

Element.Style.border.hmiddle.width=Breite für horizontalen mittleren Rahmen:

Element.Style.border.left.color=Li&nks:

Element.Style.border.left.style=&Links:

Element.Style.border.left.width=&Links:

Element.Style.border.right.color=&Rechts:

Element.Style.border.right.style=&Rechts:

Element.Style.border.right.width=&Rechts:

Element.Style.border.top.color=&Oben:

Element.Style.border.top.style=&Oben:

Element.Style.border.top.width=&Oben:

Element.Style.border.vmiddle.color=Farbe für vertikalen mittleren Rahmen:

Element.Style.border.vmiddle.style=Stil für vertikalen mittleren Rahmen:

Element.Style.border.vmiddle.width=Breite für vertikalen mittleren Rahmen:

Element.Style.border=Rahmen:

Element.Style.box=Feld:

Element.Style.canShrink=Darf verkleiner&n

Element.Style.color=&Farbe:

Element.Style.display=Anz&eige:

Element.Style.font.family=&Schriftart:

Element.Style.font.size=&Größe:

Element.Style.font.style=&Stil:

Element.Style.font.textLineThrough=&Durchgestrichen

Element.Style.font.textOverline=&Überstrichen

Element.Style.font.textUnderline=&Unterstrichen

Element.Style.font.variant=Schriftartvariante:

Element.Style.font.weight=&Schriftbreite:

Element.Style.font=&Schriftart:

Element.Style.format.dateTime=Datum/Uhrzeit:

Element.Style.format.number.align=Zahlenformat ausrichten:

Element.Style.format.number=Nummer:

Element.Style.format.string=Zeichenfolge:

Element.Style.format=Format:

Element.Style.highlight.rules=Regeln hervorheben:

Element.Style.highlight.testExpr=Testausdruck hervorheben:

Element.Style.letterSpacing=&Zeichenabstand:

Element.Style.lineHeight=&Zeilenhöhe:

Element.Style.map.rules=Regeln zuordnen:

Element.Style.map.testExpr=Testausdruck zuordnen:

Element.Style.margin.bottom=&Unten:

Element.Style.margin.left=&Links:

Element.Style.margin.right=&Rechts:

Element.Style.margin.top=&Oben:

Element.Style.masterPage=Master-&Seite

Element.Style.orphans=&Verwaist:

Element.Style.padding.bottom=&Unten:

Element.Style.padding.left=&Links:

Element.Style.padding.right=&Rechts:

Element.Style.padding.top=&Oben:

Element.Style.padding=Umrandungsfüllung:

Element.Style.pageBreakAfter=&Nachher:

Element.Style.pageBreakBefore=&Vorher:

Element.Style.pageBreakInside=&Innen:

Element.Style.section=Abschnitt:

Element.Style.showIfBlank=&Anzeigen, wenn leer

Element.Style.text.align=Texta&usrichtung:

Element.Style.width=&Breite:

Element.Style.height=&Höhe:
#bidi_hcg
Element.Style.text.direction=&Bidirektionale Textrichtung:

Element.Style.text.indent=&Texteinzug:

Element.Style.text.transform=Text&umwandlung:

Element.Style.text=Text

Element.Style.verticalAlign=Vertikale &Ausrichtung:

Element.Style.whiteSpace=Wh&itespace:

Element.Style.widows=&Verwitwet:

Element.Style.wordSpacing=&Wortabstand:

Element.Table.caption=Beschriftung:

Element.Table.captionID=Beschriftungsschlüssel:

Element.Table.repeatHeader=Kopfzeile wiederholen:

Element.Table.slot.columnHeader=columnHeader:
Element.Table.slot.columns=Spalten:

Element.Table.slot.detail=Detail:

Element.Table.slot.footer=Fußzeile:

Element.Table.slot.group=Gruppe:

Element.Table.slot.header=Kopfzeile:

Element.TableGroup.slot.footer=Fußzeile:

Element.TableGroup.slot.header=Kopfzeile:

Element.Text.content=Inhalt:

Element.Text.contentID=&Inhaltsschlüssel:

Element.Text.contentType=&Inhaltstyp:

Element.TemplateElement.name=&Name

Element.Toc.Sytle=St&il

Element.VariableElement.name=&Name:

Element.VariableElement.type=&Typ:

Element.VariableElement.value=&Wert:

###########################################################
#EmbeddedImageNodeProvider properties
EmbeddedImageNodeProvider.action.New=Neues einge&bettetes Bild

###########################################################
#EmbeddedImagesNodeProvider properties
EmbeddedImagesNodeProvider.action.New=Neues einge&bettetes Bild

###########################################################
#ElementIdDescriptorProvider properties
ElementIdDescriptorProvider.Display.Element.ID=&Element-ID:

###########################################################
#EmptyEditPart properties
EmptyEditPart.Msg.EmptyLib=Es befinden sich keine visuellen Elemente in der Bibliothek. Fügen Sie der Bibliothek visuelle Elemente hinzu, indem Sie das Element von der Palette in die Gliederungsansicht der Bibliothek ziehen oder das Menü "Einfügen" der Gliederungsansicht verwenden.

EmptyEditPart.Msg.NonVisual=Es wurde ein nicht-visuelles Element gewählt.

###########################################################
#error properties
error.duplicateName=Der neue Name ist im gleichen Namensraum bereits vorhanden.

error.DataSource.emptyName=Der Datenquellenname darf nicht leer sein.

error.DataSet.emptyName=Der Datensatzname darf nicht leer sein.

error.OutputColumn.emptyName=Der Ausgabespaltenname darf nicht leer sein.

error.invalidName=Der Name '{0}' ist unzulässig, da er eines der folgenden Zeichen enthält: '.', '/', '\', '!', ';', ','.

###########################################################
#ExceptionHandler properties
ExceptionHandler.Label.ErrorCode=Fehlercode

ExceptionHandler.Label.ErrorMessage=Fehlermeldung

ExceptionHandler.Label.PluginId=Plug-In-ID:

ExceptionHandler.Label.PluginName=Plug-In-Name:

ExceptionHandler.Label.PluginProvider=Plug-In-Anbieter:

ExceptionHandler.Label.PluginVersion=Version:

ExceptionHandler.Label.UnknownPlugin=Unbekanntes Plug-In

ExceptionHandler.Meesage.UnexceptedExceptionOccur=Es ist eine unerwartete interne Ausnahme aufgetreten. Nähere Informationen finden Sie im Protokoll.

ExceptionHandler.Message.BirtExceptionOccur=In BIRT ist ein Ausnahmefehler aufgetreten.

ExceptionHandler.Message.CausedBy=Verursacht durch {0}.

ExceptionHandler.Message.FileNotFound=Datei nicht gefunden:

ExceptionHandler.Message.OutOfMemory=Nicht genug Arbeitsspeicher.

ExceptionHandler.Message.PartInitError=Teilinitialisierung fehlgeschlagen.

ExceptionHandler.Message.SemanticFail=Semantische Prüfung fehlgeschlagen.

ExceptionHandler.Message.UnexceptedExceptionOccur=Es ist eine interne Ausnahme aufgetreten. Nähere Informationen finden Sie in der Protokolldatei.

ExceptionHandler.Message.UnknownHost=Unbekannter Host:

ExceptionHandler.Title.Error=Fehler

ExceptionHandler.Title.FileNotFound=Datei nicht gefunden.

ExceptionHandler.Title.PartInitError=Teilinitialisierungsfehler.

ExceptionHandler.Title.UnknownHost=Unbekannter Host

ExceptionHandler.Meesage.ExceptionOccur=Eine unerwartete Ausnahme ist aufgetreten. Nähere Informationen finden Sie im Protokoll.

###########################################################
#ExportAction properties
ExportAction.actionMsg.export=Exportieren

###########################################################
#ExportToLibraryAction properties
ExportToLibraryAction.action.text=In Bibliothek e&xportieren...

ExportToLibraryAction.Button.Cancel=Abbrechen

ExportToLibraryAction.Button.No=Nein

ExportToLibraryAction.Button.Yes=Ja

ExportToLibraryAction.Dialog.Message=\nZielbibliothek verfügt eventuell über dasselbe Designelement. Möchten Sie es überschreiben?

ExportToLibraryAction.Dialog.Title=Element exportieren

ExportToLibraryAction.Message.RememberDecision=Antwort speiche&rn


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.action.text=In Bibliothek e&xportieren...

ExportToCPSouceAction.action.text=An Verbindungspr&ofil externalisieren...


###########################################################
#LibraryExplorerTreeViewPage tooltips
LibraryElementsToolHandleExtends_message=Das Element enthält lokale Bindungen ohne eindeutig angegebenen Datensatz. Dies kann zu geringer Leistung während der Laufzeit führen. Möchten Sie die lokalen Bindungen für eine verbesserte Leistung zum übergeordneten Element verschieben?

LibraryElementsToolHandleExtends_question=Die lokalen Bindungen für eine verbesserte Leistung zum übergeordneten Element verschieben?

LibraryElementsToolHandleExtends_toggle=Antwort speiche&rn

LibraryExplorerTreeViewPage.toolTips.DragAndDropOutline=Mit Drag & Drop in Gliederungsansicht

LibraryExplorerTreeViewPage.toolTips.DragAndDropLayout=Mit Drag & Drop in Layout-Editor

LibraryExplorerTreeViewPage.toolTips.DragAndDropToOutlineORLayout=Mit Drag & Drop in Gliederungsansicht oder Layout-Editor

###########################################################
#ExpressionBuidler properties
ExpressionBuidler.Dialog.Title=Ausdruckserstellung (Javascript)

ExpressionBuidler.Title=Ausdruckserstellung

ExpressionBuilder.tooltip.Sort=Sortieren

ExpressionBuilder.toolTipText.Button=Ausdruckserstellung öffnen

###########################################################
#ExpressionBuilder properties
ExpressionBuilder.Label.Category=&Kategorie:

ExpressionBuilder.Label.Functions=E&infügen durch Doppelklicken:

ExpressionBuilder.Label.Hint=Hinweis

ExpressionBuilder.Label.Operators=Operatoren:

ExpressionBuilder.Label.SubCategory=&Unterkategorie:

ExpressionBuilder.Message.Prompt=Geben Sie in das Feld für die Ausdruckserstellung einen Ausdruck ein. Durchsuchen Sie die Liste der verfügbaren Objekte, und kopieren Sie sie per Doppelklick in den Ausdruck.

ExpressionBuilder.ToolTip=Ausdruckserstellung öffnen

ExpressionBuilder.toolTipText.validate=Validieren

ExpressionBuilder.Script.NoError=Im aktuellen Skript wurde kein Syntaxfehler gefunden.

ExpressionBuilder.Script.Error=[Zeile:{0}, Spalte:{1}] {2}

ExpressionBuilder.Script.Warning=Warnung

ExpressionBuilder.Script.Confirm=\nIm aktuellen Skript ist ein Syntaxfehler aufgetreten. Möchten Sie es wirklich speichern?

ExpressionBuilder.toolTipText.calendar=Kalender

ExpressionBuilder.Calendar.Button.OK=OK

ExpressionButtonProvider.Constant=Konstante

ExpressionButtonProvider.Javascript=JavaScript-Syntax

###########################################################
#ExpressionPropertyDescriptor properties
ExpressionPropertyDescriptor.text.Edit=&Ausdruckserstellung...

###########################################################
#ExpressionProvider properties
ExpressionProvider.Category.Context=Kontext

ExpressionProvider.Category.BirtObjects=BIRT-Funktionen

ExpressionProvider.Category.DataSets=Verfügbare Datensätze

ExpressionProvider.Category.NativeObjects=Native JavaScript-Funktionen

ExpressionProvider.Category.ColumnBinding=Verfügbare Spaltenbindungen

ExpressionProvider.Category.Operators=Operatoren

ExpressionProvider.Category.Parameters=Berichtparameter

ExpressionProvider.Category.DataCubes=Aktueller Datenquader

ExpressionProvider.Category.Variables=Variablen

ExpressionProvider.Label.All=---Alle---

ExpressionProvider.Operator.Add=Fügt zwei Werte zusammen.

ExpressionProvider.Operator.AddTo=Verknüpfung zum Hinzufügen zum aktuellen Wert.

ExpressionProvider.Operator.And=Vergleicht zwei Ausdrücke und gibt den Wert TRUE zurück, wenn beide Ausdrücke wahr sind.

ExpressionProvider.Operator.Assign=Setzt einen Wert dem anderen gleich.

ExpressionProvider.Operator.BitAnd=A & B: Bitweises UND-Ergebnis von A und B zurückgeben.

ExpressionProvider.Operator.BitOr=A | B: Bitweises ODER-Ergebnis von A und B zurückgeben.

ExpressionProvider.Operator.Dec=Verringert den Wert und gibt das Ergebnis zurück.

ExpressionProvider.Operator.DividingFrom=Verknüpfung zum Teilen des aktuellen Wertes.

ExpressionProvider.Tooltip.ColumnBinding=Verweist auf

ExpressionProvider.Operator.Divides=Teilt den linken Wert durch den rechten Wert und gibt das Ergebnis zurück.

ExpressionProvider.Operator.Equals=Gibt den Wert TRUE zurück, wenn die Elemente gleich sind.

ExpressionProvider.Operator.Greater=A > B: Gibt einen wahren Wert zurück, wenn A größer als B ist.

ExpressionProvider.Operator.GreaterEquals=A >= B: Gibt einen wahren Wert zurück, wenn A größer als oder gleich B ist.

ExpressionProvider.Operator.Inc=Erhöht den Wert und gibt das Ergebnis zurück.

ExpressionProvider.Operator.LeftBracket=Linke Klammer

ExpressionProvider.Operator.RightBracket=Rechte Klammer

ExpressionProvider.Operator.Less=A < B: Gibt einen wahren Wert zurück, wenn A kleiner als B ist.

ExpressionProvider.Operator.LessEqual=A <= B: Gibt einen wahren Wert zurück, wenn A kleiner als oder gleich B ist.

ExpressionProvider.Operator.Mult=Multipliziert zwei Werte.

ExpressionProvider.Operator.MultTo=Verknüpfung zum Multiplizieren des aktuellen Wertes.

ExpressionProvider.Operator.Not=!A: Gibt einen wahren Wert zurück, wenn A falsch ist, bzw. einen falschen Wert, wenn A wahr ist.

ExpressionProvider.Operator.NotEqual=A <> B: Gibt einen wahren Wert zurück, wenn A nicht gleich B ist.

ExpressionProvider.Operator.Or=Vergleicht zwei Ausdrücke und gibt den Wert von TRUE zurück, wenn einer der Ausdrücke wahr ist.

ExpressionProvider.Operator.ReturnDec=Gibt den Wert zurück und verringert ihn dann.

ExpressionProvider.Operator.ReturnInc=Gibt den Wert zurück und erhöht ihn dann.

ExpressionProvider.Operator.Sub=Subtrahiert einen Wert vom anderen.

ExpressionProvider.Operator.SubFrom=Verknüpfung zum Subtrahieren vom aktuellen Wert.

ExpressionProvider.Operators.Assignment=Zuweisung

ExpressionProvider.Operators.Comparison=Vergleich

ExpressionProvider.Operators.Computational=Rechnerische

ExpressionProvider.Operators.Logical=Logische

ExpressionProvider.EditBindings=Bindungen bearbeiten...

ExpressionProvider.Expression.RowNumName=RowNum

ExpressionProvider.Expression.RowNumTooltip=Ausdruck, der die aktuelle Zeilenanzahl zurückgibt.

ExpressionProvider.Variables=Variablen

ExpressionProvider.Variables.Report=Berichtvariablen

ExpressionProvider.Variables.Page=Seitenvariablen

ExpressionTreeSupport.Category.Global=Global

###########################################################
#ExpressionValueCellEditor properties
ExpressionValueCellEditor.buildExpressionAction=<Ausdruckserstellung ...>

ExpressionValueCellEditor.selectValueAction=<Wert auswählen ...>

ExpressionValueCellEditor.title=Wert auswählen

###########################################################
#ExtendedEditPart properties
ExtendedEditPart.edit=Bearbeiten {0}

###########################################################
#ExtendedItemPageGenerator properties
ExtendedItemPageGenerator.List.Bookmark=Lesezeichen

ExtendedItemPageGenerator.List.TOC=Inhaltsverzeichnis

###########################################################
#FilterHandleProvider properties
FilterHandleProvider.ColumnName.Value=Wert

FilterHandleProvider.Label.Filterby=Filtern nach:

FilterHandleProvider.Modify.SaveValues=Werte speichern

###########################################################
#filters properties
filters.empty.columnName=Der Ausdruck darf nicht leer sein.

filters.error.title=Ungültige Eingabe.

filters.operators.any=ist beliebig

filters.operators.between=Ist zwischen

filters.operators.bottomN=Letzte (n) Elemente

filters.operators.bottomPercent=Unterste (n) Prozent

filters.operators.equal=ist gleich

filters.operators.false=ist FALSE

filters.operators.greaterThan=ist größer als

filters.operators.greaterThanEqual=ist größer als oder gleich

filters.operators.lessThan=ist kleiner als

filters.operators.lessThanEqual=ist kleiner als oder gleich

filters.operators.like=ist wie

filters.operators.notBetween=ist nicht zwischen

filters.operators.notEqual=ist nicht gleich

filters.operators.notNull=ist nicht null

filters.operators.null=ist null

filters.operators.topN=ist erste (n) Elemente

filters.operators.topPercent=ist oberste (n) Prozent

filters.operators.true=ist TRUE

filters.prompt.new=<Neuer Filter hier...>

###########################################################
#FixTableLayoutCalculator properties
FixTableLayoutCalculator.Error.CannotParse=Der Eingabewert kann nicht analysiert werden.

###########################################################
#FlowMoveChildCommand properties
FlowMoveChildCommand.transLabel.moveElement=Element verschieben

###########################################################
#FontPreferencePage properties
FontPreferencePage.displayname.Title=Schriftart

FontPreferencePage.label.fontDecoration=Dekoration:

FontPreferencePage.text.Preview=Mustervorschau

FontPreferencePage.text.PreviewContent=AaBbYyZz

###########################################################
#FontSizeBuilder properties
FontSizeBuilder.displayname.Auto=Automatisch

###########################################################
#FormatBuilder properties
FormatAdapter.DisplayName.None=Automatisch

FormatBuilder.Title=Formaterstellung

###########################################################
#FormatDateTimeAttributePage properties
FormatDateTimeAttributePage.Trans.SetDateTimeFormat = Legen Sie das Datum-/Uhrzeitformat fest.

###########################################################
#FormatDateTimePage properties
FormatDateTimePage.label.custom.settings.label=Beis&pielformate (Klicken Sie auf ein Beispiel, um es zu kopieren und zu bearbeiten):

FormatDateTimePage.label.custom.settings=Benutzerdefinierte Einstellungen

FormatDateTimePage.label.format.code=Format&code:

FormatDateTimePage.label.format.page=&Formatieren als:

FormatDateTimePage.label.format.locale=&Gebietsschema:

FormatDateTimePage.label.general.preview.group=Vorschau mit Format

###########################################################
# LOCALE SPECIAL:
# The datetime pattern string in this text should be formatted locale-dependent if needed
FormatDateTimePage.label.guide.format=dd/MM/yyyy hh:mm:ss a

FormatDateTimePage.label.guide.text=Im Format dd/MM/yyyy hh:mm:ss AM/PM eingeben

FormatDateTimePage.label.preview.dateTime=Datum/Uhrzeit für &Vorschau:

FormatDateTimePage.label.preview.group=Vorschau

FormatDateTimePage.label.preview.label=Vorschau mit Format:

FormatDateTimePage.label.table.column.format.name=Beispielformat

FormatDateTimePage.label.table.column.format.result=Beispiel-Formatergebnis

FormatDateTimePage.label.table.column.format.code=Beispielcode

FormatDateTimePage.preview.invalid.dateTime=Ungültige(s) Datum/Uhrzeit für Vorschau

FormatDateTimePage.preview.invalid.formatCode=Ungültiger Formatcode

###########################################################
#FormatDateTimePreferencePage properties
FormatDateTimePreferencePage.formatDateTime.title=Format Datum/Uhrzeit

###########################################################
#FormatNumberAttributePage properties
FormatNumberAttributePage.Trans.SetNumberFormat = Legen Sie das Zahlenformat fest.

###########################################################
#FormatNumberPage properties
FormatNumberPage.currency.symbol.none=Kein Symbol

FormatNumberPage.label.currency.settings=Währungseinstellungen

FormatNumberPage.label.custom.preview.group=Vorschau

FormatNumberPage.label.custom.preview.label=Vorschau mit Format:

FormatNumberPage.label.custom.settings.lable=Beis&pielformate (Klicken Sie auf ein Beispiel, um es zu kopieren und zu bearbeiten):

FormatNumberPage.label.custom.settings=Benutzerdefinierte Einstellungen

FormatNumberPage.label.decimal.places=&Dezimalstellen:

FormatNumberPage.label.rounding.mode=Modus "&Runden":

FormatNumberPage.label.fixed.settings=Feste Einstellungen

FormatNumberPage.label.format.code=Format&code:

FormatNumberPage.label.format.number.page=&Formatieren als:

FormatNumberPage.label.format.number.locale=&Gebietsschema:

FormatNumberPage.label.general.preview.group=Vorschau mit Format

FormatNumberPage.label.negative.numbers=&Negative Zahlen:

FormatNumberPage.label.percent.settings=Prozenteinstellungen

FormatNumberPage.label.preview.number=Zahl für &Vorschau:

FormatNumberPage.label.scientific.settings=Wissenschaftliche Einstellungen

FormatNumberPage.label.symbol.position=S&ymbolposition:

FormatNumberPage.label.symbol=Sym&bol:

FormatNumberPage.label.table.column.format.code=Beispielformat

FormatNumberPage.label.table.column.format.result=Beispiel-Formatergebnis

FormatNumberPage.label.use1000sSeparator=&1000er-Trennzeichen verwenden

FormatNumberPage.label.useLeadingZero=Führende Null verwenden

FormatNumberPage.label.useSymbolSpace=Leerzeichensymbol &verwenden

FormatNumberPage.preview.invalidFormatCode=Ungültiger Formatcode

FormatNumberPage.preview.invalidNumber=Zahl für Vorschau ungültig

FormatNumberPage.symblePos.after=Nach

FormatNumberPage.symblePos.before=Vor

FormatNumberPage.roundingMode.halfUp=Halb nach oben

FormatNumberPage.roundingMode.halfDown=Halb nach unten

FormatNumberPage.roundingMode.halfEven=Halb gerade

FormatNumberPage.roundingMode.up=Aufwärts

FormatNumberPage.roundingMode.down=Abwärts

FormatNumberPage.roundingMode.ceiling=Obergrenze

FormatNumberPage.roundingMode.floor=Boden

FormatNumberPage.roundingMode.unnecessary=Nicht erforderlich

###########################################################
#FormatNumberPreferencePage properties
FormatNumberPreferencePage.formatNumber.title=Format Zahl

###########################################################
#FormatPreferencePage properties
FormatPreferencePage.displayname.Title=Format

###########################################################
#FormatStringAttributePage properties
FormatStringAttributePage.Trans.SetStringFormat = Legen Sie das Zeichenkettenformat fest.

###########################################################
#FormatStringPage properties
FormatStringPage.default.preview.text=Beispielzeichenfolge

FormatStringPage.label.custom.preview.label=Vorschau mit Format:

FormatStringPage.label.custom.settings.label=Beis&pielformate (Klicken Sie auf ein Beispiel, um es zu kopieren und zu bearbeiten):

FormatStringPage.label.customSettings=Benutzerdefinierte Einstellungen

FormatStringPage.label.format.code=Format&code:

FormatStringPage.label.formatStringAs=&Formatieren als:

FormatStringPage.label.locale=&Gebietsschema:

FormatStringPage.label.preview.group=Vorschau

FormatStringPage.label.preview.string=Ze&ichenfolge für Vorschau:

FormatStringPage.label.previewWithFormat=Vorschau mit Format

FormatStringPage.label.table.collumn.exampleFormatCode=Beispielformat

FormatStringPage.label.table.collumn.exampleFormatResult=Beispiel-Formatergebnis

FormatStringPage.previewText.invalidFormatCode=Ungültiger Formatcode

###########################################################
#FormatStringPreferencePage properties
FormatStringPreferencePage.formatString.title=Format Zeichenfolge

###########################################################
#FormPage properties
FormPage.Button.Add=&Hinzufügen

FormPage.Button.AddWithDialog=&Hinzufügen...

FormPage.Button.AddWithDialog.PROP=&Datei hinzufügen...

FormPage.Button.AddWithDialog.JAR=Da&tei hinzufügen...

FormPage.Button.AddWithDialog.JS=Datei hinzufügen&...

FormPage.Button.Add.AggregateOn=A&ggregat hinzufügen

FormPage.Button.AddWithDialog.AggregateOn=A&ggregat hinzufügen...

FormPage.Button.Delete=&Entfernen

FormPage.Button.DeleteFile=&Datei entfernen

FormPage.Button.DeleteFile.Alt1=D&atei entfernen

FormPage.Button.DeleteFile.Alt2=Da&tei entfernen

FormPage.Button.Down=Nach unte&n

FormPage.Button.Down.Alt1=Nach un&ten

FormPage.Button.Down.Alt2=Nach &unten

FormPage.Button.Edit=&Bearbeiten

FormPage.Button.EditWithDialog=&Bearbeiten...

FormPage.Button.Up=Nach &oben

FormPage.Button.Up.Alt1=Nach o&ben

FormPage.Button.Up.Alt2=Na&ch oben

FormPage.Button.Binding.Refresh=A&ktualisieren

FormPage.Button.Binding.Clear=Lös&chen

FormPage.Menu.ModifyProperty=Eigenschaft modifizieren

FormPage.toolTipText.Up=Nach oben

FormPage.toolTipText.Down=Nach unten
###########################################################
#GeneralInsertMenuAction properties
GeneralInsertMenuAction.text.Data=&Daten

GeneralInsertMenuAction.text.DynamicText=D&ynamischer Text

GeneralInsertMenuAction.text.Grid=Ra&ster

GeneralInsertMenuAction.text.Image=B&ild

GeneralInsertMenuAction.text.Label=Bes&chriftung

GeneralInsertMenuAction.text.List=&Liste

GeneralInsertMenuAction.text.Table=Ta&belle

GeneralInsertMenuAction.text.Text=T&ext

###########################################################
#GeneralPreferencePage properties
GeneralPreferencePage.errorMsg.duplicate.styleName=Der Stilname ist bereits vorhanden.

GeneralPreferencePage.label.styleNameDuplicate=Stilname "{0}" ist bereits vorhanden. Wählen Sie einen anderen Namen.

GeneralPreferencePage.label.styleNamePredefind=Stilname '{0}' ist vordefiniert, wählen Sie einen anderen Namen.

GeneralPreferencePage.label.customStyle=&Benutzerdefinierter Stil

GeneralPreferencePage.Label.CustomStyle=Name:

GeneralPreferencePage.Label.Note=HINWEIS: * Stileigenschaften müssen auf einen bestimmten Berichtelementtyp angewendet werden.

GeneralPreferencePage.Label.PreDefinedStyle=Berichtelement wählen:

GeneralPreferencePage.label.predefinedStyle=Vo&rdefinierter Stil*

GeneralPreferencePage.label.nameEmpty=Leerer Stilname ist nicht zulässig.

###########################################################
#GridEditPart properties
GraphicalEditorWithFlyoutPalette_Redo.Text0=&Wiederholen

GraphicalEditorWithFlyoutPalette_Redo.Text1=&Wiederholen: {0}

GraphicalEditorWithFlyoutPalette_Redo.ToolTip0=Wiederherstellen

GraphicalEditorWithFlyoutPalette_Redo.ToolTip1=Wiederherstellen: {0}

GraphicalEditorWithFlyoutPalette_Undo.Text0=&Rückgängig

GraphicalEditorWithFlyoutPalette_Undo.Text1=&Rückgängig: {0}

GraphicalEditorWithFlyoutPalette_Undo.ToolTip0=Rückgängig

GraphicalEditorWithFlyoutPalette_Undo.ToolTip1=Rückgängig: {0}

GridEditPart.GUIDEHANDLE_TEXT = Raster

###########################################################
#GridFigure properties
GridFigure.BORDER_TEXT=Raster

###########################################################
#GridPageGenerator properties
GridPageGenerator.List.Bookmark=Lesezeichen

GridPageGenerator.List.Borders=Rahmen

GridPageGenerator.List.Font=Schriftart

GridPageGenerator.List.General=Allgemein

GridPageGenerator.List.Margin=Rand

GridPageGenerator.List.Section=Seitenumbruch

GridPageGenerator.List.TOC=Inhaltsverzeichnis

GridPageGenerator.List.Visibility=Sichtbarkeit

###########################################################
#GridProvider properties
GridProvider.action.text=&Zeile einfügen

###########################################################
#GroupDialog properties
GroupDialog.Dialog.GroupDetail=Gruppendetails

GroupDialog.Label.Bookmark=&Lesezeichen:

GroupDialog.Dialog.TOCGroup=Inhaltsverzeichnis

GroupDialog.Dialog.TOC=Elemen&tausdruck:

GroupDialog.Dialog.TOCStyle=St&il:

GroupDialog.DialogTitle.NoData=Initialisierungsfehler

GroupDialog.DialogTitle.SemanticCheckFailed=Semantische Prüfung fehlgeschlagen

GroupDialog.ErrorMesaage.IllegalGroup=Diese Gruppe ist unerlaubt.

GroupDialog.ErrorMessage.NoData=Es ist keine Datenzeile verfügbar.

GroupDialog.Label.FilterSorting=Filter und Sortierung

GroupDialog.Label.GroupOn=&Gruppierung nach:

GroupDialog.Label.HeaderFooter=Kopfzeile und Fußzeile

GroupDialog.Label.IncludeFooter=&Gruppenfußzeile einschließen

GroupDialog.Label.IncludeHeader=&Gruppenkopfzeile einschließen

GroupDialog.Label.Interval=Inter&vall:

GroupDialog.PageBreakBefore=V&or:

GroupDialog.PageBreakAfter=Na&ch:

GroupDialog.PageBreakInside=&Innen:

GroupDialog.Label.IntervalBase=Fe&sten Basiswert für Intervall verwenden:

GroupDialog.Label.Name=Na&me:

GroupDialog.Label.Range=Ber&eich:

GroupDialog.Tab.Binding=Datenbindungen

GroupDialog.Tab.Filter=Filter

GroupDialog.Tab.Sorting=Sortierung

GroupDialog.RepeatHeader=Kopfzeile wieder&holen

GroupDialog.Title.Edit=Gruppe bearbeiten

GroupDialog.Title.New=Neue Gruppe

GroupDialog.PageBreak=Seitenumbruch

GroupDialog.buttion.HideDetail=Details &ausblenden

GroupDialog.toolTipText.openExprButton=Ausdruckserstellung öffnen

GroupDialog.Button.Ascending=Auf&steigend

GroupDialog.Button.Descending=Abs&teigend
###########################################################
#GroupHandleProvider properties
GroupHandleProvider.Label.Groups=Gruppenliste:

###########################################################
#GroupProvider properties
GroupProvider.action.text=Gruppe b&earbeiten

###########################################################
#GuiExtensionManager properties
GuiExtensionManager.label.name=Erweitertes Element

GuiExtensionManager.tooltip.insert={0} einfügen

###########################################################
#HighlightHandleProvider properties
HighlightHandleProvider.text.Preview=AaBbCcYyZz (Schriftgröße =

###########################################################
#HighlightRuleBuilderDialog properties
HightlightRuleBuilder.button.add=&Hinzufügen

HightlightRuleBuilder.button.add.tooltip=Wert zur Werteliste hinzufügen

HightlightRuleBuilder.button.delete=Ent&fernen

HightlightRuleBuilder.button.delete.tooltip=Ausgewählten Wert aus Liste entfernen

HightlightRuleBuilder.button.deleteall=Alle entf&ernen

HightlightRuleBuilder.button.deleteall.tooltip=Alle Werte aus Liste entfernen

HightlightRuleBuilder.button.edit=B&earbeiten...

HightlightRuleBuilder.button.edit.tooltip=Ausgewählten Wert in Werteliste bearbeiten

HightlightRuleBuilder.label.value=&Wert:

HightlightRuleBuilder.list.item1=Wert

HightlightRuleBuilder.MsgDlg.Msg=Warnung

HightlightRuleBuilder.MsgDlg.Title=Der Wert darf nicht leer sein.

HightlightRuleBuilder.text.AND=AND

HighlightRuleBuilderDialog.choice.ValueOfThisDataItem=Wert dieses Datenelements

HighlightRuleBuilderDialog.text.AND=UND

HighlightRuleBuilderDialog.text.Any=ist beliebig

HighlightRuleBuilderDialog.text.BackgroundColor=&Hintergrundfarbe:

HighlightRuleBuilderDialog.text.Between=Ist zwischen

HighlightRuleBuilderDialog.text.Color=&Farbe:

HighlightRuleBuilderDialog.text.Default=Automatisch

HighlightRuleBuilderDialog.text.ET=ist gleich

HighlightRuleBuilderDialog.text.False=ist FALSE

HighlightRuleBuilderDialog.text.Font=&Schriftart:

HighlightRuleBuilderDialog.text.GE=größer als oder gleich

HighlightRuleBuilderDialog.text.GT=ist größer als

HighlightRuleBuilderDialog.text.LE=ist kleiner als oder gleich

HighlightRuleBuilderDialog.text.Like=ist wie

HighlightRuleBuilderDialog.text.LT=ist kleiner als

HighlightRuleBuilderDialog.text.NE=ist nicht gleich

HighlightRuleBuilderDialog.text.NotBetween=ist nicht zwischen

HighlightRuleBuilderDialog.text.NotNull=ist nicht Null

HighlightRuleBuilderDialog.text.Null=ist Null

HighlightRuleBuilderDialog.text.Preview=Vorschau:

HighlightRuleBuilderDialog.text.PreviewContent=AaBbCcYyZz
HighlightRuleBuilderDialog.text.Size=&Größe:

HighlightRuleBuilderDialog.text.Title=Markieren

HighlightRuleBuilderDialog.text.True=ist TRUE

HighlightRuleBuilderDialog.tooltip.ExpBuilder=Ausdruckserstellung öffnen

HighlightRuleBuilderDialog.tooltip.Bold=Fett

HighlightRuleBuilderDialog.tooltip.Italic=Kursiv

HighlightRuleBuilderDialog.tooltip.Underline=Unterstrichen

HighlightRuleBuilderDialog.tooltip.Text_Line_Through=Durchgestrichener Text

HighlightRuleBuilderDialog.text.applyStyle=St&il anwenden:

HighlightRuleBuilderDialog.displayText.None=Ohne

HighlightRuleBuilderDialog.text.Description=Bedingte Formatierung anwenden auf {0}. Geben Sie den Bedingungsausdruck und das Format an.\nDas Format wird angewendet, wenn der Ausdruck als TRUE ausgewertet wird.

HighlightRuleBuilderDialog.text.Description.Element.Table=Tabelle

HighlightRuleBuilderDialog.text.Description.Element.Grid=Raster

HighlightRuleBuilderDialog.text.Description.Element.List=Liste

HighlightRuleBuilderDialog.text.Description.Element.Row=Zeile

HighlightRuleBuilderDialog.text.Description.Element.Column=Spalte

HighlightRuleBuilderDialog.text.Description.Element.DataItem=Datenelement

HighlightRuleBuilderDialog.text.Description.Element.Cell=Zelle

HighlightRuleBuilderDialog.text.Group.Condition=Be&dingung

HighlightRuleBuilderDialog.text.Group.Format=Format

HighlightRuleBuilderDialog.text.Description.Element.ReportElement=Berichtselement

###########################################################
#HighlightsPage properties
HighlightsPage.Button.Add=&Hinzufügen...

HighlightsPage.Button.Delete=&Entfernen

HighlightsPage.Dialog.EditHighlight=Markierung bearbeiten

HighlightsPage.Dialog.NewHighlight=Neue Kennzeichnung

HighlightsPage.Label.Highlights=Kennzeichnungsliste:

HighlightsPage.TableColumn.Condition=Bedingung

HighlightsPage.TableColumn.Preview=Vorschau:

HighlightsPage.trans.Add=Kennzeichnungsregel hinzufügen

HighlightsPage.trans.Delete=Kennzeichnungsregel löschen

HighlightsPage.trans.Edit=Kennzeichnungsregel bearbeiten

HighlightsPage.trans.MoveDown=Kennzeichnungsregel nach unten verschieben

HighlightsPage.trans.MoveUp=Kennzeichnungsregel nach oben verschieben

HighlightsPage.toolTipText.MoveDown=Kennzeichnungsregel nach unten verschieben

HighlightsPage.toolTipText.MoveUp=Kennzeichnungsregel nach oben verschieben

HighlightsPage.label.duplicate=Dupli&kate

HighlightsPage.toolTipText.duplicate=Doppelte Kennzeichnungsregel

HighlightsPage.trans.Duplicate=Doppelte Kennzeichnungsregel

###########################################################
#HighlightsPreferencePage properties
HighlightsPreferencePage.displayname.Condition=Bedingung

HighlightsPreferencePage.displayname.Preview=Vorschau

HighlightsPreferencePage.displayname.Title=Kennzeichnungen

HighlightsPreferencePage.text.Add=&Hinzufügen...

HighlightsPreferencePage.text.Delete=&Entfernen

HighlightsPreferencePage.text.Description=Kennzeichnungsliste:

HighlightsPreferencePage.text.Edit=&Bearbeiten...

HighlightsPreferencePage.toolTipText.Up=Markierung nach oben verschieben

HighlightsPreferencePage.toolTipText.Down=Markierung nach unten verschieben

HighlightsPreferencePage.text.Duplicate=&Doppelt vergeben

HighlightsPreferencePage.toolTipText.Duplicate=Doppelte Markierung


###########################################################
#HyperlinkBuilder properties
HyperlinkBuilder.Column.Parameters=Parameter

HyperlinkBuilder.DrillThrough.SelectTargetReport=Zielbericht auswählen

HyperlinkBuilder.DrillThrough.SelectTargetAnchor=Zielanker auswählen

HyperlinkBuilder.DrillThrough.CreateLinkExpr=Verknüpfungsausdruck erstellen, der mit dem Ziellesezeichen übereinstimmt

HyperlinkBuilder.DrillThrough.ShowTargetReport=Zielbericht anzeigen in

HyperlinkBuilder.DrillThrough.SelectFormat=Format für Zielbericht auswählen

HyperlinkBuilder.DrillThrough.Tooltip=Kontexthilfe

HyperlinkBuilder.Step.1=Schritt 1:

HyperlinkBuilder.Step.2=Schritt 2:

HyperlinkBuilder.Step.3=Schritt 3:

HyperlinkBuilder.Step.4=Schritt 4:

HyperlinkBuilder.Step.5=Schritt 5:

HyperlinkBuilder.Step.6=Schritt 6:

HyperlinkBuilder.ParameterRequired=Erforderlich

HyperlinkBuilder.ParamterBuilder.Add.Title=Hyperlink-Parameter hinzufügen

HyperlinkBuilder.ParamterBuilder.Edit.Title=Hyperlink-Parameter bearbeiten

HyperlinkBuilder.Column.DataType=Datentyp

HyperlinkBuilder.Column.Values=Werte

HyperlinkBuilder.DialogTitle=Optionen für Hyperlink

HyperlinkBuilder.ErrorMessage.InvalidReport=Ziel-Berichtdesign kann nicht aufgelöst werden, vordefinierte Parameterinformationen nicht verfügbar.

HyperlinkBuilder.ErrorMessage.ReportReqired=Sie müssen die Berichtdatei angeben.

HyperlinkBuilder.DrillThroughTargetBookmark=&Ziellesezeichen

HyperlinkBuilder.DrillThroughLinkExpression=Verknüpfungs&ausdruck:

HyperlinkBuilder.Label.Bookmark=&Lesezeichen:

HyperlinkBuilder.Label.Format=Format:

HyperlinkBuilder.Label.LinkedExpression=Verknüpfter &Ausdruck:

HyperlinkBuilder.DrillThroughTargetToc=In&haltsverzeichniseintrag im Zielbericht

HyperlinkBuilder.DrillThroughSameFrame=&Gleicher Rahmen

HyperlinkBuilder.DrillThroughNewWindow=&Neues Fenster

HyperlinkBuilder.DrillThroughWholePage=&Ganze Seite

HyperlinkBuilder.DrillThroughParentFrame=&Übergeordneter Rahmen

HyperlinkBuilder.DrillThrough.ErrorMsg.DuplicateParameterName=Parametername doppelt vergeben

HyperlinkBuilder.Label.Location=&Speicherort:

HyperlinkBuilder.Label.Location.hint=(Beispiele: http://host/path, folder/file)

HyperlinkBuilder.Label.Parameters=Berichtparameter:

HyperlinkBuilder.Label.Report=Bericht:

HyperlinkBuilder.Label.SelectType=Art des Hyperlinks wählen:

HyperlinkBuilder.DrillThroughHtml=HTML
HyperlinkBuilder.Label.Target=&Ziel:

HyperlinkBuilder.Label.Tooltip=Kontexth&ilfe:

HyperlinkBuilder.Radio.Bookmark=Internes Lesezeiche&n

HyperlinkBuilder.DrillThroughPdf=PDF
HyperlinkBuilder.Radio.DrillThrough=&Drill-through

HyperlinkBuilder.ReportDesignButton=&Berichtdesign:

HyperlinkBuilder.ReportDocumentButton=Berichtdo&kument:

HyperlinkBuilder.Radio.None=K&eine Verknüpfung

HyperlinkBuilder.Radio.Uri=&URI
HyperlinkBuilder.BrowseForFile=Nach Datei suchen

HyperlinkBuilder.Button.AddParam=&Hinzufügen...

HyperlinkBuilder.Button.EditParam=Bearbeiten&...

HyperlinkBuilder.Button.RemoveAllParam=A&lles entfernen

HyperlinkBuilder.Button.RemoveParam=Ent&fernen

HyperlinkBuilder.OpenExpression=Ausdruckserstellung öffnen

HyperlinkBuilder.FileNameError.Title=Fehler

HyperlinkBuilder.FileNameError.Message=Die ausgewählte Datei ist ungültig.

HyperlinkBuilder.TargetReportFormat=Zielbericht &formatieren in:

HyperlinkParameterBuilder.Label.DataType=Datentyp:

HyperlinkParameterBuilder.Label.Parameter=&Parameter:

HyperlinkParameterBuilder.Label.Value=&Wert:

HyperlinkParameterBuilder.Lable.Required=Erforderlich:

HyperlinkParameterBuilder.Required.Choice.No=Nein

HyperlinkParameterBuilder.Required.Choice.Yes=Ja

###########################################################
#HyperLinkPage properties
HyperLinkPage.Label.LnikTo=&Verknüpfen mit:

HyperLinkPage.Label.None=Kein

HyperLinkPage.Menu.Save=Hyperlink ändern

HyperLinkPage.Button.Text=Bearbei&ten...

HyperLinkPage.toolTipText.Button=Optionen für Öffnen des Hyperlink

###########################################################
#I18nPage properties
I18nPage.text.Note=Hinweis: Um die Lokalisierung einzustellen, wählen Sie den Bericht aus, und legen Sie den Namen der Ressourcendatei im Eigenschaftseditor auf der Registerkarte 'Ressourcen' fest.

###########################################################
#ImageBuilder properties
ImageBuilder.Chooser.Title=Bilddatei öffnen

ImageBuilder.DialogTitle.Edit=Bildobjekt bearbeiten

ImageBuilder.DialogTitle.ImportFailed=Import fehlgeschlagen

ImageBuilder.DialogTitle.LoadingFailed=Bild konnte nicht geladen werden

ImageBuilder.DialogTitle.New=Neues Bildobjekt

ImageBuilder.ErrorMessage.FileLoaded=Dateiname bereits vorhanden. Bitte ändern und neu laden.

ImageBuilder.ErrorMessage.LoadingFailed=Das Bild kann nicht korrekt geladen werden.

ImageBuilder.BrowserResourceDialog.Title=Nach Bilddateien suchen

ImageBuilder.BrowserResourceDialog.Message=Wählen Sie die Bilddateien aus dem Ressourcenpfadordner aus.

ImageBuilder.ErrorMessage.OutOfMemory=Das Bild ist zu groß für den verfügbaren Speicher.

ImageBuilder.FileDialog.FilterMessage=Unterstützte Bilddatei

ImageBuilder.Button.Browser=Durchsuchen...

ImageBuilder.Button.Import=&Bild hinzufügen...

ImageBuilder.Button.SelectImageData=Bilddaten auswä&hlen...

ImageBuilder.Button.Insert=Ein&fügen

ImageBuilder.Label.SelectFrom=Bild auswählen von...

ImageBuilder.Label.EnterURI=URI ei&ngeben:

ImageBuilder.Label.EnterResourceFile=Ressourcendatei ei&ngeben:

ImageBuilder.Label.EnterExpr=&Ausdruck für dynamisches Bild eingeben:

ImageBuilder.Type.URI=&URI
ImageBuilder.Type.ResourceFile=&Bilddatei in gemeinsamen Ressourcen

ImageBuilder.Type.EmbededImage=&Eingebettetes Bild

ImageBuilder.Type.DynamicImage=&Dynamisches Bild

ImageBuilder.Description.URI=Verknüpfen mit Bild im Internet

ImageBuilder.Description.ResourceFile=Verknüpfen mit Bild in gemeinsamen Ressourcen

ImageBuilder.Description.Embedded=Bild in Bericht ei&nbetten

ImageBuilder.Description.Dynamic=Bild aus Datensatz (Datenbanken) abrufen

ImageBuilder.Button.Browser.Tooltip=Bilddatei durchsuchen

ImageBuilder.Reload.Tooltip=Bild neu laden

###########################################################
#ImageEditPart properties
ImageEditPart.trans.editImage=Bild ändern

###########################################################
#ImagePageGenerator properties
ImagePageGenerator.List.AltText=Alternativer Text

ImagePageGenerator.List.Bookmark=Lesezeichen

ImagePageGenerator.List.Borders=Rahmen

ImagePageGenerator.List.General=Allgemein

ImagePageGenerator.List.HyperLink=Hyperlink

ImagePageGenerator.List.Reference=Referenz

ImagePageGenerator.List.Section=Seitenumbruch

ImagePageGenerator.List.TOC=Inhaltsverzeichnis

ImagePageGenerator.List.Visibility=Sichtbarkeit

###########################################################
#ImportCSSStyleAction properties
ImportCSSStyleAction.text=CSS-Stil &importieren...

###########################################################
#ImportLibraryAction properties
ImportLibraryAction.Error.CannotBeEmpty=Der Namensraum darf nicht leer sein.

ImportLibraryAction.Error.HasBeenUsed=Der Namensraum wird bereits verwendet.

ImportLibraryAction.Error.HasDot=Der Namensraum darf keinen Punkt ('.') enthalten.

ImportLibraryAction.Message.ImportSuccessfully=Die Bibliothek {0} wurde importiert.

ImportLibraryAction.Message=Diese Bibliothek für das Berichtdesign verwenden?

ImportLibraryAction.Namespace=Namensraum:

ImportLibraryAction.Prefix=Präfix:

ImportLibraryAction.Title.ImportSuccessfully=Importieren erfolgreich

ImportLibraryAction.Dialog.Titile=Bibliothek verwenden

ImportLibraryAction.Dialog.Message=Wählen Sie eine Bibliothek, die in den Bericht aufgenommen werden soll.

###########################################################
#ImportLibraryDialog properties
ImportLibraryDialog.Title=Bibliothek verwenden

###########################################################
#ImportValueDialog properties
ImportValueDialog.Label.SelectColumn=&Spalte auswählen:

ImportValueDialog.Label.SelectDataSet=&Datensatz auswählen:

ImportValueDialog.Label.SelectValue=&Hinzuzufügenden Wert auswählen oder eingeben

ImportValueDialog.Title=Werte importieren

ImportValueDialog.Button.Add.ToolTip=Hinzufügen

ImportValueDialog.Button.AddAll.ToolTip=Alle hinzufügen

ImportValueDialog.Button.Remove.ToolTip=Entfernen

ImportValueDialog.Button.RemoveAll.ToolTip=Alle entfernen
###########################################################
#IncludeDetailAction properties
IncludeDetailAction.actionMsg.includeDetail=&Detail

###########################################################
#IncludeFooterAction properties
IncludeFooterAction.actionMsg.includeFooter=&Fußzeile

###########################################################
#IncludeHeaderAction properties
IncludeHeaderAction.actionMsg.includeHeader=&Kopfzeile

###########################################################
#IncludeListDetailAction properties
IncludeListDetailAction.actionMsg.includeDetail=Detail einschließen

###########################################################
#IncludeListFooterAction properties
IncludeListFooterAction.actionMsg.includeFooter=Fußzeile einschließen

###########################################################
#IncludeListHeaderAction properties
IncludeListHeaderAction.actionMsg.includeHeader=Kopfzeile einschließen

###########################################################
#IncludeTableGroupFooterAction properties
IncludeTableGroupFooterAction.actionMsg.includeFooter=Gruppenfußzeile

###########################################################
#IncludeTableGroupHeaderAction properties
IncludeTableGroupHeaderAction.actionMsg.includeHeader=Gruppenkopfzeile

###########################################################
#InputParameterDailog properties
InputParameterDailog.dialog.title.required=Erforderlicher Parameter – muss einen Wert aufweisen

InputParameterDailog.dialog.title=Parameter eingeben

InputParameterDailog.parameter.noparameter=Mit diesem Bericht ist kein Parameter verknüpft.

InputParameterDialog.ErrorMessage.InvalidFile=Der Bericht ist fehlerhaft.

InputParameterDailog.parameter.null=Null-Wert

InputParameterDialog.ErrorTitle.Error=Fehler

InputParameterDialog.boolean.checked=Ja

###########################################################
#InsertAction properties
InsertAction.text=Element ei&nfügen

###########################################################
#InsertColumnAction properties
InsertColumnAction.actionMsg.insert=Einfügen

###########################################################
#InsertColumnLeftAction properties
InsertColumnLeftAction.actionMsg.columnToLeft=Spalte nach &links

###########################################################
#InsertColumnRightAction properties
InsertColumnRightAction.actionMsg.columnToRight=Spalte nach &rechts

###########################################################
#InsertExpressionMenuAction properties
InsertExpressionMenuAction.text.Expression=Ausdruck...

###########################################################
#InsertGroupAction properties
InsertGroupAction.actionMsg.group=&Gruppe einfügen

InsertGroupAction.stackMsg.addGroup=Gruppe hinzufügen

###########################################################
#InsertGroupActionFactory properties
InsertGroupActionFactory.label.insertGroup=&Gruppe einfügen

###########################################################
#InsertInLayoutAction properties
InsertInLayoutAction.action.text=In Layout ei&nfügen

###########################################################
#InsertListGroupAction properties
InsertListGroupAction.actionMsg.insertGroup=Gruppe einfügen

InsertListGroupAction.stackMsg.insertListGroup=Listengruppe einfügen

###########################################################
#InsertPasteColumnAction properties
InsertPasteColumnAction.text=Kopierte &Spalte einfügen

###########################################################
#InsertPositionGroupAction properties
InsertPositionGroupAction.Label.Above=&Oben

InsertPositionGroupAction.Label.Below=&Unten

InsertPositionGroupAction.Label.Into=Hinein

###########################################################
#InsertRowAboveAction properties
InsertRowAboveAction.actionMsg.rowAbove=&Oben

###########################################################
#InsertRowAction properties
InsertRowAction.actionMsg.insert=E&infügen

###########################################################
#InsertRowBelowAction properties
InsertRowBelowAction.actionMsg.rowBelow=&Unten

###########################################################
#IReportGraphicConstants properties
IReportGraphicConstants.ImageType.All=Alle Bilddateien (*.BMP,*.JPG,*.JPEG,*.GIF,*.TIF,*.PNG,*.ICO)

IReportGraphicConstants.ImageType.Bmp=Bitmap-Dateien (*.BMP)

IReportGraphicConstants.ImageType.Gif=GIF-Dateien (*.GIF)

IReportGraphicConstants.ImageType.Icon=Symboldateien (*.ICO)

IReportGraphicConstants.ImageType.Jpg=JPEG-Dateien (*.JPG,*.JPEG)

IReportGraphicConstants.ImageType.Png=PNG-Dateien (*.PNG)

IReportGraphicConstants.ImageType.Tif=TIF-Dateien (*.TIF)

###########################################################
#JSDocumentProvider properties
JSDocumentProvider.Content.Default=Neuer Inhalt

JSDocumentProvider.document.newName=Neuer Inhalt

###########################################################
#JSEditor properties
JSEditor.cmb.NewEventFunction=Neue Ereignisfunktion...

JSEditor.Label.Script=Skript:

JSEditor.Button.Validate=Skript validieren

JSEditor.Button.Reset=Skript zurücksetzen

JSEditor.Button.Help=Beschreibung anzeigen

JSEditor.Display.DefaultText=//JS Script-Editor \n

JSEditor.Display.NoExpression=Dieses Element verfügt über keine Methoden.

JSEditor.Input.trial=Test

JSEditor.Text.NoText=Kein Text

JSEditor.Text.NoDescription=Diese Methode hat keine Beschreibung.

JSEditor.Validate.Title=Gültigkeit

JSEditor.Validate.NoError=Im aktuellen Skript wurde kein Syntaxfehler gefunden.

JSEditor.Folding.Group=Folding

JSEditor.Folding.ExpandAll.label=Alle erweitern

JSEditor.Folding.CollapseAll.label=Alle ausblenden

JSEditor.Folding.CollapseMethods.label=Methoden ausblenden

JSEditor.Folding.CollapseComments.label=Kommentare ausblenden

###########################################################
#label properties
label.defaultValue=(Standardwert)

label.input=Eingabe

label.inputOutput=Eingabe/Ausgabe

label.no=Nein

label.output=Ausgabe

label.redo=Wiederherstellen

label.undo=Rückgängig

label.yes=Ja

###########################################################
#LabelEditPart properties
LabelEditPart.Figure.Default=Beschriftung

###########################################################
#LabelPageGenerator properties
LabelPageGenerator.List.Bookmark=Lesezeichen

LabelPageGenerator.List.Borders=Rahmen

LabelPageGenerator.List.General=Allgemein

LabelPageGenerator.List.HyperLink=Hyperlink

LabelPageGenerator.List.I18n=Lokalisierung

LabelPageGenerator.List.Margin=Rand

LabelPageGenerator.List.Padding=Umrandungsfüllung

LabelPageGenerator.List.Section=Seitenumbruch

LabelPageGenerator.List.TOC=Inhaltsverzeichnis

LabelPageGenerator.List.Visibility=Sichtbarkeit

###########################################################
#LayoutConfigurationBlock properties
LayoutConfigurationBlock.Default.Unit.Auto=Automatisch

LayoutConfigurationBlock.Default.Unit.Label=&Standardeinheit:
LayoutConfigurationBlock.Default.Layout.Label=Standardlayout:
LayoutConfigurationBlock.Default.Orientation.Label=Standardausrichtung:

###########################################################
#LayoutEditor properties
LayoutEditor.ErrorMessage.OpenError=Beim Öffnen des Editors ist ein Fehler aufgetreten.

LayoutEditor.Page.Layout=Normal

LayoutEditor.Page.MasterPage=Master-Seite

LayoutEditor.Page.Source=XML-Quelldatei

###########################################################
#LayoutPageAction properties
LayoutPageAction.actionText.layout=Layout

###########################################################
#LibrariesNodeProvider properties
LibrariesNodeProvider.DisplayLabel.Libraries=Bibliotheken

###########################################################
#LibraryExplorerView properties
LibraryExplorerView.defaultText.notAvailable=Ressourcen nicht verfügbar.

###########################################################
#LibraryExplorerProvider properties
LibraryExplorerProvider.FolderNotExist=Der in der Einstellung definierte BIRT-Ressourcenordner ist nicht vorhanden.

###########################################################
#LibraryPage properties
LibraryPage.Label.Namespace=&Namespace:

LibraryPage.Label.Path=&Pfad:

###########################################################
#LibraryProvider properties
LibraryProvider.openDefinedLibrary.error.dialog.message=Fehler beim Öffnen der angegebenen Bibliothek

LibraryProvider.openDefinedLibrary.error.dialog.title=Fehler

###########################################################
#ListBandControlFigure properties
ListBandControlFigure.tooltip.GroupFooter=Listengruppe - Fußzeile

ListBandControlFigure.tooltip.GroupHeader=Listengruppe - Kopfzeile

ListBandControlFigure.tooltip.ListDetail=Liste - Detail

ListBandControlFigure.tooltip.ListFooter=Liste - Fußzeile

ListBandControlFigure.tooltip.ListHeader=Liste - Kopfzeile

###########################################################
#ListBandProvider properties
ListBandProvider.action.text=Gruppe e&infügen

###########################################################
#ListBandProxy properties
ListBandProxy.list.detail=Detail

ListBandProxy.list.footer=Fußzeile

ListBandProxy.list.groupFooter=GF

ListBandProxy.list.groupHeader=GK

ListBandProxy.list.header=Kopfzeile

###########################################################
#ListContainProcess properties
ListContainProcess.action.New=Neu....

ListContainProcess.action.NewRow=Neue Zeile

ListContainProcess.action.NewSubSection=Neuer Teilbereich

###########################################################
#ListEditPart properties
ListEditPart.GUIDEHANDLE_TEXT = Liste

###########################################################
#ListFigure properties
ListFigure.BORDER_TEXT=Liste

###########################################################
#ListHandleAdapt properties
ListHandleAdapt.transLabel.insertGroup=Gruppe einfügen

###########################################################
#ListPageGenerator properties
ListPageGenerator.List.Bookmark=Lesezeichen

ListPageGenerator.List.Borders=Rahmen

ListPageGenerator.List.Font=Schriftart

ListPageGenerator.List.General=Allgemein

ListPageGenerator.List.Section=Seitenumbruch

ListPageGenerator.List.TOC=Inhaltsverzeichnis

ListPageGenerator.List.Visibility=Sichtbarkeit


###########################################################
#MapPage properties
MapPage.label.add=&Hinzufügen...

MapPage.label.condition=Bedingung

MapPage.label.delete=&Entfernen

MapPage.label.displayValue=Angezeigter Wert:

MapPage.label.mapList=Zuordnungsliste:

MapPage.transName.addMapRule=Zuordnungsregel hinzufügen

MapPage.transName.deleteMapRule=Zuordnungsregel löschen

MapPage.transName.editMapRule=Zuordnungsregel bearbeiten

MapPage.transName.moveDownRule=Zuordnungsregel nach unten verschieben

MapPage.transName.moveUpMapRule=Zuordnungsregel nach oben verschieben

MapPage.toolTipText.moveUp=Zuordnungsregel nach oben verschieben

MapPage.toolTipText.moveDown=Zuordnungsregel nach unten verschieben

MapPage.label.duplicate=Dupli&kate

MapPage.toolTipText.duplicate=Doppelte Zuordnungsregel

MapPage.transName.Duplicate=Doppelte Zuordnungsregel

###########################################################
#MapPreferencePage properties
MapPreferencePage.displayname.Condition=Bedingung

MapPreferencePage.displayname.DisplayValue=Angezeigter Wert

MapPreferencePage.displayname.Title=Zuordnung

MapPreferencePage.text.Add=&Hinzufügen...

MapPreferencePage.text.Delete=&Entfernen

MapPreferencePage.text.Description=Zuordnungsregeln:

MapPreferencePage.text.Edit=&Bearbeiten...

MapPreferencePage.toolTipText.Up=Zuordnungsregel nach oben verschieben

MapPreferencePage.toolTipText.Down=Zuordnungsregel nach unten verschieben

MapPreferencePage.text.Duplicate=&Doppelt vergeben

MapPreferencePage.toolTipText.Duplicate=Doppelte Zuordnungsregel

###########################################################
#MapRuleBuilderDialog properties
MapRuleBuilder.button.add=&Hinzufügen

MapRuleBuilder.button.add.tooltip=Wert zur Werteliste hinzufügen

MapRuleBuilder.button.delete=Entfe&rnen

MapRuleBuilder.button.delete.tooltip=Ausgewählten Wert aus Liste löschen

MapRuleBuilder.button.deleteall=Alle entf&ernen

MapRuleBuilder.button.deleteall.tooltip=Alle Werte aus Liste entfernen

MapRuleBuilder.button.edit=B&earbeiten...

MapRuleBuilder.button.edit.tooltip=Ausgewählten Wert in Werteliste bearbeiten

MapRuleBuilder.DialogTitle.New=Neue Zuordnungsregel

MapRuleBuilder.DialogTitle.Edit=Zuordnungsregel bearbeiten

MapRuleBuilder.Button.ResourceKey=Ressourcen&schlüssel:

MapRuleBuilder.Button.Reset=&Zurücksetzen

MapRuleBuilder.Button.Browse=&Durchsuchen...

MapRuleBuilder.Button.Browse.Tooltip=Durchsuchen und Schlüssel auswählen

MapRuleBuilder.label.value=&Wert:

MapRuleBuilder.list.item1=Wert

MapRuleBuilder.MsgDlg.Msg=Warnung

MapRuleBuilder.MsgDlg.Title=Der Wert darf nicht leer sein.

MapRuleBuilder.text.AND=AND

MapRuleBuilderDialog.text.Between=ist zwischen

MapRuleBuilderDialog.text.Condition=&Wenn folgende Bedingung wahr ist:

MapRuleBuilderDialog.text.Display=&Dann wird folgender Wert angezeigt:

MapRuleBuilderDialog.text.ET=ist gleich

MapRuleBuilderDialog.text.GE=größer als oder gleich

MapRuleBuilderDialog.text.GT=ist größer als

MapRuleBuilderDialog.text.LE=ist kleiner als oder gleich

MapRuleBuilderDialog.text.Like=ist wie

MapRuleBuilderDialog.text.LT=ist kleiner als

MapRuleBuilderDialog.text.NE=ist nicht gleich

MapRuleBuilderDialog.text.NotBetween=ist nicht zwischen

MapRuleBuilderDialog.text.NotNull=ist nicht Null

MapRuleBuilderDialog.text.Null=ist Null

MapRuleBuilderDialog.text.Title=Übersicht

MapRuleBuilderDialog.MsgDlg.Title=Warnung

MapRuleBuilderDialog.MsgDlg.Msg=Der Wert darf nicht leer sein.

###########################################################
#SortkeyBuilder dialog properties
SortkeyBuilder.DialogTitle.New=Neuer Sortierschlüssel

SortkeyBuilder.DialogTitle.Edit=Sortierschlüssel bearbeiten

SortkeyBuilder.DialogMessage.New=Füllen Sie die folgenden Inhalte aus, um einen Sortierschlüssel zu erstellen.

SortkeyBuilder.DialogMessage.Edit=Bearbeiten Sie die folgenden Inhalte, um den Sortierschlüssel zu aktualisieren.

SortkeyBuilder.DialogTitle.Label.Prompt=Sortierschlüssel eingeben:

SortkeyBuilder.DialogTitle.Label.Key=&Schlüssel:

SortkeyBuilder.DialogTitle.Label.Direction=&Richtung:

SortkeyBuilder.Label.Locale=&Gebietsschema:

SortkeyBuilder.Label.Strength=&Stärke:

SortkeyBuilder.DialogTitle.Button.ExpressionBuilder=Ausdruckserstellung öffnen

SortkeyBuilder.DialogTitle.Error.SetSortKey.Title=Fehler

SortkeyBuilder.Locale.Auto=Automatisch

SortkeyBuilder.Strength.ASCII=ASCII
SortkeyBuilder.Strength.PRIMARY=Primär

SortkeyBuilder.Strength.SECONDARY=Sekundär

SortkeyBuilder.Strength.TERTIARY=Tertiär

SortkeyBuilder.Strength.QUATERNARY=Quartär

SortkeyBuilder.Strength.IDENTICAL=Identisch

###########################################################
#FilterConditionBuilder dialog properties
FilterConditionBuilder.DialogTitle.New=Neue Filterbedingung

FilterConditionBuilder.DialogTitle.Edit=Filterbedingung bearbeiten

FilterConditionBuilder.DialogMessage.New=Füllen Sie die folgenden Inhalte aus, um eine Filterbedingung zu erstellen.

FilterConditionBuilder.DialogMessage.Edit=Bearbeiten Sie die folgenden Inhalte, um die Filterbedingung zu aktualisieren.

FilterConditionBuilder.choice.ValueOfThisDataItem=Wert dieses Datenelements

FilterConditionBuilder.tooltip.ExpBuilder=Ausdruckserstellung öffnen

FilterConditionBuilder.text.AND=UND

FilterConditionBuilder.text.Condition=Filterbedingung eingeben:

FilterConditionBuilder.Shell.Title=Filterbedingung

FilterConditionBuilder.list.item1=Wert

FilterConditionBuilder.button.edit=B&earbeiten...

FilterConditionBuilder.button.delete=Ent&fernen

FilterConditionBuilder.button.deleteall=Alle entf&ernen

FilterConditionBuilder.button.add=&Hinzufügen

FilterConditionBuilder.button.edit.tooltip=Ausgewählten Wert in Werteliste bearbeiten

FilterConditionBuilder.button.delete.tooltip=Ausgewählten Wert aus Liste löschen

FilterConditionBuilder.button.deleteall.tooltip=Alle Werte aus Liste löschen

FilterConditionBuilder.button.add.tooltip=Wert zur Werteliste hinzufügen

FilterConditionBuilder.label.value=&Wert:

FilterConditionBuilder.multipleValue.label=&Wert

FilterConditionBuilder.multipleValue.button.add=&Hinzufügen

FilterConditionBuilder.multipleValue.button.remove=Entfe&rnen

FilterConditionBuilder.multipleValue.button.removeAll=Alle ent&fernen

FilterConditionBuilder.multipleValue.button.edit=&Bearbeiten...

FilterConditionBuilder.Button.UpdateAggregation=&Summen neu berechnen

###########################################################
#MarignPropertyDescriptor properties
MarignPropertyDescriptor.error.message=Der eingegebene Wert ist zu groß.

MarignPropertyDescriptor.error.title=Wert zu groß

###########################################################
#MasterPageAction properties
MasterColumnsPropertyDescriptor.Button.Text.Custom=&Benutzerdefiniert

MasterColumnsPropertyDescriptor.Button.Text.OneColumn=&Eine

MasterColumnsPropertyDescriptor.Button.Text.ThreeColumns=D&rei

MasterColumnsPropertyDescriptor.Button.Text.TwoColumns=&Zwei

MasterColumnsPropertyDescriptor.Combo.Text.Column.Number=A&nzahl der Spalten:

MasterPageAction.actionText.masterPage=Master-Seite

###########################################################
#MasterPageGenerator properties
MasterPageGenerator.List.Columns=Spalten

MasterPageGenerator.List.General=Allgemein

MasterPageGenerator.List.Borders=Rahmen

MasterPageGenerator.List.Header&Footer=Kopfzeile/Fußzeile

MasterPageGenerator.List.Margins=Seitenrand

###########################################################
#MasterPageGeneralPage properties
MasterPageGeneralPage.Trans.SetType=Typ festlegen

###########################################################
#MasterPagePaletteFactory properties
MasterPagePaletteFactory.Autotext.TotalPageCount=Gesamt-Seitenzähler

MasterPagePaletteFactory.Autotext.Author_Page_Date=Verfasser\Nr., Seite\Nr., Datum\Nr.

MasterPagePaletteFactory.Autotext.Confidential_Page=Vertraulich, Seite\Nr.

MasterPagePaletteFactory.Autotext.CreatedBy=Erstellt von

MasterPagePaletteFactory.Autotext.CreatedOn=Erstellt für

MasterPagePaletteFactory.Autotext.Date=Datum

MasterPagePaletteFactory.Autotext.Filename=Dateiname

MasterPagePaletteFactory.Autotext.Label=AutoText
MasterPagePaletteFactory.Autotext.LastPrinted=Zuletzt ausgedruckt

MasterPagePaletteFactory.Autotext.Page=Seite

MasterPagePaletteFactory.Autotext.PageXofY=Seite n von m

MasterPagePaletteFactory.AutotextTooltip.TotalPageCount=Berichtobjekt des Gesamt-Seitenzählers

MasterPagePaletteFactory.AutotextTooltip.AuthorPageDate=Verfasser-Nr., Datumsnr.-Berichtobjekt

MasterPagePaletteFactory.AutotextTooltip.ConfidentialPage=Vertrauliches Berichtobjekt

MasterPagePaletteFactory.AutotextTooltip.CreatedBy=Erstellt durch Berichtobjekt

MasterPagePaletteFactory.AutotextTooltip.CreatedOn=Erstellt für Berichtobjekt

MasterPagePaletteFactory.AutotextTooltip.Date=Datum des Berichtobjekts

MasterPagePaletteFactory.AutoTextTooltip.Filename=Dateiname des Berichtobjekts

MasterPagePaletteFactory.AutotextTooltip.LastPrinted=Zuletzt gedrucktes Berichtobjekt

MasterPagePaletteFactory.AutotextTooltip.Page=Seite des Berichtsobjekts

MasterPagePaletteFactory.AutotextTooltip.PageXofY=Seite n von m Berichtobjekt

MasterPagePaletteFactory.elementLabel.authorPageDate=Verfasser#, Seite#, Datum

MasterPagePaletteFactory.elementLabel.confidentialPage=Vertraulich, Seite

MasterPagePaletteFactory.elementLabel.createdBy=Erstellt von

MasterPagePaletteFactory.elementLabel.createdOn=Erstellt für

MasterPagePaletteFactory.elementLabel.date=Datum

MasterPagePaletteFactory.elementLabel.fileName=Dateiname

MasterPagePaletteFactory.elementLabel.lastPrinted=Zuletzt ausgedruckt

MasterPagePaletteFactory.elementLabel.page=Seite

MasterPagePaletteFactory.elementLabel.pageOf=Seite n von m

MasterPagePaletteFactory.paletteDrawerName.autoText=AutoText
MasterPagePaletteFactory.ToolTip.Grid=Rasterberichtobjekt

MasterPagePaletteFactory.ToolTip.Text=Textberichtobjekt

MasterPagePaletteFactory.Autotext.Variable=Variable

MasterPagePaletteFactory.AutotextTooltip.Variable=Variablenelement

###########################################################
#MasterPageProcess properties
MasterPageProcess.action.Edit=Bearbeiten

MasterPageProcess.action.New=Neu

MasterPageProcess.text.CannotDelete=Das Design enthält nur eine Master-Seite, die nicht gelöscht werden kann.

MasterPageProcess.text.Display=Zeigt das Dialogfeld zur Seiteneinrichtung an

MasterPageProcess.text.DisplayName=MasterPage

###########################################################
#MergeAction properties
MergeAction.actionMsg.merge=&Zellen verbinden

###########################################################
#ModulePage properties
ModulePage.ErrorMessage.Duplicate=Der Name wird bereits verwendet.

ModulePage.ErrorMessage.WrongExtensionName=Der Name muss auf ".properties" enden.

ModulePage.ErrorMessage.InfoExtensionName=Geben Sie einen Namen ein, der mit ".properties" endet.

ModulePage.text.Add=&Hinzufügen...

ModulePage.text.ExistingFile=Vorhandene Ressourcendateien:

ModulePage.title.AddNewFile=Neue Ressourcendatei hinzufügen

ModulePage.Resourcefile.Dialog.Title=Ressourcendateien durchsuchen

ModulePage.Resourcefile.Dialog.Message=Wählen Sie eine Ressourcendatei oder einen Ordner aus, und geben Sie zum Erstellen einen neuen Dateinamen ein.

ModulePage.text.Path=&Pfad:

###########################################################
#MultiPageEditorContributor properties
MultiPageEditorContributor.actionText.sampleAction=Beispielaktion

MultiPageEditorContributor.actionToolTip.sampleActionToolTip=Kontexthilfe für Beispielaktion

MultiPageEditorContributor.actionToolTipText.sampleActionToolTip=Kontexthilfe für Beispielaktion

MultiPageEditorContributor.dialogMsg.sampleActionExecuted=Beispielaktion ausgeführt

MultiPageEditorContributor.menuText.editorMenu=Editor&menü

###########################################################
#NewLibraryWizard properties
NewLibraryWizard.choicePageTitle.report=Bibliothek

NewLibraryWizard.displayName.NewReportFileExtension=.rptlibrary
NewLibraryWizard.displayName.NewReportFileNamePrefix=NEUE_BIBLIOTHEK

NewLibraryWizard.error.NoProject=Kein geöffnetes Projekt im Arbeitsbereich vorhanden. Erstellen Sie ein neues Projekt über "Datei -> Neu -> Projekt", oder öffnen Sie ein vorhandenes Projekt.

NewLibraryWizard.pageDescription.createNewReport=Neue Bibliothek erstellen

NewLibraryWizard.taskName.openFileForEdit=Datei zum Bearbeiten wird geöffnet...

NewLibraryWizard.text.ConfirmSwitchPerspective=Wechsel der Perspektive bestätigen

NewLibraryWizard.text.Container=Behälter "

NewLibraryWizard.text.CreateReport=Neue Bibliothek erstellen

NewLibraryWizard.text.Creating=Wird erstellt

NewLibraryWizard.text.DoesNotExist=" existiert nicht.

NewLibraryWizard.text.OpenFileForEditing=Datei zum Bearbeiten wird geöffnet...

NewLibraryWizard.text.SwitchPerspective=Dieser Dateityp ist mit der Perspektive {0} verknüpft. Möchten Sie nun zu dieser Perspektive wechseln?

NewLibraryWizard.title.Choose=Wählen Sie aus, wie die Bibliothek erstellt werden soll.

NewLibraryWizard.title.Error=Fehler

NewLibraryWizard.title.New=Neue Bibliothek

NewLibraryWizard.title.Report=Bibliothek

NewLibraryWizard.title.ReportWizard=Bibliotheksassistent

NewLibraryWizard.title.Template=TemplateChoicePage
NewLibraryWizard.title.WizardPage=WizardPage
NewLibraryWizard.widowTitle.new=Neu

NewLibraryWizard.wizardPageTitle.report=Bibliothek

###########################################################
#NewReportProjectWizard properties

NewReportProjectWizard.bin=bin
NewReportProjectWizard.caseVariantExistsError=Im Dateisystem wird Groß- und Kleinschreibung nicht unterschieden. Es ist bereits ein Projekt vorhanden, dessen Benennung zu ''{0}'' im Widerspruch steht.

NewReportProjectWizard.description=Neues Berichtprojekt erstellen

NewReportProjectWizard.errorMessage=Beim Erstellen des neuen Berichtprojekts ist ein Fehler aufgetreten.

NewReportProjectWizard.errorOpeningWindow=Beim Öffnen eines Fensters ist ein Fehler aufgetreten.

NewReportProjectWizard.internalError=Interner Fehler: {0}

NewReportProjectWizard.javaProject=Java-Projekt erstellen

NewReportProjectWizard.perspSwitchMessage=Diese Art von Projekt ist mit der Perspektive {0} verknüpft. Jetzt zu dieser Perspektive wechseln?

NewReportProjectWizard.perspSwitchTitle=Wechsel der Perspektive bestätigen

NewReportProjectWizard.projectSetting=Projekteinstellung

NewReportProjectWizard.src=src
NewReportProjectWizard.title=Berichtprojekt

NewReportProjectWizard.windowTitle=Neues Berichtprojekt

###########################################################
#NewReportWizard properties
NewReportWizard.choicePageTitle.report=Bericht

NewReportWizard.displayName.NewReportFileExtension=.rptdesign
NewReportWizard.displayName.NewReportFileNamePrefix=new_report
NewReportWizard.error.NoProject=Kein geöffnetes Projekt im Arbeitsbereich vorhanden. Erstellen Sie ein neues Projekt über "Datei -> Neu -> Projekt", oder öffnen Sie ein vorhandenes Projekt.

NewReportWizard.pageDescription.createNewReport=Neuen Bericht erstellen

NewReportWizard.pageDescription.selectReportTemplate=Wählen Sie den Berichtstyp aus, den Sie erstellen möchten.

NewReportWizard.taskName.openFileForEdit=Datei zum Bearbeiten wird geöffnet...

NewReportWizard.text.ConfirmSwitchPerspective=Wechsel der Perspektive bestätigen

NewReportWizard.text.Container=Behälter "

NewReportWizard.text.CreateReport=Neuen Bericht erstellen

NewReportWizard.text.Creating=Wird erstellt

NewReportWizard.text.DoesNotExist=" existiert nicht.

NewReportWizard.text.OpenFileForEditing=Datei zum Bearbeiten wird geöffnet...

NewReportWizard.text.SelectTemplate=Wählen Sie den Berichtstyp aus, den Sie erstellen möchten.

NewReportWizard.text.SwitchPerspective=Dieser Dateityp ist mit der Perspektive {0} verknüpft. Möchten Sie nun zu dieser Perspektive wechseln?

NewReportWizard.title.Choose=Wählen Sie aus, wie der Bericht erstellt werden soll.

NewReportWizard.title.Error=Fehler

NewReportWizard.title.New=Neuer Bericht

NewReportWizard.title.Report=Bericht

NewReportWizard.title.ReportWizard=Berichtsassistent

NewReportWizard.title.Template=TemplateChoicePage
NewReportWizard.title.WizardPage=WizardPage
NewReportWizard.widowTitle.new=Neu

NewReportWizard.wizard.msgDirErr=Das Zielverzeichnis ist nicht vorhanden und kann nicht erstellt werden.


###########################################################
#NewResourceFileDialog properties
NewResourceFileDialog.label.NewFile=&Neuer Dateiname:

NewResourceFileDialog.ErrorMessage=Dateiname muss auf ".properties" enden.

NewResourceFileDialog.ErrorMessageInvalid=Ungültiger Dateiname

NewResourceFileDialog.FileExists={0} ist bereits vorhanden. Überschreiben?

###########################################################
#ProjectFileDialog properties
ProjectFileDialog.ErrorMessage=Datei muss eine Datei sein, kein Verzeichnis.

ProjectFileDialog.Title=Projektdatei wählen

ProjectFileDialog.Message=Wählen Sie eine Datei im aktuellen Projekt.

ProjectFileDialog.Text.Menu=Menü

###########################################################
#ProjectSelectionDialog properties
ProjectSelectionDialog.Message=Wählen Sie das zu konfigurierende Projekt:

ProjectSelectionDialog.Text.SpecialSettings=Nur Projekte mit projektspezifischen Einstellungen anzeigen

ProjectSelectionDialog.Title=Projektspezifische Konfiguration

###########################################################
#ResourceFilterDialog properties
ResourceFilterDialog.Title=Filter für Berichtselemente

ResourceFilterDialog.Lable.Description=Filterbeschreibung:

ResourceFilterDialog.Lable.Viewer=Wählen Sie die von der Anzeige auszublendenden Elemente aus:

###########################################################
#NewSectionDialog properties
NewSectionDialog.displayname.Container=Behälter

NewSectionDialog.displayname.Data=Daten

NewSectionDialog.displayname.Grid=Raster

NewSectionDialog.displayname.Image=Bild

NewSectionDialog.displayname.Label=Beschriftung

NewSectionDialog.displayname.Line=Linie

NewSectionDialog.displayname.List=Liste

NewSectionDialog.displayname.Rectangle=Rechteck

NewSectionDialog.displayname.Table=Tabelle

NewSectionDialog.displayname.Text=Text

NewSectionDialog.text.Prefix=Verfügbare Bereiche:

NewSectionDialog.title.insertElement=Element einfügen

NewSectionDialog.Title=Element einfügen

###########################################################
#NewTemplateWizard properties
NewTemplateWizard.displayName.NewReportFileNamePrefix=NEUE_VORLAGE

NewTemplateWizard.pageDescription.createNewTemplate=Neue Berichtvorlage erstellen.

NewTemplateWizard.text.CreateReport=Neue Vorlage erstellen

NewTemplateWizard.text.Creating=Wird erstellt

NewTemplateWizard.text.OpenFileForEditing=Vorlagendatei wird zum Bearbeiten geöffnet...

NewTemplateWizard.title.New=Neue Vorlage

NewTemplateWizard.title.Report=Bericht

NewTemplateWizard.title.Template=Vorlage

NewTemplateWizard.title.WizardPage=WizardPage

###########################################################
#NoneAction properties
NoneAction.text=Keine

###########################################################
#OutputColumnDefnPage properties
OutputColumnDefnPage.description=Definition für Ausgabespalten eingeben, die vom Abrufskript erstellt werden:

OutputColumnDefnPage.OutputColumns.Tooltip=Spalten innerhalb einer Datenzeile anzeigen und definieren

OutputColumnPage.error.createNewColumn=Beim Hinzufügen einer neuen Spalte ist ein Fehler aufgetreten.

OutputColumnPage.error.invalidSelection=Es ist kein gültiges Tabellenelement ausgewählt.

###########################################################
#ColumnDefPage properties
ColumnDefPage.description=Definitionsseite für Ausgabespalten von Skript-Datensatz

ColumnDefPage.title=Ausgabespalten

###########################################################
#ResultSetColumnPage properties
ResultSetColumnPage.button.add=&Hinzufügen...

ResultSetColumnPage.button.edit=&Bearbeiten...

ResultSetColumnPage.button.delete=&Löschen

ResultSetColumnPage.button.up=&Nach oben

ResultSetColumnPage.button.down=Nach &unten

ResultSetColumnPage.inputDialog.newColumn.title=Neue Skript-Datensatzspalte

ResultSetColumnPage.inputDialog.editColumn.title=Skript-Datensatzspalte bearbeiten

ResultSetColumnPage.inputDialog.label.columnName=&Spaltenname:

ResultSetColumnPage.inputDialog.label.dataType=Daten&typ:

ResultSetColumnPage.inputDialog.label.alias=Spalten&alias:

ResultSetColumnPage.inputDialog.label.displayName=&Anzeigename:

ResultSetColumnPage.inputDialog.label.displayNameKey=Namens&schlüssel anzeigen:

ResultSetColumnPage.inputDialog.warning.emptyColumnName=Eingabe des Spaltennamens erforderlich.

ResultSetColumnPage.inputDialog.error.sameValue.columnNameAndAlias=Spaltenname und Spaltenalias dürfen nicht identisch sein.

ResultSetColumnPage.inputDialog.error.duplicatedColumnName=Spaltenname '{0}' wurde bereits verwendet.

ResultSetColumnPage.inputDialog.error.duplicatedAlias=Spaltenalias '{0}' wurde bereits verwendet.

###########################################################
#OutputColumnsPage properties
OutputColumnPage.OutputColumns.Tooltip=Spalten innerhalb einer Datenzeile anzeigen und definieren

OutputColumnPage.toolTipText.Edit=Ausgabespalte bearbeiten

OutputColumnPage.title.OutputColumnInputDialog=Ausgabespalte

OutputColumnPage.title.OutputColumnInputDialog.NewOutputColumn=Neue Ausgabespalte

OutputColumnPage.title.OutputColumnInputDialog.EditOutputColumn=Ausgabespalte bearbeiten

OutputColumnPage.OutputColumns.DuplicatedName=Der Name existierte bereits!

OutputColumnsPage.description=Ausgabespalten definieren:

OutputColumnsPage.DataTypeConvert.Note=Hinweis

OutputColumnsPage.DataTypeConvert.Message=Eine solche Konvertierung kann zu Genauigkeitsverlust oder einem Laufzeitfehler führen. Möchten Sie sie wirklich durchführen?

###########################################################
#PageBreakPreferencePage properties
PageBreakPreferencePage.displayname.Title=Seitenumbruch

###########################################################
#PageSetAction properties
PageSetAction.text.code=&Skript

PageSetAction.Text.EditCode=&Skript bearbeiten...

PageSetAction.text.layout=&Layout

PageSetAction.text.master=&Master

PageSetAction.text.normal=&Normal

PageSetAction.text.preview=&Vorschau

###########################################################
#PageSetupOutlineProcess properties
PageSetupOutlineProcess.action.Edit=Seiteneinrichtung bearbeiten

PageSetupOutlineProcess.action.New=Neue MasterPage

PageSetupOutlineProcess.text.Display=Zeigt das Dialogfeld zur Seiteneinrichtung an

PageSetupOutlineProcess.text.Switch=Wechselt zum Seiten-Master-Editor

###########################################################
#ParameterDialog properties
ParameterDialog.Boolean.False=False

ParameterDialog.Boolean.True=True

ParameterDialog.Button.ChangeFormat=&Ändern...

ParameterDialog.Button.CreateDataSet=&Neu erstellen...

ParameterDialog.Button.ImportValue=&Werte importieren...

ParameterDialog.Button.RemoveDefault=St&andard entfernen

ParameterDialog.Button.SetDefault=&Als Standard einstellen

ParameterDialog.CheckBox.IsRequired=er&forderlich

ParameterDialog.CheckBox.DoNotEchoInput=Eingabe nicht wiederh&olen

ParameterDialog.CheckBox.Hidden=&Ausgeblendet

ParameterDialog.CheckBox.Distinct=Doppelte Werte zu&lassen

ParameterDialog.Choice.NoDefault=Kein Standardwert

ParameterDialog.Choice.NullValue=Null-Wert

ParameterDialog.Choice.BlankValue=Leerwert

ParameterDialog.Choice.None=Ohne

ParameterDialog.Choice.DisplayText=Text anzeigen

ParameterDialog.Choice.ValueColumn=Wertespalte

ParameterDialog.Choice.ASCENDING=Aufsteigend

ParameterDialog.Choice.DESCENDING=Absteigend

ParameterDialog.Choice.SelectValue=<Wert auswählen ...>

ParameterDialog.Column.Default=Standard

ParameterDialog.Column.DisplayText=Angezeigter Text

ParameterDialog.Column.DisplayTextKey=Anzeigentextschlüssel

ParameterDialog.Column.Value=Wert

ParameterDialog.DefaultValue.Add=&Hinzufügen

ParameterDialog.DisplayLabel.Combo=Kombinationsfeld

ParameterDialog.DisplayLabel.List=Listenfeld

ParameterDialog.ErrorMessage.CanootBeBlank=Der Wert darf nicht leer sein.

ParameterDialog.ErrorMessage.CanootBeNull=Der Wert darf nicht Null sein.

ParameterDialog.ErrorMessage.DuplicatedLabel=Der Anzeigetext ist bereits vorhanden.

ParameterDialog.ErrorMessage.DuplicatedLabelKey=Der Anzeigentextschlüssel existiert bereits.

ParameterDialog.ErrorMessage.DuplicatedName=Der Name wird bereits verwendet.

ParameterDialog.ErrorMessage.DuplicatedValue=Der Wert existiert bereits.

ParameterDialog.ErrorMessage.EmptyName=Name ist leer.

ParameterDialog.ErrorMessage.InvalidListLimit=Der Begrenzungswert sollte zwischen 0 und {0} liegen.

ParameterDialog.ErrorMessage.MismatchDataType=Der Wert stimmt nicht mit dem Datentyp überein.

ParameterDialog.ErrorMessage.NoAvailableColumn=Es sind keine Spalten verfügbar.

ParameterDialog.ErrorMessage.ValueColumnEmpty=Wertespalte muss angegeben werden.

ParameterDialog.ErrorMessage.NoDefaultValue=Sie müssen einen Standardwert angeben.

ParameterDialog.ErrorTitle.InvalidListLimit=Listenbegrenzung ungültig

ParameterDialog.ExpressionEditor.Title=Ausdruck bearbeiten

ParameterDialog.Flag.Default=Standard

ParameterDialog.Group.MoreOption=Anzeigen als

ParameterDialog.Group.Properties=Eigenschaften

ParameterDialog.Label.DataType=&Datentyp:

ParameterDialog.SelectionDialog.Edit=Auswahl bearbeiten

ParameterDialog.Label.DefaultValue=&Standardwert:

ParameterDialog.Label.DisplayPrompt=Anzeige-Aufforderung:

ParameterDialog.Label.DisplayType=&Anzeigetyp:

ParameterDialog.Label.Format=Formatieren als:

ParameterDialog.Label.HelpText=Hilfete&xt:

ParameterDialog.Label.ListOfValue=Auswahllistenwerte

ParameterDialog.Label.SortGroup=Sortieren

ParameterDialog.Label.Name=&Name:

ParameterDialog.Label.None=<Keine>

ParameterDialog.Label.Null=Null

ParameterDialog.Label.Preview=Vorschau mit Format

ParameterDialog.Label.PromptText=&Aufforderungstext:

ParameterDialog.Label.SelectDataSet=&Datensatz:

ParameterDialog.Label.SortKey=Sortieren &nach:

ParameterDialog.Label.SortDirection=Sortierrei&henfolge:

ParameterDialog.SelectionDialog.New=Neue Auswahl

ParameterDialog.SelectionDialog.Label.DisplayTextKey=Anzeigentext&schlüssel:

ParameterDialog.SelectionDialog.Label.DisplayText=&Anzeigentext:

ParameterDialog.SelectionDialog.Label.Value=&Wert:

ParameterDialog.SelectionDialog.Button.Resource=...
ParameterDialog.SelectionDialog.Button.Resource.Tooltip=Durchsuchen und Schlüssel auswählen

ParameterDialog.SelectionDialog.Button.Remove.Tooltip=Schlüssel entfernen

ParameterDialog.SelectionDialog.Label.Note=Hinweis: Um den Anzeigentextschlüssel einzustellen, wählen Sie den Bericht aus, und legen Sie den Namen der Ressourcendatei in der Registerkarte 'Ressourcen' im Eigenschaftseditor fest.

ParameterDialog.Label.SelectDisplayText=Angezeigten T&ext auswählen:

ParameterDialog.Label.SelectValueColumn=&Wertespalte auswählen:

ParameterDialog.Label.Value=Werte:

ParameterDialog.Label.values=Werte

ParameterDialog.Label.Sample=Beispiel

ParameterDialog.Label.Listlimit=L&istenbegrenzung:

ParameterDialog.Radio.Dynamic=D&ynamisch

ParameterDialog.Radio.Static=&Statisch

ParameterDialog.toolTipText.OpenExprButton=Ausdruckserstellung öffnen

ParameterDialog.datetime.prompt=Datums-/Uhrzeitwerte im folgenden Format eingeben: {0}

ParameterDialog.date.prompt=Kalenderdatenwerte im folgenden Format eingeben: {0}

ParameterDialog.time.prompt=Uhrzeitwerte im folgenden Format eingeben: {0}

ParameterDialog.Check.AllowMulti=Mehrere &Werte zulassen

ParameterDialog.checkBox.InputValue=Eingabewert

ParameterDialog.checkBox.BlankValue=Leerwert

ParameterDialog.checkBox.NullValue=Null-Wert

ParameterDialog.emptyColumnValueExpression.title=Ungültiger Wertspaltenausdruck.

ParameterDialog.emptyColumnValueExpression.message=Wertspaltenausdruck darf nicht leer sein.

ParameterDialog.Value.Empty=Leerer Wert

ParameterDialog.Value.Null=Null-Wert

ParameterDialog.message=Definieren Sie die Eigenschaften des Berichtparameters.

###########################################################
#SelectParameterDefaultValueDialog properties
SelectParameterDefaultValueDialog.Title=Wert auswählen

###########################################################
#ParameterDropListener properties
ParameterDropListener.Command.Copy=Element kopieren

ParameterDropListener.Command.Move=Element verschieben

###########################################################
#ParameterGroupDialog properties
ParameterGroupDialog.Page.Title=Allgemein

ParameterGroupDialog.pageTitle.general=Allgemein

###########################################################
#ParameterGroupGeneralPage properties
ParameterGroupGeneralPage.Label.DisplayName=&Anzeigename:

ParameterGroupGeneralPage.Label.Name=&Name:

###########################################################
#ParameterGroupNodeProvider properties
ParameterGroupNodeProvider.Action.GroupNew=Neue Parametergruppe

ParameterGroupNodeProvider.Action.ParameterEdit=&Bearbeiten

ParameterGroupNodeProvider.Action.ParameterNew=&Neuer Parameter

ParameterGroupNodeProvider.Dialogue.ParameterEdit=Parametergruppe bearbeiten

ParameterGroupNodeProvider.Dialogue.ParameterNew=Neuer Parameter

ParameterGroupNodeProvider.Node.Group=Parametergruppe

###########################################################
#ParameterGroupProcess properties
ParameterGroupProcess.action.Delete=Parametergruppe löschen

ParameterGroupProcess.action.Edit=Parametergruppe bearbeiten

ParameterGroupProcess.action.New=Neuer Parameter

ParameterGroupProcess.text.DisplayName=ParameterGroup
ParameterGroupProcess.text.Show=Blendet die Datenquelleditor-Seite ein

###########################################################
#ParameterNodeProvider properties
ParameterNodeProvider.dial.title.editCascading=Verschachtelungsparametergruppe bearbeiten

ParameterNodeProvider.dial.title.editScalar=Parameter bearbeiten

ParameterNodeProvider.dial.title.newCascading=Neue Verschachtelungsparametergruppe

ParameterNodeProvider.menu.text.edit=&Bearbeiten

ParameterNodeProvider.menu.text.new=Neuer Parameter

###########################################################
#ParameterProcess properties
ParameterProcess.action.Delete=Parameter löschen

ParameterProcess.action.Edit=Parameter bearbeiten

ParameterProcess.text.DisplayName=Parameter

ParameterProcess.text.Show=Blendet die Datenquelleditor-Seite ein

###########################################################
#parameters properties
parameters.datatypes.boolean=Boolesch

parameters.datatypes.dateTime=DateTime
parameters.datatypes.decimal=Decimal

parameters.datatypes.float=FLOAT

parameters.datatypes.string=Zeichenfolge

parameters.menuItem.reset=Zurücksetzen

###########################################################
#ParametersNodeProvider properties
ParametersNodeProvider.dialogue.title.group=Neue Parametergruppe

ParametersNodeProvider.dialogue.title.parameter=Neuer Parameter

ParametersNodeProvider.menu.text.cascadingParameter=Neue Verschachtel&ungsparametergruppe

ParametersNodeProvider.menu.text.group=Neue Para&metergruppe

ParametersNodeProvider.menu.text.parameter=Neuer P&arameter

###########################################################
#ParametersOutlineProcess properties
ParametersOutlineProcess.action.Edit=Bearbeiten

ParametersOutlineProcess.action.New=Neuer Parameter

ParametersOutlineProcess.action.NewGroup=Neue Gruppe

ParametersOutlineProcess.text.Open=Parameter-Editor öffnen

ParametersOutlineProcess.text.Show=Blendet die Datensatzeditor-Seite für den Datensatz ein

###########################################################
#PasteAction properties
PasteAction.dlg.title=Informationen

PasteAction.ok=OK

PasteAction.text=&Einfügen

###########################################################
#PlaceHolderEditPart properties
PlaceHolderEditPart.Trans.TransferToElement=An Berichtobjekt übertragen

###########################################################
#PreviewAction properties
PreviewAction.Text=Vorschau

###########################################################
#PreviewEditor properties
PreviewEditor.parameter.tooltip=Berichtparameter anzeigen

PreviewEditor.parameter.hint=Aktualisierungsbericht

PreviewEditor.parameter.note=Hinweis:

PreviewEditor.parameter.info=Die maximale Anzahl an Datenzeilen beträgt derzeit {0}.

PreviewEditor.parameter.change=Zum Ändern der Vorschaueinstellungen klicken.

PreviewEditor.ConfirmRefresh.Title=Aktualisierung bestätigen

PreviewEditor.ConfirmRefresh.Message=Der Vorgang kann das Vorschauergebnis beeinflussen. Möchten Sie die Vorschauseite aktualisieren?

PreviewEditor.browser.notfound=Interner Browser ist nicht verfügbar.

###########################################################
#PreviewPageAction properties
PreviewPageAction.actionText.preview=Vorschau

###########################################################
#PropertiesAction properties
PropertiesAction.Text=Eigenschaften

###########################################################
#PropertyHandleTableViewer properties
PropertyHandleTableViewer.Button.New=&Neu...

PropertyHandleTableViewer.Button.Edit=&Bearbeiten...

PropertyHandleTableViewer.Button.Remove=&Entfernen

PropertyHandleTableViewer.Button.Up=&Nach oben

PropertyHandleTableViewer.Button.Down=&Nach unten

PropertyHandleTableViewer.Menu.Remove=Entfernen

PropertyHandleTableViewer.Menu.Up=Nach oben

PropertyHandleTableViewer.Menu.Down=Nach unten

PropertyHandleTableViewer.Menu.RemoveAll=Alles entfernen

###########################################################
#PropertyHandleInputDialog properties
PropertyHandleInputDialog.Action.New=Neu

PropertyHandleInputDialog.Action.Edit=Bearbeiten

PropertyHandleInputDialog.messages.error.blankProperty={0} darf nicht leer sein.

###########################################################
#PropertyAndPreferencePage properties
PropertyAndPreferencePage.Text.Configure.Special.Settings=Projektspezifische Einstellungen konfigurieren...

PropertyAndPreferencePage.Text.Configure.Workspace.Settings=Arbeitsplatz-Einstellungen konfigurieren...

PropertyAndPreferencePage.Text.Enable.SpecialSettings=Pr&ojektspezifische Einstellungen aktivieren

###########################################################
#PublishTemplateAction properties
PublishTemplateAction.wizard.page.browse.cheatsheet.title=Wählen Sie eine Datei für den Spickzettel

PublishTemplateAction.wizard.page.browse.title=Wählen Sie eine Bilddatei

PublishTemplateAction.wizard.page.btn.browse=&Durchsuchen...

PublishTemplateAction.wizard.page.cheatsheetError=Die Spickzetteldatei ''{0}'' existiert nicht.

PublishTemplateAction.wizard.page.desc=Vorlage in Vorlagenordner registrieren.

PublishTemplateAction.wizard.page.imageError=Die Bilddatei ''{0}'' existiert nicht.

PublishTemplateAction.wizard.page.label.cheatsheet=Datei für Spickzettel:

PublishTemplateAction.wizard.page.label.description=&Beschreibung:

PublishTemplateAction.wizard.page.label.displayName=&Anzeigename:

PublishTemplateAction.wizard.page.label.image=&Vorlagenbild:

PublishTemplateAction.wizard.page.title=Vorlage veröffentlichen

PublishTemplateAction.wizard.title=Vorlage veröffentlichen

PublishTemplateAction.wizard.errorTitle=Fehler

PublishTemplateAction.wizard.message=Die Vorlage kann nicht in dem Verzeichnis veröffentlicht werden, in dem sie sich befindet. Wählen Sie zur Veröffentlichung ein anderes Verzeichnis.

PublishTemplateAction.wizard.msgDirErr=Das Zielverzeichnis ist nicht vorhanden und kann nicht erstellt werden.

PublishTemplateAction.wizard.notvalidfolder=Der in den Voreinstellungen festgelegte Vorlagenordner ist ungültig.

PublishTemplateAction.wizard.page.nameInfo=Anzeigename erforderlich

PublishTemplateAction.wizard.message.SourceFileNotExist=Die Quelldatei ist nicht vorhanden.

PublishTemplateAction.wizard.message.ThumbnailImageNotExist=Das Miniaturbild ist nicht vorhanden.

PublishTemplateAction.wizard.message.PreviewImageNotExist=Vorschaubilddatei nicht vorhanden.

PublishTemplateAction.wizard.message.PreviewImageNotValid=Die Vorschaubilddatei ist kein gültiges Bild.

PublishTemplateAction.SaveBeforeGenerating.dialog.title=Bericht speichern

PublishTemplateAction.SaveBeforeGenerating.dialog.message={0} wurde geändert. Änderungen vor dem Veröffentlichen speichern?

PublishTemplateAction.SaveBeforeGenerating.dialog.button.yes=&Ja

PublishTemplateAction.SaveBeforeGenerating.dialog.button.no=&Nein
###########################################################
#PublishLibraryAction properties
PublishLibraryAction.wizard.errorTitle=Fehler

PublishLibraryAction.wizard.message=Die Bibliothek kann nicht in dem Verzeichnis veröffentlicht werden, in dem sie sich befindet. Wählen Sie zur Veröffentlichung ein anderes Verzeichnis.

PublishLibraryAction.wizard.msgDirErr=Das Zielverzeichnis ist nicht vorhanden und kann nicht erstellt werden.

PublishLibraryAction.wizard.message.SourceFileNotExist=Die Quelldatei ist nicht vorhanden.

PublishLibraryAction.wizard.notvalidfolder=Der in der Einstellung definierte Berichtordner ist ungültig.

###########################################################
#RedoAction properties
RedoAction.Tooltip={0} wiederherstellen

###########################################################
#ExportReportWizard
ExportToLibraryAction.wizard.windowTitle=Exportieren

ExportToLibraryAction.wizard.page.title=Datei in Bibliothek exportieren

ExportToLibraryAction.wizard.page.desc=Berichtdesign in Bibliothek exportieren

ExportToLibraryAction.wizard.page.label.filename=&Dateiname:

ExportToLibraryAction.wizard.page.label.folder=&Ordner:

ExportToLibraryAction.wizard.page.button.browser=&Durchsuchen...

ExportToLibraryAction.wizard.page.dirdialog.message=Wählen Sie das Verzeichnis mit den Dateiinhalten aus.

ExportToLibraryAction.wizard.page.nameempty=Dateiname muss angegeben werden.

ExportToLibraryAction.wizard.page.folderempty=Ordner muss angegeben werden.

ExportToLibraryAction.wizard.page.foldererror=Der angegebene Ordner ist nicht vorhanden.

###########################################################
#PublishLibraryToResourceFolderAction
PublishLibraryToResourceFolderAction.Action.Text=&Bibliothek in Ordner für gemeinsame Ressourcen kopieren...

###########################################################
#PublishTemplateViewAction
DefaultNodeProvider.template.suffix=.rpttemplate
PublishTemplateViewAction.action.displaytext=Vorlage mit Ass&istent für neuen Bericht registrieren...

###########################################################
#RefreshModuleHandleAction
RefreshModuleHandleAction.Action.Text=&Bibliothek aktualisieren

RefreshModuleHandleAction.MessageBox.Title=Bibliothek aktualisieren

RefreshModuleHandleAction.MessageBox.Text=Es sind ungespeicherte Änderungen im aktuellen Design vorhanden. Möchten Sie diese Änderungen zuerst speichern?

RefreshModuleHandleAction.MessageBox.SaveButton=&Speichern

RefreshModuleHandleAction.MessageBox.DiscardButton=&Verwerfen

RefreshModuleHandleAction.MessageBox.CancelButton=Abbrechen
###########################################################
#ReloadCssStylesAction
ReloadCssStylesAction.Action.Text=CSS-Datei &neu laden

###########################################################
#ReferencePage properties
ReferencePage.Button.Edit=&Bearbeiten...

ReferencePage.Label.Source=Quel&le:

###########################################################
#RefreshAction properties
RefreshAction.text=A&ktualisieren

###########################################################
#RefreshLibExplorerAction properties
RefreshLibExplorerAction.Text=A&ktualisieren

###########################################################
#RemoveLibraryAction properties
RemoveLibraryAction.Text=Bi&bliothek entfernen

RemoveLibraryAction.config.Title=Entfernen der Bibliothek bestätigen

RemoveLibraryAction.config.Message=Möchten Sie diese Bibliothek wirklich entfernen?

###########################################################
#UseCssInReportDesignAction properties
UseCssInReportDesignAction.Text=CSS in Berichtdesign verwenden...

###########################################################
#UseCssInReportDesignAction properties
UseCssInThemeAction.Text=CSS in Bibliothek verwenden...

###########################################################
#RenameAction properties
RenameAction.text=Umbe&nennen

###########################################################
#RenameDialog properties
RenameDialog.action.Cancel=Abbrechen

RenameDialog.action.Ok=\\ \\ OK

RenameDialog.text.NewName=Neuer Name:

RenameDialog.text.Rename=Umbenennen

RenameDialog.text.ToolTip=Name darf nicht leer sein


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.SetNameAction.text=Namen festlegen

ExportElementToLibraryAction.DialogTitle.setNameFailed=Namen festlegen fehlgeschlagen

ExportElementToLibraryAction.TransLabel.Setname=Namen festlegen

ExportElementToLibraryAction.DialogTitle=Namen festlegen

ExportElementToLibraryAction.DialogMessage=Sie können vor dem Export einen Namen für das ausgewählte Element festlegen. Andernfalls wird ein Standardname verwendet.


###########################################################
#RenameInlineTool properties
RenameInlineTool.DialogTitle.RenameFailed=Umbenennen fehlgeschlagen

RenameInlineTool.TransLabel.Rename=Umbenennen

###########################################################
#RenameInputDialog properties
RenameInputDialog.DialogTitle=Eingabe umbenennen

RenameInputDialog.DialogMessage=Geben Sie den neuen Namen ein.

###########################################################
#report properties
report.designer.ui.preferences.buttonadd=&Hinzufügen...

report.designer.ui.preferences.buttonremove=&Entfernen

report.designer.ui.preferences.buttonremoveall=&Alles entfernen

report.designer.ui.preferences.dialogtitle=Bibliotheken öffnen

report.designer.ui.preferences.errormessage=Die Bibliothek ist bereits vorhanden.

report.designer.ui.preferences.errortitle=Fehler

report.designer.ui.preferences.extensions=*.rptlibrary
report.designer.ui.preferences.labeltitle=Bibliotheken hinzufügen oder entfernen

report.designer.ui.preferences.library.export.overwrite.always=&Immer

report.designer.ui.preferences.library.export.overwrite.never=&Nie

report.designer.ui.preferences.library.export.overwrite.prompt=&Eingabeaufforderung

report.designer.ui.preferences.library.export.title=Vorhandenes Element beim Exportieren des Designelements in die Bibliothek überschreiben.

report.designer.ui.preferences.template.openDialogTitle=Ordnerauswahl

report.designer.ui.preferences.template.select=&Auswählen...

report.designer.ui.preferences.template.templatedir=Vorlagen

report.designer.ui.preferences.template.templatefolder=&Vorlagenordner

report.designer.ui.preferences.template.title=Wählen Sie den Vorlagenordner.

org.eclipse.birt.report.designer.ui.preference.commenttemplates.enablecomment=Kommentar bei Erstellung eines Berichtdesigns &erstellen

org.eclipse.birt.report.designer.ui.preference.commenttemplates.defaultcomment=Copyright (c) 2008 <<Name Ihrer Firma>>
#bidi_hcg start
report.designer.ui.preferences.bidiproperties.defaultdirection=Standard-Berichtseiten&ausrichtung:

report.designer.ui.preferences.bidiproperties.ltrdirection=Von links nach rechts

report.designer.ui.preferences.bidiproperties.rtldirection=Von rechts nach links
#bidi_hcg end
###########################################################
#Resource Preference page properties
ResourecePreferencePage.title=Wählen Sie den Standard-Ressourcenordner für Bibliotheks-Explorer, Bilder,\nEigenschaftendateien usw. aus.

ResourecePreferencePage.folder=&Ressourcenordner

ResourecePreferencePage.select=&Auswählen...

ResourecePreferencePage.openDialogTitle=Nach Ordner suchen

ResourecePreferencePage.openDialogMessage=Wählen Sie den Standard-Ressourcenordner:

ResourecePreferencePage.defaultResourceFolder.dispaly=<Aktueller Projektordner>

###########################################################
#ReportAdvancedLauncherTab properties
ReportAdvancedLauncherTab.DefaultsButtonText=&Standardwerte wiederherstellen

ReportAdvancedLauncherTab.DeselectButtonText=&Markierung aufheben

ReportAdvancedLauncherTab.Name=Berichtprojekte

ReportAdvancedLauncherTab.SelectAllButtonText=&Alles markieren

ReportAdvancedLauncherTab.UseListRadioText=Wählen Sie aus der Liste die zu startenden Projekte aus.

ReportAdvancedLauncherTab.VisibleLabelText=&Verfügbare Projekte

ReportAdvancedLauncherTab.WorkspaceReportProjects=Berichtprojekte im Arbeitsplatz

###########################################################
#ReportClasspathLaucnTab properties
ReportClasspathLaucnTab.DefaultButtonText=&Standardwerte wiederherstellen

ReportClasspathLaucnTab.DeselectButtonText=&Markierung aufheben

ReportClasspathLaucnTab.Name=Java-Projekte

ReportClasspathLaucnTab.SelectAllButtonText=&Alles markieren

ReportClasspathLaucnTab.UseListRadioText=Wählen Sie aus der Liste ein zu startendes Projekt. Das Klassen-Ausgabeverzeichnis des gewählten Projekts wird dem Pfad für die Klasse hinzugefügt.

ReportClasspathLaucnTab.VisibleLabelText=&Verfügbare Projekte

ReportClasspathLaucnTab.WorkspaceJavaProjects=Java-Projekte im Arbeitsplatz

###########################################################
#ReportCreationTool properties
ReportCreationTool.ModelTrans.CreateElement=Element einfügen

###########################################################
#ReportEditor properties
ReportEditor.Button.Close=Schließen

ReportEditor.Button.Save=Speichern

ReportEditor.Dlg.Confirm=Die Datei wurde aus dem Dateisystem gelöscht. Möchten Sie Ihre Änderungen speichern oder den Editor ohne zu speichern schließen?

ReportEditor.Dlg.Save=Speichern ...

ReportEditor.error.activated.outofsync.message=Die Datei wurde im Dateisystem geändert. Sollen die Änderungen geladen werden?

ReportEditor.error.activated.outofsync.title=Datei geändert

ReportEditor.Error.Open=Fehler beim Öffnen

ReportEditor.Error.OpenEditor=Datei kann nicht geöffnet werden.

ReportEditor.Page.Code=Skript

ReportEditor.Page.Layout=Layout

ReportEditor.Page.MasterPage=MasterPage
ReportEditor.Page.Preview=Vorschau

ReportEditor.Page.PreviewSaveMessage=Die Berichtdatei wurde geändert. Möchten Sie sie vor dem Anzeigen der Vorschau speichern?

ReportEditor.Page.PreviewSaveTitle=Speichern

ReportEditor.Page.PreviewSaveToggle=Datei immer vor der Vorschau speichern.

###########################################################
#ReportLaunchConfigurationDelegate properties
ReportLaunchConfigurationDelegate.badFeatureSetup=Fehlerhafte Einstellungen für Funktion

ReportLaunchConfigurationDelegate.noStartup=Kein Autostart

###########################################################
#ReportPage properties
ReportPage.text.Add=&Hinzufügen...

ReportPage.text.Browse=&Durchsuchen...

ReportPage.title.Location=Speicherort:

ReportPage.title.setPrewImg=Wählen Sie eine Bilddatei

ReportPage.text.Resource.Add=&Durchsuchen...

ReportPage.text.Thumbnail=&Miniatur...

###########################################################
#ReportPageGenerator properties
ReportPageGenerator.List.Comments=Kommentare

ReportPageGenerator.List.Description=Beschreibung

ReportPageGenerator.List.EventHandler=Ereignis-Handler

ReportPageGenerator.List.General=Allgemein

ReportPageGenerator.List.Measurement=Maßeinheit

ReportPageGenerator.List.NamedExpressions=Benannte Ausdrücke

ReportPageGenerator.List.UserProperties=Benutzereigenschaften

ReportPageGenerator.List.Resources=Ressourcen

ReportPageGenerator.List.Resources.JarFile=JAR-Dateien:

ReportPageGenerator.List.AdvancedProperty=Erweitert

#OdaDataSourceGenerator properties
DataSourcePageGenerator.List.General=Allgemein

#OdaDataSetGenerator properties
DataSetPageGenerator.List.General=Allgemein

DataSetPageGenerator.DataSourceName=&Datenquelle

#ScalarParameterPageGenerator properties
ScalarParameterPageGenerator.List.General=Allgemein

ScalarParameterPageGenerator.List.I18n=Lokalisierung

#TemplateReportItemPageGenerator properties
TemplateReportItemPageGenerator.List.General=Allgemein

TemplateReportItemPage.description.Label.Instructions=&Anweisungen:

TemplateReportItemPageGenerator.List.I18n=Lokalisierung

TemplateReportItemPageGenerator.List.TextKey=Textschlüssel:

###########################################################
#ReportPropertySheetPage properties
ReportPropertySheetPage.Column.Title.Property=Eigenschaft

ReportPropertySheetPage.Column.Title.Value=Wert

ReportPropertySheetPage.Root.Default.Title=Info

ReportPropertySheetPage.Tooltip.Alphabetic=Alphabetisch sortieren

ReportPropertySheetPage.Tooltip.Group=Nach Gruppe sortieren

ReportPropertySheetPage.Tooltip.Local=Lokale Eigenschaften anzeigen

ReportPropertySheetPage.Value.Inherited=Übernommen

###########################################################
#ReportTemplateTransferDropTargetListener properties
ReportTemplateTransferDropTargetListener.ModelTrans.CreateElement=Element erstellen

ReportTemplateTransferDropTargetListener.transLabel.createElement=Element einfügen

###########################################################
#ResourceEditDialog properties
ResourceEditDialog.message.AddNote=Beim schnellen Hinzufügen wird der Schlüssel in der aktuellen Ressourcendatei gespeichert.

ResourceEditDialog.message.ResourceFile=Ressourcendatei:

ResourceEditDialog.text.Add=&Hinzufügen

ResourceEditDialog.text.AddWarningMsg=Leere Schlüssel sind nicht zulässig.

ResourceEditDialog.text.AddWarningTitle=Warnung

ResourceEditDialog.text.Delete=&Löschen

ResourceEditDialog.text.Key.TableColumn=Schlüssel

ResourceEditDialog.text.Key=&Schlüssel

ResourceEditDialog.text.QuickAdd=Schnelles Hinzufügen

ResourceEditDialog.text.Value.TableColumn=Wert

ResourceEditDialog.text.Value=&Wert

###########################################################
#ResourceKeyDescriptor properties
ResourceKeyDescriptor.text.Browse=&Durchsuchen...

ResourceKeyDescriptor.text.Browse.Alt=Durchsu&chen...

ResourceKeyDescriptor.text.Reset=&Zurücksetzen

ResourceKeyDescriptor.text.Reset.Alt=Zurücks&etzen:

ResourceKeyDescriptor.title.SelectKey=Schlüssel auswählen

ResourceKeyDescriptor.button.reset.tooltip=Schlüssel zurücksetzen

ResourceKeyDescriptor.button.browse.tooltip=Durchsuchen und Schlüssel auswählen

ResourceKey.invalid.containSpace=Ungültiger Anzeigenameschlüssel. Es dürfen keine eingebetteten Leerzeichen enthalten sein.
###########################################################
#RevertToReportItemAction properties
RevertToReportItemAction.text=&Auf Berichtobjekt zurücksetzen

###########################################################
#RevertToTemplateAction properties
RevertToTemplateAction.text=Zu Vorlagenobjekt zurück&verwandeln

###########################################################
#RowHandle properties
RowHandle.tooltip.GridRow=Raster - Zeile

RowHandle.tooltip.GroupFooter=Tabellengruppe - Fußzeile

RowHandle.tooltip.GroupHeader=Tabellengruppe - Kopfzeile

RowHandle.tooltip.TableDetail=Tabelle - Detail

RowHandle.tooltip.TableFooter=Tabelle - Fußzeile

RowHandle.tooltip.TableHeader=Tabelle - Kopfzeile

###########################################################
#RowHandleAdapter properties
RowHandleAdapter.table.detail=Detailzeile

RowHandleAdapter.table.footer=Fußzeile

RowHandleAdapter.table.groupFooter=Gruppenfußzeile

RowHandleAdapter.table.groupHeader=Gruppenkopfzeile

RowHandleAdapter.table.header=Kopfzeile

###########################################################
#RowPageGenerator properties
RowPageGenerator.List.Bookmark=Lesezeichen

RowPageGenerator.List.Borders=Rahmen

RowPageGenerator.List.Section=Seitenumbruch

RowPageGenerator.List.CellPadding=Zellfüllung

RowPageGenerator.List.Font=Schriftart

RowPageGenerator.List.General=Allgemein

RowPageGenerator.List.Visibility=Sichtbarkeit

###########################################################
#RowProvider properties
RowProvider.action.text.above=Zeile &oben einfügen

RowProvider.action.text.below=Zeile &unten einfügen

###########################################################
#SaveAsDialog properties
SaveAsDialog.message=Speichern Sie die Datei an einem anderen Ort.

SaveAsDialog.overwriteQuestion=Datei '{0}' ist bereits vorhanden. Möchten Sie die bestehende Datei ersetzen?

SaveAsDialog.Question=Frage

SaveAsDialog.text=Speichern unter

SaveAsDialog.title=Speichern unter

###########################################################
#CreateDirectory properties
DirectoryDialog.createQuestion=Das Verzeichnis ''{0}'' ist nicht vorhanden. Möchten Sie es erstellen?

DirectoryDialog.Question=Frage

###########################################################
#SchematicContextMenuProvider properties
SchematicContextMenuProvider.ActionText.editData=&Wert/Ausdruck bearbeiten...

SchematicContextMenuProvider.ActionText.editLabel=&Bearbeiten

SchematicContextMenuProvider.label.ResetSize=Bildgröße zurückset&zen

SchematicContextMenuProvider.label.ResetToImageDPI=B&ildauflösung optimal anpassen

SchematicContextMenuProvider.label.ResetToReportDPI=Be&richtauflösung optimal anpassen

SchematicContextMenuProvider.label.ResetToScreenDPI=Bild&schirmauflösung optimal anpassen

SchematicContextMenuProvider.label.RestToOrginalPixels=Ursprüngliche &Pixel

SchematicContextMenuProvider.Menu.EditGroup=&Gruppe bearbeiten

SchematicContextMenuProvider.Menu.Insert=E&infügen

SchematicContextMenuProvider.Menu.insertElement=Ei&nfügen

SchematicContextMenuProvider.Menu.insertRow=&Zeile

SchematicContextMenuProvider.Menu.List=Liste

SchematicContextMenuProvider.Menu.Show=Anzeigen

SchematicContextMenuProvider.Menu.Style=St&il

SchematicContextMenuProvider.Menu.Style.Apply=Stil an&wenden

SchematicContextMenuProvider.Menu.Theme=The&ma

SchematicContextMenuProvider.Menu.Theme.Apply=Thema ver&wenden

SchematicContextMenuProvider.Menu.Layout=L&ayoutvoreinstellungen
#SchematicContextMenuProvider.menutitle.applystylerule=Apply Style
#SchematicContextMenuProvider.menutitle.applythemerule=&Apply Theme
#SchematicContextMenuProvider.menutitle.editgroup=Edit Group
#SchematicContextMenuProvider.menutitle.insert=Insert
#SchematicContextMenuProvider.menutitle.list=List
#SchematicContextMenuProvider.menutitle.show=Show
#SchematicContextMenuProvider.menutitle.style=Style
SchematicContextMenuProvider.Menu.EditStyle=S&til bearbeiten

SchematicContextMenuProvider.Menu.DeleteStyle=&Stil entfernen

SchematicContextMenuProvider.Menu.DeleteGroup=Gruppe &löschen

SchematicContextMenuProvider.Menu.InsertGroupHeaderFooter=Gruppen&kopf-/fußzeile einfügen

SchematicContextMenuProvider.Menu.NewStyle=Neuer St&il

###########################################################
#ApplyLayoutPreferenceAction properties
ApplyLayoutPreferenceAction.autoLayout=&Automatisches Layout

ApplyLayoutPreferenceAction.fixedLayout=&Festes Layout

###########################################################
#SectionProcess properties
SectionProcess.action.Clear=Löschen

SectionProcess.action.Delete=Löschen

SectionProcess.action.DeleteRow=Zeile löschen

SectionProcess.action.New=Neu

SectionProcess.action.NewCell=Neue Zelle

SectionProcess.action.NewGroup=Neue Gruppe

SectionProcess.action.NewRow=Neue Zeile

SectionProcess.action.NewSection=Neuer Bereich

SectionProcess.action.NewSubSection=Neuer Teilbereich

SectionProcess.displayname.GridProcess=Raster

SectionProcess.displayname.GroupProcess=Gruppe

SectionProcess.displayname.ListProcess=Liste

SectionProcess.displayname.TableProcess=Tabelle

SectionProcess.displayname.TableRowProcess=Zeile

SectionProcess.type.Container=(Behälter)

SectionProcess.type.Data=(Daten)

SectionProcess.type.Image=(Bild)

SectionProcess.type.Label=(Beschriftung)

SectionProcess.type.List=(Liste)

SectionProcess.type.Prefix=Spalte

SectionProcess.type.Table=(Tabelle)

SectionProcess.type.Text=(Text)

###########################################################
#SelectAllAction properties
SelectAllAction.text=&Alles markieren

###########################################################
#SelectCssStyleWizard properties
SelectCssStyleWizard.wizard.title=CSS-Stile importieren

SelectCssStyleWizard.wizardPage.description.report=Wählen Sie eine CSS-Datei, und importieren Sie die Stile in das Berichtdesign.

SelectCssStyleWizard.wizardPage.description.library=Wählen Sie eine CSS-Datei aus, und importieren Sie Stile in die Bibliothek.

SelectCssStyleWizard.wizardPage.name=CSS-Stile auswählen

SelectCssStyleWizard.wizardPage.title=Stile aus CSS-Datei importieren

###########################################################
#UseCssInReportDialog properties
UseCssInReportDialog.Wizard.Title=CSS verwenden

UseCssInReportDialog.Wizard.Filename=&Dateiname:

UseCssInReportDialog.Label.Styles=&Verfügbare Stile von {0}

UseCssInReportDialog.Label.notifications=&Benachrichtigungen

UseCssInReportDialog.TitleArea.Title=CSS-Datei in Berichtdesign verwenden

UseCssInReportDialog.TitleArea.Message=Wählen Sie eine CSS-Datei aus dem Ordner 'Gemeinsam genutzte Bibliotheken', und verwenden Sie die Stile im Berichtdesign.

UseCssInReportDialog.Label.No.File=Keine CSS-Datei ausgewählt

UseCssInReportDialog.Dialog.Browse=Durchsuchen

UseCssInReportDialog.Dialog.Browse.Title=Wählen Sie eine CSS-Datei, die in den Bericht aufgenommen werden soll.

UseCssInReportDialog.Dialog.Browse.Library.Title=Wählen Sie eine CSS-Datei, die in die Bibliothek aufgenommen werden soll.

UseCssInReportDialog.Error.Already.Include={0} wurde bereits aufgenommen

UseCssInReportDialog.Dialog.Button.viewTimeBtn.Text=CSS-Datei während der Anzeige &einschließen (nur für HTML-Format übernehmen)

UseCssInReportDialog.Dialog.Label.viewTimeLb=Speicherort der CSS-Datei während der Anzeige:

UseCssInReportDialog.Dialog.Label.example=Beispiel: http://mydomain/test.css, myfolder/test/css

UseCssInReportDialog.Dialog.Label.Theme.Text=&Thema:

UseCssInReportDialog.Dialog.Text.uri=&URI:
UseCssInThemeDialog.TitleArea.Title=CSS-Datei in Berichtbibliothek verwenden

UseCssInThemeDialog.TitleArea.Message=Wählen Sie ein Thema, in dem die Stile der CSS-Datei platziert werden sollen.

CssStyleSheetNodeProvider.Label.FromServer=Vom Server

CssStyleSheetNodeProvider.Tooltip.URI=(Übernahme während der Anzeige)


###########################################################
#SelectValueDialog properties
SelectValueDialog.errorRetrievinglist=Fehler beim Abrufen der Liste

SelectValueDialog.messages.info.selectVauleUnavailable=Werteliste ist nicht verfügbar.

SelectValueDialog.messages.info.invalidSelectVauleExpression=Für die Funktion 'Wert auswählen' ist nur ein einfacher Spaltenausdruck zulässig. Ein komplexer Ausdruck wird abgewiesen.

SelectValueDialog.messages.warning.BlobColumn.unsupported=Werteliste des Ausdrucks, der auf die Spalte/Bindung des Typs BLOB verweist, kann nicht abgerufen werden.

SelectValueDialog.messages.error.selectVauleUnavailable=Der Wert für den Ausdruck kann nicht abgerufen werden.

SelectValueDialog.messages.error.selectValueNotSupported=Unterstützen diese Art der Werteauswahl nicht.

SelectValueDialog.noExpressionSet=Ein Ausdruck wurde nicht festgelegt

SelectValueDialog.retrieving=Liste wird abgerufen ...

SelectValueDialog.selectValue=Wert auswählen

SelectValueDialog.SelectValue.NullValue=<Null-Wert>

###########################################################
#SetConstraintCommand properties
SetConstraintCommand.Error.SetConstraintError=Beim Festlegen einer Einschränkung am Modell ist ein Fehler aufgetreten.

SetConstraintCommand.transLabel.setConstraint=Einschränkung festlegen

SetConstraintCommand.commandLabel.ResizeCommand=Größe anpassen
###########################################################
#ProjectFilterAction properties
ProjectFilterAction.text=Filter...

###########################################################
#ResourceFileFolderSelectionAction properties
ResourceFileFolderSelectionAction.text=Filter...

###########################################################
#ResourceFileFolderSelectionDialog properties
ResourceFileFolderSelectionDialog.Text.Menu=Menü

ResourceFileFolderSelectionDialog.button.importFile=Importdatei...

ResourceFileFolderSelectionDialog.title.error=Fehler

ResourceFileFolderSelectionDialog.title.overwrite=Überschreiben

ResourceFileFolderSelectionDialog.overwrite.msg=Eine Datei mit diesem Namen ist bereits vorhanden. Soll sie überschrieben werden?

ResourceFileFolderSelectionDialog.import.msg=Datei wird importiert:

###########################################################
#ShowPropertyAction properties
ShowPropertyAction.text=E&igenschaften

###########################################################
#SortingHandleProvider properties
SortingHandleProvider.Label.SortOn=Sortieren nach:

###########################################################
#Spinner properties
Spinner.DialogTitle.InvalidNumber=Ungültige Zahl

###########################################################
#SplitAction properties
SplitAction.actionMsg.splitCells=&Zelle teilen

###########################################################
#StyleBuilder properties
StringVariableSelectionDialog.Dialog.Label=&Variablenbeschreibung:

StringVariableSelectionDialog.Dialog.Prompt=&Wählen Sie eine Variable (? = beliebiges Zeichen, * = beliebige Zeichenfolge):

StringVariableSelectionDialog.Dialog.Title=Variable wählen

StyleBuilder.action.Apply=&Anwenden

StyleBuilder.Edit.Info=Gewünschte Stileigenschaften bearbeiten. Alle Berichtelemente mit diesem Stil werden automatisch mit dem neuen Format aktualisiert.

StyleBuilder.New.Info=Neuen Stil durch Auswahl einer Eigenschaftskategorie links und Angabe von Eigenschaftswerten erstellen.

StyleBuilder.Title=Stil

SytleBuilder.DialogTitle.New=Neuer Stil

SytleBuilder.DialogTitle.Edit=Stil bearbeiten

###########################################################
#StyleComboProvider properties
StyleComboProvider.LineStyle.None=Kein Stil

###########################################################
#StyleNodeProvider properties
StyleNodeProvider.action.Edit=Stil b&earbeiten...

StyleNodeProvider.action.New=&Neuer Stil...

###########################################################
#StyleProcess properties
StyleProcess.action.Delete=Stil entfernen

StyleProcess.action.Edit=Stil b&earbeiten...

StyleProcess.action.New=&Neuer Stil...

StyleProcess.text.CannotDelete=Standardstil kann nicht entfernt werden.

StyleProcess.text.Show=Blendet den Datensatzeditor für den Datensatz ein.

###########################################################
#StylesNodeProvider properties
StylesNodeProvider.action.New=&Neuer Stil...

###########################################################
#StylesOutlineProcess properties
StylesOutlineProcess.action.New=&Neuer Stil...

###########################################################
#TableBandProvider properties
TableBandProvider.action.text.group=Gruppe e&infügen

TableBandProvider.action.text.row=&Zeile einfügen

###########################################################
#TableEditPart properties
TableEditPart.GUIDEHANDLE_TEXT =Tabelle

TableEditPart.Label.Merge=Zellen verbinden

TableEditPart.Label.ResizeColumn=Spaltengröße anpassen

TableEditPart.Label.ResizeRow=Zeilengröße anpassen

###########################################################
#TableFigure properties
TableFigure.BORDER_TEXT=Tabelle

TableArea.Button.New=N&eu...

TableArea.Button.Edit=&Bearbeiten...

TableArea.Button.Remove=&Entfernen

TableArea.Button.Down=Nach &unten

TableArea.Button.RemoveAll=Alle ent&fernen

###########################################################
#TableHandleAdapter properties
TableHandleAdapter.name.detail=Detail

TableHandleAdapter.name.footer=Fußzeile

TableHandleAdapter.name.header=Kopfzeile

TableHandleAdapter.transLabel.deleteColumn=Spalte löschen

TableHandleAdapter.transLabel.deleteColumns=Spalten löschen

TableHandleAdapter.transLabel.deleteRow=Zeile löschen

TableHandleAdapter.transLabel.deleteRows=Zeilen löschen

TableHandleAdapter.transLabel.include=Einschließen

TableHandleAdapter.transLabel.insertColumn=Spalte einfügen

TableHandleAdapter.transLabel.insertGroup=Gruppe einfügen

TableHandleAdapter.transLabel.insertRow=Zeile einfügen

TableHandleAdapter.transLabel.notInclude=Ausschließen

TableHandleAdapter.transLabel.splitCells=Zelle teilen

TableHandleAdapter.transLable.deleteGroup=Tabellengruppe löschen

###########################################################
#TableOptionDialog properties
TableOptionDialog.message.RememberGrid=&Diese Dimensionen für neue Raster verwenden

TableOptionDialog.message.RememberTable=&Diese Dimensionen für neue Tabellen verwenden

TableOptionDialog.text.Column=&Anzahl der Spalten:

TableOptionDialog.text.GridRow=&Anzahl der Zeilen:

TableOptionDialog.text.GridSize=Rastergröße

TableOptionDialog.text.TableDetail=&Anzahl der Details:

TableOptionDialog.text.TableSize=Tabellengröße

TableOptionDialog.title.InsertGrid=Raster einfügen

TableOptionDialog.title.InsertTable=Tabelle einfügen

TableOptionDialog.text.DataSet=&Datensatz:

TableOptionDialog.text.AutoSummary=Automatische Zusa&mmenfassung in

TableOptionPage.Title=Tabelle einfügen

###########################################################
#TablePageGenerator properties
TablePageGenerator.List.Bookmark=Lesezeichen

TablePageGenerator.List.Borders=Rahmen

TablePageGenerator.List.CellPadding=Zellfüllung

TablePageGenerator.List.Font=Schriftart

TablePageGenerator.List.General=Allgemein

TablePageGenerator.List.Marign=Rand

TablePageGenerator.List.Section=Seitenumbruch

TablePageGenerator.List.TOC=Inhaltsverzeichnis

TablePageGenerator.List.Visibility=Sichtbarkeit

###########################################################
#TemplateReportItemPropertiesDialog properties
TemplateReportItemPropertiesDialog.errorMessage=Eingabeaufforderung muss Text enthalten.

TemplateReportItemPropertiesDialog.errorTitle=Fehler

TemplateReportItemPropertiesDialog.group=Eigenschaften für Berichtobjekt-Vorlage

TemplateReportItemPropertiesDialog.objectType=Elementtyp:

TemplateReportItemPropertiesDialog.nameText=&Name:

TemplateReportItemPropertiesDialog.promptText=&Anweisungen:

TemplateReportItemPropertiesDialog.title=Berichtobjekt-Vorlage erstellen

###########################################################
#performCreatePlaceHolder action
performCreatePlaceHolder.errorMessage.title=Fehler

performCreatePlaceHolder.errorMessage.content=Der angegebene Name existiert bereits. Wählen Sie einen anderen Namen, oder lassen Sie das Feld leer.

###########################################################
#TogglePropertyDescriptor properties
TogglePropertyDescriptor.toolTipText.Bold=Fett

TogglePropertyDescriptor.toolTipText.Italic=Kursiv

TogglePropertyDescriptor.toolTipText.Underline=Unterstrichen

TogglePropertyDescriptor.toolTipText.Text_Line_Through=Durchgestrichener Text

###########################################################
#TextAlignPropertyDescriptor properties
TextAlignPropertyDescriptor.0=Links

TextAlignPropertyDescriptor.1=Zentrieren

TextAlignPropertyDescriptor.2=Rechts

TextAlignPropertyDescriptor.3=Blocksatz

###########################################################
#TextAreaContextMenu properties
TextAreaContextMenu.Copy.description=Kopieren

TextAreaContextMenu.Copy.image=

TextAreaContextMenu.Copy.label=&Kopieren

TextAreaContextMenu.Copy.tooltip=Kopieren

TextAreaContextMenu.Cut.description=Ausschneiden

TextAreaContextMenu.Cut.image=

TextAreaContextMenu.Cut.label=Auss&chneiden

TextAreaContextMenu.Cut.tooltip=Ausschneiden

TextAreaContextMenu.Paste.description=Einfügen

TextAreaContextMenu.Paste.image=

TextAreaContextMenu.Paste.label=&Einfügen

TextAreaContextMenu.Paste.tooltip=Einfügen

TextAreaContextMenu.Redo.description=Wiederherstellen

TextAreaContextMenu.Redo.image=

TextAreaContextMenu.Redo.label=&Wiederherstellen

TextAreaContextMenu.Redo.tooltip=Wiederherstellen

TextAreaContextMenu.SelectAll.description=Alles markieren

TextAreaContextMenu.SelectAll.image=

TextAreaContextMenu.SelectAll.label=Alles m&arkieren

TextAreaContextMenu.SelectAll.tooltip=Alles markieren

TextAreaContextMenu.Undo.description=Rückgängig

TextAreaContextMenu.Undo.image=

TextAreaContextMenu.Undo.label=&Rückgängig

TextAreaContextMenu.Undo.tooltip=Rückgängig

###########################################################
#TextDataEditPart properties
TextDataEditPart.Figure.Dafault=Dynamischer Text

###########################################################
#TextEditDialog properties
TextEditDialog.action.text.direction=Lese&richtung von rechts nach links

TextEditDialog.action.text.editDynamicText=Dynamischen T&ext bearbeiten

TextEditDialog.action.text.formatDateTime=Format Datum/Uhrz&eit

TextEditDialog.action.text.formatNumber=Format &Zahl

TextEditDialog.action.text.formatString=Formatzeichenfol&ge

TextEditDialog.action.text.formatHTML=&HTML formatieren

TextEditDialog.choice.HTML=HTML/Dynamischer Text

TextEditDialog.choice.plainText=Einfacher Text

TextEditDialog.choice.Auto=Automatisch

TextEditDialog.comboText.formattingAndLayout=Formatierung und Layout

TextEditDialog.format.dynamicText=Dynamischer Text

TextEditDialog.format.formatting=Formatierung und Layout

TextEditDialog.format.lists=Listen

TextEditDialog.format.pageComposition=Seitenzusammenstellung

TextEditDialog.format.pageElements=Seitenelemente

TextEditDialog.formatChoice.content=Inhalt

TextEditDialog.formatChoice.dynamicText=Dynamischer Text

TextEditDialog.formatChoice.formatting=Formatierung

TextEditDialog.formatChoice.layout=Layout

TextEditDialog.formatChoice.lists=Listen

TextEditDialog.toolTip.tag.a=Anker

TextEditDialog.toolTip.tag.b=Textstil "Fett"

TextEditDialog.toolTip.tag.birtImage=BIRT-spezifisches Bild

TextEditDialog.toolTip.tag.br=Zeilenumbruch

TextEditDialog.toolTip.tag.center=Zentriert den Text

TextEditDialog.toolTip.tag.code=Computercode-Fragment

TextEditDialog.toolTip.tag.dd=Definitionsbeschreibung

TextEditDialog.toolTip.tag.del=Gelöschter Text

TextEditDialog.toolTip.tag.dl=Definitionsliste

TextEditDialog.toolTip.tag.dt=Definitionsbegriff

TextEditDialog.toolTip.tag.em=Betonung

TextEditDialog.toolTip.tag.font=Lokale Schriftartauswahl

TextEditDialog.toolTip.tag.genericStyleContainer=Generischer Sprach-/Stilbehälter

TextEditDialog.toolTip.tag.h1=Überschrift 1

TextEditDialog.toolTip.tag.h2=Überschrift 2

TextEditDialog.toolTip.tag.h3=Überschrift 3

TextEditDialog.toolTip.tag.h4=Überschrift 4

TextEditDialog.toolTip.tag.h5=Überschrift 5

TextEditDialog.toolTip.tag.h6=Überschrift 6

TextEditDialog.toolTip.tag.i=Textstil "Kursiv"

TextEditDialog.toolTip.tag.img=Standard-HTML-Bildreferenz

TextEditDialog.toolTip.tag.ins=Eingefügter Text

TextEditDialog.toolTip.tag.li=Listenelement

TextEditDialog.toolTip.tag.p=Absatz

TextEditDialog.toolTip.tag.pre=Vorformatierter Text

TextEditDialog.toolTip.tag.strong=Starke Hervorhebung

TextEditDialog.toolTip.tag.sub=Tiefgestellt

TextEditDialog.toolTip.tag.sup=Hochgestellt

TextEditDialog.toolTip.tag.tt=Text mit fester Zeichenbreite ("Videotext")

TextEditDialog.toolTip.tag.u=Textstil "Unterstrichen"

TextEditDialog.toolTip.tag.ul=Unsortierte Liste

TextEditDialog.toolTip.valueOf=Value Of

TextEditDialog.toolTipText.copy=Kopieren

TextEditDialog.toolTipText.cut=Ausschneiden

TextEditDialog.toolTipText.delete=Löschen

TextEditDialog.toolTipText.paste=Einfügen

TextEditDialog.toolTipText.redo=Wiederherstellen

TextEditDialog.toolTipText.undo=Rückgängig

TextEditDialog.toolTipText.fx=Ausdruckserstellung öffnen

TextEditDialog.action.item.formatHTML=HTML formatieren
TextEditDialog.action.item.formatDateTime=Datum/Uhrzeit formatieren

TextEditDialog.action.item.formatNumber=Zahl formatieren

TextEditDialog.action.item.formatString=Zeichenfolge formatieren

TextEditDialog.title.DlgNew=Neues Textelement

TextEditDialog.title.DlgEdit=Textelement bearbeiten

###########################################################
#TextEditPart properties
TextEditPart.Figure.Dafault=Text

TextEditPart.trans.editText=Textelement bearbeiten

###########################################################
#TextExtendsTools properties
TextExtendsTools.Message.DefaultAuthor=Birt Eclipse Report Designer-Version

###########################################################
#TextPageGenerator properties
TextPageGenerator.List.Bookmark=Lesezeichen

TextPageGenerator.List.Borders=Rahmen

TextPageGenerator.List.General=Allgemein

TextPageGenerator.List.I18n=Lokalisierung

TextPageGenerator.List.Margin=Rand

TextPageGenerator.List.Padding=Umrandungsfüllung

TextPageGenerator.List.Section=Seitenumbruch

TextPageGenerator.List.TOC=Inhaltsverzeichnis

TextPageGenerator.List.Visibility=Sichtbarkeit

###########################################################
#TextPropertyDescriptor properties
TextPropertyDescriptor.Message.Version=Eclipse BIRT Designer Version {0} Build <{1}>

###########################################################
#AutoTextPageGenerator properties
AutoTextPageGenerator.List.Bookmark=Lesezeichen

AutoTextPageGenerator.List.Borders=Rahmen

AutoTextPageGenerator.List.General=Allgemein

AutoTextPageGenerator.List.Margin=Rand

AutoTextPageGenerator.List.Padding=Füllung

AutoTextPageGenerator.List.Section=Seitenumbruch

AutoTextPageGenerator.List.TOC=Inhaltsverzeichnis

AutoTextPageGenerator.List.Visibility=Sichtbarkeit

###########################################################
#ThemesNodeProvider properties
ThemesNodeProvider.action.New=&Neues Thema...

ThemesNodeProvider.Failed=Neues Thema fehlgeschlagen

NewThemeDialog.DialogTitle=Neues Thema

NewThemeDialog.DialogMessage=Geben Sie den Namen ein.

###########################################################
#TimeOptionDialog properties
TimeOptionDialog.Label.Format=Format

TimeOptionDialog.Label.TimeZone=TimeZone

###########################################################
#ToggleMarginVisibilityAction properties
ToggleMarginVisibilityAction.text.Label=Ränder &anzeigen

ToggleMarginVisibilityAction.text.Tooltip=Ränder umschalten

###########################################################
#TwinExpressionDialog properties
TwinExpressionDialog.Dialog.Title=Werte auswählen

TwinExpressionDialog.lable.value1=Wert1:

TwinExpressionDialog.lable.value2=Wert2:

###########################################################
#UndoAction properties
UndoAction.Tooltip={0} rückgängig machen

###########################################################
#UnitPropertyDescriptor properties
UnitPropertyDescriptor.error.message=Geben Sie einen niedrigeren Wert ein\!

UnitPropertyDescriptor.error.title=Wert zu groß

###########################################################
#UseLibraryAction properties
UseLibraryAction.Text=&Bibliothek verwenden

UseLibraryAction.Error.Title=Vorhandene Datei ersetzen

UseLibraryAction.Error.Message=Die Datei ''{0}'' ist bereits im BIRT-Ressourcenordner vorhanden. Ersetzen?

###########################################################
#UseCSSAction properties
UseCSSAction.Text=&CSS verwenden

###########################################################
#UserPropertyBuilder properties
UserPropertyBuilder.0=Neue Benutzereigenschaft

UserPropertyBuilder.ErrorMessage.DuplicatedName=Der angegebene Name wird bereits verwendet.

UserPropertyBuilder.ErrorMessage.NoName=Legen Sie einen Namen fest.

UserPropertiesHandleProvier.Column.Name=Name

UserPropertiesHandleProvier.Column.Type=Typ

UserPropertyBuilder.Label.DefaultValue=&Standardwert:

UserPropertyBuilder.Label.ExpressionName=&Ausdrucksname:

UserPropertyBuilder.Label.PropertyName=&Eigenschaftsname:

UserPropertyBuilder.Label.PropertyType=&Eigenschaftstyp:

UserPropertyBuilder.Title.NamedExpression=Neuer benannter Ausdruck

###########################################################
#VisibilityPage properties
VisibilityPage.Check.HideElement=&Element ausblenden

VisibilityPage.Check.HTML=H&TML
VisibilityPage.Check.PDF=&PDF
VisibilityPage.Label.DetailAll=Detail (Alle Ausgaben)

VisibilityPage.Label.DetailSpecific=Detail (Bestimmte Ausgaben)

VisibilityPage.Label.Expression=Ausdruck:

VisibilityPage.menu.SaveHides=Versteckte Elemente speichern

VisibilityPage.Radio.AllOutputs=&Für alle Ausgaben

VisibilityPage.Radio.SpecificOutputs=Für spe&zifische Ausgaben...

###########################################################
#WidgetUtil properties
WidgetUtil.MessageBox.Error=Fehler

###########################################################
#wizard properties
wizard.transaction.createDataSource=Geben Sie den Namen und die Art der Datenquelle an.

###########################################################
#WizardChoicePage properties
WizardChoicePage.radio.createBlank=Einen leeren Bericht erstellen, ohne eine Vorlage zu verwenden.

WizardChoicePage.radio.createFromCustom=Bericht als Kopie eines anderen Berichts erstellen.

WizardChoicePage.radio.createFromTemplate=Bericht aus Vorlage erstellen.

###########################################################
#WizardCustomTemplatePage properties
WizardCustomTemplatePage.button.Browse=Durchsuchen

WizardCustomTemplatePage.label.FileName=Berichtdateinamen eingeben oder auswählen.

WizardCustomTemplatePage.label.FromLocal=Lokales Dateisystem durchsuchen.

WizardCustomTemplatePage.label.FromProjectDescription=Vorhandene Berichtdatei auswählen.

WizardCustomTemplatePage.label.FromProjectTitle=Berichtdatei-Auswahl

###########################################################
#WizardNewReportCreationPage properties
WizardNewReportCreationPage.Errors.nameExists=Dieser Name ist bereits vorhanden.

WizardNewReportCreationPage.Errors.nameEmpty=Namen dürfen nicht leer sein.

WizardNewReportCreationPage.label.browse=Durchsuchen ...

WizardNewReportCreationPage.label.directory=Verzeichnis:

WizardNewReportCreationPage.label.file.location=Speicherort der Datei

WizardNewReportCreationPage.label.file.name=Dateiname:

WizardNewReportCreationPage.label.select.directory=Wählen Sie ein Verzeichnis.

WizardNewReportCreationPage.label.useDefault=Standard verwenden

WizardNewReportCreationPage.msg.duplicate.fileName=Dieser Name ist bereits vorhanden.

WizardNewReportCreationPage.msg.empty.file.locationDirectory=Sie müssen ein Verzeichnis für den Speicherort der Datei angeben.

WizardNewReportCreationPage.msg.empty.file.name=Sie müssen einen Dateinamen angeben.

###########################################################
#WizardSelectCssStylePage properties
WizardSelectCssStylePage.button.label.browse=&Durchsuchen...

WizardSelectCssStylePage.button.label.deselectAll=&Markierung aufheben

WizardSelectCssStylePage.button.label.selectAll=&Alles markieren

WizardSelectCssStylePage.errorMsg.FileNotExist=Datei existiert nicht.

WizardSelectCssStylePage.info.canNotImport=\ \ (Importieren nicht möglich)

WizardSelectCssStylePage.label.fileName=Datei&name:

WizardSelectCssStylePage.label.importToReportDesign=\ für Import in das Berichtdesign.

WizardSelectCssStylePage.label.notifications=Benachrichtigungen:

WizardSelectCssStylePage.label.selectStylesFrom=Wählen Sie die Stile aus

WizardSelectCssStylePage.msg.FileNameEmpty=Sie müssen einen Dateinamen angeben.

WizardSelectCssStylePage.text.cannot.import.style=\ - Stil kann nicht importiert werden.

WizardSelectCssStylePage.FileNameError.Title=Fehler

WizardSelectCssStylePage.FileNameError.Message=Die ausgewählte Datei ist ungültig.

###########################################################
#WizardTemplateChoicePage properties
WizardTemplateChoicePage.label.Description=Beschreibung:

WizardTemplateChoicePage.label.Preview=Vorschau:

WizardTemplateChoicePage.label.ReportTemplates=Berichtvorlagen:

WizardTemplateChoicePage.label.ShowCheatSheets=&Spickzettel für Berichterstellung anzeigen

WizardTemplateChoicePage.label.EnableDefaultThemes=Standardthemen e&inschließen

WizardTemplateChoicePage.message.BlankReport=Einen leeren Bericht ohne vordefinierten Inhalt erstellen.

WizardTemplateChoicePage.message.ChartListing=Einen gruppierten Bericht mit einem Diagramm mit visueller Darstellung der Gruppendaten erstellen.

WizardTemplateChoicePage.message.DualColumnChartListing=Einen gruppierten Bericht mit zwei Untergruppen des Typs 'Diagramm und Liste' erstellen.

WizardTemplateChoicePage.message.DualColumnListing=Einen gruppierten Bericht mit zwei Untergruppen erstellen.

WizardTemplateChoicePage.message.FirstReport=Enthält Anweisungen zum Erstellen von Berichten mit Kundenlisten.

WizardTemplateChoicePage.message.GroupedListing=Einen Listenbericht mit Gruppierung einiger Felder erstellen.

WizardTemplateChoicePage.message.Letter=Eine Vorlage zur Erstellung personalisierter Briefe mithilfe von Feldern aus einer Datenquelle erstellen.

WizardTemplateChoicePage.message.MailingLabels=Nachrichtenbeschriftungen

WizardTemplateChoicePage.message.SideBySideChartListing=Einen gruppierten Bericht mit Diagramm und Liste nebeneinander in jeder Gruppe erstellen.

WizardTemplateChoicePage.message.SimpleListing=Einen Listenbericht mit Feldern aus einer Datenquelle erstellen.

WizardTemplateChoicePage.message.Crosstab=Lernprogramm zur Erstellung eines Kreuztabellenberichts verwenden

WizardTemplateChoicePage.message.BlankAuto=Eine leere Berichtvorlage, die beim Anzeigen automatisch Interaktivität aktiviert.

WizardTemplateChoicePage.title.BlankAuto=Leerer auto-interaktiver Bericht

WizardTemplateChoicePage.title.BlankReport=Leerer Bericht

WizardTemplateChoicePage.title.ChartListing=Diagramm & Liste

WizardTemplateChoicePage.title.DualColumnChartListing=Zwei-Spalten-Diagramm & -Listen

WizardTemplateChoicePage.title.DualColumnListing=Liste mit zwei Spalten

WizardTemplateChoicePage.title.FirstReport=Der erste Bericht

WizardTemplateChoicePage.title.GroupedListing=Gruppierte Liste

WizardTemplateChoicePage.title.Letter=Letter

WizardTemplateChoicePage.title.MailingLabels=Nachrichtenbeschriftungen

WizardTemplateChoicePage.title.SideBySideChartListing=Diagramm & Liste nebeneinander

WizardTemplateChoicePage.title.SimpleListing=Einfache Liste

WizardTemplateChoicePage.title.Crosstab=Kreuztabelle
#bidi_hcg start
WizardTemplateChoicePage.label.rtlBiDiOrientation=Von rechts nach links

WizardTemplateChoicePage.label.ltrBiDiOrientation=Von links nach rechts

WizardTemplateChoicePage.label.chooseBiDiDirection=Be&richtseitenausrichtung:

WizardTemplateChoicePage.RootDisplayName=Standard
#bidi_hcg end
###########################################################
#XMLSourcePage properties
XMLSourcePage.Error.Dialog.Message.InvalidFile=Bevor andere Seiten verwendet werden können, muss der Fehler in der XML-Quellseite behoben werden.

XMLSourcePage.Error.Dialog.Message.SaveFile=Die XML-Quellseite wurde geändert. Bevor andere Seiten verwendet werden können, muss die XML-Quellseite gespeichert werden.

XMLSourcePage.Error.Dialog.title=Fehler

XMLSourcePage.Error.Dialog.Message.PromptMsg=Die XML-Quelle für das Bericht-Design wurde geändert. Möchten Sie die Änderungen speichern?

XMLSourcePage.Error.Dialog.Message.Yes=Ja

XMLSourcePage.Error.Dialog.Message.No=Nein

XMLSourcePage.Error.Dialog.Message.Cancel=Abbrechen

###########################################################
#SaveReportAsWizard properties
SaveReportAsWizard.SettingPage.title=Vorlageneigenschaft festlegen

SaveReportAsWizard.SaveAsPageTitle=Speichern unter

SaveReportAsWizard.SaveAsLibraryMessage=Datei als Bibliothek mit einem anderen Namen speichern.

SaveReportAsWizard.SaveAsReportorTemplateMessage=Datei als Berichtdesign oder Vorlage mit einem anderen Namen speichern.

SaveReportAsWizard.SettingPageTitle=Eigenschaft für Bericht festlegen

SaveReportAsWizard.SettingPage.message=Diese Eigenschaften gelten beim Speichern des Berichts als Vorlage.

SaveReportAsWizard.SaveAsWizardWindowTitle=Speichern unter

SaveReportAsWizard.SaveAsWizardPageTitle=Speichern unter

SaveReportAsWizard.SaveAsWizardPageDesc=Speichern Sie die Datei an einem anderen Ort.

SaveReportAsWizard.SettingPage.Messages=Legen Sie die Vorlageneigenschaften fest.

###########################################################
#WizardReportSettingPage properties
WizardReportSettingPage.Error.ReportorTemplate=Dateiname muss auf {0} oder .rpttemplate enden.

WizardReportSettingPage.Error.Library=Dateiname muss auf .rptlibrary enden.

###########################################################
#WizardSaveAsPage properties
WizardSaveAsPage.FileLabel=&Dateiname:

WizardSaveAsPage.Question=Frage

WizardSaveAsPage.FolderEmpty=\ Der Ordner ist leer.

WizardSaveAsPage.NoProject=\ Das angegebene Projekt existiert nicht.

WizardSaveAsPage.PathOccupied=\ An diesem Speicherort ist bereits eine Datei vorhanden: {0}

WizardSaveAsPage.EmptyName=\ Der Name ''{0}" ist leer.

WizardSaveAsPage.SelectFolder=\ Wählen Sie den Ordner aus:

WizardSaveAsPage.NameExists=\ Dieser Name ist bereits vorhanden.

WizardSaveAsPage.OverwriteQuestion=Die Datei "{0}" ist bereits vorhanden. Ersetzen?

WizardSaveAsPage.InvalidFileName=\ ''{0}'' ist kein gültiger Dateiname.

WizardSaveAsPage.ContainerGroup=\ Wählen Sie den übergeordneten Ordner aus oder &geben Sie einen Namen ein:

###########################################################
#Library DND message
Library.DND.messages.outofsync = Die im Bericht verwendete Bibliothek wurde möglicherweise geändert. Schließen Sie diesen Bericht, und öffnen Sie ihn erneut, um die aktualisierte Bibliothek zu verwenden.

NamedExpressionsHandleProvier.Column.Name=Name

NamedExpressionsHandleProvier.Column.DefaultValue=Standardwert

Library.DND.messages.cannotApplyTheme=Das Thema kann nicht verwendet werden.

JarFileFormProvider.Column.Name=Name

JsFileFormProvider.Column.Name=Name

ReportPageGenerator.List.Resources.JsFile=JavaScript-Dateien:

PropertiesFileFormProvider.Column.Name=Name

ReportPageGenerator.List.Resources.PropertiesFile=Eigenschaftendateien:

###########################################################
#ClassFinder
ClassFinder.TaskStart=Handler-Klasse wird gesucht...

ClassFinder.DialogTitle=Auswahl der Klasse

ClassFinder.DialogMessage=Handler-Klasse &wählen:

ClassFinder.ErrorMessage=Es wurde keine Klasse gefunden.

ClassPathBlock_button.addExtFolder=Externen Klassen&ordner hinzufügen...

ClassPathBlock_button.addExtJars=E&xterne JARs hinzufügen...

ClassPathBlock_button.down=Nach unten

ClassPathBlock_button.edit=&Bearbeiten...

ClassPathBlock_button.remove=&Entfernen

ClassPathBlock_button.up=Nach oben

ClassPathBlock_FileDialog.edit.text=JAR bearbeiten

ClassPathBlock_FileDialog.jar.text=JAR-Auswahl

ClassPathBlock_FolderDialog.edit.message=Geben Sie den neuen Speicherort des Klassenordners an:

ClassPathBlock_FolderDialog.edit.text=Klassenordner bearbeiten

ClassPathBlock_FolderDialog.message=Wählen Sie den Speicherort des Klassenordners:

ClassPathBlock_FolderDialog.text=Auswahl des externen Klassenordners

ClassPathBlock_label.LibrariesList=JARs und Klassenordner im Pfa&d für die Berichtausführung:

###########################################################
#ListingSectionPage
ListingSectionPage.RepeatHeader=Kopfzeile wiede&rholen

###########################################################
#ResourceEditDialog
ResourceEditDialog.NotFile.Title=Datei nicht gefunden

ResourceEditDialog.NotFile.Message=Die Datei ''{0}'' existiert nicht.

ResourceEditDialog.ReadOnlyEncounter.Title=Schreibgeschützte Datei gefunden

ResourceEditDialog.ReadOnlyEncounter.Message=Datei ''{0}'' ist schreibgeschützt. Heben Sie den Schreibschutz auf.

###########################################################
#EventHandler
EventHandlerPage.Browse=&Durchsuchen...


###########################################################
#IDEFileReportProvider
IDEClassPathBlock.button_addExtFolder=Externen Klassen&ordner hinzufügen...

IDEClassPathBlock.button_addEXTJar=E&xterne JARs hinzufügen...

IDEClassPathBlock.button_addJar=&JARs hinzufügen...

IDEClassPathBlock.button_addProject=&Projekte hinzufügen...

IDEClassPathBlock.button_addVar=&Variable hinzufügen...

IDEClassPathBlock.button_down=Nach unten

IDEClassPathBlock.button_edit=&Bearbeiten...

IDEClassPathBlock.button_folder=&Klassenordner hinzufügen...

IDEClassPathBlock.button_remove=&Entfernen

IDEClassPathBlock.button_up=Nach oben

IDEClassPathBlock.fLibrariesList_text=JARs und Klassenordner im Pfa&d für die Berichtausführung:

IDEClassPathBlock.ProjectDialog_message=Hinzuzufügende Projekte auswählen:

IDEClassPathBlock.ProjectDialog_title=Erforderliche Projektauswahl

IDECPListLabelProvider.folder=(Klassenordner)

IDECPListLabelProvider.missing=(fehlt)

IDECPListLabelProvider.new=(neu)

IDECPListLabelProvider.unknown=unbekanntes Element

IDEFileReportProvider.ReadOnlyEncounter.Title=Schreibgeschützte Datei gefunden

IDEFileReportProvider.ReadOnlyEncounter.Message=Datei ''{0}'' ist schreibgeschützt. Heben Sie den Schreibschutz auf.

IDEResourcePageHelper.Dialog.Prompt=Wählen Sie ein Verzeichnis:

IDEResourcePageHelper.Dialog.Text=Ort relativ zum Arbeitsplatz &wählen:

IDEResourcePageHelper.Dialog.Title=Verzeichnisauswahl

IDEResourcePageHelper.File.Button=&Dateisystem...

IDEResourcePageHelper.Varible.Button=Varia&blen...

IDEResourcePageHelper.WorkSpace.Button=&Arbeitsplatz...

###########################################################
#DataSetColumnBindingsFormHandleProvider
DataSetColumnBindingsFormHandleProvider.Column.Name=Name

DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID=Anzeigenamen-ID

DataSetColumnBindingsFormHandleProvider.Column.DisplayName=Angezeigter Name

DataSetColumnBindingsFormHandleProvider.DatasetTitle=Datenspaltenbindung:

DataSetColumnBindingsFormHandleProvider.RefreshBindingColumns=Bindungsspalten aktualisieren

DataSetColumnBindingsFormHandleProvider.ReportItemTitle=Verfügbare Spaltenbindung:

DataSetColumnBindingsFormHandleProvider.NewBinding=Neue Bindung

DataSetColumnBindingsFormHandleProvider.Column.DataType=Datentyp

DataSetColumnBindingsFormHandleProvider.Column.Expression=Ausdruck

DataSetColumnBindingsFormHandleProvider.Column.AggregateOn=Aggregat in

DataSetColumnBindingsFormHandleProvider.Column.Filter=Filter

DataSetColumnBindingsFormHandleProvider.Column.Function=Funktion

DataSetColumnBindingsFormHandleProvider.ALL=Alles

DataSetColumnBindingsFormHandleProvider.GenerateBindingColumns=Bindungsspalten generieren

DataSetColumnBindingsFormHandleProvider.NONE=N/A
DataSetColumnBindingsFormHandleProvider.Trans.GenerateBindingColumns=Bindungsspalten generieren

DataSetColumnBindingsFormHandleProvider.Trans.RefreshBindingColumns=Bindungsspalten aktualisieren

###########################################################
#DataSetColumnBindingsFormPage
DataSetColumnBindingsFormPage.Button.RemoveUnused=Nicht verwendete Spalte entfernen

###########################################################
#DataSetSettingsPage
DataSetSettingsPage.DataGroup.Title=BIRT-Engine-Einstellungen zum Vergleichen von Datensatzspaltenwerten

DataSetSettingsPage.Locale.Label=&Gebietsschema der Zeichenfolgewerte:

DataSetSettingsPage.NullValueOrdering.Label=Reihenf&olge der Nullwerte:

###########################################################
#ResourceSelectionValidator
ResourceSelectionValidator.ExtError=Ausgewählte Datei muss enden auf:

ResourceSelectionValidator.EmptyError=Wählen Sie eine Datei aus.

ResourceSelectionValidator.ExtErrorSplitter=\ oder

###########################################################
#GeneralPage
GeneralPage.Library.Included=Bibliothek:

GeneralPage.Library.Modified.Note=HINWEIS: Dieses Berichtobjekt aus der Bibliothek wurde geändert. Klicken Sie zum Wiederherstellen des Objekts aus der Bibliothek auf{0}"Eigenschaften wiederherstellen".

#BookMarkPage
BookMarkPage.Modified.Note=Das Lesezeichen enthält Sonderzeichen statt der folgenden Zeichen(A-Z, a-z, 0-9, '-', '_', ':', '.'). Dies kann zu Problemen während der Laufzeit führen.

BookMarkPage.Modified.WithoutQuotes=Das Lesezeichen steht nicht in doppelten Anführungszeichen. Dies kann zu Problemen während der Laufzeit führen.

###########################################################
# LOCALE SPECIAL:
# following string need be specially localized, e.g. as per local country custom, not just simple translation
# for example:
#       the social security number in US is like 760-01-0101, the format should be @@@-@@-@@@@
#       but the SSN in Swedish is like 760101-0101, the format should be @@@@@@-@@@@
# 
FormatStringPage.SimpleTextZipCode=94103

FormatStringPage.SimpleTextZipCode4=94123

FormatStringPage.PhoneNumber=4155551111

FormatStringPage.SocialSecurityNumber=123456789

FormatStringPage.simpleTexZipCodeFormat=@@@@@

FormatStringPage.simpleTexZipCode4Format=@@@@@-@@@@
FormatStringPage.phoneNumberFormat=(@@@)@@@-@@@@
FormatStringPage.securityNumberFormat=@@@-@@-@@@@
FormatStringPage.Label.PreserveWhiteSpaces=Leerzeichen beibehalten

FormatStringPage.Preview.PreserveWhiteSpaces=\    Zeichenfolge mit führendem Leerzeichen

###########################################################
#PublishLibraryDialog
PublishLibraryDialog.ShellText=Bibliothek gemeinsam nutzen

PublishLibraryDialog.TitleArea=Bibliothek gemeinsam nutzen

PublishLibraryDialog.Message=Bibliothek in Ordner für gemeinsame Ressourcen kopieren.

PublishLibraryDialog.Label.SourceFileName=&Quelldatei:

PublishLibraryDialog.Label.FileName=Datei&name:

PublishLibraryDialog.Label.Folder=Or&dner:

PublishLibraryDialog.Label.Browse=&Durchsuchen...

PublishLibraryDialog.Message.FileNameEmpty=Dateiname erforderlich.

PublishLibraryDialog.Message.FileNameError=Dateiname muss auf .rptlibrary enden.

PublishLibraryDialog.Message.FolderEmpty=Veröffentlichungsordner erforderlich.

PublishLibraryDialog.Message.SourceFileEmpty=Name der Quelldatei erforderlich.

PublishLibraryDialog.Label.Browse2=Durchsu&chen...

PublishLibraryDialog.AddText=Bibliothek hinzufügen

PublishLibraryDialog.AddMessage=Bibliothek zum BIRT-Ressourcenordner hinzufügen

WizardLibrarySettingPage.ErrorMessage=Wählen Sie einen Ordner aus.

WizardLibrarySettingPage.Resourcefile.Dialog.Title=Ressourcenordner durchsuchen

WizardLibrarySettingPage.Resourcefile.Dialog.Message=Wählen Sie den Ressourcenordner oder dessen Unterordner aus.

PublishLibraryDialog.AddLibrary=Bibliothek hinzufügen


###########################################################
#PublishCSSDialog
PublishCSSDialog.ShellText=CSS-Datei veröffentlichen

PublishCSSDialog.TitleArea=CSS-Datei in Ressourcenordner veröffentlichen

PublishCSSDialog.Message=Wählen Sie eine CSS-Datei aus, um Stile in den Ressourcenordner zu importieren

PublishCSSDialog.Label.SourceFileName=&Quelldatei:

PublishCSSDialog.Label.FileName=Datei&name:

PublishCSSDialog.Label.Folder=&Ordner:

PublishCSSDialog.Label.Browse=&Durchsuchen...

PublishCSSDialog.Label.Browse2=Durchsu&chen...

PublishCSSDialog.ErrorMessage.SelectFolder=Wählen Sie einen Ordner aus.

PublishCSSDialog.Dialog.FileSeclect.Tilte=CSS-Datei auswählen

PublishCSSDialog.Error.Exception.Title=Fehler

PublishCSSDialog.Error.FileNotFound=Datei ''{0}'' wurde nicht gefunden.

PublishCSSDialog.Error.FileIsNotCSSFile=Datei ''{0}'' endet nicht mit ''{1}''.

PublishCSSDialog.Error.FileExist=Datei ist bereits vorhanden.

PublishCSSDialog.Message.SourceFileEmpty=Quelldateiname ist erforderlich.

PublishCSSDialog.Message.FileNameEmpty=Dateiname ist erforderlich.

PublishCSSDialog.Message.FileNameError=Dateiname muss auf .css enden.

PublishCSSDialog.Message.FolderEmpty=Veröffentlichungsordner ist erforderlich.

WizardCSSSettingPage.ErrorMessage=Wählen Sie einen Ordner aus.

WizardCSSSettingPage.Resourcefile.Dialog.Title=Ressourcenordner durchsuchen

WizardCSSSettingPage.Resourcefile.Dialog.Message=Wählen Sie den Ressourcenordner oder dessen Unterordner aus.

###########################################################
#PublishResourceDialog
PublishResourceDialog.ShellText=Ressource veröffentlichen

PublishResourceDialog.TitleArea=Ressource im Ressourcenordner veröffentlichen

PublishResourceDialog.Message=Ressourcendatei in Ressourcenordner aufnehmen

PublishResourceDialog.AddResource=Ressource hinzufügen

PublishResourceDialog.AddText=Ressource hinzufügen

PublishResourceDialog.AddMessage=Ressource zu Ressourcenordner hinzufügen

PublishResourceAction.wizard.errorTitle=Fehler

PublishResourceAction.wizard.message=Die Ressource kann nicht in dem Verzeichnis veröffentlicht werden, in dem sie sich befindet. Wählen Sie zur Veröffentlichung ein anderes Verzeichnis.

PublishResourceAction.wizard.message.SourceFileNotExist=Die Quelldatei ist nicht vorhanden.

PublishResourceAction.wizard.notvalidfolder=Der in der Einstellung definierte Ressourcenordner ist ungültig.

PublishResourceDialog.Label.FileName=&Dateiname:

PublishResourceDialog.Label.SourceFileName=&Quelldatei:

PublishResourceDialog.Label.Folder=Ordner:

PublishResourceDialog.Label.Browse=&Durchsuchen...

PublishResourceDialog.Label.Browse2=Durchsu&chen...

WizardResourceSettingPage.ErrorMessage=Wählen Sie einen Ordner aus.

WizardResourceSettingPage.Resourcefile.Dialog.Title=Ressourcenordner durchsuchen

WizardResourceSettingPage.Resourcefile.Dialog.Message=Wählen Sie den Ressourcenordner oder dessen Unterordner aus.

PublishResourceDialog.Message.SourceFileEmpty=Name der Quelldatei erforderlich.

PublishResourceDialog.Message.FileNameEmpty=Dateiname erforderlich.

PublishResourceDialog.Message.FolderEmpty=Veröffentlichungsordner erforderlich.

###########################################################
#EmbeddedImagesNodeProvider
EmbeddedImagesNodeProvider.FileNameError.Title=Fehler

EmbeddedImagesNodeProvider.FileNameError.Message=Die ausgewählte Datei ist keine Bilddatei.

EmbeddedImagesNodeProvider.stackMsg.insert=Bild einfügen

###########################################################
#AddElementtoAction properties
AddElementtoAction.Text=&Zum Bericht hinzufügen

ApplyThemeAction.Text=Auf Bericht &anwenden

###########################################################
#MultiPageReportEditor properties
MultiPageReportEditor.CheckVersion.Dialog.Title=Warnung

MultiPageReportEditor.CloseButton=Schließen

MultiPageReportEditor.ConfirmTitle=Datei ist nicht vorhanden.

MultiPageReportEditor.ConfirmVersion.Dialog.Message=Das Berichtdesign wurde mit einer früheren Version von BIRT Designer erstellt. Durch Speichern wird es auf die aktuelle Version aktualisiert. Dadurch kann es möglicherweise nicht mehr mit einer älteren Version von BIRT geöffnet werden. Möchten Sie das Berichtdesign trotzdem speichern?

MultiPageReportEditor.ConfirmVersion.Dialog.ReloadMessage=Der Inhalt dieses Editors wurde möglicherweise extern geändert. Möchten Sie die Änderungen neu laden?

MultiPageReportEditor.ConfirmVersion.Dialog.ResetMessage=Der Inhalt dieses Editors wurde möglicherweise extern geändert. Möchten Sie den Inhalt mit diesen Änderungen neu laden?

MultiPageReportEditor.ConfirmVersion.Dialog.SaveAndReloadMessage=Der Inhalt dieses Editors wurde möglicherweise extern geändert, und im Editor sind ungespeicherte Änderungen vorhanden.\n\nMöchten Sie den Editor speichern und die Änderungen neu laden?

MultiPageReportEditor.ConfirmVersion.Dialog.Title=Bestätigen

MultiPageReportEditor.SaveButton=&Speichern

MultiPageReportEditor.DiscardButton=&Verwerfen

MultiPageReportEditor.CancelButton=Abbrechen

MultiPageReportEditor.SaveConfirmMessage=Die Datei wurde gelöscht. Möchten Sie Ihre Änderungen speichern oder den Editor schließen, ohne zu speichern?

TableArea.Button.Up=&Nach oben

###########################################################
#AddJarResourceFileFolderSelectionDialog
AddJarResourceFileFolderSelectionDialog.ErrorMessage.Exist=Eine oder mehrere der ausgewählten Dateien existieren bereits.

AddJarResourceFileFolderSelectionDialog.Message=Wählen Sie {0} Dateien aus:

AddJarResourceFileFolderSelectionDialog.ErrorMessage.ExtError=Dateiname muss auf {0} enden.

AddJarResourceFileFolderSelectionDialog.Message.Or=oder


###########################################################
#AddImageResourceFileFolderSelectionDialog
AddImageResourceFileFolderSelectionDialog.Title=Eine oder mehrere der ausgewählten Dateien existieren bereits.

AddImageResourceFileFolderSelectionDialog.Message=Wählen Sie ein Bild aus:

###########################################################
#DataItemBindingDialog properties
DataItemBindingDialog.text.Name=Name:

DataItemBindingDialog.text.displayName=Anzeigename:

DataItemBindingDialog.text.DataType=Datentyp:

DataItemBindingDialog.text.Expression=Ausdruck:

DataItemBindingDialog.text.AggregateOn=Aggregat in:

DataItemBindingDialog.text.ForceBinding=Neue Bindung mit demselben Ausdruck erstellen

DataItemBindingDialog.text.All=Alles

DataItemBindingDialog.text.None=N/A
DataItemBindingDialog.text.Null=Ohne

DataItemBindingDialog.title.CreateNewItem=Neue Datenelementbindung

DataItemBindingDialog.title.EditDataItem=Datenelementbindung bearbeiten

DataItemBindingDialog.text.Function=Funktion:

DataItemBindingDialog.text.Filter=Filter:

DataItemBindingDialog.text.ArgumentList=Argument:

DataItemBindingDialog.Button.Add=Hinzufügen

DataItemBindingDialog.Button.Edit=Bearbeiten

DataItemBindingDialog.Button.Delete=Löschen

###########################################################
#DataColumBindingDialog properties
DataColumBindingDialog.title.CreateNewDataBinding=Neue Datenbindung

DataColumBindingDialog.title.EditDataBinding=Datenbindung bearbeiten

DataColumBindingDialog.title.AggBuilder=Aggregaterstellung

DataColumnBindingDialog.NewBindingDialogInputNewNameMessage=Neuer Name für Datenbindung:

DataColumnBindingDialog.NewBindingDialogInputNewNameTitle=Neuen Namen für Datenbindung angeben

DataColumnBindingDialog.NewBindingDialogMessage=Diese Datenbindung wird von mehr als einem Berichtobjekt und mehr als einer Zelle verwendet. Möchten Sie einen neuen Ausdruck für dieses Berichtobjekt erstellen? Klicken Sie auf 'Ja', um eine neue Datenbindung zu erstellen, oder auf 'Nein', um die Datenbindung für alle Stellen zu ändern.

DataColumnBindingDialog.NewBindingDialogButtonYes=Ja

DataColumnBindingDialog.NewBindingDialogButtonNo=Nein

DataColumnBindingDialog.NewBindingDialogButtonCancel=Abbrechen

DataColumnBindingDialog.NewBindingDialogTitle=Neue Datenbindung erstellen?

DataColumnBindingDialog.title.TimePeriodBuild=Aggregaterstellung für relatives Zeitintervall

DataColumBindingDialog.stackMsg.resetReference=Verweis zurücksetzen

###########################################################
#CreatePlaceHolderAction properties
ChangeDataColumnAction.text=Datenspalte än&dern...

###########################################################
#ThumbnailBuilder properties
ThumbnailBuilder.Text.Title=Miniatur verwalten

ThumbnailBuilder.Button.Text.Generate=&Generieren

ThumbnailBuilder.Button.Text.Remove=&Entfernen

ThumbnailBuilder.Button.Text.Import=&Importieren...

ThumbnailBuilder.Button.Text.Browse=&Durchsuchen...

ThumbnailBuilder.FileDialog.Title=Bilddatei auswählen

ThumbnailBuilder.FileDialog.FileNameError.Title=Fehler

ThumbnailBuilder.FileDialog.FileNameError.Message=Die ausgewählte Datei ist keine Bilddatei.

ThumbnailBuilder.Button.GenerateFromReport=Aus Bericht ge&nerieren

ThumbnailBuilder.Button.BrowseFromFileSystem=Dateisystem durch&suchen

ThumbnailBuilder.Button.ImportFromResource=Aus gemeinsamen Ressourcen i&mportieren

ThumbnailBuilder.Image.DefaultName=Miniaturbild

ThumbnailBuilder.PreviewImage.WarningDialog.Title=Warnung

ThumbnailBuilder.PreviewImage.WarningDialog.Prompt=Das ausgewählte Bild ist größer als 1014 x 768. Möglicherweise reicht der Speicher dafür nicht aus.\nMöchten Sie dennoch fortfahren?


###########################################################
#FragmentResourceEntry properties
FragmentResourceEntry.RootName=Fragmente

FragmentResourceEntry.RootDisplayName=Systemressourcen


FragmentTemplateResourceEntry.RootName=TemplateFragments
FragmentTemplateResourceEntry.RootDisplayName=Vorlagenressourcen

###########################################################
#PathResourceEntry properties
PathResourceEntry.RootName=Ressourcenordner

PathResourceEntry.RootDisplayName=Gemeinsame Ressourcen

###########################################################
#ResourceFilter properties
ResourceFilter.DisplayName.CVS=CVS-Ressourcen

ResourceFilter.DisplayName.Dot=.* Ressourcen

ResourceFilter.DisplayName.Data=*.data-Ressourcen

ResourceFilter.DisplayName.EmptyFolder=Leere Ordner

ResourceFilter.Description.CVS=Blendet Ressourcen aus, deren Name mit \"CVS\" beginnt

ResourceFilter.Description.Dot=Blendet Ressourcen aus, deren Name mit einem '.' beginnt

ResourceFilter.Description.Data=Blendet alle Datendateien aus.

ResourceFilter.Description.EmptyFolder=Blendet leere Ordner aus

###########################################################
#BIRTGotoMarker properties
BIRTGotoMarker.Error.Title=Fehler

BIRTGotoMarker.Error.Message=Das Berichtelement ist nicht vorhanden.

###########################################################
#TabbedList properties
TabbedPropertyList.properties.not.available=Eigenschaften sind nicht verfügbar.

###########################################################
#Sample Reports View properties
SampleReportsView.defautDescription=Wählen Sie aus den Kategorien einen Beispielbericht aus.

SampleReportsView.Text.Explorer=Beispielberichte

SampleReportsView.Text.Preview=Vorschau

SampleReportsView.Text.Description=Beschreibung

SampleReportsView.MessageDialog.Title=Frage

SampleReportsView.MessageDialog.Message=''{0}'' ist bereits vorhanden. Ersetzen?

SampleReportsView.Action.exportSampleReport=E&xportieren

SampleReportsView.Action.openSampleReport=&Importieren

SampleReportsView.Action.exportSampleReport.toolTipText=Ausgewählten Beispielbericht exportieren

SampleReportsView.Action.openSampleReport.toolTipText.ide=Berichtprojekt erstellen und Bericht im Projektordner aufnehmen

SampleReportsView.Action.openSampleReport.toolTipText.rcp=Ausgewählten Beispielbericht direkt in den Ordner des RCP-Arbeitsbereichs exportieren

SampleReportsView.Text.Contribute_Samples=Beispiele beitragen

UseCssStyleAction.text=CSS-Datei &verwenden...

EditUseCssStyleAction.text=CSS-Datei &bearbeiten...

EditUseCssStyleAction.EditCssTitle=CSS bearbeiten

EditUseCssStyleAction.EditCssAreaTitle.Report=CSS-Datei in Berichtdesign bearbeiten

EditUseCssStyleAction.EditCssAreaTitle.Libary=CSS-Datei in Berichtbibliothek bearbeiten

###########################################################
#GrossTabPageGenerator properties
CrosstabPageGenerator.List.General=Allgemein

CrosstabPageGenerator.List.Margin=Rand

CrosstabPageGenerator.List.Font=Schriftart

CrosstabPageGenerator.List.Borders=Rahmen

CrosstabPageGenerator.List.Section=Seitenumbruch

CrosstabPageGenerator.List.Visibility=Sichtbarkeit

CrosstabPageGenerator.List.TOC=Inhaltsverzeichnis

CrosstabPageGenerator.List.Bookmark=Lesezeichen

CrosstabPageGenerator.List.NamedExpressions=Benannte Ausdrücke

CrosstabPageGenerator.List.UserProperties=Benutzereigenschaften

CrosstabPageGenerator.List.AdvancedProperty=Erweitert

CrosstabPageGenerator.List.SubTotals=Zwischensumme

CrosstabPageGenerator.List.GrandTotals=Gesamtsummen

CrosstabPageGenerator.List.PageBreak=Seitenumbruch

###########################################################
#CubePageGenerator properties
CubePageGenerator.List.General=Allgemein

CubePageGenerator.List.Comments=Kommentare

CubePageGenerator.List.AdvancedProperty=Erweitert


###########################################################
#LibraryLayoutEditorFormPage
LibraryLayoutEditorFormPage.warning.title=Bibliothek

LibraryLayoutEditorFormPage.warning.message=Zum Hinzufügen eines Berichtobjekts zur Bibliothek ziehen Sie das Objekt aus der Palette, oder verwenden Sie das Menü 'Einfügen'.\n\nUm Berichtobjekte im Editor anzuzeigen, klicken Sie in der Gliederungsansicht darauf.

LibraryLayoutEditorFormPage.warning.prompt=&Diese Meldung nicht wieder anzeigen

###########################################################
#PageGenerator Item properties
PageGenerator.TabItem.Attributes=Eigenschaften

PageGenerator.TabItem.Binding=Bindung

PageGenerator.TabItem.Filters=Filter

PageGenerator.TabItem.Groups=Gruppen

PageGenerator.TabItem.Highlights=Kennzeichnungen

PageGenerator.TabItem.map=Zuordnung

PageGenerator.TabItem.Sorting=Sortierung

###########################################################
#AggDataDropAdapter
AggDataDropAdapter.Trans.Name=Aggregat erstellen

###########################################################
#BindingDialogHelper
BindingDialogHelper.text.Name=Name der Spalten&bindung:

BindingDialogHelper.text.DataType=Daten&typ:

BindingDialogHelper.text.Function=&Funktion:

BindingDialogHelper.text.DataField=Daten&feld:

BindingDialogHelper.text.Filter=Filter&bedingung:

BindingDialogHelper.text.AggOn=A&ggregat in:

BindingDialogHelper.text.Table=Tabe&lle

BindingDialogHelper.text.List=&Liste

BindingDialogHelper.text.Grid=Raster

BindingDialogHelper.text.Group=Gr&uppe

BindingDialogHelper.text.Expression=&Ausdruck:

BindingDialogHelper.text.displayName=Anzeige&name:

BindingDialogHelper.text.allowExport=Exp&ortieren:

BindingDialogHelper.text.allowExport.button=Export zulassen

BindingDialogHelper.text.displayNameID=Namen-ID an&zeigen:

BindingDialogHelper.bindingName.dataitem=Spaltenbindung

BindingDialogHelper.bindingName.aggregation=Aggregat

BindingDialogHelper.error.nameduplicate=Es ist bereits eine Spaltenbindung mit dem Namen {0} vorhanden.

BindingDialogHelper.error.improperexpression=Ungültiger Typ für Aggregat in Quadereinheit: '{0}'.

BindingDialogHelper.error.empty={0} ist erforderlich.

BindingDialogHelper.error.text.Name=Name der Spaltenbindung

BindingDialogHelper.text.Colon=:
BindingDialogHelper.warning.function=Der empfohlene Datentyp für die aktuelle Funktion ist '{0}'. Möchten Sie den ausgewählten Datentypen wirklich beibehalten?

BindingDialogHelper.warning.parameter=Der Ausgabedatentyp des Ausdrucks '{0}' wird für den Parameter '{1}' nicht unterstützt. Möchten Sie ihn wirklich beibehalten?

BindingDialogHelper.warning.button.yes=Ja

BindingDialogHelper.warning.button.no=Nein

BindingGroupDescriptorProvider.Flag.DataModel=\ (Datenmodell)

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.LinkModel=Datenmodellbindung:

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.DataSet=Datensatzbindung

###########################################################
#Scripts Node
Scripts.Node.DisplayName=Skripte

ScriptElementNode.Action.Text=Zu Berichtselement &wechseln

ScriptObjectNode_0=&Skript zurücksetzen

ReportPage.PreviewImage.Button.ToolTip=Miniatur verwalten

###########################################################
#CrosstabBindingDialogHelper
CrosstabBindingDialogHelper.AggOn.All=Alles

CrosstabBindingDialogHelper.bindngName.timeperiod=Periode

CrosstabBindingDialogHelper.calculation.label=Zeit&intervall:

CrosstabBindingDialogHelper.calculation.group=Zeitintervall

CrosstabBindingDialogHelper.recentday.description=Letztes Datum im Zeitintervall

CrosstabBindingDialogHelper.referencedate.label=&Bezugsdatum

CrosstabBindingDialogHelper.thisdate.label=Dieses Datum:

CrosstabBindingDialogHelper.thisdate.example.label=Bitte schließen Sie den angegebenen Datumswert in Anführungszeichen ein.\nBeispiel: "2012-01-23", "1/23/2012".

CrosstabBindingDialogHelper.thisdate.example.label.constant=Beispiel: 2012-01-23, 1/23/2012.

CrosstabBindingDialogHelper.timedimension.label=Zeitliche Di&mension:

CrosstabBindingDialogHelper.timeperiod.wrongdate=Diese Dimension unterstützt das aktuelle Bezugdatum nicht

CrosstabBindingDialogHelper.today.label=Heute

AddEditCascadingParameterDialog.label.name=&Name:

AddEditCascadingParameterDialog.label.dataset=&Datensatz:

AddEditCascadingParameterDialog.label.value=&Wert:

AddEditCascadingParameterDialog.label.displaytext=&Anzeigentext:

###########################################################
#FormatDateTimePattern
FormatDateTimePattern.datetiem_format_type_year=Jahr

FormatDateTimePattern.datetiem_format_type_short_year=Jahr (kurz)

FormatDateTimePattern.datetiem_format_type_long_month_year=Monat, Jahr (lang)

FormatDateTimePattern.datetiem_format_type_shot_month_year=Monat, Jahr (kurz)

FormatDateTimePattern.datetiem_format_type_month=Monat

FormatDateTimePattern.datetiem_format_type_long_day_of_week=Wochentag (lang)

FormatDateTimePattern.datetiem_format_type_day_of_month=Tag des Monats

FormatDateTimePattern.datetiem_format_type_medium_day_of_year=Tag des Jahres (mittel)

FormatDateTimePattern.datetiem_format_type_minutes=Minuten

FormatDateTimePattern.datetiem_format_type_secontds=Sekunden

FormatDateTimePattern.datetiem_format_type_general_time=Allgemeine Zeit

###############################################################################
# LOCALE SPECIAL:
# following text should be localized specifically for each locale, 
# the pattern may be different for each locale. 
# For example, in Polish locale, "general time" pattern should be "HH:mm:ss.SSS" 
# instead of "ahh:mm:ss.SSS".
#
FormatDateTimePattern.pattern.datetiem_format_type_year=jjjj

FormatDateTimePattern.pattern.datetiem_format_type_short_year=jj

FormatDateTimePattern.pattern.datetiem_format_type_long_month_year=MMMM jjjj

FormatDateTimePattern.pattern.datetiem_format_type_shot_month_year=MMM jj

FormatDateTimePattern.pattern.datetiem_format_type_month=MMMM

FormatDateTimePattern.pattern.datetiem_format_type_long_day_of_week=EEEE

FormatDateTimePattern.pattern.datetiem_format_type_day_of_month=tt

FormatDateTimePattern.pattern.datetiem_format_type_medium_day_of_year=MMMM tt, jj

FormatDateTimePattern.pattern.datetiem_format_type_minutes=mm

FormatDateTimePattern.pattern.datetiem_format_type_secontds=ss

FormatDateTimePattern.pattern.datetiem_format_type_general_time=ahh:mm:ss.SSS

FormatDescriptorProvider.DisplayName.None=Automatisch

ReportExamples.ContributeSamples.description=Reichen Sie einen Bugzilla-Eintrag unter {0} ein. Melden Sie einen Bug an BIRT{1}.\nHängen Sie die Berichtdesigndatei und den Screenshot über die Berichtausgabe an.\n\nHinweis: Der Screenshot sollte folgende Spezifikationen erfüllen:\n1. *.png-Bildformat;\n2. Auflösung 800*600;\n3. Max. Größe 250 KB.

ReportExamples.ContributeSamples.title=Beispiele beitragen

ReportExamples.ContributeSamples.messages=Im Folgenden finden Sie eine Anleitung zum Beitragen von Beispielen.

ReportExamples.OpenBrowser.Error.title=Fehler

ReportExamples.OpenBrowser.Error.message=Webbrowser kann nicht geöffnet werden.

###########################################################
#CopyCellContentsContextAction
CopyCellContentsContextAction.actionText=In&halt kopieren

###########################################################
#CopyCellContentsAction
CopyCellContentsAction.actionText=In&halt kopieren

###########################################################
#CopyFormatAction
CopyFormatAction.text=&Format kopieren

###########################################################
#PasteFormatAction
PasteFormatAction.text=F&ormat einfügen

###########################################################
#InsertAggregationAction
InsertAggregationAction.text=&Aggregat


###########################################################
#InsertRelativeTimePeriodAction
InsertRelativeTimePeriodAction.text=Relati&ves Zeitintervall

###########################################################
#Multiple views
MultipleAdapter.SetCurrentView=Aktuelle Ansicht einstellen

MultipleAdapter.RemoveView=Ansicht entfernen

MultipleGuideHandle.RemoveView=Entfernen

CreateChartAction.text=Diagramm-A&nsicht erstellen

CreateChartHandler.text=Diagramm-Ansicht erstellen

###########################################################
#Input Parameters
InputParameterDialog.msg.title=Eingabeparameter

InputParameterDialog.msg.requiredParam=Mit * gekennzeichnete Parameter sind erforderlich.

InputParameterDialog.err.requiredParam=Der Wert des erforderlichen Parameters [{0}] wurde nicht festgelegt.

InputParameterDialog.err.invalidValueTitle=Ungültiger Wert

InputParameterDialog.err.invalidValue=Der Wert '{0}' mit dem Typ {1} ist ungültig.

InputParameterDialog.err.emptyValue=Der Eingabewert für {0} darf nicht leer sein.

InputParameterDialog.err.emptyValue2=Der zweite Eingabewert für {0} darf nicht leer sein.

###########################################################
#Crosstab drag tracker
ColumnDragTracker.ResizeColumn=Spaltengröße anpassen

ColumnDragTracker.Show.Label=Breite:

CPListLabelProvider.misssing=(fehlt)

RowDragTracker.ResizeRow=Zeilengröße anpassen

RowDragTracker.Show.Label=Höhe:

TableDragGuideTracker.Pixels=Pixel

###########################################################
#General Designer settings
GeneralConfigurationBlock.message.general.description=Allgemeine Einstellungen für Report Designer.

GeneralConfigurationBlock.button.text.enable.gradient=&Verlaufsauswahl aktivieren

GeneralConfigurationBlock.button.text.enable.animation=A&nimierte Auswahl aktivieren

GeneralConfigurationBlock.group.label=Eingabeaufforderung

GeneralConfigurationBlock.group.defaultThemes=Bibliothek der Standardthemen

GeneralConfigurationBlock.button.text.prompt=&Eingabeaufforderung nach Öffnen der Bibliotheksdatei anzeigen

GeneralConfigurationBlock.button.text.defaultTheme.enable=Bibliothek der Standard&themen aktivieren

GeneralConfigurationBlock.button.text.defaultTheme.include=Standardthemen e&inschließen

GeneralConfigurationBlock.move.binding.group=Lokale Bindungen für eine verbesserte Leistung zu übergeordnetem Element verschieben

GeneralConfigurationBlock.move.binding.always=I&mmer

GeneralConfigurationBlock.move.binding.never=N&ie

GeneralConfigurationBlock.move.binding.prompt=Auff&ordern

###########################################################
#ResetImageSizeAction
ResetImageOriginalSizeAction.trans.label=Bildgröße zurücksetzen

ResetImageSizeAction.label=Auf Original&größe zurücksetzen
###########################################################
#Report document editor
ReportDocumentEditor.1=Berichtdokument-Editor

ReportDocumentEditor.2=Dokumentinfomationen

ReportDocumentEditor.3=Name:

ReportDocumentEditor.4=Version:

ReportDocumentEditor.5=Seitenzähler:

ReportDocumentEditor.errorMessage=Fehler beim Öffnen des Dokuments im Editor.

###########################################################
#Export Element Dialog
ExportElementDialog.Dialog.Title=Elemente in Bibliothek exportieren

ExportElementDialog.Dialog.Message=Zielbibliothek auswählen oder neues Ziel erstellen.

ExportElementDialog.label.NewFile=&Neuer Dateiname:

ExportElementDialog.ErrorMessage=Dateiname muss auf '.rptlibrary' enden.

ExportElementDialog.ErrorMessageInvalid=Ungültiger Dateiname.

ExportElementDialog.ErrorMessageDuplicate=Das Element ist bereits in der Bibliothek vorhanden.

ExportElementDialog.ErrorStatusCanNotExport=Das Element kann nicht exportiert werden.

ExportElementDialog.WarningMessageDuplicate.Message=Doppelter Komponentenname. Vorhandene Bibliothekskomponente überschreiben?\n\nKlicken Sie auf {0}, um die vorhandene Komponente zu ersetzen.\nKlicken Sie auf {1}, um zum Dialogfeld 'Element exportieren' zurückzukehren.\nKlicken Sie auf {2}, um den Vorgang abzubrechen.

ExportElementDialog.WarningMessageDuplicate.Title=Ersetzen bestätigen

ExportElementDialog.WarningMessageDuplicate.OverrideImage=Das eingebettete Bild, auf das vom exportierten Bild verwiesen wird, ist bereits in der Bibliothek vorhanden. Möchten Sie es ersetzen?

###########################################################
#NewStyleHandler
NewStyleHandler.transaction.label=Neuer Stil...

AdvancePropertyDescriptorProvider.DisplayName=Erweitert

AdvancePropertyDescriptorProvider.Tooltip.Alphabetic=Alphabetisch sortieren

AdvancePropertyDescriptorProvider.Tooltip.Group=Nach Gruppe sortieren

AdvancePropertyDescriptorProvider.Tooltip.Local=Lokale Eigenschaften anzeigen

PropertyEditorFactory.Boolean.True=wahr

PropertyEditorFactory.Boolean.False=falsch

PropertyEditorFactory.Value.Auto=Automatisch

ExportElementToLibraryAction.waring=Warnung

ExportElementToLibraryAction.waringMsg=Das Element wurde nicht benannt. Es kann kein Name für das Element festgelegt werden.

###########################################################
#ExportStyleDialog
ExportStyleDialog.Label.Text=&Thema:

ExportStyleDialog.Message=Thema zum Export in die Bibliothek wählen.

ExportStyleDialog.Title=Thema wählen

###########################################################
#UIUtil
UIUtil.previewconfirm.title=Fehler im Bericht

UIUtil.previewconfirm.message=Der Bericht enthält Fehler. Möchten Sie mit der Vorschau fortfahren?

###########################################################
#FileReportProvider
FileReportProvider.CopyConfigFile.DeleteFailure=Fehler beim Löschen von Datei: {0}

###########################################################
#ShowPropertiesViewMenu
ShowPropertiesViewMenu.ActionName=E&igenschaften

ShowPropertiesViewMenu.ErrorMessage=Fehler beim Öffnen der Eigenschaftenansicht.

ShowPropertiesViewMenu.ErrorTitle=Fehler

###########################################################
#ResetAttributePage
ResetAttributePage.Style.Restore.TooltipText=Standardstileigenschaften der aktuellen Seite wiederherstellen

ResetAttributePage.Style.Restore.Transaction.Name=Stil wiederherstellen
ResetAttributePage.Style.Restore.Menu.Name=Standardstile wiederherstellen
ResetAttributePage.Style.Restore.All.Transaction.Name=Alle Stile wiederherstellen
ResetAttributePage.Style.Restore.All.Menu.Name=Standardstile auf allen Registerkarten wiederherstellen

###########################################################
#TextEditor
TextEditor.PasteFormattedText=Formatierten Text einfügen

###########################################################
#ReloadImageAction
ReloadImageAction.Text=&Bild neu laden

###########################################################
#VariablesNodeProvider
VariablesNodeProvider.NewActionName=&Neue Variable

VariablesNodeProvider.Dialog.Title=Neue Variable

VariablesNodeProvider.NodeName=Variablen

###########################################################
#VariableNodeProvider
VariableNodeProvider.DialogTitle=Variable bearbeiten

###########################################################
#SelectVariableDialog
SelectVariableDialog.AvailableVariables=Verfügbare V&ariablen:

SelectVariableDialog.PageSeperator=----- Seitenvariablen -----

SelectVariableDialog.ReportSeperator=----- Berichtvariablen -----

SelectVariableDialog.Title=Variable einfügen

###########################################################
#VariableDialog
VariableDialog.DataType=Daten&typ:

VariableDialog.DefaultValue=&Standardwert:

VariableDialog.Name=&Name:

VariableDialog.PageVariable=Sei&tenvariable

VariableDialog.ReportVariable=&Berichtvariable

VariableDialog.VariableType=Geben Sie Folgendes ein:

VariableDialog.Error.NameDuplicate=Der Name \"{0}\" ist bereits vorhanden. Bitte wählen Sie einen anderen Namen aus.

###########################################################
#ExpressionBuilderConfigurationBlock
ExpressionBuilderConfigurationBlock.DefaultSyntax=&Standardsyntax:

###########################################################
#ExpressionEditor
ExpressionEditor.Error.EmptyExpression=Ausdruck darf nicht leer sein.

ExpressionEditor.Label.Expression=&Ausdruck:

ExpressionEditor.Title=Ausdruck bearbeiten

ExpressionSyntaxColoringPage.Accessible.Name.Background=Hintergrundfarbauswahl

ExpressionSyntaxColoringPage.Accessible.Name.Foreground=Vordergrundfarbauswahl

ExpressionSyntaxColoringPage.Button.Bold=&Fett

ExpressionSyntaxColoringPage.Button.Enable=Aktivieren

ExpressionSyntaxColoringPage.Button.Italic=&Kursiv

ExpressionSyntaxColoringPage.Button.Restore=&Standardwert wiederherstellen

ExpressionSyntaxColoringPage.Button.Strikethrough=&Durchgestrichen

ExpressionSyntaxColoringPage.Button.Underline=&Unterstrichen

ExpressionSyntaxColoringPage.Decscription=Um die Kennzeichnung der Syntax anzupassen, wählen Sie den Texttyp aus der Liste oder dem Beispieltextbereich aus, dessen Stil Sie anpassen möchten.

ExpressionSyntaxColoringPage.Element.Comment=Kommentar

ExpressionSyntaxColoringPage.Element.Default=Standard

ExpressionSyntaxColoringPage.Element.Keyword=Schlüsselwort

ExpressionSyntaxColoringPage.Element.String=Zeichenfolge

ExpressionSyntaxColoringPage.Label.Background=Hintergr&und:

ExpressionSyntaxColoringPage.Label.Foreground=&Vordergrund:

ExpressionSyntaxColoringPage.Label.SampleText=&Beispieltext:

ExpressionSyntaxColoringPage.Label.SyntaxElement=S&yntaxelement:

ExpressionSyntaxColoringPage.Link.ColorAndFont=Standardfarben und -schriftarten können auf der Voreinstellungsseite <a href="org.eclipse.ui.preferencePages.GeneralTextEditor">'Texteditor'</a> und <a href="org.eclipse.ui.preferencePages.ColorsAndFonts">'Schriftarten und Farben'</a> konfiguriert werden.

###########################################################
#VariableDropAdapter
VariableDropAdapter.TranasctionName=Datumsobjekt aus Variablen erstellen

###########################################################
#RenameInputDialog
RenameInputDialog.Message.BlankName=Der Name darf nicht leer sein.

RenameInputDialog.Message.DuplicateName=Der Name ist bereits vorhanden.

###########################################################
#Breadcrumb
BreadcrumbItemDropDown.showDropDownMenu.action.toolTip=Untergeordnete Elemente anzeigen

ToggleBreadcrumbAction.tooltip.switch.breadcrumb=Brotkrume ein/aus

###########################################################
#HyperlinkParameterProvider Category
ReportHyperlinkParameterProvider.Category.Report=Berichtparameter

RenderHyperlinkParameterProvider.Category.Render=Render-Optionen
ExceptionDialog.title=Fehler
