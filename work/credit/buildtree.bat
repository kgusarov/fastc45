@echo off
@rd dTree /S/Q 2>nul
@del "credit*.html" /Q 2>nul
@del "credit.out" /Q 2>nul
@del "credit*.xml" /Q 2>nul

set currentPath=%cd%
cd ../../lib
set libPath=%cd%
cd %currentPath%

java -classpath %classpath%;%libPath%/xstream-1.3.1.jar;%libPath%/c45.jar; ml.demo.C45Hibernation "dataset/credit" > "credit-hibernation.xml"

cd %currentPath%
jar xf ../../lib/dtree.zip
java -classpath %classpath%;%libPath%/xstream-1.3.1.jar;%libPath%/c45.jar; ml.demo.C45Spring "credit-hibernation.xml" > "credit-spring.html"
@del "credit-hibernation.xml"
"credit-spring.html"