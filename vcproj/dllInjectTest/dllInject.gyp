{
	'targets': [{
		'target_name': 'injectDll',
		'type': 'shared_library',
		'sources': ['src/injectDll.cpp',
		'src/stdafx.cpp',
		'src/stdafx.h',
		],
		'msvs_settings': {
			'VCLinkerTool': {
				'ResourceOnlyDLL': 'true',
				
			},
			
		},
		
	},
	{
		'target_name': 'dllInject',
		'type': 'executable',
		'defines': ['WIN32',
		'_CONSOLE',
		'UNICODE',
		'_UNICODE',
		],
		'sources': ['src/dllInject.cpp',
		'src/stdafx.cpp',
		'src/stdafx.h',
		'src/targetver.h',
		],
		'libraries': ['psapi.lib',
		],
		'default_configuration': 'Debug',
		'configurations': {
			'Debug': {
				'msvs_configuration_platform': 'Win32',
				'msvs_settings': {
					'VCCLCompilerTool': {
						'Optimization': '0',
						'PreprocessorDefinitions': ['_DEBUG'],
						'BasicRuntimeChecks': '3',
						'RuntimeLibrary': '1',
						'DebugInformationFormat': '3',
						
					},
					'VCLinkerTool': {
						'GenerateDebugInformation': 'true',
						'TargetMachine': '1',
						'LinkIncremental': '2',
						
					}
				}
			},
			
		},
		
	}]
}