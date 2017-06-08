require mender.inc

def mender_branch_from_preferred_version(pref_version):
    if not pref_version:
        return "master"
    else:
        # Return part before "-git", which should be branch name.
        return pref_version[0:pref_version.index("-git")]

MENDER_BRANCH = "${@mender_branch_from_preferred_version(d.getVar('PREFERRED_VERSION'))}"

SRC_URI = "git://github.com/mendersoftware/mender;protocol=https;branch=${MENDER_BRANCH}"

# The revision listed below is not really important, it's just a way to avoid
# network probing during parsing if we are not gonna build the git version
# anyway. If git version is enabled, the AUTOREV will be chosen instead of the
# SHA.
def mender_autorev_if_git_version(d):
    version = d.getVar("PREFERRED_VERSION")
    if version is not None and "git" in version:
        return d.getVar("AUTOREV")
    else:
        return "f6ffa190892202263fdb75975059fbb201adab6a"
SRCREV ?= '${@mender_autorev_if_git_version(d)}'

PV = "${MENDER_BRANCH}-git${SRCPV}"

# DO NOT change the checksum here without make sure that ALL licenses (including
# dependencies) are included in the LICENSE variable below.
LIC_FILES_CHKSUM = "file://LIC_FILES_CHKSUM.sha256;md5=ab273c82f23a11ee5ac246ad04db8b9e"
LICENSE = "Apache-2.0 & BSD-2-Clause & BSD-3-Clause & MIT & OLDAP-2.8"

# Downprioritize this recipe in version selections.
DEFAULT_PREFERENCE = "-1"
