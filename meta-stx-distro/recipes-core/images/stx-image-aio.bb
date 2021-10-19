
SUMMARY = " StarlingX Single Server"

LICENSE = "MIT"

CORE_IMAGE_EXTRA_INSTALL = " \
	packagegroup-basic \
	packagegroup-core-base-utils  \
	packagegroup-core-full-cmdline \
	packagegroup-core-lsb \
	"

STX_AIO_PKGS = "\
	packagegroup-stx-armada-app \
	packagegroup-stx-config \
	packagegroup-stx-config-files \
	packagegroup-stx-distributedcloud \
	packagegroup-stx-fault \
	packagegroup-stx-ha \
	packagegroup-stx-integ \
	packagegroup-stx-metal \
	packagegroup-stx-monitoring \
	packagegroup-stx-puppet \
	packagegroup-stx-update \
	packagegroup-stx-upstream \
	packagegroup-stx-utilities \
	\
	packagegroup-stx-controller \
	packagegroup-stx-worker \
	packagegroup-stx-storage \
	"

IMAGE_INSTALL_append = " \
	${CORE_IMAGE_BASE_INSTALL} \
	${STX_AIO_PKGS} \
	rt-tests \
	kernel-dev \
	"

IMAGE_FEATURES += " \
	package-management \
	ssh-server-openssh \
	"

inherit stx-image-list
inherit stx-postrun
inherit extrausers-config
inherit core-image
inherit distro_features_check
inherit openstack-base
inherit identity
inherit monitor

# We need docker-ce
PACKAGE_EXCLUDE += " docker"
