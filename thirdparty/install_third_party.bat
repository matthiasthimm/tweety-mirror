call mvn install:install-file -Dfile="lib/net-sf-tweety-math/choco-1_2_03.jar" -DgroupId="net.sf.tweety.dependencies" -DartifactId="choco" -Dversion="1.2.03" -Dpackaging="jar"
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b

call mvn install:install-file -Dfile="lib/net-sf-tweety-math/cream106.jar" -DgroupId="net.sf.tweety.dependencies" -DartifactId="cream" -Dversion="1.0.06" -Dpackaging="jar"
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b

call mvn install:install-file -Dfile="lib/net-sf-tweety-math/qoca.jar" -DgroupId="net.sf.tweety.dependencies" -DartifactId="qoca" -Dversion="1.0.0" -Dpackaging="jar"
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b

call mvn install:install-file -Dfile="lib/net-sf-tweety-graphs/Jama-1.0.3.jar" -DgroupId="net.sf.tweety.dependencies" -DartifactId="jama" -Dversion="1.0.3" -Dpackaging="jar"
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b

call mvn install:install-file -Dfile="lib/net-sf-tweety-cli/jspf.core-1.0.2.jar" -DgroupId="net.sf.tweety.dependencies" -DartifactId="jspf" -Dversion="1.0.2" -Dpackaging="jar"
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b
