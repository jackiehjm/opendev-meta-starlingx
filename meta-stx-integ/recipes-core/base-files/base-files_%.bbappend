inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "setup-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
        file://${STX_METADATA_PATH}/centos/setup-config.spec;beginline=1;endline=10;md5=0ba4936433e3eb7acdd7d236af0d2496 \
        "

do_install_append() {

    install -d ${D}/${sysconfdir}/profile.d
    install -m 644 ${STX_METADATA_PATH}/files/motd ${D}/${sysconfdir}/motd
    install -m 644 ${STX_METADATA_PATH}/files/prompt.sh ${D}/${sysconfdir}/profile.d/prompt.sh
    install -m 644 ${STX_METADATA_PATH}/files/custom.sh ${D}/${sysconfdir}/profile.d/custom.sh
   # chmod 600 ${D}/{sysconfdir}/exports
   # chmod 600 ${D}/{sysconfdir}/fstab
}
