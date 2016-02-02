:: hibernate.bat
:: Automatically produced by machine at 2009-05-01 ÐÇÆÚÎå20:19:00.30
:: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
::
@echo off
set currentPath=%cd% 
cd ../../lib 
set libPath=%cd%
cd %currentPath%
java -classpath %classpath%;%libPath%/xstream-1.3.1.jar;%libPath%/c45.jar; ml.demo.C45Hibernation "../../dataset/UCI/golf" > "golf-hibernation.xml"
"golf-hibernation.xml"
