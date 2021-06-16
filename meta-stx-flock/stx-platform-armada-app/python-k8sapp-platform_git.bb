SUMMARY = "StarlingX sysinv extensions: Platform Integration K8S app"
DESCRIPTION = "StarlingX sysinv extensions: Platform Integration K8S app"
SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=41687b590435621fc0676ac02c51154f"

DEPENDS += "\
    ${PYTHON_PN}-pbr-native \
    ${PYTHON_PN}-pip-native \
    ${PYTHON_PN}-wheel-native \
    "

PV = "1.0"
PR = "28"
PRAUTO = "tis"

inherit setuptools
inherit stx-metadata

STX_REPO = "platform-armada-app"
STX_SUBPATH = "${BPN}/k8sapp_platform"

S = "${WORKDIR}/k8sapp_platform"

PACKAGES += "${PN}-wheels"
PROVIDES += "${PN}-wheels"

export PBR_VERSION = "${STXPV}"
export SKIP_PIP_INSTALL = "1"

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cp -rf ${STX_METADATA_PATH} ${WORKDIR}
}

do_configure_preppend() {
	rm -rf k8sapp_platform.egg-info
}

do_compile_append() {
	${PYTHON_PN} setup.py bdist_wheel
}

do_install_append() {
	install -d ${D}/plugins
	install -m 0644 ${S}/dist/*.whl ${D}/plugins
}

FILES_${PN}-wheels += "/plugins"

SYSROOT_DIRS += "/plugins"
