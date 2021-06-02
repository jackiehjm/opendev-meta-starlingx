# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/openstack/${BP}/centos"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	file://${BPN}/puppet-keystone-specify-full-path-to-openrc.patch \
	file://${BPN}/puppet-keystone-params.pp-fix-the-service-name.patch \
	"

SRC_URI_STX += " \
	file://patches/0001-pike-rebase-squash-titanium-patches.patch \
	file://patches/0002-remove-the-Keystone-admin-app.patch \
	file://patches/0003-remove-eventlet_bindhost-from-Keystoneconf.patch \
	file://patches/0004-escape-special-characters-in-bootstrap.patch \
	file://patches/0005-Add-support-for-fernet-receipts.patch \
	file://patches/0006-update-Barbican-admin-secret-s-user-project-IDs-duri.patch \
	file://patches/0007-update-for-openstackclient-Train-upgrade.patch \
	"

do_install_append () {
	# fix the name of python-memcached
	sed -i -e 's/python-memcache\b/python-memcached/' ${D}/${datadir}/puppet/modules/keystone/manifests/params.pp
}

RDEPENDS_${PN} += " \
	python-memcached \
	python-ldappool \
	"

inherit openssl10
