# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.14

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /opt/jetbrains/clion-2019.1.4/bin/cmake/linux/bin/cmake

# The command to remove a file.
RM = /opt/jetbrains/clion-2019.1.4/bin/cmake/linux/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/parallel_counting_stars.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/parallel_counting_stars.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/parallel_counting_stars.dir/flags.make

CMakeFiles/parallel_counting_stars.dir/main.c.o: CMakeFiles/parallel_counting_stars.dir/flags.make
CMakeFiles/parallel_counting_stars.dir/main.c.o: ../main.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/parallel_counting_stars.dir/main.c.o"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/parallel_counting_stars.dir/main.c.o   -c /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/main.c

CMakeFiles/parallel_counting_stars.dir/main.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/parallel_counting_stars.dir/main.c.i"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/main.c > CMakeFiles/parallel_counting_stars.dir/main.c.i

CMakeFiles/parallel_counting_stars.dir/main.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/parallel_counting_stars.dir/main.c.s"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/main.c -o CMakeFiles/parallel_counting_stars.dir/main.c.s

CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.o: CMakeFiles/parallel_counting_stars.dir/flags.make
CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.o: ../src/fifo_pix_queue.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building C object CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.o"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.o   -c /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/src/fifo_pix_queue.c

CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.i"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/src/fifo_pix_queue.c > CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.i

CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.s"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/src/fifo_pix_queue.c -o CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.s

CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.o: CMakeFiles/parallel_counting_stars.dir/flags.make
CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.o: ../src/imagelib.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building C object CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.o"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.o   -c /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/src/imagelib.c

CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.i"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/src/imagelib.c > CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.i

CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.s"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/src/imagelib.c -o CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.s

CMakeFiles/parallel_counting_stars.dir/src/list_link.c.o: CMakeFiles/parallel_counting_stars.dir/flags.make
CMakeFiles/parallel_counting_stars.dir/src/list_link.c.o: ../src/list_link.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building C object CMakeFiles/parallel_counting_stars.dir/src/list_link.c.o"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/parallel_counting_stars.dir/src/list_link.c.o   -c /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/src/list_link.c

CMakeFiles/parallel_counting_stars.dir/src/list_link.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/parallel_counting_stars.dir/src/list_link.c.i"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/src/list_link.c > CMakeFiles/parallel_counting_stars.dir/src/list_link.c.i

CMakeFiles/parallel_counting_stars.dir/src/list_link.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/parallel_counting_stars.dir/src/list_link.c.s"
	mpicc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/src/list_link.c -o CMakeFiles/parallel_counting_stars.dir/src/list_link.c.s

# Object files for target parallel_counting_stars
parallel_counting_stars_OBJECTS = \
"CMakeFiles/parallel_counting_stars.dir/main.c.o" \
"CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.o" \
"CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.o" \
"CMakeFiles/parallel_counting_stars.dir/src/list_link.c.o"

# External object files for target parallel_counting_stars
parallel_counting_stars_EXTERNAL_OBJECTS =

parallel_counting_stars: CMakeFiles/parallel_counting_stars.dir/main.c.o
parallel_counting_stars: CMakeFiles/parallel_counting_stars.dir/src/fifo_pix_queue.c.o
parallel_counting_stars: CMakeFiles/parallel_counting_stars.dir/src/imagelib.c.o
parallel_counting_stars: CMakeFiles/parallel_counting_stars.dir/src/list_link.c.o
parallel_counting_stars: CMakeFiles/parallel_counting_stars.dir/build.make
parallel_counting_stars: /usr/lib/x86_64-linux-gnu/libpng.so
parallel_counting_stars: CMakeFiles/parallel_counting_stars.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Linking C executable parallel_counting_stars"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/parallel_counting_stars.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/parallel_counting_stars.dir/build: parallel_counting_stars

.PHONY : CMakeFiles/parallel_counting_stars.dir/build

CMakeFiles/parallel_counting_stars.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/parallel_counting_stars.dir/cmake_clean.cmake
.PHONY : CMakeFiles/parallel_counting_stars.dir/clean

CMakeFiles/parallel_counting_stars.dir/depend:
	cd /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug /home/josetobias/Dropbox/university/7th/CP/parallel_counting_stars/cmake-build-debug/CMakeFiles/parallel_counting_stars.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/parallel_counting_stars.dir/depend

