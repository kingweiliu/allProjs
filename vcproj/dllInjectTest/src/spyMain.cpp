// injectDll.cpp : Defines the entry point for the DLL application.
//

#include "stdafx.h"
#include <stdlib.h>



#ifdef _MANAGED
#pragma managed(push, off)
#endif

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{



	while (true)
	{
		DWORD *pWord = 0;
		pWord ++;
		//for (; pWord <(DWORD*)0x7FFFFFFF; pWord++)
		//{
		//	TCHAR buf[1024]=L"haha";
		//	//_ltow(*pWord, buf, 16);
		//	MessageBox(NULL, buf,L"ljw", MB_OK);
		//}
		Sleep(5000);


		
	}
    return TRUE;
}

#ifdef _MANAGED
#pragma managed(pop)
#endif

