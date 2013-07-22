{
	'includes':[
		'devenv.gypi',
	],
	'targets': [{
		'target_name': 'spy',
		'type': 'shared_library',
		'defines': ['WIN32',		
		'UNICODE',
		'_UNICODE',
		],
		'msvs_precompiled_header': 'src/stdafx.h',
        'msvs_precompiled_source': 'src/stdafx.cpp',
		'sources': [
		'src/spyMain.cpp',
		'src/Session.cpp',
		'src/Session.h',
		'src/stdafx.cpp',
		'src/stdafx.h',
		'src/spy.h',
		'src/spy.cpp',
		],
		'configuration':{
		  'VCLinkerTool':{
		    'AdditionalDependencies':[
			  'shlwapi.lib',
			]
		  },
		}
			
	},
	{
		'target_name': 'memSchr',
		'type': 'executable',
		'defines': ['WIN32',
		'_CONSOLE',
		'UNICODE',
		'_UNICODE',
		],
		'msvs_precompiled_header': 'src/stdafx.h',
        'msvs_precompiled_source': 'src/stdafx.cpp',
		'sources': ['src/memSchrMain.cpp',
		'src/stdafx.cpp',
		'src/stdafx.h',		
		],
		'libraries': ['psapi.lib',
			'shlwapi.lib'
		],
		
		
		
	}]
}