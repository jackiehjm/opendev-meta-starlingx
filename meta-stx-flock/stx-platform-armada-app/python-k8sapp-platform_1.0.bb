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

inherit setuptools

PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCREV = "42b97c591a38167623f6dccb35f3b3ff67fd78db"

STXPV = "1.0.0"
PV = "${STXPV}+git${SRCPV}"

STX_REPO = "platform-armada-app"
STX_SUBPATH = "python-k8sapp-platform/k8sapp_platform"

SRC_URI = " \
    git://opendev.org/starlingx/${STX_REPO};protocol=${PROTOCOL};branch=${BRANCH} \
"

S = "${WORKDIR}/git/${STX_SUBPATH}"

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
