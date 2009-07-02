#/*******************************************************************************
# * Copyright (c) 2008 Actuate Corporation.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v1.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-v10.html
# *
# * Contributors:
# *  Actuate Corporation  - initial API and implementation
# *******************************************************************************/

###########################################################
#HTML Emitter
HTMLEmitter.Description=HTML (HyperText Markup Language) 形式でレポートをレンダリングするためのオプションを設定します。
HTMLEmitter.DisplayName=HTML

###########################################################
#Option Display Value
OptionDisplayValue.AgentStyleEngine=スタイルの処理をユーザエージェントに委ねる
OptionDisplayValue.Enable=有効
OptionDisplayValue.Disable=無効
OptionDisplayValue.OutputMasterPageMargins=マスターページの余白を出力
OptionDisplayValue.MasterPageContent=ページヘッダとフッタを出力
OptionDisplayValue.PageFooterFloat=浮動ページフッタ
OptionDisplayValue.LayoutPrefrence=レイアウトの設定 :
OptionDisplayValue.FixedLayout=固定レイアウト
OptionDisplayValue.AutoLayout=自動レイアウト
OptionDisplayValue.HTMLType=出力の種類 :
OptionDisplayValue.CompleteHTML=HTML (完全)
OptionDisplayValue.HTMLNoCSS=HTML (CSS なし)

###########################################################
#Option Description
OptionDescription.AgentStyleEngine=True : HTML エミッターは、BIRT のスタイルを直接リポートに出力します。スタイルの計算の実装はブラウザに委ねられます。\nFalse : HTML エミッターは、BIRT スタイルエンジンを使用してスタイルの計算を行い、結果をレポートに出力します。
OptionDescription.OutputMasterPageMargins=自動レイアウトレポートのマスターページの余白を出力します。
OptionDescription.MasterPageContent=ページヘッダとフッタが含まれています。ページ規則が有効な場合のみ影響します。
OptionDescription.PageFooterFloat=ページフッタを浮動にします。ページ規則が有効な場合のみ影響します。
OptionDescription.HTMLType=種類は HTML です。
