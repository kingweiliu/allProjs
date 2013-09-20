// npSpiderHelper.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "plugin/npapi.h"
#include "plugin/npfunctions.h"
#include "ScriptableObject.h"
#include "Plugin.h"
#include "SqliteWraper.h"

NPNetscapeFuncs * g_browser;

NPError NPP_New(NPMIMEType pluginType, NPP instance, uint16_t mode, int16_t argc, char* argn[], char* argv[], NPSavedData* saved)
{

	MessageBox(NULL, L"NPP_New",0,0);

	instance->pdata = new CPlugin(instance);
	return NPERR_NO_ERROR;
}

NPError NPP_Destroy(NPP instance, NPSavedData** save)
{
    //MessageBox(NULL, L"NPP_Destroy",0,0);
    return NPERR_NO_ERROR;
}

NPError NPP_SetWindow(NPP instance, NPWindow* window)
{
    //MessageBox(NULL, L"NPP_SetWindow",0,0);
    return NPERR_NO_ERROR;
}
NPError NPP_NewStream (NPP instance, NPMIMEType type, NPStream* stream, NPBool seekable, uint16_t* stype)
{
    //MessageBox(NULL, L"NPP_NewStream",0,0);
    return NPERR_NO_ERROR;
}
NPError NPP_DestroyStream(NPP instance, NPStream* stream, NPReason reason)
{
    //MessageBox(NULL, L"NPP_DestroyStream",0,0);
    return NPERR_NO_ERROR;
}
int32_t NPP_WriteReady(NPP instance, NPStream* stream)
{
    //MessageBox(NULL, L"NPP_WriteReady",0,0);
    return 0;
}
int32_t NPP_Write(NPP instance, NPStream* stream, int32_t offset, int32_t len, void* buffer)
{
    //MessageBox(NULL, L"NPP_Write",0,0);
    return 0;

}
void NPP_StreamAsFile(NPP instance, NPStream* stream, const char* fname)
{
    //MessageBox(NULL, L"NPP_StreamAsFile",0,0);
}
void NPP_Print(NPP instance, NPPrint* platformPrint)
{
    //MessageBox(NULL, L"NPP_Print",0,0);
}
int16_t NPP_HandleEvent(NPP instance, void* event)
{
    //MessageBox(NULL, L"NPP_HandleEvent",0,0);
    return 0;
}
void NPP_URLNotify(NPP instance, const char* url, NPReason reason, void* notifyData)
{
    //MessageBox(NULL, L"NPP_URLNotify",0,0);
}
/* Any NPObjects returned to the browser via NPP_GetValue should be retained
   by the plugin on the way out. The browser is responsible for releasing. */
NPError NPP_GetValue(NPP instance, NPPVariable variable, void *ret_value)
{
    //MessageBox(NULL, L"NPP_GetValue",0,0);
	if(NPPVpluginScriptableNPObject == variable){
		CPlugin* plugin =(CPlugin*)instance->pdata ;
		if(!plugin->m_npObj)
			plugin->m_npObj = g_browser->createobject(instance, &CScriptableObject::s_NPClass);
		NPObject** np = (NPObject**)ret_value;
		*np = plugin->m_npObj;
		g_browser->retainobject(plugin->m_npObj);
	}
    return NPERR_NO_ERROR;
}
NPError NPP_SetValue(NPP instance, NPNVariable variable, void *value)
{
    //MessageBox(NULL, L"NPP_SetValue",0,0);
    return NPERR_NO_ERROR;
}
NPBool NPP_GotFocus(NPP instance, NPFocusDirection direction)
{
    //MessageBox(NULL, L"NPP_GotFocus",0,0);
    return true;
}
void NPP_LostFocus(NPP instance)
{
    //MessageBox(NULL, L"NPP_LostFocus",0,0);
}
void NPP_URLRedirectNotify(NPP instance, const char* url, int32_t status, void* notifyData)
{
    //MessageBox(NULL, L"NPP_URLRedirectNotify",0,0);
    
}
NPError NPP_ClearSiteData(const char* site, uint64_t flags, uint64_t maxAge)
{
    //MessageBox(NULL, L"NPP_ClearSiteData",0,0);
    return NPERR_NO_ERROR;
}
char** NPP_GetSitesWithData(void)
{
    //MessageBox(NULL, L"NPP_GetSitesWithData",0,0);
    return NULL;

}
void NPP_DidComposite(NPP instance)
{
    //MessageBox(NULL, L"NP_GetEntryPoints",0,0);
}

NPError WINAPI NP_GetEntryPoints(NPPluginFuncs* pFuncs)
{
	//MessageBox(NULL, L"NP_GetEntryPoints",(LPCWSTR)pFuncs,0);
	if (!pFuncs)
	{
		return NPERR_INVALID_PLUGIN_ERROR;
	}

	pFuncs->newp = &NPP_New;
	pFuncs->destroy = &NPP_Destroy;
	pFuncs->asfile = &NPP_StreamAsFile;
	pFuncs->clearsitedata = &NPP_ClearSiteData;
	pFuncs->destroystream = &NPP_DestroyStream;
	pFuncs->didComposite = &NPP_DidComposite;
	pFuncs->event = &NPP_HandleEvent;
	pFuncs->getsiteswithdata = &NPP_GetSitesWithData;
	pFuncs->getvalue  = &NPP_GetValue;
	pFuncs->gotfocus = &NPP_GotFocus;
	pFuncs->javaClass = NULL;
	pFuncs->lostfocus = &NPP_LostFocus;
	pFuncs->newstream = &NPP_NewStream;
	pFuncs->print = &NPP_Print;
	pFuncs->setvalue = &NPP_SetValue;
	pFuncs->setwindow = &NPP_SetWindow;
	pFuncs->size = sizeof(NPPluginFuncs);
	pFuncs->urlnotify = &NPP_URLNotify;
	pFuncs->urlredirectnotify = &NPP_URLRedirectNotify;
	pFuncs->version = (NP_VERSION_MAJOR << 8) | NP_VERSION_MINOR;;
	pFuncs->write = &NPP_Write;
	pFuncs->writeready = &NPP_WriteReady;

	//MessageBox(NULL, L"NP_GetEntryPoints",0,0);
	return NPERR_NO_ERROR;
}

NPError WINAPI NP_Initialize(NPNetscapeFuncs* pFuncs)
{
	//MessageBox(NULL, L"NP_Initialize",0,0);
	g_browser = pFuncs;    
	return NPERR_NO_ERROR;
}

NPError WINAPI NP_Shutdown()
{
	//MessageBox(NULL, L"NP_Shutdown",0,0);
	return NPERR_NO_ERROR;
}

const char* NP_GetMIMEDescription(void)
{
	return "application/x-spiderhelper::test";
}