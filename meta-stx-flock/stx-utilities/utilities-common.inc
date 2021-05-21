
DESCRIPTION = "stx-utilities"

PROTOCOL = "https"
SRCNAME = "utilities"
BRANCH = "r/stx.5.0"
SRCREV = "f65f5d91916b2a62344b3fb8e69097137bd73183"

STXPV = "1.0.0"
DESTSUFFIX = "${BPN}-${STXPV}"
PV = "${STXPV}+git${SRCPV}"

S = "${WORKDIR}/${DESTSUFFIX}"

SRC_URI = " \
   git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
   "

DEPENDS = " \
	python-pip \
	python-pbr-native \
	systemd \
"

## Skip tpm2-openssl-engine2
