SUMMARY = "Cert-Manager Helm charts"
DESCRIPTION = "StarlingX Cert-Manager Helm charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    helm-native \
"

PROTOCOL = "https"
SRCREV = "1d6ecc9cf8d841782acb5f3d3c28467c24c5fd18"

PV = "1.0"
PR = "9"
PRAUTO = "tis"

inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "cert-manager-armada-app"
STX_SUBPATH = "cert-manager-helm/files"

SRC_URI_STX = " \
	file://0001-Patch-for-acmesolver.patch \
	"

SRC_URI = " \
	git://github.com/jetstack/cert-manager;protocol=${PROTOCOL} \
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
	# Host a server for the charts
	chartmuseum --debug --port=${CHARTMUSEUM_PORT} --context-path='/charts' --storage="local" --storage-local-rootdir="." &
	sleep 2
	helm repo add local http://localhost:${CHARTMUSEUM_PORT}/charts

	# Copy CRD yaml files to templates
	cp deploy/crds/*.yaml deploy/charts/cert-manager/templates/

	# Create the tgz files
	cp ${STX_METADATA_PATH}/Makefile deploy/charts
	cd deploy/charts

	# In Cert-manager release-0.15, 'helm lint' fails
	# on templates/BUILD.bazel (with invalid file extension)
	# Removing the problem file
	rm -f cert-manager/templates/BUILD.bazel

	# Make the charts. These produce a tgz file
	make cert-manager
	cd -

	# terminate helm server (the last backgrounded task)
	kill $!
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${B}/deploy/charts/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = " \
    helm \
"
