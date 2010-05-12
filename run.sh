#!/bin/sh

OPTS="$*"
VNCSERVER=/usr/bin/vncserver
MVN=/import/tools/maven/current/bin/mvn

if [ ! -x $MVN ]; then
    MVN=mvn
fi

function die() {
    echo "$*"
    exit 1
}

if [ -z "$DISPLAY" ]; then die "You must set the \$DISPLAY variable to use this script. Example: export DISPLAY=:1"; fi

echo Killing any lingering VNC servers on display $DISPLAY
$VNCSERVER -kill $DISPLAY 2>&1

echo Starting vncserver on display $DISPLAY
$VNCSERVER $DISPLAY -SecurityTypes None 2>&1 || die "Could not start vncserver. See output above."
echo Started vncserver on display $DISPLAY

echo Starting $MVN $OPTS
$MVN $OPTS

echo Stopping vncserver on $DISPLAY
$VNCSERVER -kill $DISPLAY 2>&1
