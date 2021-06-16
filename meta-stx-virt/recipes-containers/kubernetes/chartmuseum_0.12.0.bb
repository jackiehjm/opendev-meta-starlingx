SUMMARY = "Helm Chart Repository with support for Amazon S3 and Google Cloud Storage"
HOMEPAGE = "https://chartmuseum.com"
SECTION = "devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=44b8dd2908a2e73452f35038fe537afc"

SRC_URI = " \
	https://github.com/helm/chartmuseum/archive/v${PV}.tar.gz;downloadfilename=${BP}.tar.gz;name=src \
	https://s3.amazonaws.com/chartmuseum/release/v0.12.0/bin/linux/amd64/chartmuseum;name=bin \
	"
SRC_URI[src.md5sum] = "5c4cadff25c20e3fc17f079a457bddd9"
SRC_URI[src.sha256sum] = "61d3e6142b934eadeee23f22e5ee84e86e3d582ad27813cec655aa4ba40d485b"
SRC_URI[bin.md5sum] = "fba48bf948c224e662e84e7b295899f2"
SRC_URI[bin.sha256sum] = "53402edf5ac9f736cb6da8f270f6bbf356dcbbe5592d8a09ee6f91a2dc30e4f6"

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
	install -m 0755 -d ${D}/${bindir}/
	install -m 0755 ${WORKDIR}/chartmuseum ${D}/${bindir}/chartmuseum
}

BBCLASSEXTEND = "native nativesdk"
