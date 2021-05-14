DESCRIPTION = "stx-config"

PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCREV = "ac3fb24b1d372b9797642862962b9403539980d1"

STXPV = "1.0.0"
DESTSUFFIX = "${BPN}-${STXPV}"
PV = "${STXPV}+git${SRCPV}"

S = "${WORKDIR}/${DESTSUFFIX}"

SRC_URI = "\
	git://opendev.org/starlingx/config.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
	"

DEPENDS = "\
	puppet \
	python \
	python-pbr-native \
	"

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
