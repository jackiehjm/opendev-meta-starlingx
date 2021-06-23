SUMMARY = "Openstack Helm charts"
DESCRIPTION = "Openstack Helm charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    helm-native \
    openstack-helm-infra \
"

PROTOCOL = "https"
SRCREV = "34a7533b6484a157c8725889d0d68e792e13fc8d"

PV = "1.0.0+git${SRCPV}"

inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "openstack-armada-app"
STX_SUBPATH = "openstack-helm/files"

SRC_URI_STX = " \
	file://0001-Ceilometer-chart-add-the-ability-to-publish-events-t.patch \
	file://0002-Remove-stale-Apache2-service-pids-when-a-POD-starts.patch \
	file://0003-Nova-console-ip-address-search-optionality.patch \
	file://0004-Nova-chart-Support-ephemeral-pool-creation.patch \
	file://0005-Nova-Add-support-for-disabling-Readiness-Liveness-pr.patch \
	file://0006-Support-ingress-creation-for-keystone-admin-endpoint.patch \
	file://0007-Allow-more-generic-overrides-for-placeme.patch \
	file://0008-Allow-set-public-endpoint-url-for-keystone-endpoints.patch \
	file://0009-Wrong-usage-of-rbd_store_chunk_size.patch \
	file://0010-Add-stx_admin-account.patch \
	"

SRC_URI = " \
	git://github.com/openstack/openstack-helm;protocol=${PROTOCOL} \
	"

PATCHTOOL = "git"
PATCH_COMMIT_FUNCTIONS = "1"

S = "${WORKDIR}/git"

inherit allarch

helm_folder = "${nonarch_libdir}/helm"
toolkit_version = "0.1.0"
helmchart_version = "0.1.0"

do_configure[noexec] = "1"

do_compile () {
	# Stage helm-toolkit in the local repo
	cp ${RECIPE_SYSROOT}${helm_folder}/helm-toolkit-${toolkit_version}.tgz .

	# Host a server for the charts
	chartmuseum --debug --port=${CHARTMUSEUM_PORT} --context-path='/charts' --storage="local" --storage-local-rootdir="." &
	sleep 2
	helm repo add local http://localhost:${CHARTMUSEUM_PORT}/charts

	# Make the charts. These produce a tgz file
	make aodh
	make barbican
	make ceilometer
	make cinder
	make glance
	make heat
	make horizon
	make ironic
	make keystone
	make magnum
	make neutron
	make nova
	make panko
	make placement

	# terminate helm server (the last backgrounded task)
	kill $!

	# Remove the helm-toolkit tarball
	rm helm-toolkit-${toolkit_version}.tgz
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${B}/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = " \
    helm \
    openstack-helm-infra \
"
