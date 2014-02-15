#!/bin/bash

#-------------------------------------------------------------------
#    Tomcat Bootstrap Script 
#
#
#    The JAVA_HOME environment is necessary
#
#-------------------------------------------------------------------

# Judge the environment
cygwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
esac

# Find JAVA_HOME
noJavaHome=false
if [ -z "$JAVA_HOME" ] ; then
    noJavaHome=true
fi
if $cygwin ; then
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath -u "$JAVA_HOME"`
fi
if [ ! -e "$JAVA_HOME/bin/java" ] ; then
    noJavaHome=true
fi
if $noJavaHome ; then
    echo "Error: JAVA_HOME environment variable is not set."
    exit 1
fi

# Set Project Home
CURR_DIR=`pwd`
cd `dirname "$0"`/..
PROJECT_HOME=`pwd`
cd $CURR_DIR

if [ -z "$PROJECT_HOME" ] ; then
    echo
    echo "error, environment variable“PROJECT_HOME”not found "
    echo
    exit 1
fi

if [ -f "$PROJECT_HOME/conf/launcher.properties" ]; then
. $PROJECT_HOME/conf/launcher.properties
fi

CLASSPATH="$PROJECT_HOME/webapp/WEB-INF/lib/*"


if $cygwin ; then
    JAVA_HOME=`cygpath -w "$JAVA_HOME"`
    PROJECT_HOME=`cygpath -w "$PROJECT_HOME"`
    CLASSPATH=`cygpath -p -w "$CLASSPATH"`
fi


DEFAULT_OPTS="$DEFAULT_OPTS -Dproject.home=\"$PROJECT_HOME\" -Dproject.name=\"$APP_NAME\" -Dstart.port=${port} -Dstop.port=${shutdownPort} -DcontextPath=${contextPath} -Dwebapp=${PROJECT_HOME}/webapp "

CMD="exec \"$JAVA_HOME/bin/java\" $JVM_OPTIONS  $DEFAULT_OPTS $APP_OPTIONS -classpath \"$CLASSPATH\"  $STOP_MAIN_CLASS $APP_ARGS $@"
eval $CMD
