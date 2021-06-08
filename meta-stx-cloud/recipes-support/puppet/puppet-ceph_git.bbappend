# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/openstack/puppet-ceph-2.2.0/centos/patches"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	file://${BPN}/0005-Remove-puppetlabs-apt-as-ceph-requirement.patch \
	file://${BPN}/0011-puppet-ceph-changes-for-poky-stx.patch \
	"

SRC_URI_STX += " \
	file://0001-Roll-up-TIS-patches.patch \
	file://0002-Newton-rebase-fixes.patch \
	file://0003-Ceph-Jewel-rebase.patch \
	file://0004-US92424-Add-OSD-support-for-persistent-naming.patch \
	file://0006-ceph-disk-prepare-invalid-data-disk-value.patch \
	file://0007-Add-StarlingX-specific-restart-command-for-Ceph-moni.patch \
	file://0008-ceph-mimic-prepare-activate-osd.patch \
	file://0009-fix-ceph-osd-disk-partition-for-nvme-disks.patch \
	file://0010-wipe-unprepared-disks.patch \
	"

inherit openssl10
