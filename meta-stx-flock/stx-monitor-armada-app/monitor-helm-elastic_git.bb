SUMMARY = "Monitor Helm Elastic charts"
DESCRIPTION = "Monitor Helm Elastic charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    helm-native \
"

PROTOCOL = "https"
BRANCH = "main"
SRCREV = "945017287598479ba8653d9baf3ff26f7fe31e50"

PV = "1.0.0+git${SRCPV}"

inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "monitor-armada-app"
STX_SUBPATH = "${BPN}/files"

SRC_URI_STX = " \
	file://0001-add-makefile.patch \
	file://0002-use-oss-image.patch \
	file://0003-set-initial-masters-to-master-0.patch \
	file://0004-Update-Elastic-Apps-to-7.6.0-Releases.patch \
	file://0005-readiness-probe-enhancements.patch \
	file://0006-Metricbeat-nodeSelector-and-tolerations-config.patch \
	file://0007-Add-command-and-args-parameters-to-beats-and-logstash.patch \
	file://0008-Add-updateStrategy-parameter-to-beats-config.patch \
	file://0010-Fix-esConfig-checksum-annotation.patch \
	file://0011-Fix-Elasticsearch-readiness-probe-http-endpoint.patch \
	file://0012-Add-logstash-ingress.patch \
	"

SRC_URI = " \
	git://github.com/elastic/helm-charts;protocol=${PROTOCOL};branch=${BRANCH} \
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
	rm -f elasticsearch/Makefile
	rm kibana/Makefile
	rm filebeat/Makefile
	rm metricbeat/Makefile
	rm logstash/Makefile

	make elasticsearch
	make kibana
	make filebeat
	make metricbeat
	make logstash

	# terminate helm server (the last backgrounded task)
	kill $!
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${B}/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = " \
    helm \
"
