# Copyright (C) 2014-2015 Free Software Foundation, Inc.
# 
# Copying and distribution of this file, with or without modification,
# are permitted in any medium without royalty provided the copyright
# notice and this notice are preserved.
#
# Unusual variables checked by this code:
#	NOP - four byte opcode for no-op (defaults to none)
#	NO_SMALL_DATA - no .sbss/.sbss2/.sdata/.sdata2 sections if not
#		empty.
#	SMALL_DATA_CTOR - .ctors contains small data.
#	SMALL_DATA_DTOR - .dtors contains small data.
#	DATA_ADDR - if end-of-text-plus-one-page isn't right for data start
#	INITIAL_READONLY_SECTIONS - at start of text segment
#	OTHER_READONLY_SECTIONS - other than .text .init .rodata ...
#		(e.g., .PARISC.milli)
#	OTHER_TEXT_SECTIONS - these get put in .text when relocating
#	INITIAL_READWRITE_SECTIONS - at start of data segment (after relro)
#	OTHER_READWRITE_SECTIONS - other than .data .bss .ctors .sdata ...
#		(e.g., .PARISC.global)
#	OTHER_RELRO_SECTIONS - other than .data.rel.ro ...
#		(e.g. PPC32 .fixup, .got[12])
#	OTHER_RELRO_SECTIONS_2 - as above, but after .dynamic in text segment
#	OTHER_BSS_SECTIONS - other than .bss .sbss ...
#	ATTRS_SECTIONS - at the end
#	OTHER_SECTIONS - at the end
#	EXECUTABLE_SYMBOLS - symbols that must be defined for an
#		executable (e.g., _DYNAMIC_LINK)
#       TEXT_START_ADDR - the first byte of the text segment, after any
#               headers.
#       TEXT_BASE_ADDRESS - the first byte of the text segment.
#	TEXT_START_SYMBOLS - symbols that appear at the start of the
#		.text section.
#	DATA_START_SYMBOLS - symbols that appear at the start of the
#		.data section.
#	DATA_END_SYMBOLS - symbols that appear at the end of the
#		writeable data sections.
#	OTHER_GOT_SYMBOLS - symbols defined just before .got.
#	OTHER_GOT_SECTIONS - sections just after .got.
#	OTHER_SDATA_SECTIONS - sections just after .sdata.
#	OTHER_BSS_SYMBOLS - symbols that appear at the start of the
#		.bss section besides __bss_start.
#	PLT_NEXT_DATA - .plt next to data segment when .plt is in text segment.
#	DATA_PLT - .plt should be in data segment, not text segment.
#	PLT_BEFORE_GOT - .plt just before .got when .plt is in data segement.
#	BSS_PLT - .plt should be in bss segment
#	NO_REL_RELOCS - Don't include .rel.* sections in script
#	NO_RELA_RELOCS - Don't include .rela.* sections in script
#	NON_ALLOC_DYN - Place dynamic sections after data segment.
#	TEXT_DYNAMIC - .dynamic in text segment, not data segment.
#	EMBEDDED - whether this is for an embedded system.
#	SHLIB_TEXT_START_ADDR - if set, add to SIZEOF_HEADERS to set
#		start address of shared library.
#	INPUT_FILES - INPUT command of files to always include
#	WRITABLE_RODATA - if set, the .rodata section should be writable
#	INIT_START, INIT_END -  statements just before and just after
# 	combination of .init sections.
#	FINI_START, FINI_END - statements just before and just after
# 	combination of .fini sections.
#	STACK_ADDR - start of a .stack section.
#	OTHER_SYMBOLS - symbols to place right at the end of the script.
#	ETEXT_NAME - name of a symbol for the end of the text section,
#		normally etext.
#	SEPARATE_CODE - if set, .text and similar sections containing
#		actual machine instructions must be in wholly disjoint
#		pages from any other data, including headers
#	SEPARATE_GOTPLT - if set, .got.plt should be separate output section,
#		so that .got can be in the RELRO area.  It should be set to
#		the number of bytes in the beginning of .got.plt which can be
#		in the RELRO area as well.
#	USER_LABEL_PREFIX - prefix to add to user-visible symbols.
#	RODATA_NAME, SDATA_NAME, SBSS_NAME, BSS_NAME - base parts of names
#		for standard sections, without initial "." or suffixes.
#
# When adding sections, do note that the names of some sections are used
# when specifying the start address of the next.
#

#  Many sections come in three flavours.  There is the 'real' section,
#  like ".data".  Then there are the per-procedure or per-variable
#  sections, generated by -ffunction-sections and -fdata-sections in GCC,
#  and useful for --gc-sections, which for a variable "foo" might be
#  ".data.foo".  Then there are the linkonce sections, for which the linker
#  eliminates duplicates, which are named like ".gnu.linkonce.d.foo".
#  The exact correspondences are:
#
#  Section	Linkonce section
#  .text	.gnu.linkonce.t.foo
#  .rodata	.gnu.linkonce.r.foo
#  .data	.gnu.linkonce.d.foo
#  .bss		.gnu.linkonce.b.foo
#  .sdata	.gnu.linkonce.s.foo
#  .sbss	.gnu.linkonce.sb.foo
#  .sdata2	.gnu.linkonce.s2.foo
#  .sbss2	.gnu.linkonce.sb2.foo
#  .debug_info	.gnu.linkonce.wi.foo
#  .tdata	.gnu.linkonce.td.foo
#  .tbss	.gnu.linkonce.tb.foo
#  .lrodata	.gnu.linkonce.lr.foo
#  .ldata	.gnu.linkonce.l.foo
#  .lbss	.gnu.linkonce.lb.foo
#
#  Each of these can also have corresponding .rel.* and .rela.* sections.

if test -n "$NOP"; then
  FILL="=$NOP"
else
  FILL=
fi

test -z "$RODATA_NAME" && RODATA_NAME=rodata
test -z "$SDATA_NAME" && SDATA_NAME=sdata
test -z "$SBSS_NAME" && SBSS_NAME=sbss
test -z "$BSS_NAME" && BSS_NAME=bss
test -z "$ENTRY" && ENTRY=${USER_LABEL_PREFIX}_start
test -z "${BIG_OUTPUT_FORMAT}" && BIG_OUTPUT_FORMAT=${OUTPUT_FORMAT}
test -z "${LITTLE_OUTPUT_FORMAT}" && LITTLE_OUTPUT_FORMAT=${OUTPUT_FORMAT}
if [ -z "$MACHINE" ]; then OUTPUT_ARCH=${ARCH}; else OUTPUT_ARCH=${ARCH}:${MACHINE}; fi
test -z "${ELFSIZE}" && ELFSIZE=32
test -z "${ALIGNMENT}" && ALIGNMENT="${ELFSIZE} / 8"
test "$LD_FLAG" = "N" && DATA_ADDR=.
test -z "${ETEXT_NAME}" && ETEXT_NAME=${USER_LABEL_PREFIX}etext
test -n "$CREATE_SHLIB$CREATE_PIE" && test -n "$SHLIB_DATA_ADDR" && COMMONPAGESIZE=""
test -z "$CREATE_SHLIB$CREATE_PIE" && test -n "$DATA_ADDR" && COMMONPAGESIZE=""
test -n "$RELRO_NOW" && unset SEPARATE_GOTPLT
test -z "$ATTRS_SECTIONS" && ATTRS_SECTIONS=".gnu.attributes 0 : { KEEP (*(.gnu.attributes)) }"
DATA_SEGMENT_ALIGN="ALIGN(${SEGMENT_SIZE}) + (. & (${MAXPAGESIZE} - 1))"
DATA_SEGMENT_RELRO_END=""
DATA_SEGMENT_END=""
if test -n "${COMMONPAGESIZE}"; then
  if test "${SEGMENT_SIZE}" != "${MAXPAGESIZE}"; then
    DATA_SEGMENT_ALIGN="ALIGN (${SEGMENT_SIZE}) - ((${MAXPAGESIZE} - .) & (${MAXPAGESIZE} - 1)); . = DATA_SEGMENT_ALIGN (${MAXPAGESIZE}, ${COMMONPAGESIZE})"
  else
    DATA_SEGMENT_ALIGN="DATA_SEGMENT_ALIGN (${MAXPAGESIZE}, ${COMMONPAGESIZE})"
  fi
  DATA_SEGMENT_END=". = DATA_SEGMENT_END (.);"
  DATA_SEGMENT_RELRO_END=". = DATA_SEGMENT_RELRO_END (${SEPARATE_GOTPLT-0}, .);"
fi
if test -z "${INITIAL_READONLY_SECTIONS}${CREATE_SHLIB}"; then
  INITIAL_READONLY_SECTIONS=".interp       ${RELOCATING-0} : { *(.interp) }"
fi
if test -z "$PLT"; then
  IPLT=".iplt         ${RELOCATING-0} : { *(.iplt) }"
  PLT=".plt          ${RELOCATING-0} : { *(.plt)${IREL_IN_PLT+ *(.iplt)} }
  ${IREL_IN_PLT-$IPLT}"
fi
test -n "${DATA_PLT-${BSS_PLT-text}}" && TEXT_PLT=
if test -z "$GOT"; then
  if test -z "$SEPARATE_GOTPLT"; then
    GOT=".got          ${RELOCATING-0} : { *(.got.plt) *(.igot.plt) *(.got) *(.igot) }"
  else
    GOT=".got          ${RELOCATING-0} : { *(.got) *(.igot) }"
    GOTPLT=".got.plt      ${RELOCATING-0} : { *(.got.plt)  *(.igot.plt) }"
  fi
fi
REL_IFUNC=".rel.ifunc    ${RELOCATING-0} : { *(.rel.ifunc) }"
RELA_IFUNC=".rela.ifunc   ${RELOCATING-0} : { *(.rela.ifunc) }"
REL_IPLT=".rel.iplt     ${RELOCATING-0} :
    {
      ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__rel_iplt_start = .);}}
      *(.rel.iplt)
      ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__rel_iplt_end = .);}}
    }"
RELA_IPLT=".rela.iplt    ${RELOCATING-0} :
    {
      ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__rela_iplt_start = .);}}
      *(.rela.iplt)
      ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__rela_iplt_end = .);}}
    }"
DYNAMIC=".dynamic      ${RELOCATING-0} : { *(.dynamic) }"
RODATA=".${RODATA_NAME}       ${RELOCATING-0} : { *(.${RODATA_NAME}${RELOCATING+ .${RODATA_NAME}.* .gnu.linkonce.r.*}) }"
DATARELRO=".data.rel.ro : { *(.data.rel.ro.local* .gnu.linkonce.d.rel.ro.local.*) *(.data.rel.ro .data.rel.ro.* .gnu.linkonce.d.rel.ro.*) }"
DISCARDED="/DISCARD/ : { *(.note.GNU-stack) *(.gnu_debuglink) *(.gnu.lto_*) }"
if test -z "${NO_SMALL_DATA}"; then
  SBSS=".${SBSS_NAME}         ${RELOCATING-0} :
  {
    ${RELOCATING+${SBSS_START_SYMBOLS}}
    ${CREATE_SHLIB+*(.${SBSS_NAME}2 .${SBSS_NAME}2.* .gnu.linkonce.sb2.*)}
    *(.dyn${SBSS_NAME})
    *(.${SBSS_NAME}${RELOCATING+ .${SBSS_NAME}.* .gnu.linkonce.sb.*})
    *(.scommon)
    ${RELOCATING+${SBSS_END_SYMBOLS}}
  }"
  SBSS2=".${SBSS_NAME}2        ${RELOCATING-0} : { *(.${SBSS_NAME}2${RELOCATING+ .${SBSS_NAME}2.* .gnu.linkonce.sb2.*}) }"
  SDATA="/* We want the small data sections together, so single-instruction offsets
     can access them all, and initialized data all before uninitialized, so
     we can shorten the on-disk segment size.  */
  .${SDATA_NAME}        ${RELOCATING-0} :
  {
    ${RELOCATING+${SDATA_START_SYMBOLS}}
    ${CREATE_SHLIB+*(.${SDATA_NAME}2 .${SDATA_NAME}2.* .gnu.linkonce.s2.*)}
    *(.${SDATA_NAME}${RELOCATING+ .${SDATA_NAME}.* .gnu.linkonce.s.*})
  }"
  SDATA2=".${SDATA_NAME}2       ${RELOCATING-0} :
  {
    ${RELOCATING+${SDATA2_START_SYMBOLS}}
    *(.${SDATA_NAME}2${RELOCATING+ .${SDATA_NAME}2.* .gnu.linkonce.s2.*})
  }"
  REL_SDATA=".rel.${SDATA_NAME}    ${RELOCATING-0} : { *(.rel.${SDATA_NAME}${RELOCATING+ .rel.${SDATA_NAME}.* .rel.gnu.linkonce.s.*}) }
  .rela.${SDATA_NAME}   ${RELOCATING-0} : { *(.rela.${SDATA_NAME}${RELOCATING+ .rela.${SDATA_NAME}.* .rela.gnu.linkonce.s.*}) }"
  REL_SBSS=".rel.${SBSS_NAME}     ${RELOCATING-0} : { *(.rel.${SBSS_NAME}${RELOCATING+ .rel.${SBSS_NAME}.* .rel.gnu.linkonce.sb.*}) }
  .rela.${SBSS_NAME}    ${RELOCATING-0} : { *(.rela.${SBSS_NAME}${RELOCATING+ .rela.${SBSS_NAME}.* .rela.gnu.linkonce.sb.*}) }"
  REL_SDATA2=".rel.${SDATA_NAME}2   ${RELOCATING-0} : { *(.rel.${SDATA_NAME}2${RELOCATING+ .rel.${SDATA_NAME}2.* .rel.gnu.linkonce.s2.*}) }
  .rela.${SDATA_NAME}2  ${RELOCATING-0} : { *(.rela.${SDATA_NAME}2${RELOCATING+ .rela.${SDATA_NAME}2.* .rela.gnu.linkonce.s2.*}) }"
  REL_SBSS2=".rel.${SBSS_NAME}2    ${RELOCATING-0} : { *(.rel.${SBSS_NAME}2${RELOCATING+ .rel.${SBSS_NAME}2.* .rel.gnu.linkonce.sb2.*}) }
  .rela.${SBSS_NAME}2   ${RELOCATING-0} : { *(.rela.${SBSS_NAME}2${RELOCATING+ .rela.${SBSS_NAME}2.* .rela.gnu.linkonce.sb2.*}) }"
else
  NO_SMALL_DATA=" "
fi
if test -z "${SDATA_GOT}${DATA_GOT}"; then
  if test -n "${NO_SMALL_DATA}"; then
    DATA_GOT=" "
  fi
fi
if test -z "${SDATA_GOT}${DATA_GOT}"; then
  if test -z "${NO_SMALL_DATA}"; then
    SDATA_GOT=" "
  fi
fi
test -n "$SEPARATE_GOTPLT" && SEPARATE_GOTPLT=" "
test "${LARGE_SECTIONS}" = "yes" && REL_LARGE="
  .rel.ldata    ${RELOCATING-0} : { *(.rel.ldata${RELOCATING+ .rel.ldata.* .rel.gnu.linkonce.l.*}) }
  .rela.ldata   ${RELOCATING-0} : { *(.rela.ldata${RELOCATING+ .rela.ldata.* .rela.gnu.linkonce.l.*}) }
  .rel.lbss     ${RELOCATING-0} : { *(.rel.lbss${RELOCATING+ .rel.lbss.* .rel.gnu.linkonce.lb.*}) }
  .rela.lbss    ${RELOCATING-0} : { *(.rela.lbss${RELOCATING+ .rela.lbss.* .rela.gnu.linkonce.lb.*}) }
  .rel.lrodata  ${RELOCATING-0} : { *(.rel.lrodata${RELOCATING+ .rel.lrodata.* .rel.gnu.linkonce.lr.*}) }
  .rela.lrodata ${RELOCATING-0} : { *(.rela.lrodata${RELOCATING+ .rela.lrodata.* .rela.gnu.linkonce.lr.*}) }"
test "${LARGE_SECTIONS}" = "yes" && LARGE_BSS="
  .lbss ${RELOCATING-0} :
  {
    *(.dynlbss)
    *(.lbss${RELOCATING+ .lbss.* .gnu.linkonce.lb.*})
    *(LARGE_COMMON)
  }"
test "${LARGE_SECTIONS}" = "yes" && LARGE_SECTIONS="
  .lrodata ${RELOCATING-0} ${RELOCATING+ALIGN(${MAXPAGESIZE}) + (. & (${MAXPAGESIZE} - 1))} :
  {
    *(.lrodata${RELOCATING+ .lrodata.* .gnu.linkonce.lr.*})
  }
  .ldata ${RELOCATING-0} ${RELOCATING+ALIGN(${MAXPAGESIZE}) + (. & (${MAXPAGESIZE} - 1))} :
  {
    *(.ldata${RELOCATING+ .ldata.* .gnu.linkonce.l.*})
    ${RELOCATING+. = ALIGN(. != 0 ? ${ALIGNMENT} : 1);}
  }"
if test "${ENABLE_INITFINI_ARRAY}" = "yes"; then
  SORT_INIT_ARRAY="KEEP (*(SORT_BY_INIT_PRIORITY(.init_array.*) SORT_BY_INIT_PRIORITY(.ctors.*)))"
  SORT_FINI_ARRAY="KEEP (*(SORT_BY_INIT_PRIORITY(.fini_array.*) SORT_BY_INIT_PRIORITY(.dtors.*)))"
  CTORS_IN_INIT_ARRAY="EXCLUDE_FILE (*crtbegin.o *crtbegin?.o *crtend.o *crtend?.o $OTHER_EXCLUDE_FILES) .ctors"
  DTORS_IN_FINI_ARRAY="EXCLUDE_FILE (*crtbegin.o *crtbegin?.o *crtend.o *crtend?.o $OTHER_EXCLUDE_FILES) .dtors"
else
  SORT_INIT_ARRAY="KEEP (*(SORT(.init_array.*)))"
  SORT_FINI_ARRAY="KEEP (*(SORT(.fini_array.*)))"
  CTORS_IN_INIT_ARRAY=
  DTORS_IN_FINI_ARRAY=
fi
INIT_ARRAY=".init_array   ${RELOCATING-0} :
  {
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__init_array_start = .);}}
    ${SORT_INIT_ARRAY}
    KEEP (*(.init_array ${CTORS_IN_INIT_ARRAY}))
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__init_array_end = .);}}
  }"
FINI_ARRAY=".fini_array   ${RELOCATING-0} :
  {
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__fini_array_start = .);}}
    ${SORT_FINI_ARRAY}
    KEEP (*(.fini_array ${DTORS_IN_FINI_ARRAY}))
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__fini_array_end = .);}}
  }"
CTOR=".ctors        ${CONSTRUCTING-0} :
  {
    ${CONSTRUCTING+${CTOR_START}}
    /* gcc uses crtbegin.o to find the start of
       the constructors, so we make sure it is
       first.  Because this is a wildcard, it
       doesn't matter if the user does not
       actually link against crtbegin.o; the
       linker won't look for a file to match a
       wildcard.  The wildcard also means that it
       doesn't matter which directory crtbegin.o
       is in.  */

    KEEP (*crtbegin.o(.ctors))
    KEEP (*crtbegin?.o(.ctors))

    /* We don't want to include the .ctor section from
       the crtend.o file until after the sorted ctors.
       The .ctor section from the crtend file contains the
       end of ctors marker and it must be last */

    KEEP (*(EXCLUDE_FILE (*crtend.o *crtend?.o $OTHER_EXCLUDE_FILES) .ctors))
    KEEP (*(SORT(.ctors.*)))
    KEEP (*(.ctors))
    ${CONSTRUCTING+${CTOR_END}}
  }"
DTOR=".dtors        ${CONSTRUCTING-0} :
  {
    ${CONSTRUCTING+${DTOR_START}}
    KEEP (*crtbegin.o(.dtors))
    KEEP (*crtbegin?.o(.dtors))
    KEEP (*(EXCLUDE_FILE (*crtend.o *crtend?.o $OTHER_EXCLUDE_FILES) .dtors))
    KEEP (*(SORT(.dtors.*)))
    KEEP (*(.dtors))
    ${CONSTRUCTING+${DTOR_END}}
  }"
STACK="  .stack        ${RELOCATING-0}${RELOCATING+${STACK_ADDR}} :
  {
    ${RELOCATING+${USER_LABEL_PREFIX}_stack = .;}
    *(.stack)
  }"

TEXT_START_ADDR="SEGMENT_START(\"text-segment\", ${TEXT_START_ADDR})"
SHLIB_TEXT_START_ADDR="SEGMENT_START(\"text-segment\", ${SHLIB_TEXT_START_ADDR:-0})"

if [ -z "$SEPARATE_CODE" ]; then
  SIZEOF_HEADERS_CODE=" + SIZEOF_HEADERS"
else
  SIZEOF_HEADERS_CODE=
fi

# If this is for an embedded system, don't add SIZEOF_HEADERS.
if [ -z "$EMBEDDED" ]; then
   test -z "${TEXT_BASE_ADDRESS}" && TEXT_BASE_ADDRESS="${TEXT_START_ADDR}${SIZEOF_HEADERS_CODE}"
else
   test -z "${TEXT_BASE_ADDRESS}" && TEXT_BASE_ADDRESS="${TEXT_START_ADDR}"
fi

cat <<EOF
/* Copyright (C) 2014-2015 Free Software Foundation, Inc.

   Copying and distribution of this script, with or without modification,
   are permitted in any medium without royalty provided the copyright
   notice and this notice are preserved.  */

OUTPUT_FORMAT("${OUTPUT_FORMAT}", "${BIG_OUTPUT_FORMAT}",
	      "${LITTLE_OUTPUT_FORMAT}")
OUTPUT_ARCH(${OUTPUT_ARCH})
${RELOCATING+ENTRY(${ENTRY})}

${RELOCATING+${LIB_SEARCH_DIRS}}
${RELOCATING+${EXECUTABLE_SYMBOLS}}
${RELOCATING+${INPUT_FILES}}
${RELOCATING- /* For some reason, the Solaris linker makes bad executables
  if gld -r is used and the intermediate file has sections starting
  at non-zero addresses.  Could be a Solaris ld bug, could be a GNU ld
  bug.  But for now assigning the zero vmas works.  */}

SECTIONS
{
  /* Read-only sections, merged into text segment: */
  ${CREATE_SHLIB-${CREATE_PIE-${RELOCATING+PROVIDE (__executable_start = ${TEXT_START_ADDR}); . = ${TEXT_BASE_ADDRESS};}}}
  ${CREATE_SHLIB+${RELOCATING+. = ${SHLIB_TEXT_START_ADDR}${SIZEOF_HEADERS_CODE};}}
  ${CREATE_PIE+${RELOCATING+PROVIDE (__executable_start = ${SHLIB_TEXT_START_ADDR}); . = ${SHLIB_TEXT_START_ADDR}${SIZEOF_HEADERS_CODE};}}
EOF

emit_early_ro()
{
  cat <<EOF
  ${INITIAL_READONLY_SECTIONS}
  .note.gnu.build-id : { *(.note.gnu.build-id) }
EOF
}

test -n "${SEPARATE_CODE}" || emit_early_ro

test -n "${RELOCATING+0}" || unset NON_ALLOC_DYN
test -z "${NON_ALLOC_DYN}" || TEXT_DYNAMIC=
cat > ldscripts/dyntmp.$$ <<EOF
  ${TEXT_DYNAMIC+${DYNAMIC}}
  .hash         ${RELOCATING-0} : { *(.hash) }
  .gnu.hash     ${RELOCATING-0} : { *(.gnu.hash) }
  .dynsym       ${RELOCATING-0} : { *(.dynsym) }
  .dynstr       ${RELOCATING-0} : { *(.dynstr) }
  .gnu.version  ${RELOCATING-0} : { *(.gnu.version) }
  .gnu.version_d ${RELOCATING-0}: { *(.gnu.version_d) }
  .gnu.version_r ${RELOCATING-0}: { *(.gnu.version_r) }
EOF

if [ "x$COMBRELOC" = x ]; then
  COMBRELOCCAT="cat >> ldscripts/dyntmp.$$"
else
  COMBRELOCCAT="cat > $COMBRELOC"
fi
eval $COMBRELOCCAT <<EOF
  ${INITIAL_RELOC_SECTIONS}
  .rel.init     ${RELOCATING-0} : { *(.rel.init) }
  .rela.init    ${RELOCATING-0} : { *(.rela.init) }
  .rel.text     ${RELOCATING-0} : { *(.rel.text${RELOCATING+ .rel.text.* .rel.gnu.linkonce.t.*}) }
  .rela.text    ${RELOCATING-0} : { *(.rela.text${RELOCATING+ .rela.text.* .rela.gnu.linkonce.t.*}) }
  .rel.fini     ${RELOCATING-0} : { *(.rel.fini) }
  .rela.fini    ${RELOCATING-0} : { *(.rela.fini) }
  .rel.${RODATA_NAME}   ${RELOCATING-0} : { *(.rel.${RODATA_NAME}${RELOCATING+ .rel.${RODATA_NAME}.* .rel.gnu.linkonce.r.*}) }
  .rela.${RODATA_NAME}  ${RELOCATING-0} : { *(.rela.${RODATA_NAME}${RELOCATING+ .rela.${RODATA_NAME}.* .rela.gnu.linkonce.r.*}) }
  ${OTHER_READONLY_RELOC_SECTIONS}
  .rel.data.rel.ro ${RELOCATING-0} : { *(.rel.data.rel.ro${RELOCATING+ .rel.data.rel.ro.* .rel.gnu.linkonce.d.rel.ro.*}) }
  .rela.data.rel.ro ${RELOCATING-0} : { *(.rela.data.rel.ro${RELOCATING+ .rela.data.rel.ro.* .rela.gnu.linkonce.d.rel.ro.*}) }
  .rel.data     ${RELOCATING-0} : { *(.rel.data${RELOCATING+ .rel.data.* .rel.gnu.linkonce.d.*}) }
  .rela.data    ${RELOCATING-0} : { *(.rela.data${RELOCATING+ .rela.data.* .rela.gnu.linkonce.d.*}) }
  ${OTHER_READWRITE_RELOC_SECTIONS}
  .rel.tdata	${RELOCATING-0} : { *(.rel.tdata${RELOCATING+ .rel.tdata.* .rel.gnu.linkonce.td.*}) }
  .rela.tdata	${RELOCATING-0} : { *(.rela.tdata${RELOCATING+ .rela.tdata.* .rela.gnu.linkonce.td.*}) }
  .rel.tbss	${RELOCATING-0} : { *(.rel.tbss${RELOCATING+ .rel.tbss.* .rel.gnu.linkonce.tb.*}) }
  .rela.tbss	${RELOCATING-0} : { *(.rela.tbss${RELOCATING+ .rela.tbss.* .rela.gnu.linkonce.tb.*}) }
  .rel.ctors    ${RELOCATING-0} : { *(.rel.ctors) }
  .rela.ctors   ${RELOCATING-0} : { *(.rela.ctors) }
  .rel.dtors    ${RELOCATING-0} : { *(.rel.dtors) }
  .rela.dtors   ${RELOCATING-0} : { *(.rela.dtors) }
  .rel.got      ${RELOCATING-0} : { *(.rel.got) }
  .rela.got     ${RELOCATING-0} : { *(.rela.got) }
  ${OTHER_GOT_RELOC_SECTIONS}
  ${REL_SDATA}
  ${REL_SBSS}
  ${REL_SDATA2}
  ${REL_SBSS2}
  .rel.${BSS_NAME}      ${RELOCATING-0} : { *(.rel.${BSS_NAME}${RELOCATING+ .rel.${BSS_NAME}.* .rel.gnu.linkonce.b.*}) }
  .rela.${BSS_NAME}     ${RELOCATING-0} : { *(.rela.${BSS_NAME}${RELOCATING+ .rela.${BSS_NAME}.* .rela.gnu.linkonce.b.*}) }
  ${REL_LARGE}
  ${IREL_IN_PLT+$REL_IFUNC}
  ${IREL_IN_PLT+$RELA_IFUNC}
  ${IREL_IN_PLT-$REL_IPLT}
  ${IREL_IN_PLT-$RELA_IPLT}
EOF

if [ -n "$COMBRELOC" ]; then
cat >> ldscripts/dyntmp.$$ <<EOF
  .rel.dyn      ${RELOCATING-0} :
    {
EOF
sed -e '/^[ 	]*[{}][ 	]*$/d;/:[ 	]*$/d;/\.rela\./d;/__rela_iplt_/d;s/^.*: { *\(.*\)}$/      \1/' $COMBRELOC >> ldscripts/dyntmp.$$
cat >> ldscripts/dyntmp.$$ <<EOF
    }
  .rela.dyn     ${RELOCATING-0} :
    {
EOF
sed -e '/^[ 	]*[{}][ 	]*$/d;/:[ 	]*$/d;/\.rel\./d;/__rel_iplt_/d;s/^.*: { *\(.*\)}/      \1/' $COMBRELOC >> ldscripts/dyntmp.$$
cat >> ldscripts/dyntmp.$$ <<EOF
    }
EOF
fi

cat >> ldscripts/dyntmp.$$ <<EOF
  .rel.plt      ${RELOCATING-0} :
    {
      *(.rel.plt)
      ${IREL_IN_PLT+${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__rel_iplt_start = .);}}}
      ${IREL_IN_PLT+${RELOCATING+*(.rel.iplt)}}
      ${IREL_IN_PLT+${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__rel_iplt_end = .);}}}
    }
  .rela.plt     ${RELOCATING-0} :
    {
      *(.rela.plt)
      ${IREL_IN_PLT+${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__rela_iplt_start = .);}}}
      ${IREL_IN_PLT+${RELOCATING+*(.rela.iplt)}}
      ${IREL_IN_PLT+${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__rela_iplt_end = .);}}}
    }
  ${OTHER_PLT_RELOC_SECTIONS}
EOF

emit_dyn()
{
  if test -z "${NO_REL_RELOCS}${NO_RELA_RELOCS}"; then
    cat ldscripts/dyntmp.$$
  else
    if test -z "${NO_REL_RELOCS}"; then
      sed -e '/^[ 	]*\.rela\.[^}]*$/,/}/d;/^[ 	]*\.rela\./d;/__rela_iplt_/d' ldscripts/dyntmp.$$
    fi
    if test -z "${NO_RELA_RELOCS}"; then
      sed -e '/^[ 	]*\.rel\.[^}]*$/,/}/d;/^[ 	]*\.rel\./d;/__rel_iplt_/d' ldscripts/dyntmp.$$
    fi
  fi
  rm -f ldscripts/dyntmp.$$
}

test -n "${NON_ALLOC_DYN}${SEPARATE_CODE}" || emit_dyn

cat <<EOF
  .init         ${RELOCATING-0} :
  {
    ${RELOCATING+${INIT_START}}
    KEEP (*(SORT_NONE(.init)))
    ${RELOCATING+${INIT_END}}
  } ${FILL}

  ${TEXT_PLT+${PLT_NEXT_DATA-${PLT}}}
  ${TINY_READONLY_SECTION}
  .text         ${RELOCATING-0} :
  {
    ${RELOCATING+${TEXT_START_SYMBOLS}}
    ${RELOCATING+*(.text.unlikely .text.*_unlikely .text.unlikely.*)}
    ${RELOCATING+*(.text.exit .text.exit.*)}
    ${RELOCATING+*(.text.startup .text.startup.*)}
    ${RELOCATING+*(.text.hot .text.hot.*)}
    *(.text .stub${RELOCATING+ .text.* .gnu.linkonce.t.*})
    /* .gnu.warning sections are handled specially by elf32.em.  */
    *(.gnu.warning)
    ${RELOCATING+${OTHER_TEXT_SECTIONS}}
  } ${FILL}
  .fini         ${RELOCATING-0} :
  {
    ${RELOCATING+${FINI_START}}
    KEEP (*(SORT_NONE(.fini)))
    ${RELOCATING+${FINI_END}}
  } ${FILL}
  ${RELOCATING+PROVIDE (__${ETEXT_NAME} = .);}
  ${RELOCATING+PROVIDE (_${ETEXT_NAME} = .);}
  ${RELOCATING+PROVIDE (${ETEXT_NAME} = .);}
EOF

if test -n "${SEPARATE_CODE}"; then
  if test -n "${RODATA_ADDR}"; then
    RODATA_ADDR="\
SEGMENT_START(\"rodata-segment\", ${RODATA_ADDR}) + SIZEOF_HEADERS"
  else
    RODATA_ADDR="ALIGN(${SEGMENT_SIZE}) + (. & (${MAXPAGESIZE} - 1))"
    RODATA_ADDR="SEGMENT_START(\"rodata-segment\", ${RODATA_ADDR})"
  fi
  if test -n "${SHLIB_RODATA_ADDR}"; then
    SHLIB_RODATA_ADDR="\
SEGMENT_START(\"rodata-segment\", ${SHLIB_RODATA_ADDR}) + SIZEOF_HEADERS"
  else
    SHLIB_RODATA_ADDR="SEGMENT_START(\"rodata-segment\", ${SHLIB_RODATA_ADDR})"
    SHLIB_RODATA_ADDR="ALIGN(${SEGMENT_SIZE}) + (. & (${MAXPAGESIZE} - 1))"
  fi
  cat <<EOF
  /* Adjust the address for the rodata segment.  We want to adjust up to
     the same address within the page on the next page up.  */
  ${CREATE_SHLIB-${CREATE_PIE-${RELOCATING+. = ${RODATA_ADDR};}}}
  ${CREATE_SHLIB+${RELOCATING+. = ${SHLIB_RODATA_ADDR};}}
  ${CREATE_PIE+${RELOCATING+. = ${SHLIB_RODATA_ADDR};}}
EOF
  emit_early_ro
  emit_dyn
fi

cat <<EOF
  ${WRITABLE_RODATA-${RODATA}}
  .${RODATA_NAME}1      ${RELOCATING-0} : { *(.${RODATA_NAME}1) }
  ${CREATE_SHLIB-${SDATA2}}
  ${CREATE_SHLIB-${SBSS2}}
  ${OTHER_READONLY_SECTIONS}
  .eh_frame_hdr : { *(.eh_frame_hdr) ${RELOCATING+*(.eh_frame_entry .eh_frame_entry.*)} }
  .eh_frame     ${RELOCATING-0} : ONLY_IF_RO { KEEP (*(.eh_frame)) ${RELOCATING+*(.eh_frame.*)} }
  .gcc_except_table ${RELOCATING-0} : ONLY_IF_RO { *(.gcc_except_table
  .gcc_except_table.*) }
  .gnu_extab ${RELOCATING-0} : ONLY_IF_RO { *(.gnu_extab*) }
  /* These sections are generated by the Sun/Oracle C++ compiler.  */
  .exception_ranges ${RELOCATING-0} : ONLY_IF_RO { *(.exception_ranges
  .exception_ranges*) }
  ${TEXT_PLT+${PLT_NEXT_DATA+${PLT}}}

  /* Adjust the address for the data segment.  We want to adjust up to
     the same address within the page on the next page up.  */
  ${CREATE_SHLIB-${CREATE_PIE-${RELOCATING+. = ${DATA_ADDR-${DATA_SEGMENT_ALIGN}};}}}
  ${CREATE_SHLIB+${RELOCATING+. = ${SHLIB_DATA_ADDR-${DATA_SEGMENT_ALIGN}};}}
  ${CREATE_PIE+${RELOCATING+. = ${SHLIB_DATA_ADDR-${DATA_SEGMENT_ALIGN}};}}

  /* Exception handling  */
  .eh_frame     ${RELOCATING-0} : ONLY_IF_RW { KEEP (*(.eh_frame)) ${RELOCATING+*(.eh_frame.*)} }
  .gnu_extab    ${RELOCATING-0} : ONLY_IF_RW { *(.gnu_extab) }
  .gcc_except_table ${RELOCATING-0} : ONLY_IF_RW { *(.gcc_except_table .gcc_except_table.*) }
  .exception_ranges ${RELOCATING-0} : ONLY_IF_RW { *(.exception_ranges .exception_ranges*) }

  /* Thread Local Storage sections  */
  .tdata	${RELOCATING-0} : { *(.tdata${RELOCATING+ .tdata.* .gnu.linkonce.td.*}) }
  .tbss		${RELOCATING-0} : { *(.tbss${RELOCATING+ .tbss.* .gnu.linkonce.tb.*})${RELOCATING+ *(.tcommon)} }

  .preinit_array   ${RELOCATING-0} :
  {
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__preinit_array_start = .);}}
    KEEP (*(.preinit_array))
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__preinit_array_end = .);}}
  }
  ${RELOCATING+${INIT_ARRAY}}
  ${RELOCATING+${FINI_ARRAY}}
  ${SMALL_DATA_CTOR-${RELOCATING+${CTOR}}}
  ${SMALL_DATA_DTOR-${RELOCATING+${DTOR}}}
  .jcr          ${RELOCATING-0} : { KEEP (*(.jcr)) }

  ${RELOCATING+${DATARELRO}}
  ${OTHER_RELRO_SECTIONS}
  ${TEXT_DYNAMIC-${DYNAMIC}}
  ${OTHER_RELRO_SECTIONS_2}
  ${DATA_GOT+${RELRO_NOW+${DATA_PLT+${PLT_BEFORE_GOT+${PLT}}}}}
  ${DATA_GOT+${RELRO_NOW+${GOT}}}
  ${DATA_GOT+${RELRO_NOW+${GOTPLT}}}
  ${DATA_GOT+${RELRO_NOW-${SEPARATE_GOTPLT+${GOT}}}}
  ${RELOCATING+${DATA_SEGMENT_RELRO_END}}
  ${INITIAL_READWRITE_SECTIONS}
  ${DATA_SDATA+${SDATA}}
  ${DATA_SDATA+${OTHER_SDATA_SECTIONS}}
  ${DATA_SDATA+${SBSS}}
  ${DATA_GOT+${RELRO_NOW-${DATA_PLT+${PLT_BEFORE_GOT+${PLT}}}}}
  ${DATA_GOT+${RELRO_NOW-${SEPARATE_GOTPLT-${GOT}}}}
  ${DATA_GOT+${RELRO_NOW-${GOTPLT}}}

  ${DATA_PLT+${PLT_BEFORE_GOT-${PLT}}}

  .data         ${RELOCATING-0} :
  {
    ${RELOCATING+${DATA_START_SYMBOLS}}
    *(.data${RELOCATING+ .data.* .gnu.linkonce.d.*})
    ${CONSTRUCTING+SORT(CONSTRUCTORS)}
  }
  .data1        ${RELOCATING-0} : { *(.data1) }
  ${WRITABLE_RODATA+${RODATA}}
  ${OTHER_READWRITE_SECTIONS}
  ${SMALL_DATA_CTOR+${RELOCATING+${CTOR}}}
  ${SMALL_DATA_DTOR+${RELOCATING+${DTOR}}}
  ${SDATA_GOT+${DATA_PLT+${PLT_BEFORE_GOT+${PLT}}}}
  ${SDATA_GOT+${RELOCATING+${OTHER_GOT_SYMBOLS+. = .; ${OTHER_GOT_SYMBOLS}}}}
  ${SDATA_GOT+${GOT}}
  ${SDATA_GOT+${OTHER_GOT_SECTIONS}}
  ${DATA_SDATA-${SDATA}}
  ${DATA_SDATA-${OTHER_SDATA_SECTIONS}}
  ${RELOCATING+${DATA_END_SYMBOLS-${USER_LABEL_PREFIX}_edata = .; PROVIDE (${USER_LABEL_PREFIX}edata = .);}}
  ${RELOCATING+. = .;}
  ${RELOCATING+${USER_LABEL_PREFIX}__bss_start = .;}
  ${RELOCATING+${OTHER_BSS_SYMBOLS}}
  ${DATA_SDATA-${SBSS}}
  ${BSS_PLT+${PLT}}
  .${BSS_NAME}          ${RELOCATING-0} :
  {
   *(.dyn${BSS_NAME})
   *(.${BSS_NAME}${RELOCATING+ .${BSS_NAME}.* .gnu.linkonce.b.*})
   *(COMMON)
   /* Align here to ensure that the .bss section occupies space up to
      _end.  Align after .bss to ensure correct alignment even if the
      .bss section disappears because there are no input sections.
      FIXME: Why do we need it? When there is no .bss section, we don't
      pad the .data section.  */
   ${RELOCATING+. = ALIGN(. != 0 ? ${ALIGNMENT} : 1);}
  }
  ${OTHER_BSS_SECTIONS}
  ${LARGE_BSS_AFTER_BSS+${LARGE_BSS}}
  ${RELOCATING+${OTHER_BSS_END_SYMBOLS}}
  ${RELOCATING+. = ALIGN(${ALIGNMENT});}
EOF

LARGE_DATA_ADDR=". = SEGMENT_START(\"ldata-segment\", ${LARGE_DATA_ADDR-.});"
SHLIB_LARGE_DATA_ADDR=". = SEGMENT_START(\"ldata-segment\", ${SHLIB_LARGE_DATA_ADDR-.});"

  cat <<EOF
  ${RELOCATING+${CREATE_SHLIB-${CREATE_PIE-${LARGE_DATA_ADDR}}}}
  ${RELOCATING+${CREATE_SHLIB+${SHLIB_LARGE_DATA_ADDR}}}
  ${RELOCATING+${CREATE_PIE+${SHLIB_LARGE_DATA_ADDR}}}
  ${LARGE_SECTIONS}
  ${LARGE_BSS_AFTER_BSS-${LARGE_BSS}}
  ${RELOCATING+. = ALIGN(${ALIGNMENT});}
  ${RELOCATING+${OTHER_END_SYMBOLS}}
  ${RELOCATING+${END_SYMBOLS-${USER_LABEL_PREFIX}_end = .; PROVIDE (${USER_LABEL_PREFIX}end = .);}}
  ${RELOCATING+${DATA_SEGMENT_END}}
EOF

test -z "${NON_ALLOC_DYN}" || emit_dyn

cat <<EOF
  /* Stabs debugging sections.  */
  .stab          0 : { *(.stab) }
  .stabstr       0 : { *(.stabstr) }
  .stab.excl     0 : { *(.stab.excl) }
  .stab.exclstr  0 : { *(.stab.exclstr) }
  .stab.index    0 : { *(.stab.index) }
  .stab.indexstr 0 : { *(.stab.indexstr) }

  .comment       0 : { *(.comment) }

EOF

. $srcdir/scripttempl/DWARF.sc

cat <<EOF

  ${TINY_DATA_SECTION}
  ${TINY_BSS_SECTION}

  ${STACK_ADDR+${STACK}}
  ${ATTRS_SECTIONS}
  ${OTHER_SECTIONS}
  ${RELOCATING+${OTHER_SYMBOLS}}
  ${RELOCATING+${DISCARDED}}
}
EOF