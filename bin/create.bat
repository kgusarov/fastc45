:: creator.bat 
:: Meta Batch File Creator, Just for make a project demo
:: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
::
@echo off

if "%1"=="" goto HELP

md "..\work\%1" 2>nul
cd "..\work\%1"

:: Make htmlview.bat
set batFilename=htmlview.bat
 >"%batFilename%" echo :: %batFilename%
>>"%batFilename%" echo :: Automatically produced by machine at %date%%time%
>>"%batFilename%" echo :: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
>>"%batFilename%" echo ::
>>"%batFilename%" echo @echo off
>>"%batFilename%" echo @jar xf ../../lib/dtree.zip
>>"%batFilename%" echo @java -jar ../../lib/c45.jar "../../dataset/UCI/%1" -output html ^> "%1.html"
>>"%batFilename%" echo "%1.html"

:: Make plainview.bat
set batFilename=plainview.bat
 >"%batFilename%" echo :: %batFilename%
>>"%batFilename%" echo :: Automatically produced by machine at %date%%time%
>>"%batFilename%" echo :: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
>>"%batFilename%" echo ::
>>"%batFilename%" echo @echo off
>>"%batFilename%" echo @java -jar ../../lib/c45.jar "../../dataset/UCI/%1" -output plain ^> "%1.out"
>>"%batFilename%" echo @notepad "%1.out"

@REM Make xmlview.bat
set batFilename=xmlview.bat
 >"%batFilename%" echo :: %batFilename%
>>"%batFilename%" echo :: Automatically produced by machine at %date%%time%
>>"%batFilename%" echo :: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
>>"%batFilename%" echo ::
>>"%batFilename%" echo @echo off
>>"%batFilename%" echo @java -jar ../../lib/c45.jar "../../dataset/UCI/%1" -output xml ^> "%1.xml"
>>"%batFilename%" echo "%1.xml"

:: Make hibernate.bat
set batFilename=hibernate.bat
 >"%batFilename%" echo :: %batFilename%
>>"%batFilename%" echo :: Automatically produced by machine at %date%%time%
>>"%batFilename%" echo :: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
>>"%batFilename%" echo ::
>>"%batFilename%" echo @echo off
>>"%batFilename%" echo set currentPath=%%cd%% 
>>"%batFilename%" echo cd ../../lib 
>>"%batFilename%" echo set libPath=%%cd%%
>>"%batFilename%" echo cd %%currentPath%%
>>"%batFilename%" echo java -classpath %%classpath%%;%%libPath%%/xstream-1.3.1.jar;%%libPath%%/c45.jar; ml.demo.C45Hibernation "../../dataset/UCI/%1" ^> "%1-hibernation.xml"
>>"%batFilename%" echo "%1-hibernation.xml"

:: Make spring.bat
set batFilename=spring.bat
 >"%batFilename%" echo :: %batFilename%
>>"%batFilename%" echo :: Automatically produced by machine at %date%%time%
>>"%batFilename%" echo :: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
>>"%batFilename%" echo ::
>>"%batFilename%" echo @echo off
>>"%batFilename%" echo set currentPath=%%cd%% 
>>"%batFilename%" echo cd ../../lib 
>>"%batFilename%" echo set libPath=%%cd%%
>>"%batFilename%" echo cd %%currentPath%%
>>"%batFilename%" echo jar xf ../../lib/dtree.zip
>>"%batFilename%" echo java -classpath %%classpath%%;%%libPath%%/xstream-1.3.1.jar;%%libPath%%/c45.jar; ml.demo.C45Spring "%1-hibernation.xml" ^> "%1-spring.html"
>>"%batFilename%" echo "%1-spring.html"
 
:: Make clean.bat
set batFilename=clean.bat
 >"%batFilename%" echo :: %batFilename%
>>"%batFilename%" echo :: Automatically produced by machine at %date%%time%
>>"%batFilename%" echo :: Created by Xiaohua Xu, 2009/04/23, arterx@gmail.com
>>"%batFilename%" echo ::
>>"%batFilename%" echo @echo off
>>"%batFilename%" echo @rd dTree /S/Q 2^>nul
>>"%batFilename%" echo @del "%1*.html" /Q 2^>nul
>>"%batFilename%" echo @del "%1.out" /Q 2^>nul
>>"%batFilename%" echo @del "%1*.xml" /Q 2^>nul

cd ..\..\bin
goto END

:HELP
echo Usage: create ^<dataSetName^>
echo Example: create ..\dataset\UCI\golf
echo.
goto END

:END


