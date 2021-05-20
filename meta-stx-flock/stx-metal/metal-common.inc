DESCRIPTION = "stx-metal"

STABLE = "starlingx/master"
PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCNAME = "metal"
SRCREV = "ae753abfe9f565ae7cf7a2d4b8c4360d83b31cca"

STXPV = "1.0.0"
DESTSUFFIX = "${BPN}-${STXPV}"
PV = "${STXPV}+git${SRCPV}"

S = "${WORKDIR}/${DESTSUFFIX}"

SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
	"

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
