
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${PN}/stx-integ/0001-TIS-Patches.patch \
	file://${PN}/stx-integ/0002-Disable-timeout-for-mkfs-command.patch \
	file://${PN}/stx-integ/0003-drbd-parallel-to-serial-synchronization.patch \
	file://${PN}/stx-integ/0004-US-96914-reuse-existing-drbd-cinder-resource.patch \
	file://${PN}/stx-integ/0005-Add-PausedSync-states-to-acceptable-cstate.patch \
	file://${PN}/stx-integ/0006-CGTS-7164-Add-resource-options-cpu-mask-to-affine-drbd-kernel-threads.patch \
	file://${PN}/stx-integ/0007-Add-disk-by-path-test.patch \
	file://${PN}/stx-integ/0008-CGTS-7953-support-for-new-drbd-resources.patch \
	file://${PN}/stx-integ/0009-drbd-slow-before-swact.patch \
	file://${PN}/stx-integ/0010-Format-DRBD-resource-cpu-mask-to-support-64-or-larger-cpus.patch \
	"

inherit openssl10
