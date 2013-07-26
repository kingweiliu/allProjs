#pragma once

#include <vector>
#include <map>
#include <windows.h>

template <class T>
class CSession
{
public:
	CSession()
	{
	}
	int Search(T value){
		
		if(!m_vecAddress.empty())
		{
			// find the value according to the map.
			auto iter = m_vecAddress.rbegin();
			std::vector<void*> vecTemp;
			for (iter ; iter != m_vecAddress.rend() ; ++iter)
			{
				if(*(T*)(*iter) == value)
				{
					vecTemp.push_back(*iter);
				}
			}	
			m_vecAddress.swap(vecTemp);
		}
		else
		{
			SearchFirst(value);
		}	
		return m_vecAddress.size();
	}

	bool SetValue(T value)
	{
		if (m_vecAddress.size()>10)
		{
			return false;
		}

		auto iter = m_vecAddress.begin();
		for (;iter != m_vecAddress.end(); ++iter)
		{
			T* pValue = (T*)*iter;
			MEMORY_BASIC_INFORMATION baseMemory;
			SIZE_T nLength = VirtualQuery(pValue, &baseMemory, sizeof(baseMemory));
			if (baseMemory.Protect == PAGE_READWRITE)
			{
				*pValue = value;
			}
		}
		return true;
	}

private:
	int SearchValue(void* pStart, long size, T value)
	{
		int nCnt = 0;
		//build the vector
		T* pEnd = (T*)((char*)pStart + size);
		T* start = (T*)pStart;

		MEMORY_BASIC_INFORMATION baseMemory;
		SIZE_T nLength = VirtualQuery(start, &baseMemory, sizeof(baseMemory));

		while (start < pEnd)
		{		

			if (*start == value)
			{
				nCnt++;
				m_vecAddress.push_back(start);
			}
			start ++;
		}
		return nCnt;
	}

	void SearchFirst(T value)
	{
		char* pAddress = (char*)0;
		while (pAddress < (char*)0x7FFFFFFF)
		{
			MEMORY_BASIC_INFORMATION baseMemory;
			SIZE_T nLength = VirtualQuery(pAddress, &baseMemory, sizeof(baseMemory));
			if ( (baseMemory.Protect & (PAGE_NOACCESS | PAGE_GUARD)) || (MEM_RESERVE == baseMemory.State && baseMemory.Protect == 0))
			{

			}
			else
			{				
				SearchValue(baseMemory.BaseAddress, baseMemory.RegionSize, value);
			}

			pAddress += baseMemory.RegionSize +1;
		}
	}




	std::vector<void*> m_vecAddress;
	
};