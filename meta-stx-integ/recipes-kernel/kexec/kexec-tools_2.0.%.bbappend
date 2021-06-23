FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
	file://kdump.conf \
	file://kdump.sysconfig \
	"

do_install_append () {
	rm -f ${D}${sysconfdir}/sysconfig/kdump.conf

	install -m 0644 ${WORKDIR}/kdump.conf ${D}${sysconfdir}
	install -m 0644 ${WORKDIR}/kdump.sysconfig ${D}${sysconfdir}/sysconfig/kdump
}
