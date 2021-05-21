
DESCRIPTION = "stx-update"

PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCNAME = "update"
SRCREV = "cb9e932614477dd3675c60a5e6d48b119e4c1c89"

STXPV = "1.0.0"
DESTSUFFIX = "${BPN}-${STXPV}"
PV = "${STXPV}+git${SRCPV}"

S = "${WORKDIR}/${DESTSUFFIX}"

SRC_URI = " \
   git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
   "

DEPENDS = " \
	python \
	python-pbr-native \
	"
