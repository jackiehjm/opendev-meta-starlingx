SUMMARY = "StarlingX Cert-Manager Armada Helm Charts"
DESCRIPTION = "StarlingX Cert-Manager Armada Helm Charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0"
PR = "15"
PRAUTO = "tis"

DEPENDS += " \
    helm-native \
    cert-manager-helm \
    python-k8sapp-cert-manager \
    python-k8sapp-cert-manager-wheels \
"

inherit allarch
inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "cert-manager-armada-app"
STX_SUBPATH = "${BPN}/${BPN}"

STX_EXTRA_REPO = "helm-charts"
STX_EXTRA_SUBPATH = "psp-rolebinding/psp-rolebinding/helm-charts"
STX_METADATA_EXTRA_PATH = "${TMPDIR}/work-shared/stx-${STX_EXTRA_REPO}-source/git/${STX_EXTRA_SUBPATH}"
FILESEXTRAPATHS_prepend = "${STX_METADATA_EXTRA_PATH}:"
do_unpack[depends] += "stx-${STX_EXTRA_REPO}-source:do_patch"

S = "${WORKDIR}/${BPN}"

helm_repo = "stx-platform"
helm_folder = "${RECIPE_SYSROOT}${nonarch_libdir}/helm"

app_name = "cert-manager"
app_staging = "${B}/staging"
app_tarball = "${app_name}-${PV}-${PR}.tgz"
app_folder = "/usr/local/share/applications/helm"

do_configure[noexec] = "1"

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cp -rf ${STX_METADATA_PATH} ${WORKDIR}
	cp -rf ${STX_METADATA_EXTRA_PATH} ${S} 
}

do_compile () {
	# Host a server for the charts
	chartmuseum --debug --port=${CHARTMUSEUM_PORT} --context-path='/charts' --storage="local" --storage-local-rootdir="." &
	sleep 2
	helm repo add local http://localhost:${CHARTMUSEUM_PORT}/charts

	# Make the charts. These produce a tgz file
	cd ${S}/helm-charts
	make psp-rolebinding
	cd -

	# terminate helm server (the last backgrounded task)
	kill $!

	# Create a chart tarball compliant with sysinv kube-app.py
	# Setup staging
	mkdir -p ${app_staging}
	cp ${S}/files/metadata.yaml ${app_staging}
	cp ${S}/manifests/*.yaml ${app_staging}

	mkdir -p ${app_staging}/charts
	cp ${S}/helm-charts/*.tgz ${app_staging}/charts
	cp ${helm_folder}/cert*.tgz ${app_staging}/charts
	cd ${app_staging}

	# Populate metadata
	sed -i 's/@APP_NAME@/${app_name}/g' ${app_staging}/metadata.yaml
	sed -i 's/@APP_VERSION@/${PV}-${PR}/g' ${app_staging}/metadata.yaml
	sed -i 's/@HELM_REPO@/${helm_repo}/g' ${app_staging}/metadata.yaml

	# Copy the plugins: installed in the buildroot
	mkdir -p ${app_staging}/plugins
	cp ${RECIPE_SYSROOT}/plugins/*.whl ${app_staging}/plugins

	# package it up
	find . -type f ! -name '*.md5' -print0 | xargs -0 md5sum > checksum.md5
	tar -zcf ${B}/${app_tarball} -C ${app_staging}/ .

	# Cleanup staging
	rm -fr ${app_staging}
}

do_install () {
	install -d -m 755 ${D}/${app_folder}
	install -p -D -m 755 ${B}/${app_tarball} ${D}/${app_folder}
}

FILES_${PN} = " \
    ${app_folder} \
"

RDEPENDS_${PN} = " \
    helm \
"
