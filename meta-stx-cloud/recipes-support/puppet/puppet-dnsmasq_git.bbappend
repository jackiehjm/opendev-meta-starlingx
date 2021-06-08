# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/${BPN}/centos/files"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	file://${BPN}/0005-puppet-dnsmasq-updates-for-poky-stx.patch;striplevel=5 \
	"

SRC_URI_STX += " \
	file://0001-puppet-dnsmasq-Kilo-quilt-patches.patch;striplevel=5 \
	file://0002-Fixing-mismatched-permission-on-dnsmasq-conf.patch;striplevel=5 \
	file://0003-Support-management-of-tftp_max-option.patch;striplevel=5 \
	file://0004-Enable-clear-DNS-cache-on-reload.patch;striplevel=5 \
	"

inherit openssl10
