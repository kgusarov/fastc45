:: plainview.bat
:: Automatically produced by machine at 2009-05-01 ĞÇÆÚÎå20:19:00.29
:: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
::
@echo off
@java -jar ../../lib/c45.jar "../../dataset/UCI/golf" -output plain > "golf.out"
@notepad "golf.out"
