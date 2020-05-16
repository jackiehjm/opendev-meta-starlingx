#
## Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

S = "${S_DIR}/stx-ocf-scripts/src/ocf"

require ha-common.inc

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S_DIR}/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN} = " \
	bash \
	openstack-ras \
	"

do_install_append () {
	install -d -m 755 ${D}/usr/lib/ocf/resource.d/openstack
	install -p -D -m 755 $(find . -type f) ${D}/usr/lib/ocf/resource.d/openstack/ 
}

FILES_${PN}_append = " \
	${libdir}/ocf/resource.d/openstack/ \
	"