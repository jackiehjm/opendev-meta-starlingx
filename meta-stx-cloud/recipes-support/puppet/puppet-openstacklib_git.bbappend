# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/openstack/${BP}/centos"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	file://${BPN}/puppet-openstacklib-updates-for-poky-stx.patch \
	"

SRC_URI_STX += " \
	file://patches/0001-Roll-up-TIS-patches.patch \
	file://patches/0002-update-for-openstackclient-Train.patch \
	"

inherit openssl10
