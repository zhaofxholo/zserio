set(ZSERIO_GEN_DIR ${CMAKE_CURRENT_BINARY_DIR}/gen)
set(ZSERIO_GENERATED_SOURCES
    ${ZSERIO_GEN_DIR}/structure_types/one_string_structure/OneStringStructure.cpp
    ${ZSERIO_GEN_DIR}/structure_types/one_string_structure/OneStringStructure.h

    ${ZSERIO_GEN_DIR}/structure_types/simple_structure/SimpleStructure.cpp
    ${ZSERIO_GEN_DIR}/structure_types/simple_structure/SimpleStructure.h
)

zserio_add_library(TARGET structure_types_zs
    SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/zs
    MAIN_SOURCE structure_types.zs
    OUT_DIR ${ZSERIO_GEN_DIR}
    OUT_FILES ${ZSERIO_GENERATED_SOURCES}
    ZSERIO_CORE_DIR ${ZSERIO_JAR_DIR}
    ZSERIO_OPTIONS -withoutSourcesAmalgamation
)

add_test_sources(
    cpp/OneStringStructureTest.cpp
    cpp/SimpleStructureTest.cpp
)

add_test_dependencies(structure_types_zs)
