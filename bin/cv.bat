@echo off
if "%1"=="" goto HELP

:START
@java -cp ../lib/c45.jar ml.classifier.CrossValidator %1 %2 %3 %4 %5
goto END

:HELP
echo Usage: cv ^<dataSetName^> [fold]
echo Example: cv ..\dataset\UCI\golf 10 
goto END

:END
