
K8S_BASE_VER = "1.18"
K8S_VER = "${K8S_BASE_VER}.1"

PV = "${K8S_VER}+git${SRCREV_kubernetes}"
SRCREV_kubernetes = "7879fc12a63337efff607952a323df90cdc7a335"

SRC_SUBDIR = "src/${GO_IMPORT}"

LICENSE += "(Apache-2.0&MIT)&(Apache-2.0|CC-BY-4.0)"
LIC_FILES_CHKSUM_append = " \
	file://${SRC_SUBDIR}/logo/LICENSE;md5=b431638b9986506145774a9da0d0ad85 \
	file://${SRC_SUBDIR}/vendor/github.com/morikuni/aec/LICENSE;md5=86852eb2df591157c788f3ba889c8aec \
	file://${SRC_SUBDIR}/staging/src/k8s.io/sample-controller/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://${SRC_SUBDIR}/test/images/kitten/Dockerfile;beginline=1;endline=13;md5=78cb21f802c15df77b75bd56f9417ccf \
	file://${SRC_SUBDIR}/test/images/nautilus/Dockerfile;beginline=1;endline=13;md5=78cb21f802c15df77b75bd56f9417ccf \
	file://${SRC_SUBDIR}/staging/src/k8s.io/kubectl/LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
	file://${SRC_SUBDIR}/vendor/github.com/grpc-ecosystem/go-grpc-middleware/LICENSE;md5=7ab5c73bb7e4679b16dd7c11b3559acf \
	"
inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "kubernetes/kubernetes/centos/files"

SRC_URI_STX = "\
	file://0001-Fix-pagesize-check-to-allow-for-options-already-endi.patch;patchdir=${SRC_SUBDIR} \
	file://fix_http2_erringroundtripper_handling.patch;patchdir=${SRC_SUBDIR} \
	file://kubelet-cpumanager-disable-CFS-quota-throttling-for-.patch;patchdir=${SRC_SUBDIR} \
	file://kubelet-cpumanager-keep-normal-containers-off-reserv.patch;patchdir=${SRC_SUBDIR} \
	file://kubelet-cpumanager-infrastructure-pods-use-system-re.patch;patchdir=${SRC_SUBDIR} \
	file://kubelet-cpumanager-introduce-concept-of-isolated-CPU.patch;patchdir=${SRC_SUBDIR} \
	file://Fix-exclusive-CPU-allocations-being-deleted-at-conta.patch;patchdir=${SRC_SUBDIR} \
	file://kubeadm-create-platform-pods-with-zero-CPU-resources.patch;patchdir=${SRC_SUBDIR} \
	file://add-option-to-disable-isolcpu-awareness.patch;patchdir=${SRC_SUBDIR} \
	\
	file://kubelet-service-remove-docker-dependency.patch;patchdir=${CONTRIB_DIR};striplevel=2 \
	"

CONTRIB_URI = "https://github.com/kubernetes-retired/contrib/tarball/89f6948e24578fed2a90a87871b2263729f90ac3"
CONTRIB_DIR = "${WORKDIR}/kubernetes-retired-contrib-89f6948"

SRC_URI = "\
	git://github.com/kubernetes/kubernetes.git;branch=release-${K8S_BASE_VER};name=kubernetes \
	${CONTRIB_URI};downloadfilename=kubernetes-contrib-v${K8S_VER}.tar.gz;name=contrib \
	file://0001-hack-lib-golang.sh-use-CC-from-environment.patch \
	file://0001-cross-don-t-build-tests-by-default.patch \
	"

SRC_URI[contrib.md5sum] = "9aa15af65ed89a7167b9520573bbdcd7"
SRC_URI[contrib.sha256sum] = "97206b6c1ea8dc7cb6201f909c2d14fc68cf40faa4e2641cdefb8411e9403274"

INSANE_SKIP_${PN} += "textrel"
INSANE_SKIP_${PN}-misc += "textrel"
INSANE_SKIP_kubelet += "textrel"

do_install () {
	install -d ${D}${bindir}
	install -d ${D}${systemd_system_unitdir}/

	# Install binaries
	install -m 754 -D ${S}/${SRC_SUBDIR}/_output/local/bin/${TARGET_GOOS}/${TARGET_GOARCH}/* ${D}/${bindir}

	# kubeadm:
	install -d -m 0755 ${D}/${sysconfdir}/systemd/system/kubelet.service.d
	install -m 0644 ${STX_METADATA_PATH}/kubeadm.conf ${D}/${sysconfdir}/systemd/system/kubelet.service.d

	# kubelete-cgroup-setup.sh
	install -m 0700 ${STX_METADATA_PATH}/kubelet-cgroup-setup.sh ${D}/${bindir}

	# install the bash completion
	install -d -m 0755 ${D}${datadir}/bash-completion/completions/
	${D}${bindir}/kubectl completion bash > ${D}${datadir}/bash-completion/completions/kubectl

	# install config files
	install -d -m 0755 ${D}${sysconfdir}/${BPN}
	install -m 644 -t ${D}${sysconfdir}/${BPN} ${CONTRIB_DIR}/init/systemd/environ/*

	# install service files
	install -d -m 0755 ${D}${systemd_system_unitdir}
	install -m 0644 -t ${D}${systemd_system_unitdir} ${CONTRIB_DIR}/init/systemd/*.service

	# install the place the kubelet defaults to put volumes
	install -d ${D}${localstatedir}/lib/kubelet

	# install systemd tmpfiles
	install -d -m 0755 ${D}${sysconfdir}/tmpfiles.d
	install -p -m 0644 -t ${D}${sysconfdir}/tmpfiles.d ${CONTRIB_DIR}/init/systemd/tmpfiles.d/kubernetes.conf

	# enable CPU and Memory accounting
	install -d -m 0755 ${D}/${sysconfdir}/systemd/system.conf.d
	install -m 0644 ${STX_METADATA_PATH}/kubernetes-accounting.conf ${D}/${sysconfdir}//systemd/system.conf.d/

	# install specific cluster addons for optional use
	install -d -m 0755 ${D}${sysconfdir}/${BPN}/addons

	# Addon: volumesnapshots
	install -d -m 0755 ${D}${sysconfdir}/${BPN}/addons/volumesnapshots
	install -d -m 0755 ${D}${sysconfdir}/${BPN}/addons/volumesnapshots/crd
	install -m 0644 -t ${D}${sysconfdir}/${BPN}/addons/volumesnapshots/crd \
		${S}/${SRC_SUBDIR}/cluster/addons/volumesnapshots/crd/*
	install -d -m 0755 ${D}${sysconfdir}/${BPN}/addons/volumesnapshots/volume-snapshot-controller
	install -m 0644 -t ${D}${sysconfdir}/${BPN}/addons/volumesnapshots/volume-snapshot-controller \
		${S}/${SRC_SUBDIR}/cluster/addons/volumesnapshots/volume-snapshot-controller/*
}

SYSTEMD_PACKAGES += "${PN} kube-proxy"
SYSTEMD_SERVICE_kube-proxy = "kube-proxy.service"
SYSTEMD_SERVICE_${PN} = "\
	kube-scheduler.service \
	kube-apiserver.service \
	kube-controller-manager.service \
	"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
SYSTEMD_AUTO_ENABLE_kubelet = "disable"
SYSTEMD_AUTO_ENABLE_kube-proxy = "disable"

FILES_${PN} += "\
	${bindir}/kube-scheduler \
	${bindir}/kube-apiserver \
	${bindir}/kube-controller-manager \
	${bindir}/hyperkube \
	${bindir}/kubelet-cgroup-setup.sh \
	"

FILES_kubectl += "\
	${datadir}/bash-completion/completions/kubectl \
	"

FILES_${PN}-misc = "\
	${bindir}/conversion-gen \
	${bindir}/openapi-gen \
	${bindir}/apiextensions-apiserver \
	${bindir}/defaulter-gen \
	${bindir}/mounter \
	${bindir}/deepcopy-gen \
	${bindir}/go-bindata \
	${bindir}/go2make \
	"

RDEPENDS_${PN} += "\
	bash \
	conntrack-tools \
	kube-proxy \
	"
