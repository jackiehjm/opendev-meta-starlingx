# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/puppet-haproxy-${PV}/centos/patches"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	"

SRC_URI_STX += " \
	file://0001-Roll-up-TIS-patches.patch \
	file://0002-disable-config-validation-prechecks.patch \
	file://0003-Fix-global_options-log-default-value.patch \
	file://0004-Stop-invalid-warning-message \
	"

inherit openssl10
