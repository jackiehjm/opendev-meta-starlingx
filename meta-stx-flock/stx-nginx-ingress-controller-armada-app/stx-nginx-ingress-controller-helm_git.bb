SUMMARY = "StarlingX Nginx Ingress Controller Application Armada Helm Charts"
DESCRIPTION = "StarlingX Nginx Ingress Controller Application Armada Helm Charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.1"
PR = "15"
PRAUTO = "tis"

DEPENDS += " \
    helm-native \
    python-k8sapp-nginx-ingress-controller \
    python-k8sapp-nginx-ingress-controller-wheels \
"

inherit allarch
inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "nginx-ingress-controller-armada-app"
STX_SUBPATH = "${BPN}/${BPN}"

SRC_URI = "\
	https://github.com/kubernetes/ingress-nginx/archive/controller-v${nginx_version}.tar.gz \
	"

SRC_URI[md5sum] = "de3a31c8622c3de6f910617698d14b62"
SRC_URI[sha256sum] = "a8cc9dad5a512ef19d6f5fc1df38c254fc4417781f2152c05175113b8efbb7a5"

S = "${WORKDIR}/ingress-nginx-controller-v${nginx_version}/"

helm_repo = "stx-platform"
helm_folder = "${RECIPE_SYSROOT}${nonarch_libdir}/helm"
armada_folder = "${nonarch_libdir}/helm"

app_name = "nginx-ingress-controller"
app_staging = "${B}/staging"
app_tarball = "${app_name}-${PV}-${PR}.tgz"
app_folder = "/usr/local/share/applications/helm"

nginx_version = "0.41.2"
toolkit_version = "0.1.0"

do_configure[noexec] = "1"

do_compile () {
	# Host a server for the charts
	chartmuseum --debug --port=${CHARTMUSEUM_PORT} --context-path='/charts' --storage="local" --storage-local-rootdir="." &
	sleep 2
	helm repo add local http://localhost:${CHARTMUSEUM_PORT}/charts

	# Make the charts. These produce a tgz file
	cp ${STX_METADATA_PATH}/files/Makefile ${S}/charts
	cd ${S}/charts
	make ingress-nginx
	cd -

	# terminate helm server (the last backgrounded task)
	kill $!

	# Create a chart tarball compliant with sysinv kube-app.py
	# Setup staging
	mkdir -p ${app_staging}
	cp ${STX_METADATA_PATH}/files/metadata.yaml ${app_staging}
	cp ${STX_METADATA_PATH}/manifests/nginx_ingress_controller_manifest.yaml ${app_staging}

	mkdir -p ${app_staging}/charts
	cp ${S}/charts/*.tgz ${app_staging}/charts
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
