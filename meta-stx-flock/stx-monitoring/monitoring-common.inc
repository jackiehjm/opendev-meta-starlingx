DESCRIPTION = "stx-monitoring"

PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCNAME = "monitoring"
SRCREV = "2c456b087506437d32af445df1c6df788da7203b"

STXPV = "1.0.0"
DESTSUFFIX = "${BPN}-${STXPV}"
PV = "${STXPV}+git${SRCPV}"

S = "${WORKDIR}/${DESTSUFFIX}"

SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
	"

DEPENDS += " \
	python \
	python-pbr-native \
	mtce \
	fm-common \
	json-c \
	openssl \
	libevent \
	libgcc \
	"
