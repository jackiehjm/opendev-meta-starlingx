FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://ca-legacy \
    file://ca-legacy.8.txt \
    file://ca-legacy.conf \
    file://certdata2pem.py \
    file://certdata.txt \
    file://nssckbi.h \
    file://README.ca-certificates \
    file://README.etc \
    file://README.extr \
    file://README.java \
    file://README.openssl \
    file://README.pem \
    file://README.src \
    file://README.usr \
    file://trust-fixes \
    file://update-ca-trust \
    file://update-ca-trust.8.txt \
    "

DEPENDS += " \
    python-native \
    "

pkidir = "${sysconfdir}/pki"
catrustdir = "${pkidir}/ca-trust"
p11_format_bundle = "ca-bundle.trust.p11-kit"
legacy_default_bundle = "ca-bundle.legacy.default.crt"
legacy_disable_bundle = "ca-bundle.legacy.disable.crt"

do_configure_append () {
    rm -rf ${BPN}
    mkdir -p ${BPN}/certs
    mkdir -p ${BPN}/certs/legacy-default
    mkdir -p ${BPN}/certs/legacy-disable
    mkdir -p ${BPN}/java
}

do_compile_append () {
    cd ${S}/${BPN}/certs
    cp ${WORKDIR}/certdata.txt .
    ${STAGING_BINDIR_NATIVE}/python-native/python ${WORKDIR}/certdata2pem.py >c2p.log 2>c2p.err
    cd ${S}/${BPN}
    cat ${WORKDIR}/nssckbi.h  |grep -w NSS_BUILTINS_LIBRARY_VERSION | awk '{print "# " $2 " " $3}' > ${p11_format_bundle}
    echo '#' >> ${p11_format_bundle}

    touch ${legacy_default_bundle}
    NUM_LEGACY_DEFAULT=`find certs/legacy-default -type f | wc -l`
    if [ $NUM_LEGACY_DEFAULT -ne 0 ]; then
        for f in certs/legacy-default/*.crt; do
          echo "processing $f"
          tbits=`sed -n '/^# openssl-trust/{s/^.*=//;p;}' $f`
          alias=`sed -n '/^# alias=/{s/^.*=//;p;q;}' $f | sed "s/'//g" | sed 's/"//g'`
          targs=""
          if [ -n "$tbits" ]; then
             for t in $tbits; do
                targs="${targs} -addtrust $t"
             done
          fi
          if [ -n "$targs" ]; then
             echo "legacy default flags $targs for $f" >> info.trust
             openssl x509 -text -in "$f" -trustout $targs -setalias "$alias" >> ${legacy_default_bundle}
          fi
        done
    fi

    touch ${legacy_disable_bundle}
    NUM_LEGACY_DISABLE=`find certs/legacy-disable -type f | wc -l`
    if [ $NUM_LEGACY_DISABLE -ne 0 ]; then
        for f in certs/legacy-disable/*.crt; do
          echo "processing $f"
          tbits=`sed -n '/^# openssl-trust/{s/^.*=//;p;}' $f`
          alias=`sed -n '/^# alias=/{s/^.*=//;p;q;}' $f | sed "s/'//g" | sed 's/"//g'`
          targs=""
          if [ -n "$tbits" ]; then
             for t in $tbits; do
                targs="${targs} -addtrust $t"
             done
          fi
          if [ -n "$targs" ]; then
             echo "legacy disable flags $targs for $f" >> info.trust
             openssl x509 -text -in "$f" -trustout $targs -setalias "$alias" >> ${legacy_disable_bundle}
          fi
        done
    fi

    P11FILES=`find certs -name \*.tmp-p11-kit | wc -l`
    if [ $P11FILES -ne 0 ]; then
      for p in certs/*.tmp-p11-kit; do
        cat "$p" >> ${p11_format_bundle}
      done
    fi
    # Append our trust fixes
    cat ${WORKDIR}/trust-fixes >> ${p11_format_bundle}
}

do_install_append_class-target () {
    install -d -m 755 ${D}${pkidir}/tls/certs
    install -d -m 755 ${D}${pkidir}/java
    install -d -m 755 ${D}${catrustdir}/source
    install -d -m 755 ${D}${catrustdir}/source/anchors
    install -d -m 755 ${D}${catrustdir}/source/blacklist
    install -d -m 755 ${D}${catrustdir}/extracted
    install -d -m 755 ${D}${catrustdir}/extracted/pem
    install -d -m 755 ${D}${catrustdir}/extracted/openssl
    install -d -m 755 ${D}${catrustdir}/extracted/java
    install -d -m 755 ${D}${datadir}/pki/ca-trust-source
    install -d -m 755 ${D}${datadir}/pki/ca-trust-source/anchors
    install -d -m 755 ${D}${datadir}/pki/ca-trust-source/blacklist
    install -d -m 755 ${D}${datadir}/pki/ca-trust-legacy

    install -p -m 644 ${WORKDIR}/README.usr ${D}${datadir}/pki/ca-trust-source/README
    install -p -m 644 ${WORKDIR}/README.etc ${D}${catrustdir}/README
    install -p -m 644 ${WORKDIR}/README.extr ${D}${catrustdir}/extracted/README
    install -p -m 644 ${WORKDIR}/README.java ${D}${catrustdir}/extracted/java/README
    install -p -m 644 ${WORKDIR}/README.openssl ${D}${catrustdir}/extracted/openssl/README
    install -p -m 644 ${WORKDIR}/README.pem ${D}${catrustdir}/extracted/pem/README
    install -p -m 644 ${WORKDIR}/README.src ${D}${catrustdir}/source/README

    install -d -m 755 ${D}${datadir}/doc
    install -d -m 755 ${D}${datadir}/doc/${BP}
    install -p -m 644 ${WORKDIR}/README.ca-certificates ${D}${datadir}/doc/${BP}/README

    install -p -m 644 ${S}/${BPN}/${p11_format_bundle} ${D}${datadir}/pki/ca-trust-source/${p11_format_bundle}

    install -p -m 644 ${S}/${BPN}/${legacy_default_bundle} ${D}${datadir}/pki/ca-trust-legacy/${legacy_default_bundle}
    install -p -m 644 ${S}/${BPN}/${legacy_disable_bundle} ${D}${datadir}/pki/ca-trust-legacy/${legacy_disable_bundle}

    install -p -m 644 ${WORKDIR}/ca-legacy.conf ${D}${catrustdir}/ca-legacy.conf

    touch -r ${WORKDIR}/certdata.txt ${D}${datadir}/pki/ca-trust-source/${p11_format_bundle}

    touch -r ${WORKDIR}/certdata.txt ${D}${datadir}/pki/ca-trust-legacy/${legacy_default_bundle}
    touch -r ${WORKDIR}/certdata.txt ${D}${datadir}/pki/ca-trust-legacy/${legacy_disable_bundle}

    install -d -m 755 ${D}${bindir}
    install -p -m 755 ${WORKDIR}/update-ca-trust ${D}${bindir}/update-ca-trust
    install -p -m 755 ${WORKDIR}/ca-legacy ${D}${bindir}/ca-legacy

    # touch ghosted files that will be extracted dynamically
    touch ${D}${catrustdir}/extracted/pem/tls-ca-bundle.pem
    touch ${D}${catrustdir}/extracted/pem/email-ca-bundle.pem
    touch ${D}${catrustdir}/extracted/pem/objsign-ca-bundle.pem
    touch ${D}${catrustdir}/extracted/openssl/${openssl_format_trust_bundle}
    touch ${D}${catrustdir}/extracted/${java_bundle}
}

FILES_${PN} += "\
    ${datadir}/pki/ \
    "

RDEPENDS_${PN}_class-target += " \
    p11-kit \
    "
