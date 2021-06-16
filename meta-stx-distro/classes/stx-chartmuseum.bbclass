# This class is intended to avoid race condition by adding a lock
# for do_compile of each package that uses chartmuseum to build
# helm chart, and add a prefunc to check if the port is already
# in use by other program on the host.
#

DEPENDS += "chartmuseum-native"

CHARTMUSEUM_PORT = "8879"

python check_port_in_use() {
    import socket

    port_num = int(d.getVar("CHARTMUSEUM_PORT"))

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        res = s.connect_ex(('localhost', port_num))
    if res == 0:
        bb.fatal("The port %s is already in use, please ensure the port is not used by other programs" % (port_num))

}

# Check if the port for chartmuseum is in use
do_compile[prefuncs] += "check_port_in_use"

# Ensure we don't race against other chartmuseum instances
check_port_in_use[lockfiles] = "${TMPDIR}/stx-chartmuseum.lock"
do_compile[lockfiles] = "${TMPDIR}/stx-chartmuseum.lock"
