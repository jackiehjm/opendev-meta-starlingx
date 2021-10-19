
SUMMARY = "StarlingX stx packages"

PR = "r0"

#
# packages which content depend on MACHINE_FEATURES need to be MACHINE_ARCH
#

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = " \
	packagegroup-stx-armada-app \
	packagegroup-stx-armada-app-controller \
	packagegroup-stx-config \
	packagegroup-stx-config-controller \
	packagegroup-stx-config-files \
	packagegroup-stx-config-files-controller \
	packagegroup-stx-distributedcloud \
	packagegroup-stx-distributedcloud-controller \
	packagegroup-stx-fault \
	packagegroup-stx-fault-controller \
	packagegroup-stx-ha \
	packagegroup-stx-ha-controller \
	packagegroup-stx-integ \
	packagegroup-stx-integ-controller \
	packagegroup-stx-metal \
	packagegroup-stx-metal-controller \
	packagegroup-stx-monitoring \
	packagegroup-stx-monitoring-controller \
	packagegroup-stx-nfv-controller \
	packagegroup-stx-puppet \
	packagegroup-stx-update \
	packagegroup-stx-update-controller \
	packagegroup-stx-upstream \
	packagegroup-stx-upstream-controller \
	packagegroup-stx-utilities \
	packagegroup-stx-utilities-controller \
	\
	packagegroup-stx-controller \
	packagegroup-stx-storage \
	packagegroup-stx-worker \
	packagegroup-stx-worker-standalone \
	"

# packages for controller role
RDEPENDS_packagegroup-stx-controller = "\
	packagegroup-stx-armada-app-controller \
	packagegroup-stx-config-controller \
	packagegroup-stx-config-files-controller \
	packagegroup-stx-distributedcloud-controller \
	packagegroup-stx-fault-controller \
	packagegroup-stx-ha-controller \
	packagegroup-stx-integ-controller \
	packagegroup-stx-metal-controller \
	packagegroup-stx-monitoring-controller \
	packagegroup-stx-nfv-controller \
	packagegroup-stx-update-controller \
	packagegroup-stx-upstream-controller \
	packagegroup-stx-utilities-controller \
	\
	starlingx-dashboard \
	"

# packages for worker role
RDEPENDS_packagegroup-stx-worker = "\
	mtce-guestserver \
	platform-util-noncontroller \
	"

# packages for standalone worker, which can't be installed on AIO
RDEPENDS_packagegroup-stx-worker-standalone = "\
	workerconfig-standalone \
	"

# packages for storage role
RDEPENDS_packagegroup-stx-storage = "\
	drbd-utils \
	ldapscripts \
	platform-util-noncontroller \
	"

RDEPENDS_packagegroup-stx-puppet = "\
	stx-puppet \
	puppet-dcdbsync \
	puppet-dcmanager \
	puppet-dcorch \
	puppet-fm \
	puppet-mtce \
	puppet-nfv \
	puppet-patching \
	puppet-smapi \
	puppet-sshd \
	puppet-sysinv \
	puppet-manifests \
	"

RDEPENDS_packagegroup-stx-config = " \
	cert-mon \
	config-gate-worker \
	config-gate \
	playbookconfig \
	sysinv-agent \
	sysinv-fpga-agent \
	sysinv \
	tsconfig \
	"
RDEPENDS_packagegroup-stx-config-controller = " \
	controllerconfig \
	workerconfig-subfunction \
	cgts-client \
	"

RDEPENDS_packagegroup-stx-config-files  = " \
	dnsmasq \
	shadow \
	openldap \
	ntp \
	haproxy \
	syslog-ng \ 
	sudo \
	docker-ce \
	openvswitch \
	systemd \
	nfs-utils \
	nfs-utils-config \
	iptables \
	logrotate \
	mlx4-init \
	initscripts \
	procps \
	iscsi-initiator-utils \
	libpam-runtime \
	rabbitmq-server \
	rsync \
	base-files \
	audit \
	auditd \
	audit-python \
	"
RDEPENDS_packagegroup-stx-config-files-controller  = " \
	lighttpd \
	lighttpd-module-proxy \
	lighttpd-module-setenv \
	memcached \
	"

RDEPENDS_packagegroup-stx-fault = " \
        fm-api \
        fm-common \
        "
RDEPENDS_packagegroup-stx-fault-controller = " \
        fm-doc \
        fm-mgr \
        fm-rest-api \
        python-fmclient \
        "

RDEPENDS_packagegroup-stx-ha = " \
        sm-common-libs \
	sm-eru \
        stx-ocf-scripts \
        "

RDEPENDS_packagegroup-stx-ha-controller = " \
        sm \
        sm-db \
        sm-tools \
        libsm-common \
        sm-api \
        sm-client \
        "

RDEPENDS_packagegroup-stx-metal = " \
	mtce \
	mtce-pmon \
	mtce-hwmon \
	mtce-hostw \
	mtce-lmon \
	mtce-compute \
	mtce-storage \
	"
RDEPENDS_packagegroup-stx-metal-controller = " \
	mtce-control \
	platform-kickstarts \
	pxe-network-installer \
	"

RDEPENDS_packagegroup-stx-monitoring = " \
	collectd-extensions \
	monitor-tools \
	"
RDEPENDS_packagegroup-stx-monitoring-controller = " \
	influxdb-extensions \
	vm-topology \
	"

RDEPENDS_packagegroup-stx-distributedcloud = " \
	distributedcloud-dcdbsync \
	distributedcloud-ocf \
	"
RDEPENDS_packagegroup-stx-distributedcloud-controller = " \
	distributedcloud-client-dcmanager \
	distributedcloud-dcmanager \
	distributedcloud-dcorch \
	"

RDEPENDS_packagegroup-stx-nfv-controller = " \
	nfv-common \
	nfv-plugins \
	nfv-tools \
	nfv-vim \
	nfv-client \
	mtce-guestagent \
	mtce-guestserver \
	"

RDEPENDS_packagegroup-stx-upstream = " \
	python-neutronclient \
	python-aodhclient \
	python-barbican \
	python-barbicanclient \
	python-cinderclient \
	python-glanceclient \
	python-gnocchiclient \
	python-heatclient \
	python-keystoneauth1 \
	python-keystoneclient \
	python-novaclient \
	python-openstackclient \
	python-openstacksdk \
	"
RDEPENDS_packagegroup-stx-upstream-controller = " \
	barbican \
	python-django-horizon \
	python-ironicclient \
	python-pankoclient \
	openstack-ras \
	"

RDEPENDS_packagegroup-stx-update = " \
	cgcs-patch \
	cgcs-patch-agent \
	enable-dev-patch \
	"
RDEPENDS_packagegroup-stx-update-controller = " \
	cgcs-patch-controller \
	patch-alarm \
	"

RDEPENDS_packagegroup-stx-integ = " \
	dpkg \
	dtc \
	python-redfishtool \
	puppet-boolean \
	puppetlabs-create-resources \
	puppet-dnsmasq \
	puppet-drbd \
	puppet-filemapper \
	puppet-ldap \
	puppetlabs-lvm \
	puppet-network \
	puppet-nslcd \
	puppetlabs-postgresql \
	puppet-puppi \
	mariadb \
	docker-distribution \
	docker-forward-journald \
	armada \
	etcd \
	kexec-tools \
	kubernetes \
	python-cherrypy \
	python-setuptools \
	spectre-meltdown-checker \
	kvm-timer-advance-setup \
	ceph \
	lldpd \
        lvm2 \
        tzdata \
	registry-token-server \
	"
RDEPENDS_packagegroup-stx-integ-controller = " \
	drbd-utils \
	ldapscripts \
	python-3parclient \
	python-lefthandclient \
	python-ryu \
	"

RDEPENDS_packagegroup-stx-utilities = " \
	build-info \
	stx-ssl \
	collector \
	collect-engtools \
	logmgmt \
	namespace-utils \
	nfscheck \
	stx-extensions \
	worker-utils \
	update-motd \
	pci-irq-affinity \
	"
RDEPENDS_packagegroup-stx-utilities-controller = " \
	ceph-manager \
	python-cephclient \
	platform-util-controller \
	"

RDEPENDS_packagegroup-stx-armada-app = "\
	monitor-helm \
	monitor-helm-elastic \
	openstack-helm \
	openstack-helm-infra \
	stx-monitor-helm \
	stx-openstack-helm \
	stx-platform-helm \
	"
RDEPENDS_packagegroup-stx-armada-app-controller = "\
	stx-cert-manager-helm \
	stx-nginx-ingress-controller-helm \
	"
