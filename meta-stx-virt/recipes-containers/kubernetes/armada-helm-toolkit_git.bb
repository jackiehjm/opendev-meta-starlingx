SUMMARY = "Openstack-Helm-Infra helm-toolkit chart"
DESCRIPTION = "Openstack-Helm-Infra helm-toolkit chart"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "helm-native"

PROTOCOL = "https"
SRCREV = "c9d6676bf9a5aceb311dc31dadd07cba6a3d6392"

PV = "1.0.0+git${SRCPV}"

inherit allarch
inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "kubernetes/armada-helm-toolkit/files"

SRC_URI_STX = " \
	file://0001-Allow-multiple-containers-per-daemonset-pod.patch \
	file://0002-Add-imagePullSecrets-in-service-account.patch \
	file://0004-Partial-revert-of-31e3469d28858d7b5eb6355e88b6f49fd6.patch \
	file://0006-Fix-pod-restarts-on-all-workers-when-worker-added-re.patch \
	"

SRC_URI = " \
	git://github.com/openstack/openstack-helm-infra;protocol=${PROTOCOL} \
	"

S = "${WORKDIR}/git"


helm_folder = "${nonarch_libdir}/helm"

do_configure[noexec] = "1"

do_compile () {
	# Host a server for the charts
	chartmuseum --debug --port=${CHARTMUSEUM_PORT} --context-path='/charts' --storage="local" --storage-local-rootdir="." &
	sleep 2
	helm repo add local http://localhost:${CHARTMUSEUM_PORT}/charts

	# Make the charts. These produce tgz files
	make helm-toolkit
	# Both armada-helm-toolkit and openstack-helm-infra provide the same
	# helm-toolkit tarball filename. Rename files with 'armada-' prefix
	# to prevent 'Transaction check error'.
	for filename in *.tgz; do mv -v "$filename" "armada-$filename"; done

	# terminate helm server (the last backgrounded task)
	kill $!
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${B}/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = "helm"
