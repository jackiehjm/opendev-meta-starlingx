SUMMARY = "Puppet module for OpenStack Keystone"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=12a15a9ebddda7d856c783f745e5ee47"

PV = "11.3.0"
SRC_REV = "305c91cac00f720ad6461b442e71b52b12f9ae57"
PROTOCOL = "https"
BRANCH = "stable/pike"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/puppet-keystone.git;protocol=${PROTOCOL};rev=${SRC_REV};branch=${BRANCH} \
	file://puppet-keystone/Add-gemspec.patch \
	file://puppet-keystone/0001-pike-rebase-squash-titanium-patches.patch \
	file://puppet-keystone/0002-remove-the-Keystone-admin-app.patch \
	file://puppet-keystone/0003-remove-eventlet_bindhost-from-Keystoneconf.patch \
	file://puppet-keystone/0004-escape-special-characters-in-bootstrap.patch \
	file://puppet-keystone/0005-Add-support-for-fernet-receipts.patch \
	"

inherit ruby

DEPENDS += " \
	ruby \
	facter \
	"

RDEPENDS_${PN} += " \
	ruby \
	facter \
	puppet \
	"

RUBY_INSTALL_GEMS = "puppet-keystone-${PV}.gem"

do_install_append() {
	: 
}
