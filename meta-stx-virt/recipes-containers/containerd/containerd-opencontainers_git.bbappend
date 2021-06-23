FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "kubernetes/containerd/centos/files"

STX_EXTRA_REPO = "config-files"
STX_EXTRA_SUBPATH = "containerd-config/files"
STX_METADATA_EXTRA_PATH = "${TMPDIR}/work-shared/stx-${STX_EXTRA_REPO}-source/git/${STX_EXTRA_SUBPATH}"
FILESEXTRAPATHS_prepend = "${STX_METADATA_EXTRA_PATH}:"

do_patch[depends] += "stx-${STX_EXTRA_REPO}-source:do_patch"

SRCREV = "d76c121f76a5fc8a462dc64594aea72fe18e1178"
SRC_URI = "\
	git://github.com/containerd/containerd;branch=release/1.3 \
	file://0001-build-use-oe-provided-GO-and-flags.patch;patchdir=src/${GO_IMPORT} \
	file://containerd.service \
	"

SRC_URI_STX = "\
	file://0001-customize-containerd-for-StarlingX.patch;patchdir=src/${GO_IMPORT};striplevel=2 \
	file://0002-archive-skip-chmod-IsNotExist-error.patch;patchdir=src/${GO_IMPORT};striplevel=2 \
	"

CONTAINERD_VERSION = "v1.3.3"

VENDOR_SRCDIR = "${S}/src/${GO_IMPORT}/vendor/src"
CONTAINERD_DIR = "${VENDOR_SRCDIR}/github.com/containerd/containerd"

CONTAINERD_SUBDIR = "\
	api archive cio cmd containers content defaults diff errdefs events filters \
	gc identifiers images labels leases log metadata metrics mount namespaces \
	oci platforms plugin protobuf reference remotes rootfs runtime services \
	snapshots sys version \
	"
CONTAINERD_PKG_SUBDIR = "dialer oom process progress seed stdio testutil timeout ttrpcutil"
CONTAINERD_CONTRIB_SUBDIR = "apparmor aws nvidia seccomp snapshotservice"

do_compile() {
	export GOARCH="${TARGET_GOARCH}"

	# link fixups for compilation
	rm -f ${VENDOR_SRCDIR}
	ln -sf ./ ${VENDOR_SRCDIR}

	mkdir -p ${CONTAINERD_DIR}/
	mkdir -p ${CONTAINERD_DIR}/pkg/
	mkdir -p ${CONTAINERD_DIR}/contrib/

	# without this, the stress test parts of the build fail
	cp ${S}/src/${GO_IMPORT}/*.go ${CONTAINERD_DIR}

	for c in ${CONTAINERD_SUBDIR}; do
		if [ -d ${S}/src/${GO_IMPORT}/${c} ]; then
			ln -sfn ${S}/src/${GO_IMPORT}/${c} ${CONTAINERD_DIR}/${c}
		fi
	done
	for c in ${CONTAINERD_PKG_SUBDIR}; do
		if [ -d ${S}/src/${GO_IMPORT}/pkg/${c} ]; then
			ln -sfn ${S}/src/${GO_IMPORT}/pkg/${c} ${CONTAINERD_DIR}/pkg/${c}
		fi
	done
	for c in ${CONTAINERD_CONTRIB_SUBDIR}; do
		if [ -d ${S}/src/${GO_IMPORT}/contrib/${c} ]; then
			ln -sfn ${S}/src/${GO_IMPORT}/contrib/${c} ${CONTAINERD_DIR}/contrib/${c}
		fi
	done

	export GOPATH="${S}/src/${GO_IMPORT}/.gopath:${S}/src/${GO_IMPORT}/vendor:${STAGING_DIR_TARGET}/${prefix}/local/go"
	export GOROOT="${STAGING_DIR_NATIVE}/${nonarch_libdir}/${HOST_SYS}/go"

	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export CGO_ENABLED="1"
	export CGO_CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export CGO_LDFLAGS="${LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export BUILDTAGS="no_btrfs static_build netgo"
	export CFLAGS="${CFLAGS}"
	export LDFLAGS="${LDFLAGS}"

	cd ${S}/src/${GO_IMPORT}
	oe_runmake binaries
}

do_install_append() {
	install -p -m 755 ${S}/src/${GO_IMPORT}/bin/containerd-shim-runc-v1 ${D}/${bindir}
	install -p -m 755 ${S}/src/${GO_IMPORT}/bin/containerd-shim-runc-v2 ${D}/${bindir}
	install -p -m 755 ${S}/src/${GO_IMPORT}/bin/containerd-stress ${D}/${bindir}
	install -p -m 755 ${S}/src/${GO_IMPORT}/bin/ctr ${D}/${bindir}

	# from containerd-config.spec in stx config-files repo
	install -d -m 755 ${D}/${sysconfdir}/pmon.d/
	install -p -m 644 ${STX_METADATA_EXTRA_PATH}/containerd-pmon.conf ${D}/${sysconfdir}/pmon.d/containerd.conf
	install -d -m 755 ${D}/${sysconfdir}/systemd/system/containerd.service.d/
	install -p -m 644 ${STX_METADATA_EXTRA_PATH}/containerd-stx-override.conf ${D}/${sysconfdir}/systemd/system/containerd.service.d/
}

RDEPENDS_${PN} += "cri-tools"
