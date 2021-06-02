# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/${BP}/centos/files"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	"

SRC_URI_STX += " \
	file://0001-TIS-Patches.patch \
	file://0002-Disable-timeout-for-mkfs-command.patch \
	file://0003-drbd-parallel-to-serial-synchronization.patch \
	file://0004-US-96914-reuse-existing-drbd-cinder-resource.patch \
	file://0005-Add-PausedSync-states-to-acceptable-cstate.patch \
	file://0006-CGTS-7164-Add-resource-options-cpu-mask-to-affine-drbd-kernel-threads.patch \
	file://0007-Add-disk-by-path-test.patch \
	file://0008-CGTS-7953-support-for-new-drbd-resources.patch \
	file://0009-drbd-slow-before-swact.patch \
	file://0010-Format-DRBD-resource-cpu-mask-to-support-64-or-larger-cpus.patch \
	"

inherit openssl10
