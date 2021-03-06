#pragma once

#include "plugin/npapi.h"
#include "plugin/nptypes.h"
#include "plugin/npruntime.h"
#include "SqliteWraper.h"
#include <map>


class CScriptableObject : public NPObject
{
public:
	CScriptableObject(NPP);
	~CScriptableObject(void);
	typedef bool (CScriptableObject::*pFuncCmd)(NPObject *npobj, NPIdentifier name, const NPVariant *args, uint32_t argCount, NPVariant *result);

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

	CSqliteWraper m_sql;


	int m_ljwName;

private:

	bool newChap(NPObject *npobj, NPIdentifier name, const NPVariant *args, uint32_t argCount, NPVariant *result);
	bool newSecion(NPObject *npobj, NPIdentifier name, const NPVariant *args, uint32_t argCount, NPVariant *result);

	std::map<std::string, pFuncCmd> m_mapCmds;

};

