DESCRIPTION = "stx-fault"

PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCREV = "86544b085b2553515da81be4b88b267381b0c6cb"

STXPV = "1.0.0"
DESTSUFFIX = "${BPN}-${STXPV}"
PV = "${STXPV}+git${SRCPV}"

S = "${WORKDIR}/${DESTSUFFIX}"

LICENSE = "Apache-2.0"

DEPENDS = " \
        util-linux \
        postgresql \
        python \
        python-pbr-native \
        python-six \
        python-oslo.i18n \
        python-oslo.utils \
        python-requests \
        bash \
        net-snmp \
"

RDEPENDS_${PN} += " bash"


SRC_URI = " \
   git://opendev.org/starlingx/fault.git;protocol=${PROTOCOL};rev=${SRCREV};destsuffix=${DESTSUFFIX};branch=${BRANCH};subpath=${SUBPATH0};name=opendev \
   "
