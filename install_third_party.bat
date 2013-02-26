call mvn install:install-file -Dfile="projects/net-sf-tweety-math/lib/choco-1_2_03.jar" -DgroupId="net.sf.tweety.dependencies" -DartifactId="choco" -Dversion="1.2.03" -Dpackaging="jar"
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b

call mvn install:install-file -Dfile="projects/net-sf-tweety-math/lib/cream106.jar" -DgroupId="net.sf.tweety.dependencies" -DartifactId="cream" -Dversion="1.0.06" -Dpackaging="jar"
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b

call mvn install:install-file -Dfile="projects/net-sf-tweety-math/lib/qoca.jar" -DgroupId="net.sf.tweety.dependencies" -DartifactId="qoca" -Dversion="1.0.0" -Dpackaging="jar"
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b
