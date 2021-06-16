SUMMARY = "An orchestrator for managing a collection of Kubernetes Helm charts"
DESCRIPTION = "An orchestrator for managing a collection of Kubernetes Helm charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
	helm-native \
	armada-helm-toolkit \
	"

PROTOCOL = "https"
SRCREV = "7ef4b8643b5ec5216a8f6726841e156c0aa54a1a"

PV = "0.2.0+git${SRCPV}"

inherit allarch
inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "kubernetes/armada/centos/files"

SRC_URI_STX = " \
	file://0001-Add-Helm-v2-client-initialization-using-tiller-postS.patch \
	"

SRC_URI = " \
	git://opendev.org/airship/armada.git;protocol=${PROTOCOL} \
	"

S = "${WORKDIR}/git"

helm_folder = "${nonarch_libdir}/helm"
toolkit_version = "0.1.0"
charts_staging = "./charts"

do_configure[noexec] = "1"

do_compile () {
	# Stage helm-toolkit in the local repo
	cp ${RECIPE_SYSROOT}${helm_folder}/armada-helm-toolkit-${toolkit_version}.tgz ${charts_staging}/helm-toolkit-${toolkit_version}.tgz

	# Host a server for the charts
	chartmuseum --debug --port=${CHARTMUSEUM_PORT} --context-path='/charts' --storage="local" --storage-local-rootdir="${charts_staging}" &
	sleep 2
	helm repo add local http://localhost:${CHARTMUSEUM_PORT}/charts

	cd ${charts_staging}
	helm dependency update armada
	helm lint armada
	rm -v -f ./requirements.lock ./requirements.yaml
	helm template --set pod.resources.enabled=true armada
	helm package armada
	cd -

	# terminate helm server (the last backgrounded task)
	kill $!

	# Remove the helm-toolkit tarball
	rm ${charts_staging}/helm-toolkit-${toolkit_version}.tgz
}

do_install () {
	install -d -m 755 ${D}/opt/extracharts
	install -p -D -m 755 ${B}/${charts_staging}/armada-*.tgz ${D}/opt/extracharts
}

FILES_${PN} = "/opt/extracharts"

RDEPENDS_${PN} = " \
    helm \
"
