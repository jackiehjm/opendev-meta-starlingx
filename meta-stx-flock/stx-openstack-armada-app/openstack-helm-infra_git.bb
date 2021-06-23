SUMMARY = "Openstack-Helm-Infra charts"
DESCRIPTION = "Openstack-Helm-Infra charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "helm-native"

PROTOCOL = "https"
SRCREV = "34d54f2812b7d54431d548cff08fe8da7f838124"

PV = "1.0"
PR = "38"
PRAUTO = "tis"

inherit stx-chartmuseum
inherit stx-metadata

STX_REPO = "openstack-armada-app"
STX_SUBPATH = "${BPN}/files"

SRC_URI_STX = " \
	file://0001-Add-imagePullSecrets-in-service-account.patch \
	file://0002-Set-Min-NGINX-handles.patch \
	file://0003-Partial-revert-of-31e3469d28858d7b5eb6355e88b6f49fd6.patch \
	file://0004-Fix-pod-restarts-on-all-workers-when-worker-added-re.patch \
	file://0005-Add-io_thread_pool-for-rabbitmq.patch \
	file://0006-Enable-override-of-rabbitmq-probe-parameters.patch \
	file://0007-Fix-ipv6-address-issue-causing-mariadb-ingress-not-ready.patch \
	file://0008-Fix-rabbitmq-could-not-bind-port-to-ipv6-address-iss.patch \
	file://0009-Enable-override-of-mariadb-server-probe-parameters.patch \
	file://0010-Mariadb-use-utf8_general_ci-collation-as-default.patch \
	file://0011-Add-mariadb-database-config-override-to-support-ipv6.patch \
	file://0012-enable-Values.conf.database.config_override-for-mari.patch \
	file://0013-Allow-set-public-endpoint-url-for-all-openstack-types.patch \
	file://0014-Add-tolerations-to-rabbitmq-chart.patch \
	file://0015-Add-tolerations-to-mariadb-chart.patch \
	"

SRC_URI = " \
	git://github.com/openstack/${BPN};protocol=${PROTOCOL} \
	"

PATCHTOOL = "git"
PATCH_COMMIT_FUNCTIONS = "1"

S = "${WORKDIR}/git"

inherit allarch

helm_folder = "${nonarch_libdir}/helm"

do_configure[noexec] = "1"

do_compile () {
	# Host a server for the charts
	chartmuseum --debug --port=${CHARTMUSEUM_PORT} --context-path='/charts' --storage="local" --storage-local-rootdir="." &
	sleep 2
	helm repo add local http://localhost:${CHARTMUSEUM_PORT}/charts

	# Make the charts. These produce tgz files
	make helm-toolkit
	make gnocchi
	make ingress
	make libvirt
	make mariadb
	make memcached
	make openvswitch
	make rabbitmq
	make ceph-rgw

	# terminate helm server (the last backgrounded task)
	kill $!
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${B}/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = "helm"
