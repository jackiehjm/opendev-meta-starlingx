PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCNAME = "platform-armada-app"
SRCREV = "42b97c591a38167623f6dccb35f3b3ff67fd78db"

STXPV = "1.0.0"
DESTSUFFIX = "${BPN}-${STXPV}"
PV = "${STXPV}+git${SRCPV}"

S = "${WORKDIR}/${DESTSUFFIX}"

SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0} \
	"
