########################################
# Determine the location of the script #
########################################

# resolve symbolic links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`

##############################################
# Identify Maven location relative to script #
##############################################

M2_HOME="$PRGDIR"/"$EXECUTABLE"/../apache-maven
EXECUTABLE="$M2_HOME"/bin/mvn


# Check that target executable exists
if [ ! -x "$EXECUTABLE" ]; then
  echo "Cannot find $EXECUTABLE"
  echo "This file is needed to run this program"
  exit 1
fi

#exec "$EXECUTABLE" start "$@"

#############################
# Execute the Maven command #
#############################

exec "$EXECUTABLE" -version