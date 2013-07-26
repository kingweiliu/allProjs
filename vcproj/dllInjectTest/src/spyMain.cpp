// injectDll.cpp : Defines the entry point for the DLL application.
//

#include "stdafx.h"
#include <stdlib.h>
#include "public.h" 
#include "Session.h"
#include "process.h"


unsigned __stdcall ThreadRun( void * )
{
	HANDLE hFile  = ::CreateFile(PIPENAME, GENERIC_READ | GENERIC_WRITE, FILE_SHARE_READ | FILE_SHARE_WRITE, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL , NULL);

	if (hFile ==INVALID_HANDLE_VALUE)
	{
		return -1 ;
	}	

	char buffer[1024];
	CMessage* pMsg = (CMessage*)buffer;
	pMsg->MsgCmd = Msg_OK;
	pMsg->DataLength = 0;
	DWORD dwWrite = 0;
	BOOL bRet = WriteFile(hFile, buffer, 8+pMsg->DataLength, &dwWrite, NULL);
	CSession<DWORD> * pSession = NULL;
	do 
	{
		bRet = ReadFile(hFile, buffer, 1024, &dwWrite, NULL);
		switch (pMsg->MsgCmd)
		{
		case Msg_StartSession:
			{
				if (pSession)
				{
					delete pSession;
				}
				pSession = new CSession<DWORD>		;	
			}
			break;
		case Msg_Find:
			{
				if (!pSession)
				{
					pSession = new CSession<DWORD>;
				}
				DWORD dwData = (DWORD)pMsg->Data;
				int count = pSession->Search(dwData);
				pMsg->Data = (void*)count;
				WriteFile(hFile, buffer, 1024, &dwWrite, NULL);
			}

			break;
		case Msg_Edit:
			{
				if (!pSession)
				{
					break;
				}
				DWORD dwData = (DWORD)pMsg->Data;
				bool bRet =pSession->SetValue(dwData);
				pMsg->Data = (void*) (bRet ? (void*)1:0);
				WriteFile(hFile, buffer, 1024, &dwWrite, NULL);

			}
		}

	} while (bRet);

	return 1;
}
	

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
		_beginthreadex(NULL, NULL, ThreadRun, NULL, NULL, NULL);
		break;
	}

	return TRUE;
	
}



