// dllInject.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "psapi.h"
#include <iostream>
#include <string>
#include <Windows.h>
#include <shlwapi.h>


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

int _tmain(int argc, _TCHAR* argv[])
{
	DWORD dwProcId = getProcIdNeedInject();
	injectDll(dwProcId, L"spy.dll");

	return 0;
}

