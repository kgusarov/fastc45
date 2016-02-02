:: htmlview.bat
:: Automatically produced by machine at 2009-05-01 ÐÇÆÚÎå20:19:00.28
:: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
::
@echo off
@jar xf ../../lib/dtree.zip
@java -jar ../../lib/c45.jar "../../dataset/UCI/golf" -output html > "golf.html"
"golf.html"
