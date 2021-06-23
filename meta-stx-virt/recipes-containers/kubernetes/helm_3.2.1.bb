
SUMMARY = "The Kubernetes Package Manager"
HOMEPAGE = "https://github.com/kubernetes/helm/releases "
SECTION = "devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a18660411af65e17f370bdbb50e6957"

S = "${WORKDIR}/linux-amd64"

inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "kubernetes/helm/centos/files"

SRC_URI = " \
	https://get.helm.sh/helm-v${PV}-linux-amd64.tar.gz \
	"
SRC_URI[md5sum] = "98764c2c0175bd306223cc985700d619"
SRC_URI[sha256sum] = "018f9908cb950701a5d59e757653a790c66d8eda288625dbb185354ca6f41f6b"

INSANE_SKIP_${PN} = "ldflags"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT  = "1"

RDEPENDS_${PN} += " bash"


do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	install -m 0755 -d ${D}/${sbindir}/
	install -m 0750 -d ${D}/${sysconfdir}/sudoers.d

	install -m 0755 ${S}/helm ${D}/${sbindir}/
	install -m 0755 ${STX_METADATA_PATH}/helm.sudo ${D}/${sysconfdir}/sudoers.d/helm
	install -m 0755 ${STX_METADATA_PATH}/helm-upload ${D}/${sbindir}/
	install -m 0755 ${STX_METADATA_PATH}/helmv2-cli.sh ${D}/${sbindir}/helmv2-cli
}

BBCLASSEXTEND = "native nativesdk"

FILES_${PN} = " \
	${sbindir}/helm \
	${sbindir}/helm-upload \
	${sbindir}/helmv2-cli \
	${sysconfdir}/sudoers.d \
	${sysconfdir}/sudoers.d/helm \
	"
