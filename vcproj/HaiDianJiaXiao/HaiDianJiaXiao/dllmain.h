// dllmain.h : Declaration of module class.

class CHaiDianJiaXiaoModule : public ATL::CAtlDllModuleT< CHaiDianJiaXiaoModule >
{
public :
	DECLARE_LIBID(LIBID_HaiDianJiaXiaoLib)
	DECLARE_REGISTRY_APPID_RESOURCEID(IDR_HAIDIANJIAXIAO, "{747B046A-E205-41DC-9CBA-11D9150A7927}")
};

extern class CHaiDianJiaXiaoModule _AtlModule;
