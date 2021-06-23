SUMMARY = "StarlingX Monitor Application Armada Helm Charts"
DESCRIPTION = "StarlingX Monitor Application Armada Helm Charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    monitor-helm \
    monitor-helm-elastic \
"

inherit allarch
inherit stx-metadata

STX_REPO = "monitor-armada-app"
STX_SUBPATH = "stx-monitor-helm/stx-monitor-helm"

helm_folder = "${nonarch_libdir}/helm"
armada_folder = "${nonarch_libdir}/armada"
app_folder = "${nonarch_libdir}/application"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	install -d -m 755 ${D}${armada_folder}
	install -p -D -m 755 ${STX_METADATA_PATH}/manifests/*.yaml ${D}${armada_folder}
	install -d -m 755 ${D}${app_folder}
	install -p -D -m 755 ${STX_METADATA_PATH}/files/metadata.yaml ${D}${app_folder}/monitor_metadata.yaml
}

FILES_${PN} = " \
    ${app_folder} \
    ${armada_folder} \
"

RDEPENDS_${PN} = " \
    helm \
    monitor-helm \
    monitor-helm-elastic \
"
