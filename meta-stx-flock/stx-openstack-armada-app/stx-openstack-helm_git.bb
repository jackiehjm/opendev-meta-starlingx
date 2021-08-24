SUMMARY = "StarlingX Openstack Application Helm charts"
DESCRIPTION = "StarlingX Openstack Application Helm charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0"
PR = "83"
PRAUTO = "tis"

DEPENDS += " \
    helm-native \
    openstack-helm-infra \
"

inherit allarch
inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "openstack-armada-app"
STX_SUBPATH = "${BPN}/${BPN}"

S = "${WORKDIR}/${BPN}"

helm_folder = "${nonarch_libdir}/helm"
armada_folder = "${nonarch_libdir}/armada"
app_folder = "${nonarch_libdir}/application"
toolkit_version = "0.1.0"
helmchart_version = "0.1.0"

do_configure[noexec] = "1"

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cp -rf ${STX_METADATA_PATH} ${WORKDIR}
}

do_compile () {
	# Stage helm-toolkit in the local repo
	cp ${RECIPE_SYSROOT}${helm_folder}/helm-toolkit-${toolkit_version}.tgz ${S}/helm-charts/

	# Host a server for the charts
	chartmuseum --debug --port=${CHARTMUSEUM_PORT} --context-path='/charts' --storage="local" --storage-local-rootdir="./helm-charts" &
	sleep 2
	helm repo add local http://localhost:${CHARTMUSEUM_PORT}/charts

	# Make the charts. These produce a tgz file
	cd ${S}/helm-charts
	make nova-api-proxy
	make garbd
	make keystone-api-proxy
	make fm-rest-api
	make nginx-ports-control
	make dcdbsync
	make psp-rolebinding
	cd -

	# terminate helm server (the last backgrounded task)
	kill $!

	# Remove the helm-toolkit tarball
	rm ${S}/helm-charts/helm-toolkit-${toolkit_version}.tgz
}

do_install () {
	install -d -m 755 ${D}${app_folder}
	install -p -D -m 755 ${S}/files/metadata.yaml ${D}${app_folder}
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${S}/helm-charts/*.tgz ${D}${helm_folder}
	install -d -m 755 ${D}${armada_folder}
	install -p -D -m 755 ${S}/manifests/*.yaml ${D}${armada_folder}
}

FILES_${PN} = " \
    ${app_folder} \
    ${helm_folder} \
    ${armada_folder} \
"

RDEPENDS_${PN} = " \
    helm \
    openstack-helm \
    openstack-helm-infra \
    python-k8sapp-openstack \
    python-k8sapp-openstack-wheels \
"
