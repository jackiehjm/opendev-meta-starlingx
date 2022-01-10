
DESCRIPTION = " stx-ansible-playbooks"

STABLE = "starlingx/master"
PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCREV = "d1aa4e6064bfbf0537a954ad6458518de5226602"
S = "${WORKDIR}/git"
PV = "1.0.0+git${SRCPV}"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = " \
	git://opendev.org/starlingx/ansible-playbooks.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://0001-one_time_config_tasks-add-grubby-args-for-efi-and-bi.patch \
	file://0002-update_sysinv_database-do-not-fail-if-ceph-monitor-a.patch \
	file://0003-update_sysinv_database-wait-after-provision.patch \
	file://0004-bringup_flock_services-use-systmd-for-fminit-and-add.patch \
	file://0005-persist-config-add-retry-for-etcd.patch \
	file://0006-Ensure-n3000-opae-image-cache-is-not-deleted.patch \
	file://0007-Fix-restore-user-images-playbook.patch \
	file://0008-Checking-images-on-registry.local-before-download.patch \
	file://0009-download_images-add-support-to-load-image-offline-fi.patch \
        "

RDEPENDS_playbookconfig = " \
	nscd \
	python \
	python-netaddr \
	python-ptyprocess \
	python-pexpect \
	python-ansible \
	sysinv \
	grub \
	grubby \
	dracut \
	openssl-bin \
	ipset \
	"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	cd ${S}/playbookconfig/src
	oe_runmake -e \
		DESTDIR=${D}/${datadir}/ansible/stx-ansible
}

ANSIBLE_SSH_TIMEOUT = "60"
ANSIBLE_SSH_RETRY = "3"

PLAYBOOKS_DIR = "ansible/stx-ansible/playbooks"

do_install_append() {
	sed -i -e 's|/usr/local/bin|${bindir}|' \
	    ${D}${datadir}/${PLAYBOOKS_DIR}/enable_secured_etcd.yml \
	    ${D}${datadir}/${PLAYBOOKS_DIR}/roles/recover-ceph-data/tasks/main.yml \
	    ${D}${datadir}/${PLAYBOOKS_DIR}/roles/bootstrap/apply-manifest/tasks/apply_bootstrap_manifest.yml \
	    ${D}${datadir}/${PLAYBOOKS_DIR}/roles/bootstrap/apply-manifest/tasks/apply_etcd_manifest.yml \
	    ${D}${datadir}/${PLAYBOOKS_DIR}/roles/provision-edgeworker/prepare-edgeworker/kubernetes/tasks/install-ubuntu-packages.yml

	sed -i -e 's|/usr/local/sbin/helm-upload |${sbindir}/helm-upload |' \
	       -e 's|${base_sbindir}/helm |${sbindir}/helm |' \
	       ${D}${datadir}/${PLAYBOOKS_DIR}/roles/bootstrap/bringup-essential-services/tasks/bringup_helm.yml \
	       ${D}${datadir}/${PLAYBOOKS_DIR}/roles/common/armada-helm/tasks/main.yml
}

pkg_postinst_ontarget_${PN}() { 
	cp $D${sysconfdir}/ansible/ansible.cfg $D${sysconfdir}/ansible/ansible.cfg.orig
	cp $D${sysconfdir}/ansible/hosts $D${sysconfdir}/ansible/hosts.orig
	cp $D${datadir}/${PLAYBOOKS_DIR}/ansible.cfg $D${sysconfdir}/ansible
	cp $D${datadir}/${PLAYBOOKS_DIR}/hosts $D${sysconfdir}/ansible

	sed -i -e 's/#timeout = .*/timeout = ${ANSIBLE_SSH_TIMEOUT}/' \
	       -e 's/#retries = .*/retries = ${ANSIBLE_SSH_RETRY}/' \
	       -e 's/pipelining =.*/pipelining = True/' \
	       $D${sysconfdir}/ansible/ansible.cfg
}

FILES_${PN}_append = " \
	${datadir} \
	"
