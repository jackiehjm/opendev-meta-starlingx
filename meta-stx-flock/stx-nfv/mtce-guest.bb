PACKAGES += " ${PN}agent"
PACKAGES += " ${PN}server"

require nfv-common.inc

SUBPATH0 = "${PN}/src"

SRC_URI += " \
	file://0001-mtce-guest-Fix-ldflags-usage.patch \
	file://0002-mtce-guest-agent-server-fix-script-path.patch;striplevel=3 \
	file://0003-guest-agent-server-fix-daemon-paths.patch;striplevel=3 \
	"

inherit systemd
SYSTEMD_PACKAGES += "${PN}agent"
SYSTEMD_PACKAGES += "${PN}server"
SYSTEMD_SERVICE_${PN}agent = "guestAgent.service"
SYSTEMD_SERVICE_${PN}server= "guestServer.service"
SYSTEMD_AUTO_ENABLE_${PN}agent = "disable"
SYSTEMD_AUTO_ENABLE_mtce-geustserver = "enable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

RDEPENDS_${PN} += " ${PN}agent ${PN}server"

EXTRA_OEMAKE = '-e MAJOR="1" MINONR="0" \
		INCLUDES=" -I. -I${STAGING_INCDIR}/mtce-common/ -I${STAGING_INCDIR}/mtce-daemon/ " \
		CPPFLAGS="${CXXFLAGS}" LDFLAGS="${LDFLAGS}"'

do_install() {

	oe_runmake -e install DESTDIR=${D} PREFIX=${D}/usr/ \
		       SYSCONFDIR=${D}/${sysconfdir} \
		            LOCALBINDIR=${D}/${bindir} \
			    UNITDIR=${D}/${systemd_system_unitdir}

	rm -rf ${D}/var
	rm -rf ${D}/var/run
}

FILES_${PN}server = " \
	${sysconfdir}/logrotate.d/guestServer.logrotate \
	${systemd_system_unitdir}/guestServer.service \
	${sysconfdir}/mtc/guestServer.ini \
	${sysconfdir}/init.d/guestServer \
	${sysconfdir}/pmon.d/guestServer.conf \
	${bindir}/guestServer \
	${sysconfdir}/mtc/tmp \
	"

FILES_${PN}agent = " \
	${systemd_system_unitdir}/guestAgent.service \
	${sysconfdir}/logrotate.d/guestAgent.logrotate \
	${libdir}/ocf/resource.d/platform/guestAgent \
	${sysconfdir}/mtc/guestAgent.ini \
	${sysconfdir}/init.d/guestAgent \
	${bindir}/guestAgent \
"

FILES_${PN} = ""

ALLOW_EMPTY_${PN} = "1"
