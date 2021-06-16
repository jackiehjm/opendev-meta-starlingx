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

require platform-armada-app-common.inc
SUBPATH0 = "python-k8sapp-platform/k8sapp_platform"

PACKAGES += "${PN}-wheels"
PROVIDES += "${PN}-wheels"

export PBR_VERSION = "${STXPV}"
export SKIP_PIP_INSTALL = "1"

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
