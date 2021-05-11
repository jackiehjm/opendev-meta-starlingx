
FILESEXTRAPATHS_prepend := "${THISDIR}/patches:${THISDIR}/files:"
DESCRIPTION = " stx-ansible-playbooks"

STABLE = "starlingx/master"
PROTOCOL = "https"
BRANCH = "r/stx.5.0"
SRCREV = "490874f7bbd60f0117aa08d5a5fd582670d801b6"
S = "${WORKDIR}/git"
PV = "1.0.0+git${SRCPV}"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = " \
	git://opendev.org/starlingx/ansible-playbooks.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://0002-update_sysinv_database-do-not-fail-if-ceph-monitor-a.patch \
	file://0003-update_sysinv_database-wait-after-provision.patch \
	file://0004-bringup_flock_services-use-systmd-for-fminit-and-add.patch \
	file://0005-persist-config-add-retry-for-etcd.patch \
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

do_install_append() {
	sed -i -e 's|/usr/local/bin|${bindir}|' \
	    ${D}${datadir}/ansible/stx-ansible/playbooks/enable_secured_etcd.yml \
	    ${D}${datadir}/ansible/stx-ansible/playbooks/roles/recover-ceph-data/tasks/main.yml \
	    ${D}${datadir}/ansible/stx-ansible/playbooks/roles/bootstrap/apply-manifest/tasks/apply_bootstrap_manifest.yml \
	    ${D}${datadir}/ansible/stx-ansible/playbooks/roles/bootstrap/apply-manifest/tasks/apply_etcd_manifest.yml \
	    ${D}${datadir}/ansible/stx-ansible/playbooks/roles/provision-edgeworker/prepare-edgeworker/kubernetes/tasks/install-ubuntu-packages.yml
}
pkg_postinst_ontarget_${PN}() { 
	cp $D${sysconfdir}/ansible/ansible.cfg $D${sysconfdir}/ansible/ansible.cfg.orig
	cp $D${sysconfdir}/ansible/hosts $D${sysconfdir}/ansible/hosts.orig
	cp $D${datadir}/ansible/stx-ansible/playbooks/ansible.cfg $D${sysconfdir}/ansible
	cp $D${datadir}/ansible/stx-ansible/playbooks/hosts $D${sysconfdir}/ansible

	sed -i -e 's/#timeout = .*/timeout = ${ANSIBLE_SSH_TIMEOUT}/' \
	       -e 's/#retries = .*/retries = ${ANSIBLE_SSH_RETRY}/' \
	       $D${sysconfdir}/ansible/ansible.cfg
}

FILES_${PN}_append = " \
	${datadir} \
	"
