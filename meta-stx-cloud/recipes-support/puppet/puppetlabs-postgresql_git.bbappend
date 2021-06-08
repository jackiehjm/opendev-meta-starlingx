# For the stx files and patches repo
SRCREV_stx = "821de96615cb6f93fbc39f4baaa769029328d34d"
STXBRANCH = "r/stx.5.0"
STXSUBPATH = "config/puppet-modules/puppet-postgresql-${PV}/centos/files"
STXDSTSUFX = "stx-files"

inherit stx-patch

FILESEXTRAPATHS_prepend := "${WORKDIR}/${STXDSTSUFX}:"

SRC_URI += " \
	git://opendev.org/starlingx/integ.git;protocol=${PROTOCOL};branch=${STXBRANCH};destsuffix=${STXDSTSUFX};subpath=${STXSUBPATH};name=stx \
	file://${BPN}/0003-puppetlabs-postgresql-account-for-naming-diffs.patch \
	file://${BPN}/0004-poky-postgresql-updates.patch \
	file://${BPN}/0005-puppetlabs-postgresql-poky.patch \
	file://${BPN}/0006-adjust_path-remove-refs-to-local-bin.patch \
	file://${BPN}/postgresql.service \
	"

SRC_URI_STX += " \
	file://0001-Roll-up-TIS-patches.patch \
	file://0002-remove-puppetlabs-apt-as-a-requirement.patch \
	"

RDEPENDS_${PN}_append = " \
	postgresql \
	postgresql-contrib \
	postgresql-client \
	postgresql-timezone \
	postgresql-plperl \
	postgresql-plpython \
	"
#postgresql-dev
#postgresql-pltcl
#postgresql-setup


do_install_append() {
	install -d -m0755 ${D}/usr/lib/systemd/system
	install -m0644 ${WORKDIR}/${PN}/postgresql.service ${D}/usr/lib/systemd/system
}

FILES_${PN}_append = " /usr/lib/systemd/system/postgresql.service"

inherit openssl10
