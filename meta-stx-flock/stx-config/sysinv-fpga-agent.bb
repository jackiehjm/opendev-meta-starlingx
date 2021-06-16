SUMMARY = "StarlingX FPGA Agent Package"

require config-common.inc

SUBPATH0 = "sysinv/sysinv-fpga-agent"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN} = " \
	sysinv-fpga-agent.service \
	sysinv-conf-watcher.service \
	sysinv-conf-watcher.path \
	"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install_append() {
	# compute init scripts
	install -d -m 755 ${D}${sysconfdir}/init.d
	install -p -D -m 755 sysinv-fpga-agent ${D}${sysconfdir}/init.d/sysinv-fpga-agent

	install -d -m 755 ${D}${sysconfdir}/pmon.d
	install -p -D -m 644 sysinv-fpga-agent.conf ${D}${sysconfdir}/pmon.d/sysinv-fpga-agent.conf
	install -p -D -m 644 sysinv-fpga-agent.service ${D}${systemd_system_unitdir}/sysinv-fpga-agent.service
	install -p -D -m 644 sysinv-conf-watcher.service ${D}${systemd_system_unitdir}/sysinv-conf-watcher.service
	install -p -D -m 644 sysinv-conf-watcher.path ${D}${systemd_system_unitdir}/sysinv-conf-watcher.path

	sed -i -e 's|${bindir}/systemctl|${base_bindir}/systemctl|' ${D}${systemd_system_unitdir}/sysinv-conf-watcher.service

	# Workaround to call "docker login" during startup.  Called by puppet.
	install -d -m 755 ${D}${sbindir}
	install -p -D -m 755 run_docker_login ${D}${sbindir}/run_docker_login
}

RDEPENDS_${PN} += " bash"

