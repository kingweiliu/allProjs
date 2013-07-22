set PATH=%PATH%;D:\program\gyp;C:\Python27
set GYP_MSVS_VERSION=2010
dir
gyp dllInject.gyp --depth=.
cmd