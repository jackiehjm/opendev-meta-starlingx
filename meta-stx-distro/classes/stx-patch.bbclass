# This class is intended to apply the patches fetch from
# stx git repo defined in SRC_URI so to avoid maitaining
# a local copy in the recipe's metadata.
#
# Please set the FILESEXTRAPATHS to include the correct
# search path of the patches and add each patch in SRC_URI_STX
# e.g
#FILESEXTRAPATHS_prepend := "${WORKDIR}/stx-files:"
#SRC_URI_STX = "file://patches/0001-stx-patch.patch"

SRC_URI_STX ?= ""

do_patch_prepend() {
    bb.build.exec_func('add_stx_patch', d)
}

python add_stx_patch() {
    src_uri = d.getVar('SRC_URI', False)
    src_uri_stx = d.getVar('SRC_URI_STX', False)
    d.setVar('SRC_URI', src_uri_stx + " " + src_uri)
}
