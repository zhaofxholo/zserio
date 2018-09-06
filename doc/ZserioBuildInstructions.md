# Zserio Compiler Build Instructions

This section describes zserio build instructions for developers. If you are users, it should be normally
enough just to download the latest build of the zserio compiler and runtime libraries from
[Zserio Releases](https://github.com/ndsev/zserio/releases).

## Zserio Projects

Before we start to describe build instructions for zserio compiler, it's good to known which independent
projects can be found in zserio repository.

Zserio consists of the following independent projects:

- **Zserio core** - This Java project contains pure zserio compiler which parses zserio language. It doesn't
contain any generators.

- **C++ extension** - This Java project contains C++ generator.

- **C++ runtime library** - This C++ project contains runtime library for C++ extension which must be linked 
to generated C++ sources.

- **Java extension** - This Java project contains Java generator.

- **Java runtime library** - This Java project contains runtime library for Java extension which must be linked 
to generated Java sources.

- **XML extension** - This Java project contains XML generator.

- **Doc extension** - This Java project contains HTML generator.

- **Ant task** - This Java project contains Ant task `zserio` for zserio compiler.

- **Zserio bundle** - This Java project creates one standalone jar with Zserio core and all available
extensions together with their dependent jars.

## Prerequisites

Before you start building, make sure you have the following components installed:

- JAVA JDK
- Ant
- CMake

## Building by Bash Script

The easiest way how to build zserio compiler is by using prepared Bash script `build.sh` located in project's
folder `scripts`.

This script builds Zserio projects and installs them in folder `distr` (located in project's root by default). 

### Usage

The script `build.sh` has the following usage:

`build.sh [-h] [-c] [-p] [-o <dir>] package...`

The following table describes all supported switches:

Switch   | Switch long name     | Desription
:------: | -------------------- | -------------------------------
`-h`     | `--help`             | Show help.
`-c`     | `--clean`            | Clean package instead of build.
`-p`     | `--purge`            | Purge `build` and `distr` directories before build.
`-o`     | `--output-directory` | Output directory where `build` and `distr` will be located.

The argument `package` can be the arbitrary combination of the following packages:

Package            | Desription
------------------ | -------------------------------
`ant_task`         | Zserio Ant task.
`core`             | Zserio core.
`cpp`              | Zserio C++ extension.
`cpp_rt-linux32`   | Zserio C++ extension runtime library for native linux32 (gcc).
`cpp_rt-linux64`   | Zserio C++ extension runtime library for native linux64 (gcc).
`cpp_rt-windows32` | Zserio C++ extension runtime library for windows32 target (MinGW).
`cpp_rt-windows64` | Zserio C++ extension runtime library for windows64 target (MinGW64).
`java`             | Zserio Java extension.
`java_rt`          | Zserio Java extension runtime library.
`xml`              | Zserio XML extension.
`doc`              | Zserio Documentation extension.
`zserio`           | Zserio bundle (Zserio Core packed together with all already built extensions).
`all-linux32`      | All available packages for linux32.
`all-linux64`      | All available packages for linux64.
`all-windows32`    | All available packages for windows32.
`all-windows64`    | All available packages for windows64.

### Example

The following command creates Zserio bundle jar with Java extension together with Java runtime library jar
in `distr` directory:

`./build.sh core java java_rt zserio`

The following command creates Zserio bundle jar with C++ extension together with C++ runtime library for
linux64 platform in `distr` directory:

`./build.sh core cpp cpp_rt-linux64 zserio`

The following command creates Zserio bundle jar with all available extensions together with all runtime
libraries for linux64 platform in `distr` directory:

`./build.sh all-linux64`

## Building by Hand

If you don't like Bash script, you can build Zserio by hand.

All Java projects use Ant and can be built by the command:

`ant -f build.xml install`

There is one exception for Zserio bundle jar which is built by the command:

`ant -f build.xml zserio_bundle.install`

All C++ projects use CMake and can be build by commands:

```
mkdir build
cd build
cmake ..
cmake --build .
```

> Please don't forget about dependencies. Zserio core must be build before all extensions and Zserio bundle
> must be built as the last one.

### Example

The following command creates Zserio bundle jar with Java extension together with Java runtime library jar
in `distr` directory:

```
ant install
ant -f compiler/extensions/java install
ant -f compiler/extensions/java/runtime install
ant zserio_bundle.install
```