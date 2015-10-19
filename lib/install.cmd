@echo off
call mvn install:install-file -Dfile=MarvinBeans-5.0.jar -DgroupId=chemaxon -DartifactId=MarvinBeans -Dversion=5.0 -Dpackaging=jar
call mvn install:install-file -Dfile=sdlib.jar -DgroupId=com.pfizer -DartifactId=sdlib -Dversion=1.0 -Dpackaging=jar
