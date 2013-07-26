

#define PIPENAME  L"\\\\.\\pipe\\SpyChanel"

enum MsgCommand
{
	Msg_OK=1,
	Msg_Find,
	Msg_StartSession,
	Msg_Edit,
	Msg_Exit
};

struct CMessage
{
	MsgCommand MsgCmd;
	DWORD DataLength;
	VOID* Data;
};


