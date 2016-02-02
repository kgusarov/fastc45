:: plainview.bat
:: Automatically produced by machine at 2009-05-01 ĞÇÆÚÎå20:18:54.84
:: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
::
@echo off
@java -jar ../../lib/c45.jar "../../dataset/UCI/iris" -output plain > "iris.out"
@notepad "iris.out"
