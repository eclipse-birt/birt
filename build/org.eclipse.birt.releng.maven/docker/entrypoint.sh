#!/bin/bash
set -e

PROJECT=birt
FILE_DEFAULT="BIRT-Runtime.aggr"
REPO_DEFAULT="repo-birt"
FILE=${1:-${FILE_DEFAULT}}
PROJECT_AGGR=build/org.eclipse.birt.releng.maven/${FILE}
AGGR=/opt/cbiAggr/cbiAggr

WORKSPACE=/workspace
REPO=${WORKSPACE}/${2:-${REPO_DEFAULT}}
REPO_RAW=${WORKSPACE}/repo-raw

FILE_AGGR=${WORKSPACE}/${PROJECT_AGGR}
LOCAL_P2=${WORKSPACE}/build/org.eclipse.birt.p2updatesite/target/repository

SNAPSHOT=${SNAPSHOT:-false}

echo "--------------------------------------------------"
echo "BIRT CBI Aggregator (LOCAL WORKSPACE)"
echo "Aggregation file : ${FILE_AGGR}"
echo "Local p2 repo    : ${LOCAL_P2}"
echo "SNAPSHOT         : ${SNAPSHOT}"
echo "--------------------------------------------------"

if [ ! -d "${LOCAL_P2}" ]; then
  echo "ERROR: Local p2 repository not found:"
  echo "  ${LOCAL_P2}"
  echo "Did you run the BIRT build first?"
  exit 1
fi

# Patch snapshot flag
if [ "${SNAPSHOT}" = "true" ]; then
  sed -i 's/snapshot=".*"/snapshot="true"/g' "${FILE_AGGR}"
else
  sed -i 's/snapshot=".*"/snapshot="false"/g' "${FILE_AGGR}"
fi

mkdir -p "${REPO_RAW}"

"${AGGR}" aggregate \
  -consoleLog \
  --buildModel "${FILE_AGGR}" \
  --action CLEAN_BUILD \
  --buildRoot "${REPO_RAW}" \
  -vmargs \
    -Xmx4G \
    -Dorg.eclipse.ecf.provider.filetransfer.excludeContributors=org.eclipse.ecf.provider.filetransfer.httpclientjava \
    -Dp2.${PROJECT}=file:${LOCAL_P2}

mkdir -p "${REPO}"

if [ "${SNAPSHOT}" = "true" ]; then
  mv "${REPO_RAW}/final" "${REPO}"
else
  mv "${REPO_RAW}/final-unpublished" "${REPO}"
fi

rm -rf "${REPO_RAW}"

echo "Aggregation finished successfully."