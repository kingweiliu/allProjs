#include "StdAfx.h"
#include "ScriptableObject.h"
#include "npfunctions.h"

extern NPNetscapeFuncs* g_browser;

NPClass CScriptableObject::s_NPClass = {
	NP_CLASS_STRUCT_VERSION,
	&Allocate,
	&Deallocate,
	&Invalidate,
	&HasMethod,
	&Invoke,
	&InvokeDefault,
	&HasProperty,
	&GetProperty,
	&SetProperty, 
	&RemoveProperty,
	&Enumeration,
	&Construct	
};

CScriptableObject::CScriptableObject(NPP npp):m_pNpp(npp)
{
	m_mapCmds["newChap"] = &CScriptableObject::newChap;
	m_mapCmds["newSection"] = &CScriptableObject::newSecion;
}


CScriptableObject::~CScriptableObject(void)
{
}

NPObject* CScriptableObject::Allocate(NPP npp, NPClass *aClass)
{
	return new CScriptableObject(npp);
}
void CScriptableObject::Deallocate(NPObject *npobj){
}
void CScriptableObject::Invalidate(NPObject *npobj){

}
bool CScriptableObject::HasMethod(NPObject *npobj, NPIdentifier name){	
	NPUTF8 * strPropName = g_browser->utf8fromidentifier(name);
	CScriptableObject* scriptObj = (CScriptableObject*)npobj;
	if(scriptObj->m_mapCmds.find(std::string(strPropName)) != scriptObj->m_mapCmds.end())
		return true;
	return false;

}




bool CScriptableObject::Invoke(NPObject *npobj, NPIdentifier name, const NPVariant *args, uint32_t argCount, NPVariant *result){
	NPUTF8 * strPropName = g_browser->utf8fromidentifier(name);

	CScriptableObject* scriptObj = (CScriptableObject*)npobj;
	std::map<std::string, pFuncCmd>::iterator iter =  scriptObj->m_mapCmds.find(strPropName);
	if (iter!=scriptObj->m_mapCmds.end())
	{
		return (scriptObj->*(iter->second))(npobj, name, args, argCount, result);
	}

	//if (!strcmp(strPropName, "showName"))
	//{
	//	if (argCount==3)
	//	{
	//		
	//		//DWORD dwCnt = MultiByteToWideChar(CP_UTF8, NULL, args->value.stringValue.UTF8Characters, args->value.stringValue.UTF8Length, NULL, 0);
	//		//TCHAR* szContent = new TCHAR[dwCnt+1];
	//		//MultiByteToWideChar(CP_UTF8, NULL, args->value.stringValue.UTF8Characters, args->value.stringValue.UTF8Length, szContent, dwCnt+1);
	//		//szContent[dwCnt] = 0;
	//		//MessageBox(NULL, szContent, NULL, MB_OK);
	//		//delete szContent;
	//	}
	//}
	//

	//if (!strcmp(strPropName, "Add"))
	//{
	//	if (argCount == 2)
	//	{
	//		if (args[0].type != NPVariantType_Double || args[1].type != NPVariantType_Double)
	//		{
	//			return false;
	//		}

	//		result->type = NPVariantType_Double;
	//		result->value.doubleValue = args[0].value.doubleValue + args[1].value.doubleValue;
	//		return true;
	//	}

	//}
	return false;
}
bool CScriptableObject::InvokeDefault(NPObject *npobj, const NPVariant *args, uint32_t argCount, NPVariant *result){

	return true;
}
bool CScriptableObject::HasProperty(NPObject *npobj, NPIdentifier name){
	NPUTF8 * strPropName = g_browser->utf8fromidentifier(name);
	if (!strcmp(strPropName, "LjwName"))
		return true;
	return false;
}
bool CScriptableObject::GetProperty(NPObject *npobj, NPIdentifier name, NPVariant *result){
	CScriptableObject* scriptObj =(CScriptableObject*)npobj;	
	NPUTF8 * strPropName = g_browser->utf8fromidentifier(name);
	if (!strcmp(strPropName, "LjwName"))
	{
		result->type = NPVariantType_Double;
		result->value.doubleValue = scriptObj->m_ljwName;
		return true;
	}
	
	return false;
}
bool CScriptableObject::SetProperty(NPObject *npobj, NPIdentifier name, const NPVariant *value){
	CScriptableObject* scriptObj =(CScriptableObject*)npobj;
	NPUTF8 * strPropName = g_browser->utf8fromidentifier(name);
	if (!strcmp(strPropName, "LjwName"))
	{
		if (value->type == NPVariantType_Double)
		{
			scriptObj->m_ljwName = value->value.doubleValue;
		}
		return true;
	}
	return false;
}
bool CScriptableObject::RemoveProperty(NPObject *npobj, NPIdentifier name){
	return true;
}
bool CScriptableObject::Enumeration(NPObject *npobj, NPIdentifier **value, uint32_t *count){
	return true;
}
bool CScriptableObject::Construct(NPObject *npobj, const NPVariant *args, uint32_t argCount, NPVariant *result){
	return true;
}
//-------------------------------------------------------------------
bool CScriptableObject::newChap(NPObject *npobj, NPIdentifier name, const NPVariant *args, uint32_t argCount, NPVariant *result)
{
	m_sql.AddChapter(args, args+1, args+2, args+3);
	return true;
}
bool CScriptableObject::newSecion(NPObject *npobj, NPIdentifier name, const NPVariant *args, uint32_t argCount, NPVariant *result)
{
	if (argCount != 2)
	{
		return false;
	}
	m_sql.AddVolume(args, args+1);
	return true;
}
