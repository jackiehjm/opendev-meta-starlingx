# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/puppet-rabbitmq-5.5.0/centos/patches"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	file://${BPN}/0007-init.pp-do-not-check-the-apt-resource.patch \
	file://${BPN}/0008-puppet-rabbitmq-poky.patch \
	file://${BPN}/0009-remove-apt-requirement.patch \
	"

SRC_URI_STX += " \
	file://0001-Roll-up-TIS-patches.patch \
	file://0002-Changed-cipher-specification-to-openssl-format.patch \
	file://0004-Partially-revert-upstream-commit-f7c3a4a637d59f3065d.patch \
	file://0005-Remove-the-rabbitmq_nodename-fact.patch \
	"

inherit openssl10

DEPENDS_append = " puppet-staging"
