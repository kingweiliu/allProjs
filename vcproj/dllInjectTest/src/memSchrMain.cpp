// dllInject.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "psapi.h"
#include <iostream>
#include <string>
#include <Windows.h>
#include <shlwapi.h>
#include "public.h"


char buf[1024];

HANDLE hPipe = NULL;

enum OP{
	OP_Process,
	OP_Input,
	OP_Edit,
	OP_End,
};

int ShowMenu()
{
	printf("please select an option:\n");
	printf("\t1. select an process to inject.\n");
	printf("\t2. input a value to search.\n");
	printf("\t3. edit memory value.\n");
	printf("\t4. end inject.\nplease make a selection:");
	int index = 0;
	scanf("%d", &index);
	return index;
}


//所有的进程列举出来
// 选择一个进程进行注入

DWORD getProcIdNeedInject()
{
	DWORD dwProcessIDs[1024] = {0}, dwNeed = 0;
	BOOL bRet = EnumProcesses(dwProcessIDs, sizeof(DWORD)*1024, &dwNeed);
	if (!bRet)
		return -1;

	for (int i = 0; i< dwNeed/sizeof(DWORD); ++i)
	{
		HANDLE hProc = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ , FALSE, dwProcessIDs[i]);
		if (!hProc)
			continue;
		HMODULE hMod = NULL;
		DWORD dwModNeeds = 0;
		TCHAR pszName[MAX_PATH];
		if(EnumProcessModules(hProc, &hMod, sizeof(hMod), &dwModNeeds)){
			GetModuleBaseName(hProc, hMod, pszName, MAX_PATH);
			printf("%d--%ls\n", dwProcessIDs[i], pszName);
		}
		CloseHandle(hProc);
	}
	int nProcId;
	std::cin>>nProcId;
	return nProcId;
}

BOOL injectDll(DWORD dwProcID, TCHAR* pszDll)
{
	TCHAR pszDir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, pszDir);
	TCHAR pszDllName[MAX_PATH];
	::PathCombine(pszDllName, pszDir, pszDll);

	HANDLE hProc = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_WRITE | PROCESS_VM_OPERATION | PROCESS_CREATE_THREAD, FALSE, dwProcID);	
	VOID* pAddr = VirtualAllocEx(hProc, NULL, MAX_PATH, MEM_COMMIT, PAGE_READWRITE);
	SIZE_T sizeWrited = 0;
	BOOL bRet = WriteProcessMemory(hProc, pAddr, pszDllName, sizeof(pszDllName),  &sizeWrited);
	if (!bRet)
	{
		return FALSE;
	}

	PTHREAD_START_ROUTINE pLoadLibrary = (PTHREAD_START_ROUTINE)::GetProcAddress(::GetModuleHandleW(L"Kernel32"), "LoadLibraryW");
	DWORD dwThreadID; 
	HANDLE hThrd=  ::CreateRemoteThread(hProc, NULL, NULL, pLoadLibrary, pAddr, NULL,  &dwThreadID);

	
	return INVALID_HANDLE_VALUE == hThrd ;
}

template <class T>
BOOL searchInMem(T* pStart, SIZE_T dwLength, T dwValue){
	T* pEnd = (T*)((char*)pStart + dwLength);
	while (pStart < pEnd)
	{		
		if (*pStart == dwValue)
		{
			printf("\t%x\n", pStart);
		}
		pStart ++;
	}
	return TRUE;
	
}

int _tmain(int argc, _TCHAR* argv[])
{
	int index = ShowMenu();	
	do 
	{
		switch (index)
		{
		case 1:
			{
				DWORD dwProcId = getProcIdNeedInject();

				hPipe = CreateNamedPipe(
					PIPENAME,
					PIPE_ACCESS_DUPLEX, 
					PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE | PIPE_WAIT,
					PIPE_UNLIMITED_INSTANCES , 
					4096, 
					4096,
					0,
					NULL);
				if (hPipe == INVALID_HANDLE_VALUE)
				{
					return -1;
				}

				injectDll(dwProcId, L"build\\debug\\spy.dll");
				BOOL bConnect = ConnectNamedPipe(hPipe, NULL);
				if (!bConnect && GetLastError()!=ERROR_PIPE_CONNECTED)
				{
					return -1;
				}
				DWORD dwRead = 0;
				BOOL bRead = FALSE;
				bRead = ReadFile(hPipe, buf, 1024, &dwRead, NULL);
				CMessage* pMsg = (CMessage*)buf;
				if (pMsg->MsgCmd != Msg_OK)
				{
					return -1;
				}

				pMsg->MsgCmd = Msg_StartSession;
				pMsg->DataLength = 0;
				WriteFile(hPipe, buf, 8+pMsg->DataLength, &dwRead, NULL);

			}
			break;
		case 2:
			{
				if (!hPipe)
				{
					break;
				}
				printf("please Input dword to find:");
				DWORD dwFind = 0;
				scanf("%d", &dwFind);
				CMessage* pMsg = (CMessage*)buf;
				pMsg->MsgCmd = Msg_Find;
				pMsg->Data = (void*)dwFind;
				pMsg->DataLength = 4;
				DWORD dwDataOperated = 0;
				BOOL bRead = WriteFile(hPipe, buf, 8+pMsg->DataLength, &dwDataOperated, NULL);
				if (!bRead)
				{
					break;
				}
				bRead = ReadFile(hPipe, buf, 1024, &dwDataOperated, NULL);
				if (!bRead)
				{
					break;
				}
				printf("find result:%d\n", (DWORD)pMsg->Data);
			}
			break;
		case 3:
			{
				printf("please enter changed value:");
				int n;
				scanf("%d", &n);
				CMessage* pMsg = (CMessage*)buf;
				pMsg->MsgCmd = Msg_Edit;
				pMsg->Data = (void*)n;
				pMsg->DataLength = 4;
				DWORD dwDataOperated = 0;
				BOOL bRead = WriteFile(hPipe, buf, 8+pMsg->DataLength, &dwDataOperated, NULL);
				if (!bRead)
				{
					break;
				}
			}
			break;
		}
		index = ShowMenu();
	} while (index<4);
	return 0;
}

