
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/stx-integ/0001-Roll-up-TIS-patches.patch \
	file://${BPN}/stx-integ/0002-update-for-openstackclient-Train.patch \
	\
	file://${BPN}/puppet-openstacklib-updates-for-poky-stx.patch \
	"

inherit openssl10
