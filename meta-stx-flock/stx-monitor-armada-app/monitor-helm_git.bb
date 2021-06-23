SUMMARY = "Monitor Helm charts"
DESCRIPTION = "Monitor Helm charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    helm-native \
"

PROTOCOL = "https"
SRCREV = "92b6289ae93816717a8453cfe62bad51cbdb8ad0"

PV = "1.0.0+git${SRCPV}"

inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "monitor-armada-app"
STX_SUBPATH = "monitor-helm/files"

SRC_URI_STX = " \
	file://0001-Add-Makefile-for-helm-charts.patch \
	file://0002-kibana-workaround-checksum-for-configmap.yaml.patch \
	file://0003-helm-chart-changes-for-stx-monitor.patch \
	file://0004-ipv6-helm-chart-changes.patch \
	file://0005-decouple-config.patch \
	file://0006-add-system-info.patch \
	file://0007-three-masters.patch \
	file://0008-Update-stx-monitor-for-kubernetes-API-1.16.patch \
	file://0009-add-curator-as-of-2019-10-10.patch \
	file://0010-Update-kube-state-metrics-1.8.0-to-commit-09daf19.patch \
	file://0011-update-init-container-env-to-include-node-name.patch \
	file://0012-Add-imagePullSecrets.patch \
	file://0013-removed-unused-images.patch \
	file://0014-Add-rbac-replicasets-to-apps-apigroup-commit-1717e2d.patch \
	file://0015-script-flexibility.patch \
	file://0016-use-main-container-image-for-initcontainer.patch \
	file://0017-stable-nginx-ingress-allow-nodePort-for-tcp-udp-serv.patch \
	file://0018-Update-nginx-ingress-chart-for-Helm-v3.patch \
	"


SRC_URI = " \
	git://github.com/helm/charts;protocol=${PROTOCOL};name=helm-charts \
	"

PATCHTOOL = "git"
PATCH_COMMIT_FUNCTIONS = "1"

S = "${WORKDIR}/git"

inherit allarch

helm_folder = "${nonarch_libdir}/helm"
helmchart_version = "0.1.0"

do_configure[noexec] = "1"

do_compile () {
	# Host a server for the charts
	chartmuseum --debug --port=${CHARTMUSEUM_PORT} --context-path='/charts' --storage="local" --storage-local-rootdir="." &
	sleep 2
	helm repo add local http://localhost:${CHARTMUSEUM_PORT}/charts

	# Create the tgz files
	cd stable
	make kube-state-metrics
	make nginx-ingress
	make elasticsearch-curator

	# terminate helm server (the last backgrounded task)
	kill $!
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${S}/stable/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = " \
    helm \
"
