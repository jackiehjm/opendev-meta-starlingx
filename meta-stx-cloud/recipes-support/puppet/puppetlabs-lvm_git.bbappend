# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/puppet-lvm/centos/files"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	"

SRC_URI_STX += " \
	file://0001-puppet-lvm-kilo-quilt-changes.patch;striplevel=5 \
	file://0002-UEFI-pvcreate-fix.patch;striplevel=5 \
	file://0003-US94222-Persistent-Dev-Naming.patch;striplevel=5 \
	file://0004-extendind-nuke_fs_on_resize_failure-functionality.patch;striplevel=5 \
	file://Fix-the-logical-statement-for-nuke_fs_on_resize.patch;striplevel=5 \
	"
RDEPENDS_${PN} += " \
	lvm2 \
	lvm2-scripts \
	lvm2-udevrules \
	"

inherit openssl10
