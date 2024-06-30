#/*******************************************************************************
# * Copyright (c) 2008 Actuate Corporation.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v2.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-2.0.html
# *
# * Contributors:
# *  Actuate Corporation  - initial API and implementation
# *******************************************************************************/

###########################################################
#HTML Emitter
HTMLEmitter.Description=配置用于以 HTML (超文本标记语言)格式呈现报表的选项。

HTMLEmitter.DisplayName=HTML
XHTMLEmitter.Description=配置用于以 XHTML 格式呈现报表的选项。

XHTMLEmitter.DisplayName=XHTML

###########################################################
#Option Display Value
OptionDisplayValue.AgentStyleEngine=将样式处理委托给用户代理

OptionDisplayValue.Enable=启用

OptionDisplayValue.Disable=禁用

OptionDisplayValue.OutputMasterPageMargins=输出主页边距

OptionDisplayValue.MasterPageContent=输出页眉和页脚

OptionDisplayValue.PageFooterFloat=浮动页脚

OptionDisplayValue.LayoutPreference=布局首选项:

OptionDisplayValue.FixedLayout=固定布局

OptionDisplayValue.AutoLayout=自动布局

OptionDisplayValue.HTMLType=输出类型:

OptionDisplayValue.CompleteHTML=完成 HTML

OptionDisplayValue.HTMLNoCSS=无 CSS 的 HTML

###########################################################
#Option Description
OptionDescription.AgentStyleEngine=真: 表示 HTML 发射器会直接将 BIRT 样式输出到报表，并依赖于浏览器实施样式计算。\n假: 表示 HTML 发射器将使用 BIRT 样式引擎计算样式，并将结果输出到报表。

OptionDescription.OutputMasterPageMargins=输出自动布局报表的主页边距。

OptionDescription.MasterPageContent=包含页眉和页脚，仅在启用分页时生效。

OptionDescription.PageFooterFloat=浮动页脚，仅在启用分页时生效。

OptionDescription.HTMLType=HTML 类型。
