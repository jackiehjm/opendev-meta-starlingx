SUMMARY = "StarlingX sysinv extensions: Cert Manager"
DESCRIPTION = "StarlingX sysinv extensions: Cert Manager"
SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=41687b590435621fc0676ac02c51154f"

PV = "1.0"
PR = "15"
PRAUTO = "tis"

DEPENDS += "\
    ${PYTHON_PN}-pbr-native \
    ${PYTHON_PN}-pip-native \
    ${PYTHON_PN}-wheel-native \
    "

inherit setuptools
inherit stx-metadata

STX_REPO = "cert-manager-armada-app"
STX_SUBPATH = "python-k8sapp-cert-manager/k8sapp_cert_manager"

S = "${WORKDIR}/k8sapp_cert_manager"

PACKAGES += "${PN}-wheels"
PROVIDES += "${PN}-wheels"

export PBR_VERSION = "${PV}"
export SKIP_PIP_INSTALL = "1"

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cp -rf ${STX_METADATA_PATH} ${WORKDIR}
}

do_configure_preppend() {
	rm -rf k8sapp_cert_manager.egg-info
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
