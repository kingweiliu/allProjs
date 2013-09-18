#pragma once

#include "plugin/npapi.h"
#include "plugin/nptypes.h"
#include "plugin/npruntime.h"



class CScriptableObject : public NPObject
{
public:
	CScriptableObject(NPP);
	~CScriptableObject(void);


	static NPObject *Allocate(NPP npp, NPClass *aClass);
	static void Deallocate(NPObject *npobj);
	static void Invalidate(NPObject *npobj);
	static bool HasMethod(NPObject *npobj, NPIdentifier name);
	static bool Invoke(NPObject *npobj, NPIdentifier name, const NPVariant *args, uint32_t argCount, NPVariant *result);
	static bool InvokeDefault(NPObject *npobj, const NPVariant *args, uint32_t argCount, NPVariant *result);
	static bool HasProperty(NPObject *npobj, NPIdentifier name);
	static bool GetProperty(NPObject *npobj, NPIdentifier name, NPVariant *result);
	static bool SetProperty(NPObject *npobj, NPIdentifier name, const NPVariant *value);
	static bool RemoveProperty(NPObject *npobj, NPIdentifier name);
	static bool Enumeration(NPObject *npobj, NPIdentifier **value, uint32_t *count);
	static bool Construct(NPObject *npobj, const NPVariant *args, uint32_t argCount, NPVariant *result);

	static NPClass s_NPClass;

	NPP m_pNpp;

	NPObject * m_scriptObj;

	int m_ljwName;
};

