# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/openstack/${BP}/centos/patches"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	"

SRC_URI_STX += " \
	file://0001-Remove-log_dir-from-conf-files.patch \
	file://0002-add-psycopg2-drivername-to-postgresql-settings.patch \
	"

do_install_append () {
	# fix the name of python-memcached
	sed -i -e 's/python-memcache\b/python-memcached/' ${D}/${datadir}/puppet/modules/oslo/manifests/params.pp
}

RDEPENDS_${PN} += "python-memcached"

inherit openssl10
