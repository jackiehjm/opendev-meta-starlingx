DESCRIPTION = "stx-config"

PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCREV = "d97dd66f45a5ab4ae88d86e5f6c4b573bf7e7396"

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
