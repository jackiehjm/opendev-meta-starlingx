
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# The patches in stx-integ sub-dir is from https://opendev.org/starlingx/integ.git
SRC_URI += " \
	file://${PN}/stx-integ/0001-pike-rebase-squash-titanium-patches.patch \
	file://${PN}/stx-integ/0002-remove-the-Keystone-admin-app.patch \
	file://${PN}/stx-integ/0003-remove-eventlet_bindhost-from-Keystoneconf.patch \
	file://${PN}/stx-integ/0004-escape-special-characters-in-bootstrap.patch \
	file://${PN}/stx-integ/0005-Add-support-for-fernet-receipts.patch \
	file://${PN}/stx-integ/0006-update-Barbican-admin-secret-s-user-project-IDs-duri.patch \
	file://${PN}/stx-integ/0007-update-for-openstackclient-Train-upgrade.patch \
	\
	file://${PN}/puppet-keystone-specify-full-path-to-openrc.patch \
	file://${PN}/puppet-keystone-params.pp-fix-the-service-name.patch \
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
