:: spring.bat
:: Automatically produced by machine at 2009-05-01 ������20:19:00.31
:: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
::
@echo off
set currentPath=%cd% 
cd ../../lib 
set libPath=%cd%
cd %currentPath%
jar xf ../../lib/dtree.zip
java -classpath %classpath%;%libPath%/xstream-1.3.1.jar;%libPath%/c45.jar; ml.demo.C45Spring "golf-hibernation.xml" > "golf-spring.html"
"golf-spring.html"
