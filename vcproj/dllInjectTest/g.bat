set PATH=%PATH%;e:\gyp
set GYP_MSVS_VERSION=2005
dir
gyp dllInject.gyp --depth=.
cmd