cmake_minimum_required (VERSION 3.13)

project (Contour)

include_directories(./includes)

add_executable(Contour src/main.c includes/tracker.h src/tracker.c includes/imagelib.h src/imagelib.c includes/contour_check.h src/contour_check.c includes/logger.h src/logger.c includes/converter.h src/converter.c)

target_link_libraries(Contour m)