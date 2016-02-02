@echo off
if "%1"=="" goto HELP

:START
java -cp %classpath%;../lib/c45.jar ml.classifier.dt.C45 %1 %2 %3 %4 %5
goto END

:HELP
echo Usage: c45 ^<dataSetName^>
echo Example: c45 ..\dataset\UCI\golf
goto END

:END
