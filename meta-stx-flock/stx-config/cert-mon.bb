SUMMARY = "StarlingX Certificate Monitor Package"

require config-common.inc

SUBPATH0 = "sysinv/cert-mon/files"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN} = "cert-mon.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -m 755 -p -D cert-mon ${D}/usr/lib/ocf/resource.d/platform/cert-mon
	install -m 644 -p -D cert-mon.service ${D}/${systemd_system_unitdir}/cert-mon.service
	install -m 644 -p -D cert-mon.syslog ${D}/${sysconfdir}/syslog-ng/conf.d/cert-mon.conf
	install -m 644 -p -D cert-mon.logrotate ${D}/${sysconfdir}/logrotate.d/cert-mon.conf
}

FILES_${PN} += "\
	/usr/lib/ocf/resource.d \
	${systemd_system_unitdir} \
	${sysconfdir} \
	"
