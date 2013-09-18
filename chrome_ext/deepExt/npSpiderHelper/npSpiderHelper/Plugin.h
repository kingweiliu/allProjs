#pragma once
#include "ScriptableObject.h"

class CPlugin
{
public:
	CPlugin(NPP pNpp);
	~CPlugin(void);
	
	NPObject* m_npObj;
	NPP m_Npp;
};

