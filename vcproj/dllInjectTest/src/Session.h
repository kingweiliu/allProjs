#pragma once

#include <vector>
#include <map>
#include <windows.h>


extern std::map<void*, long> g_mapMemLayout;

template <class T>
class CSession
{
public:
	CSession()
	{
		if (!g_mapMemLayout.size())
		{
			char* pAddress = (char*)0;
			while (pAddress < (char*)0x7FFFFFFF)
			{
				MEMORY_BASIC_INFORMATION baseMemory;
				SIZE_T nLength = VirtualQuery(pAddress, &baseMemory, sizeof(baseMemory));
				if ((baseMemory.Protect & PAGE_NOACCESS) || (MEM_RESERVE == baseMemory.State && baseMemory.Protect == 0))
				{

				}
				else
				{
					g_mapMemLayout[baseMemory.BaseAddress] = baseMemory.RegionSize;
				}

				pAddress += baseMemory.RegionSize +1;
			}
		}		
	}
	int Search(T value){
		auto iter = g_mapMemLayout.begin();
		if(m_vecAddress.size())
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
			// initialize the map.
			for (;iter != g_mapMemLayout.end();++iter)
			{
				SearchValue((T*)iter->first, iter->second, value);
			}
		}	
		return m_vecAddress.size();
	}

private:
	int SearchValue(T* start, long size, T value)
	{
		int nCnt = 0;
		//build the vector
		T* pEnd = (T*)((char*)start + size);
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

	std::vector<void*> m_vecAddress;
	
};