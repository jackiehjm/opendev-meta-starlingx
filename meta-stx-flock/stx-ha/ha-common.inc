DESCRIPTION = "stx-ha"

PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCNAME = "ha"
SRCREV = "e8f2c3dc42ce702e7453e2633b6df468764bb27a"

STXPV = "1.0.0"
DESTSUFFIX = "${BPN}-${STXPV}"
PV = "${STXPV}+git${SRCPV}"

S = "${WORKDIR}/${DESTSUFFIX}"

SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
	"

inherit systemd

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

DEPENDS += " \
	fm-common \
	sqlite3 \
	python \
	python-pbr-native \
	"
