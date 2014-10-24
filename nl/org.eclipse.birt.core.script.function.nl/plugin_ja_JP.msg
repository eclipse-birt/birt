#Properties file for org.eclipse.birt.core.script.bre
Bundle-Name.0 = 関数プラグイン

dataType.String=String

dataType.Any=Any

dataType.DateTime=DateTime
dataType.Integer=Integer

dataType.Double=Double

dataType.Number=Number

dataType.Boolean=Boolean

dataType.DoubleArray=Double[]

FunctionDesc.charLength=文字列内の文字数を返します。

FunctionDesc.concat=指定したすべての文字列を結合した文字列を返します。

FunctionDesc.indexOf=ソース文字列でターゲットを検索し、最初にパターンに一致する文字のインデックスを返します。 \n検索は、start で指定した位置から開始されます。 すべてのインデックス値はゼロベースです。\nstart を省略した場合、0 が指定されたとみなされます。 \n文字列検索では大文字と小文字が区別されます。

FunctionDesc.left=文字列の最初から n 個の文字を返します。 \nn が 0 の場合は、空白の文字列を返します。 \nn が str の長さよりも大きい場合は、文字列全体を返します。 \nn を省略した場合は、値 1 を使用します。

FunctionDesc.right=文字列 str の最後から n 個の文字を返します。 \nn が 0 の場合は、空白の文字列を返します。 n が str の長さよりも大きい場合は、文字列全体を返します。 \nn を省略した場合は、値 1 を使用します。

FunctionDesc.search=indexOf 関数とほぼ同じですが次の点で異なります : \n(1) 文字列の比較では大文字と小文字を区別ない\n(2) パターン文字列にはワイルドカード文字を含めることができる\n* は任意の文字のシーケンス (空白を含む) に一致する\n?  は、任意の 1 文字に一致する

FunctionDesc.toLower=文字列のソースにあるすべての文字を小文字に変換します。

FunctionDesc.toUpper=文字列のソースにあるすべての文字を大文字に変換します。

FunctionDesc.trim=先頭および末尾にあるすべての空白文字 (スペース、タブなど) を削除します。\nまた、連続するすべての空白文字は 1 つに結合されます。

FunctionDesc.trimLeft=先頭にあるすべての空白を削除します。 末尾および単語間の空白は削除されません。

FunctionDesc.trimRight=末尾にあるすべての空白を削除します。 先頭および単語間の空白は削除されません。

FunctionDesc.anyOf=ソースがターゲットで指定した範囲に含まれるか

FunctionDesc.between=ソースがターゲット 1 とターゲット 2 の間にある場合は TRUE、 それ以外は FALSE 返します。

FunctionDesc.notBetween=ソースがターゲット 1 とターゲット 2 の間にない場合は TRUE、 それ以外は FALSE 返します。

FunctionDesc.equalTo=ソースはターゲットと等しい

FunctionDesc.greaterThan=ソースはターゲットより大きい

FunctionDesc.lessThan=ソースはターゲットより小さい

FunctionDesc.greaterOrEqual=ソースはターゲット以上

FunctionDesc.lessOrEqual=ソースはターゲット以下

FunctionDesc.notEqual=ソースとターゲットは等しくない

FunctionDesc.like=ソースはターゲットの SQL パターンと一致

FunctionDesc.notLike=ソースはターゲットの SQL パターンと一致しない

FunctionDesc.match=ソースはターゲットの JavaScript パターンと一致

FunctionDesc.compareString=2 つの文字列を指定した条件で比較

FunctionDesc.add=2 つの数値 n1 と n2 の合計。

FunctionDesc.substract=n1 から n2 を引く

FunctionDesc.multiple=n1 と n2 を掛ける。 この関数は、関数 [multiply] の下位互換目的でのみ使用できます。

FunctionDesc.multiply=n1 と n2 を掛ける

FunctionDesc.divide=被序数を除数で割る

FunctionDesc.round=数値を指定した桁で四捨五入します。 \n桁数には負数も指定できます。 \n指定しない場合は、桁数のデフォルト値は 0 になります。

FunctionDesc.roundUp=数値を指定した桁で切り上げます。 \nデフォルトの桁数は 0 です。

FunctionDesc.roundDown=数値を指定した桁で切り捨てます。 \nデフォルトの桁数は 0 です。

FunctionDesc.ceiling=数値を指定した精度の倍数に最も近い値に切り上げます。

FunctionDesc.mod=数値を除数で割った余りです。 結果の符合は除数と同じなります。

FunctionDesc.safeDivide=被序数を除数で割った結果が 0 または無限でない場合は、その結果を返します。 \nそれ以外は、0 の場合は指定した値を返します。

FunctionDesc.year=指定した日付/時間の値の年 (4 桁)

FunctionDesc.quarter=指定した日付/時間の値の四半期数 (1-4)

FunctionDesc.month=日付/時間の値の月。 (オプション) 整数値 : \n1 (デフォルト) : 月の数値 (1-12) を返す\n2 : ユーザのロケールにより正式な月の名前を返す (例 : 英語の場合は January から December)\n3 : ユーザのロケールにより省略した月の名前を返す (例 : 英語の場合は Jan から Dec)

FunctionDesc.week=日付/時間の値 d の年間週数 (1-52)。

FunctionDesc.day=月の日数 (1-31)。

FunctionDesc.weekDay=曜日。 (オプション) 整数値 : \n1 : 1 (日曜日) から 7 (土曜日) までの数値を返す\n2 : 1 (月曜日) から 7 (日曜日) までの数値を返す\n3 : 0 (月曜日) から 6 (日曜日) までの数値を返す\n4 : ユーザのロケールにより曜日名を返す (例 : 英語の場合は Sunday から Saturday)\n5 : ユーザのロケールに対応する曜日名の省略形を返す (例 : 英語の場合は Sun から Sat)

FunctionDesc.today=現在の日付の午前 0 時のタイムスタンプの値を返します。

FunctionDesc.now=現在のタイムスタンプを返します。

FunctionDesc.diffYear=差を年数で返します。

FunctionDesc.diffMonth=差を月数で返します。

FunctionDesc.diffQuarter=差を四半期数で返します。

FunctionDesc.diffWeek=差を週数で返します。

FunctionDesc.diffDay=差を日数で返します。

FunctionDesc.diffHour=差を時間数で返します。

FunctionDesc.diffMinute=差を分数で返します。

FunctionDesc.diffSecond=差を秒数で返します。

FunctionDesc.addYear=日付に n 年を加算

FunctionDesc.addMonth=日付に n か月を加算

FunctionDesc.addQuarter=日付に n 四半期を加算

FunctionDesc.addWeek=日付に n 週を加算

FunctionDesc.addDay=日付に n 日を加算

FunctionDesc.addHour=日付に n 時間を加算

FunctionDesc.addMinute=日付に n 分を加算

FunctionDesc.addSecond=日付に n 秒を加算

FunctionDesc.firstDayOfYear=日付が含まれる年の初日を返します。

FunctionDesc.firstDayOfQuarter=日付が含まれる四半期の初日を返します。

FunctionDesc.firstDayOfMonth=日付が含まれる月の初日を返します。

FunctionDesc.firstDayOfWeek=日付が含まれる週の初日を返します。

FunctionDesc.ddb=二倍定率法を使用して算出した、特定の 1 期間の資産の減価償却 (注釈)。

FunctionDesc.fv=定期的な定額支払いと一定利率に基づいた年金の将来価値。

FunctionDesc.ipmt=定期的な定額支払いと一定利率に基づいた\n特定期間の年金の利払い。

FunctionDesc.irr=既存の配列内の、一連の定期的なキャッシュフロー、支払い、収入の内部収益率。

FunctionDesc.mirr=既存の配列内の、一連の定期的なキャッシュフロー (支払いおよび収入) の修正収益率。

FunctionDesc.nper=定期的な定額支払いと一定利率に基づいた年金の期間数。

FunctionDesc.npv=変動する一連の定期的なキャッシュフロー (流出入) の、特定の利率での正味現在価値。

FunctionDesc.percent=2 つの数値の割合 (%) を計算します。

FunctionDesc.pmt=定期的な定額支払いと一定利率に基づいた年金の支払い。

FunctionDesc.ppmt=利率と定額支払いスケジュールに基づいた、特定期間の年金の元金支払い。

FunctionDesc.pv=将来の定期的な定額支払いと一定利率に基づいた年金の現在価値。

FunctionDesc.rate=期間あたりの年金の利率。

FunctionDesc.sln=1 期間の資産の定額償却。

FunctionDesc.syd=年数の合計に基づいた、特定の期間の資産の減価償却 (注釈)。

FunctionDesc.addDate=開始日に年数、月数、日数を加えて得られた日付を返します。

FunctionDesc.addTime=開始日に時間数、分数、秒数を加えて得られた日付を返します。

FunctionDesc.subDate=開始日から年数、月数、日数を引いて得られた日付を返します。

FunctionDesc.subTime=開始日から時間数、分数、秒数を引いて得られた日付を返します。

FunctionDesc.days=2 つの日付の間の日数

FunctionDesc.hours=2 つの日付の間の時間数

FunctionDesc.minutes=2 つの日付の間の分数

FunctionDesc.months=2 つの日付の間の月数

FunctionDesc.seconds=2 つの日付の間の秒数

FunctionDesc.years=2 つの日付の間の年数

FunctionDesc.date= 指定の年、月、日、時、分、秒で初期化された Date オブジェクトを返します。 \n(年、月、日は必ず入力し、時、分、秒は必要に応じて入力してください)

FunctionDesc.duration.year=[年] フィールドの値を整数値 * として取得します。存在しない場合は 0 を返します。\nlexicalDuration は、期間を表す文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.month=[月] フィールドの値を整数値 * として取得します。存在しない場合は 0 を返します。\nlexicalDuration は、期間を表す文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.day=[日] フィールドの値を整数値 * として取得します。存在しない場合は 0 を返します。\nlexicalDuration は、期間を表す文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.hour=[時間] フィールドの値を整数値として取得します。存在しない場合は 0 を返します。\nlexicalDuration は、期間を表す文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.minute=[分] フィールドの値を整数値として取得します。存在しない場合は 0 を返します。\nlexicalDuration は、期間を表す文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.second=[秒] フィールドの値を整数値として取得します。存在しない場合は 0 を返します。\nlexicalDuration は、期間を表す文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.timeInMills=期間の長さをミリ秒で返します。\nlexicalDuration は文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。\nstartDate 引数を使用すると、一意的な解釈を指定できます

FunctionDesc.duration.isLongerThan=lexicalDuration1 が lexicalDuration2 よりも厳密に長いかどうかをチェックします。\n例 :「P1D」(1 日) >「PT12H」(12 時間) および「P2Y」(2 年) >「P23M」(23 か月)

FunctionDesc.duration.isShorterThan=lexicalDuration1 が lexicalDuration2 よりも厳密に短いかどうかをチェックします。\n例 :「PT12H」(12 時間) <「P1D」(1 日)

FunctionDesc.duration.add=lexicalDuration1 + lexicalDuration2 の値の新しい期間を計算します。\nlexicalDuration は文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.substract=lexicalDuration1 - lexicalDuration2 の値の新しい期間を計算します。\nlexicalDuration は文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.multiply=lexicalDuration の値よりもファクタが長い新しい期間を計算します。\nlexicalDuration は文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。\nFactor is the times longer of new Duration to create

FunctionDesc.duration.negate=-lexicalDuration の値の新しい期間オブジェクトを返します。\nlexicalDuration は文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.getSign=-1、0、または 1 の期間の記号を返します。\nlexicalDuration は文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.compare=lexicalDuration1 と lexicalDuration2 の間の半順序関係です。\nlexicalDuration は文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。

FunctionDesc.duration.addTo=Date オブジェクトにこの期間を加算します。\nlexicalDuration は文字列プレゼンテーションで、「PnYnMnDTnHnMnS」(例 : P5Y2M15DT4H40M20S) で示します。\nThe given Date argument is used to be modified

FunctionDesc.format=指定したオブジェクトおよびパターンから書式設定された文字列を取得します。
